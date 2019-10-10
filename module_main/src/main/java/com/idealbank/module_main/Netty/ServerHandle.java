package com.idealbank.module_main.Netty;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.idealbank.module_main.Netty.bean.FileBean;
import com.idealbank.module_main.Netty.bean.Message;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandle extends ChannelInboundHandlerAdapter {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	final Handler handler;

	public ServerHandle(Handler handler) {
		this.handler = handler;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message request= JSON.parseObject(msg.toString(),Message.class);
		System.out.println("client:" + request.getId() + ","  + "," + request.getResponseMessage());

//		Response response = new Response();
//		response.setId(request.getId());
//		response.setName("response" + request.getId());
//		response.setResponseMessage("响应内容" + request.getRequestMessage());

		switch (request.getType()){
			case PIC:
				Log.e("PIC","PIC");
//				setImg(request.getAttachment());
//				System.out.println("request.getAttachment():"+request.getAttachment().length);
				break;
			case PING:
				Log.e("PING","PING");
//				Message message = handler.obtainMessage(0x02);
//				message.obj = request.getRequestMessage() ;
//				message.sendToTarget();
				break;
			case FORM:

				break;
			case RFID:

				break;
			case EXCL:
				Log.e("EXCL","EXCL");
//				ImageUtils.setExcl(request.getAttachment());
//				System.out.println("request.getAttachment():"+request.getAttachment().length);
//				FileBean bean= (FileBean) JsonUtil.jsonToObject(JsonUtil.jsonToString(request.getFileBean()), FileBean.class);
//				ImageUtils.	setExclList(request.getFileName(),request.getFileData());
//				new ExportTask(bean).execute();
				break;
		}


//
//		System.out.println("SimpleChatClient:" + msg);
//
//		Channel incoming = ctx.channel();
//		for (Channel channel : channels) {
//			if (channel != incoming) {
//				channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
//			} else {
//				channel.writeAndFlush("[you]" + msg + "\n");
//			}
//		}
//		Message message = handler.obtainMessage(0x02);
//		message.obj = msg;
//		message.sendToTarget();
	}

	class ExportTask extends AsyncTask<String, Void, String> {

		FileBean bean;

		public ExportTask(FileBean bean) {
			this.bean = bean;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.e("","正在导出文件...");
		}

		@Override
		protected void onPostExecute(String errorMsg) {
			super.onPostExecute(errorMsg);
			Log.e("",errorMsg);
			if (errorMsg != null) {
//                DialogUtil.showMessageDialog(errorMsg, TaskResultActivity.this);
			} else {
//                DialogUtil.showMessageDialog("导出成功!", TaskResultActivity.this);
			}
		}

		@Override
		protected String doInBackground(String... strings) {
//			ImageUtils.	setExclList(bean);
			return "sss";
		}
	}

	static void setImg(byte[] data) {
		if (data.length < 3) {
			return;
		}
		try {
			char separator = File.separatorChar;
//			String path = System.getProperty("user.dir") + separator + "recieve" + separator
//					+ System.currentTimeMillis() + ".png";
//			String path = "/storage/emulated/0/DCIM/camera/IMG_66666666.png";
			String path = "/storage/emulated/0/DCIM/camera/vid.mp4";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream output = new FileOutputStream(file);
			output.write(data, 0, data.length);
			output.flush();
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//利用写空闲发送心跳检测消息
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
				case WRITER_IDLE:
//                    Message pingMsg = new Message(MsgType.PING);
//                    pingMsg.setData(new Date().getTime()+"");
//                    ctx.writeAndFlush(JSON.toJSONString(pingMsg));
//					ctx.writeAndFlush(JSON.toJSONString("12345"));

//					Response response = new Response();
//					response.setId(1);
//					response.setName("服务端" );
//					response.setType(MsgType.PING);
//					response.setResponseMessage("响应内容" +"：这是PING");
//					ChannelMap.getChannel("1").writeAndFlush(response);
					System.out.println("send ping to server----------");
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
		}
		channels.add(ctx.channel());
//		Message message = handler.obtainMessage(0x01);
//		message.obj = "[SYSTEM] - " + incoming.remoteAddress() + "加入";
//		message.sendToTarget();
		ChannelMap.addChannel("1",incoming);

				EventBus.getDefault().post(new NetEvent(true,true));

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
		}
		channels.remove(ctx.channel());

//		channels.add(ctx.channel());
//		Message message = handler.obtainMessage(0x01);
//		message.obj = "[SYSTEM] - " + incoming.remoteAddress() + "离开";
//		message.sendToTarget();
		ChannelMap.delChannel("1");
		EventBus.getDefault().post(new NetEvent(true,false));
	}
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception { // (4)
//        Channel incoming = ctx.channel();
//        for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
//            } else {
//                channel.writeAndFlush("[you]" + s + "\n");
//            }
//        }
//    }


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "在线");
//		Message message = handler.obtainMessage(0x01);
//		message.obj = "[SYSTEM] - " + incoming.remoteAddress() + "在线";
//		message.sendToTarget();

//        Response response = new Response();
//        response.setId(1);
//        response.setName("服务端" );
//        response.setResponseMessage("响应内容" +"：你叫什么");
//		ctx.writeAndFlush(response);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
//		Message message = handler.obtainMessage(0x01);
//		message.obj = "[SYSTEM] - " + incoming.remoteAddress() + "掉线";
//		message.sendToTarget();
		ChannelMap.delChannel("1");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "异常");

		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();

//		Message message = handler.obtainMessage(0x01);
//		message.obj = "[SYSTEM] - " + incoming.remoteAddress() + "异常";
//		message.sendToTarget();
		ChannelMap.delChannel("1");
	}


}
