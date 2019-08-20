
connect to INVDB;

create table containers
(
 containerid VARCHAR(30) NOT NULL,
 type VARCHAR(20),
 status VARCHAR(20),
 brand VARCHAR(50),
 capacity DECIMAL,
 CREATIONDATE TIMESTAMP,
 UPDATEDATE TIMESTAMP,
 PRIMARY KEY (containerid)
);


COMMIT;
