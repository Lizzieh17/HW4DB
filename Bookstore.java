/*
 *  DO:  more $HOME/.my.cnf to see your MySQL username and  password
 *  CHANGE:  MYUSERNAME  and   MYMYSQLPASSWORD  in the main function of
 *  this program to your username and mysql password 
 *  MAKE SURE that you download mysql-connector-java-5.1.40-bin.jar from this assignment description on the class website
 *  COMPILE AND RUN: 
 *  javac *.java
 *  java -cp .:mysql-connector-java-5.1.40-bin.jar Bookstore
 *  */
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Bookstore {

    // The instance variables for the class
    private Connection connection;
    private Statement statement;

    // The constructor for the class
    public Bookstore() {
        connection = null;
        statement = null;
    }

    // The main program", that tests the methods
    public static void main(String[] args) throws SQLException {
        String Username = "seh051";
        String mysqlPassword = "Eiza0eiv";

        // Print menu
        System.out.println("Welcome to the Bookstore Database!");
        System.out.println("\nAvailable operations:");
        System.out.println("1) Find all available (not purchased) copies at a given bookstore");
        System.out.println("2) Purchase an available copy from a particular bookstore");
        System.out.println("3) List all purchases for a particular bookstore");
        System.out.println("4) Cancel a purchase");
        System.out.println("5) Add a new book for a bookstore");
        System.out.println("6) Quit");

        // Check if the user has provided the correct number of arguments
        if (args.length != 0) {
            System.out.println("This program does not take any command line arguments.");
            System.exit(1);
        }

        // Create a Bookstore instance and initialize the database
        Bookstore test = null;
        try {
            // Create a Bookstore instance and initialize the database
            test = new Bookstore();
            test.initDatabase(Username, mysqlPassword);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            System.exit(1);
        }

        // Prompt the user for input
        Scanner scanner = new Scanner(System.in); // Create a Scanner object to read user input
        System.out.print("Enter your choice: ");
        String userInput = scanner.nextLine();

        int bookID, purchaseID;
        String bookName, author, publicationDate, type, bookstoreName;

        // Process the input
        switch (userInput) {
            case "1":
                System.out.println("You selected: Find all available (not purchased) copies at a given bookstore");
                // Call the corresponding method here
                // Prompt for bookstore name
                System.out.print("Enter the bookstore name: ");
                bookstoreName = scanner.nextLine(); // Read the bookstore name from user input
                // Call the findCopies method with the provided bookstore name

                test.findCopies(bookstoreName);
                break;
            case "2":
                System.out.println("You selected: Purchase an available copy from a particular bookstore");
                // Call the corresponding method here
                System.out.println("Enter the bookstore name: ");
                bookstoreName = scanner.nextLine(); 
                System.out.println("Enter the book name: ");
                bookName = scanner.nextLine();

                test.purchcaseCopy(bookstoreName, bookName);
                break;
            case "3":
                System.out.println("You selected: List all purchases for a particular bookstore");
                // Call the corresponding method here
                System.out.println("Enter the bookstore name: ");
                bookstoreName = scanner.nextLine(); 

                test.listPurchases(bookstoreName);
                break;
            case "4":
                System.out.println("You selected: Cancel a purchase");
                // Call the corresponding method here
                System.out.println("Enter the purchase id: ");
                purchaseID = scanner.nextInt(); 

                test.cancelPurchase(purchaseID);
                break;
            case "5":
                System.out.println("You selected: Add a new book for a bookstore");
                // Call the corresponding method here
                System.out.println("Enter the book id: ");
                bookID = scanner.nextInt(); 
                scanner.nextLine();
                System.out.println("Enter the book name: ");
                bookName = scanner.nextLine(); 
                System.out.println("Enter the author: ");
                author = scanner.nextLine(); 
                System.out.println("Enter the publication date(YYYY-MM-DD): ");
                publicationDate = scanner.nextLine(); 
                System.out.println("Enter the book type: ");
                type = scanner.nextLine(); 

                test.addBook(bookID, bookName, author, publicationDate, type);
                break;
            case "6":
                System.out.println("You selected: Quit");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        // Disconnect
        test.disConnect();
        // Close the scanner
        scanner.close();
    }

    // Connect to the database
    public void connect(String Username, String mysqlPassword) throws SQLException {
        try {
            String url = "jdbc:mysql://localhost/" + Username + "?useSSL=false";
            System.out.println(url);
            connection = DriverManager.getConnection(url, Username, mysqlPassword);
        } catch (Exception e) {
            throw e;
        }
    }

    // Disconnect from the database
    public void disConnect() throws SQLException {
        connection.close();
        statement.close();
    }

    public String getCurrentTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(formatterTime);
        return formattedTime;
    }

    public String getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatteredDate = currentDate.format(formatterDate);
        return formatteredDate;
    }

    // Execute an SQL query passed in as a String parameter and print the resulting relation
    public void query(String q) {
        try {
            ResultSet resultSet = statement.executeQuery(q);
            System.out.println("---------------------------------");
            System.out.println("Query: \n" + q + "\n\nResult: ");
            print(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Print the results of a query with attribute names on the first line. Followed by the tuples, one per line
    public void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        printHeader(metaData, numColumns);
        printRecords(resultSet, numColumns);
    }

    // Print the attribute names
    public void printHeader(ResultSetMetaData metaData, int numColumns) throws SQLException {
        for (int i = 1; i <= numColumns; i++) {
            if (i > 1)
                System.out.print(",  ");
            System.out.print(metaData.getColumnName(i));
        }
        System.out.println();
    }

    // Print the attribute values for all tuples in the result
    public void printRecords(ResultSet resultSet, int numColumns) throws SQLException {
        String columnValue;
        while (resultSet.next()) {
            for (int i = 1; i <= numColumns; i++) {
                if (i > 1)
                    System.out.print(",  ");
                columnValue = resultSet.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }
    }

    // Insert into any table, any values from data passed in as String parameters
    public void insert(String table, String values) {
        String q = "INSERT into " + table + " values (" + values + ")";
        try {
            statement.executeUpdate(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find All available (not purchased) copies at a given bookstore
    public void findCopies(String bookstoreName) {
        // SQL query to find all available copies at a given bookstore
        String q = "SELECT b.bookID, b.bookName, c.copyID " +
                "FROM Book b " +
                "JOIN Copy c ON b.bookID = c.bookID " +
                "JOIN Bookstore bs ON c.bookstoreID = bs.bookstoreID " +
                "JOIN Purchase p ON c.copyID = p.copyID " + 
                "WHERE bs.bookstoreName = '" + bookstoreName + "'";
        query(q);
    }

    public void purchcaseCopy(String bookstoreName, String bookName){
        String q = null;
        query(q);
    }


    public void listPurchases(String bookstoreName){
        String q = null;
        query(q);
    }

    
    public void cancelPurchase(int purchaseID){
        String q = null;
        query(q);
    }

    public void addBook(int bookID, String bookName, String author, String publicationDate, String type){
        String q = null;
        query(q);
    }


    // init and testing - Assumes that the tables are already created
    public void initDatabase(String Username, String Password) throws SQLException {
        connect(Username, Password);
        // create a statement to hold mysql queries
        statement = connection.createStatement();
    }
}
