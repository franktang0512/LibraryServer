package Lib.control;

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
		
        String account =  object.getString("account");
        String psd =  object.getString("psd");
        
       
        
        
        
        if(account.equals("abc")&&psd.equals("123")) {
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kine\":1}");
        }else if(account.equals("admin")&&psd.equals("libadmin")){
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kine\":2}");
        }else {
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
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kine\":"+user.getKind()+"}");
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
