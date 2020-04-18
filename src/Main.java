import java.sql.*;
import java.util.*;

public class Main
{
	private static Scanner teclado;

	/**
	 * Pantalla de inicio
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws Exception
	{
		teclado = new Scanner(System.in);
		
		DataHandler dl = new DataHandler();
		dl.getDBConnection();
		
		int perfil = 0;
		boolean opcionElegida;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{	
				System.out.println("\n\nBienvenido a la Asociación Española de Videojuegos");
				System.out.println("Seleccione el perfil con el que desea acceder a la aplicación");
				System.out.println("1-. Administrador");
				System.out.println("2-. Organizador");
				System.out.println("3-. Participante");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					perfil = teclado.nextInt();
					opcionElegida = true;
				}	
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(perfil)
			{
				case 1 : administrador(); break;
				//case 2 : organizador(); break;
				//case 3 : participante(); break;
				default: salir = true; break;
			}
		}
		System.out.println("Ha finalizado su sesión");
		dl.closeConnection();
	}
	
	
	/**
	 * Pantalla principal del Administrador
	 * @throws SQLException
	 */
	public static void administrador() throws Exception
	{
		int opcionAdmin = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Bienvenido, administrador. ¿Qué desea hacer?");
				System.out.println("1-. Administrar torneos");
				System.out.println("2-. Administrar organizadores");
				System.out.println("3-. Administrar videojuegos");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					opcionAdmin = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(opcionAdmin)
			{
				//case 1: administrarTorneos(); break;
				//case 2: administrarOrganizadores(); break;
				case 3: administrarVideojuegos(); break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para administrar videojuegos
	 * @throws Exception
	 */
	public static void administrarVideojuegos() throws Exception
	{
		int opcionVideojuego = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Administrar Videojuegos. ¿Qué desea hacer?");
				System.out.println("1-. Añadir nuevo videojuego");
				System.out.println("2-. Ver videojuegos disponibles");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					opcionVideojuego = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(opcionVideojuego)
			{
				case 1: anadirNuevoVideojuego(); break;
				//case 2: verVideojuegosDisponibles(); break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para crear un nuevo videojuego
	 * @throws Exception
	 */
	public static void anadirNuevoVideojuego() throws Exception
	{
		String titulo;
		double precio;
		int anio;
		String[] dispositivos = new String[10];
		String disp = " ";
		int nMaxDisp = 10;
		
		System.out.println("\n\nIntroduzca los datos del videojuego. Pulse Enter si desea dejar un campo en blanco");
		teclado.nextLine();
		System.out.print("Título: ");
		titulo = teclado.nextLine();
		System.out.print("Precio: ");
		precio = teclado.nextDouble();
		System.out.print("Año de lanzamiento: ");
		anio = teclado.nextInt();
		teclado.nextLine();
		
		while(nMaxDisp > 0 && !disp.isEmpty())
		{
			System.out.println("Introduzca los dispositivos en los que está disponible. Máximo: "
								+ nMaxDisp + " dispositivos");
			System.out.print("Nombre del dispositivo: ");
			disp = teclado.nextLine();
			if(!disp.isEmpty())
			{
				dispositivos[10 - nMaxDisp] = disp;
				nMaxDisp--;
			}
		}
		
		Array array = DataHandler.conn.createArrayOf("ListaDispositivos", dispositivos);
		
		CallableStatement call = DataHandler.conn.prepareCall("{call Add_Videojuego(?,?,?,?)}");
		call.setString(1, titulo);
		call.setDouble(2, precio);
		call.setInt(3, anio);
		call.setArray(4, array);
		call.executeUpdate();
	}
	
	
	
	
	
}
