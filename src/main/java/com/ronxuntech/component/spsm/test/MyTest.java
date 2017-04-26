package com.ronxuntech.component.spsm.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/spring/ApplicationContext.xml")
public class MyTest implements PageProcessor{

//	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(200000).setCycleRetryTimes(3).setCharset("gb2312")
//		.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

	private Site site = Site.me().setRetryTimes(3).setSleepTime(5000)
			.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.addHeader("Accept-Encoding", "gzip, deflate, sdch")
			.addHeader("Accept-Language", "zh-CN,zh;q=0.8")
			.addHeader("Connection", "keep-alive")
			.addHeader("Cookie", "pageReferrInSession=http%3A//rd2.zhaopin.com/RdApply/Resumes/Apply/index; LastCity=%e6%b7%b1%e5%9c%b3; LastCity%5Fid=765; JSSearchModel=0; notlogin=1; utype=204958515; _jzqy=1.1490174984.1490174984.1.jzqsr=baidu|jzqct=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98.-; __xsptplus30=30.1.1490174987.1490174987.1%231%7Cother%7Ccnt%7C121122523%7C%7C%23%23qBreflE5C0NtJKjUeQ1u_BrcjB1lptux%23; __zpWAM=1490178597568.176653.1490178598.1490178598.1; LastSearchHistory=%7b%22Id%22%3a%220bf66d5e-b100-44c0-8122-6d2118ceee67%22%2c%22Name%22%3a%22java+%2b+%e6%b7%b1%e5%9c%b3%22%2c%22SearchUrl%22%3a%22http%3a%2f%2fsou.zhaopin.com%2fjobs%2fsearchresult.ashx%3fjl%3d%25e6%25b7%25b1%25e5%259c%25b3%26kw%3djava%26sm%3d0%26p%3d1%22%2c%22SaveTime%22%3a%22%5c%2fDate(1490181205805%2b0800)%5c%2f%22%7d; _jzqx=1.1489390620.1490236829.3.jzqsr=passport%2Ezhaopin%2Ecom|jzqct=/account/login.jzqsr=zhaopin%2Ecom|jzqct=/; _jzqa=1.3163171851433431000.1489390620.1490174984.1490236829.5; Home_ResultForCustom_isOpen=false; urlfrom=121113803; urlfrom2=121113803; adfcid=pzzhubiaoti; adfcid2=pzzhubiaoti; adfbid=0; adfbid2=0; dywez=95841923.1490945364.1.12.dywecsr=other|dyweccn=121113803|dywecmd=cnt|dywectr=%E6%99%BA%E8%81%94; __utmt=1; JsOrglogin=614875547; at=a10a55689ec04941b7d89c90a8a8dd9a; Token=a10a55689ec04941b7d89c90a8a8dd9a; rt=d62c33a40a504b7c8c9888227b1a0808; uiioit=3D79306C4D735665546443640832516841745970426450645F77263176645579456C437350655064456400325068497457702; xychkcontr=13442140%2c0; lastchannelurl=https%3A//passport.zhaopin.com/org/login; JsNewlogin=204958515; cgmark=2; NewPinLoginInfo=; NewPinUserInfo=; isNewUser=1; ihrtkn=; RDpUserInfo=; RDsUserInfo=24342E695571597944320775406A5E710C6A5C68406B4C7409333979246B4C34196905710A7906324775546A08715B6A1B68146B427467333B79576B3DBC550CD23FE0379CA208753B6A2F71096A5868426B4D74063347795B6B45345A69537129793A320E75880758263634C6EA0B0DA80DB2517C1E3AFD1D65923A53713F7939320E75486A2D710F6A2C683C6B447445331479046B16341069007102790E325B751C6A0571536A2968026B1474533307791E6B5C3409690771197911321D751A6A0471596A4768126B1774093326793E6B4C345B6953712A7920320E754A6A4671076A5868516B487407334F79596B413451692C712F7949320275496A5A71066A5B68466B4B7404334279516B353424695571C4FB0E54CE233CFC621664FC0539893842747E333879576B41345A6958715A7944320375496A5A710F6A2A68356B447407334679526B4A343F693C715679453203754B6A5071776A28684C6B3A7471334479596B45345E695A715A79403203754B6A5071706A28684C6B3A7471334479596B45345E695A715A79403203754B6A2F710D6A5968436B4C74023345795A6B41345A6958715B794F3277753A6A5671046A5268226B30740F334679516B38343A695571597940320175576A5A71076A50684A6B38747E3348795A6B4A348; getMessageCookie=1; ensure=1; SearchHead_Erd=rd; dywea=95841923.1196958447980452000.1490945364.1490945364.1490945364.1; dywec=95841923; dyweb=95841923.12.9.1490945464983; __utma=269921210.1687744835.1489390494.1490256299.1490945365.21; __utmb=269921210.12.9.1490945464991; __utmc=269921210; __utmz=269921210.1490945365.21.15.utmcsr=other|utmccn=121113803|utmcmd=cnt; Hm_lvt_38ba284938d5eddca645bb5e02a02006=1490236808,1490236888,1490252104,1490945364; Hm_lpvt_38ba284938d5eddca645bb5e02a02006=1490945490; Home_ResultForCustom_orderBy=DATE_MODIFIED%2C1; Home_ResultForCustom_searchFrom=custom")
			.addHeader("Host", "rdsearch.zhaopin.com")
			.addHeader("Referer", "http://rdsearch.zhaopin.com/Home/SearchByCustom?source=rd")
			.addHeader("Upgrade-Insecure-Requests", "1")
			.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

	public static final String URL_LIST="http://rdsearch.zhaopin.com/.*";//内容详情页
	public static final String SEC_LIST=" http://rd.zhaopin.com/resumepreview/resume/viewone/2/";//列表页的地址
	public static final String URL_HOME="http://rdsearch.zhaopin.com/Home/ResultForCustom?SF_1_1_1=java&SF_1_1_18=765&orderBy=DATE_MODIFIED,1&SF_1_1_27=0&exclude=1";//主页

	@Override
	public void process(Page page) {
		String rawText = page.getRawText();
		Html html=new Html(rawText);
		System.out.println(html);
		String t = html.xpath("//div[@class='resumes-index-main']/div[5]/form/table/tbody//td[@class='first-weight']/a/@t").get();
		String k=html.xpath("//div[@class='resumes-index-main']/div[5]/form/table/tbody//td[@class='first-weight']/a/@k").get();
		String resumeurlpart=html.xpath("//div[@class='resumes-index-main']/div[5]/form/table/tbody//td[@class='first-weight']/a/@resumeurlpart").get();

		String URL =SEC_LIST+resumeurlpart+"?searchresume=1&t="+t+"&k="+k+"&v=0";

		page.addTargetRequest(URL);
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new MyTest()).addUrl(URL_HOME).thread(1).run();
	}
}
