package com.ronxuntech.component.spsm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.ronxuntech.util.UuidUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

public class CgrisSpider implements PageProcessor{

	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000).setCharset("gb2312")
//			Cookie:CNZZDATA1259170489=1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081; ASPSESSIONIDSCBRCDRR=JCCCEJHCOHAJJGEHLFLFHPLD
//				   CNZZDATA1259170489=1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081; ASPSESSIONIDSAASDDRR=EAHJLFEDNGLBFLHCGDMPDGEL
			.addCookie("CNZZDATA1259170489", "1377572436-1479282708-http%253A%252F%252Fwww.cgris.net%252F%7C1484544081")
			.addCookie("ASPSESSIONIDSCBRCDRR", "EAHJLFEDNGLBFLHCGDMPDGEL")
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
		
			;
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		Spider.create(new CgrisSpider()).thread(1).addUrl("http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E9%80%89%E6%8B%A9.asp").run();
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
	
		Html html=page.getHtml();
		System.out.println(html);
		
		
		Request request = new Request("http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E5%88%86%E5%B8%83.asp");
		String temp1="数据库_作物中文名";
	 	String dataBaseName="";
		try {
			dataBaseName =  URLEncoder.encode(temp1, "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	 	String temp2 = "满江红    ";
	 	String value="";
		try {
			value = URLEncoder.encode(temp2, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String temp3="B1";
		String submit="";
		try {
			submit = URLEncoder.encode(temp3, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String temp4="确认";
		String submit2="";
		try {
			submit2 =  URLEncoder.encode(temp4, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	 	
		String a="";
		try {
			a = new String("数据库_作物中文名".getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String b="";
		try {
			b = new String("满江红    ".getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String c="";
		try {
			c= new String("确认".getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		 NameValuePair[] nameValuePair ={
				 //中文
				 	
				  	new BasicNameValuePair(dataBaseName,value),
				  	new BasicNameValuePair(submit,submit2),
				   //url 编码
//				 	new BasicNameValuePair("%CA%FD%BE%DD%BF%E2_%D7%F7%CE%EF%D6%D0%CE%C4%C3%FB","%C2%FA%BD%AD%BA%EC++++"),
//					new BasicNameValuePair("B1","%C8%B7%C8%CF"),
					//浏览器上的参数
//					new BasicNameValuePair("数据库_作物中文名","满江红    "),
//					new BasicNameValuePair("B1","确认"),
//				 	new BasicNameValuePair(a,b),
//					new BasicNameValuePair("B1",c),
				 
//					
				    };
		 request.putExtra("nameValuePair", nameValuePair);
		 request.setMethod(HttpConstant.Method.POST);
		
		
		 page.addTargetRequest(request);
		
	}

}
