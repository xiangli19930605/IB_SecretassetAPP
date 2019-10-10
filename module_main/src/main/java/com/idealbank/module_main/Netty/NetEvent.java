package com.idealbank.module_main.Netty;


/**
 * @Description:用于网络的事件
 * @author http://blog.csdn.net/finddreams
 */ 
public class NetEvent {
	public boolean isOpenServer;
	public boolean isNet;

	public void setNet(boolean net) {
		isNet = net;
	}



	public NetEvent(boolean isOpenServer, boolean isNet) {
		this.isNet = isNet;
		this.isOpenServer = isOpenServer;
	}


	public boolean isOpenServer() {
		return isOpenServer;
	}

	public void setOpenServer(boolean openService) {
		isOpenServer = openService;
	}

	public boolean isNet() {
		return isNet;
	}
}
