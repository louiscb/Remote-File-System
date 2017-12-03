# Client-Server Remote File System

This project uses Remote Method Invocation to allow communication betweeen a client and server. (https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/)

It mimics a remote server file system that a client would login to and upload and download files. 

The database that stores information about accounts and files uses Derby. (https://db.apache.org/derby/) 

### How to Run
To create the database run the create-file-system-DB.sql

Then to start the database server run 'startNetworkServer -p 1620'

### Useful Commands:
startNetworkServer -p 1620

##### Example of sql command for our DB:
insert into accounts (username, password) values ('ahmad', '1234');
