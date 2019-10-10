package com.idealbank.module_main.Netty;

import android.os.Handler;

import com.idealbank.module_main.Netty.web.MyWebSocketChannelHandler;
import com.idealbank.module_main.Netty.web.WebSocketChannelInitializer;

import org.simple.eventbus.EventBus;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class FileServer {
    private static Handler handler;
    ChannelFuture f;

    private static class Holder {
        private static final FileServer HANDLER = new FileServer(handler);
    }

    public static FileServer getInstance() {
        return FileServer.Holder.HANDLER;
    }

    public FileServer(Handler handler) {
        this.handler = handler;
    }

    public void start() {
//        new FileServer.ServerThread().start();
        new FileServer.WebServerThread().start();
    }

    class ServerThread extends Thread {
        @Override
        public void run() {
            EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)  //服务类
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap(); // (2)  //设置线程池
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class) // (3)//设置socket工厂、
//                        .childHandler(new SimpleChatServerInitializer(handler))  //(4)  //设置管道工厂
//                        .option(ChannelOption.SO_BACKLOG, 128)          // (5)//serverSocketchannel的设置，链接缓冲池的大小
////                            .option(ChannelOption.SO_SNDBUF, 5) //设置发送数据缓冲大小
//                        .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受数据缓冲大小
//
//
//                        .childOption(ChannelOption.SO_KEEPALIVE, true)// (6)//socketchannel的设置,维持链接的活跃，清除死链接
//                        .childOption(ChannelOption.TCP_NODELAY, true);//socketchannel的设置,关闭延迟发送

                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                System.out.println("有客户端连接上来:" + ch.localAddress().toString());
                                ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                                ch.pipeline().addLast(new ObjectEncoder());
                                ch.pipeline().addLast(new ServerHandle(GlobalHandler.getInstance()));
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128)
                        .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                        .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                System.out.println("SimpleChatServer 启动了");


                // 绑定端口，开始接收进来的连接
                f = b.bind(8080).sync(); // (7)
                handler.obtainMessage(0x00).sendToTarget();
                EventBus.getDefault().post(new NetEvent(true, false));
//            title.setClickable(false);
                // 等待服务器  socket 关闭 。
                // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new NetEvent(false, false));
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
//            title.setClickable(true);
                EventBus.getDefault().post(new NetEvent(false, false));
                System.out.println("SimpleChatServer 关闭了");
            }


        }
    }

    class WebServerThread extends Thread {
        @Override
        public void run() {
//            EventLoopGroup bossGroup = new NioEventLoopGroup();
//            EventLoopGroup workerGroup = new NioEventLoopGroup();
//            try {
//                ServerBootstrap serverBootstrap = new ServerBootstrap();
//                serverBootstrap.group(bossGroup, workerGroup)
//                        .channel(NioServerSocketChannel.class)
//                        .handler(new LoggingHandler(LogLevel.INFO))
//                        .childHandler(new WebSocketChannelInitializer())
//                        .option(ChannelOption.SO_BACKLOG, 128)
//                        .option(ChannelOption.SO_SNDBUF, 32 * 1024)
//                        .option(ChannelOption.SO_RCVBUF, 32 * 1024)
//                        .childOption(ChannelOption.SO_KEEPALIVE, true);
//
//                ChannelFuture channelFuture = null;
//                try {
//                    channelFuture = serverBootstrap.bind(8080).sync();
//                    channelFuture.channel().closeFuture().sync();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workGroup);
                    b.channel(NioServerSocketChannel.class);
                    b.childHandler(new MyWebSocketChannelHandler());
                    System.out.println("服务端开启等待客户端连接....");
                    Channel ch = b.bind(8080).sync().channel();
                    ch.closeFuture().sync();

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    //优雅的退出程序
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }


//            } finally {
//                bossGroup.shutdownGracefully();
//                workerGroup.shutdownGracefully();
//            }
        }
    }
}
