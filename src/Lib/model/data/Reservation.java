package Lib.model.data;

import java.util.Date;

public class Reservation {

	private String u_id;
    private String b_id;
    private Date reserveDay;
    
    public Reservation(String u_id, String b_id, Date rD) {
		this.u_id = u_id;
		this.b_id = b_id;
		this.reserveDay = rD;
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
	public Date getReserveDay() {
    	return reserveDay;
    }
	public void setReserveDay(Date d) {
		reserveDay = d;
    }

}
