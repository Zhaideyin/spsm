package com.ronxuntech.component.spsm;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronxuntech.component.spsm.util.AnnexUtil;
import com.ronxuntech.component.spsm.util.HttpClientDownloader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class SpiderTest implements PageProcessor{

	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000)
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
//			.addHeader("Upgrade-Insecure-Requests", "1")
//			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
//			.addCookie("PHPSESSID", "fg45g4esaboeh13i3cqhhvbet0")
//			.addCookie("Hm_lvt_9d8f86a65bc11382cd39fbe78884c454", "1483948469,1484012200")
//			.addCookie("Hm_lpvt_9d8f86a65bc11382cd39fbe78884c454", "1484030576")
			;
			
	// 附件工具
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Html html= page.getHtml();
		
		//			  (http://www.sczyw.com/show\\?classguid=(\\w+-)*(\\w+)*(&)*guid=(\\w+-){4}\\w+)
		String regex="(http://crop.agridata.cn/96-014/CGRIS水稻.HTM)";
//		String pageNumInContent=html.xpath("//*[@id='AspNetPager1']/a[9]/@href").toString();
		List<String> links =html
				.xpath("/html/body/table[2]")
				.links()
				.regex(regex)
				.all();
		System.out.println("links:"+links.toString());
//		System.out.println("[pagenum:"+pageNumInContent);
		System.out.println("content:"+html.xpath("//body/outerHtml()").toString());
		page.addTargetRequests(links);
		
	}

	public static void main(String[] args) throws MalformedURLException {
//		Spider.create(new SpiderTest()).setDownloader(new HttpClientDownloader())
//		.addUrl("http://www.cgris.net/xw/upload/2004%E5%86%9C%E4%BD%9C%E7%89%A9%E5%B9%B3%E5%8F%B0%E5%B7%A5%E4%BD%9C%E8%A6%81%E6%B1%82.doc")
//		.addUrl("http://www.seedchina.com.cn/defaultInfoList.aspx?Id=5&isSubType=yes")
//		.addUrl("http://crop.agridata.cn/ch_NewsFrm/newsList.asp")
//		.addUrl("http://www.agrione.cn/technology/shengwujishu/dongwulei/69433.html")
//		.thread(1).run();
		
//		String a= "http://www.3456.tv/business/zhongzi_sichuan.html";
//		AnnexUtil annexUtil =AnnexUtil.getInstance();
//		System.out.println(annexUtil.getBasePath(a));
//			
		SpiderTest t =new SpiderTest();
		t.strRe();
		
	}
	
	
	/**
	 * 主要针对某些特殊的网站。如：
	 * 	网站能抓到 ：共158条，分8页，当前第页      转到页 
	 * 	或者抓到：js 中含有 countPage = **页。
	 * @param pageNumInContent
	 * @return
	 */
	/*public String getTotalPageNum(String pageNumInContent,String seedUrl){
		String totalPageNum ="";
		
		if(pageNumInContent==null || pageNumInContent.equals("")){
			totalPageNum ="1";
			return totalPageNum;
		}
		//js生成的分页
		if(seedUrl.contains("http://www.flower.agri.cn/") || seedUrl.contains("http://www.tea.agri.cn")){
			String regex = "(countPage.= (\\d+))";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(pageNumInContent);
			if(matcher.find()){
				String regex1 = "(\\d+)";
				Pattern pattern1 = Pattern.compile(regex1);
				Matcher matcher1 = pattern1.matcher(matcher.group());
				System.out.println("matcher.group():"+matcher.group());
				if(matcher1.find()){
					totalPageNum = matcher1.group();
				}
			}
		}
		if(seedUrl.contains("http://www.caas.net.cn")){
			String content[] =pageNumInContent.split("，");
			String regex = "(\\d+页)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content[1]);
			if(matcher.find()){
				totalPageNum =matcher.group().replaceAll("页", "");
			}
		}
		if(seedUrl.contains("http://cxpt.agri.gov.cn") || seedUrl.contains("http://www.agri.cn")
				|| seedUrl.contains("http://catf.agri.cn") || seedUrl.contains("http://www.moa.gov.cn")
				|| seedUrl.contains("http://pfscnew.agri.gov.cn")){
			String regex = "(\\((\\d+))";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(pageNumInContent);
			if(matcher.find()){
				totalPageNum = matcher.group().replaceAll("\\(", "");
			}
		}
		
		if(seedUrl.contains("http://rdjc.lknet.ac.cn")){
			String regex = "(currpage=(\\d+))";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(pageNumInContent);
			if(matcher.find()){
				totalPageNum= matcher.group().replaceAll(".*\\=", "");
			}
		}
		
		if(seedUrl.contains("http://www.seedchina.com.cn") || seedUrl.contains("http://crop.agridata.cn")){
			
				totalPageNum = pageNumInContent;
		}
		if(seedUrl.contains("http://www.agrione.cn")){
			String [] temp =pageNumInContent.split("\\");
			totalPageNum = temp[1];
		}
		
		return totalPageNum;
	}*/
	
	public void test(){
		String url="http://gp.zgny.com.cn/Techs/Page_1_NodeId_Gp_js_zmjs.shtml";
		String [] a= url.split("\\d+");
		for(int i= 0;i<a.length;i++){
			System.out.println(a[i]);
		}
	}
	
	public Set<String> listToSet(List<String> list) {
		Set<String> set = new HashSet<>();
		set.addAll(list);
		return set;
	}
	
	public List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<>();
		list.addAll(set);
		System.out.println("list.size()"+list.size());
		return list;
	}
	
	public void strRe(){
		String c="%25CA%25FD%25BE%25DD%25BF%25E2_%25D7%25F7%25CE%25EF%25D6%25D0%25CE%25C4%25C3%25FB=%25C2%25FA%25BD%25AD%25BA%25EC%2B%2B%2B%2B&B1=%25C8%25B7%25C8%25CF";
		System.out.println(c.replaceAll("25", ""));
	}
}
