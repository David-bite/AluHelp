package com.aluhelp.controlador;

import com.aluhelp.database.ConexionBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/uploadpdf")
@MultipartConfig
public class UploadPDFServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Part filePart = request.getPart("pdf");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // RUTA CONSISTENTE
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filePath = uploadPath + fileName;

        // Guardar archivo
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Guardar en BD
        try (Connection conn = ConexionBD.getConnection()) {

            String sql = "INSERT INTO documentos(nombre, usuario_id, file_path) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fileName);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, fileName);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("mensaje", "PDF subido correctamente.");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
