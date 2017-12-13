import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class SenderRunner implements Runnable {
	private Socket client = null;
	private Vector<String> v = new Vector<String>();
	private Vector<ChatUser> userlist = new Vector<ChatUser>();
	private Vector<ChatMessage> cmlist = new Vector<ChatMessage>();
	private int counter; 
	String username = "";
	String type = "";
	public SenderRunner(Socket client,Vector<String> v, int counter,Vector<ChatUser> userlist,Vector<ChatMessage> cmlist){
		this.client = client;
		this.v = v;
		this.counter = v.size();
		this.userlist = userlist;
		this.cmlist = cmlist;
		System.out.println("counter: "+this.counter);
	}
			
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			//crea canali input/output
		InputStream is = client.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader buffer= new BufferedReader(reader);
		
		OutputStream os = client.getOutputStream();
		OutputStreamWriter wr = new OutputStreamWriter(os);
		BufferedWriter outbuffer = new BufferedWriter(wr);
		//inizializza username e tipo di thread di client (sender o receiver)
		String line = buffer.readLine();
		username = line;
		type = buffer.readLine();
		//crea un nuovo chatUser, controlla la lista degli utenti  
		//e invia la risposta al client (login corretto/errore)
		ChatUser user = new ChatUser(username,type);
		int response = addUser(user,type);
		
		outbuffer.write(response+"");
		outbuffer.newLine();
		outbuffer.flush();
		
		//controlla se il thread da creare � per i sender o per i receiver
		switch(type){
		//thread per i client sender
		case ("sender"):
			System.out.println(type + " " + username + " si è connesso");
			try{
				line = buffer.readLine();
			}
			catch(StringIndexOutOfBoundsException e){
				line = buffer.readLine();
				e.printStackTrace();
			}
			while(line!=null){//continuiamo a leggere finch� non si riceve dal client la parola quit
				//logout
				if (line.equals("/quit")){
					client.close();
					synchronized(this.userlist){
						for(int y = 0; y < userlist.size();y++){
							if (userlist.get(y).getUsername().equals(username)){
								userlist.remove(y);
							}
						}
					}
					synchronized(this.v){
						v.add("@"+username);
						v.add("/quit");
						v.notifyAll();
					}
					System.out.println(username+ ": CONNECTION CLOSED");
					break;
				}
				//mostra lista utenti
				else if (line.equals("/ul")){
					String list = "";
					for (int i = 0; i < userlist.size();i++){
						list = list + userlist.get(i).getUsername()+ " ";
					}
					
					synchronized(this.v){
						v.add("@"+ username);
						v.add(list);
						v.notifyAll();
					}
				}
				//invio di messaggio privato
				else if (line.charAt(0)=='@'){
					String dest = "@"+line.substring(1, line.length());
					line = "[p] " + buffer.readLine();
					synchronized((Integer)this.counter){counter++;System.out.println(counter);}
					ChatMessage cm = new ChatMessage(dest,username + ": " + line,counter);
					synchronized(this.v){
						v.add(dest);
						v.add(cm.getMsx());
						v.notifyAll();
					}
					synchronized(this.cmlist){
						cmlist.add(cm);
					}
				}
				//mostra ultimi dieci messaggi
				else if(line.equals("/ml")){
					String list = ""; 
					synchronized(this.cmlist){
						int u = cmlist.size()-10;
						if(u < 0) u = 0;
						while(u < cmlist.size()){
							String dest = cmlist.get(u).getDestination();
							System.out.println("dest: "+dest);
							if(dest.equals("@all")||dest.equals("@"+this.username)){
								list = list + cmlist.get(u).getMsx();
								u++;
							}
							else{
								u++;
							}
						}
					}
					ChatMessage cm = new ChatMessage("@"+ username,list,counter);
					synchronized(this.v){
						v.add(cm.getDestination());
						v.add(cm.getMsx());
						v.notifyAll();
					}
					
				}
				//invio messaggio globale
				else {
					System.out.println(line);
					synchronized(this.v){
						synchronized((Integer)this.counter){counter++;}
						ChatMessage cm = new ChatMessage("@all",username + ": "+line,counter);
						v.add("@all");
						
						v.add(cm.getMsx());
						
						v.notifyAll();
						synchronized(this.cmlist){
							cmlist.add(cm);
						}
					}
					
					
					//PublicServer.addMsx(line);
					//PublicServer.getMsxlist().notifyAll();
					//System.out.println(PublicServer.getMsxlist().size());
				}
				line = buffer.readLine();
			}
			
				break;
		//thread per i client receiver
		case("receiver"):
			
			int i = this.counter -2;
			if(i <0) i = 0;
			System.out.println(type + " " + username + " si è connesso");
			while(true){
				//legge dal vettore buffer tutti i messaggi fino in fondo, 
				//se l'indicatore � in fondo si mette in attesa sul vettore 
				//fino a quando un nuovo messagio non viene inviato.
				synchronized(this.v){
					System.out.println(i);
					if (i < v.size()){
					line = v.get(i);
					outbuffer.write(line);
					outbuffer.newLine();
					outbuffer.flush();
					i++;
					//logout automatico del receiver
					if(line.equals("/quit") && v.get(i-1).equals("@"+this.username)){
						return;
					}
					}
					else v.wait();
					}
				
				
				
			}			
		}
	}catch (Exception e){
		e.printStackTrace();
	}
		
		
	}
	public int addUser(ChatUser cu,String ruolo){
		synchronized(userlist){
		for(int i = 0; i < userlist.size();i++){
			if(userlist.get(i).isSender() && ruolo.equals("sender") && cu.getUsername().equals(userlist.get(i).getUsername())){
				return -1;
			}
			if(userlist.get(i).isReceiver() && ruolo.equals("receiver") && cu.getUsername().equals(userlist.get(i).getUsername())){
				return -1;
			}
			if (userlist.get(i).isSender() && cu.getUsername().equals(userlist.get(i).getUsername())) {
				userlist.get(i).setReceiver(true);
				System.out.println("utente "+ cu.getUsername()+ " attivo");
				return 0;
			}
			if (userlist.get(i).isReceiver() && cu.getUsername().equals(userlist.get(i).getUsername())) {
				userlist.get(i).setSender(true);
				System.out.println("utente "+ cu.getUsername()+ " attivo");
				return 0;
			}
		}
		userlist.add(cu);
		System.out.println("autenticazione concessa");
		return 0;
	}
}

}