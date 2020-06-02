package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import tags.Decode;
import tags.Tags;
import client.Client;

public class ClientServer {

	private String username = "";
	private ServerSocket serverPeer;
	private int port;
	private boolean isStop = false;

//	public void stopServerPeer() {
//		isStop = true;;
//	}
// 
//	public boolean getStop() {
//		return isStop;
//	}

	public ClientServer(String name) throws Exception {
		username = name;
		port = Client.getPort();
		serverPeer = new ServerSocket(port);
		(new WaitPeerConnect()).start();
	}
	
	public void exit() throws IOException {
		isStop = true;
		serverPeer.close();
	}
	public ServerSocket getServerSocket() {
		return serverPeer;
	}
 
	class WaitPeerConnect extends Thread {

		Socket connection; 
		ObjectInputStream getRequest;

		@Override
		public void run() {
			super.run();
			while (!isStop) {  
				try {
					connection = serverPeer.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					String name = Decode.getNameRequestChat(msg); 
					ArrayList<String> dataGroupChat = Decode.getNameRequestGroupChat(msg);
					System.out.print(msg);
					System.out.print(dataGroupChat);
					if(name!=null) {
						int res = MainGui.request("Account: " + name + " want to connect with you !", true);
						ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
						if (res == 1) {
							send.writeObject(Tags.CHAT_DENY_TAG);
	
						} else if (res == 0) {
							send.writeObject(Tags.CHAT_ACCEPT_TAG);
							new ChatGui(username, name, connection, port);
						}
						send.flush();
					}
					else if(dataGroupChat!=null){
						ArrayList<String> GroupName= new ArrayList<String>();
						char[] charname=dataGroupChat.get(1).toCharArray();
						String temp="";
						for (int i = 0; i < charname.length; i++) {
							if (charname[i]!=',') {
								temp+=charname[i];
							}
							else {
								GroupName.add(temp); 
								temp="";
							}
						}
						
						ArrayList<String> ListIP= new ArrayList<String>();
						char[] charIP=dataGroupChat.get(2).toCharArray();
						temp="";
						for (int i = 0; i < charIP.length; i++) {
							if (charIP[i]!=',') {
								temp+=charIP[i];
							}
							else {
								ListIP.add(temp); 
								temp="";
							}
						}
						ArrayList<Integer> ListPort= new ArrayList<Integer>();
						char[] charPort=dataGroupChat.get(3).toCharArray();
						temp="";
						for (int i = 0; i < charPort.length; i++) {
							if (charPort[i]!=',') {
								temp+=charPort[i];
							}
							else {
								ListPort.add(Integer.parseInt(temp)); 
								temp="";
							}
						}
						int res = MainGui.request("Account: " + dataGroupChat.get(0) + "connected with you via Group "+GroupName, true);
						ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
						if (res == 0 || res ==1) {
							send.writeObject(Tags.CHAT_ACCEPT_TAG);
							send.flush();
							serverPeer.close(); 
							new GroupChatGui(username,port,dataGroupChat.get(0), ListIP, ListPort,GroupName);
						    
						}
						
					}
				} catch (Exception e) {
					break;
				}
			}
			try {
				serverPeer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
