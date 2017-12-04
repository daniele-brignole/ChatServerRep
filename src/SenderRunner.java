import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SenderRunner implements Runnable {
	private Socket client = null;
	String username = "";
	String type = "";
	public SenderRunner(Socket client){
		this.client = client;
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
					
					PublicServer.addMsx(line);;
					//System.out.println(PublicServer.getMsxlist().size());
				}
				line = buffer.readLine();
			}
			
				break;
		case("receiver"):
			System.out.println(type + " " + username + " si è connesso");
		line = buffer.readLine();
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
		}
			break;
		}
	}catch (Exception e){
		e.printStackTrace();
	}
		
		
	}

}