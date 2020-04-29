/* FICHERO PARA BORRAR TODO */

DROP TYPE edicion FORCE;
DROP TYPE participante FORCE;
DROP TYPE organizador FORCE;
DROP TYPE torneo FORCE;
DROP TYPE videojuego FORCE;
DROP TYPE amateur FORCE;
DROP TYPE profesional FORCE;
DROP TYPE Lista_Ref_Ediciones FORCE;
DROP TYPE listadispositivos FORCE;
DROP TYPE lista_ref_participantes FORCE;
DROP TYPE lista_ref_organizadores FORCE;

DROP TABLE Tabla_Videojuego FORCE;
DROP TABLE Tabla_Amateur FORCE;
DROP TABLE Tabla_Profesional FORCE;
DROP TABLE Tabla_Organizador FORCE;
DROP TABLE Tabla_Torneo FORCE;
DROP TABLE Tabla_Edicion FORCE;
DROP SEQUENCE seqVideojuego;
DROP SEQUENCE seqOrganizador;
DROP SEQUENCE seqTorneo;
DROP SEQUENCE seqEdicion;
DROP PACKAGE funcionesVideojuego;
DROP PACKAGE funcionesOrganizador;
DROP PACKAGE funcionesTorneo;
DROP PACKAGE funcionesEdicion;