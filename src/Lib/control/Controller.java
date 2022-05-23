package Lib.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import Lib.model.LibModel;
import Lib.model.data.User;

public class Controller {
	LibModel libmodel;
	
	
	public Controller(){
		
		libmodel = LibModel.getInstance();
	}
	
	
	public JSONObject enroll(JSONObject object) {
		JSONObject responseJson=null;
		try {
	        User user = new User();
	        user.setName(object.getString("name"));
	        user.setAccount(object.getString("account"));
	        user.setPassword(object.getString("psd"));
	        user.setSex(object.getInt("sex"));
	        user.setKind(object.getInt("kind"));
	        //處理java util sql date差異問題 這裡統一用sql的date
	        SimpleDateFormat formatter1=new SimpleDateFormat("dd-MMM-yyyy");
	        Date bd=formatter1.parse(object.getString("date"));
	        long timeInMilliSeconds = bd.getTime();
	        java.sql.Date date = new java.sql.Date(timeInMilliSeconds); 
	        
	        
	        user.setBirthday(date);
	        user.setEmail(object.getString("email"));
	        user.setPhone(object.getString("phone"));
	        user.setAddress(object.getString("address"));

	        libmodel.addUser(user);
	        
	        
	        responseJson = new JSONObject("{\"status\":\"successful\"}");
			
		}
		catch(Exception e) {
			responseJson = new JSONObject("{\"status\":\"fail\"}");			
		}
		return responseJson;
		
	}
	
	public JSONObject  login(JSONObject object) {
		JSONObject responseJson=null;
		
        String account =  object.getString("account");
        String psd =  object.getString("psd");
        
        
        User user = libmodel.getUser(account);
               
        if(user==null) {
        	responseJson = new JSONObject("{\"status\":\"fail\"}");
        }else if(account.equals(user.getAccount())&&psd.equals(user.getPassword())){        	
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kind\":"+user.getKind()+"}");
        }
        return responseJson;
		
	}
	
	
	
	public String commandHandle(String inputLine) {
		System.out.println("the requeset:"+inputLine);
		
		JSONObject object = new JSONObject(inputLine);	    			
		String cmd = object.getString("cmd");
		String respond="";
		
		switch(cmd) {
			case "login":
				
				respond=login(object).toString();
				
				break;
			case "enroll":
				respond=enroll(object).toString();
				break;
			//TODO:查詢圖書（可能是查書名或作者）、借閱紀錄查詢
			case "borrowBook":
				respond=enroll(object).toString();
				break;
			case "returnBook":
				respond=enroll(object).toString();
				break;
			case "lookupByBook":
				respond=enroll(object).toString();
				break;
			case "lookupByAuthor":
				respond=enroll(object).toString();
				break;
			case "lookupBookHistory":
				respond=enroll(object).toString();
				break;
			case "lookupUserHistory":
				respond=enroll(object).toString();
				break;
			default:
				respond = "{\"status\":\"Not Yet\"}";
		
		}
		return respond;
	}
//	public Response commandHandle(String s) {}
}
