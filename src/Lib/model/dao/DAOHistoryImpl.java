package Lib.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.History;
import Lib.model.data.User;


public class DAOHistoryImpl implements DAOData<History>{

	@Override
	public void save(History history) {
		// TODO Auto-generated method stub
		String sql_count = "select count(*) from lib_history_check";
	    String sql_insert = "INSERT INTO lib_history_check(chech_id,user_id,book_id,start_date,end_date) VALUES(?,?,?,?,?)";
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

	        String pattern = "%015d"; // 格式化字串，整數，長度10，不足部分左邊補0
	        String str = String.format(pattern, count + 1);
	        String his_id = "h" + str;

	        ps = conn.prepareStatement(sql_insert);
	        ps.setString(1, his_id);
	        ps.setString(2, history.getUid());
	        ps.setString(3, history.getBid());
	        ps.setDate(4, (Date) history.getBorrowDay());
	        ps.setDate(5, (Date) history.getReturnDay());

	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }
		
	}

	@Override
	public void update(History history) {
		// TODO Auto-generated method stub
        String sql = "UPDATE lib_history_check "
        		+ "SET user_id=?,book_id=?,start_date=?,end_date=? "
        		+ "WHERE chech_id=?";
        
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // 創建語句對象
            ps = conn.prepareStatement(sql);        
	        	        
	        ps.setString(1, history.getUid());
	        ps.setString(2, history.getBid());
	        ps.setDate(3, history.getBorrowDay());
	        ps.setDate(4, history.getReturnDay());
	        ps.setString(5, history.getHid());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, null);
        }
	}

	@Override
	public List<History> listAll() {
        List<History> list = new ArrayList<History>();
        String sql = "SELECT * FROM lib_history_check";
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
            	History history = new History();
            	history.setHid(rs.getString("chech_id"));
            	history.setBid(rs.getString("user_id"));
            	history.setUid(rs.getString("book_id"));
            	history.setBorrowDay(rs.getDate("start_date"));
            	history.setReturnDay(rs.getDate("end_date"));
                list.add(history);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, rs);
        }
        return list;
	}

}
