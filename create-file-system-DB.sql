connect 'jdbc:derby://localhost:1620/fileSystemDB;create=true;user=admin;password=admin';
CREATE TABLE accounts(ID integer GENERATED ALWAYS AS IDENTITY NOT NULL primary key, username varchar(20), password varchar(20));
CREATE TABLE files(ID integer GENERATED ALWAYS AS IDENTITY NOT NULL primary key, owner_id integer, name varchar(40), is_private integer, privilege varchar(20), size integer;
disconnect;
exit;