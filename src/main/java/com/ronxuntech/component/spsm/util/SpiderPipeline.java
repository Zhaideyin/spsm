package com.ronxuntech.component.spsm.util;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class SpiderPipeline implements Pipeline{

//	private AnnexUrlManager annexurlService;
	private TargetUrlManager targeturlService;
//	private SeedUrlManager seedurlService;
	private AnnexUtil annexUtil=AnnexUtil.getInstance();
	private WebInfo web;
	private SpiderManager spiderService;
	
	
	//构造函数
	public  SpiderPipeline() {
	
	}
	public SpiderPipeline(WebInfo web){
		this.web=web;
		spiderService =(SpiderManager) SpringBeanFactoryUtils.getBean("spiderService");		
//		annexurlService = (AnnexUrlManager) SpringBeanFactoryUtils.getBean("annexurlService");
		targeturlService = (TargetUrlManager) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
	}
	// 先得到抓取的东西， 然后再通过处理，存入数据库
	@Override
	public void process(ResultItems resultItems, Task task) {
		String content =resultItems.get("content").toString();
		//处理title ，有的网站是不能分开抓title 和 content 所以可能为null
		String title="";
		if(resultItems.get("title")!="" && resultItems.get("title")!=null){
		  title=resultItems.get("title").toString();
		}
				
		String pageUrl=resultItems.get("pageUrl").toString();
//		if(web.getPageMethod().equals("get")){
			annexUtil.insertintoDatabase(content, title, web, pageUrl, spiderService, targeturlService);
//		}
		
	}

}
