package Lib.model.data;

//import java.util.Date;
import java.sql.Date;

public class User {
	private String user_id;
    private int user_kind;
    private String account;
    private String password;
    private String user_name;
    private int sex;
    private Date birthday;
    private String email;
    private String address;
    private String phone;
    private int enable;
    public User() {}
    public User(String id, int kind, String acc, String psd, String name, 
    		int sex, Date birthday, String email, String address, String phone,int enable) {
		this.user_id = id;
		this.user_kind = kind;
		this.account = acc;
		this.password = psd;
		this.user_name = name;
		this.birthday = birthday;
		this.sex = sex;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.enable=enable;
    }
    
    public String getId() {
    	return user_id;
    }
	public void setId(String s) {
		user_id = s;
    }
	public int getKind() {
    	return user_kind;
    }
	public void setKind(int k) {
		user_kind = k;
    }
	public String getAccount() {
    	return account;
    }
	public void setAccount(String s) {
		account = s;
    }
	public String getPassword() {
    	return password;
    }
	public void setPassword(String s) {
		password = s;
    }
	public String getName() {
    	return user_name;
    }
	public void setName(String s) {
		user_name = s;
    }
	public Date getBirthday() {
    	return birthday;
    }
	public void setBirthday(Date d) {
		birthday = d;
    }
	public int getSex() {
    	return sex;
    }
	public void setSex(int s) {
		sex = s;
    }
	public String getAddress() {
    	return address;
    }
	public void setAddress(String s) {
		address = s;
    }
	public String getEmail() {
    	return email;
    }
	public void setEmail(String s) {
		email = s;
    }

	public String getPhone() {
    	return phone;
    }
	public void setPhone(String s) {
		phone = s;
    }
	
	public int getEnable() {
    	return enable;
    }
	public void setEnable(int s) {
		enable = s;
    }


}
