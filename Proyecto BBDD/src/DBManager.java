
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

/**
 *
 *Clase con los metodos para la conexion y uso de la base de datos
 * @author Miguel 羘gel
 */
public class DBManager {

    // Conexi贸n a la base de datos
    private static Connection conn = null;

    // Configuraci贸n de la conexi贸n a la base de datos
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "tienda";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String DB_MSQ_CONN_OK = "CONEXI覰 CORRECTA";
    private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXI覰";

    // Configuraci贸n de la tabla Clientes
    private static final String DB_CLI = "clientes";
    private static final String DB_CLI_SELECT = "SELECT * FROM " + DB_CLI;
    private static final String DB_CLI_ID = "id";
    private static final String DB_CLI_NOM = "nombre";
    private static final String DB_CLI_DIR = "direccion";

    //////////////////////////////////////////////////
    // M脡TODOS DE CONEXI脫N A LA BASE DE DATOS
    //////////////////////////////////////////////////
    ;
    
    /**
     * Intenta cargar el JDBC driver.
     * @return true si pudo cargar el driver, false en caso contrario
     */
    public static boolean loadDriver() {
        try {
            System.out.print("Cargando Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("OK!");
            return true;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Intenta conectar con la base de datos.
     *
     * @return true si pudo conectarse, false en caso contrario
     */
    public static boolean connect() {
        try {
            System.out.print("Conectando a la base de datos...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("OK!");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Comprueba la conexi贸n y muestra su estado por pantalla
     *
     * @return true si la conexi贸n existe y es v谩lida, false en caso contrario
     */
    public static boolean isConnected() {
        // Comprobamos estado de la conexi贸n
        try {
            if (conn != null && conn.isValid(0)) {
                System.out.println(DB_MSQ_CONN_OK);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(DB_MSQ_CONN_NO);
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la conexi贸n con la base de datos
     */
    public static void close() {
        try {
            System.out.print("Cerrando la conexion...");
            conn.close();
            System.out.println("OK!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // METODOS DE TABLA CLIENTES
    //////////////////////////////////////////////////
    ;
    
    // Devuelve 
    // Los argumentos indican el tipo de ResultSet deseado
    /**
     * Obtiene toda la tabla clientes de la base de datos
     * @param resultSetType Tipo de ResultSet
     * @param resultSetConcurrency Concurrencia del ResultSet
     * @return ResultSet (del tipo indicado) con la tabla, null en caso de error
     */
    public static ResultSet getTablaClientes(int resultSetType, int resultSetConcurrency) {
        try {
            PreparedStatement stmt = conn.prepareStatement(DB_CLI_SELECT, resultSetType, resultSetConcurrency);
            ResultSet rs = stmt.executeQuery();
            //stmt.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Obtiene toda la tabla clientes de la base de datos
     *
     * @return ResultSet (por defecto) con la tabla, null en caso de error
     */
    public static ResultSet getTablaClientes() {
        return getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Imprime por pantalla el contenido de la tabla clientes
     */
    public static void printTablaClientes() {
        try {
            ResultSet rs = getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            while (rs.next()) {
                int id = rs.getInt(DB_CLI_ID);
                String n = rs.getString(DB_CLI_NOM);
                String d = rs.getString(DB_CLI_DIR);
                System.out.println(id + "\t" + n + "\t" + d);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // M脡TODOS DE UN SOLO CLIENTE
    //////////////////////////////////////////////////
    ;
    
    /**
     * Solicita a la BD el cliente con id indicado
     * @param id id del cliente
     * @return ResultSet con el resultado de la consulta, null en caso de error
     */
    public static ResultSet getCliente(int id) {
        try {
            // Realizamos la consulta SQL
        	String sql = DB_CLI_SELECT + " WHERE " + DB_CLI_ID + "='" + id + "';";
            PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery();
            //stmt.close();
            
            // Si no hay primer registro entonces no existe el cliente
            if (!rs.first()) {
                return null;
            }

            // Todo bien, devolvemos el cliente
            return rs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Comprueba si en la BD existe el cliente con id indicado
     *
     * @param id id del cliente
     * @return verdadero si existe, false en caso contrario
     */
    public static boolean existsCliente(int id) {
        try {
            // Obtenemos el cliente
            ResultSet rs = getCliente(id);

            // Si rs es null, se ha producido un error
            if (rs == null) {
                return false;
            }

            // Si no existe primer registro
            if (!rs.first()) {
                rs.close();
                return false;
            }

            // Todo bien, existe el cliente
            rs.close();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Imprime los datos del cliente con id indicado
     *
     * @param id id del cliente
     */
    public static void printCliente(int id) {
        try {
            // Obtenemos el cliente
            ResultSet rs = getCliente(id);
            if (rs == null || !rs.first()) {
                System.out.println("Cliente " + id + " NO EXISTE");
                return;
            }
            
            // Imprimimos su informaci贸n por pantalla
            int cid = rs.getInt(DB_CLI_ID);
            String nombre = rs.getString(DB_CLI_NOM);
            String direccion = rs.getString(DB_CLI_DIR);
            System.out.println("Cliente " + cid + "\t" + nombre + "\t" + direccion);

        } catch (SQLException ex) {
            System.out.println("Error al solicitar cliente " + id);
            ex.printStackTrace();
        }
    }

    /**
     * Solicita a la BD insertar un nuevo registro cliente
     *
     * @param nombre nombre del cliente
     * @param direccion direcci贸n del cliente
     * @return verdadero si pudo insertarlo, false en caso contrario
     */
    public static boolean insertCliente(String nombre, String direccion) {
        try {
            // Obtenemos la tabla clientes
            System.out.print("Insertando cliente " + nombre + "...");
            ResultSet rs = getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            // Insertamos el nuevo registro
            rs.moveToInsertRow();
            rs.updateString(DB_CLI_NOM, nombre);
            rs.updateString(DB_CLI_DIR, direccion);
            rs.insertRow();

            // Todo bien, cerramos ResultSet y devolvemos true
            rs.close();
            System.out.println("OK!");
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Solicita a la BD modificar los datos de un cliente
     *
     * @param id id del cliente a modificar
     * @param nombre nuevo nombre del cliente
     * @param direccion nueva direcci贸n del cliente
     * @return verdadero si pudo modificarlo, false en caso contrario
     */
    public static boolean updateCliente(int id, String nuevoNombre, String nuevaDireccion) {
        try {
            // Obtenemos el cliente
            System.out.print("Actualizando cliente " + id + "... ");
            ResultSet rs = getCliente(id);

            // Si no existe el Resultset
            if (rs == null) {
                System.out.println("Error. ResultSet null.");
                return false;
            }

            // Si tiene un primer registro, lo eliminamos
            if (rs.first()) {
                rs.updateString(DB_CLI_NOM, nuevoNombre);
                rs.updateString(DB_CLI_DIR, nuevaDireccion);
                rs.updateRow();
                rs.close();
                System.out.println("OK!");
                return true;
            } else {
                System.out.println("ERROR. ResultSet vac铆o.");
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Solicita a la BD eliminar un cliente
     *
     * @param id id del cliente a eliminar
     * @return verdadero si pudo eliminarlo, false en caso contrario
     */
    public static boolean deleteCliente(int id) {
        try {
            System.out.print("Eliminando cliente " + id + "... ");

            // Obtenemos el cliente
            ResultSet rs = getCliente(id);

            // Si no existe el Resultset
            if (rs == null) {
                System.out.println("ERROR. ResultSet null.");
                return false;
            }

            // Si existe y tiene primer registro, lo eliminamos
            if (rs.first()) {
                rs.deleteRow();
                rs.close();
                System.out.println("OK!");
                return true;
            } else {
                System.out.println("ERROR. ResultSet vac铆o.");
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    //////////////////////////////////////////
    //Volcar datos a ficheros
    /////////////////////////////////////////
    
    public static void volcarDatos(String ruta) {
    	String rutaGeneral ="Ficheros/"+ruta;
    	File f = new File(rutaGeneral);
    	
    	try {
    		FileWriter wf = new FileWriter(f);
    		
    		wf.write(DB_NAME+"\t"+DB_CLI+"\n");
    		wf.write(DB_CLI_ID+"\t"+DB_CLI_NOM+"\t\t"+DB_CLI_DIR+"\n");
    		
    		ResultSet rs = getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    		 
    		while (rs.next()) {
                 int id = rs.getInt(DB_CLI_ID);
                 String n = rs.getString(DB_CLI_NOM);
                 String d = rs.getString(DB_CLI_DIR);
                 wf.write(id+"\t"+n+"\t\t"+d+"\n");
             }
             wf.close();
             rs.close();
             System.out.println("Informacion escrita en el archivo");
 			
 		} catch (IOException e) {
 			e.printStackTrace();
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
    }
    ///////////////////////////////////////////
    //Insertar nuevo cliente ficheros
    //////////////////////////////////////////
    
    public static void nuevoClienteFichero(String ruta) {
    	File f = new File(ruta);
    	try {
			Scanner leerFichero=new Scanner(f);
			
			leerFichero.nextLine();
			leerFichero.nextLine();
			leerFichero.nextLine();
			
		
			while(leerFichero.hasNext()) {
				
				String insertar=leerFichero.nextLine();
				String datosCliente[]=insertar.split(",");
				insertCliente(datosCliente[0],datosCliente[1]);
			}
			
			leerFichero.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
    }
    ////////////////////////////////////////////
    //Modificar datos cliente ficheros
    ///////////////////////////////////////////
    
    public static void modificarClienteFichero(String ruta) {
    	File f=new File(ruta);
    	
    	try {
			Scanner leerFichero=new Scanner(f);
			
			leerFichero.nextLine();
			leerFichero.nextLine();
			leerFichero.nextLine();
			
			while(leerFichero.hasNext()) {
				
				String actualizar=leerFichero.nextLine();
				String datosCliente[]=actualizar.split(",");
				updateCliente(Integer.parseInt(datosCliente[0]),datosCliente[1],datosCliente[2]);
			}
			
			leerFichero.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
    	
    }
    //////////////////////////////////////////////
    //Eliminar cliente ficheros
    /////////////////////////////////////////////
    
    public static void eliminarClienteFichero(String ruta) {
    	File f=new File(ruta);
    	
    	try {
			Scanner leerFichero=new Scanner(f);
			
			leerFichero.nextLine();
			leerFichero.nextLine();
			
			while(leerFichero.hasNext()) {
				
				String eliminar=leerFichero.nextLine();
				String datosCliente[]=eliminar.split(",");
				
				for(int i=0;i<datosCliente.length;i++) {
					
					deleteCliente(Integer.parseInt(datosCliente[i]));
				}
			}
			
			leerFichero.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
    }
    
    /////////////////////////////////////////
    //Crear tabla
    /////////////////////////////////////////
    public static void crearTabla(String nombre,String columna1,String columna2,String columna3) {
		String sentencia="CREATE TABLE "+nombre+"("+columna1+" varchar(50) primary key,"+columna2+" varchar(50),"+columna3+" varchar(50))";
		try {
			PreparedStatement stmt=conn.prepareStatement(sentencia);
			stmt.execute();
			System.out.println("La nueva tabla se ha creado correctamente");
		} catch (SQLException e) {
			System.err.println("Error al crear la nueva tabla");
		}
	}
    
    /////////////////////////////////////////
    //Filtrar tabla
    ////////////////////////////////////////
    public static void filtrarClientesCiudad(String ciudad) {
    	  try {
  			CallableStatement cStmt = conn.prepareCall("{call filtrarPorCiudad(?)}");
  			cStmt.setString(1,ciudad);
  			cStmt.execute();
  			
  			ResultSet rs = cStmt.getResultSet();  
  			
  			while (rs.next()) {  
  				 int id = rs.getInt(DB_CLI_ID);
  	             String n = rs.getString(DB_CLI_NOM);
  	             String d = rs.getString(DB_CLI_DIR);
  	             System.out.println(id + "\t" + n + "\t" + d);
            }  
  		} catch (SQLException e) {
  			System.err.println("No ha sido posible filtrar clientes");
  		}  
    }

}
