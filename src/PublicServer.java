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
		listaUtenti = new Vector<ChatUser>();
		msxlist = new Vector<String>();
		cmlist = new Vector<ChatMessage>();
		int counter = 0;
		try {
			ServerSocket server = new ServerSocket(4000);//creo socketserver in ascolto su porta 4000
			while(true){
				Socket s = server.accept(); //il socketserver va in ascolto
				
				SenderRunner chatthread = new SenderRunner(s,msxlist,counter,listaUtenti,cmlist);
				Thread t = new Thread(chatthread);
				t.start();
			}	
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Vector<String> msxlist;
	
	private static Vector<ChatUser> listaUtenti;
	private static Vector<ChatMessage> cmlist;
	public static int addUser(ChatUser cu,String ruolo){
			synchronized(listaUtenti){
			for(int i = 0; i < listaUtenti.size();i++){
				if(listaUtenti.get(i).isSender() && ruolo.equals("sender") && cu.getUsername().equals(listaUtenti.get(i).getUsername())){
					return -1;
				}
				if(listaUtenti.get(i).isReceiver() && ruolo.equals("receiver") && cu.getUsername().equals(listaUtenti.get(i).getUsername())){
					return -1;
				}
			}
			listaUtenti.add(cu);
			return 1;
		}
	}
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
