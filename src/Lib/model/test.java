package Lib.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.sql.Date;
import java.util.List;

import Lib.model.dao.DAOData;
import Lib.model.dao.DAOUserImpl;
import Lib.model.data.Book;
import Lib.model.data.User;

public class test {

	public static void main(String[] args) throws ParseException {
//		DAOData userDAO = new DAOUserImpl();
		LibModel l = LibModel.getInstance();
		Book b = new Book();
		b.setID(null);
		b.setName("jQuery網頁設計範例教學");
		b.setAuthor("德瑞工作室");
		b.setIsbn("0-000-00000-5");
		b.setPublisher("藍膠出版");
		b.setPublishYear(2011);
		b.setAmount(1);
		

		l.addBook(b, 1);
		System.out.println("finish");
		//List<User> list = userDAO.listAll();
//		User u = new User();
//		u.setAccount("abc");
//		u.setPassword("123");
//		u.setKind(1);
//		u.setSex(1);
//        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
//        Date bd=(Date) formatter1.parse("1994-12-4");	
//
//
//        long timeInMilliSeconds = bd.getTime();
//        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
//        
//        
//        
//		u.setBirthday(date);
//		u.setPhone("909090909099090");
//		u.setAddress("Taipei");
//		u.setEmail("ffff@yahoo.com");
//		u.setName("Flick");
////		userDAO.save(u);
//		
//		
//		
//		LibModel ll = LibModel.getInstance();
//		User u0 = ll.getUserByAcc("abc");
//		System.out.println(u0.getId());
//		
		

	}

	
	

	
	
}
