package common;

// This file contains all the methods implemented in the server and are remotely called by the client

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CatalogueServer extends Remote{

    public void createAccount (String name , String password) throws RemoteException;

    public void deleteAccount () throws RemoteException;

    public void login(CatalogueClient remoteClient, String name, String password) throws RemoteException;

    public void logout () throws RemoteException;

    public List<String> listFiles () throws RemoteException;

    public boolean status() throws  RemoteException;

    public void uploadToDB () throws RemoteException;

    public void downloadFromDB ( String fileName) throws RemoteException;

}
