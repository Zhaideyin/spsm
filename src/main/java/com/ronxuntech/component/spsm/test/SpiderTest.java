package com.ronxuntech.component.spsm.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;

import com.sun.tools.javac.jvm.ByteCodes;
import okhttp3.Response;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

public class SpiderTest implements PageProcessor {

//	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000).setCharset("utf-8")
//			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36");

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .setCharset("gb2312");

    // 附件工具
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
//		Html html= page.getHtml();
        List<String> titles = page.getHtml().xpath("/html/body/table[5]/tbody/").all();
        System.out.println("title:"+titles);
        String url = "http://icgr.caas.net.cn/Gis%E4%BD%9C%E7%89%A9%E5%88%86%E5%B8%83.asp";
        Request request = new Request(url);
        String  databaseName = "数据库_作物中文名";
        String data = "满江红    ";
        String submit ="确认";
        NameValuePair[] nameValuePair ={
                new BasicNameValuePair(databaseName,data),
                new BasicNameValuePair("B1",submit)
        };
        request.putExtra("nameValuePair", nameValuePair);
        request.setMethod(HttpConstant.Method.POST);
        page.addTargetRequest(request);

    }

    public static void main(String[] args) throws Exception {
       /* SpiderTest t = new SpiderTest();
        Spider.create(t).addUrl("http://icgr.caas.net.cn/gis%E4%BD%9C%E7%89%A9%E9%80%89%E6%8B%A9.asp").thread(1).run();*/

        Map<String,String> param = new HashMap<>();

        param.put(new String("数据库_作物中文名".getBytes(),"gb2312"),new String("满江红    ".getBytes(),"gb2312"));
        param.put(new String("B1".getBytes(),"gb2312"),new String("确认".getBytes(),"gb2312"));

        HttpListener listener =new HttpListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                System.out.println("1111111111111111");
            }
        };

        HttpClient client = HttpClient.getInstance();
        client.syncPost("http://icgr.caas.net.cn/gis%E4%BD%9C%E7%89%A9%E9%80%89%E6%8B%A9.asp",param);
    }


}
