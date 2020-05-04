/* FICHERO PARA LA CREACIÓN DE TABLAS */

/* Tabla Videojuego */
CREATE TABLE Tabla_Videojuego OF Videojuego
(
    CONSTRAINT PK_Tabla_Videojuego PRIMARY KEY(Id)
)
PCTFREE 5
PCTUSED 90
STORAGE (
    INITIAL 256K
    PCTINCREASE 10
    MINEXTENTS 2
    MAXEXTENTS 10
);
show errors;

/* Tabla Participante */

CREATE TABLE Tabla_Participante OF Participante
(
    CONSTRAINT PK_Tabla_Participante PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Participante UNIQUE(Dni)
)
PCTFREE 15
PCTUSED 95
STORAGE (
    INITIAL 32M
    PCTINCREASE 30
    MINEXTENTS 2
    MAXEXTENTS 15
)
NESTED TABLE Inscrito_En STORE AS Lista_Ediciones_Participante;
show errors;

/* Tabla Organizador */
CREATE TABLE Tabla_Organizador OF Organizador
(
    CONSTRAINT PK_Tabla_Organizador PRIMARY KEY(Id),
    CONSTRAINT AK_Tabla_Organizador UNIQUE(Dni)
)
PCTFREE 10
PCTUSED 40
STORAGE (
    INITIAL 8M
    PCTINCREASE 20
    MINEXTENTS 2
    MAXEXTENTS 12
)
NESTED TABLE Organiza STORE AS Lista_Ediciones_Organizador;
show errors;

/* Tabla Torneo */
CREATE TABLE Tabla_Torneo OF Torneo
(
    CONSTRAINT PK_Tabla_Torneo PRIMARY KEY(Id),
    Centrado_En NOT NULL
)
PCTFREE 5
PCTUSED 30
STORAGE (
    INITIAL 4M
    PCTINCREASE 20
    MINEXTENTS 2
    MAXEXTENTS 10
)
NESTED TABLE Tiene_Ediciones STORE AS Lista_Ediciones_Torneo;
show errors;


CREATE TABLE Tabla_Edicion OF Edicion
(
    CONSTRAINT PK_Tabla_Edicion PRIMARY KEY(Id),
    Pertenece_a NOT NULL
)
PCTFREE 5
PCTUSED 40
STORAGE (
    INITIAL 8M
    PCTINCREASE 20
    MINEXTENTS 2
    MAXEXTENTS 12
)
NESTED TABLE Participan STORE AS Lista_Participantes_Edicion,
NESTED TABLE Organizada_Por STORE AS Lista_Organizadores_Edicion;
show errors;