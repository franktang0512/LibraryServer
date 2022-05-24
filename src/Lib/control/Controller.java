package Lib.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import Lib.model.LibModel;
import Lib.model.data.Book;
import Lib.model.data.History;
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
	        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
	        Date bd=formatter1.parse(object.getString("birth"));
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
			System.out.print(e.toString());
		}
		return responseJson;
		
	}
	
	public JSONObject  login(JSONObject object) {
		JSONObject responseJson=null;
		
        String account =  object.getString("account");
        String psd =  object.getString("psd");
        
        
        User user = libmodel.getUserByAcc(account);
               
        if(user==null) {
        	responseJson = new JSONObject("{\"status\":\"fail\"}");
        }else if(account.equals(user.getAccount())&&psd.equals(user.getPassword())){        	
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kind\":"+user.getKind()+"}");
        }
        return responseJson;
		
	}
	
	public JSONObject lookupBook(JSONObject object) {
		
		JSONObject responseJson=null;
		String booksstring="";
		String keyword =  object.getString("keyword");
		ArrayList<Book> books = (ArrayList<Book>) libmodel.getSearchBooks(keyword);	
		
		if(books==null) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"Sorry We don't have the book you are looking for\"}");
		}
		
		for(int i =0;i<books.size();i++) {
			
			booksstring+="{\"bookname\":\""+books.get(i).getName()+"\"}";
			if(!(i==books.size()-1)) {
				booksstring+=",";
			}			
		}
//		System.out.println("booksstring:"+booksstring);
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
		return responseJson;
	}
	
	public JSONObject borrowBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		User u =libmodel.getUserByAcc(account);
		java.util.Date now = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(now.getTime());
		History history = new History(null,u.getId(),book_id,sqlDate,null);
		libmodel.takeOutBook(history);
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	public JSONObject returnBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		User u =libmodel.getUserByAcc(account);
		Book b = new Book();
		b.setID(book_id);
		History h = libmodel.getHistroy(u, b);
		//TODO:search for the null end date and 
		
		java.util.Date now = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(now.getTime());
		h.setReturnDay(sqlDate);
		libmodel.putBackBook(h);
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	
//	public JSONObject returnBook(JSONObject object) {
//		JSONObject responseJson=null;
//		String book_id =  object.getString("book_id");
//		String account =  object.getString("account");
//		History history = new History();
//		
//		
//		return responseJson;
//	}
	//TODO:借閱紀錄查詢
	public JSONObject lookupHistoryByBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		Book b = new Book();
		b.setID(book_id);
		libmodel.getBookHistroy(b);

		
		
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
				respond=borrowBook(object).toString();
				break;
			case "returnBook":
				respond=returnBook(object).toString();
				break;
			case "lookupBook":
				respond=lookupBook(object).toString();
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
