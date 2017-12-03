package common;

// This file contains all the methods implemented in the server and are remotely called by the client

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CatalogueServer extends Remote {

    public void createAccount(CatalogueClient remoteClient, String name, String password) throws RemoteException;

    public void deleteAccount (CatalogueClient remoteClient) throws RemoteException;

    public void login(CatalogueClient remoteClient, String name, String password) throws RemoteException;

    public void logout (CatalogueClient remoteClient) throws RemoteException;

    public List<String> listFiles (CatalogueClient remoteClient) throws RemoteException;

    public void uploadToDB(CatalogueClient remoteClient, String fileName, String isPrivate, String privilege) throws RemoteException;

    public void downloadFromDB (CatalogueClient remoteClient, String fileName) throws RemoteException;

    void deleteFile(CatalogueClient remoteClient, String fileName) throws RemoteException;
}
