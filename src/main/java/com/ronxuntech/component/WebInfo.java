package com.ronxuntech.component;

import java.util.List;

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
}
