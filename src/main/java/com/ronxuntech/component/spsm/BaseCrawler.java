package com.ronxuntech.component.spsm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexSpider;
import com.ronxuntech.component.spsm.util.ImgOrDocPipeline;
import com.ronxuntech.component.spsm.util.ReadXML;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;
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
	
	//单例模式 
    private static BaseCrawler crawler=new BaseCrawler();  
    //静态工厂方法   
    public static BaseCrawler getInstance() {    
        return crawler;  
    }
    
//    private String seedUrl;
//    private String typeId;
	// 图片文件保存路径
    private String filePath=PathUtil.getClasspath()+"uploadFiles/spsm/annex";
	private Spider s;
	// 创建service
	private SpiderService spiderService ;
	private WebInfo web=new WebInfo();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1500);

	private String typeId;
	
	
	public void process(Page page) {
		spiderService= (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
//		spiderService=new SpiderService();
				// 获取页面信息保存在html 对象中。
				Html html = page.getHtml();
				// 将爬取的目标地址添加到队列里面   web.getUrlRex()
//				http://www.natesc.org.cn/Html/2010_03_08/21606_115457_2010_03_08_133230.html
				page.addTargetRequests(html.links().regex(web.getUrlRex()).all());
				// 抽取数据
//				page.putField("url", page.getUrl().regex(web.getUrlRex()).toString());
//				page.putField("title", html.xpath(web.getList().get(0).toString()).toString());
//				page.putField("content", html.xpath(web.getList().get(1).toString()).toString());

				PageData pd = new PageData();
				pd.put("SPIDER_ID", System.currentTimeMillis());
				pd.put("TITEL", html.xpath(web.getList().get(0).toString()).toString());
				pd.put("CONTENT", html.xpath(web.getList().get(1).toString()).toString());
				pd.put("TYPE", typeId);
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d =sdf.format(new Date());
				pd.put("CREATE_TIME",d);
				
				//如果含有文字或者图片  传递当前页面的地址过去，
				String pageUrl ="";
//				for (Map.Entry<String, Object> entry : page.getResultItems().getAll().entrySet()) {
//					System.out.println("遍历resultItems:");
//					// entry.getValue()是保存的所有链接的集合。包括获取的title.
//					if (entry.getValue() instanceof List) {
//						pageUrl=entry.getValue().toString();
//						System.out.println("获取的是List  " + entry.getValue().toString());
//					}
//				}
				//获取当前页的图片或者文档
				pageUrl =page.getUrl().toString();
				System.out.println("pageUrl   :"+pageUrl);
				if(web.isHasDoc()){
					if(!(pageUrl.equals(web.getSeed()))){
						//通过正则表达式来找出符合图片或者文档的链接
						Pattern p=Pattern.compile(web.getDocRegex()); 
						System.out.println("html:"+html.xpath(web.getList().get(1).toString()+"/html()").toString());
				        Matcher m=p.matcher(html.xpath(web.getList().get(1).toString()+"/html()").toString()); 
				        while(m.find())
				        { 
				        	if(pageUrl!=""){
				        		AnnexSpider annex=new AnnexSpider(pageUrl,web.getSeed(),web);
				        		Spider.create(annex).addUrl(pageUrl).addPipeline(new ImgOrDocPipeline(filePath)).thread(1).run();
				        	}
				        } 
					}
					
					
				}
				try {
					// 如果怕去的 内容是空，则不跳出。不存入数据库。
					if(!(pd.get("CONTENT").toString().trim().equals(""))|| !(pd.get("CONTENT")==null)){
						spiderService.save(pd);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				if(web.isHasDoc()){
//					
//				}
		}
		public Site getSite() {
		return site;
	}


	/**
	 * 文本信息保存
	 * 
	 * @param web
	 * @param typeId
	 *            数据类型
	 */

	/*public void downLoadWords(Web web, String typeId) {
		spiderService= (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
//		spiderService=new SpiderService();
		PageProcessor pageProcessor = new PageProcessor() {

			// 设置爬取的次数和间隔的时间
			private Site site = Site.me().setRetryTimes(3).setSleepTime(1500);

			public void process(Page page) {
				// 获取页面信息保存在html 对象中。
				Html html = page.getHtml();
				// 将爬取的目标地址添加到队列里面
				page.addTargetRequests(html.links().regex(web.getUrlRex()).all());
				// 抽取数据
//				page.putField("url", page.getUrl().regex(web.getUrlRex()).toString());
//				page.putField("title", html.xpath(web.getList().get(0).toString()).toString());
//				page.putField("content", html.xpath(web.getList().get(1).toString()).toString());

				PageData pd = new PageData();
				pd.put("SPIDER_ID", System.currentTimeMillis());
				pd.put("TITEL", html.xpath(web.getList().get(0).toString()).toString());
				pd.put("CONTENT", html.xpath(web.getList().get(1).toString()).toString());
				pd.put("TYPE", typeId);
				
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String d =sdf.format(new Date());
//				pd.put("CREATE_TIME",d);
				try {
					// 如果怕去的 内容是空，则不跳出。不存入数据库。
					if(!(pd.get("CONTENT").toString().trim().equals(""))){
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
		 s=Spider.create(pageProcessor).addUrl(web.getSeed()).thread(5);
		 s.start();
	}*/

	/**
	 * 图片下载
	 * 
	 * @param web
	 */
	/*public void downLoadImg(Web web) {
		PageProcessor ImgCrawler = new PageProcessor() {
			// 我也不知道是什么
			private Site site = Site.me().setDomain(UrlUtils.getDomain(web.getSeed()));

			public void process(Page page) {
				// 通过page获取到页面， 并保存到html对象中
				Html html = page.getHtml();
				// 抓取所有符合的页面的链接
				List<String> requests = html.links().regex(web.getUrlRex()).all();
				// 添加到爬取的队列中
				page.addTargetRequests(requests);
				// 抓取符合条件的所有链接
				List<String> listProcess = html.xpath(web.getImgTag()).regex(web.getImgRegex()).all();
				// 获取title，用于创建文件夹，保存相关的图片
				String imgHostFileName = html.xpath("//title/text()").toString();
				// 将获取的文件名保存到list中。
				listProcess.add(0, imgHostFileName);
//				page.putField("img", listProcess);
			}

			public Site getSite() {
				return site;
			}

		};

		 s=Spider.create(ImgCrawler).addUrl(web.getSeed()).addPipeline(new ImgOrDocPipeline(filePath)).thread(10);
		 s.start();
	}
*/
	/**
	 * 文档下载
	 * 
	 * @param web
	 */
	/*public void downLoadDoc(Web web) {
		PageProcessor docCrawler = new PageProcessor() {
			// 我也不知道是什么
			private Site site = Site.me().setDomain(UrlUtils.getDomain(web.getSeed()));

			public void process(Page page) {
				// 通过page获取到页面， 并保存到html对象中
				Html html = page.getHtml();
				// BaseCrawler b =new BaseCrawler();
				// System.out.println(html.toString());
				// 抓取所有符合的页面链接
				List<String> requests = html.links().regex(web.getUrlRex()).all();

				// 添加到队列中
				page.addTargetRequests(requests);
				// 抓取符合条件的所有链接
				List<String> listProcess = html.xpath(web.getDocTag()).regex(web.getDocRegex()).all();
				
				 * for (Map.Entry<String, Object> entry :
				 * page.getResultItems().getAll().entrySet()) {
				 * System.out.println("遍历resultItems:"); //
				 * entry.getValue()是保存的所有链接的集合。包括获取的title. if (entry.getValue()
				 * instanceof List) { System.out.println("获取的是List  " +
				 * entry.getValue().toString()); } }
				 
				// 获取title，用于创建文件夹，保存相关的文档
				String docHostFileName = html.xpath("//title/text()").toString();
				// String
				// fileName=html.xpath("//a[@contentmenu='body']/text()").toString();
				// System.out.println("fileName "+fileName);
				// 将获取的文件名保存到list中。
				listProcess.add(0, docHostFileName);
//				page.putField("doc", listProcess);
			}

			public Site getSite() {
				return site;
			}
		};

		s=Spider.create(docCrawler).addUrl(web.getSeed()).addPipeline(new ImgOrDocPipeline(filePath)).thread(10);
		s.start();
	}*/

	/**
	 * ajax分页
	 * 
	 * @param web
	 */
/*	public void pagingByAjax(Web web, String typeId) {
		System.getProperties().setProperty("webdriver.chrome.driver", "C:\\Users\\tongbu\\Downloads\\chromedriver.exe");
		// 设置模拟浏览器
		WebDriver webDriver = (WebDriver) new ChromeDriver();
		webDriver.get(web.getSeed());// 种子注入
		// 获取总的页数
		// WebElement all =
		// webDriver.findElement(By.id("GridView1_LabelPageCount"));
		int num = web.getTotalPage();
		//循环操作每页
		for (int j = 1; j <= num; j++) {
			
			downLoadWords(web, typeId);
			if (web.hasImg) {
				downLoadImg(web);
			}
			// 如果网页存在文档则爬取文档（目前包括 .doc .pdf）
			if (web.hasDoc) {
				downLoadDoc(web);
			}
			webDriver.findElement(By.id(web.getPageTag())).click();
		}

	}*/

	/**
	 * 纯post请求分页
	 * 
	 * @param web
	 */
	/*public void pagingByPost(Web web) {

	}*/

	/**
	 * get请求分页
	 * 
	 * @param web
	 */
	/*public void pagingByGet(Web web) {

	}*/

	/**
	 * 开启方法
	 * 
	 * @param seedUrl
	 *            种子
	 * @param typeId
	 *            类型id
	 * @throws Exception
	 */
	public void start(String seedUrl, String typeId) throws Exception {
		this.typeId =typeId;
		ReadXML readXML = new ReadXML();
		final List list = readXML.ResolveXml();
		// 循环遍历读取到的xml
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> hashMap = (HashMap<String, String>) list.get(i);

			String seed = hashMap.get("seed");
			System.out.println("baseCrawler     "+seed);
			// 通过传递的种子来判断网页的规则，如果种子不对应，则结束当层循环
			if (!(seed.equals(seedUrl))) {
				continue;
			}
			String urlRex = hashMap.get("urlRex");
			String tag1 = hashMap.get("tag1");
			String tag2 = hashMap.get("tag2");
			// 文字类抓取
			// 用web类来传值
//			final Web web = new Web();
			web.setSeed(seed);
			web.setUrlRex(urlRex);
			List<String> taglist = new ArrayList<String>();
			taglist.add(tag1);
			taglist.add(tag2);
			web.setList(taglist);

			// 图片爬取
			boolean hasImg = Boolean.parseBoolean(hashMap.get("hasImg"));
			String imgRegex = hashMap.get("imgRegex");
			String imgTag = hashMap.get("imgTag");
			web.setImgTag(imgTag);
			web.setImgRegex(imgRegex);
			web.setHasImg(hasImg);
			// 文件
			boolean hasDoc = Boolean.parseBoolean(hashMap.get("hasDoc"));
			String DocRegex = hashMap.get("docRegex");
			String docTag = hashMap.get("docTag");
			web.setDocTag(docTag);
			web.setDocRegex(DocRegex);
			web.setHasDoc(hasDoc);

			// 分页
			int totalPage = Integer.parseInt(hashMap.get("totalPage"));
			String pageAjaxTag = hashMap.get("pageAjaxTag");
			String pageGetTag = hashMap.get("pageGetTag");
			String pagePostTag=hashMap.get("pagePostTag");
			String pageMethod =hashMap.get("pageMethod");
			web.setTotalPage(totalPage);
			web.setPageMethod(pageMethod);
			web.setPageAjaxTag(pageAjaxTag);
			web.setPageGetTag(pageGetTag);
			web.setPagePostTag(pagePostTag);
			// -----------------------------------------------------------------------------
			// 分页
			if (pageMethod.equals("ajax")) {
//				pagingByAjax(web, typeId);// 如果网页存在图片则爬取图片
				
			} else if (pageMethod.equals("get")) {

				
			} else {

			}

		}
	}
	
	
	
	/**
	 * 停止
	 */
/*	public void down(){
//		if(crawler.getsDoc()!=null ){
//			crawler.getsDoc().stop();
//			System.out.println(crawler     + "           000 stop ----------------------------");
//		}
//		if(crawler.getsImg()!=null ){
//			crawler.getsImg().stop();
//		}
//		if(crawler.getsWord()!=null ){
//			crawler.getsWord().stop();
//		}
		if(crawler.getS()!=null){
			s.stop();
		}
		
		System.out.println(crawler     + "           stop ----------------------------");
	}
*/
	// 用来传递数据的类
	/*class Web {
		private String seed;// 种子
		private String urlRex;// 要抓取的url正则表达式
		private List<String> list;// 标签列表
		private String imgRegex; // 过滤图片的正则表达式
		private String docRegex; // 过滤文档的正则表达式
		private String imgTag; // 提取图片的 标签
		private String docTag; // 提取文档的标签
		private boolean hasImg; // 是否存在图片
		private boolean hasDoc; // 是否存在文档
		private int totalPage; // 总页数
		private String pageTag; // 标签
		private String pageMethod; // 分页方式

		public int getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(int totalPage) {
			this.totalPage = totalPage;
		}

		public String getPageTag() {
			return pageTag;
		}

		public void setPageTag(String pageTag) {
			this.pageTag = pageTag;
		}

		public String getPageMethod() {
			return pageMethod;
		}

		public void setPageMethod(String pageMethod) {
			this.pageMethod = pageMethod;
		}

		public String getImgTag() {
			return imgTag;
		}

		public void setImgTag(String imgTag) {
			this.imgTag = imgTag;
		}

		public String getDocTag() {
			return docTag;
		}

		public void setDocTag(String docTag) {
			this.docTag = docTag;
		}

		public String getSeed() {
			return seed;
		}

		public void setSeed(String seed) {
			this.seed = seed;
		}

		public String getUrlRex() {
			return urlRex;
		}

		public void setUrlRex(String urlRex) {
			this.urlRex = urlRex;
		}

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}

		public String getImgRegex() {
			return imgRegex;
		}

		public void setImgRegex(String imgRegex) {
			this.imgRegex = imgRegex;
		}

		public String getDocRegex() {
			return docRegex;
		}

		public void setDocRegex(String docRegex) {
			this.docRegex = docRegex;
		}

		public boolean isHasImg() {
			return hasImg;
		}

		public void setHasImg(boolean hasImg) {
			this.hasImg = hasImg;
		}

		public boolean isHasDoc() {
			return hasDoc;
		}

		public void setHasDoc(boolean hasDoc) {
			this.hasDoc = hasDoc;
		}

	}
*/
		}
