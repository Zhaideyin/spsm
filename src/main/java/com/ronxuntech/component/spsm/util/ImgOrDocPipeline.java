package com.ronxuntech.component.spsm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 
    public ImgOrDocPipeline(String path,List<String> annexNameList) {
        setPath(path);
        annexUtil =new AnnexUtil();
        this.annexNameList=annexNameList;
    }
    // 附件名
    private	List<String> annexNameList;
    private AnnexUtil annexUtil;
	private  StringBuffer imgFileNameNew =new StringBuffer();
 
	public StringBuffer getImgFileNameNew() {
		return imgFileNameNew;
	}

	public void setImgFileNameNew(StringBuffer imgFileNameNew) {
		this.imgFileNameNew = imgFileNameNew;
	}

	public void process(ResultItems resultItems, Task task) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            	//entry.getValue()是保存的所有链接的集合。包括获取的title.
            	if (entry.getValue() instanceof List) {
            		//	取得所有的链接。
            		List listOne= (List) entry.getValue();
                    List<String> list = new ArrayList<String>();
                    
                    for(int i=0;i<listOne.size();i++){
                        list.add((String)listOne.get(i));
                    }
                   //list.get(0)是title 所以不是链接。如果从零开始要报错。
                    for(int i=1;i<list.size();i++)
                    {
                    	System.out.println("listSize():"+list.size());
                        StringBuffer sb = new StringBuffer();
                        StringBuffer imgFileNameNewYuan =sb.append(path); //此处提取文件夹名，即之前采集的标题名
                                
                        //这里先判断文件夹名是否存在，不存在则建立相应文件夹
                        File fp=new File(imgFileNameNewYuan.toString());
                        if(!fp.exists()){
                        	fp.mkdirs();
                        }
                        //这里通过httpclient下载之前抓取到的图片网址，并放在对应的文件中
                        //取得的连接中地址符号表示为 &amp;在网页中不需要amp;所以取出链接并且将连接中的amp： 去掉得到网页正的url.
                        String url = list.get(i).replace("amp;","");
                        System.out.println("imgOrDocUrl:"+url);
                        HttpGet httpget = new HttpGet(url);
                        HttpResponse response = httpclient.execute(httpget);
                        HttpEntity entity = response.getEntity();
                        InputStream in = entity.getContent();
                        File file = new File(path+annexNameList.get(i-1).toString());
 
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
