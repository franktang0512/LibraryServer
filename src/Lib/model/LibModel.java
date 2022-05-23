package Lib.model;

import java.util.ArrayList;
import java.util.List;

import Lib.model.dao.*;
import Lib.model.data.*;

public class LibModel {
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
		userDAO = new DAOUserImpl();
		bookDAO = new DAOBookImpl();
		historyDAO = new DAOHistoryImpl();	
		resevationDAO = new DAOReservationImpl();
		recommendationsDAO = new DAORecommendImpl();
		updateModel();
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
		histories = (ArrayList<History>) userDAO.listAll();	
		resevations = (ArrayList<Reservation>) userDAO.listAll();
		recommendations = (ArrayList<Recommend>) userDAO.listAll();
		
	}
	
	//TODO:建立User資料處裡函式
	public List<User> getAllUsers() {
		return users;
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
	public List<Book> getSearchBooks(String keyword){

		
		int bookcount =this.books.size();
		ArrayList<Book> similar_string_book = new ArrayList<Book>();
		
		for(int j=0;j<bookcount;j++)
		{
			if(this.books.get(j).getName().indexOf(keyword)!=-1){
				similar_string_book.add(this.books.get(j));
			}
		}
		if(similar_string_book.size()==0){
			return null;
		}

		return similar_string_book;

	}
	public synchronized void addBook(Book b) {
		//如果已經有了只要數量++
		boolean exist=false;
		for(Book book : books) {
			if(b.getIsbn().equals(book.getIsbn())) {
				book.setAmount(book.getAmount()+1);
				exist=true;
				bookDAO.update(book);
			}
		}
		//不存在才要加入
		if(!exist) {
			bookDAO.save(b);			
		}
		updateModel();		
	}
	public synchronized void returnBook(Book b) {
		
	}
	
}
