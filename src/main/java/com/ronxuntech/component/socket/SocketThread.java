package com.ronxuntech.component.socket;

public class SocketThread extends Thread {  
	
	private Integer port;
	
	//无参构造
    public SocketThread() {
		
	}
    
    //有参构造
	 public SocketThread(int port) {
		super();
		this.port = port;
	}

	@Override  
     public void run(){  
		 try {
			 //单例模式创建server
			 SocketServers.getInstance().start(port);
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	
     }  
	
	//getter sertter
	public Integer getPort() {
		return port;
	}


	public void setPort(Integer port) {
		this.port = port;
	}

	 
}
