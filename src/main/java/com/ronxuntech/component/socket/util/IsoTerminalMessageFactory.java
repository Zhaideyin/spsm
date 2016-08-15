package com.ronxuntech.component.socket.util;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 终端报文工具
 * @author train
 *
 */
public class IsoTerminalMessageFactory {
	
	private String packet_encoding="UTF-8";
	
	private String path;
	
	private String TPDU="6009028275";
	private String HEADER="603100310032";//12位BCD，6字节
	
	public IsoTerminalMessageFactory(String path){
		this.path=getClass().getResource("/").getFile().toString()+path;
	}
	// 8583报文初始位图:64位01字符串
	public String getInitBitMap(){
		String initBitMap = 
		  "00000000" + "00000000" + "00000000" + "00000000" 
		+ "00000000" + "00000000" + "00000000" + "00000000";
		return initBitMap;
	}

	/**
	 * 解析报文
	 * @param bytes
	 * @param len 头长度
	 * @return
	 */
	public LinkedHashMap<String,Object> unpack(byte[] bytes,int len){

		int bitMapLength=16;
		LinkedHashMap<String,Object> filedMap=new LinkedHashMap<String,Object>();
		try {
			
			//截掉头长度
			byte[] bitMap = new byte[bitMapLength];
			System.arraycopy(bytes, len+4, bitMap, 0, bitMapLength);
			String bitMapStr=ParseUtil.bcdhex2binary(new String(bitMap));
			//记录当前位置,从位图后开始遍历取值 
			int pos = len+4+bitMapLength;
	        for (int i = 0; i < bitMapStr.length(); i++) {
	        	String filedValue = "";//字段值
	        	String filedName = "Bit " + (i+1);
	        	
		        if (i!=0&&bitMapStr.charAt(i) == '1') {
		        	//获取域定义信息
		        	Map<String,Object> field=getField(i+1);
		        	String defType=field.get("type").toString();//类型定义例string
					int defLen=(int) field.get("length");//长度定义,例20
					boolean isFixLen=true;//是否定长判断
					boolean isBCD=true;
					
					if(defType.indexOf(".")>0){//变长域
						isFixLen=false;
						defLen=StringUtil.countCharAtString(defType,".".charAt(0));//获取类型后面.的个数
					}
					// 截取该域信息
					if (!isFixLen) {//变长域
						int defLen1 =defLen;
						if(defLen1%2!=0)defLen1++;
						String realLenStr=new String(bytes, pos, defLen1, packet_encoding);//报文中实际记录域长,例如16,023
						
						int realLen=Integer.valueOf(realLenStr);
						if(defType.startsWith("ascii")||defType.startsWith("mix")){
							realLen=Integer.valueOf(realLenStr)*2;
							isBCD=false;
						}

						int realAllLen=defLen1+realLen;//该字段总长度（包括长度值占的长度）

						byte[] filedValueByte=new byte[Integer.valueOf(realLen)];
						System.arraycopy(bytes, pos+defLen1, filedValueByte, 0, filedValueByte.length);
						filedValue=new String(filedValueByte,packet_encoding);
						pos += realAllLen;//记录当前位置
						if(isBCD&&realLen%2!=0){
							//单数长度位数，BCD压缩后多一位
							pos++;
						}
					} else {//定长域
						int defLen2 = defLen;
						if(defType.startsWith("ascii")||defType.equals("b")){
							defLen2=defLen2*2;
						}
						
						filedValue = new String(bytes, pos, defLen2, packet_encoding);
						pos += defLen2;//记录当前位置
					}
					if(defType.startsWith("ascii")){
						filedValue=ParseUtil.hexToString(filedValue);
					}
					filedMap.put(filedName, filedValue);
		        	
		        }
	        }
        }catch (Exception e) {
			e.printStackTrace();
		}
		return filedMap;
	}
	/**
	 * 组装报文
	 * @param datafiledMap
	 * @return
	 */
	public byte[] pack(LinkedHashMap<String,Object> datafiledMap,String mti){
		Map<String,Object> all=formatValueTo8583(datafiledMap,this.getInitBitMap());
		try {
			String  bitMap=(String)all.get("bitMap");
			// 128域位图二进制字符串转16位16进制
			byte[] bitmaps= ParseUtil.binary2hex(bitMap).getBytes();
			
			LinkedHashMap pacBody=(LinkedHashMap)all.get("formatedFiledMap");
			StringBuffer last128=new StringBuffer();
			Iterator it=pacBody.keySet().iterator();
			for(;it.hasNext();){
				String key=(String)it.next();
				String value=(String)pacBody.get(key);
				last128.append(value);
			}
			byte[] bitContent = last128.toString().getBytes(packet_encoding);//域值
			
			//组装
			byte[] package8583=null;
			
			package8583=StringUtil.arrayApend(package8583,TPDU.getBytes());
			package8583=StringUtil.arrayApend(package8583,HEADER.getBytes());
			package8583=StringUtil.arrayApend(package8583,mti.getBytes());
			package8583=StringUtil.arrayApend(package8583, bitmaps);
			package8583=StringUtil.arrayApend(package8583, bitContent);
			//报文长度,2字节，不够补0
			int len=package8583.length;
			String hex=ParseUtil.intToHex(len/2);
			hex=strCopy("0",(4-hex.length()))+hex;
			package8583=StringUtil.arrayApend(hex.getBytes(),package8583);
			return package8583;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	public Map<String,Object> formatValueTo8583(LinkedHashMap<String,Object> filedMap,String  bitMap){
		Map<String,Object> map=new HashMap<String,Object>();
		LinkedHashMap<String,Object> formatedFiledMap=new LinkedHashMap<String,Object>();
		if(filedMap!=null){
			Iterator it=filedMap.keySet().iterator();
			for(;it.hasNext();){
				String fieldId=(String)it.next();
				String fieldValue=(String)filedMap.get(fieldId);
				
				try{
					if (fieldValue == null) {
						System.out.println("error:报文域 {" + fieldId + "}为空值");
						fieldValue = "";
						return null;
					}
					//将域值编码转换，保证报文编码统一
					fieldValue=new String(fieldValue.getBytes(packet_encoding),packet_encoding);
					// 组二进制位图串
					bitMap = changeBitMapFlag(fieldId, bitMap);
					//获取域定义信息
		        	Map<String,Object> field=getField(Integer.parseInt(fieldId));
		        	String defType=field.get("type").toString();//类型定义例string
					int defLen=(int) field.get("length");//长度定义,例20
					boolean isFixLen=true;//是否定长判断
					boolean isBCD=true;
					if(defType.startsWith("ascii")||defType.startsWith("mix")||defType.equals("b")){
						isBCD=false;
					}
					
					if(defType.indexOf(".")>0){//变长域
						isFixLen=false;
						defLen=StringUtil.countCharAtString(defType,".".charAt(0));//获取类型后面.的个数
					}
					//如果是ascii变长，需要还原出来计算长度
					if(!isFixLen&&defType.startsWith("ascii")){
						fieldValue=ParseUtil.stringToHex(fieldValue);
					}
					int fieldLen = fieldValue.getBytes(packet_encoding).length;//字段值得实际长度
					
					if (!isFixLen) {
						// 变长域(变长域最后组装成的效果：例如变长3位，这里的3是指长度值占3位，字段值是123456，最后结果就是006123456)
						int maxLen=(int) field.get("length");
						
						int defLen1 = Integer.valueOf(defLen);
						if(defLen1%2!=0)defLen1++;
						if (fieldLen > maxLen) {
							System.out.println("error:字段" + fieldId + "的数据定义长度的长度为" + defLen + "位,长度不能超过"+(10*defLen1));
							return null;
						}else{
							//将长度值组装入字段
							int len=Integer.parseInt(getVaryLengthValue(fieldValue, defLen1));

							if(defType.startsWith("mix")||defType.startsWith("ascii")){
								len=len/2;
							}
							String str=String.valueOf(len);
							if(str.length()!=defLen1){
								//补0
								str=strCopy("0",(defLen1-str.length()))+len;
							}
							
							
							fieldValue = str + fieldValue;

							//如果位数长度是奇数,并且是bcd编码，需要补0
							if(isBCD&&len%2!=0){
								fieldValue+="0";
							}
						}
					}else{
						//定长域(一个字段规定是N位，那么字段值绝对不能超过N位，不足N位就在后面补空格)
						int defLen2 = Integer.valueOf(defLen);
						if(defType.equals("b")){
							defLen2=defLen2*2;
						}
						if (fieldLen > defLen2) {
							System.out.println("error:字段" + fieldId + "的数据定义长度为" + defLen + "位,长度不能超过"+defLen);
							return null;
						}else{
							fieldValue=getFixFieldValue(fieldValue,defLen2);//定长处理
							if(defType.startsWith("ascii")){
								fieldValue=ParseUtil.stringToHex(fieldValue);
							}
						}
						
					}

					// 返回结果赋值
					if (filedMap.containsKey(fieldId)) {
						if (formatedFiledMap.containsKey(fieldId)) {
							formatedFiledMap.remove(fieldId);
						}
						
						formatedFiledMap.put(fieldId, fieldValue);
					} else {
						System.out.println("error:" +fieldId + "配置文件中不存在!");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		map.put("formatedFiledMap", formatedFiledMap);
		map.put("bitMap", bitMap);
		return map;
	}
	public Map<String,Object> getField(int id){
		 File myXML = new File(path);  
         SAXReader sr = new SAXReader();  
         try {  
            Document doc  =  sr.read(myXML);  
            Element root = doc.getRootElement();
            for (Iterator e = root.elementIterator();e.hasNext();) {  
                Element node = (Element) e.next();
                if(Integer.parseInt(node.attribute("id").getData().toString().trim())==id){
                	Map<String,Object> field=new HashMap<String,Object>();
                	field.put("id", id);
                	field.put("length", Integer.parseInt(node.attribute("length").getData().toString().trim()));
                	field.put("type", node.attribute("type").getData());
                	return field;
                }
            }
            return null;
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }
         return null;
	}
	public static String changeBitMapFlag(String fieldNo, String res) {
		int indexNo=Integer.parseInt(fieldNo);
		res = res.substring(0, indexNo-1) + "1" + res.substring(indexNo);
		return res;
	}
	/**
	 * 获取字符串变长值
	 * @param valueStr
	 * @param defLen
	 * 例如getFixLengthValue("12345678", 2)返回08
	 * 例如getFixLengthValue("12345678", 3)返回0008
	**/
	public String getVaryLengthValue(String valueStr,int defLen){
		return getVaryLengthValue(valueStr,defLen,packet_encoding);
	}
	public String getVaryLengthValue(String valueStr,int defLen,String encoding){
		
		String fixLen="";
		try{
			if(valueStr==null){
				return strCopy("0",defLen);
			}else{
				byte[] valueStrByte=null;
				//这里最好指定编码，不使用平台默认编码
				if(encoding==null||encoding.trim().equals("")){
					valueStrByte=valueStr.getBytes();
				}else{
					valueStrByte=valueStr.getBytes(encoding);
				}
				int len=valueStrByte.length;//字段实际长度
				String len1=String.valueOf(len);
				fixLen=strCopy("0",(defLen-len1.length()))+len1;
			} 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return fixLen;
	}
	
	/**
	 * 将字段值做定长处理，不足定长则在后面补空格
	 * @param valueStr
	 * @param defLen
	 * @return
	 */
	public String getFixFieldValue(String valueStr,int defLen){
		return getFixFieldValue(valueStr,defLen,packet_encoding);
	}
	public String getFixFieldValue(String valueStr,int defLen,String encoding){
		String fixLen="";
		try {
			if(valueStr==null){
				return strCopy(" ",defLen);
			}else{
				byte[] valueStrByte=null;
				//这里最好指定编码，不使用平台默认编码
				if(encoding==null||encoding.trim().equals("")){
					valueStrByte=valueStr.getBytes();
				}else{
					valueStrByte=valueStr.getBytes(encoding);
				}
				//长度的判断使用转化后的字节数组长度，因为中文在不同的编码方式下，长度是不同的，GBK是2，UTF-8是3，按字符创长度算就是1.
				//解析报文是按照字节来解析的，所以长度以字节长度为准，防止中文带来乱码
				if(valueStrByte.length>defLen){
					return null;
				}else{
					fixLen=valueStr+strCopy(" ",defLen-valueStrByte.length);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return fixLen;
	}
	public static String strCopy(String str,int count){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i < count;i++){
			sb.append(str);
		}
		return sb.toString();
	}
}
