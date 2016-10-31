package com.ronxuntech.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ronxuntech.component.spsm.util.ReadXML;

public class WebInfo {
	private String seed;// 种子
	private String urlRex;// 要抓取的url正则表达式
	private List<String> list;// 标签列表
	private String imgRegex; // 过滤图片的正则表达式
	private String docRegex; // 过滤文档的正则表达式
	private String imgTag; // 提取图片的 标签
	private String docTag; // 提取文档的标签
	private boolean hasImg; // 是否存在图片
	private boolean hasDoc; // 是否存在文档
	private int totalPage; // 总页数
	private String pageAjaxTag; // 异步标签
	private String pageMethod; // 分页方式
	private String pageGetTag;  // get
	private String pagePostTag;  //post
	private String typeId;  //传进来的类型id
	
	
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

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
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
	
	
	public static WebInfo init(String seedUrl, String typeId) throws Exception {
		WebInfo web=new WebInfo();
		web.typeId = typeId;
		ReadXML readXML = new ReadXML();
		final List list = readXML.ResolveXml();
		// 循环遍历读取到的xml
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> hashMap = (HashMap<String, String>) list.get(i);

			String seed = hashMap.get("seed");
			System.out.println("baseCrawler     " + seed);
			// 通过传递的种子来判断网页的规则，如果种子不对应，则结束当层循环
			if (!(seed.equals(seedUrl))) {
				continue;
			}
			String urlRex = hashMap.get("urlRex");
			String tag1 = hashMap.get("tag1");
			String tag2 = hashMap.get("tag2");
			// 文字类抓取
			// 用web类来传值
			// final Web web = new Web();
			web.setSeed(seed);
			web.setUrlRex(urlRex);
			List<String> taglist = new ArrayList<String>();
			taglist.add(tag1);
			taglist.add(tag2);
			web.setList(taglist);

			// 图片爬取
			boolean hasImg = Boolean.parseBoolean(hashMap.get("hasImg"));
			String imgRegex = hashMap.get("imgRegex");
			String imgTag = hashMap.get("imgTag");
			web.setImgTag(imgTag);
			web.setImgRegex(imgRegex);
			web.setHasImg(hasImg);
			// 文件
			boolean hasDoc = Boolean.parseBoolean(hashMap.get("hasDoc"));
			String DocRegex = hashMap.get("docRegex");
			String docTag = hashMap.get("docTag");
			web.setDocTag(docTag);
			web.setDocRegex(DocRegex);
			web.setHasDoc(hasDoc);

			// 分页
			int totalPage = Integer.parseInt(hashMap.get("totalPage"));
			String pageAjaxTag = hashMap.get("pageAjaxTag");
			String pageGetTag = hashMap.get("pageGetTag");
			String pagePostTag = hashMap.get("pagePostTag");
			String pageMethod = hashMap.get("pageMethod");
			web.setTotalPage(totalPage);
			web.setPageMethod(pageMethod);
			web.setPageAjaxTag(pageAjaxTag);
			web.setPageGetTag(pageGetTag);
			web.setPagePostTag(pagePostTag);
		}
		return web;
	}

}
