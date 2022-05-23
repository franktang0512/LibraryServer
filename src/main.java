import java.io.IOException;
import java.net.ServerSocket;


import org.json.*;

import Lib.view.LibServer;




public class main {
	
	public static void main(String[] args) {
		
		
//		String s="{\r\n"
//		+ "	\"cmd\":\"login\",\r\n"
//		+ "	\"account\":\"abc\",\r\n"
//		+ "	\"psd\":\"123\"	\r\n"
//		+ "}";
//		JSONObject ob = new JSONObject(s);	
//		
//        String cmd = ob.getString("cmd");

		ServerSocket libSocket = null;		
			try{
				System.out.println(args);
		        	//create server socket
				libSocket = new ServerSocket(10234);
				System.out.println("start server");
				try{ 
					while (true){
		                //wait accept
						LibServer connent=new LibServer(libSocket.accept());
						connent.start();	
					}
				} 
				catch(IOException e) { 
					System.err.println("Accept failed."); 
					System.exit(1); 
				} 
			} 
			catch (IOException e){ 
				System.err.println("Could not listen on port: 10234."); 
				System.exit(1); 
			} 
			finally{
				try{
					
					libSocket.close();
				}
				catch (IOException e)
				{ 
					System.err.println("Could not close port: 10234."); 
					System.exit(1); 
				}     
			}
	}

}
