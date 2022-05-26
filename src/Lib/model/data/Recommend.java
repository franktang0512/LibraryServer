package Lib.model.data;


public class Recommend {
	
	private String recommend_id;
	private String u_id;
    private String bookinfo;
    private int censored;
    public Recommend() {}
    public Recommend(String u_id, String bookinfo,int censor) {
		this.u_id = u_id;
		this.bookinfo = bookinfo;
		this.censored = censor;
    }
    
    public String getRecommendID() {
    	return recommend_id;
    }
	public void setRecommendID(String s) {
		recommend_id = s;
    }
    
    public String getUserID() {
    	return u_id;
    }
	public void setUserID(String s) {
		u_id = s;
    }
	public String getBookInfo() {
    	return bookinfo;
    }
	public void setBookInfo(String s) {
		bookinfo = s;
    }
	
	public int getCensored() {
    	return censored;
    }
	public void setCensored(int s) {
		censored = s;
    }

}
