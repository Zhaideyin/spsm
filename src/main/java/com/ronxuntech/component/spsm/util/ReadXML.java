package com.ronxuntech.component.spsm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.regexp.recompile;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ronxuntech.util.PathUtil;
/**
 * 读取配置文件，并且返回一个map
 * Created by tongbu on 2016/10/8 0008.
 */
public class ReadXML {
    public List<HashMap>  ResolveXml() throws Exception{
        List<HashMap> listmap=new ArrayList<HashMap>();
        SAXReader reader = new SAXReader();
        //获取项目资源路径
        String filePath=PathUtil.getClassResources();
        File file = new File(filePath+"spsm/URL.xml");
        
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        
        for (Element child : childElements) {
            HashMap<String ,String> hsmap=new HashMap<String ,String>();
            //获得种子
            System.out.println(child.elementText("seed"));
            hsmap.put("seed",child.elementText("seed"));
            //获得url的正则表达式
            hsmap.put("urlRex",child.elementText("urlRex"));
            //标签列表
            List list = child.element("tagList").elements();
            for(int i=0;i<list.size();i++){
                Element element=(Element) list.get(i);
                hsmap.put("tag"+(i+1),element.getStringValue());
                System.out.println(element.getStringValue());
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
            hsmap.put("docRegex",elementDoc2.getStringValue());
            Element elementDoc3=(Element) listImg.get(2);
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
            listmap.add(hsmap);
        }
        return listmap;
    }
    
    /**
     * 测试
     * @param args
     */
    
    public static void main(String[] args) {
        ReadXML readXML=new ReadXML();
        try {
            readXML.ResolveXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
}
