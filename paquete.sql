CREATE OR REPLACE PROCEDURE Add_Videojuego(Titulo IN VARCHAR, Precio IN NUMBER, 
                            Anio IN NUMBER, Dispositivos IN ListaDispositivos) IS
BEGIN
    
    INSERT INTO Tabla_Videojuego VALUES(Videojuego(seqVideojuego.NEXTVAL,Titulo,Precio
                                        ,Anio, Dispositivos));

END;
/