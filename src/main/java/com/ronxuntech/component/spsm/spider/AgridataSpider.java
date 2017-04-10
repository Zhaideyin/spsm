package com.ronxuntech.component.spsm.spider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.component.spsm.util.NextPageUrlUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.crop.impl.CropService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 用于 http://crop.agridata.cn/A010105.ASP
 * 
 * @author angrl
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/spring/ApplicationContext.xml")
public class AgridataSpider implements PageProcessor {

	private WebInfo web;
	private SpiderManager spiderService;
	private TargetUrlManager targeturlService;
	private AnnexUrlManager annexurlService;
	@Autowired(required=true)
	private CropService cropService;
	private Map<String, String> cropMap=new HashMap<>();
	// 保存seedURl的种子id
	private String seedUrlId = "";
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();

	//得到链接中的中文，作物名。
	String cropNameRegex = "\\/[\u4e00-\u9fa5]+";
	Pattern cropNamePattern = Pattern.compile(cropNameRegex);
	
	//得到作物名：如水稻中的：黑珍米
	String regex ="(\\/([\u4e00-\u9fa5]+).*\\.htm)";
	Pattern pattern = Pattern.compile(regex);
	
	public AgridataSpider(WebInfo web,String seedUrlId){
		this.web =web;
		this.seedUrlId=seedUrlId;
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		cropService = (CropService) SpringBeanFactoryUtils.getBean("cropService");
		initCropAndCropTypeRelation();
	}
	private Site site = Site.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setTimeOut(200000)
			.setCharset("gb2312")
			.setCycleRetryTimes(3);
	public AgridataSpider() {
	}
	
	
	@Override
	public Site getSite() {
		return site;
	}
	
	public void setCropService(CropService cropService) {
		this.cropService = cropService;
	}

	@Override
	public void process(Page page) {
		//作物名
		 String cropName ="";
		//作物品种
		String breedName =""; 
		
		Html html = page.getHtml();
		//得到所有作物的链接地址，		
		List<String> listCropUrl = html.xpath("/html/body/table[2]").links().all(); 
		//将抓到的链接存入数据库
		annexUtil.targeturlInsertDatabase(listCropUrl, seedUrlId, targeturlService);
		page.addTargetRequests(listCropUrl);
		//得到作物的名称和作物品种
		String pageUrl=page.getUrl().toString();
		try {
			//将编码过后的url 解码。再进行正则匹配。
			pageUrl = URLDecoder.decode(pageUrl,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Matcher matcher = pattern.matcher(pageUrl);
		if(matcher.find()){
			String temp =matcher.group().replaceAll(".htm", "");
			Matcher m_cropName = cropNamePattern.matcher(temp);
			if(m_cropName.find()){
				cropName = m_cropName.group().substring(1, m_cropName.group().length());
				breedName  = temp.substring(m_cropName.group().length()+1, temp.length());
				try {
					//解码特殊符号。
					breedName = URLDecoder.decode(breedName, "gb2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	

		//得到想要的内容。
		String contents ="";
		if(!(pageUrl.toLowerCase().contains("cgris"))){
			contents= html.xpath("/html/body/table/tbody/html()").toString();
//
			System.out.println("pageURL:"+pageUrl);
			//放入map 中，在pipeline中取。
			page.putField("content", contents.trim());
			page.putField("title", breedName);
			page.putField("pageUrl", pageUrl);
			//通过作物来查询所属作物类别。如， 大豆  属于 油料作物
			
		}else{
//			修改不是目标地址的状态
			annexUtil.updateTargetStatus(page.getUrl().toString(), targeturlService);
		}
		
		// 当取得的内容为空时，设置skip之后，这个页面的结果不会被Pipeline处理
		if (page.getResultItems().get("title") == null || page.getResultItems().get("title").toString().trim().equals("")) {
			page.setSkip(true);
		}else if(StringUtils.isNotEmpty(contents)){
			String cropTypeId = cropMap.get(cropName).toString();
			annexUtil.annexSaveAndDown(page, contents, breedName,cropTypeId,cropName, breedName,web, annexurlService);
		}
		
	}

	/**
	 * 初始化 作物和作物类别对应关系，如： 大豆(key)-油料作物的id（value），大白菜-蔬菜id（value）
	 * @return cropMap
	 */
	public Map<String, String> initCropAndCropTypeRelation(){
		try {
			List<PageData> cropList =cropService.listAll(new PageData());
			for(int i= 0; i<cropList.size();i++){
				cropMap.put(cropList.get(i).getString("CROPNAME"), cropList.get(i).getString("CROPTYPEID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("cropMap:"+cropMap.toString());
		return cropMap;
	}

	/**
	 *
	 * @param pageUrl
	 * @return
	 */
	public Map<String,String> initCropMap(String pageUrl){
		Map<String,String> cropMap1 = new HashMap<>();
		String [] temp = pageUrl.split("/");
		String crop = temp[temp.length-1].replaceAll("\\.htm","");
		System.out.println("crop:"+crop);
		if(crop.contains("-")){
			String [] crops = crop.split("-");
			cropMap1.put("cropName",crops[0]);
			cropMap1.put("breedName",crops[1]);
		}
		return cropMap1;
	}
}
