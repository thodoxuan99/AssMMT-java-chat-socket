package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import data.Peer;
import data.Friend;
import tags.Decode;
import tags.Tags;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class ServerCore {

	private ArrayList<Peer> dataPeer = null;
	private static ArrayList<Friend> listFriend = null;
	private static ArrayList<Friend> listRequestFr = null;
	private ServerSocket server;						
	private Socket connection;
	private ObjectOutputStream obOutputClient;		
	private ObjectInputStream obInputStream;			
	public boolean isStop = false, isExit = false;		
	
	//Intial server socket
	public ServerCore(int port) throws Exception { 
		server = new ServerSocket(port);
		listFriend=loadlistFriend();
		listRequestFr=loadlistRequestFriend();
		dataPeer=loadPeer();
		(new WaitForConnect()).start();			
	}
	
	//	show status of state
	private String sendSessionAccept() throws Exception {
		String msg = Tags.SESSION_ACCEPT_OPEN_TAG;
		int size = dataPeer.size();				
		for (int i = 0; i < size; i++) {		
			Peer peer = dataPeer.get(i);	
			msg += Tags.PEER_OPEN_TAG;			
			msg += Tags.PEER_NAME_OPEN_TAG;
			msg += peer.getName();
			msg += Tags.PEER_NAME_CLOSE_TAG;
			msg += Tags.IP_OPEN_TAG;
			msg += peer.getHost();
			msg += Tags.IP_CLOSE_TAG;
			msg += Tags.PORT_OPEN_TAG;
			msg += peer.getPort();
			msg += Tags.PORT_CLOSE_TAG;
			msg += Tags.PEER_STATE_OPEN_TAG;
			msg += Boolean.toString(peer.getState());
			msg += Tags.PEER_STATE_CLOSE_TAG;
			msg += Tags.PEER_LISTFR_OPEN_TAG;
			for (int j = 0; j < peer.getListFr().size(); j++) {
				msg += peer.getListFr().get(j);
				msg+= ',';
			}
			msg += Tags.PEER_LISTFR_CLOSE_TAG;
			msg += Tags.PEER_LISTREFR_OPEN_TAG;
			for (int j = 0; j < peer.getListRequestFr().size(); j++) {
				msg += peer.getListRequestFr().get(j);
				msg+= ',';
			}
			msg += Tags.PEER_LISTREFR_CLOSE_TAG;
			msg += Tags.PEER_CLOSE_TAG;			
		}
		msg += Tags.SESSION_ACCEPT_CLOSE_TAG;	
		return msg;
	}
	
	//	close server
	public void stopserver() throws Exception {
		storeListFriend(listFriend);
		storeListRequestFriend(listRequestFr);
		storePeer(dataPeer);
		isStop = true;
		server.close();							
		connection.close();						
	}
	
	//client connect to server
	private boolean waitForConnection() throws Exception {
		connection = server.accept();			
		obInputStream = new ObjectInputStream(connection.getInputStream());		
		String msg = (String) obInputStream.readObject();		 				 
		ArrayList<String> getDataUser = Decode.getUser(msg);	
		ArrayList<String> getDataAccount = Decode.getAccount(msg);
		ArrayList<String> getAddFriend = Decode.addfriend(msg);
		ArrayList<String> getAcceptFr = Decode.acceptFr(msg);
		ServerGui.updateMessage(msg);
		if (getDataAccount != null) {
			ServerGui.updateMessage("Sign Up");
			if (register(getDataAccount.get(0),getDataAccount.get(1),getDataAccount.get(2))) {
				saveNewPeer(getDataAccount.get(0),getDataAccount.get(1), connection.getInetAddress()			
						.toString(), Integer.parseInt(getDataAccount.get(3))); 			
				ServerGui.updateMessage(getDataAccount.get(0)); 
			} else
				return false;
		}
		else if (getDataUser != null) {
			ServerGui.updateMessage("Login");
			if (isExistsUser(getDataUser.get(0),getDataUser.get(1))) {
				setOnline(getDataUser.get(0),getDataUser.get(1), connection.getInetAddress()			
						.toString(), Integer.parseInt(getDataUser.get(2))); 
				ServerGui.updateMessage(getDataUser.toString());
				ServerGui.updateMessage(getDataUser.get(0));	 
				ServerGui.updateNumberClient();
			} else
				return false;
		}
		else if(getAddFriend!=null) {
			ServerGui.updateMessage("Addfr");
			updateListRequest(getAddFriend.get(0), getAddFriend.get(1));
		}
		else if(getAcceptFr!=null) {
			ServerGui.updateMessage("AcceptFr");
			updateListFriend(getAcceptFr.get(0), getAcceptFr.get(1));
		}
		else {		
			ServerGui.updateMessage("Exit");
			isExit=! Decode.updatePeerOnline(dataPeer, msg);		
			ServerGui.updateMessage(Boolean.toString(isExit));
			if(isExit)ServerGui.decreaseNumberClient();
		}
		return true;
	}
	
	private boolean setOnline(String name,String pwd, String ip, int port) throws Exception {
		int size= dataPeer.size(); 
		for (int i = 0; i < size; i++) { 
			if (dataPeer.get(i).getName().equals(name)) {
				dataPeer.get(i).setHost(ip);
				dataPeer.get(i).setPort(port);
				dataPeer.get(i).setState(true);
				return true;
			}			
		}
		return false;
	}
	
	private void updateListRequest(String sender,String reiceiver) {
		boolean a,b;
		int temp_int=0;
		for (int i = 0; i < dataPeer.size(); i++) {
			if (dataPeer.get(i).getName().equals(reiceiver))temp_int=i;
			if(dataPeer.get(i).getName().equals(sender)) {
				for (int j = 0; j < dataPeer.get(i).getListFr().size(); j++) {
					if(dataPeer.get(i).getListFr().equals(reiceiver)) return;
				}
			}
		}
		dataPeer.get(temp_int).getListRequestFr().add(sender);
		for (int i = 0; i < listRequestFr.size(); i++) {
			if(listRequestFr.get(i).getUser().equals(sender)&&listRequestFr.get(i).getUserFriend().equals(reiceiver)) return;
		}
		for (int i = 0; i < listFriend.size(); i++) {
			a=listFriend.get(i).getUser().equals(sender)&&listFriend.get(i).getUserFriend().equals(reiceiver);
			b=listFriend.get(i).getUser().equals(reiceiver)&&listFriend.get(i).getUserFriend().equals(sender);
			if(a||b) return;
		}
		Friend friend =new Friend();
		friend.setFriend(sender, reiceiver);
		listRequestFr.add(friend);
		
	}
	
	private void updateListFriend(String sender,String reiceiver) {
		ServerGui.updateMessage(sender);
		ServerGui.updateMessage(reiceiver);
		Friend friend = new Friend();
		for (int i = 0; i < dataPeer.size(); i++) {
			if(dataPeer.get(i).getName().equals(sender)) {
				dataPeer.get(i).getListRequestFr().remove(reiceiver);
				dataPeer.get(i).getListFr().add(reiceiver);
				ServerGui.updateMessage("Check");
			}	
			if(dataPeer.get(i).getName().equals(reiceiver))
				dataPeer.get(i).getListFr().add(sender);
		}
		for (int i = 0; i <listRequestFr.size(); i++) {
			if(listRequestFr.get(i).getUser().equals(reiceiver)&&listRequestFr.get(i).getUserFriend().equals(sender)) {
				listRequestFr.remove(i);
				break;
			}
		}
		friend.setFriend(reiceiver, sender);
		listFriend.add(friend);
	}
	
	private Peer saveNewPeer(String user,String pwd, String ip, int port) throws Exception {
		Peer newPeer = new Peer();		
		if (dataPeer.size() == 0)				
			dataPeer = new ArrayList<Peer>();
		newPeer.setPeer(user,pwd, ip, port,false);		
		dataPeer.add(newPeer);
		return newPeer;
	}
	
	private boolean register(String name,String pass,String confirmPass) {
		if (! pass.equals(confirmPass)) return false;
		int size = dataPeer.size();
		for (int i = 0; i < size; i++) {
			Peer peer = dataPeer.get(i);
			if (peer.getName().equals(name))
				return false;
		}
		return true;
		
	}
	private boolean isExistsUser(String name,String pass) throws Exception {
		int size = dataPeer.size();
		for (int i = 0; i < size; i++) {
			Peer peer = dataPeer.get(i); 
			if (peer.getName().equals(name) && peer.getPass().equals(pass))
				return true;
		}
		return false;
	}
	
	private ArrayList<Friend> loadlistFriend() throws IOException{
		ArrayList<Friend> listFriend =new ArrayList<Friend>();
		Friend newFriend= new Friend();
		BufferedReader br =null;
		try {
			br = new BufferedReader(new FileReader("data\\friend.txt")); 
			int num=0;
			char ch;
			int j=0;
			String temp="";
			while ((num=br.read())!=-1) {
				ch=(char)num;
				if(num!=13) {
				if (ch!='\n') {
					if (ch!=',') {
						temp=temp+ch;
					}else {
						switch(j) {
						case 0: newFriend.setUser(temp);; break;
						case 1: newFriend.setUserFriend(temp);; break;
						}
						j++;
						temp="";
						
					}
					
							
				}else {
					j=0;
					listFriend.add(newFriend);
					newFriend=new Friend();
				}
				
			}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			br.close();
		}
		return listFriend;
	}
	
	public static void storeListFriend(ArrayList<Friend> listFriend) throws IOException {
		FileWriter out=null;
		Friend friend=new Friend();
		try {
			out =  new FileWriter("data\\friend.txt");
			for (int i = 0; i < listFriend.size(); i++) {
				friend=listFriend.get(i);
				out.write(friend.getUser());out.write(',');
				out.write(friend.getUserFriend());out.write(',');
				out.write('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	private ArrayList<Friend> loadlistRequestFriend() throws IOException{
		ArrayList<Friend> listFriend =new ArrayList<Friend>();
		Friend newFriend= new Friend();
		BufferedReader br =null;
		try {
			br = new BufferedReader(new FileReader("data\\requestFriend.txt")); 
			int num=0;
			char ch;
			int j=0;
			String temp="";
			while ((num=br.read())!=-1) {
				ch=(char)num;
				if(num!=13) {
				if (ch!='\n') {
					if (ch!=',') {
						temp=temp+ch;
					}else {
						switch(j) {
						case 0: newFriend.setUser(temp);; break;
						case 1: newFriend.setUserFriend(temp);; break;
						}
						j++;
						temp="";
						
					}
					
							
				}else {
					j=0;
					listFriend.add(newFriend);
					newFriend=new Friend();
				}
				
			}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			br.close();
		}
		return listFriend;
	}
	
	public static void storeListRequestFriend(ArrayList<Friend> listFriend) throws IOException {
		FileWriter out=null;
		Friend friend=new Friend();
		try {
			out =  new FileWriter("data\\requestFriend.txt");
			for (int i = 0; i < listFriend.size(); i++) {
				friend=listFriend.get(i);
				out.write(friend.getUser());out.write(',');
				out.write(friend.getUserFriend());out.write(',');
				out.write('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	
	private ArrayList<Peer> loadPeer() throws IOException{
		ArrayList<Peer> listPeer =new ArrayList<Peer>();
		Peer newPeer= new Peer();
		ArrayList<String> listFriend = null;
		ArrayList<String> listRequestFr = null;
		BufferedReader br =null;
		try {
			br = new BufferedReader(new FileReader("data\\account.txt")); 
			System.out.print("Content\n");
			int num=0;
			char ch;
			int j=0;
			String temp="";
			while ((num=br.read())!=-1) {
				ch=(char)num;
				if(num!=13) {
				if (ch!='\n') {
					if (ch!=',') {
						temp=temp+ch;
					}else {
						switch(j) {
						case 0: newPeer.setName(temp); break;
						case 1: newPeer.setPass(temp); break;
						case 2: newPeer.setHost(temp); break;
						case 3: newPeer.setPort(Integer.parseInt(temp) ); break;
						case 4: newPeer.setState(Boolean.parseBoolean(temp)); break;
						}
						j++;
						temp="";
						
					}
					
							
				}else {
					j=0;
					listFriend=getFriend(newPeer.getName());
					newPeer.setListFriend(listFriend);
					listRequestFr=getRequestFriend(newPeer.getName());
					newPeer.setListRequestFriend(listRequestFr);
					listPeer.add(newPeer);
					newPeer=new Peer();
				}
				
			}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			br.close();
		}
		for (int i = 0; i < listPeer.size(); i++) {
			System.out.print(listPeer.get(i).getName());System.out.print("  ");
			System.out.print(listPeer.get(i).getPass());System.out.print("  ");
			System.out.print(listPeer.get(i).getHost());System.out.print("  ");
			System.out.print(listPeer.get(i).getPort());System.out.print("  ");
			System.out.print(listPeer.get(i).getState());System.out.print("  ");
			System.out.print(listPeer.get(i).getListFr());System.out.print("  ");
			System.out.println(listPeer.get(i).getListRequestFr());
		}
		return listPeer;
	}
	
	
	
	public static void storePeer(ArrayList<Peer> listPeer) throws IOException {
		FileWriter out=null;
		Peer peer=new Peer();
		try {
			out =  new FileWriter("data\\account.txt");
			for (int i = 0; i < listPeer.size(); i++) {
				peer=listPeer.get(i);
				out.write(peer.getName());out.write(',');
				out.write(peer.getPass());out.write(',');
				out.write(peer.getHost());out.write(',');
				out.write(Integer.toString(peer.getPort()));out.write(',');
				out.write(Boolean.toString(peer.getState()));out.write(',');
				out.write('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	public static ArrayList<String> getFriend(String name){
		ArrayList<String> listFriendd =new ArrayList<String>();
		for (int i = 0; i < listFriend.size(); i++) {
			if ( listFriend.get(i).getUser().equals(name) && !listFriendd.contains(listFriend.get(i).getUserFriend()) ) {
				listFriendd.add(listFriend.get(i).getUserFriend());
			}
			if ( listFriend.get(i).getUserFriend().equals(name)&&!listFriendd.contains(listFriend.get(i).getUser())) {
				listFriendd.add(listFriend.get(i).getUser());
			}
		}
		return listFriendd;
	}
	public static ArrayList<String> getRequestFriend(String name){
		ArrayList<String> listReFriendd =new ArrayList<String>();
		for (int i = 0; i < listRequestFr.size(); i++) {
			if ( listRequestFr.get(i).getUserFriend().equals(name)) {
				listReFriendd.add(listRequestFr.get(i).getUser());
			}
		}
		return listReFriendd;
	}
	
	public class WaitForConnect extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (!isStop) {
					if (waitForConnection()) {
						if (isExit) {
							isExit = false;
						} else {
							obOutputClient = new ObjectOutputStream(connection.getOutputStream());
							obOutputClient.writeObject(sendSessionAccept());
							obOutputClient.flush();
							obOutputClient.close();
						}
					} else {
						obOutputClient = new ObjectOutputStream(connection.getOutputStream());
						obOutputClient.writeObject(Tags.SESSION_DENY_TAG);
						obOutputClient.flush();
						obOutputClient.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
