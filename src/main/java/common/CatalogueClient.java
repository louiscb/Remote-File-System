package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

// This file contains all the methods implemented in the client and are remotely called by the server
public interface CatalogueClient extends Remote{
    void receiveMessage (String message) throws RemoteException;

    void setId (int id) throws RemoteException;

    int getId () throws RemoteException;
}
