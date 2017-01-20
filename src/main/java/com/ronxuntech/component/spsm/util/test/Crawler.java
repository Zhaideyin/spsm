package com.ronxuntech.component.spsm.util.test;

public class Crawler {

	/*public void run() {

		// 存储urls文件的路径
		String filepath = "D:\\webmagic\\spsm\\urls";
		//
		Scheduler scheduler = new FileCacheQueueScheduler(filepath);
		// 如果网页有图片或者文档， 开启的线程不能太多， 因为下载文档或者图片的时候也会开启3个线程，
		// 如果开十个爬取网页，那么有图或者文档的时候，则会开启30个。这样服务器估计受不了，所以我这里选择了4个
		// 网站/标题作为文件夹 清除特殊字符
		String regex = "[:/|*?%<>\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(web.getSeed());
		String webUrl = matcher.replaceAll("").trim();
		String fileDir = PathUtil.getClasspath() + "uploadFiles/spsm/" + webUrl + "/";

		if (web.isHasDoc() || web.isHasImg()) {
			Spider.create(new BaseCrawler(web, seedUrlId)).addUrl(web.getSeed()).thread(5)
					.setDownloader(new HttpClientDownloader()).addPipeline(new SpiderPipeline(web))
					.addPipeline(new ImgOrDocPipeline(fileDir)).scheduler(scheduler).run();
		} else {
			Spider.create(new BaseCrawler(web, seedUrlId)).addUrl(web.getSeed()).thread(10)
					.setDownloader(new HttpClientDownloader()).addPipeline(new SpiderPipeline(web)).scheduler(scheduler)
					.run();
		}
	}*/
}
