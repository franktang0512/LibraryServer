package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.sql.Date;

public class main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
//		Socket echoSocket=new Socket("140.122.184.121",10234);
		Socket echoSocket=new Socket("127.0.0.1",10234);
		PrintWriter out;
		BufferedReader in;
//		String sss="{\"cmd\":\"login\",\"account\":\"zzz123\",\"psd\":\"zzz123\"}";
//		String sss="{\"cmd\":\"lookupBook\",\"keyword\":\"lea\"}";
//		String sss="{\"cmd\":\"enroll\",\"name\":\"Flora\",\"sex\":2,\"birth\":\"1990-2-3\",\"email\":\"floara@ntnu.edu.tw\",\"address\":\"Taipei\","
//				+ "\"phone\":\"0900053322\",\"account\":\"florauser\",\"psd\":\"florahavefun\",\"kind\":2}";
		
//		String sss="{"+"\"cmd\":\"login\","+"\"account\":\"abc\","+"\"psd\":\"123\"}";
//		String sss="{"+"\"cmd\":\"returnBook\","+"\"book_id\":\"b00000000001\","+"\"account\":\"abc\"}";
//		String sss="{"+"\"cmd\":\"borrowBook\","+"\"book_id\":\"b00000000001\","+"\"account\":\"abc\"}";
//		String sss="{"+"\"cmd\":\"lookupBookHistory\","+"\"book_id\":\"b00000000001\"}";
//		String sss="{"+"\"cmd\":\"lookupUserHistory\","+"\"account\":\"abc\"}";
//		String sss="{"+"\"cmd\":\"modifyMember\","+"\"account\":\"abc\",\"name\":\"zzz\",\"sex\":1,\"birth\":\"2008-11-01\",\"email\":\"abc@gmail.com\",\"address\":\"abc\","
//				+ "\"phone\":\"0912345678\",\"psd\":\"zzz123\"}";
		
		String sss="{"+"\"cmd\":\"modifyMember\",\"account\":\"zzz123\","+"\"name\":\"zzz\",\"sex\":1,\"birth\":\"2008-11-01\",\"email\":\"abc@gmail.com\",\"address\":\"abc\","
				+ "\"phone\":\"0912345678\",\"psd\":\"zzz123\"}";
		
		
		
		
		out = new PrintWriter(echoSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));		
		out.println(sss);
		String s1 = in.readLine();
		System.out.println(s1);
	    


	}

}
