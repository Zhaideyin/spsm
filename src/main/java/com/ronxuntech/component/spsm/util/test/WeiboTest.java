package com.ronxuntech.component.spsm.util.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class WeiboTest implements PageProcessor{
	private Site site=Site.me()
			.setSleepTime(3000)
			.setRetryTimes(3)
			.setTimeOut(2000000)
//			SINAGLOBAL=1679083047666.3787.1470296738512; 
//			UOR=www.cnbeta.com,widget.weibo.com,www.molotang.com;
//			login_sid_t=09d50a178ab284dba11b1c7bdcb895dd; 
//			YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; 
//			YF-V5-G0=a9b587b1791ab233f24db4e09dad383c; 
//			_s_tentry=-; 
//			Apache=177554813570.4752.1483941681645; 
//			ULV=1483941681652:31:2:1:177554813570.4752.1483941681645:1483586181652; 
//			WBtopGlobal_register_version=c689c52160d0ea3b; 
//			SCF=Apw5cWEJEA5w7xmsKe7Kcc43wETRJ7JhLfNiPj9wWKdCVONmk5TMIRGwN7sxMAbZ9eEQr-5kXqSMF2n8idV6Mqs.; 
//			SUB=_2A251d1dvDeRxGeNH7VoT8irLzT2IHXVWBc-nrDV8PUNbmtBeLRmikW9R1o_NbaL8adBk8swdA5OPNOL9KQ..;
//			SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5W.YATwUu9XFCyYpDCZe655JpX5K2hUgL.Fo-4SonEeoBNSo22dJLoIpnLxKqLBoqL1hnLxK.L1h5L1-xk1KzRSoet;
//			SUHB=0vJIBWtDTOY9aC;
//			ALF=1484546494;
//			SSOLoginState=1483941695; 
//			un=17828086445; 
//			wvr=6;
//			YF-Page-G0=8ec35b246bb5b68c13549804abd380dc
			.setCharset("utf-8")
			/*.addCookie("SINAGLOBAL", "1679083047666.3787.1470296738512")
			.addCookie("UOR", "www.cnbeta.com,widget.weibo.com,www.molotang.com")
			.addCookie("login_sid_t", "09d50a178ab284dba11b1c7bdcb895dd")
			.addCookie("YF-Ugrow-G0", "9642b0b34b4c0d569ed7a372f8823a8e")
			.addCookie("_s_tentry", "-")
			.addCookie("ULV", "1483941681652:31:2:1:177554813570.4752.1483941681645:1483586181652")
			.addCookie("WBtopGlobal_register_version", "c689c52160d0ea3b")
			.addCookie("SCF", "Apw5cWEJEA5w7xmsKe7Kcc43wETRJ7JhLfNiPj9wWKdCVONmk5TMIRGwN7sxMAbZ9eEQr-5kXqSMF2n8idV6Mqs.")
			.addCookie("SUB", "_2A251d1dvDeRxGeNH7VoT8irLzT2IHXVWBc-nrDV8PUNbmtBeLRmikW9R1o_NbaL8adBk8swdA5OPNOL9KQ..")
			.addCookie("SUBP", "0033WrSXqPxfM725Ws9jqgMF55529P9D9W5W.YATwUu9XFCyYpDCZe655JpX5K2hUgL.Fo-4SonEeoBNSo22dJLoIpnLxKqLBoqL1hnLxK.L1h5L1-xk1KzRSoet")
			.addCookie("SUHB", "0vJIBWtDTOY9aC")
			.addCookie("ALF", "1484546494")
			.addCookie("SSOLoginState", "1483941695")
			.addCookie("un", "17828086445")
			.addCookie("wvr", "6")
			.addCookie("YF-Page-G0","8ec35b246bb5b68c13549804abd380dc")*/
			
//			_T_WM=b98ee3a454ab8d032bb1e05144723d5e; 
//			ALF=1486536963;
//			SCF=Apw5cWEJEA5w7xmsKe7Kcc43wETRJ7JhLfNiPj9wWKdCc4KAcRTEZUSP-xq_jo_9qBZwOBb4ktWZNoljkvsV9Bk.; 
//			SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5W.YATwUu9XFCyYpDCZe655JpX5o2p5NHD95Qf1KqReozXS0qpWs4Dqcjqi--ci-zciKnRi--4iKn7iKL2P0.E1hq0;
//			SUB=_2A251d0RBDeRxGeNH7VoT8irLzT2IHXVWmGwJrDV6PUJbkdBeLVndkW0-e-P0jiFqTejxlXMiwQYMz5z9zg..;
//			SUHB=0vJIBWtDTOYW0o;
//			SSOLoginState=1483944977;
//			M_WEIBOCN_PARAMS=uicode%3D20000061%26featurecode%3D20000180%26fid%3D4062018229018548%26oid%3D4062018229018548
			.addCookie("_T_WM", "b98ee3a454ab8d032bb1e05144723d5e")
			.addCookie("ALF", "1486536963")
			.addCookie("SCF", "Apw5cWEJEA5w7xmsKe7Kcc43wETRJ7JhLfNiPj9wWKdCc4KAcRTEZUSP")
			.addCookie("SUBP", "0033WrSXqPxfM725Ws9jqgMF55529P9D9W5W.YATwUu9XFCyYpDCZe655JpX5o2p5NHD95Qf1KqReozXS0qpWs4Dqcjqi--ci-zciKnRi--4iKn7iKL2P0.E1hq0")
			.addCookie("SUB", "_2A251d0RBDeRxGeNH7VoT8irLzT2IHXVWmGwJrDV6PUJbkdBeLVndkW0-e-P0jiFqTejxlXMiwQYMz5z9zg..")
			.addCookie("SUHB", "0vJIBWtDTOYW0o")
			.addCookie("SSOLoginState", "1483944977")
			.addCookie("M_WEIBOCN_PARAMS", "uicode%3D20000061%26featurecode%3D20000180%26fid%3D4062018229018548%26oid%3D4062018229018548")
			
			;
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Html html = page.getHtml();
		
		String title =html.xpath("/html/body/div[1]/div[1]/div/div/article/div/allText()").all().toString();
		System.out.println("tiltes:"+title);
	}
	
	public static void main(String[] args) {
		WeiboTest t =new WeiboTest();
		Spider.create(t).addUrl("http://m.weibo.cn/").thread(1).run();
	}

}
