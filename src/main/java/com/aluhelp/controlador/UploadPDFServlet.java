/*UploadPDFServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.DocumentoDAO;
import com.aluhelp.daoimpl.DocumentoDAOImpl;
import com.aluhelp.database.ConexionBD;
import com.aluhelp.modelo.Documento;
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
    
    private DocumentoDAO documentoDAO = new DocumentoDAOImpl();

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
        try {
            Documento nuevo= new Documento(usuarioId, fileName, fileName);
            documentoDAO.guardar(nuevo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("mensaje", "PDF subido correctamente.");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
