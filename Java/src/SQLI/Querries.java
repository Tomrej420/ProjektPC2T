package SQLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import Books.Book;
import Books.Novel;
import Books.TextBook;


/**
 * SelectQuerries
 */

public class Querries {

  private static final String insertQuery = "REPLACE INTO Books (id,Name,Author,Release_date,Available,Book_type,Genre_Grade) VALUES (?,?,?,?,?,?,?)";
  private static final String selectQuery = "SELECT * FROM Books";
  private static final String deleteQuery = "DELETE FROM Books WHERE id=?";

  public void LoadAll(JTable table, ArrayList<Book> books) {

    Connection conn = ConnManager.connect();
    try (Statement prStmt = conn.createStatement(); ResultSet rs = prStmt.executeQuery(selectQuery)) {
      ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
      DefaultTableModel model = (DefaultTableModel) table.getModel();

      int cols = rsmd.getColumnCount();
      String[] colName = new String[cols];
      for (int i = 0; i < cols; i++) {
        colName[i] = rsmd.getColumnName(i + 1);
        model.setColumnIdentifiers(colName);
      }

      boolean borrowed;
      int tmp, id, year;
      String name, author, avaiable, book_type, genre;
      while (rs.next()) {
        Book book;
        id = rs.getInt(1);
        name = rs.getString(2);
        author = rs.getString(3);
        year = rs.getInt(4);
        tmp = rs.getInt(5);
        book_type = rs.getString(6);
        genre = rs.getString(7);

        if (tmp == 1) {
          avaiable = "yes";
          borrowed = true;
        } else {
          avaiable = "no";
          borrowed = false;
        }

        String[] row = { Integer.toString(id), name, author, Integer.toString(year), avaiable, book_type, genre };

        if (book_type.equals("NOVEL")) {

          book = new Novel(id, name, author, year, borrowed, Novel.Genre.valueOf(genre));
          books.add(book);

        } else {
          book = new TextBook(id, name, author, year, borrowed, TextBook.Grade.valueOf(genre));
          books.add(book);

        }

        model.addRow(row);

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void Delete(int id) {
    Connection conn = ConnManager.connect();
    try {

      PreparedStatement prepSt = conn.prepareStatement(deleteQuery);
      prepSt.setInt(1, id);
      prepSt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void SaveAll(ArrayList<Book> books) {
    Connection conn = ConnManager.connect();

    try {
      for (Book book : books) {
        PreparedStatement prepSt = conn.prepareStatement(insertQuery);
        prepSt.setInt(1, book.getId());
        prepSt.setString(2, book.getName());
        prepSt.setString(3, book.getAuthor());
        prepSt.setInt(4, book.getReleaseyear());
        if (book.getBorrowed()) {
          prepSt.setInt(5, 1);
        } else
          prepSt.setInt(5, 0);
        if (book instanceof Novel) {
          prepSt.setString(6, "NOVEL");
        } else
          prepSt.setString(6, "TEXTBOOK");

        if (book instanceof Novel) {
          prepSt.setString(7, ((Novel) book).getGenre().toString());
        } else
          prepSt.setString(7, ((TextBook) book).getGrade().toString());

        prepSt.executeUpdate();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}