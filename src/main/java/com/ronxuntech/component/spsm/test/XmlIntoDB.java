package com.ronxuntech.component.spsm.test;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.service.spsm.databasetype.DataBaseTypeManager;
import com.ronxuntech.service.spsm.listtype.ListTypeManager;
import com.ronxuntech.service.spsm.navbartype.NavBarTypeManager;
import com.ronxuntech.service.spsm.sublisttype.SubListTypeManager;
import com.ronxuntech.service.spsm.webinfo.WebInfoManager;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by angrl on 2017/3/28.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/spring/ApplicationContext.xml")
public class XmlIntoDB {
    private WebInfo web;
    @Resource
    private WebInfoManager webInfoService;
    @Resource
    private DataBaseTypeManager databasetypeService;
    @Resource
    private NavBarTypeManager navbartypeService;
    @Resource
    private ListTypeManager listtypeService;
    @Resource
    private SubListTypeManager sublisttypeService;


    private Map datatypeMap = new HashMap();

    public XmlIntoDB(){
    }

    @Test
    public void test() throws Exception {   
        XmlIntoDB xmlIntoDB = new XmlIntoDB();
        System.out.println("database:"+databasetypeService);
//        List<PageData> list =  databasetypeService.listAll(new PageData());
//        System.out.println(list.size());
        initDatatype();
        List<WebInfo> webInfoList = new ArrayList<>();
        try {
            webInfoList = WebInfo.init();
            insertIntoDB(webInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("webinfoList:"+webInfoList.size());
        
    }

    public void insertIntoDB(List<WebInfo> webInfoList) throws Exception {
    	System.out.println("insertintoDb:");
        for (int i = 0; i < webInfoList.size(); i++) {
            web = webInfoList.get(i);
            PageData pd = new PageData();
            pd.put("WEBINFO_ID", UuidUtil.get32UUID());
            pd.put("SEED", web.getSeed());
            pd.put("URLTAG",web.getUrlTag());
            pd.put("URLREX", web.getUrlRex());
            pd.put("TITLETAG", web.getList().get(0));
            pd.put("CONTENTTAG", web.getList().get(1));
            pd.put("HASIMG", web.isHasImg());
            pd.put("IMGREGEX", web.getImgRegex());
            pd.put("IMGTAG", web.getImgTag());
            pd.put("HASDOC", web.isHasDoc());
            pd.put("DOCTAG", web.getDocTag());
            pd.put("DOCREGEX", web.getDocRegex());
            pd.put("TOTALPAGE", web.getTotalPage());
            pd.put("PAGEAJAXTAG", web.getPageAjaxTag());
            pd.put("PAGEGETTAG", web.getPageGetTag());
            pd.put("PAGEPOTTAG",web.getPagePostTag());
            pd.put("PAGEMETHOD", web.getPageMethod());
            pd.put("PAGEENCODING", web.getPageEncoding());
            System.out.println("web.getdatabasetyep:"+web.getDatabaseType()+"--     "+web.getSeed());
            String sublistId=""; 
            if(StringUtils.isNotEmpty(web.getSublistType())){
            	sublistId = datatypeMap.get(web.getSublistType()).toString();
            }
            String listId=""; 
            if(StringUtils.isNotEmpty(web.getListType()) && datatypeMap.containsKey(web.getListType())){
            	listId = datatypeMap.get(web.getListType()).toString();
            }            
            
            pd.put("DATABASETYPEID", datatypeMap.get(web.getDatabaseType()).toString());
            pd.put("NAVBARTYPEID", datatypeMap.get(web.getNavbarType()).toString());
            pd.put("LISTTYPEID", listId);
            pd.put("SUBLISTID", sublistId);
            pd.put("CREATETIME",web.getCreateTime());
            try {
                webInfoService.save(pd);
                System.out.println("sava success！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void initDatatype() throws Exception {
        List<PageData> databaseType = new ArrayList<>();
        databaseType = databasetypeService.listAll(new PageData());
        List<PageData> navbarType = navbartypeService.listAll(new PageData());
        List<PageData> listType = listtypeService.listAll(new PageData());
        List<PageData> sublistType =sublisttypeService.listAll(new PageData());


        for(int i =0;i<databaseType.size();i++){
            datatypeMap.put(databaseType.get(i).getString("DATABASETYPENAME"),databaseType.get(i).getString("DATABASETYPE_ID"));
        }
        for(int i=0;i<navbarType.size();i++){
            datatypeMap.put(navbarType.get(i).getString("NAVBARTYPENAME"),navbarType.get(i).getString("NAVBARTYPE_ID"));
        }
        for(int i=0;i<listType.size();i++){
            datatypeMap.put(listType.get(i).getString("LISTTYPENAME"),listType.get(i).getString("LISTTYPE_ID"));
        }
        for(int i=0;i<sublistType.size();i++){
            datatypeMap.put(sublistType.get(i).getString("SUBLISTTYPENAME"),sublistType.get(i).getString("SUBLISTTYPE_ID"));
        }
       System.out.println("datatyemap:"+datatypeMap.toString());
    }
}
