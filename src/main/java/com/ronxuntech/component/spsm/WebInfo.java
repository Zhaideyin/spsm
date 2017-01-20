package com.ronxuntech.component.spsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ronxuntech.component.spsm.util.ReadXML;

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

	/**
	 * 初始化web,将传递来的种子网页
	 * @param seedUrl
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public static WebInfo init(String seedUrl, String typeId) throws Exception {
		WebInfo web=new WebInfo();
		web.typeId = typeId;
		ReadXML readXML = new ReadXML();
		final List list = readXML.ResolveXml();
		//将传递来的typeID,对应各个级别上、
		List<String> typeList=web.getTypeID(typeId);
		
		if(typeList!=null){
			 int num=typeList.size()-1;
			 for(int i=num;i>=0;i--){
				 if(i==num){
					 web.setDatabaseType(typeList.get(i).toString());
//					 System.out.println("DATABASETYPE:"+web.getDatabaseType());
				 }
				 if(num-i==1){
					 web.setNavbarType(typeList.get(i).toString());
				 }
				 if(num-i==2){
					 web.setListType(typeList.get(i).toString());
				 }
				 if(num-i==3){
					 web.setSublistType(typeList.get(i).toString());
				 }
				 
			}
		}
		
		// 循环遍历读取到的xml
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> hashMap = (HashMap<String, String>) list.get(i);

			String seed = hashMap.get("seed");
//			System.out.println("baseCrawler     " + seed);
			// 通过传递的种子来判断网页的规则，如果种子不对应，则结束当层循环
			if (!(seed.equals(seedUrl))) {
				continue;
			}
			
			String urlTag = hashMap.get("urlTag");
			if(urlTag.equals("") || urlTag==null){
				urlTag = "//body";
			}
			String urlRex = hashMap.get("urlRex");
			String tag1 = hashMap.get("tag1");
			String tag2 = hashMap.get("tag2");
			// 文字类抓取
			// 用web类来传值
			// final Web web = new Web();
			web.setSeed(seed.trim());
			web.setUrlTag(urlTag);
			web.setUrlRex(urlRex.trim());
			List<String> taglist = new ArrayList<String>();
			taglist.add(tag1.trim());
			taglist.add(tag2.trim());
			web.setList(taglist);

			// 图片爬取
			boolean hasImg = Boolean.parseBoolean(hashMap.get("hasImg"));
			String imgRegex = hashMap.get("imgRegex");
			String imgTag = hashMap.get("imgTag");
			web.setImgTag(imgTag.trim());
			web.setImgRegex(imgRegex.trim());
			web.setHasImg(hasImg);
			// 文件
			boolean hasDoc = Boolean.parseBoolean(hashMap.get("hasDoc"));
			String DocRegex = hashMap.get("docRegex");
			String docTag = hashMap.get("docTag");
			web.setDocTag(docTag.trim());
			web.setDocRegex(DocRegex.trim());
			web.setHasDoc(hasDoc);

			// 分页
			String totalPage = hashMap.get("totalPage");
			String pageAjaxTag = hashMap.get("pageAjaxTag");
			String pageGetTag = hashMap.get("pageGetTag");
			String pagePostTag = hashMap.get("pagePostTag");
			String pageMethod = hashMap.get("pageMethod");
			String pageEncoding =hashMap.get("pageEncoding");
			web.setTotalPage(totalPage);
			web.setPageMethod(pageMethod.trim());
			web.setPageAjaxTag(pageAjaxTag.trim());
			web.setPageGetTag(pageGetTag.trim());
			web.setPagePostTag(pagePostTag.trim());
			web.setPageEncoding(pageEncoding.trim());
		}
		return web;
	}
	
	/**
	 *通过传递来的字符串，分割数据类型id.
	 * @param web
	 * @return
	 */
	public List<String> getTypeID(String typeIds){
		if(!(typeIds.equals(""))){
			List<String> typeList =new ArrayList<String>();
			String typeId[] =typeIds.split(",");
			for(int i=0;i<typeId.length;i++){
				typeList.add(typeId[i]);
			}
			return typeList;
		}else{
			return null;
		}
		
	}

	@Override
	public String toString() {
		return "WebInfo [seed=" + seed + ",urlTag="+urlTag+" urlRex=" + urlRex + ", list=" + list + ", imgRegex=" + imgRegex
				+ ", docRegex=" + docRegex + ", imgTag=" + imgTag + ", docTag=" + docTag + ", hasImg=" + hasImg
				+ ", hasDoc=" + hasDoc + ", totalPage=" + totalPage + ", pageAjaxTag=" + pageAjaxTag + ", pageMethod="
				+ pageMethod + ", pageGetTag=" + pageGetTag + ", pagePostTag=" + pagePostTag + ", typeId=" + typeId
				+ ", pageEncoding=" + pageEncoding + ", databaseType=" + databaseType + ", navbarType=" + navbarType
				+ ", listType=" + listType + ", sublistType=" + sublistType + "]";
	}

}
