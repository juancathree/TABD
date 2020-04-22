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
