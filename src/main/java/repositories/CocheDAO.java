package repositories;

import exceptions.DBException;
import exceptions.PropertiesReaderException;
import models.Coche;
import models.Extra;
import utils.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CocheDAO {

    /**
     * metodo encargado de insertar un coche
     *
     * @param c el coche a insertar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void insertarCoche(Coche c) throws DBException, SQLException, PropertiesReaderException {
        String sql = "INSERT INTO coches (matricula, marca, modelo, extras, precio, id_propietario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getMatricula().trim().toUpperCase());
            ps.setString(2, c.getMarca().trim());
            ps.setString(3, c.getModelo().trim());

            String extrasTexto = extrasATexto(c.getExtras());
            if (extrasTexto == null) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, extrasTexto);
            }
            ps.setDouble(5, c.getPrecio());

            if (c.getIdPropietario() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, c.getIdPropietario());
            }
            ps.executeUpdate();

        }
    }

    /**
     * metodo encargado de listar los coches que no tienen propietario
     *
     * @return una lista de coches
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public List<Coche> listarCoches() throws DBException, SQLException, PropertiesReaderException {
        String sql = "SELECT matricula, marca, modelo, extras, precio, id_propietario FROM coches WHERE id_propietario IS NULL";
        List<Coche> lista = new ArrayList<>();
        Connection conn = ConexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCoche(rs));
            }
        }
        return lista;

    }

    /**
     * metodo encargado de listar los coches del concesionario que si tienen propietarios
     *
     * @return un mapa con los coches y el dni del propietario
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public Map<Coche, String> listarCochesPropietarios() throws DBException, SQLException, PropertiesReaderException {
        String sql = """
                SELECT c.matricula, c.marca, c.modelo, c.extras, c.precio, c.id_propietario, p.dni
                FROM coches c
                INNER JOIN propietarios p ON c.id_propietario = p.id_propietario
                """;
        Map<Coche, String> resultado = new LinkedHashMap<>();
        Connection conn = ConexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Coche coche = mapearCoche(rs);
                String dni = rs.getString("dni");
                resultado.put(coche, dni);
            }
        }
        return resultado;
    }

    /**
     * metodo encargado de borrar un coche del concesionario
     *
     * @param c el coche a borrar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void modificarCoche(Coche c) throws DBException, SQLException, PropertiesReaderException {
        String sql = "UPDATE coches SET marca = ?, modelo = ?, extras = ?, precio = ? WHERE matricula = ?";
        Connection conn = ConexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getMarca().trim());
            ps.setString(2, c.getModelo().trim());

            String extrasTexto = extrasATexto(c.getExtras());
            if (extrasTexto == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, extrasTexto);
            }

            ps.setDouble(4, c.getPrecio());
            ps.setString(5, c.getMatricula().trim().toUpperCase());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DBException("No se encontró ningún coche con la matrícula: " + c.getMatricula());
            }
        }
    }

    /**
     * metodo encargado de borrar un coche del concesionario
     * @param matricula la matricula del coche a borrar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void borrarCoche(String matricula) throws DBException, SQLException, PropertiesReaderException {
        String sql = "DELETE FROM coches WHERE matricula = ?";
        Connection conn = ConexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula.trim().toUpperCase());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DBException("No se encontró ningún coche con la matrícula: " + matricula);
            }
        }
    }

    public List<Coche> buscarCochesPorMarcaSP(String marca)
            throws DBException, SQLException, PropertiesReaderException {

        if (!ConexionDB.MYSQL.equals(ConexionDB.obtenerDB())) {
            throw new DBException("Los procedimientos almacenados solo están disponibles en MySQL.");
        }

        String sql = "{CALL sp_coches_por_marca(?)}";
        List<Coche> lista = new ArrayList<>();
        Connection conn = ConexionDB.getConnection();

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, marca.trim());

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCoche(rs));
                }
            }
        }
        return lista;
    }

    private Coche mapearCoche(ResultSet rs) throws SQLException {
        Extra extra = textoAExtras(rs.getString("extras"));

        int idProp = rs.getInt("id_propietario");
        Integer idPropietario = rs.wasNull() ? null : idProp;

        return new Coche(
                rs.getString("matricula"),
                rs.getString("marca"),
                rs.getString("modelo"),
                extra,
                rs.getDouble("precio"),
                idPropietario
        );
    }

    private String extrasATexto(Extra extra) {
        if (extra == null || extra.getExtras() == null || extra.getExtras().isEmpty()) {
            return null;
        }
        return String.join("|", extra.getExtras());
    }

    private Extra textoAExtras(String texto) {
        List<String> lista = new ArrayList<>();
        if (texto != null && !texto.isBlank()) {
            for (String item : texto.split("\\|")) {
                String limpio = item.trim();
                if (!limpio.isEmpty()) {
                    lista.add(limpio);
                }
            }
        }
        return new Extra(lista);
    }

}
