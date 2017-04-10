package com.ronxuntech.component.spsm.util;

import java.util.ArrayList;
import java.util.List;

public class NextPageUrlUtil {

    private static NextPageUrlUtil nextPageUrlUtil = new NextPageUrlUtil();

    public static NextPageUrlUtil getInstance() {
        return nextPageUrlUtil;
    }

    /**
     * 组装下一页的链接。针对 http://rdjc.lknet.ac.cn/list.php 这一系列的网站
     *
     * @param
     * @param str
     * @param totalpage
     */
    public List<String> jointNextpage(String str, int totalpage) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        int n = seed.indexOf("?");
        String id = seed.substring(n + 1);
        String suffex = "&" + id + "&keyword=&select=";
        String prefix = "http://rdjc.lknet.ac.cn/list.php?currpage=";
        for (int i = 2; i < totalpage; i++) {
            urlList.add(prefix + i + suffex);
        }
//		page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * 组装下一页的链接，主要用于http://catf.agri.cn 、 http://cxpt.agri.gov.cn
     * http://www.moa.gov.cn http://pfscnew.agri.gov.cn http://www.tea.agri.cn
     * 这一类的链接
     *
     * @param
     * @param str
     * @param totalpage
     */
    public List<String> jointNextpage2(String str, int totalpage) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i < totalpage; i++) {
            urlList.add(seed + "index_" + i + ".htm");
        }
//		page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * 组装 http://www.caas.net.cn 一类的网站链接
     *
     * @param
     * @param str
     * @param totalpage
     */
    public List<String> jointNextpage3(String str, int totalpage) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i < totalpage; i++) {
            urlList.add(seed + "index" + i + ".shtml");
        }
//		page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * http://finance.aweb.com.cn 等网站
     *
     * @param
     * @param str
     * @param totalpage
     */

    public List<String> jointNextpage4(String str, int totalpage) {
        List<String> urlList = new ArrayList<>();
        String seed = str;
        for (int i = 1; i <= totalpage; i++) {
            urlList.add(seed + "index_" + i + ".html");
        }
//		  page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * http://www.sczyw.com
     *
     * @param
     * @param str
     * @param totalpage
     */
    public List<String> jointNextpage5(String str, int totalpage) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        for (int i = 2; i <= totalpage; i++) {
            urlList.add(seed + "&currpage=" + i);
        }
//			page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * http://www.3456.tv/
     *
     * @param str
     * @param totalpage
     * @return
     */
    public List<String> jointNextpage6(String str, int totalpage) {
        System.out.println("totalPage--------:" + totalpage);
        String seed = str.replaceAll("\\.html", "");
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i <= totalpage; i++) {
            urlList.add(seed + "_" + i + ".html");
        }
//			page.addTargetRequests(urlList);
        return urlList;
    }

    /**
     * http://www.agrione.cn
     *
     * @param
     * @param str
     * @param totalpage
     */
    public List<String> jointNextpage7(String str, int totalpage) {
        String seed = str;
        List<String> urlList = new ArrayList<>();
        String prf = "index-";
        String suf = ".html";
        for (int i = 1; i < totalpage; i++) {
            urlList.add(seed + prf + i + suf);
        }
//			page.addTargetRequests(urlList);
        return urlList;
    }
}
