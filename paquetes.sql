/* FICHERO PARA LA CREACIÓN DE PAQUETES */
SET SERVEROUTPUT ON;

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


CREATE OR REPLACE PACKAGE funcionesTorneo AS
    PROCEDURE Add_Torneo(Titulo IN VARCHAR, nomReglas IN VARCHAR, Juego_Id IN NUMBER);
    PROCEDURE Delete_Torneo(Cod IN NUMBER);
    PROCEDURE Update_Torneo(Cod IN NUMBER, title IN VARCHAR, rules IN VARCHAR, gameId IN NUMBER);
END funcionesTorneo;
/

CREATE OR REPLACE PACKAGE BODY funcionesTorneo AS
    
    PROCEDURE Add_Torneo(Titulo IN VARCHAR, nomReglas IN VARCHAR, Juego_Id IN NUMBER) IS
    l_bfile BFILE;
    l_clob CLOB;
    refJuego REF Videojuego;
    BEGIN
        BEGIN
            SELECT REF(V) INTO refJuego FROM Tabla_Videojuego V WHERE Id = Juego_Id;
        EXCEPTION 
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20002, 'No existe el videojuego con el ID ' || Juego_Id);
        END;

        INSERT INTO Tabla_Torneo VALUES(seqTorneo.NEXTVAL, Titulo, EMPTY_BLOB(),refJuego,Lista_Ref_Ediciones())
        RETURNING Reglas INTO l_clob;
        /*l_bfile := BFILENAME('C:\Temp' ,nomReglas);
        DBMS_LOB.OPEN(l_bfile, DBMS_LOB.LOB_READONLY);
        DBMS_LOB.LOADFROMFILE(l_clob, l_bfile, DBMS_LOB.GETLENGTH(l_bfile));
        DBMS_LOB.CLOSE(l_bfile);
        COMMIT;*/
    END;

    PROCEDURE Delete_Torneo(Cod IN NUMBER) IS
    BEGIN
        DELETE FROM Tabla_Torneo WHERE Id=Cod;
        EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'No existe el torneo con el ID ' || Cod);
    END;

    PROCEDURE Update_Torneo(Cod IN NUMBER, title IN VARCHAR, rules IN VARCHAR, gameId IN NUMBER) IS
    refJuego REF Videojuego;
    BEGIN
        BEGIN
            SELECT REF(V) INTO refJuego FROM Tabla_Videojuego V WHERE Id = gameId;
        EXCEPTION 
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20002, 'No existe el videojuego con el ID ' || gameId);
        END;

        UPDATE Tabla_Torneo SET Titulo=title, Centrado_En=refJuego WHERE Id=Cod;
    EXCEPTION 
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20002, 'No existe el torneo con el ID ' || Cod);
    END;

END funcionesTorneo;
/
show errors;

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
    PROCEDURE Add_Edicion(numOrg IN NUMBER, cod IN NUMBER, ini IN DATE, fin IN DATE);
    PROCEDURE Delete_Edicion(Cod IN NUMBER);
    PROCEDURE Add_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Update_Edicion(codEdicion IN NUMBER, codTorneo IN NUMBER, ini IN DATE, fi IN DATE);
END funcionesEdicion;
/

CREATE OR REPLACE PACKAGE BODY funcionesEdicion AS

    PROCEDURE Add_Edicion(numOrg IN NUMBER, cod IN NUMBER, ini IN DATE, fin IN DATE) IS
        fechasIncorrectas EXCEPTION;
        refTorneo REF TORNEO;
        refOrganizador REF Organizador;
        maxId NUMBER;
        refEdicion REF Edicion;
    BEGIN
        BEGIN
            SELECT REF(V) INTO refTorneo FROM Tabla_Torneo V WHERE Id = cod;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20001, 'No existe el torneo con el ID ' || cod);
        END;

        BEGIN
            SELECT REF(V) INTO refOrganizador FROM Tabla_Organizador V WHERE Id = numOrg;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20001, 'No existe el organizador con el ID ' || cod);
        END;

        IF(ini > fin) THEN
            RAISE fechasIncorrectas;
        ELSE
            INSERT INTO Tabla_Edicion VALUES(seqEdicion.NEXTVAL, ini, fin, Lista_Ref_Participantes(),
            Lista_Ref_Organizadores(refOrganizador), refTorneo);
            SELECT MAX(Id) INTO maxId FROM Tabla_Edicion;
            SELECT REF(E) INTO refEdicion FROM Tabla_Edicion E Where Id = maxId;
            INSERT INTO TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = cod) VALUES(refEdicion);
            INSERT INTO TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id = numOrg) VALUES(refEdicion);
        END IF;

    EXCEPTION
        WHEN fechasIncorrectas THEN
            RAISE_APPLICATION_ERROR(-20010, 'La fecha de fin de la edición no puede ser anterior a la de inicio');
    END;


    PROCEDURE Delete_Edicion(Cod IN NUMBER) IS
    BEGIN
        DELETE FROM Tabla_Edicion WHERE Id=Cod; /*añadir trigger para borrar referencia en Torneo, Organizador y Participante*/
    END;

    
    PROCEDURE Add_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER) IS
        refOrganizador REF Organizador;
        refEdicion REF Edicion;
        yaEnEdicion NUMBER;
        organizadorEnEdicionException EXCEPTION;
    BEGIN
        BEGIN
            SELECT REF(E) INTO refEdicion FROM Tabla_Edicion E WHERE Id = cod;
            SELECT REF(V) INTO refOrganizador FROM Tabla_Organizador V WHERE Id = codOrg;
            SELECT COUNT(*) INTO yaEnEdicion FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = cod) T
            WHERE T.COLUMN_VALUE.Id = codOrg;

            IF yaEnEdicion != 0 THEN
                RAISE organizadorEnEdicionException;
            END IF;

        EXCEPTION
            WHEN organizadorEnEdicionException THEN
                RAISE_APPLICATION_ERROR(-20012, 'Este organizador ya se encuentra en la edicion');
        END;

        INSERT INTO TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id = codOrg) VALUES(refEdicion);
        INSERT INTO TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = cod) VALUES(refOrganizador);
    END;


    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER) IS
    BEGIN
        DELETE FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id=cod) T WHERE T.COLUMN_VALUE.Id=codOrg;
        DELETE FROM TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id=codOrg) T WHERE T.COLUMN_VALUE.Id=cod;
    END;


    PROCEDURE Update_Edicion(codEdicion IN NUMBER, codTorneo IN NUMBER, ini IN DATE, fi IN DATE) IS
        fechasIncorrectas EXCEPTION;
        refTorneo REF TORNEO;
    BEGIN
        BEGIN
            SELECT REF(V) INTO refTorneo FROM Tabla_Torneo V WHERE Id = codTorneo;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20001, 'No existe el torneo con el ID ' || codTorneo);
        END;

        IF(ini > fi) THEN
            RAISE fechasIncorrectas;
        ELSE
            UPDATE Tabla_Edicion SET Inicio = ini, Fin = fi, Pertenece_a = refTorneo WHERE Id = codEdicion;
        END IF;
    EXCEPTION
        WHEN fechasIncorrectas THEN
            RAISE_APPLICATION_ERROR(-20010, 'La fecha de fin de la edicion no puede ser anterior a la de inicio');
    END;

END funcionesEdicion;
/

CREATE OR REPLACE PACKAGE funcionesParticipante AS
    PROCEDURE Remove_Participation(idPart IN NUMBER, tipoPart IN VARCHAR, idEdicion in NUMBER);
END funcionesParticipante;
/

CREATE OR REPLACE PACKAGE BODY funcionesEdicion AS

    PROCEDURE Remove_Participation(idPart IN NUMBER, tipoPart IN VARCHAR, idEdicion in NUMBER) AS
    BEGIN
        IF tipoPart == "amateur" THEN
            DELETE FROM TABLE(SELECT Inscrito_En FROM Tabla_Amateur WHERE Id=idPart) T WHERE T.COLUMN_VALUE.Id=idEdicion;
        ELSIF tipoPart == "profesional" THEN
            DELETE FROM TABLE(SELECT Inscrito_En FROM Tabla_Profesional WHERE Id=idPart) T WHERE T.COLUMN_VALUE.Id=idEdicion;
        END IF;
    END;


END funcionesParticipante;
/