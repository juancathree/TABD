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
    Dispositivos ListaDispositivos,

    /* Declaración de métodos */
    MEMBER PROCEDURE borrarVideojuego(),
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getTitulo RETURN VARCHAR,
    MEMBER FUNCTION getPrecio RETURN NUMBER,
    MEMBER FUNCTION getAnio RETURN NUMBER,
    MEMBER FUNCTION getDispositivos RETURN ListaDispositivos,
    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR),
    MEMBER PROCEDURE setPrecio(precio IN NUMBER),
    MEMBER PROCEDURE setAnio(anio IN NUMBER),
    MEMBER PROCEDURE setDispositivos(dispositivos IN ListaDispositivos)
);
/

CREATE TYPE BODY Videojuego AS

    MEMBER PROCEDURE borrarVideojuego() IS
        BEGIN
            DELETE from Videojuego
            where Id = SELF.Id;
        END;

    MEMBER FUNCTION getId RETURNS NUMBER IS 
        BEGIN   
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getTitulo RETURNS VARCHAR IS
        BEGIN
            RETURN SELF.Titulo;
        END;

    MEMBER FUNCTION getPrecio RETURNS NUMBER IS
        BEGIN
            RETURN SELF.Precio;
        END;
    
    MEMBER FUNCTION getAnio RETURNS NUMBER IS
        BEGIN
            RETURN SELF.Anio;
        END;

    MEMBER FUNCTION getDispositivos RETURNS ListaDispositivos IS
        BEGIN
            RETURN SELF.Dispositivos;
        END;

    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR) IS 
        BEGIN
            UPDATE Videojuego
            SET Titulo = titulo
            WHERE Id = SELF.Id;
        END;

    MEMBER PROCEDURE setPrecio(precio IN NUMBER) IS
        BEGIN
            UPDATE Videojuego
            SET Precio = precio
            WHERE Id = SELF.Id;
        END;

    MEMBER PROCEDURE setAnio(anio IN NUMBER) IS
        BEGIN
            UPDATE Videojuego
            SET Anio = anio
            WHERE Id = SELF.Id;
        END;

    MEMBER PROCEDURE setDispositivos(dispositivos IN ListaDispositivos) IS
        BEGIN
            UPDATE Videojuego
            SET Dispositivos = dispositivos
            WHERE Id = SELF.Id;
        END;

END;
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
    Inscrito_En Lista_Ref_Ediciones,

    /* Declaración de métodos */
    MEMBER PROCEDURE borrarParticipante,
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getNombre RETURN VARCHAR,
    MEMBER FUNCTION getApellidos RETURN VARCHAR,
    MEMBER FUNCTION getDni RETURN VARCHAR,
    MEMBER FUNCTION getNacimiento RETURN DATE,
    MEMBER FUNCTION getDomicilio RETURN VARCHAR,
    MEMBER FUNCTION getEmail RETURN VARCHAR,
    MEMBER PROCEDURE setNombre(nombre IN VARCHAR),
    MEMBER PROCEDURE setApellidos(apellidos IN VARCHAR),
    MEMBER PROCEDURE setDni(dni IN VARCHAR),
    MEMBER PROCEDURE setNacimiento(nac IN DATE),
    MEMBER PROCEDURE setDomicilio(domicilio IN VARCHAR),
    MEMBER PROCEDURE setEmail(email IN VARCHAR)

) NOT FINAL;
/
show errors;

/* Subtipo de Participante: Amateur */

CREATE OR REPLACE TYPE Amateur UNDER Participante
(
    /* Atributos específicos del subtipo */
    Fotografia BLOB,

    /* Declaracion de métodos */
    MEMBER FUNCTION getFotografia RETURN BLOB,
    MEMBER PROCEDURE setFotografia(foto IN BLOB)
);
/
show errors;

/* Subtipo de Participante: Profesional */

CREATE OR REPLACE TYPE Profesional UNDER Participante
(
    /* Atributos específicos del subtipo */
    Curriculum CLOB,

    /* Declaración de métodos */
    MEMBER FUNCTION getCurriculum RETURN CLOB,
    MEMBER PROCEDURE setCurriculum(curriculum IN CLOB)
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
    Organiza Lista_Ref_Ediciones,

    /* Declaración de métodos */
    MEMBER PROCEDURE borrarOrganizador,
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getNombre RETURN VARCHAR,
    MEMBER FUNCTION getApellidos RETURN VARCHAR,
    MEMBER FUNCTION getNacimiento RETURN DATE,
    MEMBER FUNCTION getDni RETURN VARCHAR,
    MEMBER PROCEDURE setNombre(nombre IN VARCHAR),
    MEMBER PROCEDURE setApellidos(apellidos IN VARCHAR),
    MEMBER PROCEDURE setNacimiento(nac IN DATE),
    MEMBER PROCEDURE setDni(dni IN VARCHAR)
);
/
show errors;

/* Tipo Edicion */

CREATE OR REPLACE TYPE Edicion AS OBJECT
(
    /* Declaración de atributos */
    Id NUMBER(5),
    Inicio DATE,
    Fin DATE,

    /* Declaración de asociaciones */
    Participan Lista_Ref_Participantes,
    Organizada_Por Lista_Ref_Organizadores,
    Pertenece_a REF Torneo,

    /* Declaración de métodos */
    MEMBER PROCEDURE borrarEdicion,
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getInicio RETURN DATE,
    MEMBER FUNCTION getFin RETURN DATE,
    MEMBER PROCEDURE setInicio(inicio IN DATE),
    MEMBER PROCEDURE setFin(fin IN DATE)
);
/
show errors;

CREATE OR REPLACE TYPE Lista_Ediciones AS TABLE OF Edicion; /* Para almacenar las ediciones de un torneo*/
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
    Tiene_Ediciones Lista_Ediciones, /* tabla anidada para composición */

    /* Declaración de métodos */
    MEMBER FUNCTION numEdiciones RETURN NUMBER,
    MEMBER FUNCTION totalParticipantes RETURN NUMBER,
    MEMBER PROCEDURE borrarTorneo,
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getTitulo RETURN VARCHAR,
    MEMBER FUNCTION getReglas RETURN CLOB,
    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR),
    MEMBER PROCEDURE setReglas(reglas IN CLOB)
);
/
show errors;