package Lib.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Lib.model.dao.*;
import Lib.model.data.*;

public class LibModel {
	Timer timer;
	//TODO:放ado
	DAOData userDAO;
	DAOData bookDAO;
	DAOData historyDAO;
	DAOData resevationDAO;
	DAOData recommendationsDAO;
	
	//TODO:資料
	ArrayList<User>users;
	ArrayList<Book>books;
	ArrayList<History>histories;
	ArrayList<Reservation>resevations;
	ArrayList<Recommend>recommendations;
	
	//TODO:建立獨體模式
	private volatile static LibModel libmodel;
	private LibModel(){
		timer= new Timer();
		//每天跑一次
		timer.schedule(new LibTimer(),2000L,1000*60*60*24L);
		userDAO = new DAOUserImpl();
		bookDAO = new DAOBookImpl();
		historyDAO = new DAOHistoryImpl();	
		resevationDAO = new DAOReservationImpl();
		recommendationsDAO = new DAORecommendImpl();
		updateModel();
	}
	//預約者在書還回後3天內會保留，取消超過3天的預約
	public void checkReserver3Days() {
		if(resevations==null) {
			return;
		}
		for(Reservation r :resevations) {
			if(r.getIsFinished()==0) {
				Book book = null;
				for(Book b : books) {
					if(r.getBid().equals(b.getID())) {
						book = b;
						break;
					}				
				}
				if(book.getAmount()>0) {
					ArrayList<History> his = (ArrayList<History>) this.getBookHistroy(book);
					if(his.size()==0) {
						break;
					}
			        
					long now = System.currentTimeMillis();
			        Date currentdate = new Date(now);
			        Date d=null;
			        d = Date.valueOf("1900-01-01");

					for(History h :his) {
						if(d.after(h.getReturnDay())) {
							d = h.getReturnDay();
						}
					}					
				    Calendar c = Calendar.getInstance();
			        c.setTime(d);
			        c.add(Calendar.DATE, 3);
			        d = new Date(c.getTimeInMillis());
			        if(d.before(currentdate)) {
			        	r.setIsFinished(1);
			        	this.setReserve(r);
			        }					
				}			
			}
		}
		updateModel();
	}
	
	class LibTimer extends TimerTask{
	    public void run() {
	    	checkReserver3Days();
	    }
	}
	
	public static LibModel getInstance() {
		if(libmodel == null) {
			synchronized (LibModel.class) {
				if(libmodel == null) {
					libmodel = new LibModel();
				}
			}
		}		
		return libmodel;
	}
	
	//triggered if there is any write into DB or when initializing the model, and then update the data members in this model
	private void updateModel() {	
		users = (ArrayList<User>) userDAO.listAll();	
		books = (ArrayList<Book>) bookDAO.listAll();	
		histories = (ArrayList<History>) historyDAO.listAll();	
		resevations = (ArrayList<Reservation>) resevationDAO.listAll();
		recommendations = (ArrayList<Recommend>) recommendationsDAO.listAll();
		
	}
	
	//TODO:建立User資料處裡函式
	public List<User> getAllUsers() {
		return users;
	}
	
	public User getUserByID(String uid) {
		User user=null;
		for (User u : users) {
			  if(u.getId().equals(uid)) {
				  user =u;
			  }
		}	
		return user;
	}
	public User getUserByAcc(String acc) {		
		User user=null;
		for (User u : users) {
			  if(u.getAccount().equals(acc)) {
				  user =u;
			  }
		}	
		return user;
	}
	public synchronized void addUser(User user) {		
		userDAO.save(user);
		updateModel();
	}
	public synchronized void updateUser(User user) {		
		userDAO.update(user);
		updateModel();
	}
	
	//TODO:建立Book資料處裡函式
	public List<Book> getAllBooks(){
		return books;
	}
	public Book getBookByID(String bid){
		Book book=null;
		for (Book b : books) {
			  if(b.getID().equals(bid)) {
				  book =b;
			  }
		}	
		return book;
		
	}
	public List<Book> getSearchBooks(String keyword){

		
		int bookcount =this.books.size();
		ArrayList<Book> similar_string_book = new ArrayList<Book>();
		
		for(int j=0;j<bookcount;j++)
		{
			if((this.books.get(j).getName().indexOf(keyword)!=-1)||(this.books.get(j).getAuthor().indexOf(keyword)!=-1)){
				similar_string_book.add(this.books.get(j));
			}
		}
		if(similar_string_book.size()==0){
			return null;
		}

		return similar_string_book;

	}
	public synchronized void addBook(Book b,int add_amount) {
		//如果已經有了只要數量++
//		System.out.println(b.getID());
		boolean exist=false;
		
		for(Book book : books) {
//			System.out.println(book.getID());
//			System.out.println(b.getIsbn().equals(book.getIsbn()));
//			System.out.println(b.getID().equals(book.getID()))		
			if(b.getID()==null) {
				break;
			}
			if(/*b.getIsbn().equals(book.getIsbn())||*/b.getID().equals(book.getID())) {				
//				System.out.println(book.getAmount());
				book.setAmount(book.getAmount()+add_amount);
//				System.out.println(book.getAmount());				
				exist=true;
				bookDAO.update(book);
			}
		}
		//不存在才要加入
		if(!exist) {
			b.setAmount(add_amount);
			bookDAO.save(b);			
		}
		updateModel();		
	}
	//TODO:借閱紀錄
	
	
	public List<History> getBookHistroy(Book b) {
		ArrayList<History> his=new ArrayList<History>();
//		System.out.println("========0=======");
		for(History h:histories) {
//			System.out.println("========1======="+h.getBid()+h.getBid().equals(b.getID()));
			if(h.getBid().equals(b.getID())) {
				his.add(h);				
			}			
		}		
		
		return his;
	}
	public List<History> getUserHistroy(User u) {
		ArrayList<History> his=new ArrayList<History>();
		for(History h:histories) {
			if(h.getUid().equals(u.getId())) {
				his.add(h);				
			}			
		}		
		return his;
	}
	public History getHistroy(User u, Book b) {
		History his=null;	
		for(History h:histories) {
			if(h.getBid().equals(b.getID())&&h.getUid().equals(u.getId())&&h.getReturnDay()==null){//&&h.getReturnDay().equals(null)) {
				his =h;				
				break;
			}			
		}		
//		
//		
//		System.out.println(his.getBid());
//		System.out.println(his.getHid());
//		System.out.println(his.getUid());
//		System.out.println(his.getBorrowDay());
//		System.out.println(his.getReturnDay());
		
		return his;
	}
	
	public synchronized void takeOutBook(History h) {
//		History his = new History();
		//更動書籍數量
		for(Book book : books) {
			if(book.getID().equals(h.getBid())) {
				book.setAmount(book.getAmount()-1);
				bookDAO.update(book);
				break;
			}
		}		
		//加入新的借閱紀錄
		historyDAO.save(h);		
		updateModel();
	}
	
	public synchronized void putBackBook(History h) {
		Book b = new Book();
		b.setID(h.getBid());		
		addBook(b,1);
		historyDAO.update(h);
		updateModel();
	}


	public synchronized void updateBook(Book b) {	
		bookDAO.update(b);
		updateModel();
	}
	
	public synchronized void addReserve(Reservation r) {	
		resevationDAO.save(r);
		updateModel();
	}
	public List<Reservation> getReserveByUser(User u) {	
		ArrayList<Reservation> reserve= new ArrayList<Reservation>();
		
		for (Reservation r : resevations) {
			  if(u.getId().equals(r.getUid())&&r.getIsFinished()==0) {
				  reserve.add(r);
			  }
		}	
//		System.out.println(reserve.size());
		return reserve;
	}
	public Reservation getReserveByBookUser(Book b,User u) {	
		Reservation reserve=null;
		
		for (Reservation r : resevations) {
			  if(u.getId().equals(r.getUid())&&b.getID().equals(r.getBid())&&r.getIsFinished()==0) {
				  reserve =r;
			  }
		}	

		return reserve;
	}
	//取得預約中 預約時間最早的
	public List<Reservation> getReserveByBook(Book b) {	
		ArrayList<Reservation> reserve= new ArrayList<Reservation>();

		long now = System.currentTimeMillis();
		Date d = new Date(now);
		
		for (Reservation r : resevations) {
			  if(b.getID().equals(r.getBid())&&r.getIsFinished()==0) {
				  if(r.getReserveDay().before(d)) {
					  reserve.add(r);
				  }
				  
			  }
		}	
		return reserve;
	}
	//取消預約及拿到預約的書時用
	public synchronized void setReserve(Reservation r) {	
		resevationDAO.update(r);
		updateModel();
	}
	//還沒有審核的推薦圖書清單
	public List<Recommend> getNotCensoredRecommend(){
		ArrayList<Recommend> rec=new ArrayList<Recommend>();
		for(Recommend r:recommendations) {
			if(r.getCensored()==2) {
				rec.add(r);
			}
		}
		if(rec.size()==0) {
			return null;
		}
		
		return rec;
	}
	//推薦圖書
	public synchronized void addRecommend(Recommend r) {	
		recommendationsDAO.save(r);
		updateModel();
	}
	//審核圖書 沒過就設為2
	public synchronized void censoredRecommend(Recommend r) {	
		recommendationsDAO.update(r);
		updateModel();
	}
	
	public boolean isSameRecommend(String s) {
		boolean same = false;
		for(Recommend r:recommendations) {
			if(r.getBookInfo().equals(s)) {
				same = true;
			}
		}

		return same;
	}
	
	public Recommend getNotCensoredRecommendByBookName(String s) {
		Recommend re =null;
		for(Recommend r:recommendations) {
			if(r.getBookInfo().equals(s)&&r.getCensored()==2) {
				re = r;
			}
		}

		return re;
	}
	
	public List<Recommend> getAllCensoredPassRecommend() {
		ArrayList<Recommend> re =null;
		for(Recommend r:recommendations) {
			if(r.getCensored()==1) {
				re.add(r);
			}
		}
		return re;
	}


	
	
	
	
}
