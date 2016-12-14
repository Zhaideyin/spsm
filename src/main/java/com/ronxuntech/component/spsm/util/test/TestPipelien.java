package com.ronxuntech.component.spsm.util.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class TestPipelien implements Pipeline{

	@Override
	public void process(ResultItems resultItems, Task task) {
		String content = resultItems.get("content").toString();
		File file=new File("D:\\webmagic/a.txt");
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
	         BufferedWriter bw = new BufferedWriter(fw);
	         bw.write(content);
	         bw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
