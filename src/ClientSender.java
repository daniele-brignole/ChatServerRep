import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;


public class ClientSender {
	private static int port = 4000;
	
	public static void main(String[] args) throws IOException {
		//String addr = "127.0.0.1";
		String username;
		Socket s = new Socket();
		InetSocketAddress ISaddr = new InetSocketAddress(args[0], port);
		stamp("Benvenuto, inserisci il tuo username per effettuare il login");
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(reader);
		username = buffer.readLine();
		try{
			s.connect(ISaddr);
			
			OutputStream os = s.getOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(os);
			BufferedWriter outbuffer = new BufferedWriter(wr);
			
			outbuffer.write(username);
			outbuffer.newLine();
			outbuffer.flush();
			
			outbuffer.write("sender");
			outbuffer.newLine();
			outbuffer.flush();
			
			InputStream is = s.getInputStream();
			InputStreamReader rd = new  InputStreamReader(is);
			BufferedReader inbuffer = new BufferedReader(rd);
			String ok = inbuffer.readLine();
			
			if(ok.equals("0")){
				stamp("Connessione riuscita");
				stamp("Benvenuto " + username + ", hai effettuato l'accesso al servizio di scrittura");
				stamp("Scrivi e premi invio per inviare messaggi agli altri utenti connessi");
				stamp("Digita /commands per mostrare i comandi disponibili");
			}
			else if(ok.equals("-1")){
				stamp("Errore: utente gia connesso");
				return;
			}
			
			String line = "";
			while(true) {
				if((line = buffer.readLine()).equals("")){
					System.out.println("Errore di digitazione, si prega di inserire un testo valido");
				}
				else{
					if(line.equals("/command")){
						stamp("/ml:  mostra lista ultimi messaggi disponibili");
						stamp("/ul:  mostra lista utenti connessi");
						stamp("/quit:  effettua il logout");
						stamp("@nomeutente:  invia un messaggio privato all'utente scelto");
					}
					else{
						outbuffer.write(line);
						outbuffer.newLine();
						outbuffer.flush();
						if(line.equals("/quit")) {
							break;
						}
					}

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
