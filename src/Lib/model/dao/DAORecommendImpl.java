package Lib.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.History;
import Lib.model.data.Recommend;


public class DAORecommendImpl implements DAOData<Recommend>{

	@Override
	public void save(Recommend recommend) {
		String sql_count = "select count(*) from lib_recommend";
	    String sql_insert = "INSERT INTO lib_recommend(recommend_id,uid,bookinfo) VALUES(?,?,?)";
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

	        String pattern = "%015d"; // �榡�Ʀr��A��ơA����10�A�������������0
	        String str = String.format(pattern, count + 1);
	        String recommend_id = "rec" + str;

	        ps = conn.prepareStatement(sql_insert);
	        ps.setString(1, recommend_id);
	        ps.setString(2, recommend.getUserID());
	        ps.setString(3, recommend.getBookInfo());
	        ps.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }
		
	}

	@Override
	public void update(Recommend recommend) {
        String sql = "UPDATE lib_recommend "
        		+ "SET uid=?,bookinfo=? "
        		+ "WHERE recommend_id=?";
        
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // �Ыػy�y��H
            ps = conn.prepareStatement(sql);        
	        	        
	        ps.setString(1, recommend.getUserID());
	        ps.setString(2, recommend.getBookInfo());
	        ps.setString(3, recommend.getRecommendID());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, null);
        }
		
	}

	@Override
	public List<Recommend> listAll() {
        List<Recommend> list = new ArrayList<Recommend>();
        String sql = "SELECT * FROM lib_recommend";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            // �Ыػy�y��H
            ps = conn.prepareStatement(sql);
            // ����SQL�y�y
            rs = ps.executeQuery();
            while(rs.next()) {
            	Recommend recommend = new Recommend();
            	recommend.setRecommendID(rs.getString("recommend_id"));
            	recommend.setUserID(rs.getString("uid"));
            	recommend.setBookInfo(rs.getString("bookinfo"));
                list.add(recommend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, rs);
        }
        return list;
	}


}
