package data;

public class Friend {
	private String user="";
	private String userFriend="";
	public void setFriend(String userName,String userFriendName) {
		user=userName;
		userFriend=userFriendName;
	}
	public String getUser() {
		return user;
	}
	public String getUserFriend() {
		return userFriend;
	}
	public void setUser(String userName) {
		user=userName;
	}
	public void setUserFriend(String userFriendNamee) {
		userFriend=userFriendNamee;
	}
}
