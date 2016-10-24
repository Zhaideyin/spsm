package com.ronxuntech.component.spsm.util;

import java.util.ArrayList;
import java.util.List;

import com.ronxuntech.component.WebInfo;
import com.ronxuntech.util.PathUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.UrlUtils;

public class AnnexSpider implements PageProcessor {

	private WebInfo web;
	private Site site;
	private String pageUrl;  //附件地址

	public AnnexSpider(String pageUrl,String startUrl,WebInfo web) {
		this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl));
		this.pageUrl=pageUrl;
		this.web=web;
	}
	
	
	/**
	 * 测试抓取有文档的页面
	 * 
	 */
	public void process(Page page) {
		// 在符合urlPatten这样的页面过滤符合以下条件的链接 "http://www.natesc.org.cn"+
		String docRegex = web.getDocRegex();
		String imgRegex =web.getImgRegex();
		// 将所有满足urlPatten的链接全部放入 requests中。
		Html obj = page.getHtml();
		List<String> requests = new ArrayList<>();
		requests.add(pageUrl);
//				obj.links().regex(urlPattern).all();
		// 将满足条件的链接加入待爬取的队列中。
		page.addTargetRequests(requests);
		String imgHostFileName = obj.xpath("//title/text()").toString().replaceAll(" - 中国种子咨询网", "");
		List<String> listProcess= new ArrayList<>();
		//判断图片和
		if(docRegex!=null || docRegex!=""){
			listProcess = obj.xpath("//a").regex(docRegex).all();
		}else if(imgRegex!=null || imgRegex!=""){
			listProcess = obj.xpath(web.getImgTag()).regex(imgRegex).all();
		}
		
		/**
		 * 显示抓取的；链接的
		 */
		/*for (Map.Entry<String, Object> entry : page.getResultItems().getAll().entrySet()) {
			System.out.println("遍历resultItems:");
			// entry.getValue()是保存的所有链接的集合。包括获取的title.
			if (entry.getValue() instanceof List) {
				System.out.println("获取的是List  " + entry.getValue().toString());
			}
		}*/

		
//		System.out.println("imghost       :" + imgHostFileName);
//		System.out.println("listProces        :" + listProcess.size());
		// 此处将标题一并抓取，之后提取出来作为文件名
		listProcess.add(0, imgHostFileName);
		page.putField("img", listProcess);
	}

	public Site getSite() {
		return site;
	}
}