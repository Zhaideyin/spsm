package com.ronxuntech.component.socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

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
	
//	//xml路径。
	private String filePath;
//	//有参构造
	
	//无参构造
	public ServerInHandler() {
	}
	
	//获取xml内容
//	public String readXml(String filePath){
//		String content="";
//		content=Fileutil.readFile(filePath);
//		return content;
//	}
	
	public ServerInHandler(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
//		
//		HttpRequest mReq = (HttpRequest) msg;
//		String clientIP = mReq.headers().get("X-Forwarded-For");
//		if (clientIP == null) {
//			InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
//					.remoteAddress();
//			clientIP = insocket.getAddress().getHostAddress();
//		
//		}
		//获取配置文件的内容。
		String xmlContent =Fileutil.readFile(getFilePath());
		System.out.println(xmlContent+"----serverhandler----------------");
        ByteBuf result = (ByteBuf) msg;  
        byte[] result1 = new byte[result.readableBytes()];
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中  
        result.readBytes(result1); 
        //将接受的数据转换成16进制
	    String result2 = new String(result1);
	    System.out.println("result2:ascii"+result2);
	    //转换成16进制
	    String  resultStr =ParseUtil.stringToHex(result2);
	    System.out.println("resultStr:"+resultStr);
	   
		// 接收并打印客户端的信息  
		// 释放资源，这行很关键  
//	    System.out.println("Client said:"+result2);  
        result.release();
        PageData pd = new PageData();
      //解析前,先通过端口得到协议，然后再通过协议解析
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        IsoChannelMessageFactory factory=null;
        int temp=1;
        try{
        	factory=new IsoChannelMessageFactory(getFilePath());
			map=factory.unpack(resultStr.getBytes(), 0);
			//接受8583的报文存入数据库
			if(map!=null){
				pd.put("STATE","解析成功");
			}else{
				pd.put("STATE","解析失败");
			}
			System.out.println("****正常执行---------");
			
        }catch(Exception e){
        	System.out.println("***catch----------");
        	 pd.put("STATE","解析失败");
        	 temp=0;
        }finally {
        	 pd.put("REQUESTSOCKET_ID", System.currentTimeMillis());
        	 pd.put("CONTENT", resultStr);
        	 System.out.println("map is empty:---");
        	 if(map!=null){
        		 pd.put("FILEDMAP",map.toString());
        	 }
        	 pd.put("CREATETIME",new Date());
        	 requestsocketService.save(pd);
		}
        
    	Date date=new Date();
    	LinkedHashMap<String,Object> data2=null;
        ByteBuf encoded=null;
        //如果解析成功。则可以组装，并且存入数据库
        if(temp==1){
        	 // 向客户端发送消息  
    		System.out.println("========================");
    		if(map!=null){
    			data2=new LinkedHashMap<String,Object>();
    			//data2.put("2", map.get("Bit 2"));
    			data2.put("3", map.get("Bit 3"));
    			data2.put("4", map.get("Bit 4"));
    			SimpleDateFormat df0 = new SimpleDateFormat("MMddHHmmss");
    			SimpleDateFormat df1 = new SimpleDateFormat("HHmmss");
    			SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
    			data2.put("7",df0.format(date));
    			data2.put("12",df1.format(date));
    			data2.put("13",df2.format(date));
    			data2.put("15",map.get("Bit 15"));
    			data2.put("18",map.get("Bit 18"));
    			data2.put("25",map.get("Bit 25"));
    			data2.put("32", map.get("Bit 32"));
    			data2.put("33", map.get("Bit 33"));
    			data2.put("37", map.get("Bit 37"));
    			data2.put("41", map.get("Bit 41"));
    			data2.put("42", map.get("Bit 42"));
    			data2.put("48", map.get("Bit 48"));
    			data2.put("49", map.get("Bit 49"));
    			data2.put("60", map.get("Bit 60"));
    			data2.put("103", map.get("Bit 103"));
    		}
    		//data2.put("128", map.get("Bit 128"));
    		//System.out.println(new String(factory.pack(data2,"0210")));
    		String response="";
    		//返回客户端
    		if(factory!=null && data2!=null){
    			response =new String(factory.pack(data2,"0210"));
    		}
    		
            if(response!="" && response!=null){
            	PageData pd1 = new PageData();
            	try{
	            	// 在当前场景下，发送的数据必须转换成ByteBuf数组  
	                encoded = ctx.alloc().buffer(4 * response.length());  
	                encoded.writeBytes(response.getBytes()); 
	                //解析过后组装的报文，传给其他公司的
	                pd1.put("RESPONSESOCKET_ID",System.currentTimeMillis());
	                pd1.put("CONTENT", response);
	                pd1.put("CREATETIME",new Date());
	                pd1.put("FILEDMAP",data2.toString());
	                pd1.put("STATE","组装成功");
            	}catch (Exception e){
            		pd1.put("STATE","组装失败");
            	}finally{
            		responsesocketService.save(pd1);
            	}
            }
        }
        if(encoded!=null  && encoded.toString()!=""){
        	ctx.write(encoded);  
        }
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
    
}  