package client.startup;

import client.view.UserInterface;
import common.CatalogueServer;

import java.net.MalformedURLException;
import java.rmi.*;

public class Main {
    public static void main(String[] args) {
        try {
            CatalogueServer server = (CatalogueServer) Naming.lookup("FILE_SERVER");
            UserInterface ui = new UserInterface(server);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
