package server.controller;

import common.CatalogueClient;
import common.CatalogueServer;
import server.integration.DBConnector;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class Controller extends UnicastRemoteObject implements CatalogueServer {
    private final DBConnector db;

    public Controller() throws RemoteException {
        super();
        System.out.println("Server Running...");
        db = new DBConnector();
    }

    @Override
    public void createAccount(String name, String password) throws RemoteException {
        System.out.println(name + " , " + password);
    }

    @Override
    public void deleteAccount() throws RemoteException {

    }

    @Override
    public void login(CatalogueClient remoteClient, String name, String password) throws RemoteException {
        int id = 0;

        try {
            id = db.getUserID(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id > 0) {
            remoteClient.receiveMessage("You are logged in");
            remoteClient.setId(id);
        } else {
            remoteClient.receiveMessage("Invalid username or password");
        }
    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public List<String> listFiles(CatalogueClient remoteClient) throws RemoteException {
        if (!isLoggedIn(remoteClient))
            return null;

        //code

        return null;
    }

    @Override
    public boolean status() throws RemoteException {
        return false;
    }

    @Override
    public void uploadToDB() throws RemoteException {
    }

    @Override
    public void downloadFromDB( String fileName) throws RemoteException {
    }

    private boolean isLoggedIn(CatalogueClient remoteClient) throws RemoteException {
        if (remoteClient.getId() == -1) {
            remoteClient.receiveMessage("You need to login first to perform this command");
            return false;
        } else {
            return true;
        }
    }

}
