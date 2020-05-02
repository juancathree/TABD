/* FICHERO PARA LA CREACIÓN DE TABLAS */

/* Tabla Videojuego */
CREATE TABLE Tabla_Videojuego OF Videojuego
(
    CONSTRAINT PK_Tabla_Videojuego PRIMARY KEY(Id)
);
show errors;

/* Tabla Participante */

CREATE TABLE Tabla_Participante OF Participante
(
    CONSTRAINT PK_Tabla_Participante PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Participante UNIQUE(Dni)
)
NESTED TABLE Inscrito_En STORE AS Lista_Ediciones_Participante;
show errors;

/* Tabla Organizador */
CREATE TABLE Tabla_Organizador OF Organizador
(
    CONSTRAINT PK_Tabla_Organizador PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Organizador UNIQUE(Dni)
)
NESTED TABLE Organiza STORE AS Lista_Ediciones_Organizador;
show errors;

/* Tabla Torneo */
CREATE TABLE Tabla_Torneo OF Torneo
(
    CONSTRAINT PK_Tabla_Torneo PRIMARY KEY(Id),
    Centrado_En NOT NULL
)
NESTED TABLE Tiene_Ediciones STORE AS Lista_Ediciones_Torneo;
show errors;


CREATE TABLE Tabla_Edicion OF Edicion
(
    CONSTRAINT PK_Tabla_Edicion PRIMARY KEY(Id),
    Pertenece_a NOT NULL
)
NESTED TABLE Participan STORE AS Lista_Participantes_Edicion,
NESTED TABLE Organizada_Por STORE AS Lista_Organizadores_Edicion;
show errors;