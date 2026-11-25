package services;

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
import repositories.CocheDAO;
import repositories.InformeDAO;
import repositories.PropietarioDAO;
import repositories.TraspasoDAO;
import utils.ConexionDB;
import utils.InicializadorMySQL;
import utils.InicializadorSQLite;
import validators.CocheValidator;
import validators.PropietarioValidator;
import validators.TraspasoValidator;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConcesionarioService {

    private PropietarioDAO propietarioDAO = new PropietarioDAO();
    private CocheDAO cocheDAO = new CocheDAO();
    private TraspasoDAO traspasoDAO = new TraspasoDAO();
    private InformeDAO informeDAO = new InformeDAO();
    private ImportarCSVService importadorCSV = new ImportarCSVService();

    private PropietarioValidator propietarioValidator = new PropietarioValidator();
    private CocheValidator cocheValidator = new CocheValidator();
    private TraspasoValidator traspasoValidator = new TraspasoValidator();

    /**
     * metodo que hace la conexion a la base de datos
     *
     * @param tipoDB la base de datos elegida por el usuario
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void conectarBBDD(String tipoDB) throws DBException, SQLException, PropertiesReaderException {
        ConexionDB.elegirDB(tipoDB);
        ConexionDB.getConnection();
    }

    /**
     * metodo que crea las tablas
     *
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void inicializarTablas() throws DBException, SQLException, PropertiesReaderException {
        String db = ConexionDB.obtenerDB();
        if (db == null) {
            throw new DBException("no hay conexion");
        }

        if (db.equals(ConexionDB.MYSQL)) {
            InicializadorMySQL.inicializarTablas();
        } else {
            InicializadorSQLite.inicializarTablas();
        }
    }

    /**
     * metodo que registra un nuevo propietario
     *
     * @param propietario el propietario a insertar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws DNIException
     * @throws PropietarioException
     * @throws TelefonoException
     */
    public void registrarPropietario(Propietario propietario)
            throws DBException, SQLException, PropertiesReaderException,
            DNIException, PropietarioException, TelefonoException {

        propietarioValidator.validarPropietario(propietario);

        Propietario existente = propietarioDAO.buscarPorDni(propietario.getDni());
        if (existente != null) {
            throw new DBException("Ya existe un propietario registrado con el DNI: " + propietario.getDni());
        }

        propietarioDAO.insertarPropietario(propietario);
    }

    /**
     * metodo que inserta un coche sin propietario
     *
     * @param coche el coche a insertar.
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws MatriculaException
     * @throws CocheException
     */
    public void insertarCoche(Coche coche)
            throws DBException, SQLException, PropertiesReaderException,
            MatriculaException, CocheException {

        cocheValidator.validarCoche(coche);

        coche.setIdPropietario(null);
        cocheDAO.insertarCoche(coche);
    }

    /**
     * metodo que importa el csv
     *
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws IOException
     */
    public void importarCochesCSV() throws DBException, SQLException, PropertiesReaderException, IOException {
        importadorCSV.importarCoches();
    }

    /**
     * metodo que obtiene todos los coche que no tienen propietario
     *
     * @return la lista de coches sin propietarios
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public List<Coche> listarCochesConcesionario() throws DBException, SQLException, PropertiesReaderException {
        return cocheDAO.listarCoches();
    }

    /**
     * metodo que obtiene todos los coches que tienen propietario
     *
     * @return un mapa con el coche y el dni
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public Map<Coche, String> listarCochesPropietarios() throws DBException, SQLException, PropertiesReaderException {
        return cocheDAO.listarCochesPropietarios();
    }

    /**
     * metodo que modifica un coche
     *
     * @param coche el coche a buscar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws MatriculaException
     * @throws CocheException
     */
    public void modificarCoche(Coche coche)
            throws DBException, SQLException, PropertiesReaderException,
            MatriculaException, CocheException {

        cocheValidator.validarCoche(coche);
        cocheDAO.modificarCoche(coche);
    }

    /**
     * metodo que elimina un coche de la base de datos por su matricula
     *
     * @param matricula la matricula del vehículo a borrar.
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws MatriculaException
     */
    public void borrarCoche(String matricula)
            throws DBException, SQLException, PropertiesReaderException, MatriculaException {

        cocheValidator.validarMatricula(matricula);
        cocheDAO.borrarCoche(matricula);
    }

    /**
     * metodo que realiza un traspaso de forma transaccional validando comprador y coche.
     *
     * @param dniComprador   el DNI del cliente comprador
     * @param matriculaCoche la matrícula del coche a traspasar
     * @param montoEconomico el importe económico del traspaso
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws DNIException
     * @throws MatriculaException
     * @throws TraspasoException
     */
    public void realizarTraspaso(String dniComprador, String matriculaCoche, double montoEconomico)
            throws DBException, SQLException, PropertiesReaderException,
            DNIException, MatriculaException, TraspasoException {

        propietarioValidator.validarDNI(dniComprador);
        cocheValidator.validarMatricula(matriculaCoche);
        traspasoValidator.validarMonto(montoEconomico);

        traspasoDAO.realizarTraspaso(dniComprador, matriculaCoche, montoEconomico);
    }

    /**
     * metodo que ejecuta el procedimiento almacenado sp_coches_por_marca en mysql
     *
     * @param marca la marca de coches a buscar.
     * @return la lista de coches encontrados para esa marca.
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws CocheException
     */
    public List<Coche> buscarCochesPorMarcaSP(String marca)
            throws DBException, SQLException, PropertiesReaderException, CocheException {

        if (!ConexionDB.MYSQL.equals(ConexionDB.obtenerDB())) {
            throw new DBException("Esta opción solo está disponible cuando estás conectado a MySQL.");
        }

        if (marca == null || marca.isBlank()) {
            throw new CocheException("Error: el coche debe tener marca");
        }

        String sql = "{CALL sp_coches_por_marca(?)}";
        List<Coche> coches = new ArrayList<>();
        Connection conn = ConexionDB.getConnection();

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, marca.trim());

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    String matricula = rs.getString("matricula");
                    String marcaRes = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    Extra extras = textoAExtras(rs.getString("extras"));
                    double precio = rs.getDouble("precio");

                    int idProp = rs.getInt("id_propietario");
                    Integer idPropietario = rs.wasNull() ? null : idProp;

                    coches.add(new Coche(matricula, marcaRes, modelo, extras, precio, idPropietario));
                }
            }
        }

        return coches;
    }

    /**
     * metodo que genera el fichero de texto con el informe resumen del concesionario
     *
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     * @throws IOException
     */
    public void generarInforme() throws DBException, SQLException, PropertiesReaderException, IOException {
        informeDAO.generarInforme();
    }

    /**
     * metodo que cierra la conexión activa con la base de datos al finalizar el programa
     *
     * @throws SQLException
     */
    public void cerrarConexion() throws SQLException {
        ConexionDB.cerrarConexion();
    }

    /**
     * metodo que convierte una cadena de extras separados por | en un objeto extra
     *
     * @param textoExtras la cadena con los extras leída de la base de datos
     * @return el extra con la lista de equipamientos
     */
    private Extra textoAExtras(String textoExtras) {
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