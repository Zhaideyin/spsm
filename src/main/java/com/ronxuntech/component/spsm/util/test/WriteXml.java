package com.ronxuntech.component.spsm.util.test;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.util.PathUtil;

public class WriteXml {

	private WebInfo web;
	
	public WebInfo getWeb() {
		return web;
	}

	public void setWeb(WebInfo web) {
		this.web = web;
	}

	public void writeDom() throws DocumentException, IOException{
		  //构造dom树  
		 SAXReader reader = new SAXReader();
        //获取项目资源路径
		String filePath=PathUtil.getClassResources();
        Document document  = DocumentHelper.createDocument();  
        
        //根目录
        Element roote = document.addElement("webList");  
        //根
        Element webpage = roote.addElement("webpage");
        //子节点
        Element seed = webpage.addElement("seed"); 
        seed.setText(web.getSeed().trim());
        
        Element urlRex = webpage.addElement("urlRex");  
        urlRex.setText(web.getUrlRex().trim());
        //taglist 的子节点 
        Element tagList = webpage.addElement("tagList");
       	Element tag1=tagList.addElement("tag1");
       	Element tag2=tagList.addElement("tag1");
       	tag1.setText(web.getList().get(0).trim());
       	tag2.setText(web.getList().get(1).trim());
       	
        Element img = webpage.addElement("img"); 
        Element hasImg = img.addElement("img");
        Element  imgRegex =img.addElement("imgRegex");
        Element imgTag =img.addElement("imgTag");
        hasImg.setText(web.isHasImg()+"");
        imgRegex.setText(web.getImgRegex().trim());
        imgTag.setText(web.getImgTag().trim());
        
        Element doc = webpage.addElement("doc");
        Element hasDoc = doc.addElement("hasDoc");
        Element  docRegex =doc.addElement("docRegex");
        Element docTag =doc.addElement("docTag");
        hasDoc.setText(web.isHasDoc()+"");
        docRegex.setText(web.getDocRegex().trim());
        docTag.setText(web.getDocTag());
        
        Element page = webpage.addElement("page");  
        Element totalPage = page.addElement("totalPage");
        Element pageAjaxTag = page.addElement("pageAjaxTag");
        Element pageGetTag = page.addElement("pageGetTag");
        Element pagePostTag = page.addElement("pagePostTag");
        Element pageMethod = page.addElement("pageMethod");
        Element pageEncoding = page.addElement("pageEncoding");
        totalPage.setText(web.getTotalPage()+"".trim());
        pageAjaxTag.setText(web.getPageAjaxTag().trim());
        pageGetTag.setText(web.getPageGetTag().trim());
        pagePostTag.setText(web.getPagePostTag().trim());
        pageMethod.setText(web.getPageMethod().trim());
        pageEncoding.setText(web.getPageEncoding().trim());
        
          
        //设置字符编码方式  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        format.setEncoding("utf-8");  
  
        //开始写  
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(filePath+"spsm/URL.xml"),format);  
        xmlWriter.write(document);  
        xmlWriter.close();  
	}
	
	public static void main(String[] args) throws Exception {
		WriteXml write=new WriteXml();
		write.writeDom();
	}
}
