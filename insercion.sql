/* FICHERO PARA INSERCIÓN DE DATOS INICIALES EN LAS TABLAS */

/* INSERCIÓN DE VIDEOJUEGOS */
INSERT INTO Tabla_Videojuego VALUES(seqVideojuego.NEXTVAL, 'Call of Duty', 20.10, 2010, ListaDispositivos('XBOX', 'PS3'));
INSERT INTO Tabla_Videojuego VALUES(seqVideojuego.NEXTVAL, 'Minecraft', 15.99, 2015, ListaDispositivos('PS3', 'PS4'));
INSERT INTO Tabla_Videojuego VALUES(seqVideojuego.NEXTVAL, 'Elder Scrolls Online', 40.55, 2018, ListaDispositivos('PS4', 'PC', 'XBOX'));

/* INSERCIÓN DE ORGANIZADORES */
INSERT INTO Tabla_Organizador VALUES(seqOrganizador.NEXTVAL, 'Juan', 'Lopez Gomez', 
TO_DATE('1980-03-04','YYYY-MM-DD'), '11223344X',Lista_Ref_Ediciones());

INSERT INTO Tabla_Organizador VALUES(seqOrganizador.NEXTVAL, 'Maria', 'Jimenez Santos',
TO_DATE('1990-10-12','YYYY-MM-DD'), '22334455J',Lista_Ref_Ediciones());

INSERT INTO Tabla_Organizador VALUES(seqOrganizador.NEXTVAL, 'Francisco Jose', 'Ortega Perez',
TO_DATE('1995-07-04','YYYY-MM-DD'), '33445566R',Lista_Ref_Ediciones());

