package com.ronxuntech.component.spsm.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.WebInfo;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;

public class AnnexUtil {

	/**
	 * 附件下载
	 * 
	 * @param web
	 * @param pd
	 * @param page
	 */
	// 将所有的文档链接取出放入，。
	private StringBuffer path;

	public void downloadAnnex(WebInfo web, PageData pd, Page page) {
		// 如果含有文字或者图片 传递当前页面的地址过去，
		String pageUrl = "";
		pageUrl = page.getUrl().toString();
		Html html = page.getHtml();
		if (web.isHasImg()) {
			if (!(pageUrl.equals(web.getSeed()))) {
				downImg(html, pageUrl, web, pd);
			}
		}
		if (web.isHasDoc()) {
			if (!(pageUrl.equals(web.getSeed()))) {
				downDoc(html, pageUrl, web, pd);
			}

		}
	}

	/**
	 * 图片下载
	 * 
	 * @param html
	 * @param pageUrl
	 * @param web
	 * @param pd
	 */
	public void downImg(Html html, String pageUrl, WebInfo web, PageData pd) {
		// 通过正则表达式来找出符合图片或者文档的链接
		Pattern p = Pattern.compile(web.getImgRegex());
		String content = html.xpath(web.getList().get(1).toString()).toString();
		Matcher m = p.matcher(content);
		// 用于存放多个文档链接。或者图片链接
		List<String> pathList = new ArrayList<>();
		
		ImgOrDocPipeline img=null;
		while (m.find()) {
			if (pageUrl != "") {
				AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web);
				// 获取的图片或者文档的链接。
				String annexUrl = m.group();
				Map<String, Object> map = null;
				try {
					map = findDirAndFileName(annexUrl,html,web);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				// 文件路径
				String fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
				String fileName = map.get("fileName").toString();
				// 截取前面的作为文件夹
//				fileDir = fileDir.substring(0, fileDir.lastIndexOf("/") + 1);
				// 清楚特殊字符
				String regex = "[|*?%<>\"]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(fileDir);
				fileDir = matcher.replaceAll("").trim();
				img = new ImgOrDocPipeline(fileDir, fileName);
				// 开始爬取图片
				Spider.create(annex).addUrl(pageUrl).addPipeline(img).thread(5).start();
				// 将当前下载的文档或者图片的路径保存到list
			
				
				pathList.add(fileDir + fileName);
				// 替换原来html中读取文档或者图片的url
				content = content.replace(m.group(),fileDir+fileName);
				pd.put("CONTENT", content);
				pd.put("KEYWORDS",annexUrl);
			}
			
		}
		// 将一个网页上的 多个图片或者文档的路径取出存入数据库通过“；”来区分
		path=new StringBuffer();
		for (int j = 0; j < pathList.size(); j++) {
			path.append(pathList.get(j) + ";");
		}
		pd.put("ANNEX_PATH", path.toString());
	}

	/**
	 * downDoc 文件下载
	 * 
	 * @param html
	 * @param pageUrl
	 * @param web
	 * @param pd
	 */
	public void downDoc(Html html, String pageUrl, WebInfo web, PageData pd) {
		// 通过正则表达式来找出符合图片或者文档的链接
		Pattern p = Pattern.compile(web.getDocRegex());
		String content = html.xpath(web.getList().get(1).toString()).toString();
		Matcher m = p.matcher(content);
		// 用于存放多个文档链接。或者图片链接
		List<String> pathList = new ArrayList<>();
		ImgOrDocPipeline doc=null;
		// 循环检测是否匹配文档下载的链接
//		int nameNum=0;
		while (m.find()) {
			if (pageUrl != "") {
				AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web);
				// 获取的图片或者文档的链接。
				String annexUrl = m.group();
				System.out.println("annexUrl"+annexUrl);
				Map<String, Object> map = null;
				try {
					// 把原网址的链接截取出要建立的文件夹 、文件名
					map = findDirAndFileName(annexUrl,html,web);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				// 文件路径
				String fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
				// doc文档名
				String fileName =getFileName();
						//html.xpath(web.getDocName().get(nameNum).toString()).toString();
				//nameNum++;
				
				int i = fileName.lastIndexOf(".");
				// 扩展名
				String extName=com.google.common.io
                        .Files.getFileExtension(annexUrl);
				// 文件名
//				String temp = fileName.substring(0, i);
				// 整个名称
				fileName = fileName+ extName;
//				fileDir = fileDir.substring(0, fileDir.lastIndexOf("/") + 1);
				// 清楚特殊字符
				String regex = "[|*?%<>\"]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(fileDir);
				fileDir = matcher.replaceAll("").trim();
				doc = new ImgOrDocPipeline(fileDir, fileName);
				Spider.create(annex).addUrl(pageUrl).addPipeline(doc).thread(1).start();
				// 将当前下载的文档或者图片的路径保存到list
				pathList.add(fileDir + fileName);
				// 将新的路径替换成新的路径。
				System.out.println(m.group());
				content = content.replace(m.group(),map.get("fileDir").toString()+fileName);
				pd.put("KEYWORDS", annexUrl);
			}
		}
		// 将一个网页上的 多个图片或者文档的路径取出存入数据库通过“；”来区分
		path=new StringBuffer();
		for (int j = 0; j < pathList.size(); j++) {
			path.append(pathList.get(j) + ";");
		}
		pd.put("CONTENT", content);
		pd.put("ANNEX_PATH", path.toString());
				
	}

	/**
	 * 将下载的地址作为 文件夹和文件名。通过最后一个 \ 来区分
	 * 
	 * @param annexUrl
	 * @return
	 * @throws MalformedURLException
	 */
	public Map findDirAndFileName(String annexUrl,Html html,WebInfo web) throws MalformedURLException {
		Map<String, Object> map = new HashMap();

		URL url = new URL(annexUrl);
		String hostName = url.getHost();
		int index = annexUrl.lastIndexOf("/");
		// 文件名
		String fileName = annexUrl.substring(index + 1);
		// 文件夹
		String dir = annexUrl.replace("http://", "");
		String fileDir = dir.replace("https://", "").replace(hostName + "/", "").replace(fileName, "");

		
		String test="uploadFiles/spsm/"+html.xpath(web.getList().get(0)).toString();
		System.out.println("test "+test);
		map.put("fileName", fileName);
		map.put("fileDir", test);
		return map;
	}

	//时间戳  +生成5位数的随机数 作为doc或者其他文件的文件名
	public String getFileName(){
		String fileName="";
		fileName=""+System.currentTimeMillis()+(int)(Math.random()*(99999-10000+1)+10000)+".";
		return fileName;
	}
}
