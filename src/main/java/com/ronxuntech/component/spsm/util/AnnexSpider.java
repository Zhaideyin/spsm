package com.ronxuntech.component.spsm.util;
/**
 * AnnexSpider 附件爬取类
 *	by xieyun 
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ronxuntech.component.spsm.WebInfo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.UrlUtils;

public class AnnexSpider implements PageProcessor {

	private WebInfo web;
	private Site site;
	private String pageUrl;  //附件地址
	private AnnexUtil annexUtil=AnnexUtil.getInstance();
	public AnnexSpider(String pageUrl,String startUrl,WebInfo web) {
		this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl));
		this.pageUrl=pageUrl;
		this.web=web;
	}
	
	
	public void process(Page page) {
		// 在符合urlPatten这样的页面过滤符合以下条件的链接 "http://www.natesc.org.cn"+
		String docRegex = web.getDocRegex();
		String imgRegex =web.getImgRegex();
		
		// 将所有满足urlPatten的链接全部放入 requests中。
		Html obj = page.getHtml();
		List<String> requests = new ArrayList<>();
		requests.add(pageUrl);
		// 将满足条件的链接加入待爬取的队列中。
		page.addTargetRequests(requests);
		String imgHostFileName = obj.xpath("//title/text()").toString().replaceAll(" - 中国种子咨询网", "");
		List<String> listProcess= new ArrayList<>();
		//判断图片和文档是否存在
		if(StringUtils.isNotEmpty(docRegex)){
			listProcess = obj.xpath("//a").regex(docRegex).all();  // 此时抓取出来的是html中的链接， 没有 协议 域名 以及项目名等。
			
		}else if(StringUtils.isNotEmpty(imgRegex)){
			listProcess = obj.xpath(web.getImgTag()).regex(imgRegex).all();
		}
		//判断网页抓取的图片或者文档是否是相对地址，如果是相对地址，那么加上协议  主机  项目名。。
		List<String> templist= new ArrayList<>();
		for(int i=0;i<listProcess.size();i++){
			// 如果不是绝对路径，则通过抓取到的链接，和当前页面的 ｕｒｌ来拼接下载地址。
			if(!(listProcess.get(i).contains("http"))){
				String temp= annexUtil.getTargetUrl(listProcess.get(i),pageUrl);
				templist.add(temp);
			}
		}
		listProcess = null;
		listProcess =templist;
		// 此处将标题一并抓取，之后提取出来作为文件名
		listProcess.add(0, imgHostFileName);
		page.putField("img", listProcess);
	}

	public Site getSite() {
		return site;
	}
}