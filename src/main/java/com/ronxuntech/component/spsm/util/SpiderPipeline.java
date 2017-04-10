package com.ronxuntech.component.spsm.util;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

public class SpiderPipeline implements Pipeline {

    //	private AnnexUrlManager annexurlService;
    private TargetUrlManager targeturlService;
    //	private SeedUrlManager seedurlService;
    private AnnexUtil annexUtil = AnnexUtil.getInstance();
    private WebInfo web;
    private SpiderManager spiderService;

    //构造函数
    public SpiderPipeline() {

    }

    public SpiderPipeline(WebInfo web) {
        this.web = web;
        spiderService = (SpiderManager) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlManager) SpringBeanFactoryUtils.getBean("targeturlService");
    }


    // 先得到抓取的东西， 然后再通过处理，存入数据库
    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            String pageUrl = resultItems.get("pageUrl").toString();
            PageData pd = resultItems.get("pd");
            // 如果怕去的 内容是空，则不跳出。不存入数据库。
            if (!("".equals(resultItems.get("content").toString().trim()))) {
                if (web.getSeed().contains("http://crop.agridata.cn/96-014/default.html") && pageUrl.toLowerCase().contains("cgris")) {
                    return;
                }
                // 将爬取的数据放入数据库
                spiderService.save(pd);
                // 当前页面不是种子页面，通过查询，然后修改该url的状态。
                if (!(pageUrl.equals(web.getSeed()))) {
                    annexUtil.updateTargetStatus(pageUrl, targeturlService);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
