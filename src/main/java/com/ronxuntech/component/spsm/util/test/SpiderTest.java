package com.ronxuntech.component.spsm.util.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.component.spsm.util.SpiderPipeline;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Html;

public class SpiderTest implements PageProcessor{

	private static Scheduler scheduler = new FileCacheQueueScheduler("D:\\webmagic");
	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000).setCharset("gb2312");
	public static void main(String[] args) {
		TestPipelien tpipeline =new TestPipelien();
		Spider.create(new SpiderTest())
//		.addUrl("http://www.chinaforage.com/guifanbiaozhun/muxubiaozhu.htm")
		.addUrl("http://www.chinaforage.com/guifanbiaozhun/mucaobiaozhun.htm")
		.thread(5)
//		.addPipeline(tpipeline)
		.setDownloader(new HttpClientDownloader())
//		.scheduler(scheduler)
		.run();
		
		/*Map<String, Object> m=new HashMap<>();
		m.put("a", "aaa");

		if(m.get("b")!=null){
			System.out.println(m.get("a").toString());
		}*/
		
//		String str ="00000";
//		SpiderTest st =new SpiderTest();
//		str =st.t1(str);
//		st.t2(str);
//		System.out.println(str);
	
		
//		String h="<a title='农业部办公厅关于举办《种子法》配套办法培训班的通知.docx' href='http://www.seedchina.com.cn/FileDownload/DownloadTransfer.aspx?filename=农业部办公厅关于举办《种子法》配套办法培训班的通知.docx&amp;&amp;filePath=Attachment/20160918075425842.docx' target='_blank'>";
//		Pattern p = Pattern.compile("(http://www.seedchina.com.cn/FileDownload/DownloadTransfer.aspx\\?filename=.*.(\\w*).*filePath=[a-zA-z]*/[0-9]*\\.(doc|docx|xls|pdf))");
//		Matcher m = p.matcher(h);
//		while(m.find()){
//			System.out.println(m.group());
//		}
		

	}

	@Override
	public void process(Page page) {
		Html html=page.getHtml();
		
//		System.out.println(html.toString());
//		String error=html.xpath("title").toString();
//		System.out.println("error:"+error);
//		if(error.contains("404")){
//			System.out.println("错误执行");
//		}
//		System.out.println("----------------------------------------------------");
		List<String> imgs=html.xpath("//a/@href").regex("(http://www.chinaforage.com/.*.(doc|docx|xls|xlsx))").all();
				/*html.links()
				.regex("(http://catf.agri.cn/gzdt/[0-9]+/t[0-9]+_[0-9]+.htm)").all();
		*/
		String content = html.xpath("//*[@id='table3']/tbody/html()").all().toString();
//		List<String> urlList =html.links().regex("(http://pfscnew.agri.gov.cn/scdt/[0-9]+/t[0-9]+_[0-9]+.htm)").all();
//		List<String> list=new ArrayList<>();
//		for(String url :urlList){
//			String url1 ="http://www.seedchina.com.cn/"+url.replace("amp;","");
//			list.add(url1);
//		}
//		System.out.println("urlList:"+urlList.toString());
//		page.putField("content", content);
//		page.addTargetRequests(urlList);
		if (page.getResultItems().get("content")==null){
	        //设置skip之后，这个页面的结果不会被Pipeline处理
	        page.setSkip(true);
	    }
		System.out.println(content);
//		Pattern p = Pattern.compile("(http://www.seedchina.com.cn/FileDownload/DownloadTransfer.aspx\\?filename=.*.(\\w*).*filePath=[a-zA-z]*/[0-9]*.(doc|docx|xls|pdf))");
//		
//		Matcher m = p.matcher(html.xpath("//div[@class='conzw']/html()").toString());
//		while(m.find()){
//			System.out.println(m.group());
//		}
		
//		List<String> doc =html.xpath("//a").regex("(http://www.seedchina.com.cn/FileDownload/DownloadTransfer.aspx\\?filename=.*.(\\w*).*filePath=[a-zA-z]*/[0-9]*.(\\w*))").all();
//		String contents =
//		html.links().regex("").all();
		System.out.println("imgs"+imgs.toString());
//		System.out.println("doc" +doc.toString());
//		System.out.println("contents :"+contents);
//		page.putField("content", imgs.toString());
	}

	@Override
	public Site getSite() {
		return site;
	}

	public String t1(String str){
		str="22222";
		return str;
	}
	public void t2(String str){
		str="12121";
	}
}
