package com.ronxuntech.component.spsm.spider;

import java.util.ArrayList;
import java.util.List;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.controller.base.BaseController;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.Logger;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 *
 */
public class ZgnySpider implements PageProcessor {

    private WebInfo web;
    private SpiderManager spiderService;
    private TargetUrlManager targeturlService;
    private AnnexUrlManager annexurlService;
    // 保存seedURl的种子id
    private String seedUrlId = "";
    // 附件工具
    private AnnexUtil annexUtil = AnnexUtil.getInstance();
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000);

    public ZgnySpider(WebInfo web, String seedUrlId) {
        this.web = web;
        this.seedUrlId = seedUrlId;
        spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
        annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
    }

    public ZgnySpider() {
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        Html html = page.getHtml();
        List<String> links = html.xpath(web.getUrlTag()).links()
                .regex(web.getUrlRex()).all();

        annexUtil.insertIntoPage(page, web, annexUtil, annexurlService, targeturlService);
        /*String pageNumInContent = html.xpath(web.getTotalPage()).toString();
        int totalPageNum = annexUtil.getTotalPageNum(pageNumInContent, web.getSeed());

        if (pageFlag == 0) {
            jointNextpage(page, web.getSeed(), totalPageNum);
            pageFlag++;
        }*/

        //将下一页的链接放入列表
        page.addTargetRequest(html.xpath(web.getPageGetTag()).toString());
        // 循环将list中的链接取出， 并存入targetUrl 的pd中。
        try {
            annexUtil.targeturlInsertDatabase(links, seedUrlId, targeturlService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
            // 设置skip之后，这个页面的结果不会被Pipeline处理
            page.setSkip(true);
        }

    }

    /**
     * 拼接下一页
     * @param page
     * @param str
     * @param totalPageNum
     */
    private void jointNextpage(Page page, String str, int totalPageNum) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        String[] temp = seed.split("\\d+");

        String prf = temp[0];
        String suf = temp[1];
        for (int i = 2; i <=totalPageNum; i++) {
            urlList.add(prf + i + suf);
        }
        page.addTargetRequests(urlList);
    }

}
