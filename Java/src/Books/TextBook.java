package src.Books;

public class TextBook extends Book {
    public enum Grade {
        First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eight, Ninth, HighSchool, University
    }

    private Grade grade;

    public TextBook(int id, String name, String author, int year, boolean borrowed, Grade grade) {
        super(id, name, author, year, borrowed);
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
