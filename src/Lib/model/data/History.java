package Lib.model.data;

import java.util.Date;

public class History {
	private String u_id;
    private String b_id;
    private Date borrowDay;
    private Date returnDay;
    
    public History(String u_id, String b_id, Date bd, Date rd) {
		this.u_id = u_id;
		this.b_id = b_id;
		this.borrowDay = bd;
		this.returnDay = rd;
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
