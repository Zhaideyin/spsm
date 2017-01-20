package com.ronxuntech.component.spsm.util.test;

import us.codecraft.webmagic.MultiPageModel;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;

import java.util.Collection;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */    
//@TargetUrl("http://news.163.com/\\d+/\\d+/\\d+/\\w+*.html") 
//@HelpUrl("http://finance\\.aweb\\.com\\.cn/\\d+/\\d+_\\d+.shtml")
@TargetUrl("http://news.szhk.com/\\d+/\\d+/\\d+/\\d+.html")
public class News164 implements MultiPageModel {

    @ExtractByUrl("http://news.szhk.com/\\d+/\\d+/\\d+/\\d+.html")
    private String pageKey;

    @ExtractByUrl(value = "http://finance\\.aweb\\.com\\.cn/.*\\.shtml", notNull = false)
    private String page;

    @ExtractBy(value="//*[@id=\"content\"]/div[1]/ul//a/regex('http://finance\\.aweb\\.com\\.cn/.*\\.shtml',1)" , multi = true, notNull = false)
    private List<String> otherPage;

    @ExtractBy("//*[@id=\"content\"]/div[1]/h1/span/text()")
    private String title;

  //*[@id="content"]/div[1]
  //    @ExtractBy("//*[@id=\"content\"]/div[1]/p[1]")
    @ExtractBy("//*[@id=\"content\"]/div[1]")
    private String content;

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
        News164 news164 = new News164();
        news164.title = this.title;
        News164 pagedModel1 = (News164) multiPageModel;
        news164.content = this.content + pagedModel1.content;
        return news164;
    }

    @Override
    public String toString() {
        return "News163{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", otherPage=" + otherPage +
                '}';
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me(), News164.class).addUrl("http://news.szhk.com/2017/01/05/282958211780211_2.html")
        		    .addPipeline(new MultiPagePipeline()).addPipeline(new ConsolePipeline()).run();
    }

}
