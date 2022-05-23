package Lib.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.User;

public class DAOUserImpl implements DAOData<User>{


	@Override
	public void save(User user) {

	    String sql_count = "select count(*) from lib_user";
	    String sql_insert = "INSERT INTO lib_user(s_id,user_name,account,password,kind,sex,birth,email,address,phone,can_use) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
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

	        String pattern = "%09d"; // �榡�Ʀr��A��ơA����10�A�������������0
	        String str = String.format(pattern, count + 1);
	        String user_id = "u" + str;

	        ps = conn.prepareStatement(sql_insert);
	        ps.setString(1, user_id);
	        ps.setString(2, user.getName());
	        ps.setString(3, user.getAccount());
	        ps.setString(4, user.getPassword());
	        ps.setInt(5, user.getKind());
	        ps.setInt(6, user.getSex());
	        ps.setDate(7, (Date) user.getBirthday());
	        ps.setString(8, user.getEmail());
	        ps.setString(9, user.getAddress());
	        ps.setString(10, user.getPhone());
	        ps.setInt(11,1);
	        ps.executeUpdate();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtils.close(conn, ps, rs);
	    }

	}
	
	@Override
	public void update(User user) {
        String sql = "UPDATE lib_user "
        		+ "SET user_name=?,password=?,kind=?,sex=?,birth=?,email=?,address=?,phone=?,can_use=? "
        		+ "WHERE account=?";
        
        
        
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // �Ыػy�y��H
            ps = conn.prepareStatement(sql);
	        
	        ps.setString(1, user.getName());	        
	        ps.setString(2, user.getPassword());
	        ps.setInt(3, user.getKind());
	        ps.setInt(4, user.getSex());
	        ps.setDate(5, (Date) user.getBirthday());
	        ps.setString(6, user.getEmail());
	        ps.setString(7, user.getAddress());
	        ps.setString(8, user.getPhone());
	        ps.setInt(9,user.getEnable());
	        ps.setString(10, user.getAccount());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, null);
        }
		
	}

	@Override
	public List<User> listAll() {		
		
        List<User> list = new ArrayList<User>();
        String sql = "SELECT * FROM lib_user";
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
                User user = new User(rs.getString("s_id"),rs.getInt("kind"), rs.getString("account"), rs.getString("password"), rs.getString("user_name"), rs.getInt("sex"), rs.getDate("birth"), rs.getString("email"), rs.getString("address"), rs.getString("phone"),rs.getInt("can_use"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	DBUtils.close(conn, ps, rs);
        }
        return list;

	}





}
