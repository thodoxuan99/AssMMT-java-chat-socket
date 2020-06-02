package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;

import data.Peer;
import tags.Decode;
import tags.Encode;
import tags.Tags;

public class Client {

	public static ArrayList<Peer> clientarray = null;
	private ClientServer server;
	private InetAddress IPserver;
	private int portServer = 8080;
	private String nameUser = "";
	private boolean isStop = false;
	private static int portClient = 10000;  
	private int timeOut = 10000;  //time to each request is 10 seconds.
	private Socket socketClient;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;

	
	public Client(String arg, int arg1, String name, String dataUser) throws Exception {
		IPserver = InetAddress.getByName(arg);
		nameUser = name;
		portClient = arg1;
		clientarray = Decode.getAllUser(dataUser);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				updateFriend();
			}
		}).start();
		server = new ClientServer(nameUser);
		(new Request()).start();
	}

	public static int getPort() {
		return portClient; 
	}

	public void request() throws Exception {
		socketClient = new Socket(); 
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.sendRequest(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		msg = (String) serverInputStream.readObject();
		serverInputStream.close();
		//		just for test
		//System.out.println("toantoan" + msg); //test server return to user
		clientarray = Decode.getAllUser(msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateFriend();
				
			}
		}).start();
	}

	public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					Thread.sleep(timeOut);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void addFriend(String friend) throws IOException {
		socketClient = new Socket(); 
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.addFriend(nameUser,friend);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		try {
			msg = (String) serverInputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverInputStream.close();
		clientarray = Decode.getAllUser(msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.print("Hello");
				updateFriend();
				
			}
		}).start();
	}
	public void acceptFriend(String friend) throws IOException {
		socketClient = new Socket(); 
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.acceptRequest(nameUser, friend);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		try {
			msg = (String) serverInputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("msg");
		serverInputStream.close();
		clientarray = Decode.getAllUser(msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateFriend();
			}
		}).start();
	}

	public void intialNewChat(String IP, int host, String guest) throws Exception {
		final Socket connclient = new Socket(InetAddress.getByName(IP),host);
		System.out.println("Checking");
		ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream()); 
		sendrequestChat.writeObject(Encode.sendRequestChat(nameUser));
		sendrequestChat.flush(); 
		
		ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
		String msg = (String) receivedChat.readObject();
		if (msg.equals(Tags.CHAT_DENY_TAG)) { 
			MainGui.request("Your friend denied connect with you!", false);
			connclient.close(); 
			return;
		}
		//not if
		new ChatGui(nameUser, guest, connclient , portClient);
	}
	public void intialNewGroupChat(ArrayList<String> ListIP, ArrayList<Integer> ListPort, ArrayList<String> Listguest) throws Exception {
		Socket connclient;
		boolean isAccept =false;
		String msg;
		ArrayList<String>ListIP_Has_Creator=ListIP;
		ListIP_Has_Creator.add(InetAddress.getLocalHost().getHostAddress());
		ArrayList<Integer>ListPort_hasCreator=ListPort;
		ListPort_hasCreator.add(portClient);
		ArrayList<String>ListGuest_hasCreator=Listguest;
		ListGuest_hasCreator.add(nameUser);
		for (int i = 0; i < ListIP.size(); i++) {
			connclient = new Socket(InetAddress.getByName(ListIP.get(i)), ListPort.get(i));
			ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream()); 
			sendrequestChat.writeObject(Encode.sendRequestGroupChat(nameUser,ListIP_Has_Creator,ListPort_hasCreator,ListGuest_hasCreator)); 
			sendrequestChat.flush(); 
			ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
			msg = (String) receivedChat.readObject(); 
			if (!msg.equals(Tags.CHAT_DENY_TAG)) isAccept=true;
			connclient.close();
		}
		if (!isAccept) { 
			MainGui.request("All your friend denied connect with you!", false);
			return;
		}
		server.exit();
		new GroupChatGui(nameUser,portClient,nameUser , ListIP_Has_Creator, ListPort_hasCreator, ListGuest_hasCreator);
	return;
	}

	public void exit() throws IOException, ClassNotFoundException {
		isStop = true;
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.exit(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverOutputStream.close(); 
		server.exit();
	}

	public void updateFriend(){
		int size = clientarray.size();
		MainGui.resetList();
		//while loop
		Peer peer=null;
		int i = 0;
		while (i < size) {
			peer=clientarray.get(i);
			if (!peer.getName().equals(nameUser))
				MainGui.updateActiveMainGui(peer.getName());
			else {
				for (int j = 0; j < peer.getListRequestFr().size(); j++) {
					MainGui.updateRequestFrMainGui(peer.getListRequestFr().get(j));
				}
				for (int j = 0; j < peer.getListFr().size(); j++) {
					for (int j2 = 0; j2 < size; j2++) {
						if(peer.getListFr().get(j).equals(clientarray.get(j2).getName()) ) {
							if(clientarray.get(j2).getState()==true) MainGui.updateFriendMainGui(clientarray.get(j2).getName(),true);
							else MainGui.updateFriendMainGui(clientarray.get(j2).getName(),false); 
					}
				}
				}
			}
			i++;
		}
	}
}