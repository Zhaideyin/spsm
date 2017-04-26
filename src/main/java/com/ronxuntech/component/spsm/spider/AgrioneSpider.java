package com.ronxuntech.component.spsm.spider;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.NextPageUrlUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/*
 * 针对这类网站 的 http://www.agrione.cn
 * 
 * 
 */
public class AgrioneSpider implements PageProcessor {
    private WebInfo web;
    private SpiderManager spiderService;
    private TargetUrlManager targeturlService;
    private AnnexUrlManager annexurlService;
    // 保存seedURl的种子id
    private String seedUrlId = "";
    // 附件工具
    private AnnexUtil annexUtil = AnnexUtil.getInstance();
    private NextPageUrlUtil nextPageUrlUtil = NextPageUrlUtil.getInstance();
    private int pageFlag = 0;
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000)
            .addHeader("Upgrade-Insecure-Requests", "1")
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
            .addCookie("PHPSESSID", "fg45g4esaboeh13i3cqhhvbet0")
            .addCookie("Hm_lvt_9d8f86a65bc11382cd39fbe78884c454", "1483948469,1484012200")
            .addCookie("Hm_lpvt_9d8f86a65bc11382cd39fbe78884c454", "1484017938");


    public AgrioneSpider(WebInfo web, String seedUrlId) {
        spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
        annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
        this.web = web;
        this.seedUrlId = seedUrlId;
    }

    public AgrioneSpider() {
    }

    @Override
    public void process(Page page) {

        Html html = page.getHtml();
        List<String> links = html.links().xpath(web.getUrlTag()).regex(web.getUrlRex()).all();
        page.addTargetRequests(links);

        String pageNumInContent = html.xpath(web.getTotalPage()).toString();
        int totalPageNum = annexUtil.getTotalPageNum(pageNumInContent, web.getSeed());
        //拼接下一页的链接并放入Targetrequests 只是执行一次。
        if (pageFlag == 0) {
            List<String> urlList2 = nextPageUrlUtil.jointNextpage7(web.getSeed(), totalPageNum);
            links.addAll(urlList2);
            pageFlag++;
        }

        // 循环将list中的链接取出， 并存入targetUrl 的pd中。
        try {
            annexUtil.targeturlInsertDatabase(links, seedUrlId, targeturlService);
        } catch (Exception e) {

        }
        boolean isNextpage = annexUtil.isSave(web, page);
        // --------将需要的数据 得到后数据存入数据库（排除种子页面、分页链接）----------
        //将抓取的数据处理并放入page
        annexUtil.insertIntoPage(page, web, annexUtil, annexurlService, targeturlService);

        if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
            // 设置skip之后，这个页面的结果不会被Pipeline处理
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        site.setCharset(web.getPageEncoding());
        return site;
    }


}
