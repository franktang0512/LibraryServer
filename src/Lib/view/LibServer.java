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
        			System.out.println("³s½uµ²§ô");
        			out.close();
	    	    	in.close();
	    	    	clientSocket.close();
	    	    	break;
	    		}	    		
	    	}	    	
	    }
		catch(Exception e) {}
	}
}
