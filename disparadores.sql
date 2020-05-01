/* FICHERO PARA LA CREACIÓN DE DISPARADORES */

/* Disparador para evitar borrar un videojuego del que todavía existe un torneo */

CREATE OR REPLACE TRIGGER videojuegoConTorneos BEFORE DELETE ON Tabla_Videojuego
FOR EACH ROW
DECLARE
    CURSOR existeTorneo IS SELECT DEREF(Centrado_En).Id T FROM Tabla_Torneo;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    FOR r_torneo IN existeTorneo LOOP
      
        IF NOT(:OLD.Id != r_torneo.T) THEN
            RAISE_APPLICATION_ERROR(-20013, 'No se puede borrar un videojuego para el que existen torneos');
        END IF;

    END LOOP;
END videojuegoConTorneos;
/

/* Disparador para borrar un torneo del que todavía existe una edicion */

CREATE OR REPLACE TRIGGER torneoConEdicion BEFORE DELETE ON Tabla_Torneo
FOR EACH ROW
DECLARE
    CURSOR existeEdicion IS SELECT DEREF(Pertenece_a).Id T, Id idEdicion FROM Tabla_Edicion;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    FOR r_edicion IN existeEdicion LOOP
      
        IF NOT(:OLD.Id != r_edicion.T) THEN
            DELETE FROM Tabla_Edicion WHERE Id = r_edicion.idEdicion; 
        END IF;

    END LOOP;
END torneoConEdicion;
/


/* Disparador para evitar borrar un organizador que es el único organizador de alguna edición */

CREATE OR REPLACE TRIGGER organizadorUnico BEFORE DELETE ON Tabla_Organizador
FOR EACH ROW
DECLARE
    numOrganizadores NUMBER;
    CURSOR edicionesDelOrganizador IS 
    SELECT V.COLUMN_VALUE.Id E FROM TABLE(SELECT organiza FROM Tabla_Organizador WHERE Id = :OLD.Id) V;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN

    FOR r_edicion IN edicionesDelOrganizador LOOP /* por cada edición del organizador */
        SELECT COUNT(*) INTO numOrganizadores FROM TABLE(select Organizada_Por FROM Tabla_Edicion
        WHERE Id = r_edicion.E); 

        IF numOrganizadores > 1 THEN
            DELETE FROM TABLE(SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = r_edicion.E) T
            WHERE T.COLUMN_VALUE.Id = :OLD.Id;
        ELSE
            RAISE_APPLICATION_ERROR(-20014, 'No se puede eliminar al organizador unico de una edicion');
        END IF;
    END LOOP;
    
END organizadorUnico;
/

/* Disparador para borrar una edicion que tiene participantes */

CREATE OR REPLACE TRIGGER edicionConParticipanteA BEFORE DELETE ON Tabla_Amateur
FOR EACH ROW
DECLARE
    CURSOR existeParticipante IS SELECT V.COLUMN_VALUE.Id E FROM TABLE(SELECT inscrito_en FROM Tabla_Amateur WHERE Id = :OLD.Id) V;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    FOR r_edicion IN existeParticipante LOOP
      
        IF NOT(:OLD.Id != r_edicion.T) THEN
            DELETE FROM TABLE(SELECT inscrito_en FROM Tabla_Amateur WHERE Id = r_edicion.E) T WHERE T.COLUMN_VALUE.Id = :OLD.Id; 
        END IF;

    END LOOP;
END edicionConParticipanteA;
/

CREATE OR REPLACE TRIGGER edicionConParticipanteP BEFORE DELETE ON Tabla_Profesional
FOR EACH ROW
DECLARE
    CURSOR existeParticipante IS SELECT V.COLUMN_VALUE.Id E FROM TABLE(SELECT inscrito_en FROM Tabla_Profesional WHERE Id = :OLD.Id) V;
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    FOR r_edicion IN existeParticipante LOOP
      
        IF NOT(:OLD.Id != r_edicion.T) THEN
            DELETE FROM TABLE(SELECT inscrito_en FROM Tabla_Profesional WHERE Id = r_edicion.E) T WHERE T.COLUMN_VALUE.Id = :OLD.Id; 
        END IF;

    END LOOP;
END edicionConParticipanteP;
/
