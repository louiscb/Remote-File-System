package server.integration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void testPrintTable (){
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery("select * from files");
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
                String cityName1 = results.getString(4);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName + "\t\t" + cityName1);
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

    public boolean isUserNameTaken(String name) throws SQLException {
        String str = null;
        statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select username from accounts where username  = '" + name + "' ");
        if(results.next())
            str = results.getString(1);
        results.close();
        statement.close();

        if(str!=null)
            return true;

        else
            return false;

    }

    public void createAccount(String name, String password) throws SQLException {
        statement = connection.createStatement();
        statement.execute("insert into accounts (username, password) values ('" + name + "', '" + password +"')");
        statement.close();
        testPrint();
    }

    public void deleteAccount(int id) throws SQLException{
        statement = connection.createStatement();
        statement.execute("delete from accounts where id = " + id);
        statement.close();
        testPrint();
    }

    public List<String> getFiles(int id) throws SQLException{
        List<String> list = new ArrayList<String>();
        statement= connection.createStatement();
        ResultSet results = statement.executeQuery("select name from files where owner_id = " + id +  " OR is_private = 0");
        while(results.next()){
            list.add(results.getString(1));
        }
        statement.close();
        return list;
    }

    public void upload(int id, String fileName, int isPrivate) throws FileUploadError, SQLException {
        statement = connection.createStatement();
        statement.execute("insert into files (owner_id, name, is_private) values ("+ id +", '"+ fileName +"',"+ isPrivate +" ) ");
        statement.close();
    }

    public String download(int id, String fileName) throws FileDownloadError, SQLException {
        statement = connection.createStatement();
        StringBuilder stringBuilder = new StringBuilder();
        ResultSet results = statement.executeQuery("select * from files where (name = '" + fileName + "' AND owner_id = " + id + ") OR (name = '" + fileName + "' AND is_private = " + 0 + ")");
        if(results.next()){
            stringBuilder.append("Owner: ").append(results.getString(2)).append("\n");
            stringBuilder.append("File name: ").append(results.getString(3)).append("\n");
            stringBuilder.append("Permissions: ").append(results.getString(4)).append("\n");
        }
        statement.close();
        System.out.println(String.valueOf(stringBuilder));
        return String.valueOf(stringBuilder);
    }
}