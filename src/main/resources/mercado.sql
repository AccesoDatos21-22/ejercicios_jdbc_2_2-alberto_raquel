drop database if exists mercado;

create database mercado;

use mercado; 

drop table if exists proveedores;

create table proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));

insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');
insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');
insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');

create table CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));

insert into CAFES values('Colombian', 00101, 7.99, 0, 0);
insert into CAFES values('French_Roast', 00049, 8.99, 0, 0);
insert into CAFES values('Espresso', 00150, 9.99, 0, 0);
insert into CAFES values('Colombian_Decaf', 00101, 8.99, 0, 0);
insert into CAFES values('French_Roast_Decaf', 00049, 9.99, 0, 0);