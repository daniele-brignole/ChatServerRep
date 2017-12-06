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
		this.counter = counter;
		this.userlist = userlist;
		this.cmlist = cmlist;
		System.out.println(this.counter);
	}
			
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
		InputStream is = client.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader buffer= new BufferedReader(reader);
		
		OutputStream os = client.getOutputStream();
		OutputStreamWriter wr = new OutputStreamWriter(os);
		BufferedWriter outbuffer = new BufferedWriter(wr);
		
		String line = buffer.readLine();
		username = line;
		type = buffer.readLine();
		ChatUser user = new ChatUser(username,type);
		
		int response = addUser(user,type);
		if(response == 1) {
			synchronized(this.v){
				
				v.add("connesso");
				
				v.notifyAll();
			}
		}
		else if (response == -1){
			synchronized(this.v){
				
				v.add("errore");
				
				v.notifyAll();
			}
		}
		//System.out.println("Numero utenti connessi: "+ userlist.size());
		
		switch(type){
		case ("sender"):
			System.out.println(type + " " + username + " si è connesso");
			line = buffer.readLine();
			while(line!=null){//continuiamo a leggere finch� non si riceve dal client la parola quit
				
				if (line.equals("quit")){
					client.close();
					synchronized(this.userlist){
						for(int y = 0; y < userlist.size();y++){
							if (userlist.get(y).getUsername().equals(username)){
								userlist.remove(y);
							}
						}
					}
					System.out.println(username+ ": CONNECTION CLOSED");
					break;
				}
				
				else if (line.charAt(0)=='@'){
					String dest = "@"+line.substring(1, line.length());
					line = buffer.readLine();
					synchronized((Integer)this.counter){counter++;System.out.println(counter);}
					ChatMessage cm = new ChatMessage(dest,line,counter);
					synchronized(this.v){
						v.add(dest);
						v.add(cm.getMsx());
						v.notifyAll();
					}
				}
				
				else if(line.equals("/msxList")){
					String list = ""; 
					synchronized(this.cmlist){
						int u = cmlist.size()-10;
						if(u < 0) u = 0;
						for (int i = u; i<cmlist.size();i++){
							list = list + cmlist.get(i).getMsx()+" ";
						}
					}
					ChatMessage cm = new ChatMessage("@"+ username,list,counter);
					synchronized(this.v){
						v.add(cm.getDestination());
						v.add(cm.getMsx());
						v.notifyAll();
					}
					
				}
				
				else {
					System.out.println(line);
					synchronized(this.v){
						synchronized((Integer)this.counter){counter++;}
						ChatMessage cm = new ChatMessage(null,line,counter);
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
		case("receiver"):
			
			int i = this.counter;
			System.out.println(type + " " + username + " si è connesso");
			while(true){
				synchronized(this.v){
					if (i < v.size()){
					line = v.get(i);
					outbuffer.write(line);
					outbuffer.newLine();
					outbuffer.flush();
					i++;
					}
					else v.wait();
					}
				
				
				
			}
			//break;
		/*line = buffer.readLine();
		while(line!=null){//continuiamo a leggere finch� non si riceve dal client la parola quit
			System.out.println(line);
			if (line.equals("quit")){
				client.close();
				System.out.println(username+ ": CONNECTION CLOSED");
				break;
			}
			else{
				outbuffer.write(line);
				outbuffer.newLine();
				outbuffer.flush();
			}
			line = buffer.readLine();
		}*/
			
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
			}
			if (userlist.get(i).isSender() && cu.getUsername().equals(userlist.get(i).getUsername())) {
				userlist.get(i).setSender(true);
				System.out.println("utente "+ cu.getUsername()+ " attivo");
			}
		}
		userlist.add(cu);
		return 1;
	}
}

}