package com.ronxuntech.component.spsm.spider;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.annexurl.impl.AnnexUrlService;
import com.ronxuntech.service.spsm.crop.CropManager;
import com.ronxuntech.service.spsm.crop.impl.CropService;
import com.ronxuntech.service.spsm.croptype.CropTypeManager;
import com.ronxuntech.service.spsm.croptype.impl.CropTypeService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.spider.impl.SpiderService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import com.ronxuntech.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 特殊网站：http://www.cgris.net/photobase/default.html
 * create by angrl
 * date :2017-3-20
 */

public class CgrisSpider implements PageProcessor {

    private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1500).setTimeOut(20000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .setCharset("gb2312");

    private String regex = "[\u4e00-\u9fa5]+";
    private Pattern pattern = Pattern.compile(regex);
//    private Map<String, String> cropTypeMap = new HashMap<>();

    private CropTypeManager croptypeService;
    private CropManager cropService;
    private AnnexUrlManager annexurlService;
    private SpiderManager spiderService;
    private TargetUrlManager targeturlService;

    private AnnexUtil annexUtil = AnnexUtil.getInstance();
    private WebInfo web;
    //当前网页作物信息

//        new HashMap<>();

    private String seedUrlId;

    public CgrisSpider() {

    }

    public CgrisSpider(WebInfo web, String seedUrlId) {
        this.web = web;
        this.seedUrlId = seedUrlId;
        spiderService = (SpiderService) SpringBeanFactoryUtils.getBean("spiderService");
        targeturlService = (TargetUrlService) SpringBeanFactoryUtils.getBean("targeturlService");
//		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
        annexurlService = (AnnexUrlService) SpringBeanFactoryUtils.getBean("annexurlService");
        croptypeService = (CropTypeService) SpringBeanFactoryUtils.getBean("croptypeService");
        cropService = (CropService) SpringBeanFactoryUtils.getBean("cropService");
        getALlCropType();
    }

    // 附件工具
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        Html html = page.getHtml();
        List<String> links = html.xpath("/html/body/div[3]/center/table/tbody/tr/td/div/center/table/tbody|/html/body/table[2]/tbody/tr/td/div/center/table").links().all();

        String pageUrl = "";
        //url解码
        try {
            pageUrl = URLDecoder.decode(page.getUrl().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,String> cropMap = getInitData(pageUrl);
        //插入数据库
        if (pageUrl != null) {
            getInitData(pageUrl);
            insertCropType(cropMap);
            Map<String, String> cropTypeMap = getALlCropType();
            //抓取数据
            String title = cropMap.get("cropName").toString();
            String contents = html.xpath("/html/body/table[2]/tbody/tr/td/div/center/table/tbody/tr/td/div/table/").toString();
            if (null != contents && "" != contents) {
                page.putField("content", contents.trim());
                page.putField("title", title);
                page.putField("pageUrl", pageUrl);
                System.out.println("process:croptypemap:" + cropTypeMap.toString());

                String cropTypeName = cropMap.get("cropTypeName").toString();
                String cropTypeId = cropTypeMap.get(cropTypeName).toString();
                String cropName = cropMap.get("cropName").toString();
                String breedName = cropMap.get("breedName").toString();
                if (breedName.contains("page")) {
                    page.setSkip(true);
                }
                annexUtil.annexSaveAndDown(page, contents, title, cropTypeId, cropName, breedName, web, annexurlService);
            }
            if (page.getResultItems().get("content") == null || page.getResultItems().get("content").equals("")) {
                page.setSkip(true);
            }
        }
        page.addTargetRequests(links);
    }

    /**
     * 过滤包含有page的无用页面
     *
     * @param links
     */
    public void linksFilter(List<String> links) {
        System.out.println(links.size());
        System.out.println(links.toString());
        for (int i = links.size() - 1; i >= 0; i--) {
            if (links.get(i).toString().contains("page")) {
//                System.out.println("i=="+i);
                links.remove(i);
            }
        }
    }

    /**
     * 查询出所有作物类型
     *
     * @return cropTypeMap
     * @throws Exception
     */
    public Map<String, String> getALlCropType() {
        Map<String, String> cropTypeMap = new HashMap<>();
        List<PageData> list = new ArrayList<>();
        try {
            list = croptypeService.listAll(new PageData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将Name作为键，id作为value
        for (PageData pd : list) {
            cropTypeMap.put(pd.getString("CROPTYPENAME"), pd.getString("CROPTYPE_ID"));
        }
        System.out.println("getallcropType cropTypeMap:" + cropTypeMap);
        return cropTypeMap;
    }

    /**
     * 通过作物类型查询出所有的作物名
     *
     * @param cropTypeId
     * @return cropMap
     * @throws Exception
     */
    public Map<String, String> getAllCrop(String cropTypeId) throws Exception {
        Map<String, String> allCropMap = new HashMap<>();
        PageData pd = new PageData();
        pd.put("CROPTYPE_ID", cropTypeId);
        List<PageData> list = cropService.listFindByCropTypeID(pd);
        for (PageData pageData : list) {
            allCropMap.put(pageData.getString("CROPNAME"), pageData.getString("CROP_ID"));
        }
        return allCropMap;
    }

    /**
     * 将抓到的作物类别，作物名等 插入数据库
     *
     * @param cropMap
     */
    public void insertCropType(Map<String, String> cropMap) {
        String breedName = cropMap.get("breedName").toString();
        String cropName = cropMap.get("cropName").toString();
        String cropTypeName = cropMap.get("cropTypeName").toString();
        //得到最新的cropTypeMap；
//        getALlCropType();
        System.out.println("cropMap:" + cropMap.toString());
        //三个参数都不为空才执行存存储
        if (StringUtils.isNotEmpty(breedName) && StringUtils.isNotEmpty(cropName) && StringUtils.isNotEmpty(cropTypeName)) {
            Map<String, String> cropTypeMap = getALlCropType();
            // 数据库中是否存在该作物类型,如果存在，得到id,通过id查询作物，
            if (cropTypeMap.containsKey(cropTypeName)) {
                //得到改作物类型的id.然后通过id 查询出所有的作物。
                String cropType_Id = cropTypeMap.get(cropTypeName).toString();
                System.out.println("cropTypeId:" + cropType_Id);
                try {
                    //重新得到当前作物类型的所有作物、
                    Map<String,String> allCropMap = getAllCrop(cropType_Id);
//                    System.out.println("insertCropType-allcropMap:" + allCropMap.toString());
                    //如果作物不存在，则存入数据库
                    if (!(allCropMap.containsKey(cropName))) {
                        PageData pd1 = new PageData();
                        pd1.put("CROP_ID", UuidUtil.get32UUID());
                        pd1.put("CROPNAME", cropName);
                        pd1.put("CROPTYPEID", cropType_Id);
                        try {
                            cropService.save(pd1);
                            System.out.println("just save Crop!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //不存在作物类型，则插入数据库;然后通过作物类型id 将作物插入数据库。
            } else {
                PageData pd = new PageData();
                String cropType_Id = UuidUtil.get32UUID();
                pd.put("CROPTYPE_ID", cropType_Id);
                pd.put("CROPTYPENAME", cropTypeName);
                try {
                    croptypeService.save(pd);
                    System.out.println("save Croptype");
                    //重新得到map
                    this.getALlCropType();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //存作物
                PageData pd1 = new PageData();
                pd1.put("CROP_ID", UuidUtil.get32UUID());
                pd1.put("CROPNAME", cropName);
                pd1.put("CROPTYPEID", cropType_Id);
                try {
                    cropService.save(pd1);
                    System.out.println("save Crop!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 通过pageUrl 得到 作物的品种名，作物名，作物类型数据
     *
     * @param pageUrl
     * @return
     */
    public Map<String, String>  getInitData(String pageUrl) {
        Map<String, String> cropMap = new HashMap();
        String breedName = "";  //作物品种名
        String cropName = "";  //作物名
        String cropTypeName = "";//作物类型
        //将当前url分割，然后通过循环， 得到想要的作物名，
        String cropType[] = pageUrl.split("/");
        //匹配次数
        int matcherTime = 0;

        for (int i = cropType.length - 1; i > 0; i--) {
            Matcher matcher = pattern.matcher(cropType[i]);
            //赋初始值  作物品种
            if (i == cropType.length - 1) {
                breedName = cropType[i].replaceAll("\\.htm", "").trim();
            }

            if (matcher.find()) {
                matcherTime++;
                //分割后取出的第一个始终放入cropName;
                if (matcherTime == 1) {
                    if (i != cropType.length - 1) {
                        cropName = cropType[i].trim();
                    } else {
                        cropName = cropType[i].trim();
                    }
                }
                //如果第二次得到的，如果循环次数同matcherTime 那么则是作物名。如果不等，则是作物类型。如：蔬菜
                if (matcherTime == 2) {
                    if (i == cropType.length - 2) {
                        cropName = cropType[i].trim();
                    } else {
                        cropTypeName = cropType[i].trim();
                    }
                }
                //如果存在第三次命中，则作物类型为 当前的cropType中对应的值。
                if (matcherTime == 3) {
                    cropTypeName = cropType[i].trim();
                }
                //如果存在第四次命中，则作物类型为当前的cropType中对应的值
                if (matcherTime == 4) {
                    cropTypeName = cropType[i].trim();
                }
            }
        }
        // 如：http://www.cgris.net/photobase/cropphoshop/杂草/page_01.htm
        // http://www.cgris.net/photobase/cropphoshop/杂草/杂草-稗草.htm
        // breedName = 杂草-稗草 cropTypename=杂草 cropName = "";
        if (matcherTime < 2) {
            cropMap.put("cropTypeName", cropName);
            cropMap.put("breedName", breedName);
            cropMap.put("cropName", "");
        } else {
            cropMap.put("cropTypeName", cropTypeName);
            cropMap.put("breedName", breedName);
            cropMap.put("cropName", cropName);
        }
        return cropMap;
    }
}
