import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



import java.io.OutputStream;

public class PublicServer {

	public static void main(String[] args) {
		msxlist = new Vector<String>();
		counter = 0;
		try {
			ServerSocket server = new ServerSocket(4000);//creo socketserver in ascolto su porta 4000
			while(true){
				Socket s = server.accept(); //il socketserver va in ascolto
				
				SenderRunner chatthread = new SenderRunner(s,msxlist,counter);
				Thread t = new Thread(chatthread);
				t.start();
			}	
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Vector<String> msxlist;
	private static int counter;
	
	public static void addMsx(String msx){
		synchronized(msxlist){
			PublicServer.msxlist.add(msx);
		}
	}
	public static Vector<String> getMsxlist() {
		synchronized(msxlist){
			return msxlist;
		}
		
	}

	public static void setMsxlist(Vector<String> msxlist) {
		PublicServer.msxlist = msxlist;
	}

}
