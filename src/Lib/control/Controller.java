package Lib.control;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

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
	
	//TODO:�n�J���U
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
		        //�B�zjava util sql date�t�����D �o�̲Τ@��sql��date
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
	
	//TODO:Ū�̭ɾ\
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
			//�ƶ������H�Ǹ��p��Ѷq�N�i�H�ɾ\
			if(r.get(i).getUid().equals(u.getId())&&i+1<=b.getAmount()) {
				//�����w�����A�A�i�J�ɮѵ{��
				r.get(i).setIsFinished(1);
				break;
			}else if(r.get(i).getUid().equals(u.getId())&&i+1>b.getAmount()) {
				responseJson = new JSONObject("{\"status\":\"fail\",\"message\":\"you have to wait for the order\"}");
				return responseJson;
			}
			//�ƶ��w�����H>=�{���Ѷq �N�L�k�ɾ\
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
	//TODO:���y�����d�R��
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
	        //�B�zjava util sql date�t�����D �o�̲Τ@��sql��date
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
	//TODO:�ɾ\�����d��
	public JSONObject lookupBookHistory(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		Book b = new Book();
		b.setID(book_id);
//		System.out.println("========0=======");
		ArrayList<History> hislist = (ArrayList<History>) libmodel.getBookHistroy(b);
//		System.out.println("========1=======");
		//TODO:�g��JSON
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
		
		//TODO:�g��JSON
		
//		System.out.println("========1=======");
		//TODO:�g��JSON
		if(hislist.size()==0) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"none of the record\"}");
		}
//		System.out.println("========1=======1");
		
		ArrayList<History> hislist_continue = new ArrayList<History>();
		ArrayList<String> continue_b = new ArrayList<String>();
		int x =0;
		//�X����ɬ���
		for(int i =0;i<hislist.size();i++) {
//			System.out.println("========1=======2");
			if(i==0) {
				History h = new History();
				h.setBid(hislist.get(i).getBid());
				h.setHid(hislist.get(i).getHid());
				h.setUid(hislist.get(i).getUid());
				h.setBorrowDay(hislist.get(i).getBorrowDay());
				h.setReturnDay(hislist.get(i).getReturnDay());
				hislist_continue.add(h);
				continue_b.add("0");
//				System.out.println("========1=======3");
				continue;					
			}
			
			if(hislist.get(i).getReturnDay()!=null&&hislist.get(i).getBid().equals(hislist_continue.get(hislist_continue.size()-1).getBid())&&hislist.get(i).getBorrowDay().toString().equals(hislist_continue.get(hislist_continue.size()-1).getReturnDay().toString())
					) {
				
				hislist_continue.get(hislist_continue.size()-1).setBid(hislist_continue.get(hislist_continue.size()-1).getBid());
				hislist_continue.get(hislist_continue.size()-1).setHid(hislist_continue.get(hislist_continue.size()-1).getHid());
				hislist_continue.get(hislist_continue.size()-1).setUid(hislist_continue.get(hislist_continue.size()-1).getUid());
				hislist_continue.get(hislist_continue.size()-1).setBorrowDay(hislist_continue.get(hislist_continue.size()-1).getBorrowDay());
				hislist_continue.get(hislist_continue.size()-1).setReturnDay(hislist.get(i).getReturnDay());
				continue_b.set(continue_b.size()-1, "1");
//				System.out.println("========1=======4");
				continue;
				
			}else if(hislist_continue.get(hislist_continue.size()-1).getReturnDay()!=null&&hislist.get(i).getBid().equals(hislist_continue.get(hislist_continue.size()-1).getBid())&&hislist.get(i).getBorrowDay().toString().equals(hislist_continue.get(hislist_continue.size()-1).getReturnDay().toString())){
				hislist_continue.get(hislist_continue.size()-1).setBid(hislist_continue.get(hislist_continue.size()-1).getBid());
				hislist_continue.get(hislist_continue.size()-1).setHid(hislist_continue.get(hislist_continue.size()-1).getHid());
				hislist_continue.get(hislist_continue.size()-1).setUid(hislist_continue.get(hislist_continue.size()-1).getUid());
				hislist_continue.get(hislist_continue.size()-1).setBorrowDay(hislist_continue.get(hislist_continue.size()-1).getBorrowDay());
				hislist_continue.get(hislist_continue.size()-1).setReturnDay(null);
				continue_b.set(continue_b.size()-1, "1");
//				System.out.println("========1=======41");
			}else {
				History h = new History();
				h.setBid(hislist.get(i).getBid());
				h.setHid(hislist.get(i).getHid());
				h.setUid(hislist.get(i).getUid());
				h.setBorrowDay(hislist.get(i).getBorrowDay());
				h.setReturnDay(hislist.get(i).getReturnDay());
				hislist_continue.add(h);
				continue_b.add("0");
//				System.out.println("========1=======5");
			}				
					
		}
		String s="";
		for(int i =0;i<hislist_continue.size();i++) {
			History h =  hislist_continue.get(i);
			String bid = h.getBid();
//			System.out.println("========3=======");
			Book b = libmodel.getBookByID(bid);
//			System.out.println("========4=======");
			String username = u.getName();
			String useracc = u.getAccount();
			Date borrowdate =h.getBorrowDay();
			Date returndate = h.getReturnDay();
//			long now = System.currentTimeMillis();
//			Date returndate_ = (returndate!=null?returndate:new Date(now));
//			Date borrowdate_ =(Date) borrowdate.clone();
//		    Calendar c = Calendar.getInstance();
//	        c.setTime(borrowdate_);
//	        c.add(Calendar.DATE, 30);
//	        borrowdate_= new Date(c.getTimeInMillis());
//			int iscontinue =0;
//			if(borrowdate_.before(returndate_)) {
//				iscontinue=1;
//			}
			
			s+="{\"book_id\":\""+b.getID()+"\",\"book_name\":\""+b.getName()+"\","
							+ "\"borrow_date\":\""+(borrowdate!=null?borrowdate.toString():null)+"\","
							+ "\"return_date\":\""+(returndate!=null?returndate.toString():null)+"\""+","
							+ "\"isContinue\":"+continue_b.get(i)+"}";
			if(i!=hislist_continue.size()-1) {
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
//		System.out.println("===================0");
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
//		System.out.println("===================1");
		ArrayList<Reservation> userreserve = (ArrayList<Reservation>)libmodel.getReserveByUser(u);
//		System.out.println("===================2");
		if(userreserve==null) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"no reservation so far\"}");
			return responseJson;			
		}
//		System.out.println("===================3");
		ArrayList<Book> b = new ArrayList<Book>();
		for(Reservation r:userreserve) {
			b.add(libmodel.getBookByID(r.getBid()));
		}
//		System.out.println("===================4");
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
//		System.out.println("===================5");
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
//		System.out.println("===================1");
		if(libmodel.isSameRecommend(book_name)) {
			responseJson = new JSONObject("{\"status\":\"fail\",\"msg\":\"someone has already recommended, contact lib admin to check detail if other books(more than 1) has same name of what you recommended \"}");
			return responseJson;
		}
		System.out.println(libmodel.isSameRecommend(book_name));
//		System.out.println("===================2");
		User u = libmodel.getUserByAcc(account);
//		System.out.println("===================3");
		Recommend recommend = new Recommend();
		recommend.setUserID(u.getId());
		recommend.setBookInfo(book_name);
		recommend.setCensored(2);
		libmodel.addRecommend(recommend);
//		System.out.println("===================4");
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

	
	
	public JSONObject checkreserve(JSONObject object) {
		JSONObject responseJson=null;
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		ArrayList<Reservation> reserves = (ArrayList<Reservation>) libmodel.getReserveByUser(u);
		ArrayList<Book> books = new ArrayList<Book>();
		for(Reservation r :reserves) {
			Book b = libmodel.getBookByID(r.getBid());
			if(b.getAmount()>0) {
				
				ArrayList<Reservation> rrr = (ArrayList<Reservation>) libmodel.getReserveByBook(b);
				if(rrr.size() !=0) {
					int order =0;
					for(int i =0;i<rrr.size();i++) {
						if(rrr.get(i).getUid().equals(u.getId())){
							order = i+1;
						}
					}
					if(order <= b.getAmount()) {
						books.add(b);
						continue;
					}					
				}else {
					books.add(b);
				}				
			}			
		}
		String booksstring="";
		if(books.size()==0) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"no reseved book can be borrowed\"}");
			
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
		booksstring="{\"status\":\"successful\",\"books\":["+booksstring+"]}";
		responseJson= new JSONObject(booksstring);
		
		return responseJson;
	}
	
	
	
	public JSONObject continueBorrow(JSONObject object) {
		JSONObject responseJson=null;
		String book_id =  object.getString("book_id");
		String account =  object.getString("account");
		User u = libmodel.getUserByAcc(account);
		Book b =libmodel.getBookByID(book_id);
		//���ˬdreserve ���S���H�Ƴo����
		ArrayList<Reservation> rrr = (ArrayList<Reservation>)libmodel.getReserveByBook(b);
		if(rrr.size()>0&&!rrr.get(0).getUid().equals(u.getId())) {
			return new JSONObject("{\"status\":\"fail\",\"message\":\"someone has been reserving\"}");			
		}
		
		//�S���ƶ����ѴN�i�H�� ���k�ٮɶ��A���s�p��
		History history = libmodel.getHistroy(u, b);
		long now = System.currentTimeMillis();
		Date sqlDate = new Date(now);
		history.setReturnDay(sqlDate);		
		libmodel.putBackBook(history);
		
		History history_new = new  History();
		history_new.setBid(b.getID());
		history_new.setUid(u.getId());
		history_new.setBorrowDay(sqlDate);
		libmodel.takeOutBook(history_new);

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
			//TODO:�d�߹Ϯѡ]�i��O�d�ѦW�Χ@�̡^�B�ɾ\�����d��
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
				//�����Ѫ��ɾ\�̰O��
				respond=lookupBookHistory(object).toString();
				break;
			case "lookupUserHistory":
				//�ɾ\�̭ɹL�Ѫ��O��
				respond=lookupUserHistory(object).toString();
				break;	
			case "forget":
				respond=forget(object).toString();
				break;	
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
			case "checkreserve":
				respond=checkreserve(object).toString();
				break;
			case "continueBorrow":
				respond=continueBorrow(object).toString();
				break;
				
			//more �޲z���o���T��
				
				
				
			default:
				respond = "{\"status\":\"Not Yet\"}";
		
		}
		return respond;
	}

}


