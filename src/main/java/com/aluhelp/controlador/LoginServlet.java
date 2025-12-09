/*/LoginServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.UsuarioDAO;
import com.aluhelp.daoimpl.UsuarioDAOImpl;

import com.aluhelp.modelo.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try  {

            // Usamos el DAOUsuario.buscarPorCorreoYContrasena(correo, contrasena);
            Usuario usuario = usuarioDAO.buscarPorCorreoYContrasena(correo, contrasena);

            if (usuario!=null) {

                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("nombre", usuario.getNombre());
                session.setAttribute("correo", usuario.getCorreo());

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
