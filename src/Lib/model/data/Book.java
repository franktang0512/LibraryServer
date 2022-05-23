package Lib.model.data;

public class Book {
	private String book_id;
    private String book_name;
    private String author;
    private String publishYear;
    private String publisher;
    private String isbn;
    private String amount;
    
    public Book(String id, String name, String author, String publishY, String publisher, 
    		String isbn, String amount) {
		this.book_id = id;
		this.book_name = name;
		this.author = author;
		this.publishYear = publishY;
		this.publisher = publisher;
		this.isbn = name;
		this.amount = amount;
    }
    
    public String getId() {
    	return book_id;
    }
	public void setId(String s) {
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
	public String getPublishYear() {
    	return publishYear;
    }
	public void setPublishYear(String s) {
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
	public String getAmount() {
    	return amount;
    }
	public void setAmount(String s) {
		amount = s;
    }

}
