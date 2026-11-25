package repositories;

import exceptions.DBException;
import exceptions.PropertiesReaderException;
import models.Propietario;
import utils.ConexionDB;

import java.sql.*;

public class PropietarioDAO {

    /**
     * metodo encargado de insertar un nuevo propietario en la base de datos
     * @param p el propietario a insertar
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public void insertarPropietario(Propietario p) throws DBException, SQLException, PropertiesReaderException {
        String sql = "INSERT INTO propietarios (dni, nombre, apellido, telefono) VALUES (?, ?, ?, ?)";
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getDni().trim().toUpperCase());
            ps.setString(2, p.getNombre().trim());
            ps.setString(3, p.getApellido().trim());

            if (p.getTelefono() == null || p.getTelefono().isBlank()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, p.getTelefono().trim());
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * metodo encargado de devolver un propietario por su dni
     * @param dni el dni del propietario a busvar
     * @return el propietario si lo encuentra, null si no lo encuentra
     * @throws DBException
     * @throws SQLException
     * @throws PropertiesReaderException
     */
    public Propietario buscarPorDni(String dni) throws DBException, SQLException, PropertiesReaderException {
        String sql = "SELECT id_propietario, dni, nombre, apellido, telefono FROM propietarios WHERE dni = ?";
        Connection conn = ConexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Propietario(
                            rs.getInt("id_propietario"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }
}
