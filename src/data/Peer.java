package data;

import java.util.ArrayList;

public class Peer {

	private String namePeer = "";
	private String password ="";
	private String hostPeer = "";
	private int portPeer = 0;
	private boolean state =false;
	private ArrayList<String> listFriend = null;
	private ArrayList<String> listRequestFr=null;
	public void setPeer(String name,String pwd, String host, int port,boolean statee) {
		namePeer = name;
		hostPeer = host;
		portPeer = port;
		password=pwd;
		state=statee;
		listFriend=new ArrayList<String>();
		listRequestFr=new ArrayList<String>();
	}

	public void setName(String name) {
		namePeer = name;
	}

	public void setHost(String host) {
		hostPeer = host;
	}

	public void setPort(int port) {
		portPeer = port;
	}

	public String getName() {
		return namePeer;
	}

	public String getHost() {
		return hostPeer;
	}

	public int getPort() {
		return portPeer;
	}
	public String getPass() {
		return password;
	}
	public void setPass(String pwd) {
		password=pwd;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean statee) {
		state=statee;
	}
	public void setListFriend(ArrayList<String> listFriend) {
		this.listFriend=listFriend;
	}
	public ArrayList<String> getListFr(){
		return listFriend;
	}
	public void setListRequestFriend(ArrayList<String> listFriend) {
		this.listRequestFr=listFriend;
	}
	public ArrayList<String> getListRequestFr(){
		return listRequestFr;
	}
}

