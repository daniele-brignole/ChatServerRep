import java.util.*;

public class ChatMessage {
	private String destination;
	private String msx;
	private String timestamp;
	private int counter;
	public ChatMessage(String destination, String msx, int counter) {
		this.destination = destination;
		this.counter = counter;
		Date d = new Date();
		int h = d.getHours();
		int m = d.getMinutes();
		
		timestamp = "["+h + ":" + m + "]"; 
		this.msx = timestamp + " " + msx;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMsx() {
		return msx;
	}
	public void setMsx(String msx) {
		this.msx = msx;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
