import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SenderRunner implements Runnable{

	public SenderRunner(InputStream is){
		this.is = is;
	}
	@Override
	public void run() {
		try{
			while(true){
				InputStreamReader reader = new InputStreamReader(is);				
				BufferedReader buffer = new BufferedReader(reader);	
				String line = buffer.readLine();
				while(line != null) {
					if(line.equals("quit")) {
						//s.close();
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
	private InputStream is;
}
