package com.android.automation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyThread extends Thread {
	public String txt1;
	Socket socket = null;
	String buffer = "";

	public MyThread(String str) {
		txt1 = str;
	}
	
	public static void main(String[] args) {
		MyThread m = new MyThread("d");
		m.start();
	}

	@Override
	public void run() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress("192.168.1.100", 5000), 5000);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			int i = 0;
			buffer = "";
			while (true) {
				String line = "11111"+ i + "33";
				writer.write(line);
				writer.flush();
				
				i ++ ;
				if(i > 9){
					i = 0;
				}
				Thread.sleep(500);
				System.out.println(bff.readLine());
			}

		} catch (SocketTimeoutException aa) {
			aa.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
