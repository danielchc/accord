PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE usuarios(
	uuid CHAR(40),
	nomeUsuario VARCHAR(50),
	contrasinal VARCHAR(200)
);
CREATE TABLE amigos(
	u1 VARCHAR(40),
	u2 VARCHAR(40),
	aceptado BIT,
	PRIMARY KEY (u1,u2), 
	FOREIGN KEY (u1) REFERENCES usuarios(uuid),
	FOREIGN KEY (u2) REFERENCES usuarios(uuid)
);

COMMIT;
