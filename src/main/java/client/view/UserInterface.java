package client.view;

import common.CatalogueClient;
import common.CatalogueServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private CatalogueServer server;
    private CatalogueClient remoteClient;

    public UserInterface (CatalogueServer server) throws RemoteException {
        this.server = server;
        this.remoteClient = new ServerOutput();
        System.out.println("Connected to server");
        run();
    }

    void run () {
        while (true){
            try {
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();

                switch (userInput) {
                    case "create":
                        create();
                        break;

                    case "delete":
                        delete();
                        break;

                    case "login":
                        login();
                        break;

                    case "logout":
                        logout();
                        break;

                    case "list":
                        list();
                        break;

                    case "upload":
                        upload();
                        break;

                    case "download":
                        download();
                        break;

                    default:
                        System.out.println("Not a command");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void create() throws RemoteException {
        String userName = outputInput("Enter your new username: ");
        String password = outputInput("Enter your new password: ");
        server.createAccount(userName, password);
    }

    private void delete() throws RemoteException {
        server.deleteAccount();
        System.out.println("Deleted your account");
    }

    private void login() throws RemoteException {
        String userName = outputInput("Enter your username: ");
        String password = outputInput("Enter your password: ");
        server.login(remoteClient, userName, password);
    }

    private void logout() throws RemoteException {
        server.logout();
        System.out.println("Logged out your account");
    }

    private void list() throws RemoteException {
        List<String> fileList = server.listFiles();
        System.out.println("Here's your files: ");

        for (String s: fileList) {
            System.out.println(s);
        }
    }

    private void upload() throws RemoteException {
        server.uploadToDB();
    }

    private void download() throws RemoteException {
        String file = outputInput("Enter your username: ");
        server.downloadFromDB(file);
    }

    private String outputInput (String msg) {
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.print(msg);
        input = sc.nextLine();
        return input;
    }

    private class ServerOutput extends UnicastRemoteObject implements CatalogueClient {

        protected ServerOutput() throws RemoteException { }

        @Override
        public void receiveMessage(String message) throws RemoteException {
            System.out.println(message);
        }
    }
}