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
        System.out.println("Connected to server...");
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

                    case "delete account":
                        deleteAccount();
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

                    case "delete file":
                        deleteFile();
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
        server.createAccount(remoteClient, userName, password);
    }

    private void deleteAccount() throws RemoteException {
        server.deleteAccount(remoteClient);
    }

    private void login() throws RemoteException {
        String userName = outputInput("Enter your username: ");
        String password = outputInput("Enter your password: ");
        server.login(remoteClient, userName, password);
    }

    private void logout() throws RemoteException {
        server.logout(remoteClient);
        System.out.println("Logged out your account");
    }

    private void list() throws RemoteException {
        List<String> fileList = server.listFiles(remoteClient);

        if(fileList != null) {
            System.out.println("Here's your files: ");

            for (String s : fileList) {
                System.out.println(s);
            }
        }
    }

    private void upload() throws RemoteException {
        String file = outputInput("Enter the name of the file you want to upload: ");
        String isPublic = outputInput("Do you want the file to be public? (Enter yes or no): ");

        String privilege = "Read & Write";

        if (isPublic.contains("yes")) {
            while (true) {
                privilege = outputInput("Enter a number for the privileges you want other people to have: 1) Read Only 2) Read & Write");
                if (privilege.equals("1")) {
                    privilege = "Read Only";
                    break;
                } else if (privilege.equals("2")) {
                    privilege = "Read & Write";
                    break;
                } else {
                    System.out.println("Wrong input");
                }
            }
        }

        server.uploadToDB(remoteClient, file, isPublic, privilege);
    }

    private void download() throws RemoteException {
        String file = outputInput("Enter the name of the file you want to download: ");
        server.downloadFromDB(remoteClient, file);
    }

    private void deleteFile() throws RemoteException {
        String fileName = outputInput("Enter the name of the file to be delete");
        server.deleteFile(remoteClient, fileName);
    }

    private String outputInput (String msg) {
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.print(msg);
        input = sc.nextLine();
        return input;
    }

    private class ServerOutput extends UnicastRemoteObject implements CatalogueClient {
        public int clientId =-1;

        protected ServerOutput() throws RemoteException { }

        @Override
        public void receiveMessage(String message) throws RemoteException {
            System.out.println(message);
        }

        public void setId (int id) throws RemoteException {
            clientId = id;
        }

        public int getId () throws RemoteException {
            return clientId;
        }
    }
}