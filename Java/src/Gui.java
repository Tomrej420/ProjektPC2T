package src;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import src.Books.Book;
import src.Books.Novel;
import src.Books.TextBook;
import src.Books.Novel.Genre;
import src.Books.TextBook.Grade;
import src.SQLI.Querries;

public class Gui extends JFrame implements ActionListener {

  private JButton addButton, editButton, deleteButton, markButton, listButtonAuthor, listButtonGenre,
      listButtonBorrowed,
      saveButton, loadButton;
  private JPanel btnpanel;
  private Font font = new Font("Verdana", Font.BOLD, 20);
  private Font labelfont = new Font("Verdana", Font.BOLD, 12);
  private DefaultTableModel model = new DefaultTableModel();
  private String[] type = { "NOVEL", "TEXTBOOK" };
  private JLabel id, name, author, year, borrowed, genrelabel, booktypelabel, availableLabel;

  private JTextField nametf, authortf, yeartf;
  private JButton[] buttons = new JButton[9];
  private JLabel[] labels = new JLabel[8];
  private JTable table = new JTable(model);
  private JComboBox<String> booktype = new JComboBox<>(type);
  private JComboBox<Enum> genre = new JComboBox<>();
  private JScrollPane sp = new JScrollPane(table);
  private JRadioButton jb = new JRadioButton();
  private ArrayList<Book> books = new ArrayList<>();

  public Gui() {

    super("Moje první okno");

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setSize(1400, 1000);
    this.setLocationRelativeTo(null);
    this.setLayout(null);
    this.setResizable(false);

    btnpanel = new JPanel();
    booktypelabel = new JLabel("Typ knihy:");
    genrelabel = new JLabel("Žánr knihy:");
    availableLabel = new JLabel("Dostupná:");
    name = new JLabel("Název knihy:");
    author = new JLabel("Jméno autora:");
    year = new JLabel("Rok vydání:");
    nametf = new JTextField();
    authortf = new JTextField();
    yeartf = new JTextField();
    id = new JLabel();
    borrowed = new JLabel();

    btnpanel.setLayout(new GridLayout(9, 1, 10, 10));

    btnpanel.setBounds(50, 100, 350, 750);
    sp.setBounds(450, 100, 875, 450);
    booktype.setBounds(450, 600, 120, 25);
    genre.setBounds(620, 600, 120, 25);
    booktypelabel.setBounds(450, 575, 150, 25);
    genrelabel.setBounds(620, 575, 150, 25);
    nametf.setBounds(760, 600, 150, 25);
    authortf.setBounds(930, 600, 150, 25);
    yeartf.setBounds(1100, 600, 60, 25);
    name.setBounds(760, 575, 150, 25);
    author.setBounds(930, 575, 150, 25);
    year.setBounds(1100, 575, 80, 25);
    jb.setBounds(1225, 595, 40, 40);
    availableLabel.setBounds(1200, 562, 75, 50);

    booktype.addActionListener(this);
    genre.addActionListener(this);

    this.add(btnpanel);
    this.add(sp);
    this.add(booktype);
    this.add(genre);
    this.add(genrelabel);
    this.add(booktypelabel);
    this.add(nametf);
    this.add(authortf);
    this.add(yeartf);
    this.add(name);
    this.add(author);
    this.add(year);
    this.add(jb);
    this.add(availableLabel);
    this.addWindowListener(null);

    addButton = new JButton("Přidat");
    editButton = new JButton("Upravit");
    deleteButton = new JButton("Smazat");
    markButton = new JButton("Vypůjčená");
    listButtonAuthor = new JButton("Výpis knih od autora");
    listButtonGenre = new JButton("Výpis knih ze žánru");
    listButtonBorrowed = new JButton("Výpis vypůjčených knih");
    saveButton = new JButton("Uložit knihu");
    loadButton = new JButton("Načíst knihu");

    buttons[0] = addButton;
    buttons[1] = editButton;
    buttons[2] = deleteButton;
    buttons[3] = markButton;
    buttons[4] = listButtonAuthor;
    buttons[5] = listButtonGenre;
    buttons[6] = listButtonBorrowed;
    buttons[7] = saveButton;
    buttons[8] = loadButton;

    labels[0] = id;
    labels[1] = name;
    labels[2] = author;
    labels[3] = year;
    labels[4] = borrowed;
    labels[5] = genrelabel;
    labels[6] = booktypelabel;
    labels[7] = availableLabel;
    for (int i = 0; i < labels.length; i++) {
      labels[i].setFont(labelfont);
    }

    for (int i = 0; i < buttons.length; i++) {
      buttons[i].setFont(font);
      buttons[i].setFocusable(false);
      buttons[i].addActionListener(this);
      buttons[i].putClientProperty("JButton.buttonType", "roundRect");

    }
    btnpanel.add(buttons[0]);
    btnpanel.add(buttons[1]);
    btnpanel.add(buttons[2]);
    btnpanel.add(buttons[3]);
    btnpanel.add(buttons[4]);
    btnpanel.add(buttons[5]);
    btnpanel.add(buttons[6]);
    btnpanel.add(buttons[7]);
    btnpanel.add(buttons[8]);

    table.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) {
        nametf.setText(books.get(table.getSelectedRow()).getName());
        authortf.setText(books.get(table.getSelectedRow()).getAuthor());
        yeartf.setText(String.valueOf(books.get(table.getSelectedRow()).getReleaseyear()));

        if (books.get(table.getSelectedRow()).getBorrowed()) {
          jb.setSelected(true);
        } else
          jb.setSelected(false);
      }
    });

    UpdateTable();
    this.setVisible(true);
  }

  public void UpdateTable() {
    model.setRowCount(0);
    Querries get = new Querries();
    get.LoadAll(table, books);
  }

  public void Add() {
    int lastid = (books.get(books.size() - 1)).getId();
    Book book;
    if (nametf.getText().equals("") || authortf.getText().equals("")) {
      JOptionPane.showMessageDialog(this, "Prosím vyplňte údaje");
    } else {

      String tmp;
      boolean btmp;
      if (jb.isSelected()) {
        tmp = "yes";
        btmp = true;
      } else
        tmp = "no";
      btmp = false;
      lastid = lastid + 1;
      String[] data = { String.valueOf(lastid), nametf.getText(), authortf.getText(), yeartf.getText(), tmp,
          booktype.getSelectedItem().toString(), genre.getSelectedItem().toString() };

      if (booktype.getSelectedItem() == "NOVEL") {
        book = new Novel(lastid, nametf.getText(), authortf.getText(), Integer.valueOf(yeartf.getText()), btmp,
            Novel.Genre.valueOf(genre.getSelectedItem().toString()));
        books.add(book);

      } else if (booktype.getSelectedItem() == "TEXTBOOK") {
        book = new TextBook(lastid, nametf.getText(), authortf.getText(), Integer.valueOf(yeartf.getText()), btmp,
            TextBook.Grade.valueOf(genre.getSelectedItem().toString()));
        books.add(book);
      }
      model.addRow(data);

    }

  }

  public void Edit() {
    if (nametf.getText().equals("") || authortf.getText().equals("")) {
      JOptionPane.showMessageDialog(this, "Prosím vyplňte údaje");
    } else {
      int row = table.getSelectedRow();
      books.get(row).setName(nametf.getText());
      books.get(row).setAuthor(authortf.getText());
      books.get(row).setReleaseyear(Integer.parseInt(yeartf.getText()));
      if (jb.isSelected()) {
        books.get(row).setBorrowed(true);
      } else
        books.get(row).setBorrowed(false);

      model.setValueAt(nametf.getText(), row, 1);
      model.setValueAt(authortf.getText(), row, 2);
      model.setValueAt(Integer.parseInt(yeartf.getText()), row, 3);
    }

  }

  public void Remove() {
    int row = table.getSelectedRow();
    model.removeRow(row);
    books.remove(row);
  }

  public void Burrow() {
    int row = table.getSelectedRow();
    if (jb.isSelected()) {
      model.setValueAt(true, row, 4);
      books.get(table.getSelectedRow()).setBorrowed(true);
    }

    if (!jb.isSelected()) {
      model.setValueAt(false, row, 4);
      books.get(table.getSelectedRow()).setBorrowed(false);
    }
  }

  public void SortByAuthor() {
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) table.getModel()));
    sorter.setRowFilter(RowFilter.regexFilter(authortf.getText()));

    if (authortf.getText().equals(null)) {
      table.setRowSorter(null);
    } else
      table.setRowSorter(sorter);
  }

  public void SortByGenre() {

    if (genre.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(this, "Prosím zvolte žánr");
    } else {
      TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) table.getModel()));
      sorter.setRowFilter(RowFilter.regexFilter(genre.getSelectedItem().toString()));

      if (genre.getSelectedItem().toString().equals(null)) {
        table.setRowSorter(null);
      } else
        table.setRowSorter(sorter);
    }
  }

  public void SortByBurrowed() {

    String filter;
    if (jb.isSelected()) {
      filter = "yes";
    } else
      filter = "no";

    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) table.getModel()));
    sorter.setRowFilter(RowFilter.regexFilter(filter));

    table.setRowSorter(sorter);
  }

  public void SaveToTXT() {

    try {
      File myFile = new File(books.get(table.getSelectedRow()).getName() + ".txt");
      if (myFile.createNewFile()) {
        FileWriter myWriter = new FileWriter(books.get(table.getSelectedRow()).getName() + ".txt");
        BufferedWriter bf = new BufferedWriter(myWriter);
        bf.write(String.valueOf(books.get(table.getSelectedRow()).getId()));
        bf.newLine();
        bf.write((books.get(table.getSelectedRow()).getName()));
        bf.newLine();
        bf.write((books.get(table.getSelectedRow()).getAuthor()));
        bf.newLine();
        bf.write(String.valueOf(books.get(table.getSelectedRow()).getReleaseyear()));
        bf.newLine();
        bf.write(books.get(table.getSelectedRow()).getBorrowed().toString());
        bf.newLine();
        if (books.get(table.getSelectedRow()) instanceof Novel) {
          bf.write(((Novel) books.get(table.getSelectedRow())).getGenre().toString());
          bf.newLine();
          bf.write("Novel");
          bf.close();
          myWriter.close();
          JOptionPane.showMessageDialog(this, "Soubor byl uložen");
        }
        if (books.get(table.getSelectedRow()) instanceof Novel) {
          bf.write(((TextBook) books.get(table.getSelectedRow())).getGrade().toString());
          bf.newLine();
          bf.write("Textbook");
          bf.close();
          myWriter.close();
          JOptionPane.showMessageDialog(this, "Soubor byl uložen");
        }

      } else {
        JOptionPane.showMessageDialog(this, "Soubor už existuje");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void LoadFromTXT() {

    Book book;
    try {
      JFileChooser fc = new JFileChooser();
      int response = fc.showOpenDialog(fc);
      if (response == JFileChooser.APPROVE_OPTION) {
        File file = new File(fc.getSelectedFile().getName());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        ArrayList<String> Lines = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
          Lines.add(line);
        }

        if (Lines.get(6).equals("Novel")) {
          book = new Novel(Integer.parseInt(Lines.get(0)), Lines.get(1), Lines.get(2), Integer.parseInt(Lines.get(3)),
              Boolean.parseBoolean(Lines.get(4)), Novel.Genre.valueOf(Lines.get(5)));
          books.add(book);
        } else
          book = new TextBook(Integer.parseInt(Lines.get(0)), Lines.get(1), Lines.get(2),
              Integer.parseInt(Lines.get(3)), Boolean.parseBoolean(Lines.get(4)), TextBook.Grade.valueOf(Lines.get(5)));
        books.add(book);

        String[] data = { Lines.get(0), Lines.get(1), Lines.get(2), Lines.get(3), Lines.get(4), Lines.get(6),
            Lines.get(5) };
        model.addRow(data);

        br.close();
        fr.close();

      }

    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

  }

  public void actionPerformed(ActionEvent event) {

    if (event.getSource() == booktype) {
      if (booktype.getSelectedItem() == "NOVEL") {
        genre.removeAllItems();

        for (Genre g : Genre.values()) {
          genre.addItem(g);
        }
      } else if (booktype.getSelectedItem() == "TEXTBOOK") {
        genre.removeAllItems();

        for (Grade g : Grade.values()) {
          genre.addItem(g);
        }
      }

    }

    if (event.getSource() == addButton) {
      Add();
      Querries sq = new Querries();
      sq.SaveAll(books);
    }

    if (event.getSource() == editButton) {
      Edit();
      Querries sq = new Querries();
      sq.SaveAll(books);
    }

    if (event.getSource() == deleteButton) {
      Querries sq = new Querries();
      sq.Delete(books.get(table.getSelectedRow()).getId());
      Remove();

    }
    if (event.getSource() == markButton) {
      Burrow();
      Querries sq = new Querries();
      sq.SaveAll(books);
    }
    if (event.getSource() == listButtonAuthor) {
      SortByAuthor();
    }
    if (event.getSource() == listButtonGenre) {
      SortByGenre();
    }
    if (event.getSource() == listButtonBorrowed) {
      SortByBurrowed();
    }
    if (event.getSource() == saveButton) {
      SaveToTXT();

    }
    if (event.getSource() == loadButton) {
      LoadFromTXT();
      Querries sq = new Querries();
      sq.SaveAll(books);
    }

  }

}
