package src.Books;

public class Novel extends Book {

    public enum Genre {
        fiction, fantasy, romance, thriller, horror, biography, history, psychological
    }

    private Genre genre;

    public Novel(int id, String name, String author, int year, boolean borrowed, Genre genre) {
        super(id, name, author, year, borrowed);
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

}
