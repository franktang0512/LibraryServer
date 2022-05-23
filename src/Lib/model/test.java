package Lib.model;

import java.util.List;

import Lib.model.dao.DAOData;
import Lib.model.dao.DAOUserImpl;
import Lib.model.data.User;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DAOData userDAO = new DAOUserImpl();
		
		List<User> list = userDAO.listAll();
		System.out.println(list.get(0).getBirthday());

	}

}
