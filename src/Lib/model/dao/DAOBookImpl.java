package Lib.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.Book;
import Lib.model.data.User;


public class DAOBookImpl implements DAOData<Book>{

	@Override
	public void save(Book b) {

	    String sql_count = "select count(*) from lib_book";
	    String sql_insert = "INSERT INTO lib_book(b_id,b_name,author,pubyear,isbn,amount,publisher) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
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

	        String pattern = "%020d"; // 格式化字串，整數，長度10，不足部分左邊補0
	        String str = String.format(pattern, count + 1);
	        String book_id = "b" + str;

	        ps = conn.prepareStatement(sql_insert);
	        ps.setString(1, b.getID());
	        ps.setString(2, b.getName());
	        ps.setString(3, b.getAuthor());
	        ps.setInt(4,b.getPublishYear());
	        ps.setString(5, b.getIsbn());
	        ps.setInt(6, b.getAmount());
	        ps.setString(7, b.getPublisher());

	        ps.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }
		
	}

	@Override
	public void update(Book b) {
		// TODO Auto-generated method stub
        String sql = "UPDATE lib_book "
        		+ "SET b_name=?,author=?,pubyear=?,isbn=?,amount=?,publisher=? "
        		+ "WHERE b_id=?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // 創建語句對象
            ps = conn.prepareStatement(sql);        
	        	        
	        ps.setString(1, b.getName());
	        ps.setString(2, b.getAuthor());
	        ps.setInt(3, b.getPublishYear());
	        ps.setString(4, b.getIsbn());
	        ps.setInt(5, b.getAmount());
	        ps.setString(6, b.getPublisher());
	        ps.setString(7, b.getID());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, null);
        }
		
	}

	@Override
	public List<Book> listAll() {		
        List<Book> list = new ArrayList<Book>();
        String sql = "SELECT * FROM lib_book";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            // 創建語句對象
            ps = conn.prepareStatement(sql);
            // 執行SQL語句
            rs = ps.executeQuery();
            while(rs.next()) {
                Book book = new Book(rs.getString("b_id"),
                		rs.getString("b_name"),
                		rs.getString("author"),
                		rs.getInt("pubyear"),
                		rs.getString("publisher"),
                		rs.getString("isbn"),
                		rs.getInt("amount"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, rs);
        }
        return list;		

	}
}
