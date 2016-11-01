package com.ronxuntech.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class AjaxCrawler {

	// 单例模式
	private static AjaxCrawler ajaxCrawler = new AjaxCrawler();

	// 静态工厂方法
	public static AjaxCrawler getInstance() {
		return ajaxCrawler;
	}

	// 创建service
	private SpiderService spiderService;
	private AnnexUtil annexUtil =new AnnexUtil();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	public void start(WebInfo web){
		System.getProperties().setProperty("webdriver.chrome.driver", "D:\\tool\\chromedriver.exe");
		WebDriver webDriver = (WebDriver) new ChromeDriver();
		// 设置模拟浏览器
		webDriver.get(web.getSeed());
		int num =web.getTotalPage();
		PageProcessor doc=null;
		for(int i=0;i<num;i++){
//			PageProcessor 
			doc =new PageProcessor() {
				public void process(Page page) {
					spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
					// 获取页面信息保存在html 对象中。
					Html html = page.getHtml();
					// 将爬取的目标地址添加到队列里面 web.getUrlRex()
					page.addTargetRequests(html.links().regex(web.getUrlRex()).all());
					//替换all 生成的 "[] ,"
					String regex="[\\[|\\]|,]";
					String content=html.xpath(web.getList().get(1).toString()).all().toString().replaceAll(regex, "");
					String title=html.xpath(web.getList().get(0).toString()).all().toString().replaceAll(regex, "");
					PageData pd = new PageData();
					pd.put("SPIDER_ID", System.currentTimeMillis());
					pd.put("TITLE",title );
					pd.put("CONTENT", content);
					pd.put("TYPE", web.getTypeId());
					//得到时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String d = sdf.format(new Date());
					pd.put("CREATE_TIME", d);
					// 获取当前页的图片或者文档
					annexUtil.downloadAnnex(web, pd, page);
					try {
						// 如果怕去的 内容是空，则不跳出。不存入数据库。
						if (!(content.trim().equals(""))) {
							spiderService.save(pd);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				public Site getSite() {
					return site;
				}
			};
			Spider.create(doc).thread(10).run();
			//模拟器点击分页
			webDriver.findElement(By.id(web.getPageAjaxTag())).click();
		}
		//关闭模拟器
		webDriver.close();
	}
	
}
