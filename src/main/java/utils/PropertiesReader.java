package utils;

import exceptions.PropertiesReaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesReader {
    private final static Path file = Path.of("config.properties");
    private Properties p;
    private static PropertiesReader instance = null;

    private PropertiesReader() throws PropertiesReaderException {
        p = new Properties();
        if (!Files.exists(file)) {
            throw new PropertiesReaderException("ERROR: El archivo config.properties no se encuentra en la ruta: " + file.toAbsolutePath());
        }

        // Usamos try-with-resources para asegurar que el archivo se cierra
        try (BufferedReader lector = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            p.load(lector);

        } catch (IOException e) {
            System.out.println("ERROR: No se ha podido leer el archivo config.properties. Detalles técnicos: " + e.getMessage());
            throw new PropertiesReaderException(e.getMessage());
        }
    }

    public static PropertiesReader getInstance() throws PropertiesReaderException {
        if (instance == null) {
            instance = new PropertiesReader();
        }

        return instance;
    }

    public String get(String propiedad) {
        return p.getProperty(propiedad);
    }
}
