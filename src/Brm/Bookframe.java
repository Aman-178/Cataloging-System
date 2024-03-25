package Brm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class Bookframe {
    Connection con;
    PreparedStatement ps;
    JFrame frame=new JFrame("Book Management System project");
    JTabbedPane tabbedPane=new JTabbedPane();
    JPanel insertpanel;
    JPanel    viewpanel;
    JLabel l1,l2,l3,l4,l5;
    JTextField t1,t2,t3,t4,t5;
    JButton SaveButton;

    JTable table;
    JScrollPane scrollpane;
    JButton updateButton; // Declare as a class-level field
    JButton deleteButton;
    DefaultTableModel tm;
    String[]colname={"Book Id","Tittle","Price","Author","Publisher"};
    public Bookframe(){
        getConnectionFromMysql();
        intiComponents();

    }
    void getConnectionFromMysql() {
        String url = "jdbc:mysql://localhost:3306/school";
        try {
            con = DriverManager.getConnection(url, "root", "123@Mysql");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void intiComponents(){
        //Components for Insert Form;
        l1=new JLabel();
        l1.setText("Book Id");
        l2=new JLabel();
        l2.setText("Tittle");
        l3=new JLabel();
        l3.setText("Price");
        l4=new JLabel();
        l4.setText("Author");
        l5=new JLabel();
        l5.setText("Publisher");
        t1=new JTextField();
        t2=new JTextField();
        t3=new JTextField();
        t4=new JTextField();
        t5=new JTextField();
        SaveButton=new JButton("Save");
        l1.setBounds(100,100,100,20);
        l2.setBounds(100,150,100,20);
        l3.setBounds(100,200,100,20);
        l4.setBounds(100,250,100,20);
        l5.setBounds(100,300,100,20);
        t1.setBounds(250,100,100,20);
        t2.setBounds(250,150,100,20);
        t3.setBounds(250,200,100,20);
        t4.setBounds(250,250,100,20);
        t5.setBounds(250,300,100,20);
        SaveButton.setBounds(100,350,100,30);
        //saveButton event handling we have to do.
        SaveButton.addActionListener(new InsertBookRecord());
        insertpanel=new JPanel();
        insertpanel.setLayout(null);
        insertpanel.add(l1);
        insertpanel.add(l2);
        insertpanel.add(l3);
        insertpanel.add(l4);
        insertpanel.add(l5);
        insertpanel.add(t1);
        insertpanel.add(t2);
        insertpanel.add(t3);
        insertpanel.add(t4);
        insertpanel.add(t5);

        insertpanel.add(SaveButton);

        ArrayList<Book>booklist=fetchBookRecord();
        setDataOnTable(booklist);
        updateButton= new JButton("Update Book");
        updateButton.addActionListener(new UpdateBookRecord());

        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(new DeleteBookRecord());
        viewpanel=new JPanel();
        viewpanel.add(updateButton);
        viewpanel.add(deleteButton);
        scrollpane=new JScrollPane(table);
        viewpanel.add(scrollpane);


        tabbedPane.add(viewpanel);
        tabbedPane.add(insertpanel);
        tabbedPane.addChangeListener(new TabChangeHandler());

        frame.add(tabbedPane);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);






    }
    void setDataOnTable(ArrayList<Book>booklist){
        Object[][]obj=new Object[booklist.size()][5];
        for(int i=0;i< booklist.size();i++){
            obj[i][0]=booklist.get(i).getBookId();
            obj[i][1]=booklist.get(i).getTittle();
            obj[i][2]=booklist.get(i).getprice();
            obj[i][3]=booklist.get(i).getAuthor();
            obj[i][4]=booklist.get(i).getPublisher();
        }
        table=new JTable();
        tm=new DefaultTableModel();
        tm.setColumnCount(5);
        tm.setRowCount(booklist.size());
        tm.setColumnIdentifiers(colname);
        for(int i=0;i<booklist.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);
    }

    ArrayList<Book> fetchBookRecord(){
        ArrayList<Book>booklist=new ArrayList<Book>();
        String q="select*from book";
        try{
         ps=con.prepareStatement(q);
          ResultSet rs=ps.executeQuery();
          while(rs.next()){
              Book b=new Book();
              b.setBookId(rs.getInt(1));
              b.setTittle(rs.getString(2));
              b.setprice(rs.getDouble(3));
              b.setAuthor(rs.getString(4));
              b.setPublisher(rs.getString(5));
              booklist.add(b);
          }
        }catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        finally {
            return booklist;
        }
     }
    class InsertBookRecord implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Book b1 = readFromData();
            String q = "INSERT into Book (bookId, tittle, price, author, publisher) VALUES (?, ?, ?, ?, ?)";
            try {
                ps = con.prepareStatement(q);
                ps.setInt(1, b1.getBookId());
                ps.setString(2, b1.getTittle());
                ps.setDouble(3, b1.getprice());
                ps.setString(4, b1.getAuthor());
                ps.setString(5, b1.getPublisher());
                ps.execute();  // Use executeUpdate for INSERT queries
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
                t5.setText("");
            } catch (SQLException ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        }

        Book readFromData() {
            Book b1 = new Book();
            b1.setBookId(Integer.parseInt(t1.getText()));
            b1.setTittle(t2.getText());
            b1.setprice(Double.parseDouble(t3.getText()));
            b1.setAuthor(t4.getText());
            b1.setPublisher(t5.getText());

            // Validate and parse the price field
            try {
                b1.setprice(Double.parseDouble(t3.getText()));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid double
                System.out.println("Invalid price input. Please enter a valid number.");
            }

            return b1;
        }
    }
    void updateTable(ArrayList<Book>booklist){
        Object[][]obj=new Object[booklist.size()][5];
        for(int i=0;i< booklist.size();i++){
            obj[i][0]=booklist.get(i).getBookId();
            obj[i][1]=booklist.get(i).getTittle();
            obj[i][2]=booklist.get(i).getprice();
            obj[i][3]=booklist.get(i).getAuthor();
            obj[i][4]=booklist.get(i).getPublisher();
        }


        tm.setRowCount(booklist.size());

        for(int i=0;i<booklist.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);
    }

    class TabChangeHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            int index = tabbedPane.getSelectedIndex();
            if (index == 0) {
                System.out.println("insert");
            }
            if (index == 1) {
                ArrayList<Book> booklist = fetchBookRecord();
                updateTable(booklist);
            }
        }
    }

    class UpdateBookRecord implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Book> updatedbooklist = readTableData();
            String q = "update Book set tittle=?, price=?, author=?, publisher=? where bookId=?";
            try {
                ps = con.prepareStatement(q);
                for (int i = 0; i < updatedbooklist.size(); i++) {
                    Book book = updatedbooklist.get(i);
                    ps.setString(1, book.getTittle());
                    ps.setDouble(2, book.getprice());
                    ps.setString(3, book.getAuthor());
                    ps.setString(4, book.getPublisher());
                    ps.setInt(5, book.getBookId());
                    ps.addBatch();  // Add the batch
                }
                ps.executeBatch();  // Execute the batch
            } catch (SQLException ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        }

        ArrayList<Book> readTableData() {
            ArrayList<Book> updateBookList = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                Book book = new Book();
                book.setBookId(Integer.parseInt(table.getValueAt(i, 0).toString()));
                book.setTittle(table.getValueAt(i, 1).toString());
                book.setprice(Double.parseDouble(table.getValueAt(i, 2).toString()));
                book.setAuthor(table.getValueAt(i, 3).toString());
                book.setPublisher(table.getValueAt(i, 4).toString());
                updateBookList.add(book);
            }
            return updateBookList;
        }
    }

    class DeleteBookRecord implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int rowNo = table.getSelectedRow();
            if (rowNo != -1) {
                int id = (int) table.getValueAt(rowNo, 0);
                String q = "delete from book where bookId=?";
                try {
                    ps = con.prepareStatement(q);
                    ps.setInt(1, id);
                    ps.execute();
                } catch (SQLException ex) {
                    System.out.println("Exception:" + ex.getMessage());
                } finally {
                    ArrayList<Book> booklist = fetchBookRecord();
                    updateTable(booklist);
                }
            }
        }
    }

}
