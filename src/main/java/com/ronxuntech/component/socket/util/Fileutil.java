package com.ronxuntech.component.socket.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.ronxuntech.util.PathUtil;

public  class Fileutil {

	/**@param fileName  文件名
	 * @param content  路径
	 * 创建本地文件
	 * @throws IOException 
	 */
	
	public static String createFile(String fileName,String content) throws  IOException{
		String  filePath = PathUtil.getClasspath()+"/uploadFiles/file/"+fileName+".xml";
		File f = new File(filePath);
		FileOutputStream fop = new FileOutputStream(f);
		// 构建FileOutputStream对象,文件不存在会自动新建
		OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
		// 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
		writer.append(content);
		// 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
		writer.close();
		//关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
		fop.close();
		// 关闭输出流,释放系统资源
		FileInputStream fip = new FileInputStream(f);
		// 构建FileInputStream对象
		InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
		// 构建InputStreamReader对象,编码与写入相同
		StringBuffer sb = new StringBuffer();
		while (reader.ready()) {
			sb.append((char) reader.read());
			// 转成char加到StringBuffer对象中
		}
		System.out.println(sb.toString());
		reader.close();
		// 关闭读取流
		fip.close();
		// 关闭输入流,释放系统资源
		return filePath;
	}
	
	/**@param path
	 * 文件路径
	 * 读取文件
	 */
	public static String readFile(String path){
		StringBuilder result = new StringBuilder();
        try{
        	File f =new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
	}
	
	/**
	 * @throws IOException 
	 * 修改本地文件内容，如果修改了说明，则修改文件名。
	 * @param filePath  文件名
	 * @param content  新的内容（路径）
	 * @param newName  新的文件名
	 * @throws  
	 */
	public static String  updateFile(String filePath,String content,String newName) throws UnsupportedEncodingException, IOException  {
		String  newPath = PathUtil.getClasspath()+"/uploadFiles/file/"+newName+".xml";
		
		if(newPath.equals(filePath)){
			System.out.println("-----路径一样-------------------------------------");
			File f = new File(filePath);
			FileOutputStream fop = new FileOutputStream(f);
			// 构建FileOutputStream对象,文件不存在会自动新建
			OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
			// 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
			writer.append(content);
			// 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
			writer.close();
			//关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
			fop.close();
			// 关闭输出流,释放系统资源
			FileInputStream fip = new FileInputStream(f);
			// 构建FileInputStream对象
			InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 构建InputStreamReader对象,编码与写入相同
			StringBuffer sb = new StringBuffer();
			while (reader.ready()) {
				sb.append((char) reader.read());
				// 转成char加到StringBuffer对象中
			}
			System.out.println(sb.toString());
			reader.close();
			// 关闭读取流
			fip.close();
		}else{
			createFile(newName, content);
			File f =new File(filePath);
			f.delete();
		}
			
			// 关闭输入流,释放系统资源
		
		return newPath;
	}
}
