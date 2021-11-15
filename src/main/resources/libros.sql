drop database if exists libreria;

create database libreria;

use libreria; 

create table libros (
   isbn integer not null,
   titulo varchar(50) not null,
   autor varchar(50) not null,
   editorial varchar(25) not null,
   paginas integer not null,
   copias integer not null,
   constraint isbn_pk primary key (isbn)
);

insert into libros values(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3);
insert into libros values(12453,"Minix","Stallings","Informatica",345,4);
insert into libros values(1325,"Linux","Richard Stallman","FSF",168,10);
insert into libros values(1725,"Java","Juan Garcia","Programacion",245,9);