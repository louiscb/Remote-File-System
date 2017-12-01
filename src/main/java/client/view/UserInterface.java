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
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            switch (userInput){
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
        }
    }

    private void create() {
        Scanner sc = new Scanner(System.in);
        String userName;
        String password;
        System.out.println("Enter you username:");
        userName = sc.nextLine();
        System.out.println("Enter your password:");
        password = sc.nextLine();
        try {
            server.createAccount(userName, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void delete() {
        try {
            server.deleteAccount();
            System.out.println("Deleted your account");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        String userName;
        String password;
        System.out.println("Enter you username:");
        userName = sc.nextLine();
        System.out.println("Enter your password:");
        password = sc.nextLine();
        try {
            server.login(remoteClient, userName, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void logout() {
        try {
            server.logout();
            System.out.println("Logged out your account");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void list() {
        try {
            List<String> fileList = server.listFiles();
            System.out.println("Here's your files: ");
            for (String s: fileList) {
                System.out.println(s);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void upload() {
        try {
            server.uploadToDB();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private void download() {
        Scanner sc = new Scanner(System.in);
        String file;
        System.out.println("Enter the file you want to download:");
        file = sc.nextLine();
        try {
            server.downloadFromDB(file);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }

    private class ServerOutput extends UnicastRemoteObject implements CatalogueClient {

        protected ServerOutput() throws RemoteException {
        }

        @Override
        public void receiveMessage(String message) throws RemoteException {
            System.out.println(message);
        }
    }

}