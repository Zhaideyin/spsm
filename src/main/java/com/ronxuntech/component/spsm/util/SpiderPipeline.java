package com.ronxuntech.component.spsm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.spsm.FinancePageModel;
import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import com.ronxuntech.util.UuidUtil;

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
		
		/*if(web.getSeed().contains("http://finance.aweb.com.cn")){
			Map<String,Object> map=resultItems.getAll();
			for(String key:map.keySet()){
				//得到类。通过方法去取content title
				FinancePageModel finance= (FinancePageModel) map.get(key);
	        	
				PageData pd = new PageData();
	            pd.put("SPIDER_ID", UuidUtil.get32UUID());
//	            System.out.println("spider contents:"+getContent(finance.getContent()));
	            System.out.println("Spiderpipeline contents :"+finance.getContent());
	    		pd.put("CONTENT",finance.getContent());
	    		pd.put("TITLE", finance.getTitle());
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		pd.put("CREATE_TIME",sdf.format(new Date()));
	    		pd.put("TARGETURLID", resultItems.getRequest().getUrl().toString());
	    		pd.put("DATABASETYPE", web.getDatabaseType());
	    		pd.put("NAVBARTYPE", web.getNavbarType());
	    		pd.put("LISTTYPE", web.getListType());
	    		pd.put("SUBLISTTYPE", web.getSublistType());
	    		
	    		try {
					spiderService.save(pd);
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
	        }
		}else{*/
			try {
				String pageUrl=resultItems.get("pageUrl").toString();
				PageData pd= resultItems.get("pd");
				// 如果怕去的 内容是空，则不跳出。不存入数据库。
				if (!("".equals(resultItems.get("content").toString().trim()))) {
					// 将爬取的数据放入数据库
					spiderService.save(pd);
					// 当钱页面不是种子页面，通过查询，然后修改该url的状态。
					if (!(pageUrl.equals(web.getSeed()))) {
						annexUtil.updateTargetStatus(pageUrl, targeturlService);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
