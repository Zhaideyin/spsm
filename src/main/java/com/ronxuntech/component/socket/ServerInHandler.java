package com.ronxuntech.component.socket;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
	// 创建service
	ResponseSocketService responsesocketService = (ResponseSocketService) SpringBeanFactoryUtils
			.getBean("responsesocketService");
	RequestSocketService requestsocketService = (RequestSocketService) SpringBeanFactoryUtils
			.getBean("requestsocketService");
	SocketPortService socketportService = (SocketPortService) SpringBeanFactoryUtils.getBean("socketportService");

	// load up the knowledge base
	KieServices ks = KieServices.Factory.get();
	KieContainer kContainer = ks.getKieClasspathContainer();
	KieSession kSession = kContainer.newKieSession("ksession-rules");
	Message message = new Message();

	// xml路径。
	public String filePath;

	// 无参构造
	public ServerInHandler() {
	}

	// 有参构造
	public ServerInHandler(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 获取配置文件的内容。
		String xmlContent = Fileutil.readFile(getFilePath());

		ByteBuf result = (ByteBuf) msg;
		byte[] result1 = new byte[result.readableBytes()];

		// msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
		result.readBytes(result1);

		// 将接受的数据转换成16进制
		String result2 = new String(result1);

		// 转换成16进制
		String resultStr = ParseUtil.stringToHex(result2);
		// 释放资源，这行很关键
		result.release();
		PageData pd = new PageData();
		// 解析前,先通过端口得到协议，然后再通过协议解析
		IsoChannelMessageFactory factory = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		// 控制解析是否成功，如果成功则组装报文，没有直接跳过。
		int temp = 0;
		try {
			factory = new IsoChannelMessageFactory(getFilePath());

			map = factory.unpack(resultStr.getBytes(), 0);
			// 取出交易码
			String pcode = map.get("Bit 3").toString();
			message.setMessage(resultStr);
			message.setPath(getFilePath());
			message.setProcessCode(pcode);
			// 传递解析过后的域及值
			message.setBitmap(map);
			// 设置返回类型
			String type = message.reponseTypeByXml("channelReponse.xml", message.getType(resultStr));
			message.setReturnType(type);
			if (map != null) {
				// 如果解析成功则设置1。失败则设置0 // 如果解析失败则不会组装
				pd.put("STATE", "解析成功");
				kSession.insert(message);
				kSession.fireAllRules();
				temp = 1;
			} else {
				pd.put("STATE", "解析失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 接受8583的报文存入数据库
			pd.put("REQUESTSOCKET_ID", System.currentTimeMillis());
			pd.put("CONTENT", resultStr);
			pd.put("FILEDMAP", map.toString());
			pd.put("CREATETIME", new Date());
			requestsocketService.save(pd);
		}

		ByteBuf encoded = null;
		if (temp == 1) {
			String response = message.getMessage();
			if (response != " " && response != null) {
				// 在当前场景下，发送的数据必须转换成ByteBuf数组
				encoded = ctx.alloc().buffer(4 * response.length());
				encoded.writeBytes(response.getBytes());
				// 解析过后组装的报文，
				PageData pd1 = new PageData();
				pd1.put("RESPONSESOCKET_ID", System.currentTimeMillis());
				pd1.put("CONTENT", response);
				pd1.put("STATE", "1");
				pd1.put("FILEDMAP", message.getBitmap().toString());
				pd1.put("CREATETIME", new Date());
				responsesocketService.save(pd1);
			}
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
//-----------------------------------------------------------------------------------------------------------------------
	
	// 报文类
	public static class Message {
		private String path; // xml路径
		private LinkedHashMap<String, Object> bitmap; // 域
		private String head; // 头
		private String returnType; // 返回的类型
		private String processCode; // 交易码
		private String message; // 传递的报文
		private int length; // 长度

		// 根据报文得出类型。
		public String getType(String message) {
			byte[] type = new byte[8];
			System.arraycopy(message.getBytes(), 0, type, 0, 8);
			return ParseUtil.hexToString(new String(type));
		}

		/**
		 * 
		 * @param xmlPath
		 *            xml文件路径
		 * @param accept
		 *            接收到的报文的类型
		 * @return 返回回应的相应类型
		 */
		public String reponseTypeByXml(String xmlPath, String accept) {
			String xmlPath1 = getClass().getResource("/").getFile().toString() + xmlPath;
			System.out.println("xmlPath1:" + xmlPath1);
			LinkedHashMap<String, String> fileds = new LinkedHashMap<String, String>();
			File myXML = new File(xmlPath1);
			SAXReader sr = new SAXReader();
			String returnType = null;
			try {
				Document doc = sr.read(myXML);
				Element root = doc.getRootElement();
				for (Iterator e = root.elementIterator(); e.hasNext();) {
					Element node = (Element) e.next();
					fileds.put(node.elementText("acceptType"), node.elementText("reponseType"));
				}

			} catch (DocumentException e1) {
				e1.printStackTrace();
			}

			if (fileds.containsKey(accept)) {
				returnType = fileds.get(accept);
			} else {
				System.out.println("erro: no match");
			}

			return returnType;
		}
//get set
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

		public LinkedHashMap<String, Object> getBitmap() {
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
			this.path = path;
		}
	}
}
