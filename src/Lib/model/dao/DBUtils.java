package Lib.model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

// JDBC���u����
public class DBUtils {
    private static Properties properties = new Properties();
	static final String JDBC_DRIVER = "org.postgresql.Driver";  	
	static final String DB_URL = "jdbc:postgresql://ec2-34-233-214-228.compute-1.amazonaws.com:5432/d69u85fv8eq57s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	static final String USER ="obnetcgcbbxpbl";
	static final String PASS ="f9962202a36812df8f540b57baa67c68f820eb76d173235711bdf6362ccc4525";
    // ��JdbcUtil�����r�`�X�[���b�iJVM�ɡA�ߧY����
    static{
        try {
            //�[���MŪ��db.properties���
//           InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
//           properties.load(inStream);
           Class.forName("org.postgresql.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // �Ыبê�^�@��Connection��H
    public static Connection getConnection(){
        try {
//            return DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),
//                    properties.getProperty("password"));
            return DriverManager.getConnection(DB_URL,USER,PASS);
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //���������귽
    public  static void close(Connection conn, Statement st, ResultSet rs){
        try {
            if(rs != null){
                rs.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(st != null){
                    st.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if(conn != null){
                        conn.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}