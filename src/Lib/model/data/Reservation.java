package Lib.model.data;

import java.sql.Date;

//import java.util.Date;

public class Reservation {

	private String reserve_id;
	private String u_id;
    private String b_id;
    private Date reserveDay;
    private int is_finished;
    public Reservation() {}
    public Reservation(String r_id,String u_id, String b_id, Date rD,int is_finished) {
		this.reserve_id =r_id;
    	this.u_id = u_id;
		this.b_id = b_id;
		this.reserveDay = rD;
		this.is_finished = is_finished;
	}
    public String getReserveid() {
    	return reserve_id;
    }
	public void setReserveid(String s) {
		reserve_id = s;
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
	
	public int getIsFinished() {
    	return is_finished;
    }
	public void setIsFinished(int is_finished) {
		this.is_finished = is_finished;
    }
	

}
