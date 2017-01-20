package com.ronxuntech.component.spsm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.component.spsm.util.ImgOrDocPipeline;
import com.ronxuntech.component.spsm.util.SpiderPipeline;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import oracle.security.o3logon.a;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Html;

/**
 * 最常见的方式，点击下一页，url会发生变化的
 * 
 * @author angrl
 *
 */
public class BaseCrawler implements PageProcessor {

	// 单例模式
	// private BaseCrawler crawler ;

	// 静态工厂方法
	public static BaseCrawler getInstance() {
		return new BaseCrawler();
	}

	public BaseCrawler() {

	}

	public BaseCrawler(WebInfo web, String seedUrlId) {
		this.web = web;
		this.seedUrlId = seedUrlId;
	}

	// 重新下载目标网页地址的集合
	private List<String> retargetUrlList;
	// 创建service
	private SpiderManager spiderService;
	private TargetUrlManager targeturlService;
//	private SeedUrlManager seedurlService;
	private AnnexUrlManager annexurlService;
	// 用于存放第一次页面抓取到的list，用于去重
	List<String> tempList = new ArrayList<>();
	// 保存seedURl的种子id
	private String seedUrlId = "";
	private WebInfo web;
	//
	private int temp = 0;
	private int pageNumtemp = 0;
	// 用于判断是否是重下
	private int reDown = 0;
	private int currentPageNum = 0;
	// 用于控制循环
//	private int forInt = 1;
	private Site site = Site.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setTimeOut(200000)
			.setCycleRetryTimes(3)
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36");
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	// 判断页面是否存在
	private String error = "404";

	private String regex = "[\\[|\\]|,]";

	// 数据爬取的主要方法
	public void process(Page page) {
		spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
		targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
		// 如果是重下，则将传递过来的targeturl添加到队列中。并且修改redown的值
		if (reDown == 0 && retargetUrlList != null) {
			page.addTargetRequests(retargetUrlList);
			reDown++;
		}
		// 获取页面信息保存在html 对象中。
		Html html = page.getHtml();
		String pageUrl = page.getUrl().toString().trim();
		// 判断页面是否存在。如果不存在，则直接更新数据库，targetUrl 的状态
		String errorTitle = html.xpath("title").toString();
		if (errorTitle.contains(error)) {
			annexUtil.updateTargetStatus(pageUrl, targeturlService);
		}
		// 将爬取的目标地址添加到队列里面 web.getUrlRex()
		List<String> list =new ArrayList<>();
//		if(web.getSeed().contains("http://finance.aweb.com.cn")){
//			//为了去掉该网站没有用的链接
//			list = html.xpath("//div[@class='newLists']").links().regex(web.getUrlRex()).all();
//			if(list.size()==0 || list==null){
//				list =html.links().regex(web.getUrlRex()).all();
//			}
//		}else{
		if(StringUtils.isNotBlank(web.getUrlRex())){
			list = html.xpath(web.getUrlTag()).links().regex(web.getUrlRex()).all();
		}
			
//		}
		// -------------将targeturl 去重-----------------------------------
//		Set<String> urlSet = annexUtil.listToSet(list);
//		List<String> urlList = annexUtil.setToList(urlSet);
		annexUtil.removeDuplicateWithOrder(list);
		// --------------将targeturl 存入数据库去重开始-------------------------------

		int flag = 0;
		// 判断是否是第一次取页面 ，如果是则将第一次取的链接放入tempList 如果不是，则判断抓取到 新的链接是否和之前抓取的一样，
		// 如果一样，则flag++，不加入数据库
		if (temp == 0) {
			tempList = list;
			// 将传递来的targeturl 添加到下载地址
			if (retargetUrlList != null) {
				page.addTargetRequests(retargetUrlList);
			}
		} else {
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
	
		/*// 去掉页面上相同的链接
		Set<String> urlSet = annexUtil.listToSet(list);
		List<String> urlList = annexUtil.setToList(urlSet);*/

		// 将抓取到符合的链接放入列表中
		page.addTargetRequests(list);

		// 得到总页数
		int pageNum = 0;
		if (pageNumtemp == 0) {
			if (StringUtils.isNotBlank(web.getTotalPage())) {
				if(StringUtils.isNumeric(web.getTotalPage())){
					pageNum = Integer.parseInt(web.getTotalPage());
				}else{
					String pageNumInContent = html.xpath(web.getTotalPage()).toString();
//					System.out.println("pageNumInContent:"+pageNumInContent);
					pageNum =annexUtil.getTotalPageNum(pageNumInContent, page.getUrl().toString());
				}
				
			} else {
				pageNum = 1;
			}
			pageNumtemp++;
		}

		// 讲所有页的链接添加到targeturl 中，(通过获取 下一页 中的链接地址，放入target中， )
		if (web.getPageGetTag() != "") {
			// for(;forInt<pageNum;forInt++){
			// 下一页是个链接的。 比如： 下一页是 ....html.
			String nextPage="";
			if(StringUtils.isNotBlank(web.getPageAjaxTag())){
				if(currentPageNum<1){//第一次没有 ‘上一页’两次的
					nextPage = html.xpath(web.getPageAjaxTag()).toString();
				}else{
					 nextPage = html.xpath(web.getPageGetTag()).toString();
				}
			}else{
				 nextPage = html.xpath(web.getPageGetTag()).toString();
			}
			page.addTargetRequest(nextPage);
			if(null!=nextPage && nextPage.length()>0){
				list.add(nextPage);
			}
			
			currentPageNum++;
			// }
		}

		// -------特殊网站分页开始----------------------------------------------------------------------------------
		// 该网站没有下一页的按钮，全是页码
		List<String> urlList2 =new ArrayList<>();
		if (temp == 0 && web.getSeed().contains("http://rdjc.lknet.ac.cn/list.php")) {
			// 拼接网站下一页地址链接
			// jointNextpage(page, urlList, web.getSeed(), web.getTotalPage());
			if (pageNum > 1) {
				urlList2 = jointNextpage(page, web.getSeed(), pageNum);
				temp++;
			}

		}

		
		// 特殊分页，没有分页的 标签， 不过分页有规律
		if (temp == 0
				&& (web.getSeed().contains("http://catf.agri.cn") || (web.getSeed().contains("http://cxpt.agri.gov.cn")
						|| web.getSeed().contains("http://www.tea.agri.cn")
						|| web.getSeed().contains("http://pfscnew.agri.gov.cn")
						|| web.getSeed().contains("http://www.moa.gov.cn")))) {
			if (pageNum > 1) {
				 urlList2= jointNextpage2(page, web.getSeed(), pageNum);
				temp++;
			}

		}

		if (temp == 0 && web.getSeed().contains("http://www.caas.net.cn")) {
			if (pageNum > 1) {
				 urlList2=jointNextpage3(page, web.getSeed(), pageNum);
			}
		}
		
		if(temp == 0 && web.getSeed().contains("http://finance.aweb.com.cn") 
				||web.getSeed().contains("http://www.nykfw.com")  ||
				web.getSeed().contains("http://www.gdcct.net")  && pageNum >1){
			urlList2=jointNextpage4(page, web.getSeed(), pageNum);
		}

		if(temp ==0 && web.getSeed().contains("http://www.sczyw.com") && pageNum>1){
			 urlList2=jointNextpage5(page, web.getSeed(), pageNum);
		}
		
		if(temp ==0 && web.getSeed().contains("http://www.3456.tv/") && pageNum>1){
			urlList2 = jointNextpage6(page, web.getSeed(), pageNum);
		}
		//将得到的分页链接也加入到target 中， 然后存入targeturl 中，
		if(urlList2!=null  && urlList2.size()!=0){
			list.addAll(urlList2);
		}
		
		// ------特殊网站结束--------------------------------------------------------------------------------------------

		// 循环将list中的链接取出， 并存入targetUrl 的pd中。
		if (flag == 0 && list.size() != 0) {
			try {
				annexUtil.targeturlInsertDatabase(list, seedUrlId, targeturlService);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

	public Site getSite() {
		site.setCharset(web.getPageEncoding());
		return site;
	}

	/**
	 * 开始爬取
	 * 
	 * @param web
	 */
	public void start(WebInfo web) {
		this.web = web;
		// site.setCharset(web.getPageEncoding());
		// System.out.println("site:"+site.getCharset());
		try {
			temp = 0;
			seedUrlId = annexUtil.getSeedUrlId(web);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 存储urls文件的路径
		String filepath = PathUtil.getClasspath()+"\\urls";
		//
		Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		// 如果网页有图片或者文档， 开启的线程不能太多， 因为下载文档或者图片的时候也会开启3个线程，
		// 如果开十个爬取网页，那么有图或者文档的时候，则会开启30个。这样服务器估计受不了，所以我这里选择了4个
		// 网站/标题作为文件夹 清除特殊字符
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		String fileDir = PathUtil.getClasspath() + "uploadFiles/spsm/" + webUrl + "/";

		if(web.getSeed().contains("http://www.agrione.cn")){
			Spider spider=Spider.create(new AgrioneSpider(web,seedUrlId))
					.setDownloader(new HttpClientDownloader())
					.addUrl(web.getSeed())
					.addPipeline(new  SpiderPipeline(web)).addPipeline(new ImgOrDocPipeline(fileDir))
//					.scheduler(scheduler)
					.thread(5);
			spider.run();
		}
		
		if (web.isHasDoc() || web.isHasImg()) {
			Spider.create(new BaseCrawler(web, seedUrlId)).addUrl(web.getSeed()).thread(3)
					.setDownloader(new HttpClientDownloader()).addPipeline(new SpiderPipeline(web))
					.addPipeline(new ImgOrDocPipeline(fileDir)).scheduler(scheduler).run();
		} else {
			Spider.create(new BaseCrawler(web, seedUrlId)).addUrl(web.getSeed()).thread(5)
					.setDownloader(new HttpClientDownloader()).addPipeline(new SpiderPipeline(web))
					.scheduler(scheduler).run();
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
	 * 组装下一页的链接。针对 http://rdjc.lknet.ac.cn/list.php 这一系列的网站
	 * 
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	public List<String> jointNextpage(Page page,  String str, int totalpage) {
		String seed = str;
		List<String> urlList =new ArrayList<>();
		int n = seed.indexOf("?");
		String id = seed.substring(n + 1);
		String suffex = "&" + id + "&keyword=&select=";
		String prefix = "http://rdjc.lknet.ac.cn/list.php?currpage=";
		for (int i = 2; i < totalpage; i++) {
			urlList.add(prefix + i + suffex);
		}
		page.addTargetRequests(urlList);
		return urlList;
	}

	/**
	 * 组装下一页的链接，主要用于http://catf.agri.cn 、 http://cxpt.agri.gov.cn
	 * http://www.moa.gov.cn http://pfscnew.agri.gov.cn http://www.tea.agri.cn
	 * 这一类的链接
	 * 
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	public List<String> jointNextpage2(Page page,  String str, int totalpage) {
		String seed = str;
		List<String> urlList =new ArrayList<>();
		for (int i = 1; i < totalpage; i++) {
			urlList.add(seed + "index_" + i + ".htm");
		}
		page.addTargetRequests(urlList);
		return urlList;
	}

	/**
	 * 组装 http://www.caas.net.cn 一类的网站链接
	 * 
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	public List<String> jointNextpage3(Page page,  String str, int totalpage) {
		String seed = str;
		List<String> urlList =new ArrayList<>();
		for (int i = 1; i < totalpage; i++) {
			urlList.add(seed + "index" + i + ".shtml");
		}
		page.addTargetRequests(urlList);
		return urlList;
	}

	/**
	 * http://finance.aweb.com.cn 等网站
	 * 
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	
	  public List<String> jointNextpage4(Page page,String str,int totalpage){
		  List<String> urlList =new ArrayList<>();
		  String seed=str; for(int i=1;i<=totalpage;i++){
			  	urlList.add(seed+"index_"+i+".html"); 
		  }
		  page.addTargetRequests(urlList);
		  return urlList;
	  }
	 
	/**
	 * http://www.sczyw.com
	 * @param page
	 * @param urlList
	 * @param str
	 * @param totalpage
	 */
	  public List<String> jointNextpage5(Page page, String str, int totalpage) {
			String seed = str;
			List<String> urlList =new ArrayList<>();
			for (int i = 2; i <= totalpage; i++) {
				urlList.add(seed + "&currpage=" + i);
			}
			page.addTargetRequests(urlList);
			return urlList;
		}
	  
	  /**
	   * http://www.3456.tv/
	   * @param page
	   * @param str
	   * @param totalpage
	   * @return
	   */
	  public List<String> jointNextpage6(Page page, String str, int totalpage) {
			String seed = str.replaceAll("\\.html", "");
			List<String> urlList =new ArrayList<>();
			for (int i = 2; i <= totalpage; i++) {
				urlList.add(seed + "_" + i+".html");
			}
			page.addTargetRequests(urlList);
			return urlList;
		}
	  
	  
	  
	public static void main(String[] args) {
		BaseCrawler b = new BaseCrawler();
		Page page = new Page();
		List<String> urlList = new ArrayList<>();
		b.jointNextpage(page, "http://rdjc.lknet.ac.cn/list.php?colid=", 10);
	}
}
