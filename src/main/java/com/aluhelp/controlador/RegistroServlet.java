/*RegistroServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.database.ConexionBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try (Connection conn = ConexionBD.getConnection()) {

            // Verifica si el correo ya existe
            String checkSql = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, correo);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("error", "El correo ya está registrado.");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }

            // aca se inserta el usuario
            String sql = "INSERT INTO usuarios(nombre, correo, contrasena) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, contrasena);

            stmt.executeUpdate();

            // se manda al login
            //aqui manda el parametro EXITO
            response.sendRedirect("login.jsp?exito=1");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno: " + e.getMessage());
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
