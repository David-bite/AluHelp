/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.daoimpl;

import com.aluhelp.dao.ResumenDAO;
import com.aluhelp.database.ConexionBD;
import com.aluhelp.modelo.Resumen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResumenDAOImpl implements ResumenDAO {

    @Override
    public void guardar(Resumen resumen) throws Exception {
        String sql = "INSERT INTO resumenes(usuario_id, texto_original, texto_resumido) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, resumen.getUsuarioId());
            stmt.setString(2, resumen.getTextoOriginal());
            stmt.setString(3, resumen.getTextoResumido());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Resumen> listarPorUsuario(int usuarioId) throws Exception {

        List<Resumen> lista = new ArrayList<>();
        String sql = "SELECT * FROM resumenes WHERE usuario_id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Resumen(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("texto_original"),
                        rs.getString("texto_resumido")
                ));
            }
        }

        return lista;
    }

    @Override
    public Resumen obtenerPorId(int id) throws Exception {

        String sql = "SELECT * FROM resumenes WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Resumen(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("texto_original"),
                        rs.getString("texto_resumido")
                );
            }
        }

        return null;
    }
}
