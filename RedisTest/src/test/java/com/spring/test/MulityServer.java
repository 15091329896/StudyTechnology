package com.spring.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MulityServer {
	public static void main(String[] args) throws IOException {
		System.out.println("------------Server-------------");
		ServerSocket server = new ServerSocket(8888);
		Boolean isRunning = true;
		while (isRunning) {
			Socket client = server.accept();
			System.out.println(" 一个客户端建立了连接 ");
			new Thread(new Channel(client)).start();
		}
		server.close();
	}

	static class Channel implements Runnable {
		private Socket client;
		private DataInputStream dis;
		private DataOutputStream dos;

		public Channel(Socket client) {
			this.client = client;
			try {
				// 输入
				dis = new DataInputStream(client.getInputStream());
				// 输出
				dos = new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				release();
			}

		}


		private String receive() {
		String datas = "";
			try {
				datas = dis.readUTF();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return datas;
		}


		

		private void release() {
			try {
				if (null != dos) {
					dos.close();
				}
				if (dis != null) {
					dis.close();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void send(String string) {
			try {
				dos.writeUTF(string);
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String uname = "";
			String upwd = "";
			String[] dataArray = receive().split("&");
			for (String info : dataArray) {
				String[] userInfo = info.split("=");
				if (userInfo[0].equals("uname")) {
					System.out.println("你的用户名是：" + userInfo[1]);
					uname = userInfo[1];
				} else if (userInfo[0].equals("upwd")) {
					System.out.println("你的密码是：" + userInfo[1]);
					upwd = userInfo[1];
				}
			}
			if (uname.equals("root") && upwd.equals("root")) { // 成功
				send("登录成功，欢迎回来");
			} else { // 失败
				send("用户名或者密码错误");
			}
			release();
		}
		

		public Socket getClient() {
			return client;
		}

		public void setClient(Socket client) {
			this.client = client;
		}

		public DataInputStream getDis() {
			return dis;
		}

		public void setDis(DataInputStream dis) {
			this.dis = dis;
		}

		public DataOutputStream getDos() {
			return dos;
		}

		public void setDos(DataOutputStream dos) {
			this.dos = dos;
		}

	}

}