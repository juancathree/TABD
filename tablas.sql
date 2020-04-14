/* FICHERO PARA LA CREACIÓN DE TABLAS */

/* Tabla Videojuego */
CREATE TABLE Tabla_Videojuego OF Videojuego
(
    CONSTRAINT PK_Tabla_Videojuego PRIMARY KEY(Id)
);
show errors;

/* Tabla Amateur */
CREATE TABLE Tabla_Amateur OF Amateur
(
    CONSTRAINT PK_Tabla_Amateur PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Amateur UNIQUE(Dni)
)
NESTED TABLE Inscrito_En STORE AS Lista_Ediciones_Amateur;
show errors;

/* Tabla Profesional */
CREATE TABLE Tabla_Profesional OF Profesional
(
    CONSTRAINT PK_Tabla_Profesional PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Profesional UNIQUE(Dni)
)
NESTED TABLE Inscrito_En STORE AS Lista_Ediciones_Profesional;
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
NESTED TABLE Tiene_Ediciones STORE AS Lista_Ediciones_Torneo
    (NESTED TABLE Participan STORE AS Lista_Participantes_Edicion,
    NESTED TABLE Organizada_Por STORE AS Lista_Organizadores_Edicion);
show errors;