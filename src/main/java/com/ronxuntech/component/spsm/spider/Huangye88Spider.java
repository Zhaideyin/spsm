package com.ronxuntech.component.spsm.spider;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于http://sichuan.huangye88.com/zhongzi/
 * Created by angrl on 2017/3/31.
 */
public class Huangye88Spider implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(200000).setCycleRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
    // 创建service
    private SpiderManager spiderService;
    private TargetUrlManager targeturlService;
    private AnnexUrlManager annexurlService;
    private AnnexUtil annexUtil = AnnexUtil.getInstance();
    private WebInfo web;
    private String seedUrlId;

    public Huangye88Spider(WebInfo web, String seedUrlId) {
        this.web = web;
        this.seedUrlId = seedUrlId;
        spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
        annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
    }

    public Huangye88Spider() {

    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<String> links = html.xpath(web.getUrlTag()).links().regex(web.getUrlRex()).all();
        String total = html.xpath(web.getTotalPage()).toString();
        String pageUrl = page.getUrl().toString();
        //如果得到的总条数不为空，则计算出总共的页数，然后拼接连接地址
        if (StringUtils.isNotEmpty(total)) {
            int totalNum = Integer.parseInt(total);
            if (!(pageUrl.contains("/pn"))) {
                List<String> nextPageUrlList = joinNextPage(pageUrl, getPageNum(totalNum));
                links.addAll(nextPageUrlList);
            }
        }
        //将要抓取到 链接存入库中
        annexUtil.targeturlInsertDatabase(links, seedUrlId, targeturlService);

        page.addTargetRequests(links);
        //数据处理
        annexUtil.insertIntoPage(page, web, annexUtil, annexurlService, targeturlService);
        if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
            // 设置skip之后，这个页面的结果不会被Pipeline处理
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 得到总的页数
     *
     * @param totalNum
     * @return
     */
    public int getPageNum(int totalNum) {
        int pageNum = 1;
        if (totalNum % 35 > 0) {
            pageNum = totalNum / 35 + 1;
        } else {
            pageNum = totalNum / 35;
        }
        return pageNum;
    }

    /**
     * 拼接连接
     *
     * @param str
     * @param pageNum
     * @return
     */
    public List<String> joinNextPage(String str, int pageNum) {
        List<String> urlList = new ArrayList<>();
        for (int i = 2; i < pageNum; i++) {
            urlList.add(str + "pn" + i);
        }
        return urlList;
    }
}
