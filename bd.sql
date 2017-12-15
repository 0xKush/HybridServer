drop DATABASE if EXISTS hstestdb;
drop DATABASE if EXISTS hstestdb1;
drop DATABASE if EXISTS hstestdb2;
drop DATABASE if EXISTS hstestdb3;
drop DATABASE if EXISTS hstestdb4;
################################# HSTESTDB ##################################################
create database hstestdb;
grant all privileges on hstestdb.* to hsdb@localhost identified by "hsdbpass";
use hstestdb;

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
    xds char(36),
    primary key (uuid) 
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

################################# HSTESTDB1 #################################################
create database hstestdb1;
grant all privileges on hstestdb1.* to hsdb@localhost identified by "hsdbpass";
use hstestdb1;

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
    xds char(36),
    primary key (uuid)
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

################################# HSTESTDB2 #################################################
create database hstestdb2;
grant all privileges on hstestdb2.* to hsdb@localhost identified by "hsdbpass";
use hstestdb2;

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
    xds char(36),
    primary key (uuid)
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

################################# HSTESTDB3 #################################################
create database hstestdb3;
grant all privileges on hstestdb3.* to hsdb@localhost identified by "hsdbpass";
use hstestdb3;

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
    xds char(36),
    primary key (uuid)
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;

################################# HSTESTDB4 #################################################
create database hstestdb4;
grant all privileges on hstestdb4.* to hsdb@localhost identified by "hsdbpass";
use hstestdb4;

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
    xds char(36),
    primary key (uuid)
) ENGINE=INNODB DEFAULT CHARACTER SET = utf8;