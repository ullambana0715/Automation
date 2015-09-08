package com.android.automation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MyServer {

	private boolean isStartServer;
	private ServerSocket mServer;

	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();
	Handler mHandler;
	MainActivity mActivity;
	public MyServer(MainActivity ma) {
//		mHandler = hanlder;
		mActivity = ma;
	}

	public void startSocket() {
		try {
			isStartServer = true;
			int prot = 2000;
			mServer = new ServerSocket(prot);
			System.out.println("启动server,端口:" + prot);
			Socket socket = null;
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

	public class SocketThread extends Thread {

		public int socketID;
		public Socket socket;
		public BufferedWriter writer;
		public BufferedReader reader;

		public SocketThread(Socket socket, int count) {
			socketID = count;
			this.socket = socket;
			System.out.println("新增一台客户机，socketID：" + socketID);
			mActivity.mHandler.sendEmptyMessage(MainActivity.ADD_CLIENT);
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
					System.out.println(s);
					
					MessageBody mb = new MessageBody();
					mb.machineNo = s.substring(0, 3);
					mb.dataType = s.substring(3, 4);
					mb.data = s.substring(4, 8);
					Thread.sleep(200);
					Message msg = new Message();
					msg.obj = mb;
					msg.what = MainActivity.MESSAGE_GET;
					mActivity.mHandler.sendMessage(msg);
					
//					writer.write(b);
//					writer.flush();
					
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class MessageBody {
		String machineNo;
		String dataType;
		String runningStatus;
		int lineCut;
		String data;
	}
}
