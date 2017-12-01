package server.controller;

import common.CatalogueClient;
import common.CatalogueServer;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Controller extends UnicastRemoteObject implements CatalogueServer {

    public Controller() throws RemoteException {
        System.out.println("Server Connected");
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
        remoteClient.receiveMessage("Your a bitch");
    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public List<String> listFiles() throws RemoteException {
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
    public void downloadFromDB(String fileName) throws RemoteException {

    }
}
