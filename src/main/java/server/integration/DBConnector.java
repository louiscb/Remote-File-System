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
                String priv = results.getString(5);
                String size = results.getString(6);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName + "\t\t" + cityName1 + "\t\t" + priv + "\t\t" + size);
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

    public void upload(int id, String fileName, int isPrivate, String privilege) throws FileUploadError, SQLException {
        statement = connection.createStatement();

        //Creating random file size as files don't really exist
        int size = (int) (Math.random() * 500);
        statement.execute("insert into files (owner_id, name, is_private, privilege, size) values ("+ id +", '"+ fileName +"', "+ isPrivate +", '" + privilege + "', " + size + ")");
        statement.close();
        testPrintTable();
    }

    public String download(int id, String fileName) throws FileDownloadError, SQLException {
        statement = connection.createStatement();
        StringBuilder stringBuilder = new StringBuilder();
        ResultSet results = statement.executeQuery("select * from files where (name = '" + fileName + "' AND owner_id = " + id + ") OR (name = '" + fileName + "' AND is_private = " + 0 + ")");

        if(results.next()) {
            stringBuilder.append("Owner: ").append(getOwnerName(results.getString(2))).append("\n");
            stringBuilder.append("File name: ").append(results.getString(3)).append("\n");
            stringBuilder.append("Permissions: ").append(getPermission(results.getString(4))).append("\n");
            stringBuilder.append("Privilege: ").append(results.getString(5)).append("\n");
            stringBuilder.append("Size: ").append(results.getString(6)).append("MB").append("\n");

        }

        statement.close();
        System.out.println(String.valueOf(stringBuilder));
        return String.valueOf(stringBuilder);
    }

    private String getPermission(String permission) {
        if(permission.equals(0)){
            return "Public";
        }
        return "Private";
    }

    private String getOwnerName (String ownerId ) throws SQLException{
        Statement st = connection.createStatement();
        ResultSet results = st.executeQuery( "select username from accounts where id = " + ownerId);
        String ownerName = null;
        if(results.next()){
            ownerName = results.getString(1);
        }
        st.close();
        return ownerName;
    }


    public void deleteFile(String fileName) throws SQLException{
        Statement st = connection.createStatement();
        st.execute("delete from files where name = '" + fileName + "'");
        st.close();
        testPrint();
    }
}