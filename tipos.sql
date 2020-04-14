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
show errors;

CREATE OR REPLACE TYPE BODY Videojuego AS

    MEMBER FUNCTION getId RETURN NUMBER IS 
        BEGIN   
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getTitulo RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Titulo;
        END;

    MEMBER FUNCTION getPrecio RETURN NUMBER IS
        BEGIN
            RETURN SELF.Precio;
        END;
    
    MEMBER FUNCTION getAnio RETURN NUMBER IS
        BEGIN
            RETURN SELF.Anio;
        END;

    MEMBER FUNCTION getDispositivos RETURN ListaDispositivos IS
        BEGIN
            RETURN SELF.Dispositivos;
        END;

    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR) IS 
        BEGIN
            self.titulo := titulo;
        END;

    MEMBER PROCEDURE setPrecio(precio IN NUMBER) IS
        BEGIN
            self.precio := precio;
        END;

    MEMBER PROCEDURE setAnio(anio IN NUMBER) IS
        BEGIN
            self.anio := anio;
        END;

    MEMBER PROCEDURE setDispositivos(dispositivos IN ListaDispositivos) IS
        BEGIN
            self.dispositivos := dispositivos;
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

CREATE OR REPLACE TYPE BODY Participante AS

    MEMBER FUNCTION getId RETURN NUMBER IS
        BEGIN
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getNombre RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Nombre;
        END;

    MEMBER FUNCTION getApellidos RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Apellidos;
        END;

    MEMBER FUNCTION getDni RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Dni;
        END;

    MEMBER FUNCTION getNacimiento RETURN DATE IS
        BEGIN
            RETURN SELF.Nacimiento;
        END;

    MEMBER FUNCTION getDomicilio RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Domicilio;
        END;

    MEMBER FUNCTION getEmail RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Email;
        END;

    MEMBER PROCEDURE setNombre(nombre IN VARCHAR) IS
        BEGIN
            self.nombre := nombre;
        END;

    MEMBER PROCEDURE setApellidos(apellidos IN VARCHAR) IS
        BEGIN
            self.apellidos := apellidos;
        END;

    MEMBER PROCEDURE setDni(dni IN VARCHAR) IS 
        BEGIN
            self.dni := dni;
        END;

    MEMBER PROCEDURE setNacimiento(nac IN DATE) IS
        BEGIN
            self.nacimiento := nac;
        END;

    MEMBER PROCEDURE setDomicilio(domicilio IN VARCHAR) IS
        BEGIN
            self.domicilio := domicilio;
        END;

    MEMBER PROCEDURE setEmail(email IN VARCHAR) IS
        BEGIN
            self.email := email;
        END;
END;
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

CREATE OR REPLACE TYPE BODY Amateur AS

    MEMBER FUNCTION getFotografia RETURN BLOB IS
        BEGIN
            RETURN SELF.Fotografia;
        END;

    MEMBER PROCEDURE setFotografia(foto IN BLOB) IS
        BEGIN
            self.Fotografia := foto;
        END;

END;
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

CREATE OR REPLACE TYPE BODY Profesional AS

    MEMBER FUNCTION getCurriculum RETURN CLOB IS
        BEGIN
            RETURN SELF.Curriculum;
        END;

    MEMBER PROCEDURE setCurriculum(curriculum IN CLOB) IS
        BEGIN
            self.curriculum := curriculum;
        END;

END;
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

CREATE OR REPLACE TYPE BODY Organizador AS

    MEMBER FUNCTION getId RETURN NUMBER IS
        BEGIN
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getNombre RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Nombre;
        END;

    MEMBER FUNCTION getApellidos RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Apellidos;
        END;

    MEMBER FUNCTION getNacimiento RETURN DATE IS
        BEGIN
            RETURN SELF.Nacimiento;
        END;

    MEMBER FUNCTION getDni RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Dni;
        END;

    MEMBER PROCEDURE setNombre(nombre IN VARCHAR) IS
        BEGIN
            self.nombre := nombre;
        END;

    MEMBER PROCEDURE setApellidos(apellidos IN VARCHAR) IS
        BEGIN
            self.apellidos := apellidos;
        END;

    MEMBER PROCEDURE setNacimiento(nac IN DATE) IS
        BEGIN
            self.nacimiento := nac;
        END;

    MEMBER PROCEDURE setDni(dni IN VARCHAR) IS
        BEGIN
            self.dni := dni;
        END;

END;
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
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getInicio RETURN DATE,
    MEMBER FUNCTION getFin RETURN DATE,
    MEMBER PROCEDURE setInicio(inicio IN DATE),
    MEMBER PROCEDURE setFin(fin IN DATE)
);
/

CREATE OR REPLACE TYPE BODY Edicion AS

    MEMBER FUNCTION getId RETURN NUMBER IS
        BEGIN
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getInicio RETURN DATE IS
        BEGIN
            RETURN SELF.Inicio;
        END;

    MEMBER FUNCTION getFin RETURN DATE IS
        BEGIN
            RETURN SELF.Fin;
        END;

    MEMBER PROCEDURE setInicio(inicio IN DATE) IS
        BEGIN
            self.inicio := inicio;
        END;

    MEMBER PROCEDURE setFin(fin IN DATE) IS
        BEGIN
            self.fin := fin;
        END;

END;
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
    MEMBER FUNCTION getId RETURN NUMBER,
    MEMBER FUNCTION getTitulo RETURN VARCHAR,
    MEMBER FUNCTION getReglas RETURN CLOB,
    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR),
    MEMBER PROCEDURE setReglas(reglas IN CLOB)
);
/

CREATE OR REPLACE TYPE BODY Torneo AS

    MEMBER FUNCTION numEdiciones RETURN NUMBER IS
        BEGIN
           RETURN SELF.Id; 
        END;

    MEMBER FUNCTION totalParticipantes RETURN NUMBER IS
        BEGIN
            RETURN SELF.Id;
        END;
    
    MEMBER FUNCTION getId RETURN NUMBER IS
        BEGIN
            RETURN SELF.Id;
        END;

    MEMBER FUNCTION getTitulo RETURN VARCHAR IS
        BEGIN
            RETURN SELF.Titulo;
        END;

    MEMBER FUNCTION getReglas RETURN CLOB IS
        BEGIN
            RETURN SELF.Reglas;
        END;

    MEMBER PROCEDURE setTitulo(titulo IN VARCHAR) IS
        BEGIN
            self.titulo := titulo;
        END;

    MEMBER PROCEDURE setReglas(reglas IN CLOB) IS
        BEGIN
            self.reglas := reglas;
        END;

END;
/
show errors;