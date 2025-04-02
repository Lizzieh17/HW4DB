/*
 *  DO:  more $HOME/.my.cnf to see your MySQL username and  password
 *  CHANGE:  MYUSERNAME  and   MYMYSQLPASSWORD  in the main function of
 *  this program to your username and mysql password 
 *  MAKE SURE that you download mysql-connector-java-5.1.40-bin.jar from this assignment description on the class website
 *  COMPILE AND RUN: 
 *  javac *.java
 *  java -cp .:mysql-connector-java-5.1.40-bin.jar jdbc_example
 *  */

import java.sql.*;

public class jdbc_example {

    // The instance variables for the class
    private Connection connection;
    private Statement statement;

    // The constructor for the class
    public jdbc_example() {
        connection = null;
        statement = null;
    }

    // The main program", that tests the methods
    public static void main(String[] args) throws SQLException {
        String Username = "MYUSERNAME"; // Change to your own username
        String mysqlPassword = "MYMSQLPASSWORD"; // Change to your own mysql Password

        // Create a jdbc_example instance called test
        jdbc_example test = new jdbc_example();

        // Connect and set up the database for use
        test.initDatabase(Username, mysqlPassword);

        // Run a bunch of queries
        System.out.println("\nTesting select:");
        test.query("SELECT * FROM DEPT");

        System.out.println("\nTesting insert of dept MATH:");
        test.insert ("DEPT", "'MATH', 'Mathematics', 309, 'SCEN'"); 
        test.query("SELECT * FROM DEPT WHERE DEPT_CODE = 'MATH';");

        System.out.println("\nTesting delete of dept MATH:");
        test.statement.executeUpdate("DELETE FROM DEPT WHERE DEPT_CODE = 'MATH';");
        test.query("SELECT * FROM DEPT WHERE DEPT_CODE = 'MATH';");

        System.out.println("\nTesting update of professor name :");
        test.query("SELECT * FROM PROFESSOR WHERE PROF_ID = 123456;");
        test.statement.executeUpdate("Update PROFESSOR set PROF_NAME = 'Susan Dyer' WHERE PROF_ID = 123456;");
        test.query("SELECT * FROM PROFESSOR WHERE PROF_ID = 123456;");

        // Disconnect
        test.disConnect();
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

    // Execute an SQL query passed in as a String parameter
    // and print the resulting relation
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

    // Print the results of a query with attribute names on the first line
    // Followed by the tuples, one per line
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

    // init and testing
    // Assumes that the tables are already created
    public void initDatabase(String Username, String Password) throws SQLException {

        connect(Username, Password);
        // create a statement to hold mysql queries
        statement = connection.createStatement();
    }
}
