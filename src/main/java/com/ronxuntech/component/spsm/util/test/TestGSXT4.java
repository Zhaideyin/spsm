package com.ronxuntech.component.spsm.util.test;


import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;


public class TestGSXT4 implements PageProcessor {

//    public static final String URL_LIST = "http://www\\.gsxt\\.gov\\.cn/corp-query-search-1\\.html";
    

    private Site site = Site
            .me()
            .setDomain("www.gsxt.gov.cn")
            .setSleepTime(3000)
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")            
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Connection", "keep-alive")
//            .addHeader("Content-Length", "217")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Host", "www.gsxt.gov.cn")
            .addHeader("Origin", "http://www.gsxt.gov.cn")
            .addHeader("Referer", "http://www.gsxt.gov.cn/corp-query-homepage.html")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Upgrade-Insecure-Requests", "1")
            .addCookie("__jsluid", "9dfd6cbcf92634559dbd514ae6d35018")
            .addCookie("tlb_cookie", "44query_8080")
            .addCookie("CNZZDATA1261033118", "130459163-1482469870-http%253A%252F%252Fwww.gsxt.gov.cn%252F%7C1482972178")
            .addCookie("JSESSIONID", "D6F0ABD9828E20CAE197B2DE7CCE335A-n2:1")
            .setHttpProxy(new HttpHost("121.232.147.155",9000));

    @Override
    public void process(Page page) {
    		System.out.println("=======================================================");
    		System.out.println(page.getHtml());
    		System.out.println("=======================================================");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) { 
//    	tab:ent_tab
//    	token:68255332
//    	searchword:����
//    	geetest_challenge:91259144ed9086565bba0985f8a2fc905t
//    	geetest_validate:819040f800d604c289a3aa65a5204bf5
//    	geetest_seccode:819040f800d604c289a3aa65a5204bf5|jordan      
        
    	//���ύ
        Request request = new Request("http://www.gsxt.gov.cn/corp-query-search-1.html");
        request.setMethod(HttpConstant.Method.POST);
        NameValuePair[] nameValuePair = new NameValuePair[]{
//        		new BasicNameValuePair("tab","ent_tab"),
//        		new BasicNameValuePair("token","68255332"),
        		new BasicNameValuePair("searchword","快客"),
        		new BasicNameValuePair("geetest_challenge","91259144ed9086565bba0985f8a2fc905t"),
        		new BasicNameValuePair("geetest_validate","819040f800d604c289a3aa65a5204bf5"),
        		new BasicNameValuePair("geetest_seccode","819040f800d604c289a3aa65a5204bf5|jordan")
        		};
            
        request.putExtra("nameValuePair", nameValuePair);
        
        Spider.create(new TestGSXT4()).addRequest(request).run();
    }
}
