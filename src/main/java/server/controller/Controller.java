package server.controller;

import common.CatalogueClient;
import common.CatalogueServer;
import server.integration.DBConnector;
import server.integration.FileDownloadError;
import server.integration.FileUploadError;

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
    public void createAccount(CatalogueClient remoteClient, String name, String password) throws RemoteException {
        try {
            if(db.isUserNameTaken(name)) {
                remoteClient.receiveMessage("Username taken, try another name");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.createAccount(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        remoteClient.receiveMessage("You've created your account, please log in");
    }

    @Override
    public void deleteAccount(CatalogueClient remoteClient) throws RemoteException {
        if(!isLoggedIn(remoteClient))
            return;

        try {
            db.deleteAccount(remoteClient.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logout(remoteClient);
        remoteClient.receiveMessage("Your account has been deleted");
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
            remoteClient.receiveMessage("Hello " + name + "! You are now logged in");
            remoteClient.setId(id);
        } else {
            remoteClient.receiveMessage("Invalid username or password");
        }
        db.testPrint();
    }

    @Override
    public void logout(CatalogueClient remoteClient) throws RemoteException {
        remoteClient.setId(-1);
        remoteClient.receiveMessage("You are now logged out");
    }

    @Override
    public List<String> listFiles(CatalogueClient remoteClient) throws RemoteException {
        if (!isLoggedIn(remoteClient))
            return null;

        List<String> files = null;
        try {
            files = db.getFiles(remoteClient.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Override
    public void uploadToDB(CatalogueClient remoteClient, String fileName, String isPublic, String privilege) throws RemoteException {
        if (!isLoggedIn(remoteClient))
            return;

        //default is privacy is yes
        int isPrivateDB = 1;

        if (isPublic.equalsIgnoreCase("yes"))
            isPrivateDB = 0;

        try {
            db.upload(remoteClient.getId(), fileName, isPrivateDB, privilege);
            remoteClient.receiveMessage("Upload successful");
        } catch (FileUploadError | SQLException e) {
            remoteClient.receiveMessage("File " + fileName + " didn't upload");
        }
    }

    @Override
    public void downloadFromDB(CatalogueClient remoteClient, String fileName) throws RemoteException {
        if(!isLoggedIn(remoteClient))
            return;

        try {
            String fileMetaData = null;
            try {
                fileMetaData = db.download(remoteClient.getId(), fileName);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(fileMetaData.trim().length() > 0){
                remoteClient.receiveMessage("Here's your file: " + "\n" + fileMetaData);
            }
            else{
                remoteClient.receiveMessage("File is not found");
            }

        } catch (FileDownloadError e) {
            remoteClient.receiveMessage("File didn't download");
        }
    }

    @Override
    public void deleteFile(CatalogueClient remoteClient, String fileName) throws RemoteException {
        if(!isLoggedIn(remoteClient))
            return;

        try {
            db.deleteFile(fileName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        remoteClient.receiveMessage("file " + fileName + " has been deleted");
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
