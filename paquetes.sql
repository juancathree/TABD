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
        INSERT INTO Tabla_Videojuego VALUES(seqVideojuego.NEXTVAL,Titulo,Precio,Anio,ListaDispositivos());
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
            RAISE_APPLICATION_ERROR(-20001, 'No existe el proyecto con el ID ' || Cod);
    END;

    PROCEDURE Update_Videojuego (Cod IN NUMBER, title IN VARCHAR, price IN NUMBER, year IN NUMBER) IS
    BEGIN
        UPDATE Tabla_Videojuego SET Titulo = title, Precio = price, Anio = year WHERE Id = Cod;
    EXCEPTION
      when no_data_found then
        RAISE_APPLICATION_ERROR(-20001, 'No existe el proyecto con el ID ' || Cod);    
    END;

END funcionesVideojuego;
/
