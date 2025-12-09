package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import com.aluhelp.database.ConexionBD;
import java.util.*;

@WebServlet("/MisDocumentosServlet")
public class MisDocumentosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, file_path FROM documentos WHERE usuario_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);

            ResultSet rs = stmt.executeQuery();

            List<Map<String, String>> docs = new ArrayList<>();

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("nombre", rs.getString("nombre"));
                map.put("file", rs.getString("file_path"));
                map.put("id", rs.getString("id"));
                docs.add(map);
            }

            request.setAttribute("docs", docs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("mis_documentos.jsp").forward(request, response);
    }
}
