package Lib.view;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;

import org.json.*;

import Lib.control.Controller;

//import com.alibaba.fastjson.JSONObject;



public class LibServer extends Thread{
	Socket clientSocket;
	Controller control;
	PrintWriter out;
	BufferedReader in;
	
	public LibServer(Socket clientSoc){
		clientSocket = clientSoc;
		control = new Controller();
	}	
	public void run(){
		try {			
	    	out = new PrintWriter(clientSocket.getOutputStream(),true);
	    	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	while(true) {
	    		String inputLine=in.readLine();
	    		if(inputLine!=null) {
	    			String respond=control.commandHandle(inputLine);
	    			out.println(respond);
	    		}else {
        			System.out.println("連線結束");
        			out.close();
	    	    	in.close();
	    	    	clientSocket.close();
	    	    	break;
	    		}	    		
	    	}	    	
	    }
		catch(Exception e) {}


		
//		while(true) {
//			try {

//	        	String inputLine=in.readLine();
//				//inputLine ="{"+"\"cmd\":\"login\","+"\"account\":\"abc\","+"\"psd\":\"123\"}";    	
//	        	System.out.println("cmd:"+inputLine);
//	        	System.out.println("==================");
//	        	if(inputLine!=null) {
//		        	JSONObject object = new JSONObject(inputLine);
//	    	        String cmd = object.getString("cmd");
//	    	        String account =  object.getString("account");
//	    	        String psd =  object.getString("psd");
//	    			String respond = "";
//	    	        if(cmd.equals("login")&&account.equals("abc")&&psd.equals("123")) {
//	    	        	respond = "{\r\n"
//	    	        			+ "	\"status\":\"successful\"\r\n"
//	    	        			+ "}";
//	    	        }else if(cmd.equals("login")){
//	    	        	respond = "{\r\n"
//	    	        			+ "	\"status\":\"failure\"\r\n"
//	    	        			+ "}";
//	    	        }else {
//	    	        	respond = "{\r\n"
//	    	        			+ "	\"status\":\"Not yet!!!!!1\"\r\n"
//	    	        			+ "}";        	        	
//	    	        	
//	    	        }        			
//	    			out.println(respond);
//	        	}else{
//	    			System.out.println("連線結束");
//	    			out.close();
//	    	    	in.close();
//	    	    	clientSocket.close();
//	    	    	break;
//	    		}        	
//	        	
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//
//			
//		}
//		
		
		
		
//		try {        	
//        	while(true){
//
//        		
//        		if(inputLine!=null){
//
//	            }
//        		else{
//        			System.out.println("連線結束");
//        			out.close();
//	    	    	in.close();
//	    	    	clientSocket.close();
//	    	    	break;
//        		}
//
//        	}
//        	
//        } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
