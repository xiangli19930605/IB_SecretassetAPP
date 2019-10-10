package com.idealbank.module_main.Netty;

import com.idealbank.module_main.Netty.web.MyWebSocketChannelHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NettyClient {

    /*是否连接*/
    private boolean isConnect = false;

    /*伴生对象*/
    public static NettyClient getInstance() {
        return new NettyClient();
    }
    private NioEventLoopGroup group;

    private Bootstrap bootstrap;

    private Channel channel;

    int port = 8080;

    public Observable<Boolean> connect() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workGroup);
                    b.channel(NioServerSocketChannel.class);
                    b.childHandler(new MyWebSocketChannelHandler());
                    System.out.println("服务端开启等待客户端连接....");
                    Channel ch = b.bind(8080).sync().channel();
                    channel=  ch.closeFuture().sync().channel();

                    emitter.onNext(channel.isActive());
                    isConnect = channel.isActive();
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onNext(false);
                    isConnect = false;
                } finally {
                    //优雅的退出程序
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Boolean> send() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if (isConnect) {
                    channel.writeAndFlush(2).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            //写操作完成，并没有错误发生
                            if (future.isSuccess()) {
                                System.out.println("successful");
                                emitter.onNext(future.isSuccess());
                            } else {
                                //记录错误
                                System.out.println("error");
                                future.cause().printStackTrace();
                            }
                        }
                    });
                } else {
                    emitter.onNext(false);
                }
            }
        })
                .subscribeOn(Schedulers.io())//这里注意要在工作线程发送
                .observeOn(AndroidSchedulers.mainThread());
    }


    /*是否连接*/
    public Boolean isConnect() {
        return isConnect;
    }

    /*重连*/
    public Observable<Boolean> reConnect() {
        disConnect();
        return connect();

    }


    /*关闭连接*/
    public void disConnect() {
        isConnect = false;
        group.shutdownGracefully();
    }

}
