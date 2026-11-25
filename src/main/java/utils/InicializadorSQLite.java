package utils;

import exceptions.DBException;
import exceptions.PropertiesReaderException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorSQLite {

    public static void inicializarTablas() throws SQLException, DBException, PropertiesReaderException {
        Connection conn = ConexionDB.getConnection();

        String sqlPropietarios = """
                CREATE TABLE IF NOT EXISTS propietarios (
                    id_propietario INTEGER PRIMARY KEY AUTOINCREMENT,
                    dni VARCHAR(9) NOT NULL UNIQUE,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    telefono VARCHAR(15)
                );
                """;

        String sqlCoches = """
                CREATE TABLE IF NOT EXISTS coches (
                    matricula VARCHAR(7) PRIMARY KEY,
                    marca VARCHAR(50) NOT NULL,
                    modelo VARCHAR(50) NOT NULL,
                    extras VARCHAR(255),
                    precio DOUBLE NOT NULL,
                    id_propietario INT,
                    FOREIGN KEY (id_propietario) REFERENCES propietarios(id_propietario)
                );
                """;

        String sqlTraspasos = """
                CREATE TABLE IF NOT EXISTS traspasos (
                    id_traspaso INTEGER PRIMARY KEY AUTOINCREMENT,
                    matricula_coche VARCHAR(7) NOT NULL,
                    id_vendedor INT,
                    id_comprador INT NOT NULL,
                    monto_economico DOUBLE NOT NULL,
                    FOREIGN KEY (matricula_coche) REFERENCES coches(matricula),
                    FOREIGN KEY (id_vendedor) REFERENCES propietarios(id_propietario),
                    FOREIGN KEY (id_comprador) REFERENCES propietarios(id_propietario)
                );
                """;

        try (Statement st = conn.createStatement()) {
            st.execute(sqlPropietarios);
            st.execute(sqlCoches);
            st.execute(sqlTraspasos);
        }
    }
}