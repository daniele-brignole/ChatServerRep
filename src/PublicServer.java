import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;

public class PublicServer {

	public static void main(String[] args) {
		try {
			System.out.println("SERVER STARTED");
			ServerSocket server = new ServerSocket(4000);
			
			while(true) {
				Socket s = server.accept();
				System.out.println("CONNECTION ACCEPTED");
				OutputStream os = s.getOutputStream();
				InputStream is = s.getInputStream();
				SenderRunner sr = new SenderRunner(is);
				Thread ts = new Thread(sr);
				ts.start();
				
				
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
