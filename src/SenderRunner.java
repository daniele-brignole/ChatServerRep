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
	private int last = 0; 
	String username = "";
	String type = "";
	public SenderRunner(Socket client,Vector<String> v, int last){
		this.client = client;
		this.v = v;
		this.last = last;
		System.out.println(this.last);
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
		
		switch(type){
		case ("sender"):
			System.out.println(type + " " + username + " si è connesso");
			line = buffer.readLine();
			while(line!=null){//continuiamo a leggere finch� non si riceve dal client la parola quit
				if (line.equals("quit")){
					client.close();
					System.out.println(username+ ": CONNECTION CLOSED");
					break;
				}
				else if(line.equals("/msxList")){
					for(int i = 0; i< PublicServer.getMsxlist().size();i++){
						System.out.println(PublicServer.getMsxlist().get(i));
					}
					
				}
				else {
					System.out.println(line);
					synchronized(this.v){
						
						v.add(line);
						
						v.notifyAll();
					}
					synchronized((Integer)this.last){this.last = (v.size()-1);}
					//PublicServer.addMsx(line);
					//PublicServer.getMsxlist().notifyAll();
					//System.out.println(PublicServer.getMsxlist().size());
				}
				line = buffer.readLine();
			}
			
				break;
		case("receiver"):
			
			int i = this.last;
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

}