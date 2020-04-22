import java.sql.*;
import java.sql.Date;
import java.util.*;
import oracle.jdbc.*;
import oracle.jdbc.driver.*;


public class Main
{
	private static Scanner teclado;
	public static DataHandler dl;

	/**
	 * Pantalla de inicio
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws Exception
	{
		teclado = new Scanner(System.in);
		
		dl = new DataHandler();
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
				case 1: administrarTorneos(); break;
				case 2: administrarOrganizadores(); break;
				case 3: administrarVideojuegos(); break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para administrar organizadores
	 * @throws Exception
	 */
	public static void administrarOrganizadores() throws Exception
	{
		int opcionOrganizador = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Administrar Organizadores. ¿Qué desea hacer?");
				System.out.println("1-. Añadir nuevo organizador");
				System.out.println("2-. Ver organizadores disponibles");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					opcionOrganizador = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto");
					teclado = new Scanner(System.in);
				}
			}
			
			switch(opcionOrganizador)
			{
				case 1: anadirNuevoOrganizador(); break;
				case 2: verOrganizadoresDisponibles(); break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para crear un nuevo organizador
	 * @throws Exception
	 */
	public static void anadirNuevoOrganizador() throws Exception
	{
		String nombre, apellidos, nacimiento, dni;
		try
		{
			System.out.println("\n\nIntroduzca los datos del organizador.");
			teclado.nextLine();
			System.out.print("Nombre: ");
			nombre = teclado.nextLine();
			System.out.print("Apellidos: ");
			apellidos = teclado.nextLine();
			System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
			nacimiento = teclado.nextLine();
			System.out.print("DNI: ");
			dni = teclado.nextLine();
			Date nacDate = Date.valueOf(nacimiento);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesOrganizador.Add_Organizador(?,?,?,?)}");
			call.setString(1, nombre);
			call.setString(2, apellidos);
			call.setDate(3, nacDate);
			call.setString(4, dni);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}
	
	
	/**
	 * Pantalla para ver los organizadores disponibles en la asociación
	 * @throws Exception
	 */
	public static void verOrganizadoresDisponibles() throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Organizador");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Nombre") + ", " + rs.getString("Apellidos")
			+ ". Nacido el " + rs.getDate("Nacimiento"));
		}
		rs.close();
		stmt.close();
		
		/* A continuación, el usuario podrá modificar o borrar un organizador, o ver sus ediciones organizadas */
		boolean salir = false, salirMod = false;
		int idElegido = 0, opcion = -1;
		
		while(!salir)
		{
			salirMod = false;
			try
			{
				System.out.print("\n\nSeleccione un ID (escriba 0 para salir): ");
				idElegido = teclado.nextInt();
			}
			catch(Exception e)
			{
				System.out.println("Formato introducido incorrecto");
				teclado = new Scanner(System.in);
			}
			
			if(idElegido > 0)
			{
				while(!salirMod)
				{
					System.out.println("¿Qué desea hacer?");
					System.out.println("1-. Modificar datos del organizador");
					System.out.println("2-. Eliminar organizador");
					System.out.println("3-. Ver ediciones organizadas");
					System.out.println("Otro-. Salir");
					System.out.print("Su opción: ");
					try {
						opcion = teclado.nextInt();
						salirMod = true;
					}
					catch(Exception e)
					{
						System.out.println("Formato de entrada incorrecto.");
						teclado = new Scanner(System.in);
					}
				}
				switch(opcion)
				{
					case 1: modificarDatosOrganizador(idElegido); break;
					case 2: eliminarOrganizador(idElegido); break;
					case 3: verEdicionesOrganizadas(idElegido);break;
					default: salir = true; break;
				}
			}
			else
				salir = true;
		}
	}
	
	
	/**
	 * Pantalla para modificar los datos de un organizador concreto
	 * @param id
	 * @throws Exception
	 */
	public static void modificarDatosOrganizador(int id) throws Exception
	{
		String nombre, apellidos, nacimiento, dni;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos del organizador.");
			teclado.nextLine();
			System.out.print("Nombre: ");
			nombre = teclado.nextLine();
			System.out.print("Apellidos: ");
			apellidos = teclado.nextLine();
			System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
			nacimiento = teclado.nextLine();
			System.out.print("DNI: ");
			dni = teclado.nextLine();
			Date nacDate = Date.valueOf(nacimiento);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesOrganizador.Update_Organizador(?,?,?,?,?)}");
			call.setInt(1, id);
			call.setString(2, nombre);
			call.setString(3, apellidos);
			call.setDate(4, nacDate);
			call.setString(5, dni);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}
	
	
	/**
	 * Pantalla para eliminar un organizador concreto
	 * @param id
	 * @throws Exception
	 */
	public static void eliminarOrganizador(int id) throws Exception
	{
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesOrganizador.Delete_Organizador(?)}");
			call.setInt(1, id);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Pantalla para ver las ediciones organizadas por un organizador concreto
	 * @param id
	 * @throws Exception
	 */
	public static void verEdicionesOrganizadas(int id) throws Exception
	{
		
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
				case 2: verVideojuegosDisponibles(); break;
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
		String disp = " ";
		int nMaxDisp = 10;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos del videojuego.");
			teclado.nextLine();
			System.out.print("Título: ");
			titulo = teclado.nextLine();
			System.out.print("Precio: ");
			precio = teclado.nextDouble();
			System.out.print("Año de lanzamiento: ");
			anio = teclado.nextInt();
			teclado.nextLine();
		
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesVideojuego.Add_Videojuego(?,?,?)}");
			call.setString(1, titulo);
			call.setDouble(2, precio);
			call.setInt(3, anio);
			call.executeUpdate();
		
			Statement stmt = DataHandler.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(Id) FROM Tabla_Videojuego");
			rs.next();
			int id = rs.getInt(1);
		
			while(nMaxDisp > 0 && !disp.isEmpty())
			{
				System.out.println("Introduzca los dispositivos en los que está disponible. Máximo: "
									+ nMaxDisp + " dispositivos");
				System.out.print("Nombre del dispositivo: ");
				disp = teclado.nextLine();
				if(!disp.isEmpty())
				{
					CallableStatement callDisp = DataHandler.conn.
							prepareCall("{call funcionesVideojuego.Add_Dispositivo_Videojuego(?,?,?)");
					callDisp.setInt(1, id);
					callDisp.setInt(2, 10 - nMaxDisp + 1);
					callDisp.setString(3, disp);
					callDisp.executeUpdate();
					nMaxDisp--;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}
	
	
	/**
	 * Pantalla para devolver todos los videojuegos disponibles
	 */
	public static void verVideojuegosDisponibles() throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Videojuego");
	
		while(rs.next())
		{
			System.out.print(rs.getInt("Id") + "-. " + rs.getString("Titulo") + ", " + rs.getDouble("Precio")
			+ "€ (" + rs.getInt("Anio") + "). Dispositivos: ");
			Array array = rs.getArray("Dispositivos");
			if(array != null)
			{
				Object[] dispositivos = (Object[]) array.getArray();
				for(int i = 0; i < dispositivos.length; i++)
					System.out.print(dispositivos[i] + " ");
			}
			else
				System.out.print("ninguno");
			System.out.println("");
		}
		rs.close();
		stmt.close();
		
		/* A continuación, el usuario podrá modificar o borrar un videojuego */
		boolean salir = false, salirMod = false;
		int idElegido = 0, opcion = -1;
		
		while(!salir)
		{
			salirMod = false;
			try
			{
				System.out.print("Seleccione un ID (escriba 0 para salir): ");
				idElegido = teclado.nextInt();
			}
			catch(Exception e)
			{
				System.out.println("Formato introducido incorrecto");
				teclado = new Scanner(System.in);
			}
			
			if(idElegido > 0)
			{
				while(!salirMod)
				{
					System.out.println("¿Qué desea hacer?");
					System.out.println("1-. Modificar datos del videojuego");
					System.out.println("2-. Eliminar videojuego");
					System.out.println("Otro-. Salir");
					System.out.print("Su opción: ");
					try {
						opcion = teclado.nextInt();
						salirMod = true;
					}
					catch(Exception e)
					{
						System.out.println("Formato de entrada incorrecto.");
						teclado = new Scanner(System.in);
					}
				}
				
				switch(opcion)
				{
					case 1: modificarDatosVideojuego(idElegido); break;
					case 2: eliminarVideojuego(idElegido); break;
					default: salir = true; break;
				}
			}
			else
				salir = true;
		}
	}
	
	
	/**
	 * Función para modificar los datos del videojuego dado
	 * @param id
	 */
	public static void modificarDatosVideojuego(int id)
	{
		String titulo;
		double precio;
		int anio;
		String disp = " ";
		
		try
		{
			System.out.println("\n\nIntroduzca los datos del videojuego.");
			teclado.nextLine();
			System.out.print("Título: ");
			titulo = teclado.nextLine();
			System.out.print("Precio: ");
			precio = teclado.nextDouble();
			System.out.print("Año de lanzamiento: ");
			anio = teclado.nextInt();
			teclado.nextLine();
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesVideojuego.Update_Videojuego(?,?,?,?)}");
			call.setInt(1,id);
			call.setString(2, titulo);
			call.setDouble(3, precio);
			call.setInt(4, anio);
			call.executeUpdate();
			
			Statement stmt = DataHandler.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM table(select dispositivos "
					+ "from tabla_videojuego where id = " + id + ")");
			rs.next();
			int nMaxDisp = 10 - rs.getInt(1);
			
			while(nMaxDisp > 0 && !disp.isEmpty())
			{
				System.out.println("Introduzca los dispositivos en los que está disponible. Máximo: "
									+ nMaxDisp + " dispositivos");
				System.out.print("Nombre del dispositivo: ");
				disp = teclado.nextLine();
				if(!disp.isEmpty())
				{
					CallableStatement callDisp = DataHandler.conn.
							prepareCall("{call funcionesVideojuego.Add_Dispositivo_Videojuego(?,?,?)");
					callDisp.setInt(1, id);
					callDisp.setInt(2, 10 - nMaxDisp + 1);
					callDisp.setString(3, disp);
					callDisp.executeUpdate();
					nMaxDisp--;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}
	
	
	/**
	 * Función para eliminar un videojuego dado
	 * @param id
	 */
	public static void eliminarVideojuego(int id)
	{
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesVideojuego.Delete_Videojuego(?)}");
			call.setInt(1, id);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}	
}
