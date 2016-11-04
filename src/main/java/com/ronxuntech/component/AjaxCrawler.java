package com.ronxuntech.component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri;
import com.ronxuntech.component.spsm.util.AnnexSpider;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.ImgOrDocPipeline;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import com.sun.org.apache.regexp.internal.recompile;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class AjaxCrawler{

	// 单例模式
	private static AjaxCrawler ajaxCrawler = new AjaxCrawler();

	// 静态工厂方法
	public static AjaxCrawler getInstance() {
		return ajaxCrawler;
	}
	// 创建service
	private SpiderService spiderService;
	private AnnexUtil annexUtil = new AnnexUtil();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	//chormdriver路径
		// winds
	private String chormPath1=PathUtil.getClassResources()+"spsm/chromedriver.exe";
		//linux
//	private String chormPath2="/usr/bin/google-chrome1";
	
	/**
	 * 通过webDriver 来获取当前页的page 然后通过正则表达式来匹配出相应的url.添加到一个集合中，然后再添加接口中
	 * @throws MalformedURLException 
	 */
	public Set<String> getAllUrl(WebInfo web) throws MalformedURLException{
		// 用于存放多个url
		Set<String> urlSet = new HashSet<>();
		System.getProperties().setProperty("webdriver.chrome.driver",chormPath1);
		WebDriver webDriver = (WebDriver) new ChromeDriver();
		webDriver.get(web.getSeed());
		String basePath=getBasePath(web.getSeed());
		int num = web.getTotalPage();
		for(int i = 1; i <= num; i++){
			String pageSource =webDriver.getPageSource();
			// 通过正则表达式来找出符合图片或者文档的链接
			Pattern p = Pattern.compile(web.getUrlRex());
			Matcher m = p.matcher(pageSource);
			//将当前页面匹配的都加入到urllist中
			while(m.find()) {
				urlSet.add(basePath+m.group());
			}
			if(web.getPageAjaxTag()!=""){
				webDriver.findElement(By.id(web.getPageAjaxTag())).click();
			}
		}
		webDriver.close();
		return urlSet;
	}
	/**
	 * 将set 转换成list
	 */

	public List<String> setToList(Set<String> set){
		List<String> urlList=new ArrayList<>();
		//循环将set中的放入ｌｉｓｔ中
		for(String str : set){
			urlList.add(str);
		}
		return urlList;
	}
	/**
	 * 开启爬取
	 * @param web
	 * @throws MalformedURLException 
	 */
	public void start(WebInfo web) throws MalformedURLException {
		Set<String> urlSet= getAllUrl(web);
		List<String> urlList =setToList(urlSet);

		PageProcessor doc = new PageProcessor() {
				public void process(Page page) {
					spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
					// 获取页面信息保存在html 对象中。
					Html html = page.getHtml();
					// 将爬取的目标地址添加到队列里面 web.getUrlRex()
					page.addTargetRequests(urlList);
					// 替换all 生成的 "[] ,"
					String regex = "[\\[|\\]|,]";
					String content = html.xpath(web.getList().get(1).toString()).all().toString().replaceAll(regex, "");
					String title = html.xpath(web.getList().get(0).toString()).all().toString().replaceAll(regex, "");
					PageData pd = new PageData();
					pd.put("SPIDER_ID", System.currentTimeMillis());
					pd.put("TITLE", title);
					pd.put("CONTENT", content);
					pd.put("TYPE", web.getTypeId());
					// 得到时间
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
			Spider.create(doc).addUrl(web.getSeed()).thread(10).run();
	}
	
	/**
	 *  通过传进来的ｕｒｌ取得项目的根目录、
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String getBasePath(String url) throws MalformedURLException{
		String basePath="";
		URL u =new URL(url);
		basePath=u.getProtocol()+"://"+u.getHost()+"/";
		return basePath;
	}
}
