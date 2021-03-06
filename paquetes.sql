/* FICHERO PARA LA CREACIÓN DE PAQUETES */
SET SERVEROUTPUT ON;

/* Declaración de los paquetes */

CREATE OR REPLACE PACKAGE funcionesVideojuego AS
    PROCEDURE Add_Videojuego(Titulo IN VARCHAR, Precio IN NUMBER, Anio IN NUMBER);
    PROCEDURE Add_Dispositivo_Videojuego(cod IN NUMBER, pos IN NUMBER, Disp IN VARCHAR);
    PROCEDURE Delete_Videojuego(Cod IN NUMBER);
    PROCEDURE Update_Videojuego(Cod IN NUMBER, title IN VARCHAR, price IN NUMBER, year IN NUMBER);
END funcionesVideojuego;
/

CREATE OR REPLACE PACKAGE funcionesTorneo AS
    PROCEDURE Add_Torneo(Titulo IN VARCHAR, nomReglas IN VARCHAR, Juego_Id IN NUMBER);
    PROCEDURE Delete_Torneo(Cod IN NUMBER);
    PROCEDURE Update_Torneo(Cod IN NUMBER, title IN VARCHAR, rules IN VARCHAR, gameId IN NUMBER);
END funcionesTorneo;
/

CREATE OR REPLACE PACKAGE funcionesOrganizador AS
    PROCEDURE Add_Organizador(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR);
    PROCEDURE Delete_Organizador(Cod IN NUMBER);
    PROCEDURE Update_Organizador(Cod IN NUMBER, Nom IN VARCHAR, Apell IN VARCHAR, Nac IN DATE, DniCod IN VARCHAR);
END funcionesOrganizador;
/


CREATE OR REPLACE PACKAGE funcionesEdicion AS
    PROCEDURE Add_Edicion(numOrg IN NUMBER, cod IN NUMBER, ini IN DATE, fin IN DATE);
    PROCEDURE Delete_Edicion(Cod IN NUMBER);
    PROCEDURE Add_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Add_Participante_Edicion(codEd IN NUMBER, codPar IN NUMBER);
    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER);
    PROCEDURE Delete_Participante_Edicion(codEd IN NUMBER, codPar IN NUMBER);
    PROCEDURE Update_Edicion(codEdicion IN NUMBER, codTorneo IN NUMBER, ini IN DATE, fi IN DATE);
END funcionesEdicion;
/


CREATE OR REPLACE PACKAGE funcionesParticipante AS
    PROCEDURE Add_Amateur(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR,
    Domicilio IN VARCHAR, Email IN VARCHAR, nombreFotografia IN VARCHAR);
    PROCEDURE Add_Profesional(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR,
    Domicilio IN VARCHAR, Email IN VARCHAR, nombreCurriculum IN VARCHAR);
    PROCEDURE Update_Participante(Cod IN VARCHAR, Nom IN VARCHAR, Apell IN VARCHAR, Nac IN DATE, NIF IN VARCHAR,
    Dom IN VARCHAR, Correo IN VARCHAR);
END funcionesParticipante;
/

/* CREATE OR REPLACE DIRECTORY TABD_FOLDER AS 'C:\Users\Aleph\Desktop\TABD\TRABAJO'; */

/* Definición de los paquetes */

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


CREATE OR REPLACE PACKAGE BODY funcionesTorneo AS
    
    PROCEDURE Add_Torneo(Titulo IN VARCHAR, nomReglas IN VARCHAR, Juego_Id IN NUMBER) IS
    /*l_bfile BFILE := BFILENAME('TABD_FOLDER', LOWER(nomReglas));
    l_blob BLOB;*/
    refJuego REF Videojuego;
    BEGIN
        BEGIN
            SELECT REF(V) INTO refJuego FROM Tabla_Videojuego V WHERE Id = Juego_Id;
        EXCEPTION 
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20002, 'No existe el videojuego con el ID ' || Juego_Id);
        END;

        INSERT INTO Tabla_Torneo VALUES(seqTorneo.NEXTVAL, Titulo, EMPTY_BLOB(),refJuego,Lista_Ref_Ediciones());
        /*RETURN Reglas INTO l_blob;
        DBMS_LOB.fileopen(l_bfile, Dbms_Lob.File_Readonly);
        DBMS_LOB.loadfromfile(l_blob,l_bfile,DBMS_LOB.getlength(l_bfile));
        DBMS_LOB.close(l_blob);
        DBMS_LOB.fileclose(l_bfile);
        COMMIT; */
    END;

    PROCEDURE Delete_Torneo(Cod IN NUMBER) IS
        CURSOR edicionesDelTorneo IS SELECT T.COLUMN_VALUE.Id E FROM 
        TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id = Cod) T;
    BEGIN

        FOR r_edicion IN edicionesDelTorneo LOOP
            funcionesEdicion.Delete_Edicion(r_edicion.E);
        END LOOP;

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


CREATE OR REPLACE PACKAGE BODY funcionesOrganizador AS

    PROCEDURE Add_Organizador(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR) IS
    BEGIN
        INSERT INTO Tabla_Organizador VALUES(Organizador(seqOrganizador.NEXTVAL,Nombre,Apellidos,
        Nacimiento,Dni,Lista_Ref_Ediciones()));
    END;

    PROCEDURE Delete_Organizador(Cod IN NUMBER) IS
        CURSOR edicionesDelOrganizador IS 
        SELECT V.COLUMN_VALUE.Id E FROM TABLE(SELECT organiza FROM tabla_organizador WHERE Id = Cod) V;
    BEGIN

        FOR r_edicion IN edicionesDelOrganizador LOOP
            DELETE FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = r_edicion.E) T
            WHERE T.COLUMN_VALUE.Id = Cod;
        END LOOP;

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
        IdTorneo NUMBER;
        CURSOR participantesEdicion IS SELECT V.COLUMN_VALUE.Id P FROM TABLE(SELECT Participan 
        FROM Tabla_Edicion WHERE Id = Cod) V;
        CURSOR organizadoresEdicion IS SELECT V.COLUMN_VALUE.Id O FROM TABLE(SELECT Organizada_Por
        FROM Tabla_Edicion WHERE Id = Cod) V;
    BEGIN

        SELECT DEREF(Pertenece_A).Id INTO IdTorneo FROM Tabla_Edicion WHERE Id = Cod;
        DELETE FROM TABLE(SELECT Tiene_Ediciones FROM Tabla_Torneo WHERE Id=IdTorneo) T WHERE T.COLUMN_VALUE.Id = Cod;  

        FOR r_participante IN participantesEdicion LOOP /* por cada participante, borrar la referencia a edición*/
            DELETE FROM TABLE(SELECT Inscrito_En FROM Tabla_Participante WHERE Id = r_participante.P) T
            WHERE T.COLUMN_VALUE.Id = Cod;
        END LOOP;

        FOR r_organizador IN organizadoresEdicion LOOP /* por cada organizador, borrar la referencia a edición*/
            DELETE FROM TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id = r_organizador.O) T
            WHERE T.COLUMN_VALUE.Id = Cod;
        END LOOP;

        DELETE FROM Tabla_Edicion WHERE Id=Cod;
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

    PROCEDURE Add_Participante_Edicion(codEd IN NUMBER, codPar IN NUMBER) IS
        refParticipante REF Participante;
        refEdicion REF Edicion;
        edicionPar Edicion;
        edicionElegida Edicion;
        yaEnEdicion NUMBER;
        cupoMaximo NUMBER;
        participanteEnEdicionException EXCEPTION;
        cupoMaximoException EXCEPTION;
        fechasSolapadas EXCEPTION;
        CURSOR edicionesParticipante IS SELECT T.COLUMN_VALUE.Id E FROM TABLE(SELECT Inscrito_En FROM
            Tabla_Participante WHERE Id = codPar) T;
    BEGIN
        BEGIN
            SELECT REF(E) INTO refEdicion FROM Tabla_Edicion E WHERE Id = codEd;
            SELECT VALUE(V) INTO edicionElegida FROM Tabla_Edicion V Where Id = codEd;
            SELECT REF(P) INTO refParticipante FROM Tabla_Participante P WHERE Id = codPar;
            SELECT COUNT(*) INTO yaEnEdicion FROM TABLE(SELECT Participan FROM Tabla_Edicion WHERE Id = CodEd) T
            WHERE T.COLUMN_VALUE.Id = codPar;
            SELECT COUNT(*) INTO cupoMaximo FROM TABLE(SELECT Participan FROM Tabla_Edicion WHERE Id = CodEd);

            IF yaEnEdicion != 0 THEN
                RAISE participanteEnEdicionException;
            END IF;

            IF NOT(cupoMaximo != 100) THEN
                RAISE cupoMaximoException;
            END IF;

            FOR r_edicion IN edicionesParticipante LOOP
                SELECT VALUE(V) INTO edicionPar FROM Tabla_Edicion V WHERE Id = r_edicion.E;
                IF (edicionElegida.Inicio <= edicionPar.Fin AND
                     edicionElegida.Fin >= edicionPar.Inicio) THEN
                     RAISE fechasSolapadas;
                END IF;
            END LOOP;

        EXCEPTION
            WHEN participanteEnEdicionException THEN
                RAISE_APPLICATION_ERROR(-20015, 'Este participante ya se encuentra en la edicion');
            WHEN cupoMaximoException THEN
                RAISE_APPLICATION_ERROR(-20016, 'Se ha alcanzado el cupo máximo en esta edicion (100)');
            WHEN fechasSolapadas THEN
                RAISE_APPLICATION_ERROR(-20017, 'Las fechas de la edición se solapan con otras en las que el usuario ya se ha inscrito');   
        END;

        INSERT INTO TABLE(SELECT Inscrito_En FROM Tabla_Participante WHERE Id = codPar) VALUES(refEdicion);
        INSERT INTO TABLE(SELECT Participan FROM Tabla_Edicion WHERE Id = codEd) VALUES(refParticipante);
    END;


    PROCEDURE Delete_Organizador_Edicion(cod IN NUMBER, codOrg IN NUMBER) IS
    BEGIN
        DELETE FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id=cod) T WHERE T.COLUMN_VALUE.Id=codOrg;
        DELETE FROM TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id=codOrg) T WHERE T.COLUMN_VALUE.Id=cod;
    END;


    PROCEDURE Delete_Participante_Edicion(codEd IN NUMBER, codPar IN NUMBER) IS
    BEGIN
        DELETE FROM TABLE(SELECT Participan FROM Tabla_Edicion WHERE Id = codEd) T WHERE T.COLUMN_VALUE.Id=codPar;
        DELETE FROM TABLE(SELECT Inscrito_En FROM Tabla_Participante WHERE Id = codPar) T WHERE T.COLUMN_VALUE.Id=codEd;
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
show errors;


CREATE OR REPLACE PACKAGE BODY funcionesParticipante AS

    PROCEDURE Add_Amateur(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR,
    Domicilio IN VARCHAR, Email IN VARCHAR, nombreFotografia IN VARCHAR) IS
    BEGIN
        INSERT INTO Tabla_Participante VALUES(Amateur(seqParticipante.NEXTVAL, Nombre, Apellidos, Dni,
        Nacimiento, Domicilio, Email, Lista_Ref_Ediciones(), EMPTY_BLOB()));
    END;

    PROCEDURE Add_Profesional(Nombre IN VARCHAR, Apellidos IN VARCHAR, Nacimiento IN DATE, Dni IN VARCHAR,
    Domicilio IN VARCHAR, Email IN VARCHAR, nombreCurriculum IN VARCHAR) IS
    BEGIN
        INSERT INTO Tabla_Participante VALUES(Profesional(seqParticipante.NEXTVAL, Nombre, Apellidos,
        Dni, Nacimiento, Domicilio, Email, Lista_Ref_Ediciones(), EMPTY_CLOB()));
    END;

    PROCEDURE Update_Participante(Cod IN VARCHAR, Nom IN VARCHAR, Apell IN VARCHAR, Nac IN DATE, NIF IN VARCHAR,
    Dom IN VARCHAR, Correo IN VARCHAR) IS
    BEGIN
        UPDATE Tabla_Participante SET Nombre=Nom, Apellidos = Apell, Nacimiento = Nac, Dni = NIF,
        Domicilio = Dom, Email = Correo WHERE Id = Cod;
    END;

END funcionesParticipante;
/