package com.ronxuntech.component.spsm.util;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.util.PathUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameUtil {
    // 单例模式
    private static FileNameUtil fileNameUtil = new FileNameUtil();
    public static final String DOWN_URL_PATH = PathUtil.getClasspath() + "\\urls";

    public static FileNameUtil getInstance() {
        return fileNameUtil;

    }

    /**
     * 将下载的地址作为 文件夹和文件名。通过最后一个/ 来区分
     *
     * @param annexUrl
     * @return 文件路径和文件名（不包括后缀）的一个map
     * @throws MalformedURLException
     */
    public Map<String, Object> findDirAndFileName(String annexUrl, WebInfo web) throws MalformedURLException {
        Map<String, Object> map = new HashMap<String, Object>();
        int index = annexUrl.lastIndexOf("/");
        // 文件名
        String fileName = annexUrl.substring(index + 1);

        // 网站/标题作为文件夹 清除特殊字符
        String regex = "[:/|*?%<>\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(web.getSeed());
        String webUrl = matcher.replaceAll("").trim();
        // 附件替换后的相对路径
        String fileDir = "uploadFiles/spsm/" + webUrl + "/";

        map.put("fileName", fileName);
        map.put("fileDir", fileDir);
        return map;
    }

    /**
     * 生成文档文件名 时间戳 +生成5位数的随机数 作为doc或者其他文件的文件名
     *
     * @return
     */
    public String getFileName() {
        String fileName = "";
        fileName = "" + System.currentTimeMillis() + (int) (Math.random() * (99999 - 10000 + 1) + 10000) + ".";
        return fileName;
    }

    /**
     * 先判断网页上抓到的链接，判断../ 出现的个数 如果前边含有 ./ / ../ ../../ 或没有
     * 分别截取网页地址的部分。前面两个和没有直接去掉网页的最后加上 / 后面的 如果是 ../ 则去掉倒数第1个 / 后的。
     * 并且加上传来的url最后的。同理，../../ 则是去掉倒数第2个 /后的。 tergetUrl 拼接url(主要用于ajax分页和
     * 抓取到相对路径时使用) htmlUrl(页面正则匹配到的，) pageUrl 是当前页面的地址
     *
     * @param htmlUrl,
     * @param pageUrl
     * @return
     * @throws MalformedURLException
     */
    public String getTargetUrl(String htmlUrl, String pageUrl) {
        String targetHmtlUrl = htmlUrl;
        String targetPageUrl = pageUrl;
        String targetUrl = "";
        int i = getTheTimes(htmlUrl);
        targetPageUrl = delLastSlashToEnd(targetPageUrl);
        targetHmtlUrl = delStartWithSlash(targetHmtlUrl);

        if (htmlUrl.startsWith("/")) {
            try {
                targetPageUrl = getBasePath(targetPageUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        for (int j = 0; j < i; j++) {
            targetPageUrl = delLastSlashToEnd(targetPageUrl);
            targetHmtlUrl = delStartWithSlash(targetHmtlUrl);
        }
        targetUrl = targetPageUrl + "/" + targetHmtlUrl;
        return targetUrl;
    }

    /**
     * 统计 ../出现的次数
     *
     * @return times
     */
    public int getTheTimes(String url) {
        Matcher match = Pattern.compile("\\.\\./").matcher(url);
        int times = 0;
        while (match.find()) {
            times++;
        }
        return times;
    }

    /**
     * 去掉当前页面的url 最后一个 /及以后的字符 example : www.baidu.com/h/b 则返回 www.baidu.com/h
     *
     * @param pageUrl
     * @return
     */
    public String delLastSlashToEnd(String pageUrl) {
        int i = pageUrl.lastIndexOf("/");
        pageUrl = pageUrl.substring(0, i);
        return pageUrl;
    }

    /**
     * 删除网页上抓取到的连接中 ../ 或 / 或 ./ example : ../../baidu.com --> baidu.com
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    public String delStartWithSlash(String url) {
        String target = url;
        if (url.startsWith("/")) {
            target = url.substring(1);
        } else if (url.startsWith("./")) {
            target = url.substring(2);
        } else if (url.startsWith("../")) {
            target = target.substring(3);
        }
        return target;
    }

    /**
     * 得到当前url的根路径 example :
     * http://www.cfip.cn:1012/page/fieldtitle2.cbs?resna=lyzs&; -->
     * http://www.cfip.cn:1012
     *
     * @param pageUrl
     * @return
     * @throws MalformedURLException
     */
    public String getBasePath(String pageUrl) throws MalformedURLException {
        String baseUrl = "";
        URL u = new URL(pageUrl);
        if (u.getPort() > 0) {
            baseUrl = u.getProtocol() + "://" + u.getHost() + ":" + u.getPort();
        } else {
            baseUrl = u.getProtocol() + "://" + u.getHost();
        }

        return baseUrl;
    }

    /**
     * 初始化下载文件的位置
     *
     * @param web
     * @return
     */
    public static String initDownloadFileDir(WebInfo web) {
        String regex = "[:/|*?%<>\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(web.getSeed());
        String webUrl = matcher.replaceAll("").trim();
        String downloadFileDir = PathUtil.getClasspath() + "uploadFiles/spsm/" + webUrl + "/";
        return downloadFileDir;
    }
}
