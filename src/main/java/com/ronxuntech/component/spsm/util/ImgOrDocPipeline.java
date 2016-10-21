package com.ronxuntech.component.spsm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class ImgOrDocPipeline extends FilePersistentBase implements Pipeline {

	
	 
    private Logger logger = LoggerFactory.getLogger(getClass());
    public ImgOrDocPipeline() {
        setPath("/data/webmagic/");
    }
 
    public ImgOrDocPipeline(String path) {
        setPath(path);
    }
 
 
    public void process(ResultItems resultItems, Task task) {
        String fileStorePath = this.path;
        try {
        	//这没用，在后边完全要替换掉
        	String imgShortNameNew="(http://www.meizitu.com/wp-content/uploads/)|(jpg)";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            	//entry.getValue()是保存的所有链接的集合。包括获取的title.
            	if (entry.getValue() instanceof List) {
            		System.out.println("获取的是List  "+entry.getValue().toString());
            		//	取得所有的链接。
            		List listOne= (List) entry.getValue();
                    List<String> list = new ArrayList<String>();
                    
                    for(int i=0;i<listOne.size();i++){
                        list.add((String)listOne.get(i));
                    }
                   //list.get(0)是title 所以不是链接。如果从零开始要报错。
                    for(int i=1;i<list.size();i++)
                    {
                    	
                        StringBuffer sb = new StringBuffer();
                        StringBuffer imgFileNameNewYuan =sb.append(fileStorePath)
                                .append(list.get(0)) //此处提取文件夹名，即之前采集的标题名
                                .append("\\");
                        //这里先判断文件夹名是否存在，不存在则建立相应文件夹
                        Path target = Paths.get(imgFileNameNewYuan.toString());
                        System.out.println("target:  "+target);
                        if(!Files.isReadable(target)){
                            Files.createDirectory(target);
                        }
                        String extName=com.google.common.io
                                .Files.getFileExtension(list.get(i));
                        System.out.println("list.get(i):  "+list.get(i).toString());
                        StringBuffer imgFileNameNew = imgFileNameNewYuan
                                .append((list.get(i)).replaceAll(imgShortNameNew, "")
                                .replaceAll("[\\pP‘’“”]", ""))//不懂
                                .append(".")
                                .append(extName);
 
                        //这里通过httpclient下载之前抓取到的图片网址，并放在对应的文件中
                        //取得的连接中地址符号表示为 &amp;在网页中不需要amp;所以取出链接并且将连接中的amp： 去掉得到网页正的url.
                        String url = list.get(i).replace("amp;","");
                        HttpGet httpget = new HttpGet(url);
                        HttpResponse response = httpclient.execute(httpget);
                        HttpEntity entity = response.getEntity();
                        InputStream in = entity.getContent();
                        File file = new File(imgFileNameNew.toString());
 
                        try {
                            FileOutputStream fout = new FileOutputStream(file);
                            int l = -1;
                            byte[] tmp = new byte[1024];
                            while ((l = in.read(tmp)) != -1) {
                                fout.write(tmp,0,l);
                            }
                            System.out.println("tmp:"+l);
                            fout.flush();
                            fout.close();
                        } finally {
                            in.close();
                        }
 
                    }
                }
 
                else {
                    System.out.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            httpclient.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
