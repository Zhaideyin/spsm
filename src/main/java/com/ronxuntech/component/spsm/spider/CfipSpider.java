package com.ronxuntech.component.spsm.spider;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.seedurl.impl.SeedUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by angrl on 2017/4/5.
 */
public class CfipSpider implements PageProcessor {
    private SpiderManager spiderService;
    private TargetUrlManager targeturlService;
    private SeedUrlManager seedurlService;
    private AnnexUrlManager annexurlService;
    // 用于存放第一次页面抓取到的list，用于去重
//		List<String> tempList = new ArrayList<>();
    // 保存seedURl的种子id
    private String seedUrlId = "";
    private WebInfo web;
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(20000).setCycleRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36");

    public CfipSpider() {
    }

    //构造
    public CfipSpider(WebInfo web, String seedUrlId){
        this.web=web;
        this.seedUrlId=seedUrlId;
        spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
        seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
        annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
    }

    @Override
    public void process(Page page) {

        Html html = page.getHtml();


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new CfipSpider()).
                addUrl("http://www.cfip.cn:1012/page/fieldtitle2.cbs?resna=lyzs&FieldNo=5&f=%D0%FB%B4%AB%C5%E0%D1%B5&field=%C0%B8%C4%BF&fldtype=67")
                .setDownloader(new HttpClientDownloader())
                .run();
    }
}
