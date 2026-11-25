package services;

import exceptions.DBException;
import exceptions.PropertiesReaderException;
import models.Coche;
import models.Extra;

import repositories.CocheDAO;
import utils.ConexionDB;
import utils.PropertiesReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportarCSVService {

    private CocheDAO cocheDAO = new CocheDAO();

    /**
     * metodo que lee el fichero csv y lo inserta mediante una transaccion
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws IOException
     */
    public void importarCoches() throws DBException, SQLException, PropertiesReaderException, IOException {
        String ruta = PropertiesReader.getInstance().get("ruta_csv");
        if (ruta == null || ruta.isBlank()) {
            throw new PropertiesReaderException("Falta la propiedad 'csv_ruta' en config.properties");
        }

        //lectura y validacion del fichero
        List<Coche> coches = leerFicheroCSV(Path.of(ruta));

        //transaccion
        Connection conn = ConexionDB.getConnection();
        try {
            conn.setAutoCommit(false);

            for (int i = 0; i < coches.size(); i++) {
                cocheDAO.insertarCoche(coches.get(i));
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * lee el csv por linea ignorando la primera linea
     * @param rutaCsv la ruta del csv
     * @return la lista de los coches leidos
     * @throws IOException
     */
    private List<Coche> leerFicheroCSV(Path rutaCsv) throws IOException {
        if (!Files.exists(rutaCsv)) {
            throw new IOException("No se encuentra el archivo CSV en la ruta: " + rutaCsv.toAbsolutePath());
        }

        List<Coche> coches = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(rutaCsv, StandardCharsets.UTF_8)) {
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;

                //ignoro lineas vacias y la primera linea
                if (linea.isBlank() || linea.toLowerCase().contains("matricula;marca")) {
                    continue;
                }

                String[] partes = linea.split(";");
                if (partes.length != 5) {
                    throw new IOException("Formato incorrecto en la línea " + numeroLinea + " del CSV.");
                }

                String matricula = partes[0].trim();
                String marca = partes[1].trim();
                String modelo = partes[2].trim();
                Extra extras = leerExtras(partes[3]);

                double precio;
                try {
                    precio = Double.parseDouble(partes[4].trim().replace(",", "."));
                } catch (NumberFormatException e) {
                    throw new IOException("Precio no válido en la línea " + numeroLinea + " del CSV: '" + partes[4] + "'");
                }

                Coche coche = new Coche(matricula, marca, modelo, extras, precio);
                coches.add(coche);
            }
        }

        return coches;
    }

    /**
     * convierte una cadena de extras en un objeto Extra
     * @param textoExtras la cadena de texto de los extras del coche
     * @return el extra del vehiculo con la lista de equipamiento
     */
    private Extra leerExtras(String textoExtras) {
        List<String> lista = new ArrayList<>();

        if (textoExtras != null && !textoExtras.isBlank()) {
            String[] partes = textoExtras.split("\\|");
            for (int i = 0; i < partes.length; i++) {
                String extra = partes[i].trim();
                if (!extra.isEmpty()) {
                    lista.add(extra);
                }
            }
        }

        return new Extra(lista);
    }
}