package com.ronxuntech.component.spsm.util.test;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.MultiPageModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.processor.PageProcessor;



/**
 * @author code4crafter@gmail.com <br>
 */			//这个是过滤新闻页的链接
@TargetUrl("http://finance.aweb.com.cn/20170103/628162872*.shtml") 
public class News167 implements MultiPageModel{
	private Site site=Site.me().setSleepTime(1000).setRetryTimes(3).setTimeOut(2000000);
	//([^_])*
	 @ExtractByUrl("http://finance\\.aweb\\.com\\.cn/\\d+/([^_])*.*\\.shtml")
    private String pageKey;

    @ExtractByUrl(value = "http://finance\\.aweb\\.com\\.cn/\\d+/\\d+_(\\d+)\\.shtml", notNull = false)
    private String page;

    @ExtractBy(value = "//*[@class=\"pages\"]//a/regex('http://finance\\.aweb\\.com\\.cn/\\d+/\\d+_(\\d+)\\.shtml',1)"
           )
    private List<String> otherPage;

    @ExtractBy("//*[@id=\"content\"]/div[1]/h1/span/text()")
    private String title;
    @ExtractBy(value="//*[@id=\"content\"]/div[1]/html()")
    private String content; 
//    private List<String> content;
    

    
    public List<String> getOtherPage() {
		return otherPage;
	}

	public String getTitle() {
		return title;
	}

//	public Selectable getContent() {
//		return content;
//	}

	

	public String getContent(String content) {
		String regex="<p>.*公斤</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		String conten="";
		while(matcher.find()){
			conten+=matcher.group();
		}
		return conten;
	}

	private String con;
    @Override
    public String getPageKey() {
        return pageKey;
    }

    @Override
    public Collection<String> getOtherPages() {
        return otherPage;
    }

    @Override
    public String getPage() {
        if (page == null) {
            return "1";
        }
        return page;
    }

    @Override
    public MultiPageModel combine(MultiPageModel multiPageModel) {
        News167 news163 = new News167();
        news163.title = this.title;
        News167 pagedModel1 = (News167) multiPageModel;
        news163.content =this.getContent(this.content)+pagedModel1.getContent(pagedModel1.content);
        return news163;
    }

    @Override
    public String toString() {
        return "News163{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", TargetUrl=" +  page+
                ",pageKey="+pageKey+
                '}';
    }

    public static void main(String[] args) {
//        OOSpider.create(Site.me().setTimeOut(20000), News167.class).addUrl("http://finance.aweb.com.cn/20170103/628162872_2.shtml")
//                .addPipeline(new MultiPagePipeline()).addPipeline(new ConsolePipeline()).thread(5).run();
        News167 nwes167 =new News167(); 
        /*只能执行第一次
          Spider.create(new PageProcessor() {
			@Override
			public void process(Page page) {
				
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
				System.out.println(page.getHtml().xpath("//*[@id=\"content\"]/div[1]/h1/span/text()").toString());
				List<String> links =page.getHtml().xpath("//div[@class='newLists']").links().all();
				page.addTargetRequests(links);
			}
			
			@Override
			public Site getSite() {
				return nwes167.site;
			}
		}).addUrl("http://finance.aweb.com.cn/20170103/628162872.shtml").thread(4).run();*/
        
//    	Spider.create(new News167()).addUrl("http://finance.aweb.com.cn/20170103/628162872_2.shtml")
//    	.addPipeline(new ConsolePipeline()).thread(1).run();
    }

	

	/*@Override
	public void process(Page page) {
		Html html =page.getHtml();
//		List<String> links =html.links().regex("(http://finance\\.aweb\\.com\\.cn/\\d+/\\d+[^_]*.*\\.shtml)").all();
//		List<String> 
//		String 
//		nextPage 
//		= html.xpath("//*[@id=\"content\"]/div[1]/ul//a").links().regex("http://finance\\.aweb\\.com\\.cn/\\d+/\\d+_\\d+\\.shtml").all();
//				.regex("http://finance\\.aweb\\.com\\.cn/\\d+/\\d+_\\d+\\.shtml").all();
//		System.out.println("links:"+links.toString());
//		System.out.println("nextPage:"+nextPage.toString());
//		String title = html.xpath("//*[@id=\"content\"]/div[1]/h1/span/text()").toString();
//		System.out.println("titles:"+title);
//		page.addTargetRequests(links);
		
		
		Selectable contents =html.xpath("//*[@id=\"content\"]/div[1]/p");
		List<Selectable> c =contents.nodes();
		System.out.println("contents: "+contents.toString());
		
	}*/

}
