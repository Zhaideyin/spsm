package com.ronxuntech.component.spsm.util.test;

import java.text.SimpleDateFormat;
/**
 * AnnexSpider 附件爬取类
 *	by xieyun 
 * 
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.ls.LSInput;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.ConvertUtil;
import com.ronxuntech.component.spsm.util.FileNameUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.UrlUtils;

public class AnnexSpider implements PageProcessor {

	private WebInfo web;
	private Site site;
	private String pageUrl; // 附件地址
	private AnnexUrlManager annexurlService;
	private SeedUrlManager seedurlService;
	private AnnexUtil annexUtil = AnnexUtil.getInstance();

	private FileNameUtil fileNameUtil = FileNameUtil.getInstance();
	private ConvertUtil convertUtil =ConvertUtil.getInstance();
	// 区别是图片还是文档，
	private String  temp;
	//构造函数
	public AnnexSpider(String pageUrl, String startUrl, WebInfo web,String  temp) {
		annexurlService = (AnnexUrlManager) SpringBeanFactoryUtils.getBean("annexurlService");
		seedurlService = (SeedUrlManager) SpringBeanFactoryUtils.getBean("seedurlService");
		this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl)).setRetryTimes(3);
		this.pageUrl = pageUrl;
		this.web = web;
		this.temp=temp;
	}

	//抓取附件
	public void process(Page page) {
		// 在符合urlPatten这样的页面过滤符合以下条件的链接 "http://www.natesc.org.cn"+
		String docRegex = web.getDocRegex();
		String imgRegex = web.getImgRegex();
		// 将所有满足urlPatten的链接全部放入 requests中。
		Html obj = page.getHtml();
		List<String> requests = new ArrayList<>();
		requests.add(pageUrl);
		// 将满足条件的链接加入待爬取的队列中。
		page.addTargetRequests(requests);
		String imgHostFileName = obj.xpath("//title/text()").toString().replaceAll(" - 中国种子咨询网", "");
		List<String> docListProcess = new ArrayList<>();
		List<String> imgListProcess = new ArrayList<>();
		List<String> listProcess = new ArrayList<>();
		// 判断图片和文档是否存在
		if(temp.equals("img")){
			if (StringUtils.isNotEmpty(imgRegex)) {
				imgListProcess = obj.xpath(web.getImgTag()).regex(imgRegex).all();
			}
		}
		if(temp.equals("doc")){
			if (StringUtils.isNotEmpty(docRegex)) {
				docListProcess = obj.xpath("//a/@href").regex(docRegex).all(); // 此时抓取出来的是html中的链接，
			} 
		}
		
		// 判断网页抓取的图片或者文档是否是相对地址，如果是相对地址，那么加上协议 主机 项目名。。
		// String url = list.get(i).replace("amp;", "");
		// templist 是为了得到完整的链接。
		List<String> templist = new ArrayList<>();
		
		//得到完整的下载附件的地址
		for (int i = 0; i < docListProcess.size(); i++) {
			// 如果不是绝对路径，则通过抓取到的链接，和当前页面的 ｕｒｌ来拼接下载地址。
			if (!(docListProcess.get(i).contains("http"))) {
				templist.add(fileNameUtil.getTargetUrl(docListProcess.get(i), pageUrl).replace("amp;", "").trim());
			} else {
				templist.add(docListProcess.get(i).replace("amp;", "").trim());
			}
		}
		
		for (int j = 0; j < imgListProcess.size(); j++) {
			// 如果不是绝对路径，则通过抓取到的链接，和当前页面的 ｕｒｌ来拼接下载地址。
			if (!(imgListProcess.get(j).contains("http"))) {
				templist.add(fileNameUtil.getTargetUrl(imgListProcess.get(j), pageUrl).replace("amp;", "").trim());
			} else {
				templist.add(imgListProcess.get(j).replace("amp;", "").trim());
			}
		}
		// 返回统一的listprocess
		if (templist.size() != 0) {
			listProcess = null;
			listProcess = templist;
			PageData p = new PageData();
			List<PageData> annexUrlList = saveAnnexUrl(templist);
			p.put("annexUrlList", annexUrlList);
			try {
				// 存入数据库
				annexurlService.saveAll(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 此处将标题一并抓取，之后提取出来作为文件名
			listProcess.add(0, imgHostFileName);
			page.putField("img", listProcess);
		}

	}

	public Site getSite() {
		return site;
	}

	/**
	 * 将得到的annexUrllist封装到map里,并将该链接存在的种子地址的id添加到pd, status设置为0，
	 * 
	 * @param templist
	 * @return
	 */
	public List<PageData> saveAnnexUrl(List<String> templist) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//将抓到链接的去重
		Set<String> setTmep =  convertUtil.listToSet(templist);
		List<String> listTemp= convertUtil.setToList(setTmep);
		List<PageData> annexUrlList = new ArrayList<>();
		String seedUrlId = "";
		PageData p = annexUtil.findBySeedurl(web.getSeed(), seedurlService);
		seedUrlId = p.getString("SEEDURL_ID");
		for (int i = 0; i < listTemp.size(); i++) {
			PageData pd1 = new PageData();
			pd1.put("ANNEXURL_ID", fileNameUtil.getFileName());
			pd1.put("ANNEXURL", listTemp.get(i));
			pd1.put("STATUS", "0");
			pd1.put("SEEDURLID", seedUrlId);
			pd1.put("TARGETURLID" ,pageUrl);
			pd1.put("CREATETIME",sdf.format(new Date()));
			annexUrlList.add(pd1);
		}
		return annexUrlList;
	}
}