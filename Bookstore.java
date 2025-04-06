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
    private static Scanner scanner;


    // The constructor for the class
    public Bookstore() {
        connection = null;
        statement = null;
        scanner = new Scanner(System.in);
    }

    // The main program", that tests the methods
    public static void main(String[] args) throws SQLException {
        // String Username = "lal013";
        // String mysqlPassword = "ooveiz0M";
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
        boolean running = true;
        System.out.print("Enter your choice: ");
        String userInput = scanner.nextLine();
        String bookName, bookstoreName, city;

        // Process the input
        while(running){
            switch (userInput){
                case "1":
                    System.out.println("You selected: Find all available (not purchased) copies at a given bookstore");

                    System.out.println("Enter the bookstore name: ");
                    bookstoreName = scanner.nextLine();
                    while (bookstoreName.isEmpty()) {
                        System.out.println("Bookstore name cannot be empty. Please try again.");
                        System.out.print("Enter the bookstore name: ");
                        bookstoreName = scanner.nextLine();
                    }
                    if (bookstoreName.contains("'")) {
                        bookstoreName = bookstoreName.replace("'", "''"); // Handle single quotes in input to avoid SQL injection
                    }
                    System.out.println("Enter the city: ");
                    city = scanner.nextLine(); 
                    while (city.isEmpty()) {
                        System.out.println("City cannot be empty. Please try again.");
                        System.out.print("Enter the city: ");
                        city = scanner.nextLine();
                    }
                    test.findCopies(bookstoreName,city);
                    break;
                case "2":
                    System.out.println("You selected: Purchase an available copy from a particular bookstore");

                    System.out.println("Enter the book name: ");
                    bookName = scanner.nextLine();
                    while (bookName.isEmpty()) {
                        System.out.println("Book name cannot be empty. Please try again.");
                        System.out.print("Enter the book name: ");
                        bookName = scanner.nextLine();
                    }

                    test.purchcaseCopy(bookName);
                    break;
                case "3":
                    System.out.println("You selected: List all purchases for a particular bookstore");

                    System.out.println("Enter the bookstore name: ");
                    bookstoreName = scanner.nextLine(); 
                    while (bookstoreName.isEmpty()) {
                        System.out.println("Bookstore name cannot be empty. Please try again.");
                        System.out.print("Enter the bookstore name: ");
                        bookstoreName = scanner.nextLine();
                    }
                    if (bookstoreName.contains("'")) {
                        bookstoreName = bookstoreName.replace("'", "''"); // Handle single quotes in input to avoid SQL injection
                    }
                    System.out.println("Enter the city: ");
                    city = scanner.nextLine(); 
                    while (city.isEmpty()) {
                        System.out.println("City cannot be empty. Please try again.");
                        System.out.print("Enter the city: ");
                        city = scanner.nextLine();
                    }

                    test.listPurchases(bookstoreName, city);
                    break;
                case "4":
                    System.out.println("You selected: Cancel a purchase");

                    test.cancelPurchase();
                    break;
                case "5":
                    System.out.println("You selected: Add a new book for a bookstore");

                    System.out.println("Enter the book store name: ");
                    bookstoreName = scanner.nextLine();
                    while (bookstoreName.isEmpty()) {
                        System.out.println("Bookstore name cannot be empty. Please try again.");
                        System.out.print("Enter the bookstore name: ");
                        bookstoreName = scanner.nextLine();
                    }
                    if (bookstoreName.contains("'")) {
                        bookstoreName = bookstoreName.replace("'", "''"); // Handle single quotes in input to avoid SQL injection
                    }
                    System.out.println("Enter the city: ");
                    city = scanner.nextLine(); 
                    while (city.isEmpty()) {
                        System.out.println("City cannot be empty. Please try again.");
                        System.out.print("Enter the city: ");
                        city = scanner.nextLine();
                    }

                    test.addBook(bookstoreName, city);
                    break;
                case "6":
                    System.out.println("You selected: Quit");
                    running = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            System.out.println("\n-------------------------------------------");
            System.out.println("Welcome to the Bookstore Database!");
            System.out.println("\nAvailable operations:");
            System.out.println("1) Find all available (not purchased) copies at a given bookstore");
            System.out.println("2) Purchase an available copy from a particular bookstore");
            System.out.println("3) List all purchases for a particular bookstore");
            System.out.println("4) Cancel a purchase");
            System.out.println("5) Add a new book for a bookstore");
            System.out.println("6) Quit");

            System.out.print("Enter your choice: ");
            userInput = scanner.nextLine();
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
            if (!resultSet.next()) { // Handle the case where no results are returned
                System.out.println("No results found.");
                return;
            }
            System.out.println("Query: \n" + q + "\n\nResult: ");
            print(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String q) {
        try {
            int result = statement.executeUpdate(q);
            System.out.println("---------------------------------");
            System.out.println("Query: \n" + q + "\n\nResult: ");
            System.out.println(result + " rows were affected");
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
            if (i > 1) System.out.print(" | "); // Add a separator between columns
            System.out.printf("%-20s", metaData.getColumnName(i)); // Left-align with a fixed width
        }
        System.out.println();
        System.out.println("-".repeat(numColumns * 23)); // Add a separator line
    }
    
    // Print the attribute values for all tuples in the result
    public void printRecords(ResultSet resultSet, int numColumns) throws SQLException {
        for (int i = 1; i <= numColumns; i++) {
            if (i > 1) System.out.print(" | "); // Add a separator between columns
            String columnValue = resultSet.getString(i);
            System.out.printf("%-20s", columnValue != null ? columnValue : "NULL"); // Left-align with a fixed width
        }
        System.out.println();
        while (resultSet.next()) {
            for (int i = 1; i <= numColumns; i++) {
                if (i > 1) System.out.print(" | "); // Add a separator between columns
                String columnValue = resultSet.getString(i);
                System.out.printf("%-20s", columnValue != null ? columnValue : "NULL"); // Left-align with a fixed width
            }
            System.out.println();
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
    public void findCopies(String bookstoreName, String city) {
        String q = "SELECT Book.bookName, Copy.copyID, Copy.price " + 
                        "FROM Book " +
                        "JOIN Copy ON Book.bookID = Copy.bookID " +
                        "JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID " +
                        "LEFT JOIN Purchase ON Copy.copyID = Purchase.copyID " + 
                        "WHERE Bookstore.bookstoreName = '" + bookstoreName + "' " +
                        "AND Bookstore.city = '" + city + "' " +
                        "AND Purchase.copyID IS NULL;";
        query(q);
    }

    public void purchcaseCopy(String bookName){        
        if(!this.availableBooksExists(bookName)){
            System.out.println("    This book isn't available or does not exist in the database.");
            return;
        } else {
            int copyID;

            this.getAllAvailableBooks(bookName);
            System.out.println("Please put copy id you want to purchase: ");
            copyID = scanner.nextInt();
            scanner.nextLine();

            this.addPurchase(copyID);
        }
    }

    public void listPurchases(String bookstoreName, String city){
        if(!this.bookstoreExists(bookstoreName, city)){
            System.out.println("    Couldn't find bookstore with name: " + bookstoreName + " in city: " + city + ".");
            return;
        }
        else {
            System.out.println("Listing all purchases from " + bookstoreName + " in " + city + "...");
            String q = "SELECT Book.bookName, Copy.price, Purchase.date, Purchase.time " + 
                            "FROM Purchase " +
                            "JOIN Copy ON Purchase.copyID = Copy.copyID " + 
                            "JOIN Book ON Copy.bookID = Book.bookID " + 
                            "JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID " +
                            "WHERE bookstoreName = '" + bookstoreName + "' AND city = '" + city + "';";
            query(q);            
        }
    }
    
    public void cancelPurchase(){
        int purchaseID;
        String q1 = "SELECT Purchase.purchaseID, Book.bookName, Bookstore.bookstoreName, Purchase.date, Purchase.time " + 
                            "FROM Purchase " +
                            "JOIN Copy ON Purchase.copyID = Copy.copyID " + 
                            "JOIN Book ON Copy.bookID = Book.bookID " + 
                            "JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID ";
        query(q1);

        System.out.println("Please put purchase ID you would like to cancel:");
        purchaseID = scanner.nextInt();
        scanner.nextLine();

        if(this.purchaseExists(purchaseID)){
            String q2 = "DELETE FROM Purchase WHERE purchaseID = " +  purchaseID + ";";
            update(q2);
        } else {
            // If the purchase ID does not exist, print an error message
            System.out.println("    Couldn't find purchase with ID: " + purchaseID + ".");
            return;
        }
    }

    public void addBook(String bookstoreName, String city){
        if(!this.bookstoreExists(bookstoreName, city)){
            System.out.println("    Couldn't find bookstore with name: " + bookstoreName + " in city: " + city + ".");
            return;
        }
        else {
            double price;
            int bookID, copyID;
            int bookstoreID = this.getBookstoreID(bookstoreName, city);
            String bookName, author, publicationDate, type;
            
            System.out.println("Please put the book you want to add:");
            System.out.println("Enter the book name: ");
            bookName = scanner.nextLine(); 
            System.out.println("Enter the author: ");
            author = scanner.nextLine(); 
            System.out.println("Enter the publication date(YYYY-MM-DD): ");
            publicationDate = scanner.nextLine(); 
            System.out.println("Enter the book type(non or fic): ");
            type = scanner.nextLine(); 
            System.out.println("Enter the copy price: ");
            price = scanner.nextDouble();
            System.out.println("Enter the book id: ");
            bookID = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the copy id: ");
            copyID = scanner.nextInt();
            scanner.nextLine();
            scanner.nextLine();

            String q1 = "INSERT INTO Book (bookID, bookName, author, publicationDate, type) " +
                        "VALUES(" + bookID + ", '" + bookName + "', '" + author + "', '" + publicationDate + "', '" + type + "')";
            update(q1);
            
            String q2 = "INSERT INTO Copy (copyID, bookstoreID, bookID, price) " +
                        "VALUES(" + copyID + ", " + bookstoreID + ", " + bookID + ", " + price + ")";
            update(q2);

            // Print success message
            System.out.println("Successfully added the book: " + bookName + " with Book ID: " + bookID + " and Copy ID: " + copyID + 
                " to the bookstore: " + bookstoreName + " in city: " + city + ".");
            //book table
            String q3 = "SELECT * FROM Book;";
            query(q3); // to show the book table after insertion
            String q4 = "SELECT * FROM Copy;";
            query(q4); // to show the copy table after insertion
        }
    }

    public void addPurchase(int copyID){
        if(!this.copyExists(copyID) && !this.availableCopyExists(copyID)){
            // If the copy ID does not exist or is not available for purchase, print an error message
            System.out.println("    Couldn't find copy with ID: " + copyID + " or it is not available for purchase.");
            return;
        }
        else{
            int newPurchaseId = 0;
            
            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(purchaseId) FROM Purchase");
            ){
                if (resultSet.next()) {
                    newPurchaseId = resultSet.getInt(1) + 1;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return;
            }

            String q = "INSERT INTO Purchase (purchaseId, copyID, date, time) " +
                        "VALUES(" + newPurchaseId + ", " + copyID + ", '" + this.getCurrentDate() + "', '" + this.getCurrentTime() + "')";
            update(q);
        }
    }

    public boolean bookstoreExists(String bookstoreName, String city){
        String q = "SELECT COUNT(*) FROM Bookstore WHERE bookstoreName = '" + bookstoreName + "' AND city = '" + city + "';";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
            else{
                System.out.println("Couldn't find bookstore");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't find bookstore");
            return false;
        }
    }

    public boolean purchaseExists(int purchaseID){
        String q = "SELECT COUNT(*) FROM Purchase WHERE purchaseID = " + purchaseID + ";";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
            else{
                System.out.println("Couldn't find purchase");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't find purchase");
            return false;
        }
    }

    public boolean copyExists(int copyID){
        String q = "SELECT COUNT(*) FROM Copy WHERE copyId = " + copyID + ";";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
            else{
                System.out.println("Couldn't find copy");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't find copy");
            return false;
        }
    }

    public boolean availableBooksExists(String bookName){
        String q = "SELECT COUNT(*) " +
                        "FROM Book " +
                        "JOIN Copy ON Copy.bookID = Book.bookID " +
                        "LEFT JOIN Purchase ON Purchase.copyID = Copy.copyID " +
                        "WHERE bookName = '" + bookName + "' " + 
                        "AND Purchase.copyID IS NULL;";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
            else{
                System.out.println("This book is not available");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("This book is not available");
            return false;
        }
    }

    public boolean availableCopyExists(int copyID){
        String q = "SELECT COUNT(*) " +
                        "FROM Copy " +
                        "LEFT JOIN Purchase ON Purchase.copyID = Copy.copyID " +
                        "WHERE Copy.copyID = " + copyID +  
                        " AND Purchase.copyID IS NULL;";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
            else{
                System.out.println("This copy is not available");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("This copy is not available");
            return false;
        }
    }

    public void getAllAvailableBooks(String bookName){
        String q = "SELECT Copy.copyID, Bookstore.bookstoreName, Bookstore.city, Copy.price " +
                        "FROM Book "+
                        "JOIN Copy ON Copy.bookID = Book.bookID " +
                        "JOIN Bookstore ON Bookstore.bookstoreID = Copy.bookstoreID " +
                        "LEFT JOIN Purchase ON Purchase.copyID = Copy.copyID " +
                        "WHERE Book.bookName = '" + bookName + "' AND Purchase.copyID IS NULL;";
        query(q);
    }

    public int getBookstoreID(String bookstoreName, String city){
        String q = "SELECT bookstoreID FROM Bookstore WHERE bookstoreName = '" + bookstoreName + "' AND city = '" + city + "';";
        try {
            ResultSet resultSet = statement.executeQuery(q);
            if(resultSet.next()){
                return resultSet.getInt("bookstoreID");
            }
            else{
                System.out.println("Couldn't find bookstoreID");
                return -1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    // init and testing - Assumes that the tables are already created
    public void initDatabase(String Username, String Password) throws SQLException {
        connect(Username, Password);
        // create a statement to hold mysql queries
        statement = connection.createStatement();
    }
}
