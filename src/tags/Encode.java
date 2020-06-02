package tags;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encode {

	private static Pattern checkMessage = Pattern.compile("[^<>]*[<>]");

	public static String Login(String name, String password,String port) {
		return Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.PEER_PASS_OPEN_TAG + password + Tags.PEER_PASS_CLOSE_TAG+ Tags.PORT_OPEN_TAG + port
				+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG; 
	}
	
	public static String addFriend(String name,String friend) { 
		return Tags.ADDFR_OPEN_TAG+ Tags.SENDER_OPEN_TAG+name+
				Tags.SENDER_CLOSE_TAG+Tags.REICEIVER_OPEN_TAG+friend+
				Tags.REICEIVER_CLOSE_TAG+Tags.ADDFR_CLOSE_TAG;
	}
	public static String acceptRequest(String name,String friend) {
		return Tags.ACCEPTFR_OPEN_TAG+ Tags.SENDER_OPEN_TAG+name+
				Tags.SENDER_CLOSE_TAG+Tags.REICEIVER_OPEN_TAG+friend+
				Tags.REICEIVER_CLOSE_TAG+Tags.ACCEPTFR_CLOSE_TAG;
	}
	public static String sendRequest(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
				+ name + Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG
				+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
	}

	public static String sendMessage(String message) {
//		System.out.println("(encode)Dau vao message: " + message);
		Matcher findMessage = checkMessage.matcher(message);
		String result = "";
		while (findMessage.find()) {
			String subMessage = findMessage.group(0);
			System.out.println("subMessage: " + subMessage);
			int begin = subMessage.length();			//do dai chuoi con
			char nextChar = message.charAt(subMessage.length() - 1); //ky tu cuoi cung cua message
			System.out.println("nextChar: " + nextChar);
			result += subMessage; // + nextChar
			subMessage = message.substring(begin, message.length()); 
			message = subMessage;
			findMessage = checkMessage.matcher(message);
		}
		result += message;
		System.out.println("(encode)Dau ra message: " + Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG);
		return Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG;
	}
	public static String sendMessageGroup(String sender,String message) {
//		System.out.println("(encode)Dau vao message: " + message);
		Matcher findMessage = checkMessage.matcher(message);
		String result = "";
		while (findMessage.find()) {
			String subMessage = findMessage.group(0);
			System.out.println("subMessage: " + subMessage);
			int begin = subMessage.length();			//do dai chuoi con
			char nextChar = message.charAt(subMessage.length() - 1); //ky tu cuoi cung cua message
			System.out.println("nextChar: " + nextChar);
			result += subMessage; // + nextChar
			subMessage = message.substring(begin, message.length()); 
			message = subMessage;
			findMessage = checkMessage.matcher(message);
		}
		result += message;
		return Tags.PEER_NAME_OPEN_TAG+sender+Tags.PEER_NAME_CLOSE_TAG+ Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG;
	}

	public static String sendRequestChat(String name) {
		return Tags.CHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG;
	}
	public static String sendRequestGroupChat(String name,ArrayList<String>IP, ArrayList<Integer> port, ArrayList<String> ListName) {
		String strListIP="",strListPort="",strListGuest="";
		for (int i = 0; i < IP.size(); i++) {
			strListIP=strListIP+IP.get(i)+',';
			strListPort=strListPort+Integer.toString(port.get(i))+',';
			strListGuest=strListGuest+ListName.get(i)+',';
		} 
		return Tags.GROUPCHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.IP_OPEN_TAG + strListIP
				+Tags.IP_CLOSE_TAG+ Tags.PORT_OPEN_TAG+strListPort
				+Tags.PORT_CLOSE_TAG+Tags.GROUPNAME_OPEN_TAG+ strListGuest
				+ Tags.GROUPNAME_CLOSE_TAG + Tags.GROUPCHAT_REQ_CLOSE_TAG;
	}

	public static String sendFile(String nameFile) {
		return  Tags.FILE_REQ_OPEN_TAG + nameFile + Tags.FILE_REQ_CLOSE_TAG  ; 
	}

	public static String exit(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
				+ name + Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_OFFLINE + Tags.STATUS_CLOSE_TAG
				+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG; 
	}

	public static String getCreateAccount(String name, String pass,String ConPass ,String port) {
		return Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.PEER_PASS_OPEN_TAG + pass
				+ Tags.PEER_PASS_CLOSE_TAG+ Tags.PEER_CONPASS_OPEN_TAG+ ConPass
				+ Tags.PEER_CONPASS_CLOSE_TAG+ Tags.PORT_OPEN_TAG + port 
				+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG;
	}
}

