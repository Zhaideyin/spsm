package com.ronxuntech.component.spsm.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.spsm.AjaxCrawler;
import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
/**
 * 附件下载工具类
 * @author angrl
 *
 */
public class AnnexUtil {

	// 单例模式
		private static AnnexUtil annexUtil = new AnnexUtil();

		// 静态工厂方法
		public static AnnexUtil getInstance() {
			return annexUtil;
		}
	
	// 将所有的文档链接取出放入，。
	private StringBuffer path;

	/**
	 * 附件下载，判断该网站是否有配置图片或者文档
	 * @param web
	 * @param pd
	 * @param page
	 */
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
		//保存文件的路径
		String fileDir="";
		List<String> annexNameList=new ArrayList<>();
		//循环取出抓取到的文本中符合配置的过滤图片正则的链接
		while (m.find()) {
			if (pageUrl != "") {
				// 获取的图片或者文档的链接。
				String annexUrl = m.group();
				//存放文件路径和文件名的map
				Map<String, Object> map = null;
				try {
					//取得文件路径和文件名
					map = findDirAndFileName(annexUrl,html,web);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				// 文件路径： 项目的根路径 + 网站地址
				fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
				String fileName = getFileName();
				String extName=com.google.common.io
                        .Files.getFileExtension(annexUrl);
				fileName = fileName+extName;
				annexNameList.add(fileName);
				System.out.println("annexUtil fileName:"+fileName);
				// 清除特殊字符
				String regex = "[|*?%<>\"]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(fileDir);
				fileDir = matcher.replaceAll("").trim();
				// 将当前下载的文档或者图片的路径保存到list
				pathList.add(fileDir + fileName);
				// 替换原来html中读取文档或者图片的url
				content = content.replace(m.group(),map.get("fileDir").toString()+fileName);
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
		AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web);
		ImgOrDocPipeline img = new ImgOrDocPipeline(fileDir,annexNameList);
		Spider.create(annex).addUrl(pageUrl).addPipeline(img).thread(3).start();
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
		// 循环检测是否匹配文档下载的链接
		List<String> annexUrlList =new ArrayList<>();
		List<String> annexNameList=new ArrayList<>();
		String fileDir="";

		while (m.find()) {
			if (pageUrl != "") {
				
				// 获取的图片或者文档的链接。
				String annexUrl = m.group();
				Map<String, Object> map = null;
				try {
					// 把原网址的链接截取出要建立的文件夹 、文件名
					map = findDirAndFileName(annexUrl,html,web);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				// 文件路径
				 fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
				// doc文档名
				 String fileName =getFileName();
				
				// 扩展名
				String extName=com.google.common.io
                        .Files.getFileExtension(annexUrl);
				// 整个名称
				fileName = fileName+ extName;
				annexNameList.add(fileName);
				
				// 将当前下载的文档或者图片的路径保存到list
				pathList.add(fileDir + fileName);
				// 将新的路径替换成新的路径。
				System.out.println(m.group());
				annexUrlList.add(m.group());
				content = content.replace(m.group(),map.get("fileDir").toString()+fileName);
				pd.put("KEYWORDS", annexUrl);
			}
		}
		// 将一个网页上的 多个图片或者文档的路径取出存入数据库通过“；”来区分
		path=new StringBuffer();
		for (int j = 0; j < pathList.size(); j++) {
			path.append(pathList.get(j) + ";");
		}
		AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web);
		ImgOrDocPipeline doc = new ImgOrDocPipeline(fileDir,annexNameList);
		Spider.create(annex).addUrl(pageUrl).addPipeline(doc).thread(3).start();
		pd.put("CONTENT", content);
		pd.put("ANNEX_PATH", path.toString());
				
	}

	/**
	 * 将下载的地址作为 文件夹和文件名。通过最后一个/ 来区分
	 * 
	 * @param annexUrl
	 * @return 文件路径和文件名（不包括后缀）的一个map
	 * @throws MalformedURLException
	 */
	public Map findDirAndFileName(String annexUrl,Html html,WebInfo web) throws MalformedURLException {
		Map<String, Object> map = new HashMap();
		int index = annexUrl.lastIndexOf("/");
		// 文件名
		String fileName = annexUrl.substring(index + 1);

		// 网站/标题作为文件夹  清除特殊字符
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		
		String fileDir="uploadFiles/spsm/"+webUrl+"/";
		
		map.put("fileName", fileName);
		map.put("fileDir", fileDir);
		return map;
	}
	
	/**
	 * 生成文档文件名   时间戳  +生成5位数的随机数 作为doc或者其他文件的文件名
	 * @return
	 */
	public String getFileName(){
		String fileName="";
		fileName=""+System.currentTimeMillis()+(int)(Math.random()*(99999-10000+1)+10000)+".";
		return fileName;
	}
	
	/** 先判断网页上抓到的链接，判断../ 出现的个数   如果前边含有 ./  /   ../   ../../ 或没有  分别截取网页地址的部分。前面两个和没有直接去掉网页的最后加上 / 后面的   如果是 ../ 则去掉倒数第1个  / 后的。
	 * 并且加上传来的url最后的。同理，../../ 则是去掉倒数第2个 /后的。
	 * tergetUrl 
	 * @param url htmlUrl(页面正则匹配到的，) pageUrl 是当前页面的地址
	 * @return
	 * @throws MalformedURLException
	 */
	public String getTargetUrl(String htmlUrl,String pageUrl) {
		String targetHmtlUrl=htmlUrl;
		String targetPageUrl=pageUrl;
		String targetUrl="";
		int i=getTheTimes(htmlUrl);
		targetPageUrl =delLastSlashToEnd(targetPageUrl);
		for(int j=0;j<i;j++){
			targetHmtlUrl =delStartWithSlash(targetHmtlUrl);
			targetPageUrl =delLastSlashToEnd(targetPageUrl);
		}
		targetUrl=targetPageUrl+"/"+targetHmtlUrl;
		return targetUrl;
	}
	
	/**
	 * 统计 ../出现的次数
	 * @return times
	 */
	public int getTheTimes(String url){
		Matcher match = Pattern.compile("\\.\\./").matcher(url);
        int times = 0;
        while(match.find()){
        	times++; 
        }
        return times;   
	}
	
	
	/**
	 * 去掉最后一个 /及以后的字符  如www.baidu.com/h/b  则返回 www.baidu.com/h
	 * @param pageUrl
	 * @return
	 */
	public String delLastSlashToEnd(String pageUrl){
		int i=pageUrl.lastIndexOf("/");
		pageUrl= pageUrl.substring(0, i);
		return pageUrl;
	}
	
	/**
	 * 删除网页上抓取到的连接中  ../ / ./ 
	 * @param url
	 * @return
	 */
	public String delStartWithSlash(String url){
		String target=url;
		if(url.startsWith("/")){
			target=url.substring(1);
		}else if(url.startsWith("./")){
			target=url.substring(2);
		}else if(url.startsWith("../")){
			target=target.substring(3);
		}
		return target;
	}
	
}
