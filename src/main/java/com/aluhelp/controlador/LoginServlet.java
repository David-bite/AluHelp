/*/LoginServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.database.ConexionBD;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try (Connection conn = ConexionBD.getConnection()) {

            String sql = "SELECT id, nombre FROM usuarios WHERE correo = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuarioId", rs.getInt("id"));
                session.setAttribute("nombre", rs.getString("nombre"));
                session.setAttribute("correo", correo);

                // Redirigir a página principal
                response.sendRedirect("index.jsp");
                return;
            }

            // si no encuentra el usuario manda el error
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}
