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

public class MyServer {

	private boolean isStartServer;
	private ServerSocket mServer;

	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();
	Handler mHandler;

	public MyServer(Handler hanlder) {
		mHandler = hanlder;
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
//				Thread.sleep(200);
//				mHandler.sendEmptyMessage(MainActivity.MESSAGE_GET);
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
			mHandler.sendEmptyMessage(MainActivity.ADD_CLIENT);
		}

		@Override
		public void run() {
			super.run();
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				char[] b = new char[8];
				while (isStartServer) {
					reader.read(b);
					for (char cs : b) {
						System.out.print(cs);
					}
					System.out.println();

					Thread.sleep(100);
					mHandler.sendEmptyMessage(MainActivity.MESSAGE_GET);
					
					
					writer.write(b);
					writer.flush();
					
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
