
public class ChatUser {
	ChatUser(String username,String tipo){
		this.username = username;
		if (tipo.equals("sender")) setSender(true);
		if (tipo.equals("receiver")) setReceiver(true);
	}
	
	
	
	private String username;
	private boolean sender;
	private boolean receiver;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isSender() {
		return sender;
	}
	public void setSender(boolean sender) {
		this.sender = sender;
	}
	public boolean isReceiver() {
		return receiver;
	}
	public void setReceiver(boolean receiver) {
		this.receiver = receiver;
	}
}
