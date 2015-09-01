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

public class MyServer2 {

	private boolean isStartServer;
	private ServerSocket mServer;

	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();
	Handler mHandler;
	
	public MyServer2(Handler hanlder) {
		mHandler= hanlder;
	}

	public void startSocket() {
		try {
			isStartServer = true;
			int prot = 2000;
			mServer = new ServerSocket(prot);
			System.out.println("启动server,端口:" + prot);
			Socket socket = null;
			int socketID = 0;
			startMessageThread();
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

	public void startMessageThread() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					while (isStartServer) {
						if (mMsgList.size() > 0) {
							SocketMessage from = mMsgList.get(0);
							for (SocketThread to : mThreadList) {
								if (to.socketID == from.to) {
									BufferedWriter writer = to.writer;
									JSONObject json = new JSONObject();
									json.put("from", from.from);
									json.put("msg", from.msg);
									json.put("time", from.time);
									writer.write(json.toString() + "\n");
									writer.flush();
									System.out.println("推送消息成功：" + from.msg + ">> to socketID:" + from.to);
									break;
								}
							}
							mMsgList.remove(0);
						}
						Thread.sleep(200);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
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

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			super.run();
			// 完整发送模块；
			// OutputStream ou;
			// try {
			// ou = new DataOutputStream(socket.getOutputStream());
			// while (isStartServer) {
			// ou.write("android 客户端\r\n".getBytes("utf-8"));
			// System.out.println("write：android 客户端");
			// ou.flush();
			// Thread.sleep(100);
			// }
			// ou.close();
			// socket.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			try {
				 reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				DataInputStream is = new DataInputStream(socket.getInputStream());
				char[] b = new char[32];
//				CharBuffer cb = CharBuffer.allocate(32);
				while (isStartServer) {
//					System.out.println("服务端收到的内容char:" + is.readChar());
					System.out.println("服务端收到的内容:" + reader.read(b));
//					System.out.println("服务端收到的内容read cb:" + reader.read(cb));
					
					if(reader.read(b) != -1){
						System.out.println("reader is end");
					}
					
					for(char cs : b){
						System.out.print(cs);
					}
					Thread.sleep(200);
				}
//				DataOutputStream os = new DataOutputStream(socket.getOutputStream());
//				while (isStartServer) {
//					os.write("android 客户端\r\n".getBytes("gb2312"));
//					os.flush();
//					System.out.println("服务端发出的内容:android 客户端\r\n");
//					Thread.sleep(200);
//				}
				is.close();
//				os.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// String clientInputStr = input.readUTF();
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// try {
			// reader = new BufferedReader(new
			// InputStreamReader(socket.getInputStream(), "utf-8"));
			// writer = new BufferedWriter(new
			// OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			// while(isStartServer) {
			// if(reader.ready()) {
			// String data = reader.readLine();
			// System.out.println("收到一条消息："+data);
			// JSONObject json = new JSONObject(data);
			// SocketMessage msg = new SocketMessage();
			// msg.to = json.getInt("to");
			// msg.msg = json.getString("msg");
			// msg.from = socketID;
			// mMsgList.add(msg);
			// }
			// Thread.sleep(100);
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

		}
	}
}
