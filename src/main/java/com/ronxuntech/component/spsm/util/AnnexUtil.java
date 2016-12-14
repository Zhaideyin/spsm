package com.ronxuntech.component.spsm.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.spsm.WebInfo;
import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.seedurl.impl.SeedUrlService;
import com.ronxuntech.service.spsm.spider.SpiderManager;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.service.spsm.targeturl.impl.TargetUrlService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;
import com.ronxuntech.util.SpringBeanFactoryUtils;
import com.ronxuntech.util.UuidUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.Scheduler;

/**
 * 工具类
 * 
 * @author angrl
 *
 */
public class AnnexUtil {

	// 单例模式
	private static AnnexUtil annexUtil = new AnnexUtil();

	/**
	 * 初始化方法
	 * 
	 * @return
	 */
	public static AnnexUtil getInstance() {
		return annexUtil;
	}

	// 将所有的文档链接取出放入，。
	private SeedUrlManager seedurlService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 得到种子地址的 id
	 * 
	 * @param web
	 * @return
	 * @throws Exception
	 */
	public String getSeedUrlId(WebInfo web) throws Exception {
		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		PageData p = new PageData();
		p.put("SEEDURL", web.getSeed());
		PageData p1 = seedurlService.findByUrl(p);
		String seedUrlId = p1.getString("SEEDURL_ID");
		return seedUrlId;
	}

	/**
	 * 通过targetUrl查询到 targeturlID
	 * 
	 */
	public PageData findByPageurl(String pageUrl, TargetUrlManager targeturlService) {
		PageData pdurl = new PageData();
		pdurl.put("TARGETURL", pageUrl);
		try {
			pdurl = targeturlService.findByUrl(pdurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdurl;
	}

	/**
	 * 更新爬取targeturl成功后 的状态 status 0->1
	 * 
	 * @param pageUrl
	 * @param targeturlService
	 */
	public void updateTargetStatus(String pageUrl, TargetUrlManager targeturlService) {
		PageData pdurl = findByPageurl(pageUrl, targeturlService);
		if (pdurl != null ) {
			pdurl.put("STATUS", "1");
			pdurl.put("UPDATETIME", sdf.format(new Date()).toString());
			try {
				targeturlService.edit(pdurl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断附件是否下载完成
	 * 
	 * @param seedUrl
	 * @param annexurlService
	 * @param seedurlService
	 * @return
	 */
	public boolean annexUrlDone(String seedUrl, AnnexUrlManager annexurlService, SeedUrlManager seedurlService) {
		PageData pdurl = findBySeedurl(seedUrl, seedurlService);
		PageData pd = new PageData();
		pd.put("SEEDURLID", pdurl.getString("SEEDURL_ID"));
		pd.put("STATUS", "0");
		List<PageData> annexUrlList = new ArrayList<>();
		// 查询targetURl中状态为 0的annexurl
		try {
			annexUrlList = annexurlService.listBySeedUrlIdAndStatus(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (annexUrlList.size() > 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 通过seedurl 查询。
	 * 
	 * @param seedUrl
	 * @param seedurlService
	 * @return
	 */
	public PageData findBySeedurl(String seedUrl, SeedUrlManager seedurlService) {
		PageData pdurl = new PageData();
		pdurl.put("SEEDURL", seedUrl);
		try {
			pdurl = seedurlService.findByUrl(pdurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdurl;
	}

	/**
	 * 判断种子页面的得到的url是否下载完成
	 * 
	 * @param seedUrl
	 * @param targeturlService
	 * @param seedurlService
	 * @return
	 */
	public boolean pageUrlDone(String seedUrl, TargetUrlManager targeturlService, SeedUrlManager seedurlService) {
		PageData pdurl = findBySeedurl(seedUrl, seedurlService);
		PageData pd = new PageData();
		pd.put("SEEDURLID", pdurl.getString("SEEDURL_ID"));
		pd.put("STATUS", "0");
		List<PageData> targetList = null;
		try {
			targetList = targeturlService.listBySeedUrlIdAndStatus(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果查询的结果中有值，那么则返回false,反之则返回true
		if (targetList.size() > 0) {
			return false;
		} else {
			return true;
		}

	}

	/*public boolean seedUrlDone(String seedUrl, TargetUrlManager targeturlService, SeedUrlManager seedurlService,AnnexUrlManager annexurlService){
		if (annexUtil.pageUrlDone(seedUrl, targeturlService, seedurlService)
				&& annexUtil.annexUrlDone(seedUrl, annexurlService, seedurlService)) {
			return true;
		}else{
			return false;
		}
	}*/
	
	
	/**
	 * 更新种子的status
	 * 
	 * @param seedUrl
	 * @param targeturlService
	 * @param seedurlService
	 */
	/*public void updateSeedStatus(String seedUrl, SeedUrlManager seedurlService) {
		PageData pdurl = findBySeedurl(seedUrl, seedurlService);
		if (pdurl != null && pdurl.size() != 0) {
			pdurl.put("STATUS", "1");
			try {
				seedurlService.edit(pdurl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	/**
	 * 附件下载，判断该网站是否有配置图片或者文档
	 * 
	 * @param web
	 * @param pd
	 * @param page
	 */
	/*public void downloadAnnex(WebInfo web, PageData pd, Page page) {
		StringBuffer annexUrlList = new StringBuffer();
		StringBuffer fileList = new StringBuffer();
		// 如果含有文字或者图片 传递当前页面的地址过去，
		String pageUrl = "";
		pageUrl = page.getUrl().toString();
		Html html = page.getHtml();
		String content = html.xpath(web.getList().get(1).toString()).toString();
		if (web.isHasImg()) {
			if (!(pageUrl.equals(web.getSeed()))) {
				content = downImg(content, pageUrl, web, pd,annexUrlList,fileList);
			}
		}
		if (web.isHasDoc()) {
			if (!(pageUrl.equals(web.getSeed()))) {
				downDoc(content, pageUrl, web, pd,annexUrlList,fileList);
			}

		}
	}*/

	public void downloadAnnex(WebInfo web, PageData pd, String content,String pageUrl) {
		StringBuffer annexUrlList = new StringBuffer();
		StringBuffer fileList = new StringBuffer();
		// 如果含有文字或者图片 传递当前页面的地址过去，
		String contents=content;
		if (web.isHasImg()) {
			contents = downImg(content, pageUrl, web, pd,annexUrlList,fileList);
		}
		if (web.isHasDoc()) {
			downDoc(contents, pageUrl, web, pd,annexUrlList,fileList);
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
	public String downImg(String content, String pageUrl, WebInfo web, PageData pd,StringBuffer annexUrlList,StringBuffer fileList) {
		// 通过正则表达式来找出符合图片或者文档的链接
		Pattern p = Pattern.compile(web.getImgRegex().trim());
		Matcher m = p.matcher(content);
		// 用于存放多个文档链接。或者图片链接
		List<String> pathList = new ArrayList<>();
		// 保存文件的路径
		String fileDir = "";
//		StringBuffer annexUrlList = new StringBuffer();
//		StringBuffer fileList = new StringBuffer();
		List<String> annexNameList = new ArrayList<>();
		// 循环取出抓取到的文本中符合配置的过滤图片正则的链接
		while (m.find()) {
			if (!(pageUrl.equals(""))) {
				// 获取的图片或者文档的链接。
				String annexUrl = m.group();
				// 存放文件路径和文件名的map
				Map<String, Object> map = null;
				try {
					// 取得文件路径和文件名
					map = findDirAndFileName(annexUrl, web);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				// 文件路径： 项目的根路径 + 网站地址
				fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
				String fileName = map.get("fileName").toString();
//				String fileName = getFileName();
//				String extName = com.google.common.io.Files.getFileExtension(annexUrl);
//				fileName = fileName + extName;
				annexNameList.add(fileName);
				// 清除特殊字符
				String regex = "[|*?%<>\"]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(fileDir);
				fileDir = matcher.replaceAll("").trim();
				// 将当前下载的文档或者图片的路径保存到list
				pathList.add(fileDir + fileName);
				// 替换原来html中读取文档或者图片的url
				content = content.replace(m.group(), map.get("fileDir").toString() + fileName);
				String annexUrl1 = "";
				if (annexUrl.contains("http")) {
					annexUrl1.replace("amp;", "").trim();
				} else {
					annexUrl1 = annexUtil.getTargetUrl(annexUrl, pageUrl).replace("amp;", "").trim();
				}
				annexUrlList.append(annexUrl1 + ",");
				fileList.append(fileDir + fileName + ",");
			}

		}
		pd.put("ANNEXURLS", annexUrlList.toString());
		pd.put("FILENAME", fileList.toString());
		pd.put("CONTENT", content);
		//如果下载的文件名列表不为空，则去执行下载
		if(annexNameList.size()!=0){
//			//存储urls文件的路径
//			String filepath ="D:\\webmagic\\spsm\\urls\\annex";
//		    Scheduler scheduler = new FileCacheQueueScheduler(filepath);
			 
			AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web,"img");
			ImgOrDocPipeline img = new ImgOrDocPipeline(fileDir, annexNameList, pageUrl, web.getSeed());
			Spider.create(annex).addUrl(pageUrl).addPipeline(img).thread(3).start();
		}
		return content;
	}

	/**
	 * downDoc 文件下载
	 * 
	 * @param html
	 * @param pageUrl
	 * @param web
	 * @param pd
	 */
	public void downDoc(String content, String pageUrl, WebInfo web, PageData pd,StringBuffer annexUrlList,StringBuffer fileList) {
		// 通过正则表达式来找出符合图片或者文档的链接
		Pattern p = Pattern.compile(web.getDocRegex().trim());
		Matcher m = p.matcher(content);
		// 用于存放多个文档链接。或者图片链接
		List<String> pathList = new ArrayList<>();
		// 循环检测是否匹配文档下载的链接
//		StringBuffer annexUrlList = new StringBuffer();
//		StringBuffer fileList = new StringBuffer();
		List<String> annexNameList = new ArrayList<>();
		String fileDir = "";
		while (m.find()) {
			// 获取的图片或者文档的链接。
			String annexUrl = m.group();
			Map<String, Object> map = null;
			try {
				// 把原网址的链接截取出要建立的文件夹 、文件名
				map = findDirAndFileName(annexUrl, web);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			// 文件路径
			fileDir = PathUtil.getClasspath() + map.get("fileDir").toString();
			// doc文档名
			String fileName = getFileName();

			// 扩展名
			String extName = com.google.common.io.Files.getFileExtension(annexUrl);
			// 整个名称
			fileName = fileName + extName;
			annexNameList.add(fileName);

			// 将当前下载的文档或者图片的路径保存到list
			pathList.add(fileDir + fileName);
			// 将新的路径替换成新的路径。
			String annexUrl1 = "";
			if (annexUrl.contains("http")) {
				annexUrl1 = annexUrl.replace("amp;", "").trim();
			} else {
				annexUrl1 = annexUtil.getTargetUrl(annexUrl, pageUrl).replace("amp;", "").trim();
			}
			annexUrlList.append(annexUrl1 + ",");
			content = content.replace(m.group(), map.get("fileDir").toString() + fileName);
			fileList.append(fileDir + fileName + ",");
		}
		
		pd.put("FILENAME", fileList.toString());
		pd.put("ANNEXURLS", annexUrlList.toString());
		pd.put("CONTENT", content);
		//如果下载文件的名称列表不为空，则去执行下载
		if(annexNameList.size()!=0){
//			//存储urls文件的路径
//			String filepath ="D:\\webmagic\\spsm\\urls\\annex";
//		    Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		    
			AnnexSpider annex = new AnnexSpider(pageUrl, web.getSeed(), web,"doc");
			ImgOrDocPipeline doc = new ImgOrDocPipeline(fileDir, annexNameList, pageUrl, web.getSeed());
			Spider.create(annex).addUrl(pageUrl).addPipeline(doc).thread(3).start();
		}
		

	}

	/**
	 * 将下载的地址作为 文件夹和文件名。通过最后一个/ 来区分
	 * 
	 * @param annexUrl
	 * @return 文件路径和文件名（不包括后缀）的一个map
	 * @throws MalformedURLException
	 */
	public Map<String, Object> findDirAndFileName(String annexUrl, WebInfo web) throws MalformedURLException {
		Map<String, Object> map = new HashMap<String, Object>();
		int index = annexUrl.lastIndexOf("/");
		// 文件名
		String fileName = annexUrl.substring(index + 1);

		// 网站/标题作为文件夹 清除特殊字符
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		//附件替换后的相对路径
		String fileDir = "uploadFiles/spsm/" + webUrl + "/";

		map.put("fileName", fileName);
		map.put("fileDir", fileDir);
		return map;
	}

	/**
	 * 生成文档文件名 时间戳 +生成5位数的随机数 作为doc或者其他文件的文件名
	 * 
	 * @return
	 */
	public String getFileName() {
		String fileName = "";
		fileName = "" + System.currentTimeMillis() + (int) (Math.random() * (99999 - 10000 + 1) + 10000) + ".";
		return fileName;
	}

	/**
	 * 先判断网页上抓到的链接，判断../ 出现的个数 如果前边含有 ./ / ../ ../../ 或没有
	 * 分别截取网页地址的部分。前面两个和没有直接去掉网页的最后加上 / 后面的 如果是 ../ 则去掉倒数第1个 / 后的。
	 * 并且加上传来的url最后的。同理，../../ 则是去掉倒数第2个 /后的。 tergetUrl
	 * 
	 * @param url
	 *            htmlUrl(页面正则匹配到的，) pageUrl 是当前页面的地址
	 * @return
	 * @throws MalformedURLException
	 */
	/**
	 * 得到完整地 链接
	 * 
	 * @param htmlUrl
	 *            得到的html页面上抓取的 链接。
	 * @param pageUrl
	 *            当前页的链接
	 * @return
	 * @throws MalformedURLException
	 */

	/**
	 * 拼接url(主要用于ajax分页和 抓取到相对路径时使用)
	 * 
	 * @param htmlUrl（正则匹配到的url）
	 * @param pageUrl(当前页面的url)
	 * @return
	 */
	public String getTargetUrl(String htmlUrl, String pageUrl) {
		String targetHmtlUrl = htmlUrl;
		String targetPageUrl = pageUrl;
		String targetUrl = "";
		int i = getTheTimes(htmlUrl);
		targetPageUrl = delLastSlashToEnd(targetPageUrl);
		targetHmtlUrl = delStartWithSlash(targetHmtlUrl);

		if (htmlUrl.startsWith("/")) {
			try {
				targetPageUrl = getBasePath(targetPageUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		for (int j = 0; j < i; j++) {
			targetPageUrl = delLastSlashToEnd(targetPageUrl);
			targetHmtlUrl = delStartWithSlash(targetHmtlUrl);
		}
		targetUrl = targetPageUrl + "/" + targetHmtlUrl;
		return targetUrl;
	}

	/**
	 * 统计 ../出现的次数
	 * 
	 * @return times
	 */
	public int getTheTimes(String url) {
		Matcher match = Pattern.compile("\\.\\./").matcher(url);
		int times = 0;
		while (match.find()) {
			times++;
		}
		return times;
	}

	/**
	 * 去掉当前页面的url 最后一个 /及以后的字符 example : www.baidu.com/h/b 则返回 www.baidu.com/h
	 * 
	 * @param pageUrl
	 * @return
	 */
	public String delLastSlashToEnd(String pageUrl) {
		int i = pageUrl.lastIndexOf("/");
		pageUrl = pageUrl.substring(0, i);
		return pageUrl;
	}

	/**
	 * 删除网页上抓取到的连接中 ../ 或 / 或 ./ example : ../../baidu.com --> baidu.com
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String delStartWithSlash(String url) {
		String target = url;
		if (url.startsWith("/")) {
			target = url.substring(1);
		} else if (url.startsWith("./")) {
			target = url.substring(2);
		} else if (url.startsWith("../")) {
			target = target.substring(3);
		}
		return target;
	}

	/**
	 * 得到当前url的根路径 example :
	 * http://www.cfip.cn:1012/page/fieldtitle2.cbs?resna=lyzs&amp; -->
	 * http://www.cfip.cn:1012
	 * 
	 * @param pageUrl
	 * @return
	 * @throws MalformedURLException
	 */
	public String getBasePath(String pageUrl) throws MalformedURLException {
		String baseUrl = "";
		URL u = new URL(pageUrl);
		baseUrl = u.getProtocol() + "://" + u.getHost() + ":" + u.getPort();
		return baseUrl;
	}

	/**
	 * list转换成set(用于去重)
	 * 
	 * @param list
	 * @return
	 */
	public Set<String> listToSet(List<String> list) {
		Set<String> set = new HashSet<>();
		for (String str : list) {
			if(!(str.trim().equals(""))){
				set.add(str);
			}
			
		}
		return set;
	}

	/**
	 * set转换成list;
	 * 
	 * @param set
	 * @return
	 */
	public List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<>();
		for (String str : set) {
			list.add(str);
		}
		return list;
	}

	/**
	 * 将抓到的页面信息插入数据库
	 * 
	 * @param html
	 * @param web
	 * @param pageUrl
	 * @param spiderService
	 * @param targeturlService
	 */
	
	public void insertintoDatabase(String content,String title, WebInfo web, String pageUrl, SpiderManager spiderService,
			TargetUrlManager targeturlService) {
		PageData pd = new PageData();
		// 将[],去掉
		String regex = "[\\[|\\]|,]";
		content =content.replaceAll(regex, "");
		title = title.replaceAll(regex, "");
		pd.put("SPIDER_ID", UuidUtil.get32UUID());
		pd.put("TITLE", title);
		pd.put("CONTENT", content);
		pd.put("DATABASETYPE", web.getDatabaseType());
		pd.put("NAVBARTYPE", web.getNavbarType());
		pd.put("LISTTYPE", web.getListType());
		pd.put("SUBLISTTYPE", web.getSublistType());

		String d = sdf.format(new Date());
		pd.put("CREATE_TIME", d);
		pd.put("TARGETURLID", pageUrl);
		// 获取当前页的图片或者文档
		annexUtil.downloadAnnex(web, pd, content, pageUrl);
		try {
			// 如果怕去的 内容是空，则不跳出。不存入数据库。
			if (!("".equals(content.trim()))) {
				if (content.trim().length() > 0) {
					// 将爬取的数据放入数据库
					spiderService.save(pd);
					// 当钱页面不是种子页面，通过查询，然后修改该url的状态。
					if (!(pageUrl.equals(web.getSeed()))) {
						annexUtil.updateTargetStatus(pageUrl, targeturlService);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将抓到的targetUrl存入数据库
	 * 
	 * @param urlList
	 * @param seedUrlId
	 * @param targeturlService
	 * @throws Exception 
	 */
	public void targeturlInsertDatabase(List<String> urlList, String seedUrlId, TargetUrlService targeturlService) {
		Set<String> set =listToSet(urlList);
		List<String> tempList =setToList(set);
		// 用于存入数据库
		List<PageData> targetUrlList = new ArrayList<>();
		if (tempList.size() != 0) {
			// 取出所有target,重新组装到pd 中，然后放入list 进行批量插入
			for (int i = 0; i < tempList.size(); i++) {
				if(!(tempList.get(i).equals(""))){
					PageData pd1 = new PageData();
					pd1.put("TARGETURL_ID", annexUtil.getFileName());
					pd1.put("TARGETURL", tempList.get(i));
					pd1.put("STATUS", "0");
					pd1.put("SEEDURLID", seedUrlId);
					pd1.put("CREATETIME", sdf.format(new Date()));
					targetUrlList.add(pd1);
				}
			}
				// 将得到的链接批量插入数据库
				PageData pds = new PageData();
				pds.put("targetUrlList", targetUrlList);
				try {
					targeturlService.saveAll(pds);
				} catch (Exception e) {
				}
		}

	}
	/**
	 * 是否需保存
	 * @param web
	 * @param page
	 * @return
	 */
	public boolean isSave(WebInfo web,Page page){
		String urlRegex =web.getUrlRex().replace("amp;", "");
		if(!(urlRegex.contains("http"))){
			urlRegex = annexUtil.getTargetUrl(urlRegex,web.getSeed());
		}		
		Pattern p = Pattern.compile(urlRegex);
		Matcher m = p.matcher(page.getUrl().toString());
		boolean isNextPage=true;
		while(m.find()){
			isNextPage=false;
		}
		return isNextPage;
	}

	
}
