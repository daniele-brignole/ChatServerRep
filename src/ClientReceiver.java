import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientReceiver {
	static int port = 4000;
	public static void main(String[] args) throws IOException {
		String addr = "127.0.0.1";
		String username;
		Socket s = new Socket();
		InetSocketAddress ISaddr = new InetSocketAddress(addr, port);
		stamp("Benvenuto, inserisci il tuo username per effettuare il login");
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(reader);
		username = buffer.readLine();
		try{
			s.connect(ISaddr);
			stamp("Benvenuto " + username + ", hai effettuato l'accesso al servizio di lettura");
			OutputStream os = s.getOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(os);
			BufferedWriter outbuffer = new BufferedWriter(wr);
			
			outbuffer.write(username);
			outbuffer.newLine();
			outbuffer.flush();
			
			outbuffer.write("receiver");
			outbuffer.newLine();
			outbuffer.flush();
						
			InputStream is = s.getInputStream();
			InputStreamReader rd = new  InputStreamReader(is);
			BufferedReader inbuffer = new BufferedReader(rd);
			
			//ObjectInputStream ois = new ObjectInputStream(is);
			
			while(true){
				String response = inbuffer.readLine();
				System.out.println(response);
				//String line = buffer.readLine();
				//outbuffer.write(line);
				//outbuffer.newLine();
				//outbuffer.flush();
				
				
				//line = buffer.readLine();
			}
			
			
		} 
		catch(Exception e){
			
		}
		
	}
	
	public static void stamp(String s){
		System.out.println(s);
	}

	

}
