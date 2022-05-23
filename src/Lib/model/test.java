package Lib.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.sql.Date;
import java.util.List;

import Lib.model.dao.DAOData;
import Lib.model.dao.DAOUserImpl;
import Lib.model.data.User;

public class test {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		DAOData userDAO = new DAOUserImpl();
		
		//List<User> list = userDAO.listAll();
		User u = new User();
		u.setAccount("abc");
		u.setPassword("123");
		u.setKind(1);
		u.setSex(1);
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
        Date bd=(Date) formatter1.parse("1994-12-4");	


        long timeInMilliSeconds = bd.getTime();
        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
        
        
        
		u.setBirthday(date);
		u.setPhone("0912345678");
		u.setAddress("Taipei");
		u.setEmail("ffff@yahoo.com");
		u.setName("Flick");
		userDAO.save(u);
		System.out.println();
		
		

	}

	
	

	
	
}
