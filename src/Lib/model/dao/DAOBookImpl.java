package Lib.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import Lib.model.data.Book;


public class DAOBookImpl implements DAOData<Book>{

	@Override
	public void save(Book b) {

	    String sql_count = "select count(*) from lib_book";
	    String sql_insert = "INSERT INTO lib_book() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        conn = DBUtils.getConnection();
	        ps = conn.prepareStatement(sql_count);
	        rs = ps.executeQuery();
	        int count = 0;
	        if (rs.next()) {
	            count = rs.getInt("count");
	            //System.out.println(rs.getInt("count"));
	        }

	        String pattern = "%09d"; // 格式化字串，整數，長度10，不足部分左邊補0
	        String str = String.format(pattern, count + 1);
	        String user_id = "u" + str;

//	        ps = conn.prepareStatement(sql_insert);
//	        ps.setString(1, user_id);
//	        ps.setString(2, user.getName());
//	        ps.setString(3, user.getAccount());
//	        ps.setString(4, user.getPassword());
//	        ps.setInt(5, user.getKind());
//	        ps.setInt(6, user.getSex());
//	        ps.setDate(7, (Date) user.getBirthday());
//	        ps.setString(8, user.getEmail());
//	        ps.setString(9, user.getAddress());
//	        ps.setString(10, user.getPhone());
//	        ps.setInt(11,1);
//	        ps.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }
		
	}

	@Override
	public void update(Book b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Book> listAll() {
		// TODO Auto-generated method stub
		return null;
	}
	//TODO:implement lookup similar book
	public List<Book> keyWordSearch(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
