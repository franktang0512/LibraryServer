package Lib.model.data;

import java.sql.Date;

//import java.util.Date;

public class History {
	private String h_id;
	private String u_id;
    private String b_id;
    private Date borrowDay;
    private Date returnDay;
    public History() {}
    public History(String h_id,String u_id, String b_id, Date bd, Date rd) {
		this.h_id = h_id;
    	this.u_id = u_id;
		this.b_id = b_id;
		this.borrowDay = bd;
		this.returnDay = rd;
    }
    public String getHid() {
    	return h_id;
    }
	public void setHid(String s) {
		h_id = s;
    }
    public String getUid() {
    	return u_id;
    }
	public void setUid(String s) {
		u_id = s;
    }
	public String getBid() {
    	return b_id;
    }
	public void setBid(String s) {
		b_id = s;
    }
	public Date getBorrowDay() {
    	return borrowDay;
    }
	public void setBorrowDay(Date d) {
		borrowDay = d;
    }
	public Date getReturnDay() {
    	return returnDay;
    }
	public void setReturnDay(Date d) {
		returnDay = d;
    }

}
