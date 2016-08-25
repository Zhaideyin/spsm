/*package com.ronxuntech.component.socket.util;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.ronxuntech.component.socket.ServerInHandler.Message;
*//**
 * 渠道报文工具
 * BitMap为128位
 * 域值均为ASCII，无压缩
 * @author train
 *
 *//*
public class IsoChannelMessageFactory1 {
	private String packet_encoding="UTF-8";
	private String path;
	private String TPDU="6009028275";
	private String HEADER="603100310032";//12位BCD，6字节
	
	public IsoChannelMessageFactory1(String path){
//		this.path=getClass().getResource("/").getFile().toString()+path;
		this.path=path;
	}
	// 8583报文初始位图:128位01字符串,这个位图是写死的
	
	public String getInitBitMap128(){
		String initBitMap = 
		  "10000000" + "00000000" + "00000000" + "00000000" 
		+ "00000000" + "00000000" + "00000000" + "00000000"
		+ "00000000" + "00000000" + "00000000" + "00000000"
		+ "00000000" + "00000000" + "00000000" + "00000000";
		return initBitMap;
	}
	*//**
	 * 解析报文
	 * @param bytes
	 * @param len渠道的头长度为0
	 * @return
	 *//*
	public LinkedHashMap<String,Object> unpack(byte[] bytes,int len){
		System.out.println(new String(bytes));
		//bitmap为16字节的ASCII，截取后转换成16进制
		int bitMapLength=32;
		//重新格式化为ASCII(4字节)类型+16进制(16字节)位图+ASCII+16进制(8字节)
		byte[] type = new byte[8];
		byte[] bitMap = new byte[bitMapLength];
		byte[] body=new byte[bytes.length-8-bitMapLength-16];
		byte[] mac=new byte[16];//加密字符
		System.arraycopy(bytes, 0, type, 0,8);
		System.arraycopy(bytes, 8, bitMap, 0,bitMapLength);
		System.arraycopy(bytes, 8+bitMapLength, body, 0,bytes.length-8-bitMapLength-16);
		System.arraycopy(bytes, bytes.length-16, mac, 0,16);
		//重新组装bytes
		bytes=StringUtil.arrayApend(ParseUtil.hexToString(new String(type)).getBytes(),bitMap);
		//body转换成ascii
		bytes=StringUtil.arrayApend(bytes,ParseUtil.hexToString(new String(body)).getBytes());
		bytes=StringUtil.arrayApend(bytes,mac);

		LinkedHashMap<String,Object> filedMap=new LinkedHashMap<String,Object>();
		try {
			//截掉头长度
			System.arraycopy(bytes, len+4, bitMap, 0, bitMapLength);
			//转换成16进制后再转换为2进制
			String bitMapStr=ParseUtil.bcdhex2binary(new String(bitMap));
			//记录当前位置,从位图后开始遍历取值 
			int pos = len+4+bitMapLength;
	        for (int i = 0; i < bitMapStr.length(); i++) {
	        	String filedValue = "";//字段值
	        	String filedName = "Bit "+(i+1);//修改为整型，原来为String
	        	
		        if (i!=0&&bitMapStr.charAt(i) == '1') {
		        	//获取域定义信息
		        	Map<String,Object> field=getField(i+1);
		        	String defType=field.get("type").toString();//类型定义例string
					int defLen=(int) field.get("length");//长度定义,例20
					boolean isFixLen=true;//是否定长判断

					
					if(defType.indexOf(".")>0){//变长域
						isFixLen=false;
						defLen=StringUtil.countCharAtString(defType,".".charAt(0));//获取类型后面.的个数
					}
					// 截取该域信息
					if (!isFixLen) {//变长域
						int defLen1 =defLen;
                        
						String realLenStr=new String(bytes, pos, defLen1, packet_encoding);//报文中实际记录域长,例如16,023
						
						int realLen=Integer.valueOf(realLenStr);
					
						int realAllLen=defLen1+realLen;//该字段总长度（包括长度值占的长度）
						byte[] filedValueByte=new byte[Integer.valueOf(realLen)];
						System.arraycopy(bytes, pos+defLen1, filedValueByte, 0, filedValueByte.length);
						filedValue=new String(filedValueByte,packet_encoding);
						pos += realAllLen;//记录当前位置

					} else {//定长域
						int defLen2 = defLen;
						if(defType.startsWith("ascii")||defType.equals("b")){
							defLen2=defLen2*2;
						    }
						filedValue = new String(bytes, pos, defLen2, packet_encoding);
						pos += defLen2;//记录当前位置
					}
					System.out.println("***************域——————值***************");
					System.out.println(filedName+" ，"+filedValue);
					filedMap.put(filedName, filedValue);
		        	
		        }
	        }
        }catch (Exception e) {
			e.printStackTrace();
		}
		return filedMap;
	}
	*//**
	 * 根据交易码的不同返回，需要组装的域
	 * @param 根据交易码的不同返回的域的相应配置文件名xmlPath
	 * @return 放回相应的map
	 *//*
	public  LinkedHashMap<Integer,Object> getReponseFiled(String xmlPath)
	{     String  xmlPath1=getClass().getResource("/").getFile().toString()+xmlPath; 
		  LinkedHashMap<Integer,Object> fileds=new  LinkedHashMap<Integer,Object>();
		  File myXML = new File(xmlPath1);  
          SAXReader sr = new SAXReader();
		try {
			Document  doc = sr.read(myXML);
			Element root = doc.getRootElement();
			for (Iterator e = root.elementIterator();e.hasNext();){
			      Element node = (Element) e.next();
			      if(node.elementText("arrange").equals("m"))
				  fileds.put(Integer.parseInt(node.elementText("id")), node.elementText("arrange"));
			}
			
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
       System.out.println("需要返回的域"+fileds.toString());
		return fileds;
		
	}
	*//**
	 * 
	 * @param datafiledMap 解析后得到的map
	 * @param xmlPath 配置文件 例如190000xml
	 * @param m 获得返回的类型
	 * @return
	 *//*
	
	public byte[] packNew(LinkedHashMap<String,Object> datafiledMap,String xmlPath, Message m){
		LinkedHashMap<Integer,Object> field=getReponseFiled(xmlPath);
		Map<String,Object> map=formatValueTo8583(datafiledMap);
		LinkedHashMap<String,Object> formatedFiledMap=(LinkedHashMap<String, Object>) map.get("formatedFiledMap");
		LinkedHashMap<String,Object> returnMap=new LinkedHashMap<String,Object>();
		System.out.println("*******解析后得到的域********"+formatedFiledMap.toString());
		Iterator it1=field.keySet().iterator();
		Date date1=new Date();
		while(it1.hasNext()){
			Integer fieldId=(Integer)it1.next();
			System.out.println("fieldid:"+fieldId);
			String fieldValue1=(String)datafiledMap.get(fieldId);
			if(fieldId==7){
				SimpleDateFormat df0 = new SimpleDateFormat("MMddHHmmss");
				returnMap.put("Bit 7", df0.format(date1));
				continue;
			}
			if(fieldId==12){
				SimpleDateFormat df1 = new SimpleDateFormat("HHmmss");
				returnMap.put("Bit 12", df1.format(date1));
				continue;
			}
			if(fieldId==13){
				SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
				returnMap.put("Bit 13", df2.format(date1));
				continue;
			}
			returnMap.put("Bit "+fieldId, fieldValue1);
			
		}
		System.out.println("*******组装后的域**********"+returnMap.toString());
		byte[] returnBytes=pack(returnMap,m);
		 System.out.println("返回的报文*************"+new String(returnBytes));
		return returnBytes;
	}
	*//**
	 * 组装报文
	 * @param datafiledMap 解析后得到的报文的域的map
	 * @param message 报文,
	 * @return
	 *//*
	public byte[] pack(LinkedHashMap<String,Object> datafiledMap, Message m){
	   String reponseType=m.getReturnType();
	   Map<String,Object> all=formatValueTo8583(datafiledMap);
		try {
			String  bitMap=(String)all.get("bitMap");
			// 128域位图二进制字符串转16位16进制
			byte[] bitmaps= ParseUtil.binary2hex(bitMap).getBytes();
			LinkedHashMap pacBody=(LinkedHashMap)all.get("formatedFiledMap");
			StringBuffer last128=new StringBuffer();
			Iterator it=pacBody.keySet().iterator();
			for(;it.hasNext();){
				Integer key=(Integer)it.next();
				String value=(String)pacBody.get(key);
				last128.append(value);
			}
	
			byte[] bitContent = ParseUtil.stringToHex(last128.toString()).getBytes(packet_encoding);//域值,转换成16进制
			
			//组装
			byte[] package8583=null;
			
			//package8583=StringUtil.arrayApend(package8583,TPDU.getBytes());
			//package8583=StringUtil.arrayApend(package8583,HEADER.getBytes());
			package8583=StringUtil.arrayApend(package8583,ParseUtil.stringToHex(reponseType).getBytes());
			package8583=StringUtil.arrayApend(package8583, bitmaps);
			package8583=StringUtil.arrayApend(package8583, bitContent);
			//报文长度,2字节，不够补0
			int len=package8583.length;
			String hex=ParseUtil.intToHex(len/2);
			hex=strCopy("0",(4-hex.length()))+hex;
			//package8583=StringUtil.arrayApend(hex.getBytes(),package8583);
			return package8583;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	*//**
	 * 
	 * 
	 * @param datafiledMap 解析后得到的域的map
	 * @param bitMap 位图
	 * @return
	 *//*
	public Map<String,Object> formatValueTo8583(LinkedHashMap<String, Object> datafiledMap){
		String bitMap=this.getInitBitMap128();
		Map<String,Object> map=new HashMap<String,Object>();
		LinkedHashMap<String,Object> formatedFiledMap=new LinkedHashMap<String,Object>();
		if(datafiledMap!=null){
			Iterator it=datafiledMap.keySet().iterator();
			for(;it.hasNext();){
				String fieldId=(String)it.next();
				String fieldValue=(String)datafiledMap.get(fieldId);
				try{
					if (fieldValue == null){
						System.out.println("error:报文域 {" + fieldId + "}为空值");
						fieldValue = "";
						return null;
					}
					//将域值编码转换，保证报文编码统一
					fieldValue=new String(fieldValue.getBytes(packet_encoding),packet_encoding);
					// 组二进制位图串
				//	bitMap = changeBitMapFlag(fieldId, bitMap);
					//获取域定义信息
		        	//Map<String,Object> field=getField(fieldId);
		        	String defType=field.get("type").toString();//类型定义例string
					int defLen=(int) field.get("length");//长度定义,例20
					boolean isFixLen=true;
					//是否定长判断
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
						
						if (fieldLen > maxLen) {
							System.out.println("error:字段" + fieldId + "的数据定义长度的长度为" + defLen + "位,长度不能超过"+(10*defLen1));
							return null;
						}else{
							//将长度值组装入字段
							int len=Integer.parseInt(getVaryLengthValue(fieldValue, defLen1));

							String str=String.valueOf(len);
							if(str.length()!=defLen1){
								//补0
								str=strCopy("0",(defLen1-str.length()))+len;
							}
							
							
							fieldValue = str + fieldValue;

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

						}
						
					}

					// 返回结果赋值
					if (datafiledMap.containsKey(fieldId)) {
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
	
	*//**
	 * 根据传进来的ID号获取相应的map类
	 * @param id
	 * @return map
	 *//*
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
        } catch (DocumentException e){
            e.printStackTrace();  
        }
         return null;
	}
	*//**
	 * 用来获取报文的交易码
	 * @param message传过来的报文
	 * @return 该段报文的交易码
	 *//*
	public String getProcessCode(String message){
		byte[] bytes=message.getBytes();
		LinkedHashMap<String,Object> lkhmap= unpack(bytes,0);
		String processCode=(String)lkhmap.get(3);
		return processCode;
	}
	
	*//**
	 * 根据域是否存在，组装bitmap.如果域存在则，相应的位为1，否则相应的为0
	 * @param fieldNo
	 * @param res
	 * @return
	 *//*
	public static String changeBitMapFlag(Integer fieldNo, String res) {
		//int indexNo=Integer.parseInt(fieldNo);
		res = res.substring(0,fieldNo-1) + "1" + res.substring(fieldNo);
		return res;
	}
	*//**
	 * 获取字符串变长值
	 * @param valueStr
	 * @param defLen
	 * 例如getFixLengthValue("12345678", 2)返回08
	 * 例如getFixLengthValue("12345678", 3)返回0008
	**//*
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
	
	*//**
	 * 将字段值做定长处理，不足定长则在后面补空格
	 * @param valueStr
	 * @param defLen
	 * @return
	 *//*
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
*/