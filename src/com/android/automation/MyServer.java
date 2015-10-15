package com.android.automation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

public class MyServer {

	public boolean isStartServer;
	private ServerSocket mServer;

	private ArrayList<MessageBody> mMsgList = new ArrayList<MessageBody>();
	public ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();
	Handler mHandler;
	MainActivity mActivity;
	Socket socket = null;
	public MyServer(MainActivity ma) {
		 mHandler = ma.mHandler;
//		mActivity = ma;
	}

	public void startSocket() {
		try {
			isStartServer = true;
			int prot = 5000;
			mServer = new ServerSocket(prot);
			System.out.println("启动server,端口:" + prot);
			int socketID = 0;
			while (isStartServer) {
				socket = mServer.accept();
				SocketThread thread = new SocketThread(socket, socketID++);
				thread.start();
				mThreadList.add(thread);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendStop(final String s) {
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					while(isStartServer) {
						if(mMsgList.size() > 0) {
							MessageBody from = null;
							for(int i=0;i<mMsgList.size();i++){
								if(mMsgList.get(i).machineNo.equals(s)) {
									from = mMsgList.get(i);
									System.out.println("send from server : "+from.machineNo);
								}
							}
							
							for(int i=0;i<mMsgList.size();i++) {
								if(from.machineNo.equals(s)) {
									System.out.println("send thread : "+from.machineNo);
									BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mMsgList.get(i).mSocket.getOutputStream()));
									System.out.println("sendstop");
									writer.write("stop0000\n");
									writer.flush();
									break;
								}
							}
						}
						Thread.sleep(200);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void stopSocket(){
		try {
			isStartServer = false;
			mServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class SocketThread extends Thread {

		public int socketID;
		public Socket socket;
		public BufferedWriter writer;
		public BufferedReader reader;
		int counter;
		MessageBody mb = new MessageBody();
		public SocketThread(Socket socket, int count) {
			socketID = count;
			this.socket = socket;
			System.out.println("新增一台客户机，socketID：" + socketID);
		}
		
		public void sendStop(String s){
			if(!s.equals(mb.machineNo)){
				return;
			}
			System.out.println("sendstop");
			try {
				writer.write("stop0000\n");
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void sendOk(){
			System.out.println("write ok\n");
			try {
				writer.write("OK\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			super.run();
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				char[] b = new char[8];
				String s = new String();
				while (isStartServer) {
					reader.read(b);
					s = new String(b);
					
					mb.machineNo = s.substring(0, 3);
					mb.dataType = Integer.parseInt(s.substring(3, 4));
					mb.data = Integer.parseInt(s.substring(4, 8));
					mb.mSocket = socket;
					Message msg = new Message();
					msg.obj = mb;
					msg.what = MainActivity.MESSAGE_GET;
					mHandler.sendMessage(msg);
					mMsgList.add(mb);

					if (counter > 30) {
						counter = 0;
						sendOk();
					} else {
						counter++;
					}
					Thread.sleep(500);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IOException");
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("InterruptedException");
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("NumberFormatException");
			}
		}
	}

	class MessageBody {
		String machineNo;
		int dataType;
		int runningStatus;
		int lineCut;
		int data;
		int errortimes;
		Socket mSocket;
	}
}
