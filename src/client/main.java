package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
//		Socket echoSocket=new Socket("140.122.184.121",10234);
		Socket echoSocket=new Socket("127.0.0.1",10234);
		PrintWriter out;
		BufferedReader in;
		String sss="{\"cmd\":\"login\",\"account\":\"libadmin\",\"psd\":\"libadmin\"}";
		
		//String s="{"+"\"cmd\":\"login\","+"\"account\":\"abc\","+"\"psd\":\"123\"}";
		out = new PrintWriter(echoSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));		
		out.println(sss);
		String s1 = in.readLine();
		System.out.println(s1);
	    


	}

}
