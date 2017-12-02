package server.integration;

import java.sql.*;

public class DBConnector {
    private static String dburl = "jdbc:derby://localhost:1620/fileSystemDB;user=admin;password=admin";
    private static Connection connection = null;
    private static Statement statement = null;

    public DBConnector(){
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connection = DriverManager.getConnection(dburl);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void testPrint (){
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery("select * from accounts");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++) {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
            }

            results.close();
            statement.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public int getUserID(String name, String password) throws SQLException {
        statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select id from accounts where username = '" + name + "' AND password = '" + password + "'");

        int id = -1;

        if(results.next())
            id = results.getInt(1);

        results.close();
        statement.close();
        return id;
    }
}