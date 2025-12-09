package com.aluhelp.controlador;

import com.aluhelp.database.ConexionBD;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
@WebServlet(name = "BorrarPDFServlet", urlPatterns = {"/BorrarPDFServlet"})
public class BorrarPDFServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String fileName = request.getParameter("file");
        if (fileName == null) {
            response.sendRedirect("mis_documentos.jsp");
        }

        try (Connection conn = ConexionBD.getConnection()) {

            String sql = "DELETE FROM documentos WHERE usuario_id =? AND file_path=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            stmt.setString(2, fileName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String uploadPath = getServletContext().getRealPath("") + "uploads" + File.separator + fileName;

        File archivo = new File(uploadPath);

        if (archivo.exists()) {
            archivo.delete();
        }

        response.sendRedirect("mis_documentos.jsp");
    }

}
