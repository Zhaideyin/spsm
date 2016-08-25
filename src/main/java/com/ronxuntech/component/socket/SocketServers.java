package com.ronxuntech.component.socket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocketServers {  
	private ChannelFuture f=null;
	
	private SocketServers(){
		
	}
	//单例模式 
    private static SocketServers socketServers=new SocketServers();  
    //静态工厂方法   
    public static SocketServers getInstance() {    
        return socketServers;  
    }
    
    //关闭
    public void stop() {
    	if(f!=null){
    		f.channel().close();
    	}
	}
    /**
     * 开启端口
     * @param port
     * @throws Exception
     */
    public void start(int port) throws Exception {  
        EventLoopGroup bossGroup = new NioEventLoopGroup();  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  

            ServerBootstrap b = new ServerBootstrap();  
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)  
                    .childHandler(new ChannelInitializer<SocketChannel>() {  
                        @Override  
                        public void initChannel(SocketChannel ch)  
                                throws Exception {  
                            // 注册handler  
                            ch.pipeline().addLast(new ServerInHandler());  
                        }  
                    }).option(ChannelOption.SO_BACKLOG, 128)  
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
  
            f = b.bind(port).sync();  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
    
    /**
     * 开启端口
     * @param port
     * @param path
     * @throws Exception
     */
    public void start(int port,String path) throws Exception {  
        EventLoopGroup bossGroup = new NioEventLoopGroup();  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap();  
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)  
                    .childHandler(new ChannelInitializer<SocketChannel>() {  
                        @Override  
                        public void initChannel(SocketChannel ch)  
                                throws Exception {  
                            // 注册handler  
                            ch.pipeline().addLast(new ServerInHandler(path));  
                        }  
                    }).option(ChannelOption.SO_BACKLOG, 128)  
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
  
            f = b.bind(port).sync();  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
}  
