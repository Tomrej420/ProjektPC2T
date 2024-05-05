package Books;

public class Book {

    private int id;
    private String name;
    private String author;
    private int releaseyear;
    private Boolean borrowed;

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseyear() {
        return releaseyear;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReleaseyear(int releaseyear) {
        this.releaseyear = releaseyear;
    }

    public Book(int id, String name, String author, int year, boolean borrowed) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.releaseyear = year;
        this.borrowed = borrowed;

    }
}
