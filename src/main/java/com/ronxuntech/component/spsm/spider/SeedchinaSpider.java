package com.ronxuntech.component.spsm.spider;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.component.spsm.util.ImgOrDocPipeline;
import com.ronxuntech.component.spsm.util.SpiderPipeline;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.seedurl.impl.SeedUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.service.spsm.webinfo.WebInfoManager;
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import com.ronxuntech.util.UuidUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于 网站：http://www.seedchina.com.cn/defaultInfoList.aspx?Id=5&isSubType=yes
 * 用于发送post 请求来得到想要的数据，然后抓取
 * @author angrl
 *
 */
public class SeedchinaSpider implements PageProcessor {
	// 静态工厂方法
		public static SeedchinaSpider getInstance() {
			return new SeedchinaSpider();
		}
		
		public SeedchinaSpider(){
			
		}
		
		public SeedchinaSpider(WebInfo web, String seedUrlId){
			this.web=web;
			this.seedUrlId=seedUrlId;
			spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
			targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
			seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
			annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		}
		// 重新下载目标网页地址的集合
//		private List<String> retargetUrlList;
		// 创建service
		private SpiderManager spiderService;
		private TargetUrlManager targeturlService;
		private SeedUrlManager seedurlService;
		private AnnexUrlManager	annexurlService;
		// 用于存放第一次页面抓取到的list，用于去重
//		List<String> tempList = new ArrayList<>();
		// 保存seedURl的种子id
		private String seedUrlId = "";
		private WebInfo web;
		//用于控制循环
		private int i=0;
		private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(200000).setCycleRetryTimes(3);
		// 附件工具
		private AnnexUtil annexUtil = AnnexUtil.getInstance();
		//判断页面是否存在
		private String error="404";

		private String regex = "[\\[|\\]|,]";

	public void process(Page page) {

		Html html = page.getHtml();
		String pageUrl = page.getUrl().toString().trim();
		String contents = html.xpath(web.getList().get(1)).all().toString().replaceAll(regex, "");

		//标题
		String title="";
		if(web.getList().get(0)!="" && web.getList().get(0) !=null){
		  title = html.xpath(web.getList().get(0)).toString();
		}
		page.putField("content", contents.trim());
		page.putField("title", title);
		page.putField("pageUrl", pageUrl);
		
		//过滤链接。并且将链接存入数据库
		List<String> requests=html.links().regex(web.getUrlRex()).all();
		System.out.println("rest:"+requests.toString());
		annexUtil.targeturlInsertDatabase(requests, seedUrlId, targeturlService);
		page.addTargetRequests(requests);
		
		// 保存和下载附件
//		annexUtil.annexSaveAndDown（);
		//抓取参数的值
		annexUtil.insertIntoPage(page, web, annexUtil, annexurlService, targeturlService);
		String __EVENTTARGET ="GridView1$ctl31$LinkButtonNextPage";
		String __VIEWSTATE =html.xpath("//*[@id='__VIEWSTATE']/@value").toString();
		String __EVENTVALIDATION =html.xpath("//*[@id='__EVENTVALIDATION']/@value").toString();

			//给参数设置
		if(__EVENTVALIDATION!=null && __EVENTVALIDATION !=""){
//			for(;i<web.getTotalPage();i++){
				//框架是根据url 去重的。
				Request request = new Request(web.getSeed()+"&uuid="+UuidUtil.get32UUID());
				 NameValuePair[] nameValuePair ={
					new BasicNameValuePair("__EVENTTARGET",__EVENTTARGET),
					new BasicNameValuePair("__VIEWSTATE",__VIEWSTATE),
					new BasicNameValuePair("__EVENTVALIDATION",__EVENTVALIDATION)
			};
				//发送post请求 
				 request.putExtra("nameValuePair", nameValuePair);
				 request.setMethod(HttpConstant.Method.POST);
				 page.addTargetRequest(request);
			}
		if (page.getResultItems().get("content")==null || page.getResultItems().get("content").equals("")){
	        //设置skip之后，这个页面的结果不会被Pipeline处理
	        page.setSkip(true);
	    }
	}

	public Site getSite() {
		return site;
	}
	

	/**
	 * 开始爬取
	 * 
	 * @param web
	 */
	public void start(WebInfo web) {
		this.web = web;
		System.out.println(web.toString());
		site.setCharset(web.getPageEncoding());
		try {
			seedUrlId = annexUtil.getSeedUrlId(web);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//存储urls文件的路径
		 String filepath =PathUtil.getClasspath()+"\\urls";
		//
		 Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		// 如果网页有图片或者文档， 开启的线程不能太多， 因为下载文档或者图片的时候也会开启3个线程，
		// 如果开十个爬取网页，那么有图或者文档的时候，则会开启30个。这样服务器估计受不了，所以我这里选择了4个
		// 网站/标题作为文件夹 清除特殊字符
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		String fileDir =PathUtil.getClasspath()+ "uploadFiles/spsm/" + webUrl + "/";
		
		if (web.isHasDoc() || web.isHasImg()) {
			Spider.create(new SeedchinaSpider(web,seedUrlId)).addUrl(web.getSeed())
			.thread(3).setDownloader(new HttpClientDownloader())
			.addPipeline(new SpiderPipeline(web))
			.addPipeline(new ImgOrDocPipeline(fileDir))
			.setScheduler(scheduler)
			.run();
		} else {
			Spider.create(new SeedchinaSpider(web,seedUrlId)).addUrl(web.getSeed())
			.thread(5).setDownloader(new HttpClientDownloader())
			.addPipeline(new SpiderPipeline(web))
			.setScheduler(scheduler)
			.run();
		}
	}
}