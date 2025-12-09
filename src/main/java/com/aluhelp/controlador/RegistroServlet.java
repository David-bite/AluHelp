/*RegistroServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.UsuarioDAO;
import com.aluhelp.daoimpl.UsuarioDAOImpl;
import com.aluhelp.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try  {

            
            if (usuarioDAO.existeCorreo(correo)) {
                request.setAttribute("error", "El correo ya está registrado.");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }

            
            Usuario nuevo = new Usuario(nombre, correo, contrasena);
            usuarioDAO.registrar(nuevo);

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
