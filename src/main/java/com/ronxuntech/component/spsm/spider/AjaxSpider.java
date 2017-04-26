package com.ronxuntech.component.spsm.spider;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.ConvertUtil;
import com.ronxuntech.component.spsm.util.FileNameUtil;
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
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Html;
/**
 * 用chormedriver 来模拟点击下一页，来抓取数据
 * @author angrl
 *
 *现在没有用
 *
 */
public class AjaxSpider {

	// 单例模式
	private static AjaxSpider ajaxCcrawler=new AjaxSpider() ;
	// 静态工厂方法
	public static AjaxSpider getInstance() {
		return  ajaxCcrawler;
	}
	

	// 创建service
	private SpiderManager spiderService;
	private TargetUrlManager targeturlService;
	private SeedUrlManager seedurlService;
	private AnnexUrlManager annexurlService;
	// 附件工具类
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	private FileNameUtil fileNameUtil = FileNameUtil.getInstance();
	private ConvertUtil convertUtil = ConvertUtil.getInstance();
	private String seedUrlId = "";
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(200000).setCycleRetryTimes(3);

	// --------------- chormdriver路径-------------
	private String chormPath ="";
	// 判断页面是否存在的
	private String error="404";
	private String regex = "[\\[|\\]|,]";
	/**
	 * 通过webDriver 来获取当前页的page 然后通过正则表达式来匹配出相应的url.添加到一个集合中，并返回
	 * 
	 * @param web
	 * @return 正则匹配的链接（目标页面的链接）
	 * @throws MalformedURLException
	 */
	public Set<String> getAllUrl(WebInfo web)  {
		// 用于存放多个url
		Set<String> urlSet = new HashSet<>();
		//设置相应环境的路径。
		setChormPath();
		System.getProperties().setProperty("webdriver.chrome.driver", chormPath);
		WebDriver webDriver = (WebDriver) new ChromeDriver();
		webDriver.get(web.getSeed());
		// 先循环翻页，将所有页中符合的链接放入一个集合，
		int num = Integer.parseInt(web.getTotalPage());
		for (int i = 1; i < num; i++) {
			String pageSource = webDriver.getPageSource();
			// 通过正则表达式来找出符合图片或者文档的链接
			Pattern p = Pattern.compile(web.getUrlRex());
			Matcher m = p.matcher(pageSource);
			// 将当前页面匹配的都加入到urllist中
			while (m.find()) {
				// 拼装正确 的下载 url 并且放入set
				if (m.group().contains("http")) {
					urlSet.add(m.group().replace("amp;", ""));
				} else {
					String targetUrl = fileNameUtil.getTargetUrl(m.group(), web.getSeed());
					targetUrl = targetUrl.replace("amp;", "");
					urlSet.add(targetUrl);
				}
			}
			// 这类网站比较特殊，第一页里面没有上一页，在第二页才出下 ，所以，xpath 不同。就要判断页数
			// 总页数必须大于1 ， 才有下一页。否则没有下一页点击，
			if (web.getSeed().contains("http://www.cfip.cn:1012") && num > 1) {
				if (i == 1) {
					webDriver.findElement(By.xpath(web.getPageGetTag())).click();
				} else {
					webDriver.findElement(By.xpath(web.getPageAjaxTag())).click();
				} // 如果getseed() 不是上面的网站，或者num = < 1 并且 getPageAjaxTage != ""
			} else if (web.getPageAjaxTag() != "") {
				// 通过xpath定位到‘下一页’的，以为有的页面下一页没有 id
				webDriver.findElement(By.xpath(web.getPageAjaxTag())).click();
			}
		}
		webDriver.close();
		return urlSet;
	}

	/**
	 * 开启爬取
	 * 
	 * @param web
	 * @throws Exception
	 */
	public void start(WebInfo web) throws Exception {
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		Set<String> urlSet = getAllUrl(web);
		List<String> urlList = convertUtil.setToList(urlSet);
		seedUrlId =annexUtil.getSeedUrlId(web);
		//将抓到的targetUrl 存入数据库，并设置初始状态为 0
		try{
			annexUtil.targeturlInsertDatabase(urlList, seedUrlId, targeturlService);
		}catch(Exception e ){
			
		}
		
		// 如果seedUrlId 为空，则查询。否则便是一样的
		if (seedUrlId.equals("")) {
			seedUrlId = annexUtil.getSeedUrlId(web);
		}
		// 将抓取到的目标链接放入数据库中，并设置初始状态为0 (未爬取)
		
		// 设置爬取的编码格式
		if (!(web.getPageEncoding().equals("utf-8"))) {
			site.setCharset(web.getPageEncoding());
		}
		
		// 数据抓取的接口
			PageProcessor doc = new PageProcessor() {
				public void process(Page page) {

					// 获取页面信息保存在html 对象中。
					Html html = page.getHtml();
					// 得到当前页面的地址
					String pageUrl = page.getUrl().toString();
					String errortitle=html.xpath("title").toString();
					if(errortitle.contains(error)){
						annexUtil.updateTargetStatus(pageUrl, targeturlService);
					}
					// 将爬取的目标地址添加到队列里面 web.getUrlRex()
					page.addTargetRequests(urlList);
					String contents = html.xpath(web.getList().get(1)).all().toString().trim().replaceAll(regex, "");
					String title="";
					if(web.getList().get(0)!="" && web.getList().get(0) !=null){
						  title = html.xpath(web.getList().get(0)).toString();
					}
					page.putField("content", contents.trim());
					page.putField("title", title);
					page.putField("pageUrl", pageUrl);

					annexUtil.annexSaveAndDown(page, contents, title, web, annexurlService, targeturlService);
					
					if (page.getResultItems().get("content")==null || contents.equals("")){
				        //设置skip之后，这个页面的结果不会被Pipeline处理
				        page.setSkip(true);
				    }
				}
				public Site getSite() {
					return site;
				}
			};
				
		//存储urls文件的路径
		String filepath ="D:\\webmagic\\spsm\\urls";
		//
		Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		// 如果网页有图片或者文档， 开启的线程不能太多， 因为下载文档或者图片的时候也会开启3个线程，
		// 如果开十个爬取网页，那么有图或者文档的时候，则会开启30个。这样服务器估计受不了，所以我这里选择了3个
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		String fileDir =PathUtil.getClasspath()+ "uploadFiles/spsm/" + webUrl + "/";
		if (web.isHasDoc() || web.isHasImg()) {
			Spider.create(doc).addUrl(web.getSeed()).thread(3)
			.setDownloader(new HttpClientDownloader())
			.setScheduler(scheduler)
			.addPipeline(new SpiderPipeline(web))
			.addPipeline(new ImgOrDocPipeline(fileDir))
			.run();
		} else {
			Spider.create(doc).addUrl(web.getSeed()).thread(10)
			.setDownloader(new HttpClientDownloader())
			.setScheduler(scheduler)
			.addPipeline(new SpiderPipeline(web))
			.run();
		}

	}
	
	
	
	/**
	 * 得到chormDriver的路径。
	 */
	public void setChormPath(){
		//通过系统的名称来判断
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			chormPath =PathUtil.getClassResources() + "spsm/chromedriver.exe";
		}else{
			chormPath ="/usr/bin/chromedriver";
		}
	}

}
