import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ReceiverRunner implements Runnable{
	private Socket client = null;
	String username = "";
	public ReceiverRunner(Socket client){
		this.client = client;
	}
			
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Receiver");
		InputStream is;
		try {
			is = client.getInputStream();
		
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader buffer= new BufferedReader(reader);
		
			OutputStream os = this.client.getOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(os);
			BufferedWriter outbuffer = new BufferedWriter(wr);
			
			String line = buffer.readLine();
			username = line;
			System.out.println("Sender " + username + " si è connesso");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		while(true){
			
		}
	}

}
