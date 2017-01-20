package com.ronxuntech.component.spsm;

import java.util.ArrayList;
import java.util.List;

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

public class AgrioneSpider implements PageProcessor{
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
			.addHeader("Upgrade-Insecure-Requests", "1")
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
			.addCookie("PHPSESSID", "fg45g4esaboeh13i3cqhhvbet0")
			.addCookie("Hm_lvt_9d8f86a65bc11382cd39fbe78884c454", "1483948469,1484012200")
			.addCookie("Hm_lpvt_9d8f86a65bc11382cd39fbe78884c454", "1484017938")
			;
	
	private String regex = "[\\[|\\]|,]";
	public  AgrioneSpider(WebInfo web ,String seedUrlId) {
		this.web =web;
		this.seedUrlId=seedUrlId;
	}
	
	public AgrioneSpider() {
	}

	@Override
	public void process(Page page) {
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		Html html=page.getHtml();
		List<String> links =html.xpath(web.getUrlTag()).links()
				.regex(web.getUrlRex()).all();
////		String title=html.xpath(web.getList().get(0)).toString();
////		String contents =html.xpath(web.getList().get(1)).all().toString();
		String pageUrl = page.getUrl().toString();
//		page.putField("pageUrl",pageUrl);
//		page.putField("content", contents);
//		page.putField("title", title);
		page.addTargetRequests(links);
		
		String pageNumInContent=html.xpath(web.getTotalPage()).toString();
		int totalPageNum=annexUtil.getTotalPageNum(pageNumInContent, web.getSeed());
		//拼接下一页的链接并放入Targetrequests 只是执行一次。
		if(pageFlag==0){
			List<String> urlList2 = jointNextpage(page, web.getSeed(), totalPageNum);
			links.addAll(urlList2);
			pageFlag++;
		}
		
		
		// 循环将list中的链接取出， 并存入targetUrl 的pd中。
			try {
				annexUtil.targeturlInsertDatabase(links, seedUrlId, targeturlService);
			} catch (Exception e) {
			
			}
//		//信息保存 和附件下载	
//		annexUtil.annexSaveAndDown(page, contents, title, web, annexurlService, pageUrl, spiderService,
//				targeturlService);
			boolean isNextpage = annexUtil.isSave(web, page);
			// --------将需要的数据 得到后数据存入数据库（排除种子页面、分页链接）----------
			String contents = "";
			String title = "";
			if (!(pageUrl.equals(web.getSeed().trim()))) {
				if (isNextpage == false) {
					
					contents = html.xpath(web.getList().get(1)).all().toString().replaceAll(regex, "");

					if (web.getList().get(0) != "" && web.getList().get(0) != null) {
						title = html.xpath(web.getList().get(0)).toString();
					}
					page.putField("content", contents.trim());
					page.putField("title", title);
					page.putField("pageUrl", pageUrl);

					annexUtil.annexSaveAndDown(page, contents, title, web, annexurlService, pageUrl, spiderService,
							targeturlService);

				} else { // 如果该链接不是想要的，（如下一页的链接，但是又要修改他的状态）
					annexUtil.updateTargetStatus(pageUrl, targeturlService);
				}
			}
		
		if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
			// 设置skip之后，这个页面的结果不会被Pipeline处理
			page.setSkip(true);
		}
	}

	@Override
	public Site getSite() {
		site.setCharset(web.getPageEncoding());
		return site;
	}

	/**
	 * 拼接下一页的链接，并放入addTargetRequests
	 * @param page
	 * @param str
	 * @param totalpage
	 */
	public List<String> jointNextpage(Page page,  String str, int totalpage) {
		String seed = str;
		List<String> urlList =new  ArrayList<>();
		String prf = "index-";
		String suf = ".html";
		for (int i = 1; i < totalpage; i++) {
			urlList.add(seed + prf + i + suf);
		}
		page.addTargetRequests(urlList);
		return urlList;
	}

	
}
