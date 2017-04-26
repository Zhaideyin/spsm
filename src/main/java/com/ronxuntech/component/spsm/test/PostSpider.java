package com.ronxuntech.component.spsm.test;

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
import com.ronxuntech.util.PageData;
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
public class PostSpider implements PageProcessor {
	// 静态工厂方法
		public static PostSpider getInstance() {
			return new PostSpider();
		}
		
		public PostSpider(){
			
		}
		
		public PostSpider(WebInfo web, String seedUrlId){
			this.web=web;
			this.seedUrlId=seedUrlId;
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

		private WebInfoManager webinfoService;
	public void process(Page page) {
		Html html = page.getHtml();
		String pageUrl = page.getUrl().toString().trim();
		String contents = html.xpath("//*[@class='conzw']/html()").all().toString().replaceAll(regex, "");
		System.out.println("html:"+html);
		/*List<String> links = html.links().regex("(http://www.seedchina.com.cn/DefaultInfoDetail.aspx\\?InfoId=[0-9]*&TypeId=[0-9]*)").all();

		System.out.println("contents:"+contents);
		System.out.println("links"+links.toString());
		page.addTargetRequests(links);*/
		String __EVENTTARGET ="GridView1$ctl31$LinkButtonNextPage";
		String __VIEWSTATE =html.xpath("//*[@id='__VIEWSTATE']/@value").toString();
		String __EVENTVALIDATION =html.xpath("//*[@id='__EVENTVALIDATION']/@value").toString();

			//给参数设置
		if(__EVENTVALIDATION!=null && __EVENTVALIDATION !="") {
//			for(;i<web.getTotalPage();i++){
			//框架是根据url 去重的。
			Request request = new Request( "http://www.seedchina.com.cn/defaultInfoList.aspx?Id=5&isSubType=yes"+ "&uuid=" + UuidUtil.get32UUID());
			NameValuePair[] nameValuePair = {
					new BasicNameValuePair("__EVENTTARGET", __EVENTTARGET),
					new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE),
					new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION)
			};
			//发送post请求
			request.putExtra("nameValuePair", nameValuePair);
			request.setMethod(HttpConstant.Method.POST);
			page.addTargetRequest(request);
		}
	}

	public Site getSite() {
		return site;
	}

	public static void main(String [] args) {
		Spider.create(new PostSpider()).addUrl("http://www.seedchina.com.cn/defaultInfoList.aspx?Id=5&isSubType=yes")
				.thread(2).setDownloader(new HttpClientDownloader())
				.run();
	}

}
