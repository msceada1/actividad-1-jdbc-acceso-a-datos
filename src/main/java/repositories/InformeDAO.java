package repositories;

import exceptions.DBException;
import exceptions.PropertiesReaderException;
import utils.ConexionDB;
import utils.PropertiesReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class InformeDAO {

    /**
     * metodo encargado de generar un informe txt utilizando metodos auxiliares
     *
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws IOException
     */
    public void generarInforme() throws DBException, SQLException, PropertiesReaderException, IOException {
        String ruta = PropertiesReader.getInstance().get("ruta_informe");
        if (ruta == null || ruta.isBlank()) {
            throw new PropertiesReaderException("no se ha encontrado la ruta");
        }

        Connection conn = ConexionDB.getConnection();
        int totalCoches = contarCoches(conn);
        String extraMasRepetido = buscarExtraMasRepetido(conn);

        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(ruta), StandardCharsets.UTF_8)) {
            bw.write("=== INFORME DEL CONCESIONARIO ===");
            bw.newLine();
            bw.write("Total de coches: " + totalCoches);
            bw.newLine();
            bw.newLine();

            bw.write("--- Coches agrupados por marca ---");
            bw.newLine();
            escribirCochesPorMarca(conn, bw);
            bw.newLine();

            bw.write("--- Extra más repetido ---");
            bw.newLine();
            bw.write(extraMasRepetido);
            bw.newLine();
        }
    }

    /**
     * metodo que cuenta los coches que hay en el concesionario
     *
     * @param conn la conexion del programa
     * @return un enetero con el numero total de coches que hay
     * @throws SQLException
     */
    private int contarCoches(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM coches";
        int total = 0;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt(1);
            }
        }
        return total;
    }

    private void escribirCochesPorMarca(Connection conn, BufferedWriter bw) throws SQLException, IOException {
        String sql = "SELECT matricula, marca, modelo, extras, precio FROM coches ORDER BY marca, modelo";
        String marcaActual = "";
        boolean hayCoches = false;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                hayCoches = true;
                String matricula = rs.getString("matricula");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                String extras = rs.getString("extras");
                double precio = rs.getDouble("precio");

                if (extras == null || extras.isBlank()) {
                    extras = "Sin extras";
                }

                //si hay una marca distinta a la leida antes, se añade
                if (!marca.equalsIgnoreCase(marcaActual)) {
                    marcaActual = marca;
                    bw.write("Marca: " + marcaActual);
                    bw.newLine();
                }

                bw.write("  - " + matricula + " | " + modelo + " | " + precio + " € | Extras: " + extras);
                bw.newLine();
            }
        }

        if (!hayCoches) {
            bw.write("No hay coches registrados.");
            bw.newLine();
        }
    }

    /**
     * metodo que busca el extra que mas se repite
     *
     * @param conn la conexion que usa el programa
     * @return un string con el extra mas repetido o si no hay, un mensaje informativo
     * @throws SQLException
     */
    private String buscarExtraMasRepetido(Connection conn) throws SQLException {
        String sql = "SELECT extras FROM coches WHERE extras IS NOT NULL";
        HashMap<String, Integer> conteo = new HashMap<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String textoExtras = rs.getString("extras");
                if (textoExtras != null && !textoExtras.isBlank()) {
                    String[] partes = textoExtras.split("\\|");

                    for (int i = 0; i < partes.length; i++) {
                        String extra = partes[i].trim();

                        if (!extra.isEmpty()) {
                            if (conteo.containsKey(extra)) {
                                int veces = conteo.get(extra);
                                conteo.put(extra, veces + 1);
                            } else {
                                conteo.put(extra, 1);
                            }
                        }
                    }
                }
            }
        }

        //busqueda del que tiene el numero mas alto
        String masRepetido = "No hay extras registrados";
        int maxVeces = 0;

        for (String extra : conteo.keySet()) {
            int veces = conteo.get(extra);
            if (veces > maxVeces) {
                maxVeces = veces;
                masRepetido = extra + " (" + maxVeces + " veces)";
            }
        }

        return masRepetido;
    }
}