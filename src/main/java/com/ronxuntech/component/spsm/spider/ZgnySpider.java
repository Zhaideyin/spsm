package com.ronxuntech.component.spsm.spider;

import java.util.ArrayList;
import java.util.List;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class ZgnySpider implements PageProcessor{
	
	private WebInfo web;
	private SpiderManager spiderService;
	private TargetUrlManager targeturlService;
//	private SeedUrlManager seedurlService;
	private AnnexUrlManager annexurlService;
	// 保存seedURl的种子id
	private String seedUrlId = "";
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	private int pageFlag=0;
	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000)
			;
	
	
	public  ZgnySpider(WebInfo web ,String seedUrlId) {
		this.web =web;
		this.seedUrlId=seedUrlId;
	}
	
	public ZgnySpider() {
	}

	@Override
	public Site getSite() {
		return null;
	}

	@Override
	public void process(Page page) {
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		Html html=page.getHtml();
		List<String> links =html.xpath("//*[@class='global_tx_list4']").links()
				.regex(web.getUrlRex()).all();
		String title=html.xpath(web.getList().get(0)).toString();
		String contents =html.xpath(web.getList().get(1)).all().toString();
		String pageUrl = page.getUrl().toString();
		page.putField("pageUrl",pageUrl);
		page.putField("content", contents);
		page.putField("title", title);
		page.addTargetRequests(links);
		String pageNumInContent=html.xpath(web.getTotalPage()).toString();
		int totalPageNum=annexUtil.getTotalPageNum(pageNumInContent, web.getSeed());
		
		if(pageFlag==0){
			jointNextpage(page, web.getSeed(), totalPageNum);
			pageFlag++;
		}
		
		
		// 循环将list中的链接取出， 并存入targetUrl 的pd中。
			try {
				annexUtil.targeturlInsertDatabase(links, seedUrlId, targeturlService);
			} catch (Exception e) {
			
			}
		//信息保存 和附件下载	
		annexUtil.annexSaveAndDown(page, contents, title, web, annexurlService, pageUrl, spiderService,
				targeturlService);
		
		if (page.getResultItems().get("content") == null || contents.equals("")) {
			// 设置skip之后，这个页面的结果不会被Pipeline处理
			page.setSkip(true);
		}
		
	}

	private void jointNextpage(Page page, String str, int totalPageNum) {
//		http://gp.zgny.com.cn/Techs/Page_1_NodeId_Gp_js_zmjs.shtml
		String seed = str;
		List<String> urlList =new  ArrayList<>();
		String [] temp= seed.split("\\d+");
		
		String prf = temp[0];
		String suf = temp[1];
		for (int i = 1; i < totalPageNum; i++) {
			urlList.add(prf+i+suf);
		}
		page.addTargetRequests(urlList);
	}

}
