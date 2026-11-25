package repositories;

import exceptions.DBException;
import exceptions.PropertiesReaderException;
import models.Traspaso;
import utils.ConexionDB;

import java.sql.*;

public class TraspasoDAO {

    public Traspaso realizarTraspaso(String dniComprador, String matriculaCoche, double montoEconomico)
            throws DBException, SQLException, PropertiesReaderException {

        String dniLimpio = dniComprador.trim().toUpperCase();
        String matricula = matriculaCoche.trim().toUpperCase();

        String sqlBuscarComprador = "SELECT id_propietario FROM propietarios WHERE dni = ?";
        String sqlBuscarCoche = "SELECT id_propietario FROM coches WHERE matricula = ?";
        String sqlInsertTraspaso = "INSERT INTO traspasos (matricula_coche, id_vendedor, id_comprador, monto_economico) VALUES (?, ?, ?, ?)";
        String sqlUpdateCoche = "UPDATE coches SET id_propietario = ? WHERE matricula = ?";

        Connection conn = ConexionDB.getConnection();
        boolean autoCommitOriginal = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            //verifico que exista el comprador
            Integer idComprador = null;
            try (PreparedStatement ps = conn.prepareStatement(sqlBuscarComprador)) {
                ps.setString(1, dniLimpio);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idComprador = rs.getInt("id_propietario");
                    }
                }
            }
            if (idComprador == null) {
                throw new DBException("No existe ningún propietario registrado con el DNI: " + dniLimpio);
            }

            //verifico que exista el coche y su propietario que es el vendedor
            Integer idVendedor = null;
            boolean cocheExiste = false;
            try (PreparedStatement ps = conn.prepareStatement(sqlBuscarCoche)) {
                ps.setString(1, matricula);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        cocheExiste = true;
                        int idProp = rs.getInt("id_propietario");
                        idVendedor = rs.wasNull() ? null : idProp;
                    }
                }
            }
            if (!cocheExiste) {
                throw new DBException("No existe ningún coche registrado con la matrícula: " + matricula);
            }
            if (idVendedor != null && idVendedor.equals(idComprador)) {
                throw new DBException("El comprador ya es el propietario actual de este vehículo.");
            }

            //inserto el trapaso
            Traspaso traspaso = new Traspaso(matricula, idVendedor, idComprador, montoEconomico);
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertTraspaso, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, traspaso.getMatriculaCoche());
                if (traspaso.getIdVendedor() == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, traspaso.getIdVendedor());
                }
                ps.setInt(3, traspaso.getIdComprador());
                ps.setDouble(4, traspaso.getMontoEconomico());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        traspaso.setIdTraspaso(rs.getInt(1));
                    }
                }
            }

            //actualizo el propietario del coche
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateCoche)) {
                ps.setInt(1, idComprador);
                ps.setString(2, matricula);
                ps.executeUpdate();
            }

            conn.commit();
            return traspaso;

        } catch (SQLException | DBException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommitOriginal);
        }
    }
}
