
import java.util.Scanner;

/**
 *
 * Clase principal donde se ejecutara el programa
 * 
 * @author Miguel Ángel
 */
public class GestionClientes {

	/**
	 *
	 * Main del programa
	 * 
	 * @param args argumentos del main
	 */
    public static void main(String[] args) {

        DBManager.loadDriver();
        DBManager.connect();

        boolean salir = false;
        do {
            salir = menuPrincipal();
        } while (!salir);

        DBManager.close();

    }
    /**
	 *
	 * Menu del programa
	 * 
	 * @return false para seguir en el bucle, true para salir
	 */

    public static boolean menuPrincipal() {
        System.out.println("");
        System.out.println("MENU PRINCIPAL");
        System.out.println("1. Listar clientes");
        System.out.println("2. Nuevo cliente");
        System.out.println("3. Modificar cliente");
        System.out.println("4. Eliminar cliente");
        System.out.println("5. Volcar datos tabla en fichero");
        System.out.println("6. Añadir cliente por fichero");
        System.out.println("7. Modificar cliente por fichero");
        System.out.println("8. Eliminar cliente por fichero");
        System.out.println("9. Crear una nueva tabla");
        System.out.println("10. Filtrar por ciudad(con callabStatement)");
        System.out.println("11. Salir");
        
        Scanner in = new Scanner(System.in);
            
        int opcion = pideInt("Elige una opción: ");
        
        switch (opcion) {
            case 1:
                opcionMostrarClientes();
                return false;
            case 2:
                opcionNuevoCliente();
                return false;
            case 3:
                opcionModificarCliente();
                return false;
            case 4:
                opcionEliminarCliente();
                return false;
            case 5:
            	opcionVolcarDatos();
            	return false;
            case 6:
            	opcionNuevoClienteFichero();
            	return false;
            case 7:
            	opcionModificarClienteFichero();
            	return false;
 			case 8:
 				opcionEliminarClienteFichero();
 				return false;
 			case 9:
 				opcionCrearTabla();
 				return false;
 			case 10:
 				opcionFiltrarCiudad();
 				return false;
            case 11:
                return true;
            default:
                System.out.println("Opción elegida incorrecta");
                return false;
        }
        
    }
    /**
	 *
	 * Pide un numero por teclado
	 * 
	 * @param mensaje 
	 * @return numero
	 */
    
    public static int pideInt(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                int valor = in.nextInt();
                //in.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("No has introducido un número entero. Vuelve a intentarlo.");
            }
        }
    }
    /**
	 *
	 * Pide una linea por teclado
	 * 
	 * @param mensaje
	 * @return linea
	 */
    
    public static String pideLinea(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                String linea = in.nextLine();
                return linea;
            } catch (Exception e) {
                System.out.println("No has introducido una cadena de texto. Vuelve a intentarlo.");
            }
        }
    }
    /**
	 *
	 * Nos muestra al cliente con el metodo printTablaClientes
	 */

    public static void opcionMostrarClientes() {
        System.out.println("Listado de Clientes:");
        DBManager.printTablaClientes();
    }

    /**
	 *
	 * Podemos introducir un nuevo cliente con el metodo insertCliente
	 */
    public static void opcionNuevoCliente() {
        Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del nuevo cliente:");
        String nombre = pideLinea("Nombre: ");
        String direccion = pideLinea("Dirección: ");

        boolean res = DBManager.insertCliente(nombre, direccion);

        if (res) {
            System.out.println("Cliente registrado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    /**
	 *
	 * Nos permite modificar al Cliente
	 */

    public static void opcionModificarCliente() {
        Scanner in = new Scanner(System.in);

        int id = pideInt("Indica el id del cliente a modificar: ");

        // Comprobamos si existe el cliente
        if (!DBManager.existsCliente(id)) {
            System.out.println("El cliente " + id + " no existe.");
            return;
        }

        // Mostramos datos del cliente a modificar
        DBManager.printCliente(id);

        // Solicitamos los nuevos datos
        String nombre = pideLinea("Nuevo nombre: ");
        String direccion = pideLinea("Nueva dirección: ");

        // Registramos los cambios
        boolean res = DBManager.updateCliente(id, nombre, direccion);

        if (res) {
            System.out.println("Cliente modificado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    /**
	 *
	 * Nos permite eliminar al Cliente
	 */

    public static void opcionEliminarCliente() {
        Scanner in = new Scanner(System.in);

        int id = pideInt("Indica el id del cliente a eliminar: ");

        // Comprobamos si existe el cliente
        if (!DBManager.existsCliente(id)) {
            System.out.println("El cliente " + id + " no existe.");
            return;
        }

        // Eliminamos el clientee
        boolean res = DBManager.deleteCliente(id);

        if (res) {
            System.out.println("Cliente eliminado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    /**
	 *
	 * Nos permite volcar los datos de la trabla a un fichero
	 */

    
    public static void opcionVolcarDatos() {
    	Scanner in=new Scanner (System.in);
    	System.out.println("Introduzca el nombre del fichero en el que se escribiran los datos");
    	String ruta=in.nextLine();
    	DBManager.volcarDatos(ruta);
    }
    /**
	 *
	 * Nos permite añadir a un nuevo cliente a traves de un fichero
	 */

    
    public static void opcionNuevoClienteFichero() {
    	Scanner in=new Scanner (System.in);
    	System.out.println("Introduzca la ruta del fichero para introducir un nuevo cliente");
    	String ruta=in.nextLine();
    	DBManager.nuevoClienteFichero(ruta);
    }
    /**
	 *
	 * Nos permite modificar a un  cliente a traves de un fichero
	 */
    public static void opcionModificarClienteFichero() {
    	Scanner in=new Scanner (System.in);
    	System.out.println("Introduzca la ruta del fichero para modificar cliente");
    	String ruta=in.nextLine();
    	DBManager.modificarClienteFichero(ruta);
    }
    /**
	 *
	 * Nos permite eliminar a un  cliente a traves de un fichero
	 */
    public static void opcionEliminarClienteFichero() {
    	Scanner in=new Scanner (System.in);
    	System.out.println("Introduzca la ruta del fichero para eliminar un cliente");
    	String ruta=in.nextLine();
    	DBManager.eliminarClienteFichero(ruta);
    }
    /**
	 *
	 * Nos permite crear una tabla con tres columnas
	 */
    public static void opcionCrearTabla() {
    	Scanner in=new Scanner (System.in);
    	String nombreTabla=pideLinea("Introduzca el nombre de la tabla ");
    	String columna1=pideLinea("Introduzca el nombre de la primera columna  ");
    	String columna2=pideLinea("Introduzca el nombre de la segunda columna ");
    	String columna3=pideLinea("Introduzca el nombre de la tercera columna ");
    	DBManager.crearTabla(nombreTabla, columna1, columna2, columna3);
    }
    /**
	 *
	 * Nos permite filtrar a los clientes por su ciudad
	 */
    public static void opcionFiltrarCiudad() {
    	Scanner in=new Scanner (System.in);
    	String ciudad=pideLinea("Introduzca una ciudad para filtrar a los clientes ");
    	DBManager.filtrarClientesCiudad(ciudad);
    }
    
}