import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientSender {
	private static int port = 4000;
	
	public static void main(String[] args) throws IOException {
		String addr = "127.0.0.1";
		String username;
		stamp("Benvenuto, inserisci il tuo username per effettuare il login");
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(reader);
		username = buffer.readLine();
		stamp("Benvenuto " + username + ", hai effettuato l'accesso al servizio di scrittura");
		stamp("Digita /commands per mostrare i comandi disponibili");
	}
	
	public static void stamp(String s){
		System.out.println(s);
	}
	
	
}
