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

/* INSERCIÓN DE PARTICIPANTES */
INSERT INTO Tabla_Participante VALUES(Amateur(seqParticipante.NEXTVAL, 'Antonio', 'Gonzalez Urbano',
'66776644W', TO_DATE('1996-05-23', 'YYYY-MM-DD'), 'C/ Real nº 4', 'antonio@gmail.com', 
Lista_Ref_Ediciones(), EMPTY_BLOB()));

INSERT INTO Tabla_Participante VALUES(Amateur(seqParticipante.NEXTVAL, 'Diana', 'Mendoza Gomez',
'3344444Z', TO_DATE('1989-12-03', 'YYYY-MM-DD'), 'C/ Puente nº 2', 'diana@gmail.com', 
Lista_Ref_Ediciones(), EMPTY_BLOB()));

INSERT INTO Tabla_Participante VALUES(Profesional(seqParticipante.NEXTVAL, 'Sara', 'Diaz Perez',
'99999477K', TO_DATE('1999-03-12', 'YYYY-MM-DD'), 'C/ Gardenia nº 5', 'sara@gmail.com', 
Lista_Ref_Ediciones(), EMPTY_BLOB()));