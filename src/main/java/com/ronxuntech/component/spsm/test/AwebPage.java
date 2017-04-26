package com.ronxuntech.component.spsm.test;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://finance.aweb.com.cn/20170103/628162872_(\\d+)\\.shtml")
@HelpUrl("http://finance.aweb.com.cn/20170103/628162872\\.shtml")
public class AwebPage {

//	@ExtractByUrl("http://finance.aweb.com.cn/20170103/628162872\\.shtml")
//    private String pageKey;

//    @ExtractByUrl(value = "http://finance.aweb.com.cn/20170103/628162872_(\\d+)\\.shtml", notNull = true)
//    private String page;

//    private List<String> otherPage;

    @ExtractBy("//*[@id='content']/div[1]/h1/span/text()")
    private String title;

    @ExtractBy("//*[@id='content']/div[1]/p[1]/text()")
    private String content;
	
	

//	@Override
//	public String getPageKey() {
//		return pageKey;
//	}

//	@Override
//	public Collection<String> getOtherPages() {
//		return otherPage;
//	}

//	@Override
//	public String getPage() {
//		if (page == null) {
//            return "1";
//        }
//        return page;
//	}

//	@Override
//	public PagedModel combine(PagedModel pagedModel) {
//		AwebPage awebPage =new AwebPage();
//		AwebPage awebPage1 =(AwebPage)awebPage;
//		awebPage.content =this.content+awebPage1.content;
//		return awebPage;
//	}

	@Override
	public String toString() {
		return super.toString();
	}
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public static void main(String[] args) {
        OOSpider.create( Site.me(), new ConsolePageModelPipeline(), AwebPage.class).
        addUrl("http://finance.aweb.com.cn/20170103/628162872.shtml").thread(1).run();
        
       
    }
//	@Override
//	public void afterProcess(Page page) {
//		Selectable xpath = page.getHtml().xpath("//div[@class=\"ep-pages\"]//a/@href");
//        otherPage = xpath.regex("http://news\\.163\\.com/\\d+/\\d+/\\d+/\\w+_(\\d+)\\.html").all();
//	}
	
	

}
