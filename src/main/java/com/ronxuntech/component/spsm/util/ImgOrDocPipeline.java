package com.ronxuntech.component.spsm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ronxuntech.service.spsm.annexurl.AnnexUrlManager;
import com.ronxuntech.service.spsm.seedurl.SeedUrlManager;
import com.ronxuntech.service.spsm.seedurl.impl.SeedUrlService;
import com.ronxuntech.service.spsm.targeturl.TargetUrlManager;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class ImgOrDocPipeline extends FilePersistentBase implements Pipeline {

	private Logger logger = LoggerFactory.getLogger(getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public ImgOrDocPipeline() {
		setPath("/data/webmagic/");
	}

	/**
	 * 构造方法 （用于默认爬取时使用）
	 * 
	 * @param path
	 */
	
	public ImgOrDocPipeline(String path){
		setPath(path);
		annexurlService = (AnnexUrlManager) SpringBeanFactoryUtils.getBean("annexurlService");
		targeturlService = (TargetUrlManager) SpringBeanFactoryUtils.getBean("targeturlService");
		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
	}
	/**
	 * 构造方法 （用于重爬时使用）
	 * 
	 * @param seedUrl
	 * @param annexNameList
	 * @param annexUrlList
	 */
	public ImgOrDocPipeline(String seedUrl, List<String> annexNameList, List<String> annexUrlList) {
		this.annexNameList = annexNameList;
		annexurlService = (AnnexUrlManager) SpringBeanFactoryUtils.getBean("annexurlService");
		targeturlService = (TargetUrlManager) SpringBeanFactoryUtils.getBean("targeturlService");
		seedurlService = (SeedUrlService) SpringBeanFactoryUtils.getBean("seedurlService");
		this.annexUrlList = annexUrlList;
		this.seedUrl = seedUrl;
	}

	// 附件功能
	private AnnexUtil annexUtil = AnnexUtil.getInstance();
	private ConvertUtil convertUtil =ConvertUtil.getInstance();
	// 附件名
	private List<String> annexNameList;
	private AnnexUrlManager annexurlService;
	private TargetUrlManager targeturlService;
	private SeedUrlManager seedurlService;
	private String seedUrl;
	private List<String> annexUrlList;
	private StringBuffer imgFileNameNew = new StringBuffer();

	public StringBuffer getImgFileNameNew() {
		return imgFileNameNew;
	}

	public void setImgFileNameNew(StringBuffer imgFileNameNew) {
		this.imgFileNameNew = imgFileNameNew;
	}

	// 下载文件或图片
	public void process(ResultItems resultItems, Task task) {
		try {
			// CloseableHttpClient httpclient = HttpClients.createDefault();
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
					.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

			List<String> annexUrlList =resultItems.get("annexUrlList");
			//去重imgUrl
			convertUtil.removeDuplicateWithOrder(annexUrlList);
			List<String> annexFileNameList =resultItems.get("annexFileNameList");
			convertUtil.removeDuplicateWithOrder(annexFileNameList);
//			String pageUrl =resultItems.get("pageUrl");
			StringBuffer sb = new StringBuffer();
			StringBuffer imgFileNameNewYuan = sb.append(path); // 此处提取文件夹名，即之前采集的标题名

			// 这里先判断文件夹名是否存在，不存在则建立相应文件夹
			File fp = new File(imgFileNameNewYuan.toString());
			if (!fp.exists()) {
				fp.mkdirs();
			}
			if(annexUrlList == null || annexUrlList.size()==0){
				return;
			}
			for(int i=0;i<annexUrlList.size();i++){
				String url = annexUrlList.get(i).trim();
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				File file = new File(annexFileNameList.get(i).toString());
				// 下载附件
				downAnnex(file, url, in);
				
			}
			httpclient.close();
			
		} catch (IOException e) {
			logger.warn("write file error", e);
		}
	}		
			
	
	/**
	 * 未下载的附件重新下载附件
	 */
	/*public void reDownloadAnnex() {
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		try {
			for (int i = 0; i < annexNameList.size(); i++) {
				String dir = annexNameList.get(i).toString();
				int n = dir.lastIndexOf("/");
				dir = dir.substring(0, n);
				File fp = new File(dir);
				if (!fp.exists()) {
					fp.mkdirs();
				}
				String url = annexUrlList.get(i);
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				File file = new File(annexNameList.get(i).toString());
				// 下载附件
				downAnnex(file, url, in);

			}
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	/**
	 * 下载附件
	 * 
	 * @param file
	 * @param url
	 * @param in
	 * @throws IOException
	 */
	public void downAnnex(File file, String url, InputStream in) throws IOException {
		PageData pd = new PageData();
		pd.put("ANNEXURL", url);
		pd.put("STATUS", "1");
		List<PageData> listPd=new ArrayList<>();
		try {
			listPd = annexurlService.findByUrlAndStatus(pd);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(listPd.size()>0){
			pd.put("ANNEXURL", url);
			pd.put("STATUS", "0");
			List<PageData> listPd2= new ArrayList<>();
			try {
				listPd2 = annexurlService.findByUrlAndStatus(pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(int i =0;i<listPd2.size();i++){
				listPd2.get(i).put("STATUS", "1");
				listPd2.get(i).put("UPDATETIME", sdf.format(new Date()));
				try {
					annexurlService.edit(listPd2.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				FileOutputStream fout = new FileOutputStream(file);
				int l = -1;
				byte[] tmp = new byte[1024];
				while ((l = in.read(tmp)) != -1) {
					fout.write(tmp, 0, l);
				}
				System.out.println("tmp:" + l);
				// 当下载完成后，
				PageData pd2 = new PageData();
				pd2.put("ANNEXURL", url);
				pd2.put("STATUS", "0");
				try {
					// 先查询到该url的值
				    listPd = annexurlService.findByUrlAndStatus(pd2);
					for(int i=0;i<listPd.size();i++){
						if(listPd.get(i).getString("STATUS").equals("0")){
							listPd.get(i).put("STATUS", "1");
							
							listPd.get(i).put("UPDATETIME", sdf.format(new Date()));
							 //等下载完成， 更新数据库中的状态
							annexurlService.edit(listPd.get(i));
							break;
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				fout.flush();
				fout.close();
			
		} finally {
			in.close();
		}
		}
	}

}