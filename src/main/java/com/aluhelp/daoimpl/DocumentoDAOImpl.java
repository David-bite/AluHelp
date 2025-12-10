/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.daoimpl;

import com.aluhelp.dao.DocumentoDAO;
import com.aluhelp.database.ConexionBD;
import com.aluhelp.modelo.Documento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAOImpl implements DocumentoDAO {

    @Override
    public List<Documento> listarPorUsuario(int usuarioId) throws Exception {
        List<Documento> lista = new ArrayList<>();

        String sql = "SELECT id, usuario_id, nombre, file_path FROM documentos WHERE usuario_id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Documento d = new Documento(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("nombre"),
                        rs.getString("file_path")
                );
                lista.add(d);
            }
        }

        return lista;
    }

    @Override
    public void guardar(Documento documento) throws Exception {
        String sql = "INSERT INTO documentos(nombre, usuario_id, file_path) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento.getNombre());
            stmt.setInt(2, documento.getUsuarioId());
            stmt.setString(3, documento.getFilePath());

            stmt.executeUpdate();
        }
    }

    @Override
    public void borrarDocumento(int usuarioId, String filePath) throws Exception {

        String sql = "DELETE FROM documentos WHERE usuario_id = ? AND file_path = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setString(2, filePath);

            stmt.executeUpdate();
        }
    }

    @Override
    public Documento obtenerPorId(int id) throws Exception {

        String sql = "SELECT id, usuario_id, nombre, file_path FROM documentos WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Documento(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("nombre"),
                        rs.getString("file_path")
                );
            }

        }

        return null;

    }

}
