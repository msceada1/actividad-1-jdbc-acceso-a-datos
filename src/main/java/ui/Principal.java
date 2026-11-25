package ui;

import exceptions.CocheException;
import exceptions.DBException;
import exceptions.DNIException;
import exceptions.MatriculaException;
import exceptions.PropertiesReaderException;
import exceptions.PropietarioException;
import exceptions.TelefonoException;
import exceptions.TraspasoException;
import models.Coche;
import models.Extra;
import models.Propietario;
import services.ConcesionarioService;
import utils.MiEntradaSalida;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    private static ConcesionarioService service = new ConcesionarioService();

    static void main(String[] args) {
        int opcion;

        do {
            System.out.println(menu());
            opcion = MiEntradaSalida.leerEnteroEnRango("elige una opcion", 1, 13);
            ejecutarOpcion(opcion);
        } while (opcion != 13);
    }

    /**
     * devuelve el texto del menú principal con todas las opciones disponibles
     *
     * @return una cadena con las opciones del menú.
     */
    public static String menu() {
        return "\n========= GESTIÓN DE CONCESIONARIO =========\n"
                + "1. Conectar a base de datos (MySQL / SQLite)\n"
                + "2. Inicializar tablas\n"
                + "3. Registrar propietario\n"
                + "4. Insertar coche (sin propietario)\n"
                + "5. Importar coches desde CSV\n"
                + "6. Listar coches del concesionario (sin propietario)\n"
                + "7. Listar coches con propietario y su DNI\n"
                + "8. Modificar coche\n"
                + "9. Borrar coche\n"
                + "10. Realizar traspaso\n"
                + "11. Buscar coches por marca (Procedimiento MySQL)\n"
                + "12. Generar informe resumen\n"
                + "13. Salir\n"
                + "============================================";
    }

    /**
     * Recibe la opción elegida por el usuario y ejecuta la acción correspondiente.
     *
     * @param opcion número entero de la opción del menú.
     */
    public static void ejecutarOpcion(int opcion) {
        boolean enFuncionamiento = true;
        while (enFuncionamiento) {
            try {
                switch (opcion) {
                    case 1:
                        conectarBBDD();
                        break;
                    case 2:
                        service.inicializarTablas();
                        System.out.println("Tablas inicializadas correctamente.");
                        break;
                    case 3:
                        registrarPropietario();
                        break;
                    case 4:
                        insertarCoche();
                        break;
                    case 5:
                        service.importarCochesCSV();
                        System.out.println("Coches importados desde el CSV correctamente.");
                        break;
                    case 6:
                        listarCochesConcesionario();
                        break;
                    case 7:
                        listarCochesPropietarios();
                        break;
                    case 8:
                        modificarCoche();
                        break;
                    case 9:
                        borrarCoche();
                        break;
                    case 10:
                        realizarTraspaso();
                        break;
                    case 11:
                        buscarPorMarcaSP();
                        break;
                    case 12:
                        service.generarInforme();
                        System.out.println("Informe generado correctamente.");
                        break;
                    case 13:
                        service.cerrarConexion();
                        System.out.println("Conexión cerrada. ¡Hasta pronto!");
                        enFuncionamiento = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Introduce un número entre 1 y 13.");
                        break;
                }
            } catch (DBException | PropertiesReaderException | DNIException | PropietarioException
                     | TelefonoException | MatriculaException | CocheException | TraspasoException
                     | IOException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }


    }

    private static void conectarBBDD() throws DBException, SQLException, PropertiesReaderException {
        System.out.println("Elige motor de base de datos:");
        System.out.println("1. MySQL");
        System.out.println("2. SQLite");
        int eleccion = MiEntradaSalida.leerEnteroEnRango("elige el modelo de bases de datos", 1, 2);

        if (eleccion == 1) {
            service.conectarBBDD("mysql");
            System.out.println("Conectado a MySQL correctamente.");
        } else if (eleccion == 2) {
            service.conectarBBDD("sqlite");
            System.out.println("Conectado a SQLite correctamente.");
        } else {
            System.out.println("Selección no válida.");
        }
    }

    private static void registrarPropietario()
            throws DBException, SQLException, PropertiesReaderException,
            DNIException, PropietarioException, TelefonoException {

        String dni = MiEntradaSalida.leerCadena("DNI (8 números y 1 letra): ");
        String nombre = MiEntradaSalida.leerCadena("Nombre: ");
        String apellido = MiEntradaSalida.leerCadena("Apellido: ");
        String telefono = MiEntradaSalida.leerCadena("Teléfono (9 dígitos): ");

        Propietario propietario = new Propietario(dni, nombre, apellido, telefono);
        service.registrarPropietario(propietario);
        System.out.println("Propietario registrado correctamente.");
    }

    private static void insertarCoche()
            throws DBException, SQLException, PropertiesReaderException,
            MatriculaException, CocheException {

        String matricula = MiEntradaSalida.leerCadena("Matrícula (4 números y 3 letras, ej. 1234ABC): ");
        String marca = MiEntradaSalida.leerCadena("Marca: ");
        String modelo = MiEntradaSalida.leerCadena("Modelo: ");
        Extra extras = pedirExtras();
        double precio = MiEntradaSalida.leerDoublePositivo("precio: ");

        Coche coche = new Coche(matricula, marca, modelo, extras, precio);
        service.insertarCoche(coche);
        System.out.println("Coche insertado correctamente.");
    }

    private static void listarCochesConcesionario()
            throws DBException, SQLException, PropertiesReaderException {

        List<Coche> coches = service.listarCochesConcesionario();
        if (coches.isEmpty()) {
            System.out.println("No hay coches sin propietario en el concesionario.");
        } else {
            System.out.println("--- Coches del concesionario ---");
            for (int i = 0; i < coches.size(); i++) {
                System.out.println(coches.get(i));
            }
        }
    }

    private static void listarCochesPropietarios()
            throws DBException, SQLException, PropertiesReaderException {

        Map<Coche, String> mapa = service.listarCochesPropietarios();
        if (mapa.isEmpty()) {
            System.out.println("No hay coches con propietario registrado.");
        } else {
            System.out.println("--- Coches con propietario ---");
            for (Coche c : mapa.keySet()) {
                String dni = mapa.get(c);
                System.out.println(c + " | DNI Propietario: " + dni);
            }
        }
    }

    private static void modificarCoche()
            throws DBException, SQLException, PropertiesReaderException,
            MatriculaException, CocheException {


        String matricula = MiEntradaSalida.leerCadena("Matrícula del coche a modificar: ");
        String marca = MiEntradaSalida.leerCadena("Nueva marca: ");
        String modelo = MiEntradaSalida.leerCadena("Nuevo modelo: ");
        Extra extras = pedirExtras();
        double precio = MiEntradaSalida.leerDoublePositivo("Nuevo precio: ");

        Coche coche = new Coche(matricula, marca, modelo, extras, precio);
        service.modificarCoche(coche);
        System.out.println("Coche modificado correctamente.");
    }

    private static void borrarCoche()
            throws DBException, SQLException, PropertiesReaderException, MatriculaException {

        String matricula = MiEntradaSalida.leerCadena("Matrícula del coche a borrar: ");

        service.borrarCoche(matricula);
        System.out.println("Coche eliminado correctamente.");
    }

    private static void realizarTraspaso()
            throws DBException, SQLException, PropertiesReaderException,
            DNIException, MatriculaException, TraspasoException {

        String dniComprador = MiEntradaSalida.leerCadena("DNI del comprador: ");
        String matricula = MiEntradaSalida.leerCadena("Matrícula del coche: ");
        double monto = MiEntradaSalida.leerDoublePositivo("Monto económico: ");

        service.realizarTraspaso(dniComprador, matricula, monto);
        System.out.println("Traspaso realizado correctamente.");
    }

    private static void buscarPorMarcaSP()
            throws DBException, SQLException, PropertiesReaderException, CocheException {

        String marca = MiEntradaSalida.leerCadena("Introduce la marca a buscar: ");

        List<Coche> coches = service.buscarCochesPorMarcaSP(marca);
        if (coches.isEmpty()) {
            System.out.println("No se encontraron coches de la marca " + marca + ".");
        } else {
            System.out.println("--- Coches de la marca " + marca + " ---");
            for (int i = 0; i < coches.size(); i++) {
                System.out.println(coches.get(i));
            }
        }
    }

    private static Extra pedirExtras() {
        int total = MiEntradaSalida.leerEnteroPositivo("Cuantos extras quieres añadir?");
        if (total == 0) {
            return null;
        }
        List<String> lista = new ArrayList<>();
        int contador = 0;
        while (contador < total) {
            String entrada = MiEntradaSalida.leerCadena("Introduce el extra: " + contador);
            if (!entrada.isBlank()) {
                String[] partes = entrada.split(",");
                for (int i = 0; i < partes.length; i++) {
                    String extra = partes[i].trim();
                    if (!extra.isEmpty()) {
                        lista.add(extra);
                    }
                }
            }
            contador++;
        }

        return new Extra(lista);
    }
}