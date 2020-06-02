package tags;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Tag;

import data.Peer;

public class Decode {

	private static Pattern Login = Pattern
			.compile(Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + ".*"
					+ Tags.PEER_NAME_CLOSE_TAG + Tags.PEER_PASS_OPEN_TAG+".*"				
					+Tags.PEER_PASS_CLOSE_TAG+ Tags.PORT_OPEN_TAG + ".*"
					+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG);
	private static Pattern AddFriend = Pattern
			.compile(Tags.ADDFR_OPEN_TAG + Tags.SENDER_OPEN_TAG + ".*"
					+ Tags.SENDER_CLOSE_TAG + Tags.REICEIVER_OPEN_TAG+".*"				
					+Tags.REICEIVER_CLOSE_TAG+ Tags.ADDFR_CLOSE_TAG);
	private static Pattern RequestFr = Pattern
			.compile(Tags.ACCEPTFR_OPEN_TAG + Tags.SENDER_OPEN_TAG + ".*"
					+ Tags.SENDER_CLOSE_TAG + Tags.REICEIVER_OPEN_TAG+".*"				
					+Tags.REICEIVER_CLOSE_TAG+ Tags.ACCEPTFR_CLOSE_TAG);
	private static Pattern SignUp = Pattern
			.compile(Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + ".*"
					+ Tags.PEER_NAME_CLOSE_TAG + Tags.PEER_PASS_OPEN_TAG+".*"				
					+Tags.PEER_PASS_CLOSE_TAG+ Tags.PEER_CONPASS_OPEN_TAG+".*"
					+Tags.PEER_CONPASS_CLOSE_TAG+ Tags.PORT_OPEN_TAG + ".*"
					+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG);

	private static Pattern users = Pattern.compile(Tags.SESSION_ACCEPT_OPEN_TAG
			+ "(" + Tags.PEER_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + ".+"
			+ Tags.PEER_NAME_CLOSE_TAG + Tags.IP_OPEN_TAG + ".+"
			+ Tags.IP_CLOSE_TAG + Tags.PORT_OPEN_TAG + "[0-9]+"
			+ Tags.PORT_CLOSE_TAG + Tags.PEER_STATE_OPEN_TAG+".+"
			+Tags.PEER_STATE_CLOSE_TAG+ Tags.PEER_LISTFR_OPEN_TAG+".+"
			+Tags.PEER_LISTFR_CLOSE_TAG+Tags.PEER_LISTREFR_OPEN_TAG+".+"
			+Tags.PEER_LISTREFR_CLOSE_TAG+Tags.PEER_CLOSE_TAG + ")*"
			+ Tags.SESSION_ACCEPT_CLOSE_TAG);

	private static Pattern request = Pattern
			.compile(Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
					+ "[^<>]+" + Tags.PEER_NAME_CLOSE_TAG
					+ Tags.STATUS_OPEN_TAG + "(" + Tags.SERVER_ONLINE + "|"
					+ Tags.SERVER_OFFLINE + ")" + Tags.STATUS_CLOSE_TAG
					+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG);

	private static Pattern messageGroup = Pattern.compile(Tags.PEER_NAME_OPEN_TAG
			+ ".*" + Tags.PEER_NAME_CLOSE_TAG+Tags.CHAT_MSG_OPEN_TAG
			+ ".*" + Tags.CHAT_MSG_CLOSE_TAG);
	private static Pattern message = Pattern.compile(Tags.CHAT_MSG_OPEN_TAG
			+ ".*" + Tags.CHAT_MSG_CLOSE_TAG);

	private static Pattern checkNameFile = Pattern
			.compile(Tags.FILE_REQ_OPEN_TAG + ".*" + Tags.FILE_REQ_CLOSE_TAG);

	private static Pattern feedBack = Pattern
			.compile(Tags.FILE_REQ_ACK_OPEN_TAG + ".*"
					+ Tags.FILE_REQ_ACK_CLOSE_TAG);
	public static ArrayList<String> getAccount(String msg) {
		ArrayList<String> user = new ArrayList<String>();
		if (SignUp.matcher(msg).matches()) {
			Pattern findName = Pattern.compile(Tags.PEER_NAME_OPEN_TAG + ".*"
					+ Tags.PEER_NAME_CLOSE_TAG);
			Pattern findPass = Pattern.compile(Tags.PEER_PASS_OPEN_TAG + ".*"
					+ Tags.PEER_PASS_CLOSE_TAG);
			Pattern findConPass = Pattern.compile(Tags.PEER_CONPASS_OPEN_TAG + ".*"
					+ Tags.PEER_CONPASS_CLOSE_TAG);
			Pattern findPort = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
					+ Tags.PORT_CLOSE_TAG);
			Matcher find = findName.matcher(msg);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(11, name.length() - 12));
				find = findPass.matcher(msg);
				if (find.find()) {
					String pass = find.group(0);
					user.add(pass.substring(11, pass.length() - 12));
					find = findConPass.matcher(msg);
					if (find.find()) {
						String ConPass = find.group(0);
						user.add(ConPass.substring(14, ConPass.length() - 15));
						find = findPort.matcher(msg);
						if (find.find()) {
							String port = find.group(0);
							user.add(port.substring(6, port.length() - 7));
						}else 
							return null;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} else
			return null;
		return user;
	}
	
	public static ArrayList<String> getUser(String msg) {
		ArrayList<String> user = new ArrayList<String>();
		if (Login.matcher(msg).matches()) { 
			Pattern findName = Pattern.compile(Tags.PEER_NAME_OPEN_TAG + ".*"
					+ Tags.PEER_NAME_CLOSE_TAG);
			Pattern findPass = Pattern.compile(Tags.PEER_PASS_OPEN_TAG + ".*"
					+ Tags.PEER_PASS_CLOSE_TAG);
			Pattern findPort = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
					+ Tags.PORT_CLOSE_TAG);
			Matcher find = findName.matcher(msg);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(11, name.length() - 12));
				find = findPass.matcher(msg);
				if (find.find()) {
					String pass = find.group(0);
					user.add(pass.substring(11, pass.length() - 12));
					find = findPort.matcher(msg);
					if (find.find()) {
						String port = find.group(0);
						user.add(port.substring(6, port.length() - 7));
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} else
			return null;
		return user;
	}

	public static ArrayList<String> addfriend(String msg){
		ArrayList<String> temp=new ArrayList<String>();
		Pattern findsender = Pattern.compile(Tags.SENDER_OPEN_TAG
				+".*" + Tags.SENDER_CLOSE_TAG);
		Pattern findreiceiver = Pattern.compile(Tags.REICEIVER_OPEN_TAG + ".*"
				+ Tags.REICEIVER_CLOSE_TAG);
		if (AddFriend.matcher(msg).matches()) {
			Matcher find = findsender.matcher(msg); 
			if (find.find()) {
				String sender = find.group(0);
				temp.add(sender.substring(8,sender.length()-9));
				find = findreiceiver.matcher(msg); 
				if(find.find()) {
					String reiceiver = find.group(0);
					temp.add(reiceiver.substring(11,reiceiver.length()-12));
				}
				else return null;
			}
			else return null;
				
		}else return null;
		return temp;
	}
	public static ArrayList<String> acceptFr(String msg){
		ArrayList<String> temp=new ArrayList<String>();
		Pattern findsender = Pattern.compile(Tags.SENDER_OPEN_TAG
				+".*" + Tags.SENDER_CLOSE_TAG);
		Pattern findreiceiver = Pattern.compile(Tags.REICEIVER_OPEN_TAG + ".*"
				+ Tags.REICEIVER_CLOSE_TAG);
		if (RequestFr.matcher(msg).matches()) {
			Matcher find = findsender.matcher(msg); 
			if (find.find()) {
				String sender = find.group(0);
				temp.add(sender.substring(8,sender.length()-9));
				find = findreiceiver.matcher(msg); 
				if(find.find()) {
					String reiceiver = find.group(0);
					temp.add(reiceiver.substring(11,reiceiver.length()-12));
				}
				else return null;
			}
			else return null;
				
		}else return null;
		return temp;
	}
	public static ArrayList<Peer> getAllUser(String msg) {
		ArrayList<Peer> user = new ArrayList<Peer>();
		Pattern findPeer = Pattern.compile(Tags.PEER_OPEN_TAG
				+ Tags.PEER_NAME_OPEN_TAG + "[^<>]*" + Tags.PEER_NAME_CLOSE_TAG
				+ Tags.IP_OPEN_TAG + "[^<>]*" + Tags.IP_CLOSE_TAG
				+ Tags.PORT_OPEN_TAG + "[0-9]*" + Tags.PORT_CLOSE_TAG
				+ Tags.PEER_STATE_OPEN_TAG + "[^<>]*" + Tags.PEER_STATE_CLOSE_TAG
				+ Tags.PEER_LISTFR_OPEN_TAG + "[^<>]*" + Tags.PEER_LISTFR_CLOSE_TAG
				+ Tags.PEER_LISTREFR_OPEN_TAG + "[^<>]*" + Tags.PEER_LISTREFR_CLOSE_TAG
				+ Tags.PEER_CLOSE_TAG);
		Pattern findName = Pattern.compile(Tags.PEER_NAME_OPEN_TAG + ".*"
				+ Tags.PEER_NAME_CLOSE_TAG);
		Pattern findPort = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
				+ Tags.PORT_CLOSE_TAG); 
		Pattern findIP = Pattern.compile(Tags.IP_OPEN_TAG + ".+"
				+ Tags.IP_CLOSE_TAG); 
		Pattern findState = Pattern.compile(Tags.PEER_STATE_OPEN_TAG + ".+"
				+ Tags.PEER_STATE_CLOSE_TAG);
		Pattern findListFr = Pattern.compile(Tags.PEER_LISTFR_OPEN_TAG + ".*"
				+ Tags.PEER_LISTFR_CLOSE_TAG);
		Pattern findListReFr = Pattern.compile(Tags.PEER_LISTREFR_OPEN_TAG + ".*"
				+ Tags.PEER_LISTREFR_CLOSE_TAG);
		if (users.matcher(msg).matches()) {
			Matcher find = findPeer.matcher(msg);
			while (find.find()) {
				String peer = find.group(0);
				String data = "";
				Peer dataPeer = new Peer();
				Matcher findInfo = findName.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setName(data.substring(11, data.length() - 12));
				}
				findInfo = findIP.matcher(peer); 
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setHost(findInfo.group(0).substring(5,
							data.length() - 5));
				}
				findInfo = findPort.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setPort(Integer.parseInt(data.substring(6,
							data.length() - 7)));
				}
				findInfo = findState.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataPeer.setState(Boolean.parseBoolean(data.substring(12,
							data.length() - 13)));
				}
				findInfo = findListFr.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					String dataString= data.substring(13, data.length()-14);
					ArrayList<String> newlist= new ArrayList<String>();
					String temp="";
					char[] charr= dataString.toCharArray();
					for (int i = 0; i < dataString.length(); i++) {
						if(charr[i]!=',')  temp+=charr[i];
						else {
							newlist.add(temp);
							temp="";
						}
					}
					dataPeer.setListFriend(newlist);
					System.out.println(data);
				}
				
				findInfo = findListReFr.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					String dataString= data.substring(15, data.length()-16);
					ArrayList<String> newlist2= new ArrayList<String>();
					String temp="";
					char[] charr= dataString.toCharArray();
					for (int i = 0; i < dataString.length(); i++) {
						if(charr[i]!=',')  temp+=charr[i];
						else {
							newlist2.add(temp);
							temp="";
						}
					}
					dataPeer.setListRequestFriend(newlist2);
					System.out.println(data);
				}
				user.add(dataPeer);
			}
		} else
			return null;
		return user;
	}
	
	public static boolean updatePeerOnline(
			ArrayList<Peer> peerList, String msg) {
		Pattern alive = Pattern.compile(Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG);
		Pattern killUser = Pattern.compile(Tags.PEER_NAME_OPEN_TAG + "[^<>]*"
				+ Tags.PEER_NAME_CLOSE_TAG);
		if (request.matcher(msg).matches()) {
			Matcher findState = alive.matcher(msg); 
			if (findState.find())
				return true; 
			findState = killUser.matcher(msg);
			if (findState.find()) {
				String findPeer = findState.group(0);
				int size = peerList.size();
				String name = findPeer.substring(11, findPeer.length() - 12);
				for (int i = 0; i < size; i++)
					if (name.equals(peerList.get(i).getName())) {
						peerList.get(i).setState(false);
						break;
					}
			}
		}
		return false;
	}

	public static String getMessage(String msg) {
//		System.out.print("Ham getMessage o decode.java duoc goi");
//		System.out.print(msg);
		if (message.matcher(msg).matches()) {
			int begin = Tags.CHAT_MSG_OPEN_TAG.length();
			int end = msg.length() - Tags.CHAT_MSG_CLOSE_TAG.length();
			System.out.println(begin + " "+ end);
			String message = msg.substring(begin, end);
			return message;
		}
		return null;
	}
	public static ArrayList<String> getMessageGroup(String msg) {
//		System.out.print("Ham getMessage o decode.java duoc goi");
//		System.out.print(msg);
		ArrayList<String> temp= new ArrayList<String>(); 
		Pattern getName = Pattern.compile(Tags.PEER_NAME_OPEN_TAG + "[^<>]*" + Tags.PEER_NAME_CLOSE_TAG);
		Pattern getMess = Pattern.compile(Tags.CHAT_MSG_OPEN_TAG + "[^<>]*" + Tags.CHAT_MSG_CLOSE_TAG);
		if (messageGroup.matcher(msg).matches()) {
			Matcher findInfo = getName.matcher(msg);
			if(findInfo.find()) {
				String sender= findInfo.group(0);
				temp.add(sender.substring(11,sender.length()-12));
				findInfo = getMess.matcher(msg);
				if(findInfo.find()) {
					String Mess= findInfo.group(0);
					temp.add(Mess.substring(10,Mess.length()-11));
					return temp;
				}
				else return null;
			}
			else return null;
		}
		return null;
	}

	public static String getNameRequestChat(String msg) {
		Pattern checkRequest = Pattern.compile(Tags.CHAT_REQ_OPEN_TAG
				+ Tags.PEER_NAME_OPEN_TAG + "[^<>]*" + Tags.PEER_NAME_CLOSE_TAG
				+ Tags.CHAT_REQ_CLOSE_TAG);
		if (checkRequest.matcher(msg).matches()) {  
			int lenght = msg.length();
			String name = msg
					.substring(
							(Tags.CHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG)
									.length(),
							lenght
									- (Tags.PEER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG)
											.length());
			return name;
		}
		return null;
	}
	
	public static ArrayList<String> getNameRequestGroupChat(String msg){
		ArrayList<String> temp=new ArrayList<String>();
		Pattern checkRequestGroup = Pattern.compile(Tags.GROUPCHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + "[^<>]*"
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.IP_OPEN_TAG + "[^<>]*"
				+Tags.IP_CLOSE_TAG+ Tags.PORT_OPEN_TAG+"[^<>]*"
				+Tags.PORT_CLOSE_TAG+Tags.GROUPNAME_OPEN_TAG+ "[^<>]*"
				+ Tags.GROUPNAME_CLOSE_TAG + Tags.GROUPCHAT_REQ_CLOSE_TAG);
		Pattern getName= Pattern.compile(Tags.PEER_NAME_OPEN_TAG+ "[^<>]*"+Tags.PEER_NAME_CLOSE_TAG);
		Pattern getIP= Pattern.compile(Tags.IP_OPEN_TAG +"[^<>]*"+Tags.IP_CLOSE_TAG);
		Pattern getPort= Pattern.compile(Tags.PORT_OPEN_TAG+"[^<>]*"+Tags.PORT_CLOSE_TAG);
		Pattern getGroupName= Pattern.compile(Tags.GROUPNAME_OPEN_TAG+"[^<>]*"+ Tags.GROUPNAME_CLOSE_TAG);
		if (checkRequestGroup.matcher(msg).matches()) {
			Matcher findInfo = getName.matcher(msg);
			if (findInfo.find()) {
				String name =findInfo.group(0);
				temp.add(name.substring(11,name.length()-12));
				findInfo=getGroupName.matcher(msg);
				if(findInfo.find()) {
					String GroupName=findInfo.group(0);
					temp.add(GroupName.substring(12,GroupName.length()-13));
					findInfo=getIP.matcher(msg);
					if(findInfo.find()) {
						String IP=findInfo.group(0);
						temp.add(IP.substring(4,IP.length()-5));
						findInfo=getPort.matcher(msg);
						if(findInfo.find()) {
							String Port=findInfo.group(0);
							temp.add(Port.substring(6,Port.length()-7));
							return temp;
						}
						else return null;
					}
					else return null;
				}
				else return null;
			}
			else return null;
		}
		else return null;
	}
	public static boolean checkFile(String name) {
		
		if (checkNameFile.matcher(name).matches()) return true;
		return false;
		
	}

	public static boolean checkFeedBack(String msg) {
		if (feedBack.matcher(msg).matches())
			return true;
		return false;
	}
}

