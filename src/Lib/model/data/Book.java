package Lib.model.data;

public class Book {
	private String book_id;
    private String book_name;
    private String author;
    private int publishYear;
    private String publisher;
    private String isbn;
    private int amount;
    public Book() {}
    public Book(String id, String name, String author, int publishY, String publisher, 
    		String isbn, int amount) {
		this.book_id = id;
		this.book_name = name;
		this.author = author;
		this.publishYear = publishY;
		this.publisher = publisher;
		this.isbn = name;
		this.amount = amount;
    }
    
    public String getID() {
    	return book_id;
    }
	public void setID(String s) {
		book_id = s;
    }
	public String getName() {
    	return book_name;
    }
	public void setName(String s) {
		book_name = s;
    }
	public String getAuthor() {
    	return author;
    }
	public void setAuthor(String s) {
		author = s;
    }
	public int getPublishYear() {
    	return publishYear;
    }
	public void setPublishYear(int s) {
		publishYear = s;
    }
	public String getPublisher() {
    	return publisher;
    }
	public void setPublisher(String s) {
		publisher = s;
    }
	public String getIsbn() {
    	return isbn;
    }
	public void setIsbn(String s) {
		isbn = s;
    }
	public int getAmount() {
    	return amount;
    }
	public void setAmount(int bookaccount) {
		amount = bookaccount;
    }

}
