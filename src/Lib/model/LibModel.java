package Lib.model;

import java.util.ArrayList;
import java.util.List;

import Lib.model.dao.*;
import Lib.model.data.*;

public class LibModel {
	//TODO:放ado
	
	//TODO:資料
	ArrayList<User>users;
	ArrayList<Book>books;
	ArrayList<History>histories;
	ArrayList<Reservation>resevations;
	ArrayList<Recommend>recommendations;
	
	//TODO:建立獨體模式
	private volatile static LibModel libmodel;
	private LibModel(){
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
		DAOData userDAO = new DAOUserImpl();		
		users = (ArrayList<User>) userDAO.listAll();
		
		DAOData bookDAO = new DAOBookImpl();		
		books = (ArrayList<Book>) bookDAO.listAll();
		
		DAOData historyDAO = new DAOHistoryImpl();		
		histories = (ArrayList<History>) userDAO.listAll();
		
		DAOData resevationDAO = new DAOReservationImpl();		
		resevations = (ArrayList<Reservation>) userDAO.listAll();
		
		DAOData recommendationsDAO = new DAORecommendImpl();		
		recommendations = (ArrayList<Recommend>) userDAO.listAll();
		
	}
	
	//TODO:建立資料處裡函式
	public User getUser(String acc) {
		
		User user=null;
		for (User u : users) {
			  if(u.getAccount().equals(acc)) {
//				  user = new User();
				  user =u;
			  }
		}	
		return user;
	}
	
	public synchronized void setUser(User user) {
		DAOData userDAO = new DAOUserImpl();		
		userDAO.update(user);
		updateModel();
	}
	
	
	
	

	
}
