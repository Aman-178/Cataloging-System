package Brm;

public class Book {
    private int BookId;
    private String Tittle,Publisher,Author;
    private double price;

    public int getBookId(){
        return BookId;
    }
    public void setBookId(int Bookid){
        this.BookId=Bookid;
    }

    public String getTittle(){
        return Tittle;
    }
    public void setTittle(String Tittle){
        this.Tittle=Tittle;
    }
    public String getPublisher(){
        return Publisher;
    }
    public void setPublisher(String Publisher){
        this.Publisher=Publisher;
    }
    public String getAuthor(){
        return Author;
    }
    public void setAuthor(String Author){
        this.Author=Author;
    }
    public double getprice(){
        return  price;
    }
    public void setprice(double  price){
        this. price= price;
    }

}
