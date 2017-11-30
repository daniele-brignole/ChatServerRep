import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
			while(true){			
				InputStream is = s.getInputStream();
				
				reader = new InputStreamReader(is);				
				buffer = new BufferedReader(reader);				
				String line = buffer.readLine();
				
				
				while(line != null) {
					if(line.equals("quit")) {
						s.close();
						System.out.println("CONNECTION CLOSED BY CLIENT");
						break;
					} else {
						System.out.println(line);
					}
					line = buffer.readLine();
				}
			}
			
		} 
		catch(Exception e){
			
		}
		
	}
	
	public static void stamp(String s){
		System.out.println(s);
	}

	

}
