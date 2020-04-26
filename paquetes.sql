/* FICHERO PARA LA CREACIÓN DE PAQUETES */

CREATE OR REPLACE PACKAGE funcionesVideojuego AS
    PROCEDURE Add_Videojuego(Titulo IN VARCHAR, Precio IN NUMBER, Anio IN NUMBER);
    PROCEDURE Add_Dispositivo_Videojuego(cod IN NUMBER, pos IN NUMBER, Disp IN VARCHAR);
    PROCEDURE Delete_Videojuego(Cod IN NUMBER);
    PROCEDURE Update_Videojuego(Cod IN NUMBER, title IN VARCHAR, price IN NUMBER, year IN NUMBER);
END funcionesVideojuego;
/

CREATE OR REPLACE PACKAGE BODY funcionesVideojuego AS

    PROCEDURE Add_Videojuego(Titulo IN VARCHAR, Precio IN NUMBER, Anio IN NUMBER) IS
    BEGIN
        INSERT INTO Tabla_Videojuego VALUES(Videojuego(seqVideojuego.NEXTVAL,Titulo,Precio,Anio,ListaDispositivos()));
    END;
    
    PROCEDURE Add_Dispositivo_Videojuego(cod IN NUMBER, pos IN NUMBER, Disp IN VARCHAR) IS
    Nueva_Lista_Dispositivos ListaDispositivos;
    BEGIN
        SELECT Dispositivos INTO Nueva_Lista_Dispositivos FROM Tabla_Videojuego WHERE Id = cod;
        Nueva_Lista_Dispositivos.extend();
        Nueva_Lista_Dispositivos(pos) := Disp;

        UPDATE Tabla_Videojuego SET Dispositivos=Nueva_Lista_Dispositivos WHERE Id = cod;
    END;

    PROCEDURE Delete_Videojuego(Cod IN NUMBER) IS
    BEGIN
        DELETE FROM Tabla_Videojuego WHERE Id = Cod;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'No existe el videojuego con el ID ' || Cod);
    END;

    PROCEDURE Update_Videojuego (Cod IN NUMBER, title IN VARCHAR, price IN NUMBER, year IN NUMBER) IS
    BEGIN
        UPDATE Tabla_Videojuego SET Titulo = title, Precio = price, Anio = year WHERE Id = Cod;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'No existe el videojuego con el ID ' || Cod);
    END;

END funcionesVideojuego;
/


CREATE OR REPLACE PACKAGE funcionesOrganizador AS
    PROCEDURE Add_Organizador(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR);
    PROCEDURE Delete_Organizador(Cod IN NUMBER);
    PROCEDURE Update_Organizador(Cod IN NUMBER, Nom IN VARCHAR, Apell IN VARCHAR, Nac IN DATE, DniCod IN VARCHAR);
END funcionesOrganizador;
/

CREATE OR REPLACE PACKAGE BODY funcionesOrganizador AS

    PROCEDURE Add_Organizador(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR) IS
    BEGIN
        INSERT INTO Tabla_Organizador VALUES(Organizador(seqOrganizador.NEXTVAL,Nombre,Apellidos,
        Nacimiento,Dni,Lista_Ref_Ediciones()));
    END;

    PROCEDURE Delete_Organizador(Cod IN NUMBER) IS
    BEGIN
        DELETE FROM Tabla_Organizador WHERE Id = Cod;
        EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'No existe el organizador con el ID ' || Cod);
    END;

    PROCEDURE Update_Organizador(Cod IN NUMBER, Nom IN VARCHAR, Apell IN VARCHAR, Nac IN DATE, DniCod IN VARCHAR) IS
    BEGIN
        UPDATE Tabla_Organizador SET Nombre = Nom, Apellidos = Apell, Nacimiento = Nac, Dni = DniCod
        WHERE Id = Cod;
    END;

END funcionesOrganizador;
/


CREATE OR REPLACE PACKAGE funcionesEdicion AS
    PROCEDURE Add_Edicion_Torneo(ini IN DATE, fin IN DATE);
    PROCEDURE Add_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Update_Edicion(cod IN NUMBER, ini IN DATE, fin IN DATE);
END funcionesEdicion;
/

CREATE OR REPLACE PACKAGE BODY funcionesEdicion AS

    PROCEDURE Add_Edicion_Torneo(cod IN NUMBER, ini IN DATE, fin IN DATE) IS
        torneo REF Tabla_Torneo;
        id NUMBER;
    BEGIN
        INSERT INTO TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = cod)(Inicio, Fin) VALUES(ini, fin); 
        SELECT REF(T) INTO torneo FROM Tabla_Torneo T WHERE T.Id = cod;
        SELECT MAX(Id) INTO id FROM TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = cod); /* no se si funciona */
        UPDATE TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = cod) SET Pertenece_a = torneo WHERE Id = id;
    END;

    PROCEDURE Add_Organizador_Edicion(codEd IN NUMBER, codOrg IN NUMBER, codTorn IN NUMBER) IS
    BEGIN
        INSERT INTO TABLE(SELECT Organizada_Por FROM TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = codTorn) WHERE Id = codEd) 
            VALUES(SELECT REF(O) FROM Tabla_Organizador O WHERE O.Dni = dni);
        
        INSERT INTO TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id = codOrg) 
            VALUES(SELECT REF(E) FROM TABLE(SELECT * FROM TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = codTorn)) E WHERE E.Id = codEd);
    END;

    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER) IS
        organizador REF Tabla_Organizador;
        edicion REF Tabla_Edicion;
    BEGIN
        SELECT REF(O) INTO organizador FROM Tabla_Organizador O WHERE O.Id = codOrg;
        SELECT REF(E) INTO edicion FROM Tabla_Edicion E WHERE E.Id = cod;
        DELETE FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = cod) WHERE ; /* por terminar */
        DELETE FROM TABLE(SELECT Organiza FROM Table_Organizador WHERE Id = cod) WHERE ;  /* port terminar*/
    END;

    PROCEDURE Update_Edicion(cod IN NUMBER, ini IN DATE, fin IN DATE) IS
    BEGIN
        -- UPDATE Tabla_Edicion SET Inicio = ini, Fin = fin WHERE Id = cod;
    END;

END funcionesEdicion;
/