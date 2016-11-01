package com.ronxuntech.component.spsm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ronxuntech.component.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by tongbu on 2016/10/8 0008.
 */
public class BaseCrawler implements PageProcessor {

	// 单例模式
	private static BaseCrawler crawler = new BaseCrawler();

	// 静态工厂方法
	public static BaseCrawler getInstance() {
		return crawler;
	}
	// 创建service
	private SpiderService spiderService;
	private WebInfo web;
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1500);
	//附件功能
	private AnnexUtil annexUtil=new AnnexUtil();

	public void process(Page page) {
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		// 获取页面信息保存在html 对象中。
		Html html = page.getHtml();
		// 将爬取的目标地址添加到队列里面 web.getUrlRex()
		page.addTargetRequests(html.links().regex(web.getUrlRex()).all());
		//下一页是个链接的。 比如： 下一页是   ....html.
		page.addTargetRequest(html.xpath(web.getPageGetTag()).toString());
		// 分页类型判断。从而取不同的方式
		PageData pd = new PageData();
		String regex="[\\[|\\]|,]";
		String content=html.xpath(web.getList().get(1).toString()).all().toString().replaceAll(regex, "");
		String title=html.xpath(web.getList().get(0).toString()).all().toString().replaceAll(regex, "");
		pd.put("SPIDER_ID", System.currentTimeMillis());
		pd.put("TITLE", title);
		pd.put("CONTENT", content);
		pd.put("TYPE", web.getTypeId());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(new Date());
		pd.put("CREATE_TIME", d);

		// 获取当前页的图片或者文档
		annexUtil.downloadAnnex(web, pd, page);
		try {
			// 如果怕去的 内容是空，则不跳出。不存入数据库。
			if (!(pd.get("CONTENT").toString().trim().equals("")) || !(pd.get("CONTENT") == null)) {
				spiderService.save(pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Site getSite() {
		return site;
	}

	/**
	 * 开始爬取
	 * @param web
	 */
	public void start(WebInfo web){
		this.web=web;
		Spider.create(crawler).addUrl(web.getSeed()).thread(10).run();
	}

	

}
