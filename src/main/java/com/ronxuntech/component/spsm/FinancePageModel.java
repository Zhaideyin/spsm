package com.ronxuntech.component.spsm;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.MultiPageModel;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

//@ExtractBy("//*[@id=\"content\"]//a")
@TargetUrl("http://finance.aweb.com.cn/\\d+/\\d+*.shtml")
public class FinancePageModel implements MultiPageModel{
	@ExtractByUrl("http://finance.aweb.com.cn/\\d+/([^_])*.*\\.shtml")
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
    
    //过滤掉没有用的东西
    public String returnContent(String content) {
		String regex="<p>.*</p>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		String conten="";
		while(matcher.find()){
			conten+=matcher.group();
		}
		return conten;
	}

    
    
    public String getTitle() {
		return title;
	}



	public String getContent() {
		return returnContent(this.content);
	}


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
    public String toString() {
        return "FinancePageModel{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", otherPage=" + otherPage +
                '}';
    }

    @Override
    public MultiPageModel combine(MultiPageModel multiPageModel) {
    	FinancePageModel financePageModel = new FinancePageModel();
    	financePageModel.title = this.title;
        FinancePageModel pagedModel1 = (FinancePageModel) multiPageModel;
        financePageModel.content = this.content+pagedModel1.content;
        return financePageModel;
    }

    public static void main(String[] args) {
    	OOSpider.create(Site.me().addStartUrl("http://finance.aweb.com.cn/industry/news/"),FinancePageModel.class).run();
	}

}
