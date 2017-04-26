package com.ronxuntech.component.spsm.test;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E5%88%86%E5%B8%83.asp
 */
public class CgrisSpidersss implements PageProcessor{

	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000).setCharset("gb2312")
//			Cookie:CNZZDATA1259170489=1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081; ASPSESSIONIDSCBRCDRR=JCCCEJHCOHAJJGEHLFLFHPLD
//				   CNZZDATA1259170489=1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081; ASPSESSIONIDSAASDDRR=EAHJLFEDNGLBFLHCGDMPDGEL
			.addCookie("CNZZDATA1259170489", "1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081")
			.addCookie("ASPSESSIONIDSCBRCDRR", "EAHJLFEDNGLBFLHCGDMPDGEL")
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
		
			;
	public static void main(String[] args) throws UnsupportedEncodingException {
		
//		CgrisSpidersss cs =new CgrisSpidersss();
//		cs.test();
		Spider.create(new CgrisSpidersss()).thread(1).addUrl("http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E9%80%89%E6%8B%A9.asp").run();
	}


	public String urlEncoder(String str){
		String urlEncodeStr="";
		try {
			urlEncodeStr=  URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return urlEncodeStr;
	}
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page)  {
	
		Html html=page.getHtml();
		System.out.println(html);
		
		Request request = new Request("http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E5%88%86%E5%B8%83.asp");
	
		String temp="数据库_作物中文名";
		String databaseName="";
		try {
			databaseName = new String(temp.getBytes( "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
	 	String temp1 = "满江红    ";
	 	
	 	String dataValue="";
		try {
			dataValue = new String(temp1.getBytes( "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  

	 	String submit ="B1";
	 	
	 	String submitValue="";
	 	try {
	 		submitValue = new String("确认".getBytes( "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
	 	
	 	System.out.println("databaseNam:"+databaseName+"  dataValue:"+dataValue+"  submitValue :"+submitValue);
		
       
		
		System.out.println("urlcoder:"+urlEncoder(databaseName));
		 NameValuePair[] nameValuePair ={
				 //中文
				  	new BasicNameValuePair(urlEncoder(databaseName),urlEncoder(dataValue)),
				  	new BasicNameValuePair("B1",urlEncoder(submitValue)),
				    };
		 request.putExtra("nameValuePair", nameValuePair);
		 request.setMethod(HttpConstant.Method.POST);
		
		
		 page.addTargetRequest(request);
		
	}

}
