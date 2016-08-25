package com.ronxuntech.component.socket;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ronxuntech.component.socket.util.Fileutil;
import com.ronxuntech.component.socket.util.IsoChannelMessageFactory;
import com.ronxuntech.component.socket.util.ParseUtil;
import com.ronxuntech.service.socket.requestsocket.impl.RequestSocketService;
import com.ronxuntech.service.socket.responsesocket.impl.ResponseSocketService;
import com.ronxuntech.service.socket.socketport.impl.SocketPortService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class ServerInHandler extends ChannelInboundHandlerAdapter {  
	//创建service
	ResponseSocketService responsesocketService = (ResponseSocketService) SpringBeanFactoryUtils.getBean("responsesocketService");
	RequestSocketService requestsocketService = (RequestSocketService) SpringBeanFactoryUtils.getBean("requestsocketService");
	SocketPortService socketportService =(SocketPortService) SpringBeanFactoryUtils.getBean("socketportService");
	// load up the knowledge base
	KieServices ks = KieServices.Factory.get();
	KieContainer kContainer = ks.getKieClasspathContainer();
	KieSession kSession = kContainer.newKieSession("ksession-rules");
	Message message = new Message();
	//xml路径。
	public String filePath;
	//无参构造
	public ServerInHandler() {
	}
	//有参构造
	public ServerInHandler(String filePath) {
		super();
		this.filePath = filePath;
	}
    
	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
		//获取配置文件的内容。
		
		
		String xmlContent =Fileutil.readFile(getFilePath());
		System.out.println("----**************message************----------------");
		System.out.println("message:"+msg);
		
		
		
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        
        
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中  
        result.readBytes(result1); 
        
    	System.out.println("----**************result1************----------------");
    	
    	System.out.println(result1);
    	
        //将接受的数据转换成16进制
		//String result2 =  result1.toString();
    	String result2 = new String(result1);
		
	   System.out.println("result2:ascii"+result2.toString());
	    //转换成16进制
	    String  resultStr =ParseUtil.stringToHex(result2);	  
	    
	    System.out.println("***************转成了16 进制的******************");
	    System.out.println("resultStr:"+resultStr);
		// 接收并打印客户端的信息  
		// 释放资源，这行很关键  
        result.release();
        PageData pd = new PageData();
      //解析前,先通过端口得到协议，然后再通过协议解析
        IsoChannelMessageFactory factory=null;
        LinkedHashMap<String,Object> map =new LinkedHashMap<>();
        //判断是否出现异常，1表示正常，0表示异常
        int temp=1;
        try{
        	factory=new IsoChannelMessageFactory(getFilePath());

        	map =factory.unpack(resultStr.getBytes(), 0);
        	String pcode=map.get("Bit 3").toString();
			System.out.println("pcod："+pcode);
			message.setMessage(resultStr);
			message.setPath(getFilePath());
			message.setProcessCode(pcode);
			message.setBitmap(map);
			System.out.println("----------------handler-----------"+message.getProcessCode());
		    kSession.insert(message);
		    kSession.fireAllRules();
			  //接受8583的报文存入数据库
			 pd.put("STATE","1");
        }catch(Exception e){
        	 pd.put("STATE","0");
        	 temp=0;//表示异常
        }finally {
        	 pd.put("REQUESTSOCKET_ID", System.currentTimeMillis());
        	 pd.put("CONTENT",resultStr);
        	 pd.put("FILEDMAP",map.toString());

        	 pd.put("CREATETIME",new Date());
        	 requestsocketService.save(pd);
		}
            
        

            String response=message.getMessage();
            LinkedHashMap<String,Object> data2=message.getBitmap();
            ByteBuf encoded=null;
            
            if(response!=" "&& response!=null){
            	 // 在当前场景下，发送的数据必须转换成ByteBuf数组  
                encoded = ctx.alloc().buffer(4 * response.length());  
                encoded.writeBytes(response.getBytes()); 
                //解析过后组装的报文，传给其他公司的
                PageData pd1 = new PageData();
                pd1.put("RESPONSESOCKET_ID",System.currentTimeMillis());
                pd1.put("CONTENT", response);
                pd1.put("STATE","1");
                pd1.put("FILEDMAP",message.getBitmap().toString());
                pd1.put("CREATETIME",new Date());
                responsesocketService.save(pd1);
            }
        
        ctx.write(encoded);  

        ctx.flush();  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}  
	//报文的内部类
	public static class Message {
		private String path;
		private LinkedHashMap<String, Object> bitmap;
		private String head;
		private String returnType;
		private String processCode;
		private String message;
		private int length;
	
		public String getType(String  message){
			byte[] type = new byte[8];
			System.arraycopy(message.getBytes(), 0, type, 0, 8);
			return ParseUtil.hexToString(new String(type));
		}
		/**
		 * 
		 * @param xmlPath xml文件路径
		 * @param accept 接收到的报文的类型
		 * @return 返回回应的相应类型
		 */
		public String reponseTypeByXml(String xmlPath,String accept)
		{     String  xmlPath1=getClass().getResource("/").getFile().toString()+xmlPath; 
			  LinkedHashMap<String ,String> fileds=new  LinkedHashMap<String,String>();
			  File myXML = new File(xmlPath1);  
	          SAXReader sr = new SAXReader();
	          String returnType=null;
			try {
				Document  doc = sr.read(myXML);
				Element root = doc.getRootElement();
				for (Iterator e = root.elementIterator();e.hasNext();){
				      Element node = (Element) e.next();
					  fileds.put(node.elementText("acceptType"), node.elementText("reponseType"));
				}
				
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
	           if(fileds.containsKey(accept)){
	        	   returnType =fileds.get(accept);
	           }
	           else{
	        	   System.out.println("erro: no match");
	           }
			
			return returnType;
		}
		
		public String getProcessCode() {
			return processCode;
		}

		public void setProcessCode(String processCode) {
			this.processCode = processCode;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		public String getReturnType() {
			return returnType;
		}
		public void setReturnType(String returnType) {
			this.returnType = returnType;
		}

		public   LinkedHashMap<String, Object> getBitmap() {
			return bitmap;
		}

		public void setBitmap(LinkedHashMap<String, Object> bitmap) {
			this.bitmap = bitmap;
		}

		public String getHead() {
			return head;
		}

		public void setHead(String head) {
			this.head = head;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path =path;
		}
		}
	}
    
