package server.startup;

import server.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {

    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.startRegistry();
            Naming.bind("FILE_SERVER", new Controller());
        } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
            System.out.print(e);
        }
    }

    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException e) {
            //if we cant locate our registry we create it
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }

}
