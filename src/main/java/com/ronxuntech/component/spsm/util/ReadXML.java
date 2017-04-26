package com.ronxuntech.component.spsm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ronxuntech.util.PathUtil;
import sun.applet.Main;

/**
 * 读取配置文件，并且返回一个map
 * Created by tongbu on 2016/10/8 0008.
 *
 * 现在已经无用 2017-4-20
 */
public class ReadXML {
    public List<HashMap>  ResolveXml() throws Exception{
        List<HashMap> listmap=new ArrayList<HashMap>();
        SAXReader reader = new SAXReader();
        //获取项目资源路径
        String filePath=PathUtil.getClassResources();
        System.out.println("filepath:"+filePath);
        File file = new File("C:\\software\\eclipse\\workspace\\spsm_console\\spsm_console\\target\\classes\\spsm\\URL.xml");
        
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        
        for (Element child : childElements) {
            HashMap<String ,String> hsmap=new HashMap<String ,String>();
            //获得种子
            hsmap.put("seed",child.elementText("seed"));
            //获取url过滤的区域
            hsmap.put("urlTag",child.elementText("urlTag"));
            //获得url的正则表达式
            hsmap.put("urlRex",child.elementText("urlRex").replaceAll("&","&amp;"));
            //标签列表
            List list = child.element("tagList").elements();
            for(int i=0;i<list.size();i++){
                Element element=(Element) list.get(i);
                hsmap.put("tag"+(i+1),element.getStringValue());
            }
            //图片
            List listImg = child.element("img").elements();
            Element elementImg1=(Element) listImg.get(0);
            hsmap.put("hasImg",elementImg1.getStringValue());//读取到的是字符串,在用的时候需要转成boolean类型
            Element elementImg2=(Element) listImg.get(1);
            hsmap.put("imgRegex",elementImg2.getStringValue());
            Element elementImg3=(Element) listImg.get(2);
            hsmap.put("imgTag",elementImg3.getStringValue());
            
            //文件
            List listDoc = child.element("doc").elements();
            Element elementDoc1=(Element) listDoc.get(0);
            hsmap.put("hasDoc",elementDoc1.getStringValue());
            Element elementDoc2=(Element) listDoc.get(1);
            hsmap.put("docRegex",elementDoc2.getStringValue().replaceAll("&","&amp;"));
            Element elementDoc3=(Element) listDoc.get(2);
            hsmap.put("docTag",elementDoc3.getStringValue());
            
            
            //分页
            
            List listPage=child.element("page").elements();
            Element elementTotalPage=(Element) listPage.get(0);
            hsmap.put("totalPage", elementTotalPage.getStringValue());
            
            Element pageAjaxTag=(Element) listPage.get(1);
            hsmap.put("pageAjaxTag", pageAjaxTag.getStringValue());
            
            Element pageGetTag=(Element) listPage.get(2);
            hsmap.put("pageGetTag", pageGetTag.getStringValue());
            
            Element pagePostTag=(Element) listPage.get(3);
            hsmap.put("pagePostTag", pagePostTag.getStringValue());
            
            Element elementPageMethod=(Element) listPage.get(4);
            hsmap.put("pageMethod", elementPageMethod.getStringValue());
            
            Element elementPageEncoding=(Element) listPage.get(5);
            hsmap.put("pageEncoding", elementPageEncoding.getStringValue());

            List datatypeList=child.element("datatype").elements();
            Element databaseType=(Element) datatypeList.get(0);
            hsmap.put("databaseType", databaseType.getStringValue());

            Element navbarType=(Element) datatypeList.get(1);
            hsmap.put("navbarType", navbarType.getStringValue());

            Element listType=(Element) datatypeList.get(2);
            hsmap.put("listType", listType.getStringValue());

            Element sublistType=(Element) datatypeList.get(3);
            hsmap.put("sublistType", sublistType.getStringValue());

            listmap.add(hsmap);
        }
        return listmap;
    }


    public static void main(String[] args) throws Exception {
        ReadXML readXML = new ReadXML();
        List<HashMap> listmap = readXML.ResolveXml();
        for (HashMap<String,String> map:listmap
             ) {
            System.out.println(map.get("urlRex").toString());
        }
    }
}
