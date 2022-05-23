package Lib.model.data;


public class Recommend {
	
	private String u_id;
    private String book;
    
    public Recommend(String u_id, String book) {
		this.u_id = u_id;
		this.book = book;
    }
    
    public String getId() {
    	return u_id;
    }
	public void setId(String s) {
		u_id = s;
    }
	public String getBook() {
    	return book;
    }
	public void setBook(String s) {
		book = s;
    }

}
