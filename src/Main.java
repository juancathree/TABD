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
				case 2 : organizador(); break;
				case 3 : participante(); break;
				default: salir = true; break;
			}
		}
		System.out.println("Ha finalizado su sesión");
		dl.closeConnection();
	}
	
	
/******************* ADMINISTRADOR *************************/	
	
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
	 * Pantalla para administrar torneos
	 * @throws Exception
	 */
	public static void administrarTorneos() throws Exception
	{
		int opcionTorneo = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Administrar Torneos. ¿Qué desea hacer?");
				System.out.println("1-. Crear un nuevo torneo");
				System.out.println("2-. Ver torneos creados");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					opcionTorneo = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto");
					teclado = new Scanner(System.in);
				}
			}
			
			switch(opcionTorneo)
			{
				case 1: crearNuevoTorneo(); break;
				case 2: verTorneosCreados(); break;
				default: salir=true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para crear un nuevo torneo
	 * @throws Exception
	 */
	public static void crearNuevoTorneo() throws Exception
	{
		String titulo, reglas;
		int idElegido;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos del torneo.");
			teclado.nextLine();
			System.out.print("Título: ");
			titulo = teclado.nextLine();
			System.out.print("Fichero PDF de reglas: ");
			reglas = teclado.nextLine();
			System.out.println("Seleccione un videojuego: ");
			
			Statement stmt = DataHandler.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Videojuego");
			
			while(rs.next())
				System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo"));
			
			rs.close();
			stmt.close();
			
			System.out.print("Introduzca el ID: ");
			idElegido = teclado.nextInt();
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesTorneo.Add_Torneo(?,?,?)}");
			call.setString(1, titulo);
			call.setString(2, reglas);
			call.setInt(3, idElegido);
			call.executeUpdate();
		}
		catch(SQLException se) {
			System.out.println(se.getErrorCode()+": "+se.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("Error general");
		}
	}
	
	
	/**
	 * Pantalla para ver los torneos existentes
	 * @throws Exception
	 */
	public static void verTorneosCreados() throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Id, Titulo, DEREF(Centrado_En) FROM Tabla_Torneo");
		while(rs.next())
		{
			Struct juego = (Struct) rs.getObject(3);
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo") + ", del videojuego " + juego.getAttributes()[1]);
		}
		rs.close();
		stmt.close();
		
		/* A continuación, el usuario podrá modificar o borrar un torneo */
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
					System.out.println("1-. Modificar datos del torneo");
					System.out.println("2-. Eliminar torneo");
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
					case 1: modificarDatosTorneo(idElegido); break;
					case 2: eliminarTorneo(idElegido); break;
					default: salir = true; break;
				}
			}
			else
				salir = true;
		}
	}
	
	
	/**
	 * Pantalla para modificar los datos de un torneo concreto
	 * @param id
	 * @throws Exception
	 */
	public static void modificarDatosTorneo(int id) throws Exception
	{
		String titulo, reglas;
		int idElegido;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos del torneo.");
			teclado.nextLine();
			System.out.print("Título: ");
			titulo = teclado.nextLine();
			System.out.print("Fichero PDF de reglas: ");
			reglas = teclado.nextLine();
			System.out.println("Seleccione un videojuego: ");
			
			Statement stmt = DataHandler.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Videojuego");
			
			while(rs.next())
				System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo"));
			
			rs.close();
			stmt.close();
			
			System.out.print("Introduzca el ID: ");
			idElegido = teclado.nextInt();
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesTorneo.Update_Torneo(?,?,?,?)}");
			call.setInt(1, id);
			call.setString(2, titulo);
			call.setString(3, reglas);
			call.setInt(4, idElegido);
			call.executeUpdate();
		}
		catch(SQLException se) {
			System.out.println(se.getErrorCode()+": "+se.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("Error general");
		}
	}
	
	
	/**
	 * Pantalla para eliminar un torneo concreto
	 * @param id
	 * @throws Exception
	 */
	public static void eliminarTorneo(int id) throws Exception
	{
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesTorneo.Delete_Torneo(?)}");
			call.setInt(1, id);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		String nombre, apellidos;
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT VALUE(V) FROM Tabla_Organizador V WHERE Id=" + id);
		rs.next();
		Struct org = (Struct) rs.getObject(1);
		nombre = org.getAttributes()[1].toString();
		apellidos = org.getAttributes()[2].toString();
		rs.close();
		stmt.close();
		System.out.println("\nEdiciones organizadas por: " + nombre + " " + apellidos);
		
		stmt = DataHandler.conn.createStatement();
		rs = stmt.executeQuery("SELECT VALUE(V) FROM TABLE(SELECT Organiza FROM Tabla_Organizador WHERE Id="+id+") V");

		while(rs.next())
		{
			Ref refEdi = (Ref) rs.getObject(1);
			Struct edi = (Struct) refEdi.getObject();
			String identificador = edi.getAttributes()[0].toString();
			String inicio = edi.getAttributes()[1].toString();
			String fin = edi.getAttributes()[2].toString();
			Ref refTorneo = (Ref) edi.getAttributes()[5];
			Struct torneo = (Struct) refTorneo.getObject();
			System.out.println(identificador + "-. " + torneo.getAttributes()[1].toString() + 
								". Entre el " + inicio + " y el " +fin);
		}
		
		/* A continuación, el organizador podrá modificar o borrar una edición, o ver sus participantes */
		
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
					System.out.println("1-. Modificar datos de la edición");
					System.out.println("2-. Eliminar edición");
					System.out.println("3-. Ver participantes de la edición");
					System.out.println("4-. Ver organizadores de la edición");
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
					case 1: modificarDatosEdicion(idElegido); break;
					case 2: eliminarEdicion(idElegido); break;
					case 3: verParticipantesEdicion(idElegido); break;
					case 4: verOrganizadoresEdicion(idElegido, id); break;
					default: salir = true; break;
				}
			}
			else
				salir = true;
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
	
	
/****************** ORGANIZADOR *************************/
	
	/**
	 * Pantalla principal del Organizador
	 * @throws Exception
	 */
	public static void organizador() throws Exception
	{
		int opcionOrg = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		int idOrganizador = 0;
		boolean idValido = false;
		String nombreOrganizador = null;
		
		do
		{
			System.out.print("Introduzca su identificador de organizador: ");
			idOrganizador = teclado.nextInt();
			
			try
			{
				Statement stmt = DataHandler.conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT VALUE(V) FROM Tabla_Organizador V WHERE Id=" + idOrganizador);
				rs.next();
				Struct org = (Struct) rs.getObject(1);
				nombreOrganizador = org.getAttributes()[1].toString();
				idValido = true;
				rs.close();
				stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				teclado = new Scanner(System.in);
			}
		}
		while(!idValido);
		
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\nBienvenido, " + nombreOrganizador + ". ¿Qué desea hacer?");
				System.out.println("1-. Crear una nueva edición");
				System.out.println("2-. Ver ediciones organizadas");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try
				{
					opcionOrg = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(opcionOrg)
			{
				case 1: crearNuevaEdicion(idOrganizador); break;
				case 2: verEdicionesOrganizadas(idOrganizador); break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para crear una nueva edición
	 * @throws Exception
	 */
	public static void crearNuevaEdicion(int idOrganizador) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Torneo");
		
		System.out.println("Torneos disponibles: ");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo"));
		}
		rs.close();
		stmt.close();
		
		String inicio, fin;
		int id;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos de la edicion.");
			teclado.nextLine();
			System.out.print("Id del torneo: ");
			id = teclado.nextInt();
			teclado.nextLine();
			System.out.print("Inicio (YYYY-MM-DD): ");
			inicio = teclado.nextLine();
			System.out.print("Final (YYYY-MM-DD): ");
			fin = teclado.nextLine();
			Date inicioDate = Date.valueOf(inicio);
			Date finDate = Date.valueOf(fin);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Add_Edicion(?,?,?,?)}");
			call.setInt(1, idOrganizador);
			call.setInt(2, id);
			call.setDate(3, inicioDate);
			call.setDate(4, finDate);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Pantalla para modificar los datos de una edición concreta
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void modificarDatosEdicion(int idEdicion) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Torneo");
		
		System.out.println("Torneos disponibles: ");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo"));
		}
		rs.close();
		stmt.close();
		
		String inicio, fin;
		int idTorneo;
		
		try
		{
			System.out.println("\n\nIntroduzca los datos de la edicion.");
			teclado.nextLine();
			System.out.print("Id del torneo: ");
			idTorneo = teclado.nextInt();
			teclado.nextLine();
			System.out.print("Inicio (YYYY-MM-DD): ");
			inicio = teclado.nextLine();
			System.out.print("Final (YYYY-MM-DD): ");
			fin = teclado.nextLine();
			Date inicioDate = Date.valueOf(inicio);
			Date finDate = Date.valueOf(fin);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Update_Edicion(?,?,?,?)}");
			call.setInt(1, idEdicion);
			call.setInt(2, idTorneo);
			call.setDate(3, inicioDate);
			call.setDate(4, finDate);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Función para eliminar una edición dada
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void eliminarEdicion(int idEdicion) throws Exception
	{
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Delete_Edicion(?)}");
			call.setInt(1, idEdicion);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Función para ver los participantes
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void verParticipantesEdicion(int idEdicion) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT VALUE(V) FROM TABLE(SELECT Participan"
				+ " FROM Tabla_Edicion WHERE Id="+idEdicion+") V");
		
		while(rs.next())
		{
			Ref refPar = (Ref) rs.getObject(1);
			Struct par = (Struct) refPar.getObject();
			String idParticipante = par.getAttributes()[0].toString();
			String nombreParticipante = par.getAttributes()[1].toString();
			String apellidosParticipante = par.getAttributes()[2].toString();
			System.out.println(idParticipante + "-. " + nombreParticipante + " " + apellidosParticipante);
		}
		
		boolean salir = false;
		int idElegido = 0;
		
		while(!salir)
		{
			try
			{
				System.out.print("Seleccione un ID de participante para dar de baja en la edición (escriba 0 para salir): ");
				idElegido = teclado.nextInt();
				eliminarParticipanteEdicion(idEdicion, idElegido);
				salir = true;
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				teclado = new Scanner(System.in);
			}
		}	
	}
	
	
	/**
	 * Función para ver los organizadores de una edición concreta
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void verOrganizadoresEdicion(int idEdicion, int idOrganizadorVisitante) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT VALUE(V) FROM TABLE(SELECT Organizada_Por"
				+ " FROM Tabla_Edicion WHERE Id="+idEdicion+") V");
		
		while(rs.next())
		{
			Ref refOrg = (Ref) rs.getObject(1);
			Struct org = (Struct) refOrg.getObject();
			String idOrganizador = org.getAttributes()[0].toString();
			String nombreOrganizador = org.getAttributes()[1].toString();
			String apellidosOrganizador = org.getAttributes()[2].toString();
			System.out.println(idOrganizador + "-. " + nombreOrganizador + " " + apellidosOrganizador);
		}
		
		
		int opcion = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("Seleccione una opción");
				System.out.println("1-. Añadir un organizador");
				System.out.println("2-. Quitar un organizador");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try {
					opcion = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(opcion)
			{
				case 1: anadirOrganizadorAEdicion(idEdicion); break;
				case 2: quitarOrganizadorDeEdicion(idEdicion, idOrganizadorVisitante); break;
				default: salir = true; break;
			}
		}
		System.out.println("=================================================");
	}
	
	
	/**
	 * Función para dar de alta a un nuevo organizador 
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void anadirOrganizadorAEdicion(int idEdicion) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Organizador");
		int idElegido = 0;
		
		while(rs.next())
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Nombre") + " " + rs.getString("Apellidos"));
		
		
		boolean salir = false;
		while(!salir)
		{
			try
			{
				System.out.print("Seleccione un ID de organizador (escriba 0 para salir): ");
				idElegido = teclado.nextInt();
				CallableStatement call = DataHandler.conn.prepareCall
						 ("{call funcionesEdicion.Add_Organizador_Edicion(?,?)}");
				call.setInt(1, idEdicion);
				call.setInt(2, idElegido);
				call.executeUpdate();
				
				salir = true;
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				teclado = new Scanner(System.in);
			}
		}
	}
	
	
	/**
	 * Función para dar de baja a un organizador en una edición concreta
	 * @param idEdicion
	 * @param idOrganizadorVisitante
	 * @throws Exception
	 */
	public static void quitarOrganizadorDeEdicion(int idEdicion, int idOrganizadorVisitante) throws Exception
	{
		boolean salir = false;
		int idElegido = 0;
		
		while(!salir)
		{
			try
			{
				System.out.print("Seleccione un ID de organizador (escriba 0 para salir): ");
				idElegido = teclado.nextInt();
				if(idElegido==idOrganizadorVisitante) throw new SecurityException();
				salir = true;
			}
			catch(SecurityException se)
			{
				System.out.println("El organizador actual no puede darse de baja de la edición");
			}
			catch(Exception e)
			{
				System.out.println("Formato introducido incorrecto");
				teclado = new Scanner(System.in);
			}
		}
		
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall
									 ("{call funcionesEdicion.Delete_Organizador_Edicion(?,?)}");
			call.setInt(1, idEdicion);
			call.setInt(2, idElegido);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**************************** PARTICIPANTE ******************************/
	
	/**
	 * Pantalla principal del Participante
	 * @throws Exception
	 */
	public static void participante() throws Exception
	{
		int opcionPar = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		int idParticipante = 0;
		boolean idValido = false;
		String nombreParticipante = null;
		
		do
		{
			System.out.print("Introduzca su identificador de participante (si aún no se ha registrado, "
					+ "escriba -1) : ");
			idParticipante = teclado.nextInt();
			
			if(idParticipante == -1)
				idParticipante = crearParticipante();

			try
			{
				Statement stmt = DataHandler.conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT VALUE(V) FROM Tabla_Participante V WHERE Id=" + idParticipante);
				rs.next();
				Struct org = (Struct) rs.getObject(1);
				nombreParticipante = org.getAttributes()[1].toString();
				idValido = true;
				rs.close();
				stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				teclado = new Scanner(System.in);
			}
		}
		while(!idValido);
		
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\nBienvenido, " + nombreParticipante + ". ¿Qué desea hacer?");
				System.out.println("1-. Editar mis datos personales");
				System.out.println("2-. Inscribirse en una edición");
				System.out.println("3-. Ver ediciones en las que participo");
				System.out.println("Otro-. Salir");
				System.out.print("Su opción: ");
				try
				{
					opcionPar = teclado.nextInt();
					opcionElegida = true;
				}
				catch(Exception e)
				{
					System.out.println("Formato de entrada incorrecto.");
					teclado = new Scanner(System.in);
				}
			}
		
			switch(opcionPar)
			{
				case 1: editarDatosParticipante(idParticipante); break;
				case 2: inscribirseEnEdicion(idParticipante); break;
				case 3: verEdicionesParticipante(idParticipante);break;
				default: salir = true; break;
			}
		}
	}
	
	
	/**
	 * Pantalla para crear un nuevo participante en el sistema
	 * @return
	 * @throws Exception
	 */
	public static int crearParticipante() throws Exception
	{
		String nombre, apellidos, nacimiento, dni, domicilio, email;
		String fotografia, curriculum;
		int modalidad;
		
		try
		{
			System.out.println("\n\nIntroduzca sus datos personales.");
			teclado.nextLine();
			System.out.print("Nombre: ");
			nombre = teclado.nextLine();
			System.out.print("Apellidos: ");
			apellidos = teclado.nextLine();
			System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
			nacimiento = teclado.nextLine();
			System.out.print("DNI: ");
			dni = teclado.nextLine();
			System.out.print("Domicilio: ");
			domicilio = teclado.nextLine();
			System.out.print("Email: ");
			email = teclado.nextLine();
			Date nacDate = Date.valueOf(nacimiento);
		
			do
			{	
				System.out.println("Modalidad: ");
				System.out.println("1-. Amateur");
				System.out.println("2-. Profesional");
				System.out.print("Su opción: ");
				modalidad = teclado.nextInt();
			}
			while(modalidad != 1 && modalidad != 2);
		
			teclado.nextLine();
		
			switch(modalidad)
			{
				case 1: System.out.print("Seleccione una fotografía: "); 
					fotografia = teclado.nextLine();
					CallableStatement call = DataHandler.conn.prepareCall
							("{call funcionesParticipante.Add_Amateur(?,?,?,?,?,?,?)}");
					call.setString(1, nombre);
					call.setString(2, apellidos);
					call.setDate(3, nacDate);
					call.setString(4, dni);
					call.setString(5, domicilio);
					call.setString(6, email);
					call.setString(7, fotografia);
					call.executeUpdate();
					break;
				case 2: System.out.print("Seleccione un fichero de currículum: ");
					curriculum = teclado.nextLine();
					CallableStatement call2 = DataHandler.conn.prepareCall
							("{call funcionesParticipante.Add_Profesional(?,?,?,?,?,?,?)}");
					call2.setString(1, nombre);
					call2.setString(2, apellidos);
					call2.setDate(3, nacDate);
					call2.setString(4, dni);
					call2.setString(5, domicilio);
					call2.setString(6, email);
					call2.setString(7, curriculum);
					call2.executeUpdate();
					break;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(Id) FROM Tabla_Participante");
		rs.next();
		int id = rs.getInt(1);
		return id;
	}
	
	
	/**
	 * Pantalla para editar los datos de un participante
	 * @param id
	 * @throws Exception
	 */
	public static void editarDatosParticipante(int id) throws Exception
	{
		String nombre, apellidos, nacimiento, dni, domicilio, email;
		
		try
		{
			System.out.println("\n\nIntroduzca sus datos personales.");
			teclado.nextLine();
			System.out.print("Nombre: ");
			nombre = teclado.nextLine();
			System.out.print("Apellidos: ");
			apellidos = teclado.nextLine();
			System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
			nacimiento = teclado.nextLine();
			Date nacDate = Date.valueOf(nacimiento);
			System.out.print("DNI: ");
			dni = teclado.nextLine();
			System.out.print("Domicilio: ");
			domicilio = teclado.nextLine();
			System.out.print("Email: ");
			email = teclado.nextLine();
		
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesParticipante.Update_Participante(?,?,?,?,?,?,?)}");
			call.setInt(1, id);
			call.setString(2, nombre);
			call.setString(3, apellidos);
			call.setDate(4, nacDate);
			call.setString(5, dni);
			call.setString(6, domicilio);
			call.setString(7, email);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Pantalla para inscribirse en una edición de un torneo
	 * @param idParticipante
	 * @throws Exception
	 */
	public static void inscribirseEnEdicion(int idParticipante) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Edicion");
		while(rs.next())
		{
			Statement stmt2 = DataHandler.conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM TABLE(SELECT Participan FROM "
					+ "Tabla_Edicion WHERE Id = " + rs.getObject(1) + ")"); 
			rs2.next();
			
			Ref refTorneo = (Ref) rs.getObject(6);
			Struct torneo = (Struct) refTorneo.getObject();
			String nombreTorneo = torneo.getAttributes()[1].toString();
			System.out.println(rs.getInt("Id") + "-. " + nombreTorneo + ": entre el " + rs.getDate("Inicio")
			+ " y el " + rs.getDate("Fin") + ". Participantes inscritos: " + rs2.getInt(1));
			
			stmt2.close();
			rs2.close();
		}
		stmt.close();
		rs.close();
		
		
		boolean salir = false;
		int idEdicion = 0;
		
		while(!salir)
		{
			try
			{
				System.out.print("Seleccione un ID de edición (escriba 0 para salir): ");
				idEdicion = teclado.nextInt();
				if(idEdicion == 0)
					salir = true;
				else
				{
					CallableStatement call = DataHandler.conn.prepareCall
						 ("{call funcionesEdicion.Add_Participante_Edicion(?,?)}");
					call.setInt(1, idEdicion);
					call.setInt(2, idParticipante);
					call.executeUpdate();
					salir = true;
				}	
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				teclado = new Scanner(System.in);
			}
		}
	}
	
	
	/**
	 * Pantalla para ver las ediciones en las que está inscrito un participante concreto
	 * @param idParticipante
	 * @throws Exception
	 */
	public static void verEdicionesParticipante(int idParticipante) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Edicion");
		
		stmt = DataHandler.conn.createStatement();
		rs = stmt.executeQuery("SELECT VALUE(V) FROM TABLE(SELECT Inscrito_En FROM Tabla_Participante WHERE Id="+idParticipante+") V");
		
		while(rs.next())
		{
			Ref refEdi = (Ref) rs.getObject(1);
			Struct edi = (Struct) refEdi.getObject();
			String identificador = edi.getAttributes()[0].toString();
			String inicio = edi.getAttributes()[1].toString();
			String fin = edi.getAttributes()[2].toString();
			Ref refTorneo = (Ref) edi.getAttributes()[5];
			Struct torneo = (Struct) refTorneo.getObject();
			System.out.println(identificador + "-. " + torneo.getAttributes()[1].toString() + 
								". Entre el " + inicio + " y el " +fin);
		}
		rs.close();
		stmt.close();
		
		/* A continuación, el usuario podrá darse de baja de una edición, o ver sus detalles */
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
					System.out.println("1-. Darse de baja de la edición");
					System.out.println("2-. Ver detalles de la edición");
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
					case 1: eliminarParticipanteEdicion(idElegido, idParticipante); break;
					case 2: verDetallesEdicion(idElegido); break;
					default: salir = true; break;
				}
			}
			else
				salir = true;
		}		
	}
	
	
	/**
	 * @param idEdicion
	 * @param idParticipante
	 * @throws Exception
	 */
	public static void eliminarParticipanteEdicion(int idEdicion, int idParticipante) throws Exception
	{
		try
		{
			CallableStatement call = DataHandler.conn.prepareCall
									 ("{call funcionesEdicion.Delete_Participante_Edicion(?,?)}");
			call.setInt(1, idEdicion);
			call.setInt(2, idParticipante);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Pantalla para ver datos de una edición, el torneo y el videojuego al que pertenece
	 * @param idEdicion
	 * @throws Exception
	 */
	public static void verDetallesEdicion(int idEdicion) throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Pertenece_A FROM Tabla_Edicion WHERE Id="+idEdicion);
		rs.next();
		Ref refTorneo = (Ref) rs.getObject(1);
		Struct torneo = (Struct) refTorneo.getObject();
		System.out.println("\n\nDatos de la edición:");
		System.out.println("Título del torneo: " + torneo.getAttributes()[1].toString());
		Ref refVideojuego = (Ref) torneo.getAttributes()[3];
		Struct videojuego = (Struct) refVideojuego.getObject();
		System.out.println("Videojuego: " + videojuego.getAttributes()[1].toString());
		System.out.println("Precio: " + videojuego.getAttributes()[2].toString() + "€");
		System.out.println("Año de lanzamiento: " + videojuego.getAttributes()[3].toString());
		System.out.print("Dispositivos en los que está disponible: ");
		Array array = (Array) videojuego.getAttributes()[4];
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
}