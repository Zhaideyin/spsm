package com.ronxuntech.component.spsm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.NextPageUrlUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
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
public class AgridataSpider implements PageProcessor {

	private WebInfo web;
	private SpiderManager spiderService;
	private TargetUrlManager targeturlService;
//	private SeedUrlManager seedurlService;
	private AnnexUrlManager annexurlService;
	// 保存seedURl的种子id
	private String seedUrlId = "";
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	private NextPageUrlUtil nextPageUrlUtil = NextPageUrlUtil.getInstance();
	//作物类别
	private String cropType = "";
	//作物名
	private String cropName ="";
	//作物品种名
	private String breedName =""; 
/*	//油料作物
	private String[] oilCrop = new String[]{"油料作物","油菜","花生","芝麻","蓖麻","红花","大豆"};
	//棉麻作物
	private String [] diablementFort = new String[]{"蚕桑麻(含棉花)","棉花","红麻","苎麻","亚麻","黄麻"};
	//薯类作物
	private String[] potatoCrop = new String[]{"薯类(甘薯、马铃薯)","马铃薯","甘薯"};
	//蔬菜作物
	private String[] vegetableCrop = new String[]{"蔬菜","大白菜","萝卜","辣椒","菜豆","豇豆","茭白","莲藕"};
	//果树
	private String[] fruitTree = new String[]{"果树","苹果","梨","葡萄","油桃","蟠桃","柑橘","李子","杏"};
	//其他
	private String[] otherCrop = new String[]{"其他","烟草","甘蔗","甜菜","桑树","橡胶","黍稷","绿豆","蚕豆"}; 
	//玉米(含高粱)
	private String[] cornCrop = new String[]{"玉米(含高粱)","玉米","高粱"};
	//麦类(小麦、青稞、荞麦)
	private String[] wheatCrop =new String[]{"麦类","小麦","大麦","荞麦"};*/
	
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
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
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

	@Override
	public void process(Page page) {
		
		Html html = page.getHtml();
//		System.out.println("html:"+html);
		//得到所有作物的链接地址，
		List<String> listCropUrl = html.xpath("/html/body/table[2]/tbody/tr/td/table/tbody").links().all(); 
		if(listCropUrl.size()>0){
//			System.out.println("url:"+listCropUrl.get(0).toString());
			//将抓到的链接存入数据库
//			annexUtil.targeturlInsertDatabase(listCropUrl, seedUrlId, targeturlService);
			page.addTargetRequests(listCropUrl);
		}
		String imgUrl = html.xpath("//img/@src").regex("[\u4e00-\u9fa5]+.*\\.\\w+").toString();
		System.out.println("imgURL:"+imgUrl);
		//得到作物的名称和作物品种
		Matcher matcher = pattern.matcher(page.getUrl().toString());
		
		if(matcher.find()){
			String temp =matcher.group().replaceAll(".htm", "");
			Matcher m_cropName = cropNamePattern.matcher(temp);
			if(m_cropName.find()){
				System.out.println("m_cropName:"+m_cropName);
				cropName = m_cropName.group().substring(1, m_cropName.group().length());
				System.out.println("cropName:"+cropName);
				breedName  = temp.substring(m_cropName.group().length()+1, temp.length());
				try {
					//解码特殊符号。
					breedName = URLDecoder.decode(breedName, "gb2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println("breedname:"+breedName);
			}
			
		}
		//通过作物来查询所属作物类别。如， 大豆  属于 油料作物
		
		
		/*//得到品种名、
		Matcher m_varietalName = pattern.matcher(page.getUrl().toString());
		if(m_varietalName.find()){
			breedName = m_varietalName.group().substring(1, m_varietalName.group().length()-1);
			System.out.println("breedName:"+breedName);
		}*/
		
		//得到想要的内容。
		if(!(page.getUrl().toString().toLowerCase().contains("cgris"))){
			String contents = html.xpath("/html/body/table/html()").toString();
//			System.out.println("content:"+contents);
			String title = breedName;
			String pageUrl = page.getUrl().toString();
			//放入map 中，在pipeline中取。
			page.putField("content", contents.trim());
			page.putField("title", title);
			page.putField("pageUrl", pageUrl);
			//contents 不为空才执行。
			/*if(StringUtils.isNotEmpty(contents)){
				annexUtil.annexSaveAndDown(page, contents, title,cropName, varietalName,web, annexurlService, pageUrl, spiderService,
						targeturlService);
			}*/
		}else{
			//修改不是目标地址的状态
//			annexUtil.updateTargetStatus(page.getUrl().toString(), targeturlService);
		}
		
		// 当取得的内容为空时，设置skip之后，这个页面的结果不会被Pipeline处理
		if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
			page.setSkip(true);
		}
		
	}

	public static void main(String[] args) {
		Spider.create(new AgridataSpider()).addUrl("http://crop.agridata.cn/96-014/default.html").thread(1).run();
	}
}
