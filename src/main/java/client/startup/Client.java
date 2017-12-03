package client.startup;

import client.view.UserInterface;
import common.CatalogueServer;

import java.net.MalformedURLException;
import java.rmi.*;

public class Client {
    public static void main(String[] args) {
        try {
            //If you want to connect to a server that is on your local wifi, supply your local ip:
            //CatalogueServer server = (CatalogueServer) Naming.lookup("rmi://localipaddresshere/FILE_SERVER");
            CatalogueServer server = (CatalogueServer) Naming.lookup("FILE_SERVER");
            UserInterface ui = new UserInterface(server);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Couldn't find server");
            e.printStackTrace();
        }
    }
}