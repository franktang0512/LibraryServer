package Lib.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Lib.model.data.User;

public class DAOUserImpl implements DAOData<User>{


	@Override
	public void save(User user) {		
      String sql = "INSERT INTO lib_user(s_id,user_name,account,password,kind,sex,birth,email,address,phone,can_use) VALUES(?,?)";
      Connection conn = null;
      PreparedStatement ps = null;
      try {
          conn = DBUtils.getConnection();
//          // 創建語句對象
//          ps = conn.prepareStatement(sql);
//          ps.setString(1,user.getUsername());
//          ps.setInt(2,user.getAge());
//          // 執行SQL語句
          ps.executeUpdate();
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
      	DBUtils.close(conn, ps, null);
      }
		
	}
	
	@Override
	public void update(User user) {
        String sql = "UPDATE lib_user SET username=?,age=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            // 創建語句對象
            ps = conn.prepareStatement(sql);
//            ps.setString(1, user.getUsername());
//            ps.setInt(2, user.getAge());
//            ps.setLong(3, user.getId());
            // 執行SQL語句
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
            // 創建語句對象
            ps = conn.prepareStatement(sql);
            // 執行SQL語句
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
