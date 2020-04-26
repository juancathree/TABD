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
				System.out.println("\n\nBienvenido a la Asociaci�n Espa�ola de Videojuegos");
				System.out.println("Seleccione el perfil con el que desea acceder a la aplicaci�n");
				System.out.println("1-. Administrador");
				System.out.println("2-. Organizador");
				System.out.println("3-. Participante");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
				//case 3 : participante(); break;
				default: salir = true; break;
			}
		}
		System.out.println("Ha finalizado su sesi�n");
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
				System.out.println("\n\n Bienvenido, administrador. �Qu� desea hacer?");
				System.out.println("1-. Administrar torneos");
				System.out.println("2-. Administrar organizadores");
				System.out.println("3-. Administrar videojuegos");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
				System.out.println("\n\n Administrar Organizadores. �Qu� desea hacer?");
				System.out.println("1-. A�adir nuevo organizador");
				System.out.println("2-. Ver organizadores disponibles");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
	 * Pantalla para ver los organizadores disponibles en la asociaci�n
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
		
		/* A continuaci�n, el usuario podr� modificar o borrar un organizador, o ver sus ediciones organizadas */
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
					System.out.println("�Qu� desea hacer?");
					System.out.println("1-. Modificar datos del organizador");
					System.out.println("2-. Eliminar organizador");
					System.out.println("3-. Ver ediciones organizadas");
					System.out.println("Otro-. Salir");
					System.out.print("Su opci�n: ");
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
				System.out.println("\n\n Administrar Videojuegos. �Qu� desea hacer?");
				System.out.println("1-. A�adir nuevo videojuego");
				System.out.println("2-. Ver videojuegos disponibles");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
			System.out.print("T�tulo: ");
			titulo = teclado.nextLine();
			System.out.print("Precio: ");
			precio = teclado.nextDouble();
			System.out.print("A�o de lanzamiento: ");
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
				System.out.println("Introduzca los dispositivos en los que est� disponible. M�ximo: "
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
			+ "� (" + rs.getInt("Anio") + "). Dispositivos: ");
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
		
		/* A continuaci�n, el usuario podr� modificar o borrar un videojuego */
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
					System.out.println("�Qu� desea hacer?");
					System.out.println("1-. Modificar datos del videojuego");
					System.out.println("2-. Eliminar videojuego");
					System.out.println("Otro-. Salir");
					System.out.print("Su opci�n: ");
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
	 * Funci�n para modificar los datos del videojuego dado
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
			System.out.print("T�tulo: ");
			titulo = teclado.nextLine();
			System.out.print("Precio: ");
			precio = teclado.nextDouble();
			System.out.print("A�o de lanzamiento: ");
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
				System.out.println("Introduzca los dispositivos en los que est� disponible. M�ximo: "
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
	 * Funci�n para eliminar un videojuego dado
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

	/**
	 * Pantalla para organizador
	 * @throws Exception
	 */
	public static void organizador() throws Exception
	{
		int opcionOrg = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Bienvenido, organizador. �Qu� desea hacer?");
				System.out.println("1-. Organizar ediciones");
				System.out.println("2-. Organizar organizadores");
				System.out.println("3-. Organizar participantes");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
				try {
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
				case 1: organizarEdiciones(); break;
				case 2: organizarOrganizadores(); break;
				case 3: organizarParticipantes(); break;
				default: salir = true; break;
			}
		}
	}

	/**
	 * Pantalla para organizar ediciones
	 * @throws Exception
	 */
	public static void organizarEdiciones() throws Exception
	{
		int opcionOrganizador = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Organizar Ediciones. �Qu� desea hacer?");
				System.out.println("1-. Crear edicion");
				System.out.println("2-. Ver organizadores de una edicion");
				System.out.println("3-. Ver ediciones organizadas");
				System.out.println("4-. Modificar Edicion");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
				case 1: anadirNuevaEdicion(); break;
				case 2: verOrganizadoresEdicion(); break;
				case 3: verEdicionesOrganizadas(); break;
				case 4: modificarEdicion(); break;
				default: salir = true; break;
			}
		}
	}

	/**
	 * Pantalla para crear una edicion
	 * @throws Exception
	 */
	public static void anadirNuevaEdicion() throws Exception
	{
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Torneo");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Titulo"));
		}
		rs.close();
		stmt.close();

		String inicio, fin, id;
		try
		{
			System.out.println("\n\nIntroduzca los datos de la edicion.");
			teclado.nextLine();
			System.out.print("Id del torneo: ");
			id = teclado.nextLine();
			System.out.print("Inicio (YYYY-MM-DD): ");
			inicio = teclado.nextLine();
			System.out.print("Final (YYYY-MM-DD): ");
			fin = teclado.nextLine();
			Date inicioDate = Date.valueOf(inicio);
			Date finDate = Date.valueOf(fin);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Add_Edicion_Torneo(?,?,?)}");
			call.setString(1, id);
			call.setDate(2, inicioDate);
			call.setDate(3, finDate);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}


	/**
	 * Pantalla para ver organizadores de una edicion
	 * @throws Exception
	 */
	public static void verOrganizadoresEdicion() throws Exception
	{

		String dni;
		try
		{
			System.out.println("\n\nIntroduce dni: ");
			dni = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}

		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DEREF(O.Organiza) FROM Tabla_Organizador O where O.Dni = " + dni);
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. desde " + rs.getString("Inicio") + " hasta " + rs.getString("Fin"));
		}
		rs.close();


		String id;
		try
		{
			System.out.println("\n\nSeleccione la edicion: ");
			id = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}

		rs = stmt.executeQuery("SELECT Organizada_Por FROM Tabla_Edicion WHERE Id = " + id);
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Nombre") + " " + rs.getString("Apellidos"));
		}
		rs.close();
		stmt.close();
	}

	/**
	 * Pantalla para ver ediciones organizadas
	 * @throws Exception
	 */
	public static void verEdicionesOrganizadas() throws Exception
	{

		String dni;
		try
		{
			System.out.println("\n\nIntroduce dni: ");
			dni = teclado.nextLine();
		}
		catch(Exception e)
		{
					System.out.println("Formato de entrada incorrecto");
					teclado = new Scanner(System.in);
		}
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DEREF(REF(e)) FROM TABLE(SELECT Organiza FROM Tabla_Organizador WHERE dni = " + dni +")) e");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. desde " + rs.getString("Inicio") + " hasta " + rs.getString("Fin"));
		}
		rs.close();
		stmt.close();
	}

	/**
	 * Pantalla para ver ediciones organizadas
	 * @throws Exception
	 */
	public static void modificarEdicion() throws Exception
	{
		verEdicionesOrganizadas();
		String id;
		try
		{
			System.out.println("\n\nSelecciona la edicion: ");
			id = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}

		String inicio, fin;
		try
		{
			System.out.println("\n\nIntroduzca los datos de la edicion.");
			teclado.nextLine();
			System.out.print("Inicio (YYYY-MM-DD): ");
			inicio = teclado.nextLine();
			System.out.print("Final (YYYY-MM-DD): ");
			fin = teclado.nextLine();
			Date inicioDate = Date.valueOf(inicio);
			Date finDate = Date.valueOf(fin);
			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Update_Edicion(?,?,?)}");
			call.setString(1, id);
			call.setDate(2, inicioDate);
			call.setDate(3, finDate);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}
	}


	/**
	 * Pantalla para organizar organizadores
	 * @throws Exception
	 */
	public static void organizarOrganizadores() throws Exception
	{
		int opcionOrganizador = 0;
		boolean opcionElegida = false;
		boolean salir = false;
		
		while(!salir)
		{
			opcionElegida = false;
			while(!opcionElegida)
			{
				System.out.println("\n\n Organizar Organizadores. �Qu� desea hacer?");
				System.out.println("1-. Añadir organizador");
				System.out.println("2-. Quitar organizador");
				System.out.println("Otro-. Salir");
				System.out.print("Su opci�n: ");
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
				case 1: anadirOrganizador(); break;
				case 2: quitarOranizador(); break;
				default: salir = true; break;
			}
		}
	}

	/**
	 * Pantalla para añadir un organizador a una edicion
	 * @throws Exception
	 */
	public static void anadirOrganizador() throws Exception
	{
		verEdicionesOrganizadas();
		String idE, idO;
		try
		{
			System.out.println("\n\nSelecciona la edicion: ");
			idE = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}
		
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tabla_Organizador");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Nombre") + ", " + rs.getString("Apellidos")
			+ ". Nacido el " + rs.getDate("Nacimiento"));
		}
		rs.close();
		
		try
		{
			System.out.println("\n\nSelecciona el organizador a añadir: ");
			idO = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}

		try
		{			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Add_Organizador_Edicion(?,?)}");
			call.setString(1, idE);
			call.setString(2, idO);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}

	}

		/**
	 * Pantalla para quitar un organziador
	 * @throws Exception
	 */
	public static void quitarOranizador() throws Exception
	{
		verEdicionesOrganizadas();
		String idE, idO;
		try
		{
			System.out.println("\n\nSelecciona la edicion: ");
			idE = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}
		
		Statement stmt = DataHandler.conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Organizada_Por FROM Tabla_Edicion");
		
		while(rs.next())
		{
			System.out.println(rs.getInt("Id") + "-. " + rs.getString("Nombre") + ", " + rs.getString("Apellidos")
			+ ". Nacido el " + rs.getDate("Nacimiento"));
		}
		rs.close();
		
		try
		{
			System.out.println("\n\nSelecciona el organizador a quitar: ");
			idO = teclado.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("Formato de entrada incorrecto");
			teclado = new Scanner(System.in);
		}

		try
		{			
			CallableStatement call = DataHandler.conn.prepareCall("{call funcionesEdicion.Delete_Organizador_Edicion(?,?)}");
			call.setString(1, idE);
			call.setString(2, idO);
			call.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error al introducir un campo en el formulario");
		}

	}




}

