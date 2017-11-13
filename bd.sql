#Creación de la base de datos
    create database hstestdb;

#Crear un usuario para poder manipular la base de datos:
    grant all privileges on hstestdb.* to hsdb@localhost identified by "hsdbpass";

#Seleccionar la base de datos
    use hstestdb;

#Crear una tablas 

create table HTML ( 
    uuid char(36), 
    content text,
    primary key (uuid) 
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

create table XML ( 
    uuid char(36), 
    content text,
    primary key (uuid) 
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

create table XSD ( 
    uuid char(36), 
    content text,
    primary key (uuid) 
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

create table XSLT ( 
    uuid char(36), 
    content text,
    xds char(36)
    primary key (uuid) 
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;