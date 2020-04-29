/* FICHERO PARA LA CREACIÓN DE TIPOS */

/* Declaraciones adelantadas */
CREATE OR REPLACE TYPE Edicion;
/
show errors;

CREATE OR REPLACE TYPE Participante;
/
show errors;

CREATE OR REPLACE TYPE Organizador;
/
show errors;

CREATE OR REPLACE TYPE Torneo;
/
show errors;

/* Declaraciones de tipos para tablas anidadas */

CREATE OR REPLACE TYPE Lista_Ref_Ediciones AS TABLE OF REF Edicion; /* Almacena referencias a ediciones */
/
show errors;

CREATE OR REPLACE TYPE Lista_Ref_Participantes AS TABLE OF REF Participante; /* Referencias a participantes*/
/
show errors;

CREATE OR REPLACE TYPE Lista_Ref_Organizadores AS TABLE OF REF Organizador; /* Referencias a organizadores */
/
show errors;

/* Tipo Videojuego */

CREATE OR REPLACE TYPE ListaDispositivos AS VARRAY(10) OF VARCHAR(12);
/
show errors;

CREATE OR REPLACE TYPE Videojuego AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER(5),
    Titulo VARCHAR(32),
    Precio NUMBER(11,2),
    Anio NUMBER(8),
    Dispositivos ListaDispositivos
);
/
show errors;

/* Tipo Participante */

CREATE OR REPLACE TYPE Participante AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER(5),
    Nombre VARCHAR(32),
    Apellidos VARCHAR(64),
    Dni VARCHAR(9),
    Nacimiento DATE,
    Domicilio VARCHAR(128),
    Email VARCHAR(64),

    /* Declaración de asociaciones */
    Inscrito_En Lista_Ref_Ediciones
) NOT FINAL;
/
show errors;

/* Subtipo de Participante: Amateur */

CREATE OR REPLACE TYPE Amateur UNDER Participante
(
    /* Atributos específicos del subtipo */
    Fotografia BLOB
);
/
show errors;

/* Subtipo de Participante: Profesional */

CREATE OR REPLACE TYPE Profesional UNDER Participante
(
    /* Atributos específicos del subtipo */
    Curriculum CLOB
);
/
show errors;

/* Tipo Organizador */

CREATE OR REPLACE TYPE Organizador AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER(5),
    Nombre VARCHAR(32),
    Apellidos VARCHAR(64),
    Nacimiento DATE,
    Dni VARCHAR(9),

    /* Declaración de asociaciones */
    Organiza Lista_Ref_Ediciones
);
/
show errors;


/* Tipo Edicion */

CREATE OR REPLACE TYPE Edicion AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER,
    Inicio DATE,
    Fin DATE,

    /* Declaración de asociaciones */
    Participan Lista_Ref_Participantes,
    Organizada_Por Lista_Ref_Organizadores,
    Pertenece_a REF Torneo
);
/
show errors;

/* Tipo Torneo */

CREATE OR REPLACE TYPE Torneo AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER(5),
    Titulo VARCHAR(32),
    Reglas CLOB,

    /* Declaración de asociaciones */
    Centrado_En REF Videojuego,
    Tiene_Ediciones Lista_Ref_Ediciones /* tabla anidada para agregación */
);
/
show errors;