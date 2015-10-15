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
//		MyThread m = new MyThread("d");
//		m.start();
		for(int a=1;a<9;a++){
			for(int b=1;b<9;b++){
				if((a-b)==1){
					for(int c=1;c<9;c++){
						for(int d=1;d<9;d++){
							if(c!=a&&c!=b&&d!=a&&d!=b&&c!=d){
								if((c-d)==2){
									for(int e=1;e<9;e++){
										for(int f=1;f<9;f++){
											if(e!=a&&e!=b&&e!=c&&e!=d&&f!=a&&f!=b&&f!=c&&f!=d&&e!=f){
												if((e+f)==9){
													for(int g=1;g<9;g++){
														for(int h=1;h<9;h++){
															if(g!=a&&g!=b&&g!=c&&g!=d&&g!=e&&g!=f&&h!=a&&h!=b&&h!=c&&h!=d&&h!=e&&g!=f&&g!=h){
																if((g+h)==7){
																	System.out.println(a+"-"+b+"=1");
																	System.out.println(c+"-"+d+"=2");
																	System.out.println(e+"+"+f+"=9");
																	System.out.println(g+"+"+h+"=7");
																	System.out.println("==========");
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
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
