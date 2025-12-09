
package com.aluhelp.daoimpl;

import com.aluhelp.dao.UsuarioDAO;
import com.aluhelp.database.ConexionBD;
import com.aluhelp.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author HP
 */

public class UsuarioDAOImpl  implements UsuarioDAO{    
    
    @Override
    public Usuario buscarPorCorreoYContrasena(String correo, String contrasena) throws Exception {

        String sql = "SELECT id, nombre, correo, contrasena FROM usuarios WHERE correo = ? AND contrasena = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                return u;
            }

            return null; // no encontró usuario
        }
    }

    @Override
    public boolean existeCorreo(String correo) throws Exception {

        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // true si encontró al menos una fila
        }
    }

    @Override
    public void registrar(Usuario usuario) throws Exception {

        String sql = "INSERT INTO usuarios(nombre, correo, contrasena) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());

            stmt.executeUpdate();
        }
    }
}
