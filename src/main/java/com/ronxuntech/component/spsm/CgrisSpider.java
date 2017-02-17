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
		
//		CgrisSpider cs =new CgrisSpider();
//		cs.test();
		Spider.create(new CgrisSpider()).thread(1).addUrl("http://www.cgris.net/Gis%E4%BD%9C%E7%89%A9%E9%80%89%E6%8B%A9.asp").run();
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
		
	
		/*String temp="数据库_作物中文名";
		String databaseName="";
		try {
			databaseName = new String(temp.getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
	 	String temp1 = "满江红    ";
	 	
	 	String dataValue="";
		try {
			dataValue = new String(temp1.getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  

	 	String submit ="B1";
	 	
	 	String submitValue="";
	 	try {
	 		submitValue = new String("确认".getBytes( "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
	 	System.out.println("databaseNam:"+databaseName+"  dataValue:"+dataValue+"  submitValue :"+submitValue);*/
		
		
		String str1="数据库_作物中文名";
        String str2="满江红    ";
        String str3="确认";


        byte[] str1Bytes= new byte[0];//这里写原编码方式/**/
        try {
            str1Bytes = str1.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] newtstr1emp= new byte[0];//这里写转换后的编码方式
        try {
            newtstr1emp = new String(str1Bytes,"utf-8").getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String newStr1= null;//这里写转换后的编码方式
        try {
            newStr1 = new String(newtstr1emp,"gb2312");
            System.out.println(newStr1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        byte[] str2Bytes= new byte[0];//这里写原编码方式
        try {
            str2Bytes = str2.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] newtstr2emp= new byte[0];//这里写转换后的编码方式
        try {
            newtstr2emp = new String(str2Bytes,"utf-8").getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String newStr2= null;//这里写转换后的编码方式
        try {
            newStr2 = new String(newtstr2emp,"gb2312");
            System.out.println(newStr2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        byte[] str3Bytes= new byte[0];//这里写原编码方式
        try {
            str3Bytes = str3.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] newtstr3emp= new byte[0];//这里写转换后的编码方式
        try {
            newtstr3emp = new String(str3Bytes,"utf-8").getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String newStr3= null;//这里写转换后的编码方式
        try {
            newStr3 = new String(newtstr3emp,"gb2312");
            System.out.println(newStr3);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

		
		System.out.println("urlcoder:"+urlEncoder(newStr1));
		 NameValuePair[] nameValuePair ={
				 //中文
				  	new BasicNameValuePair(urlEncoder(newStr1),urlEncoder(newStr2)),
				  	new BasicNameValuePair("B1",urlEncoder(newStr3)),
				   //url 编码
//				 	new BasicNameValuePair("数据库_作物中文名","满江红    "),
//					new BasicNameValuePair("B1","确认"),
					//浏览器上的参数
//					new BasicNameValuePair("%CA%FD%BE%DD%BF%E2_%D7%F7%CE%EF%D6%D0%CE%C4%C3%FB","%C2%FA%BD%AD%BA%EC++++"),
//					new BasicNameValuePair("B1","%C8%B7%C8%CF"),
//				 	new BasicNameValuePair(name, value1),
//				 	new BasicNameValuePair("B1", submit1)
//				 	new BasicNameValuePair(a,b),
//					new BasicNameValuePair("B1",c),
				 
//					
				    };
		 request.putExtra("nameValuePair", nameValuePair);
		 request.setMethod(HttpConstant.Method.POST);
		
		
		 page.addTargetRequest(request);
		
	}

}
