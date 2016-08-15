package com.ronxuntech.component.socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import com.ronxuntech.component.socket.util.IsoChannelMessageFactory;
import com.ronxuntech.service.socket.requestsocket.impl.RequestSocketService;
import com.ronxuntech.service.socket.responsesocket.impl.ResponseSocketService;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.SpringBeanFactoryUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ServerInHandler extends ChannelInboundHandlerAdapter {  
	//创建service
	ResponseSocketService responsesocketService = (ResponseSocketService) SpringBeanFactoryUtils.getBean("responsesocketService");
	RequestSocketService requestsocketService = (RequestSocketService) SpringBeanFactoryUtils.getBean("requestsocketService");
	//@Autowired
	//private StartSocketportManager startsocketportService;
	

	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  

        ByteBuf result = (ByteBuf) msg;  
        byte[] result1 = new byte[result.readableBytes()];  
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中  
        result.readBytes(result1); 
        String resultStr = new String(result1);  
        // 接收并打印客户端的信息  
        System.out.println("Client said:" + resultStr);  
        // 释放资源，这行很关键  
        result.release();
        //接受8583的报文存入数据库
        PageData pd = new PageData();
        pd.put("REQUESTSOCKET_ID", System.currentTimeMillis());
        pd.put("CONTENT", resultStr);
        pd.put("STATE","1");
        pd.put("CREATETIME",new Date());
        requestsocketService.save(pd);
        // 向客户端发送消息  
        IsoChannelMessageFactory factory=new IsoChannelMessageFactory("8583-channel.xml");
		//String a="0200f23a448188c1801000000000120000011662277733020211221900000000008024000623163644943173163644062306234814012810855557310084991590216364494317322220002001530148140101031PA05RQ10790890          000000#156014000005100300000890887300084991590210a0c06e7381d7d6";
//		String a="30323030f23a448188c18010000000001200000131363632323737373333303230323131323231393030303030303030303038303234303030363233313633363434393433313733313633363434303632333036323334383134303132383130383535353537333130303834393931353930323136333634343934333137333232323230303032303031353330313438313430313031303331504130355251313037393038393020202020202020202020303030303030233135363031343030303030353130303330303030303839303838373330303038343939313539303210a0c06e7381d7d6";
		LinkedHashMap<String,Object> map=factory.unpack(resultStr.getBytes(), 0);
		
		
		System.out.println("========================");
		
		Date date=new Date();
		LinkedHashMap<String,Object> data2=new LinkedHashMap<String,Object>();
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
		//data2.put("128", map.get("Bit 128"));
		System.out.println(new String(factory.pack(data2,"0210")));
       
		//返回客户端
        String response =new String(factory.pack(data2,"0210"));
        
        
        // 在当前场景下，发送的数据必须转换成ByteBuf数组  
        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());  
        encoded.writeBytes(response.getBytes());  
       
        
         //解析过后组装的报文，传给其他公司的
        PageData pd1 = new PageData();
        pd1.put("RESPONSESOCKET_ID",System.currentTimeMillis());
        pd1.put("CONTENT", response);
        pd1.put("STATE","1");
        pd1.put("CREATETIME",new Date());
        responsesocketService.save(pd1);
        
        ctx.write(encoded);  
        ctx.flush();  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }  
    
    public String getPath(){
    	String path=getClass().getResource("/").getFile().toString();
    	return path;
    }
	
}  