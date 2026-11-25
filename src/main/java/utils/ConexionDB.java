package utils;

import exceptions.DBException;
import exceptions.PropertiesReaderException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    public static final String MYSQL = "mysql";
    public static final String SQLITE = "sqlite";
    private static Connection conexion = null;
    private static String dataBase = null;

    // Privado para evitar instanciación externa (Patrón Singleton)
    private ConexionDB() {
    }

    public static void elegirDB(String db) throws DBException {
        if (dataBase != null) {
            throw new DBException("el modelo de base de datos ya ha sido elegido");
        }

        if (!MYSQL.equalsIgnoreCase(db) && !SQLITE.equalsIgnoreCase(db)) {
            throw new DBException("error: el modelo de base de datos debe ser MySQL o SQLITE");
        }

        dataBase = db.toLowerCase();
    }

    public static String obtenerDB() {
        return dataBase;
    }

    /**
     * Devuelve una conexión activa a la base de datos segun la seleccionada
     */
    public static Connection getConnection() throws DBException, SQLException, PropertiesReaderException {
        if (dataBase == null) {
            throw new DBException("Debe seleccionar primero un modelo de base de datos antes de conectar.");
        }

        PropertiesReader p = PropertiesReader.getInstance();

        // Si la conexión no existe o está cerrada, creamos una nueva
        if (conexion == null || conexion.isClosed()) {
            if (dataBase.equals(MYSQL)) {
                conexion = DriverManager.getConnection(
                        p.get("mysql_url"),
                        p.get("user"),
                        p.get("password")
                );
            } else {
                conexion = DriverManager.getConnection(p.get("sqlite_url"));
                try (Statement st = conexion.createStatement()) {
                    String sql = "PRAGMA foreign_keys = ON;";
                    st.execute(sql);
                }
            }
        }

        return conexion;
    }

    /**
     * metodo encargado de cerrar la conexion cuando el usuario finaliza el programa
     *
     * @throws SQLException
     */
    public static void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        conexion = null;
    }
}
