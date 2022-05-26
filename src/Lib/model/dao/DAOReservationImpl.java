package Lib.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.History;
import Lib.model.data.Reservation;

public class DAOReservationImpl implements DAOData<Reservation>{

	@Override
	public void save(Reservation reserve) {
		String sql_count = "select count(*) from lib_reserve";
	    String sql_insert = "INSERT INTO lib_reserve(reserve_id,uid,bid,reserve_date,is_finished) VALUES(?,?,?,?,?)";
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
	        String reserve_id = "res" + str;

	        ps = conn.prepareStatement(sql_insert);
	        ps.setString(1,reserve_id);
	        ps.setString(2, reserve.getUid());
	        ps.setString(3, reserve.getBid());
	        ps.setDate(4,reserve.getReserveDay());
	        ps.setInt(5, reserve.getIsFinished());


	        ps.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }
		
	}

	@Override
	public void update(Reservation reserve) {
        String sql = "UPDATE lib_reserve "
        		+ "SET uid=?,bid=?,reserve_date=?,is_finished=? "
        		+ "WHERE reserve_id=?";
        
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // 創建語句對象
            ps = conn.prepareStatement(sql);        	        	        
	        ps.setString(1, reserve.getUid());
	        ps.setString(2, reserve.getBid());
	        ps.setDate(3, reserve.getReserveDay());
	        ps.setInt(4, reserve.getIsFinished());
	        ps.setString(5, reserve.getReserveid());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, null);
        }
		
	}

	@Override
	public List<Reservation> listAll() {
        List<Reservation> list = new ArrayList<Reservation>();
        String sql = "SELECT * FROM lib_reserve";
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
            	Reservation reserve = new Reservation();
            	reserve.setReserveid(rs.getString("reserve_id"));
            	reserve.setUid(rs.getString("uid"));
            	reserve.setBid(rs.getString("bid"));
            	reserve.setReserveDay(rs.getDate("reserve_date"));
            	reserve.setIsFinished(rs.getInt("is_finished"));
                list.add(reserve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, rs);
        }
        return list;
	}







}
