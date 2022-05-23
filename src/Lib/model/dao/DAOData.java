package Lib.model.dao;

import java.util.List;

////Encapsulation Use CRUD
public interface DAOData <T>{	
	//insert data
	void save(T t);	
//	//delete: shoudn't be used in this project i guess
//	void delete(T t);	
	//update data
	void update(T t);	
//	//select   ...where id=t.id, but i bet not to use this cuz we will fetch all the data from DB
//	T get(T t);	
	//select *
	List<T> listAll();


}