package com.ronxuntech.component.spsm;

import com.ronxuntech.component.spsm.util.ReadXML;
import com.ronxuntech.util.PageData;
import com.sun.tools.corba.se.idl.StringGen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WebInfo {
    private String seed;// 种子
    private String urlTag; //要过滤的url 所在的区域 (xpath)
    private String urlRex;// 要抓取的url正则表达式
    private List<String> list;// 标签列表
    private String imgRegex; // 过滤图片的正则表达式
    private String docRegex; // 过滤文档的正则表达式
    private String imgTag; // 提取图片的 标签
    private String docTag; // 提取文档的标签
    private boolean hasImg; // 是否存在图片
    private boolean hasDoc; // 是否存在文档
    private String totalPage; // 总页数
    private String pageAjaxTag; // 异步标签
    private String pageMethod; // 分页方式
    private String pageGetTag;  // get
    private String pagePostTag;  //post
    private String typeId;  //传进来的类型id
    private String pageEncoding;  //网页编码格式

    private String databaseType;
    private String navbarType;
    private String listType;
    private String sublistType;
    private String createTime;
    private static ReadXML readXML = new ReadXML();

    public String getPageEncoding() {
        return pageEncoding;
    }

    public void setPageEncoding(String pageEncoding) {
        this.pageEncoding = pageEncoding;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getNavbarType() {
        return navbarType;
    }

    public void setNavbarType(String navbarType) {
        this.navbarType = navbarType;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getSublistType() {
        return sublistType;
    }

    public void setSublistType(String sublistType) {
        this.sublistType = sublistType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPageAjaxTag() {
        return pageAjaxTag;
    }

    public void setPageAjaxTag(String pageAjaxTag) {
        this.pageAjaxTag = pageAjaxTag;
    }

    public String getPageGetTag() {
        return pageGetTag;
    }

    public void setPageGetTag(String pageGetTag) {
        this.pageGetTag = pageGetTag;
    }

    public String getPagePostTag() {
        return pagePostTag;
    }

    public void setPagePostTag(String pagePostTag) {
        this.pagePostTag = pagePostTag;
    }


    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPageMethod() {
        return pageMethod;
    }

    public void setPageMethod(String pageMethod) {
        this.pageMethod = pageMethod;
    }

    public String getImgTag() {
        return imgTag;
    }

    public void setImgTag(String imgTag) {
        this.imgTag = imgTag;
    }

    public String getDocTag() {
        return docTag;
    }

    public void setDocTag(String docTag) {
        this.docTag = docTag;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getUrlRex() {
        return urlRex;
    }

    public void setUrlRex(String urlRex) {
        this.urlRex = urlRex;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getImgRegex() {
        return imgRegex;
    }

    public void setImgRegex(String imgRegex) {
        this.imgRegex = imgRegex;
    }

    public String getDocRegex() {
        return docRegex;
    }

    public void setDocRegex(String docRegex) {
        this.docRegex = docRegex;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }

    public boolean isHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public void setUrlTag(String urlTag) {
        this.urlTag = urlTag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 读取xml
     * @return
     * @throws Exception
     */
    public  static  List<WebInfo> init() throws Exception {

        final List list = readXML.ResolveXml();
        List<WebInfo> webList = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            HashMap<String, String> hashMap = (HashMap<String, String>) list.get(i);
//            System.out.println("init:"+hashMap.toString());
            WebInfo web = new WebInfo();
//            list.get(i);

            String seed = hashMap.get("seed");
            String urlTag = hashMap.get("urlTag");
            if (urlTag.equals("") || urlTag == null) {
                urlTag = "//body";
            }
            String urlRex = hashMap.get("urlRex");
            String tag1 = hashMap.get("tag1");
            String tag2 = hashMap.get("tag2");
            // 文字类抓取
            // 用web类来传值
            // final Web web = new Web();
            web.setSeed(seed.trim().replaceAll("&amp;","&"));
            web.setUrlTag(urlTag.replaceAll("&amp;","&"));
            web.setUrlRex(urlRex.trim().replaceAll("&amp;","&"));
            List<String> taglist = new ArrayList<String>();
            taglist.add(tag1.trim().replaceAll("&amp;","&"));
            taglist.add(tag2.trim().replaceAll("&amp;","&"));
            web.setList(taglist);

            // 图片爬取
            boolean hasImg = Boolean.parseBoolean(hashMap.get("hasImg"));
            String imgRegex = hashMap.get("imgRegex");
            String imgTag = hashMap.get("imgTag");
            web.setImgTag(imgTag.trim().replaceAll("&amp;","&"));
            web.setImgRegex(imgRegex.trim().replaceAll("&amp;","&"));
            web.setHasImg(hasImg);
            // 文件
            boolean hasDoc = Boolean.parseBoolean(hashMap.get("hasDoc"));
            String DocRegex = hashMap.get("docRegex");
            String docTag = hashMap.get("docTag");
            web.setDocTag(docTag.trim().replaceAll("&amp;","&"));
            web.setDocRegex(DocRegex.trim().replaceAll("&amp;","&"));
            web.setHasDoc(hasDoc);

            // 分页
            String totalPage = hashMap.get("totalPage");
            String pageAjaxTag = hashMap.get("pageAjaxTag");
            String pageGetTag = hashMap.get("pageGetTag");
            String pagePostTag = hashMap.get("pagePostTag");
            String pageMethod = hashMap.get("pageMethod");
            String pageEncoding = hashMap.get("pageEncoding");
            web.setTotalPage(totalPage.replaceAll("&amp;","&"));
            web.setPageMethod(pageMethod.trim().replaceAll("&amp;","&"));
            web.setPageAjaxTag(pageAjaxTag.trim().replaceAll("&amp;","&"));
            web.setPageGetTag(pageGetTag.trim().replaceAll("&amp;","&"));
            web.setPagePostTag(pagePostTag.trim().replaceAll("&amp;","&"));
            web.setPageEncoding(pageEncoding.trim().replaceAll("&amp;","&"));

            String databaseType = hashMap.get("databaseType");
            String navbarType = hashMap.get("navbarType");
            String listType = hashMap.get("listType");
            String sublistType = hashMap.get("sublistType");
            web.setDatabaseType(databaseType.replaceAll("&amp;","&"));
            web.setNavbarType(navbarType.replaceAll("&amp;","&"));
            web.setListType(listType.replaceAll("&amp;","&"));
            web.setSublistType(sublistType.replaceAll("&amp;","&"));
            web.setCreateTime(sdf.format(new Date()).toString());
            webList.add(web);
        }
        return  webList;
    }
    /**
     * 初始化web,将传递来的种子网页
     *
     * @param pd
     * @param
     * @return String typeId
     * @throws Exception
     */
    public static WebInfo init(PageData pd) throws Exception {
        WebInfo web = new WebInfo();
        web.setSeed(pd.getString("SEED"));
        String urlTag = pd.getString("URLTAG");
        if (urlTag.equals("") || urlTag == null) {
            urlTag = "//body";
        }

        web.setUrlTag(urlTag);
        web.setUrlRex(pd.getString("URLREX"));
        List<String> taglist = new ArrayList<String>();
        taglist.add(pd.getString("TITLETAG"));
        taglist.add(pd.getString("CONTENTTAG"));
        web.setList(taglist);

        web.setImgTag(pd.getString("IMGTAG"));
        web.setImgRegex(pd.getString("IMGREGEX"));
        web.setHasImg(checkAnnex(pd.getString("HASIMG")));

        web.setDocTag(pd.getString("DOCTAG"));
        web.setDocRegex(pd.getString("DOCREGEX"));
        web.setHasDoc(checkAnnex(pd.getString("HASDOC")));

        web.setTotalPage(pd.getString("TOTALPAGE"));
        web.setPageMethod(pd.getString("PAGEMETHOD"));
        web.setPageAjaxTag(pd.getString("PAGEAJAXTAG"));
        web.setPageGetTag(pd.getString("PAGEGETTAG"));
        web.setPagePostTag(pd.getString("PAGEPOSTTAG"));
        web.setPageEncoding(pd.getString("PAGEENCODING"));

        web.setDatabaseType(pd.getString("DATABASETYPEID"));
        web.setNavbarType(pd.getString("NAVBARTYPEID"));
        web.setListType(pd.getString("LISTTYPEID"));
        web.setSublistType(pd.getString("SUBLISTTYPEID"));

        System.out.println("webinfo:"+web.toString());
        return web;
    }

    /**
     * 通过传递来的字符串，分割数据类型id.
     *
     * @param typeIds
     * @return
     */
    public List<String> getTypeID(String typeIds) {
        if (!(typeIds.equals(""))) {
            List<String> typeList = new ArrayList<String>();
            String typeId[] = typeIds.split(",");
            for (int i = 0; i < typeId.length; i++) {
                typeList.add(typeId[i]);
            }
            return typeList;
        } else {
            return null;
        }

    }

    @Override
    public String toString() {
        return "WebInfo [seed=" + seed + ",urlTag=" + urlTag + " urlRex=" + urlRex + ", list=" + list + ", imgRegex=" + imgRegex
                + ", docRegex=" + docRegex + ", imgTag=" + imgTag + ", docTag=" + docTag + ", hasImg=" + hasImg
                + ", hasDoc=" + hasDoc + ", totalPage=" + totalPage + ", pageAjaxTag=" + pageAjaxTag + ", pageMethod="
                + pageMethod + ", pageGetTag=" + pageGetTag + ", pagePostTag=" + pagePostTag + ", typeId=" + typeId
                + ", pageEncoding=" + pageEncoding + ", databaseType=" + databaseType + ", navbarType=" + navbarType
                + ", listType=" + listType + ", sublistType=" + sublistType + "]";
    }

    /**
     * 得到 true false
     * @param str
     * @return
     */
    public static boolean checkAnnex(String str){
        if(str.equals("1") || str.equalsIgnoreCase("true")){
            return true;
        }else {
            return false;
        }
    }

}
