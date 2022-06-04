package Lib.control;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;

import Lib.model.LibModel;
import Lib.model.data.Book;
import Lib.model.data.History;
import Lib.model.data.Recommend;
import Lib.model.data.Reservation;
import Lib.model.data.User;

public class Controller {
	LibModel libmodel;
	
	
	public Controller(){
		
		libmodel = LibModel.getInstance();
	}
	
	//TODO:登入註冊
	public JSONObject enroll(JSONObject object) {
		JSONObject responseJson=null;
		try {
	        User existed_user = libmodel.getUserByAcc(object.getString("account"));
	        if(existed_user==null) {
		        User user = new User();
		        user.setName(object.getString("name"));
		        user.setAccount(object.getString("account"));
		        user.setPassword(object.getString("psd"));
		        user.setSex(object.getInt("sex"));
		        user.setKind(1);
		        //處理java util sql date差異問題 這裡統一用sql的date
		        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
		        java.util.Date bd=formatter1.parse(object.getString("birth"));
		        long timeInMilliSeconds = bd.getTime();
		        Date date = new Date(timeInMilliSeconds); 
		        
		        
		        user.setBirthday(date);
		        user.setEmail(object.getString("email"));
		        user.setPhone(object.getString("phone"));
		        user.setAddress(object.getString("address"));
		        libmodel.addUser(user);
		        responseJson = new JSONObject("{\"status\":\"successful\"}");
	        }else {
	        	responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"This account is existed.\"}");
	        }	        
			
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
        	responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"No such a user.\"}");
        }else if(account.equals(user.getAccount())&&psd.equals(user.getPassword())&&user.getEnable()==1){        	
        	responseJson = new JSONObject("{\"status\":\"successful\",\"kind\":"+user.getKind()+"}");
        }else if(user.getEnable()==0){
        	responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"you have been disabled, please contact library administrator.\"}");
        }else {
        	responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"account or password error.\"}");
        }
        return responseJson;
		
	}
	
	//TODO:讀者借閱
	public JSONObject borrowBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		
		User u =libmodel.getUserByAcc(account);
		if(u==null) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"not a member\"}");
			return responseJson;
		}
		Book b = libmodel.getBookByID(book_id);
		if(b.getAmount()==0) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"no stock, refresh the page to see more\"}");
			return responseJson;
		}
		ArrayList<Reservation> r = (ArrayList<Reservation>) libmodel.getReserveByBook(b);
	
		for(int i =0;i<r.size();i++) {
			//排隊中的人序號小於書量就可以借閱
			if(r.get(i).getUid().equals(u.getId())&&i+1<=b.getAmount()) {
				//結束預約狀態，進入借書程序
				r.get(i).setIsFinished(1);
				break;
			}else if(r.get(i).getUid().equals(u.getId())&&i+1>b.getAmount()) {
				responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"you have to wait for the order\"}");
				return responseJson;
			}
			//排隊預約的人>=現有書量 就無法借閱
			if(i==r.size()-1&&r.size()>=b.getAmount()) {
				responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"someone is reserving\"}");
				return responseJson;
			}			
		}
		
		long now = System.currentTimeMillis();
		Date sqlDate = new Date(now);
		History history = new History();
		history.setBid(book_id);
//		System.out.println(history.getBid());
		history.setUid(u.getId());
//		System.out.println(history.getUid());
		history.setBorrowDay(sqlDate);
//		System.out.println(history.getBorrowDay());
		libmodel.takeOutBook(history);

		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	public JSONObject returnBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");		
		User u =libmodel.getUserByAcc(account);
		if(u==null) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"not a member\"}");
			return responseJson;
		}
		
		Book b = new Book();
		b.setID(book_id);
		History h = libmodel.getHistroy(u, b);
		
		long now = System.currentTimeMillis();
//		System.out.println(now);
		Date sqlDate = new Date(now);
//		System.out.println(sqlDate);
		h.setReturnDay(sqlDate);
//		System.out.println("h.getReturnDay());
		libmodel.putBackBook(h);
		
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	//TODO:書籍的曾查刪修
	public JSONObject insertBook(JSONObject object) {
		
		JSONObject responseJson=null;
		String book_name =  object.getString("book_name");
		String author =  object.getString("author");
		int publishedYear =  object.getInt("publishedYear");
		String publisher =  object.getString("publisher");
		String isbn =  object.getString("isbn");
		int quantity_add =  object.getInt("quantity_add");
		Book b = new Book();
		b.setName(book_name);
		b.setAuthor(author);
		b.setPublishYear(publishedYear);
		b.setPublisher(publisher);
		b.setIsbn(isbn);
		libmodel.addBook(b, quantity_add);
			
	
		responseJson= new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
		
	
	
	public JSONObject disableMember(JSONObject object) {
		JSONObject responseJson=null;
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		u.setEnable(0);
		libmodel.updateUser(u);
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	public JSONObject deleteBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		Book b = libmodel.getBookByID(book_id);
		b.setAmount(0);
		libmodel.updateBook(b);

		responseJson= new JSONObject("{\"status\":\"successful\"}");
		
		return responseJson;
	}
		
	public JSONObject modifyBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String book_name =  object.getString("book_name");
		String author =  object.getString("author");
		int publishedYear =  object.getInt("publishedYear");
		String publisher =  object.getString("publisher");
		String isbn =  object.getString("isbn");
		int quantity_add =  object.getInt("quantity_add");
		Book b = libmodel.getBookByID(book_id);;
		b.setName(book_name);
		b.setAuthor(author);
		b.setPublishYear(publishedYear);
		b.setPublisher(publisher);
		b.setIsbn(isbn);
		libmodel.updateBook(b);

		responseJson= new JSONObject("{\"status\":\"successful\"}");
		
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
			
			booksstring+="{\"book_id\":\""+books.get(i).getID()+"\",\"book_name\":\""+books.get(i).getName()+"\","
					+ "\"author\":\""+books.get(i).getAuthor()+"\",\"publishedYear\":"+books.get(i).getPublishYear()+","
							+ "\"publisher\":\""+books.get(i).getPublisher()+"\",\"isbn\":\""+books.get(i).getIsbn()+"\","
									+ "\"quantity\":"+books.get(i).getAmount()+"}";
			if(!(i==books.size()-1)) {
				booksstring+=",";
			}			
		}
		
//		System.out.println("booksstring:"+booksstring);
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
//		System.out.println(responseJson.toString());
		return responseJson;
	}

	public JSONObject modifyMember(JSONObject object) {
		JSONObject responseJson=null;
//		System.out.println("===========0=============");
		String acc =  object.getString("account");//?
//		System.out.println("===========1=============");
		User u = libmodel.getUserByAcc(acc);
//		System.out.println("===========2=============");
		try {			
			String name =  object.getString("name");
			u.setName(name);
		}catch(Exception e) {}
//		System.out.println("===========3=============");
		try {			
			int sex =  object.getInt("sex");
			u.setSex(sex);

		}catch(Exception e) {}
//		System.out.println("===========4=============");
		try {			
			String birth =  object.getString("birth");
			try {
		        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
		        java.util.Date bd=null;
				bd = formatter1.parse(birth);
		        long timeInMilliSeconds = bd.getTime();
		        Date date = new Date(timeInMilliSeconds); 
				u.setBirthday(date);
			} catch (ParseException e) {
				responseJson= new JSONObject("{\"status\":\"fail\"}");
			}
		}catch(Exception e) {}
//		System.out.println("===========5=============");
		try {			
			String email =  object.getString("email");
			u.setEmail(email);
		}catch(Exception e) {}
//		System.out.println("===========6=============");
		try {			
			String address =  object.getString("address");
			u.setAddress(address);
		}catch(Exception e) {}
//		System.out.println("===========7=============");
		try {			
			String phone =  object.getString("phone");
			u.setPhone(phone);
		}catch(Exception e) {}
//		System.out.println("===========8=============");
		try {			
			int kind =  object.getInt("kind");//?
			u.setKind(kind);
		}catch(Exception e) {}
//		System.out.println("===========9=============");
		try {			
			int enable =  object.getInt("enable");//?
			u.setEnable(enable);

		}catch(Exception e) {}
//		System.out.println("===========10=============");
		try {			
			String psd =  object.getString("psd");
			u.setPassword(psd);
		}catch(Exception e) {}
//		System.out.println("===========11=============");
		
		libmodel.updateUser(u);
		

		responseJson= new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
		
	}
	
	public JSONObject insertMember(JSONObject object) {
		JSONObject responseJson=null;
		try {
	        User user = new User();
	        user.setName(object.getString("name"));
	        user.setAccount(object.getString("account"));
	        user.setPassword((object.getString("psd")==null?object.getString("account"):object.getString("psd")));
	        user.setSex(object.getInt("sex"));
	        user.setKind(1);
	        //處理java util sql date差異問題 這裡統一用sql的date
	        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date bd=formatter1.parse(object.getString("birth"));
	        long timeInMilliSeconds = bd.getTime();
	        Date date = new Date(timeInMilliSeconds); 
	        
	        
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
	//TODO:借閱紀錄查詢
	public JSONObject lookupBookHistory(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		Book b = new Book();
		b.setID(book_id);
//		System.out.println("========0=======");
		ArrayList<History> hislist = (ArrayList<History>) libmodel.getBookHistroy(b);
//		System.out.println("========1=======");
		//TODO:寫成JSON
		if(hislist.size()==0) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"none of the record\"}");
		}
//		System.out.println("========2=======");
		String s ="";
		for(int i =0;i<hislist.size();i++) {
			History h =  hislist.get(i);
			String uid = h.getUid();
//			System.out.println("========3=======");
			User u = libmodel.getUserByID(uid);
//			System.out.println("========4=======");
			String username = u.getName();
			String useracc = u.getAccount();
			Date borrowdate =h.getBorrowDay();
			Date returndate = h.getReturnDay();
			
			s+="{\"account\":\""+useracc+"\",\"name\":\""+username+"\",\"borrow_date\":\""+(borrowdate!=null?borrowdate.toString():null)+"\",\"return_date\":\""
			+(returndate!=null?returndate.toString():null)+"\"}";
			if(i!=hislist.size()-1) {
				s+=",";
			}
//			System.out.println("========6=======");
		}
//		System.out.println("========7=======");

		String hisstring="{\"status\":\"successful\",\"histories\":["+s+"]}";
		responseJson = new JSONObject(hisstring);
		
		return responseJson;
	}
	public JSONObject lookupUserHistory(JSONObject object) {
		JSONObject responseJson=null;
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		ArrayList<History> hislist = (ArrayList<History>) libmodel.getUserHistroy(u);
		//TODO:寫成JSON
		
//		System.out.println("========1=======");
		//TODO:寫成JSON
		if(hislist.size()==0) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"none of the record\"}");
		}
//		System.out.println("========2=======");
		String s ="";
		for(int i =0;i<hislist.size();i++) {
			History h =  hislist.get(i);
			String bid = h.getBid();
//			System.out.println("========3=======");
			Book b = libmodel.getBookByID(bid);
//			System.out.println("========4=======");
			String username = u.getName();
			String useracc = u.getAccount();
			Date borrowdate =h.getBorrowDay();
			Date returndate = h.getReturnDay();
			
			s+="{\"book_id\":\""+b.getID()+"\",\"book_name\":\""+b.getName()+"\",\"borrow_date\":\""+(borrowdate!=null?borrowdate.toString():null)+"\",\"return_date\":\""
			+(returndate!=null?returndate.toString():null)+"\"}";
			if(i!=hislist.size()-1) {
				s+=",";
			}
//			System.out.println("========6=======");
		}
//		System.out.println("========7=======");

		String hisstring="{\"status\":\"successful\",\"histories\":["+s+"]}";
		responseJson = new JSONObject(hisstring);
		
		return responseJson;

	}
	
	
	public JSONObject lookupMember(JSONObject object) {
		JSONObject responseJson=null;
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		String s ="{\"status\":\"successful\","
				+ "	\"name\":\""+u.getName()+"\","
				+ "	\"sex\":\""+u.getSex()+"\","
				+ "	\"birth\":\""+u.getBirthday()+"\","
				+ "	\"email\":\""+u.getEmail()+"\","
				+ "	\"address\":\""+u.getAddress()+"\","
				+ "	\"phone\":\""+u.getPhone()+"\","
				+ "	\"account\":\""+u.getAccount()+"\",}";
		
		responseJson = new JSONObject(s);
		return responseJson;
	}
	
	public JSONObject forget(JSONObject object) {
		JSONObject responseJson=null;
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		u.setPassword(account);
		libmodel.updateUser(u);
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	//TODO:
	public JSONObject reserve(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		Book b = libmodel.getBookByID(book_id);
		Reservation having_r = libmodel.getReserveByBookUser(b, u);
		if(having_r!=null) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"you have a same book reserved already\"}");
			return responseJson;
		}
		Reservation reserve = new Reservation();
		reserve.setBid(b.getID());
		reserve.setUid(u.getId());
		long now = System.currentTimeMillis();
		Date sqlDate = new Date(now);
		reserve.setReserveDay(sqlDate);
		reserve.setIsFinished(0);
		libmodel.addReserve(reserve);
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////	
	public JSONObject cancelReserve(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		Book b = libmodel.getBookByID(book_id);
		
		Reservation reserve = libmodel.getReserveByBookUser(b, u);
		reserve.setIsFinished(1);
		libmodel.setReserve(reserve);
		
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	public JSONObject lookUpUserReserveBook(JSONObject object) {
		JSONObject responseJson=null;
		System.out.println("===================0");
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		System.out.println("===================1");
		ArrayList<Reservation> userreserve = (ArrayList<Reservation>)libmodel.getReserveByUser(u);
		System.out.println("===================2");
		if(userreserve==null) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"no reservation so far\"}");
			return responseJson;			
		}
		System.out.println("===================3");
		ArrayList<Book> b = new ArrayList<Book>();
		for(Reservation r:userreserve) {
			b.add(libmodel.getBookByID(r.getBid()));
		}
		System.out.println("===================4");
		String booksstring = "";
		for(int i =0;i<b.size();i++) {
			
			booksstring+="{\"book_id\":\""+b.get(i).getID()+"\",\"book_name\":\""+b.get(i).getName()+"\","
					+ "\"author\":\""+b.get(i).getAuthor()+"\",\"publishedYear\":"+b.get(i).getPublishYear()+","
							+ "\"publisher\":\""+b.get(i).getPublisher()+"\",\"isbn\":\""+b.get(i).getIsbn()+"\","
									+ "\"quantity\":"+b.get(i).getAmount()+"}";
			if(!(i==b.size()-1)) {
				booksstring+=",";
			}			
		}
		System.out.println("===================5");
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
//		System.out.println(responseJson.toString());
		return responseJson;
	}
	
	
	
	public JSONObject bookShouldReturnDay(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		Book b = libmodel.getBookByID(book_id);
		ArrayList<History> bookhis =(ArrayList<History>) libmodel.getBookHistroy(b);		
		Iterator itr = bookhis.iterator();
        while (itr.hasNext()) {
        	  
            // Remove elements smaller than 10 using
            // Iterator.remove()
        	History h = (History) itr.next();
//            int x = (Integer)itr.next();
            if (h.getReturnDay()!=null) {
            	itr.remove();
            }
                
        }
        Date d=null;
		long now = System.currentTimeMillis();
		d = new Date(now);
        for(History h : bookhis) {
        	Date borrowday = h.getBorrowDay();

        	if(borrowday.before(d)) {
        		d=borrowday;
        	}
        }
        
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 30);
        d = new Date(c.getTimeInMillis());
		
        responseJson = new JSONObject("{\"status\":\"successful\",\"date\":\""+d+"\"}");		
		return responseJson;
	}
	public JSONObject recommendBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_name =  object.getString("book_name");
		String account =  object.getString("account");
		if(libmodel.isSameRecommend(book_name)) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"someone has already recommended, contact lib admin to check detail if other books(more than 1) has same name of what you recommended \"}");
			return responseJson;
		}
		User u = libmodel.getUserByAcc(account);
		
		Recommend recommend = new Recommend();
		recommend.setUserID(u.getId());
		recommend.setBookInfo(book_name);
		recommend.setCensored(2);
		libmodel.addRecommend(recommend);
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;
	}
	public JSONObject lookUpRecommendBook(JSONObject object) {
		JSONObject responseJson=null;
		ArrayList<Recommend> rec = (ArrayList<Recommend>) libmodel.getNotCensoredRecommend();
		if(rec==null) {			
			return new JSONObject("{\"status\":\"fail\",\"message\":\"none of the recommended book\"}");
		}
		String booksstring ="";
		
		for(int i =0;i<rec.size();i++) {
			
			booksstring+="{\"book_id\":\""+rec.get(i).getBookInfo()+"\"}";
			if(!(i==rec.size()-1)) {
				booksstring+=",";
			}			
		}
		
//		System.out.println("booksstring:"+booksstring);
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
//		System.out.println(responseJson.toString());
		return responseJson;

	}
	//=============================================
	
	public JSONObject censorRecommendBook(JSONObject object) {
		JSONObject responseJson=null;
		String book_name =  object.getString("book_name");
		int pass =  object.getInt("pass");
		
		Recommend recommend = libmodel.getNotCensoredRecommendByBookName(book_name);
		if(recommend==null) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"none of the book name\"}");			
		}
		recommend.setCensored(pass);		
		libmodel.censoredRecommend(recommend);
		responseJson = new JSONObject("{\"status\":\"successful\"}");
		return responseJson;

	}
	public JSONObject getAllCensorRecommendBook(JSONObject object) {
		JSONObject responseJson=null;
		ArrayList<Recommend> rec = (ArrayList<Recommend>) libmodel.getAllCensoredPassRecommend();
		if(rec==null) {			
			return new JSONObject("{\"status\":\"fail\",\"message\":\"no more censored recommend\"}");
		}
		String booksstring ="";
		
		for(int i =0;i<rec.size();i++) {
			
			booksstring+="{\"book_name\":\""+rec.get(i).getBookInfo()+"\"}";
			if(!(i==rec.size()-1)) {
				booksstring+=",";
			}			
		}
		
//		System.out.println("booksstring:"+booksstring);
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
//		System.out.println(responseJson.toString());
		
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
	//預約者在書還回後3天內會保留，取消超過3天的預約
	public void checkReserver3Days() {
		
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
			case "insertBook":
				respond=insertBook(object).toString();
				break;
			case "deleteBook":
				respond=deleteBook(object).toString();
				break;
			case "modifyBook":
				respond=modifyBook(object).toString();
				break;
			case "lookupBook":
				respond=lookupBook(object).toString();
				break;

			case "modifyMember":
				respond=modifyMember(object).toString();
				break;
			case "insertMember":
				respond=insertMember(object).toString();
				break;	
			case "disableMember":
				respond=disableMember(object).toString();
				break;	
			case "lookupMember":
				respond=lookupMember(object).toString();
				break;	
			case "lookupBookHistory":
				//那本書的借閱者記錄
				respond=lookupBookHistory(object).toString();
				break;
			case "lookupUserHistory":
				//借閱者借過書的記錄
				respond=lookupUserHistory(object).toString();
				break;	
			case "forget":
				respond=forget(object).toString();
				break;	
//////////////////////////////////////////////////////////////
			case "reserve":
				respond=reserve(object).toString();
				break;	

			case "cancelReserve":
				respond=cancelReserve(object).toString();
				break;	
			case "lookUpUserReserveBook":
				respond=lookUpUserReserveBook(object).toString();
				break;	
				
			case "bookShouldReturnDay":
				respond=bookShouldReturnDay(object).toString();
				break;	
				
			case "recommendBook":
				respond=recommendBook(object).toString();
				break;
				
			case "lookUpRecommendBook":
				respond=lookUpRecommendBook(object).toString();
				break;
				
			case "censorRecommendBook":
				respond=censorRecommendBook(object).toString();
				break;
				
			case "getAllCensorRecommendBook":
				respond=getAllCensorRecommendBook(object).toString();
				break;
				
			//more 管理員發布訊息
				
				
				
			default:
				respond = "{\"status\":\"Not Yet\"}";
		
		}
		return respond;
	}

}
