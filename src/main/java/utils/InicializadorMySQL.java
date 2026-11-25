package utils;

import exceptions.DBException;
import exceptions.PropertiesReaderException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorMySQL {

    public static void inicializarTablas() throws DBException, SQLException, PropertiesReaderException {
        Connection conn = ConexionDB.getConnection();

        String sqlPropietarios = """
                CREATE TABLE IF NOT EXISTS propietarios (
                    id_propietario INT AUTO_INCREMENT PRIMARY KEY,
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
                    id_traspaso INT AUTO_INCREMENT PRIMARY KEY,
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
            String sqlDropSP = "DROP PROCEDURE IF EXISTS sp_coches_por_marca";
            String sqlCreateSP = """
                    CREATE PROCEDURE sp_coches_por_marca(IN p_marca VARCHAR(50))
                    BEGIN
                        SELECT matricula, marca, modelo, extras, precio, id_propietario
                        FROM coches
                        WHERE LOWER(marca) = LOWER(p_marca);
                    END
                    """;

            st.execute(sqlPropietarios);
            st.execute(sqlCoches);
            st.execute(sqlTraspasos);
            st.execute(sqlDropSP);
            st.execute(sqlCreateSP);
        }
    }
}
