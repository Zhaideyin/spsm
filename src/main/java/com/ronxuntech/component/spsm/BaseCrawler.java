package com.ronxuntech.component.spsm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.FileCacheQueueScheduler;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.component.spsm.util.SpiderPipeline;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.seedurl.impl.SeedUrlService;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by tongbu on 2016/10/8 0008.
 */
public class BaseCrawler implements PageProcessor {

	// 单例模式
//	private  BaseCrawler crawler ;

	// 静态工厂方法
	public static BaseCrawler getInstance() {
		return new BaseCrawler();
	}
	
	public  BaseCrawler(){
		
	}
	
	public BaseCrawler(WebInfo web,String seedUrlId){
		this.web=web;
		this.seedUrlId=seedUrlId;
	}
	// 重新下载目标网页地址的集合
	private List<String> retargetUrlList;
	// 创建service
//	private SpiderService spiderService;
	private TargetUrlService targeturlService;
//	private SeedUrlManager seedurlService;
//	private AnnexUrlManager	annexurlService;
	// 用于存放第一次页面抓取到的list，用于去重
	List<String> tempList = new ArrayList<>();
	// 保存seedURl的种子id
	private String seedUrlId = "";
	private WebInfo web;
	//
	private int temp = 0;
	// 用于判断是否是重下
	private int reDown = 0;
	//用于控制循环
	private int forInt=0; 
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(200000).setCycleRetryTimes(3);
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	//判断页面是否存在
	private String error="404";

	// 数据爬取的主要方法
	public void process(Page page) {
//		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
//		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		// 如果是重下，则将传递过来的targeturl添加到队列中。并且修改redown的值
		if (reDown == 0 && retargetUrlList!=null) {
			page.addTargetRequests(retargetUrlList);
			reDown++;
		}
		// 获取页面信息保存在html 对象中。
		Html html = page.getHtml();
		String pageUrl = page.getUrl().toString().trim();
		//判断页面是否存在。如果不存在，则直接更新数据库，targetUrl 的状态
		String errorTitle=html.xpath("title").toString();
		if(errorTitle.contains(error)){
			annexUtil.updateTargetStatus(pageUrl, targeturlService);
		}
		// 将爬取的目标地址添加到队列里面 web.getUrlRex()
		List<String> list = html.links().regex(web.getUrlRex()).all();
		// --------------将targeturl 存入数据库去重开始-------------------------------

		int flag = 0;
		// 判断是否是第一次取页面 ，如果是则将第一次取的链接放入tempList 如果不是，则判断抓取到 新的链接是否和之前抓取的一样，
		// 如果一样，则flag++，不加入数据库
		if (temp == 0) {
			tempList = list;
//			temp++;
			// 将传递来的targeturl 添加到下载地址
			if (retargetUrlList!=null) {
				page.addTargetRequests(retargetUrlList);
			}
		} else {
//			tempList =list;
			// 将页面第一次抓到的，于后面抓取到的对比， 如果有相同的。则不加入集合中。
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < tempList.size(); j++) {
					if (list.get(i).equals(tempList.get(j))) {
						flag++;
					} else {
						continue;
					}
				}
			}

		}
		// -------------将targeturl 存入数据库去重结束-----------------------------------
		// 去掉页面上相同的链接
		Set<String> urlSet = annexUtil.listToSet(list);
		List<String> urlList = annexUtil.setToList(urlSet);

		// 将抓取到符合的链接放入列表中
		page.addTargetRequests(urlList);

		// -------特殊网站分页开始----------------------------------------------------------------------------------
		// 该网站没有下一页的按钮，全是页码
		if (temp==0 && web.getSeed().contains("http://rdjc.lknet.ac.cn/list.php")) {
			// List<String> pageList =new ArrayList<>();
			// 种子页面就是第一页，所以不需要添加第一页
//			for (int i = 2; i <= web.getTotalPage(); i++) {
//				urlList.add("http://rdjc.lknet.ac.cn/list.php?currpage=" + i + "&colid=114&keyword=&select=");
//			}
			//拼接网站下一页地址链接
			jointNextpage(page, urlList, web.getSeed(), web.getTotalPage());
			temp++;
//			page.addTargetRequests(urlList);
		}
		
		//特殊分页，没有分页的 标签， 不过分页有规律
		if(temp==0 && (web.getSeed().contains("http://catf.agri.cn") ||(web.getSeed().contains("http://cxpt.agri.gov.cn")
				|| web.getSeed().contains("http://pfscnew.agri.gov.cn") || web.getSeed().contains("http://www.moa.gov.cn")) )){
			jointNextpage2(page, urlList, web.getSeed(), web.getTotalPage());
			temp++;
		}
		
		// ------特殊网站结束--------------------------------------------------------------------------------------------

		// 将下一页的链接放入targeturl 中
		if (web.getPageGetTag() != "") {
			for(;forInt<web.getTotalPage();forInt++){
				// 下一页是个链接的。 比如： 下一页是 ....html.
				String nextPage = html.xpath(web.getPageGetTag()).toString();
				page.addTargetRequest(nextPage);
				urlList.add(nextPage);
			}
		}
		// 循环将list中的链接取出， 并存入targetUrl 的pd中。
		if(flag ==0 && urlList.size()!=0){
			try {
				annexUtil.targeturlInsertDatabase(urlList, seedUrlId, targeturlService);
			} catch (Exception e) {
			}
		}
		
		boolean isNextpage =annexUtil.isSave(web, page);
		// --------将需要的数据 得到后数据存入数据库（排除种子页面、分页链接）----------
		if(!(pageUrl.equals(web.getSeed().trim()))){
			if(isNextpage==false){
				String contents = html.xpath(web.getList().get(1)).all().toString();
				String title="";
				if(web.getList().get(0)!="" && web.getList().get(0) !=null){
				  title = html.xpath(web.getList().get(0)).toString();
				}
				
				page.putField("content", contents);
				page.putField("title", title);
				page.putField("pageUrl", pageUrl);
				if (page.getResultItems().get("content")==null){
			        //设置skip之后，这个页面的结果不会被Pipeline处理
			        page.setSkip(true);
			    }
			}else{ //如果该链接不是想要的，（如下一页的链接，但是又要修改他的状态）
				annexUtil.updateTargetStatus(pageUrl, targeturlService);
			}
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
		site.setCharset(web.getPageEncoding());
		try {
			temp = 0;
			seedUrlId = annexUtil.getSeedUrlId(web);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//存储urls文件的路径
		 String filepath ="D:\\webmagic\\spsm\\urls";
		//
		 Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		// 如果网页有图片或者文档， 开启的线程不能太多， 因为下载文档或者图片的时候也会开启3个线程，
		// 如果开十个爬取网页，那么有图或者文档的时候，则会开启30个。这样服务器估计受不了，所以我这里选择了4个
		if (web.isHasDoc() || web.isHasImg()) {
			Spider.create(new BaseCrawler(web,seedUrlId)).addUrl(web.getSeed()).thread(3).
			setDownloader(new HttpClientDownloader())
			.addPipeline(new SpiderPipeline(web))
			.scheduler(scheduler)
			.run();
		} else {
			Spider.create(new BaseCrawler(web,seedUrlId)).addUrl(web.getSeed())
			.thread(10).setDownloader(new HttpClientDownloader())
			.addPipeline(new SpiderPipeline(web))
			.scheduler(scheduler)
			.run();
		}
	}

	/**
	 * 设置传递进来 重新下载的targeturlList ,和初始化 web信息
	 * 
	 * @param retargetUrlList
	 * @param web
	 */
	public void setTargetUrlListAndWebInfo(List<String> retargetUrlList, WebInfo web) {
		this.retargetUrlList = retargetUrlList;
		this.web = web;
	}
	
	/**
	 * 组装下一页的链接。针对  http://rdjc.lknet.ac.cn/list.php 这一系列的网站
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	public void jointNextpage(Page page,List<String> urlList,String str,int totalpage){
		String seed=str;
		int n=seed.indexOf("?");
		String id=seed.substring(n+1);
		String suffex="&"+id+"&keyword=&select=";
		String prefix="http://rdjc.lknet.ac.cn/list.php?currpage=";
		for (int i = 2; i <= totalpage; i++) {
			urlList.add(prefix+ i + suffex);
		}
//		for(int j=0;j<urlList.size();j++){
//			System.out.println(urlList.get(j));
//		}
		page.addTargetRequests(urlList);
	}
	
	/**
	 * 组装下一页的链接，主要用于http://catf.agri.cn  、  http://cxpt.agri.gov.cn
	 * http://www.moa.gov.cn  http://pfscnew.agri.gov.cn
	 * 这一类的链接
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	public void jointNextpage2(Page page,List<String> urlList,String str,int totalpage) {
		String seed=str;
		for(int i=1;i<=totalpage;i++){
			urlList.add(seed+"index_"+i+".htm");
		}
		page.addTargetRequests(urlList);
	}
	
	public static void main(String[] args) {
		BaseCrawler b= new BaseCrawler();
		Page page =new Page();
		List<String> urlList =new ArrayList<>();
		b.jointNextpage(page, urlList, "http://rdjc.lknet.ac.cn/list.php?colid=", 10);
	}
}
