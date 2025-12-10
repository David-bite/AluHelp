/*UniDocumentoServlet.java*/
package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import com.aluhelp.database.ConexionBD;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

@WebServlet("/unirpdf")
public class UnirDocumentoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String[] archivosSeleccionados = request.getParameterValues("pdfs");

        if (archivosSeleccionados == null || archivosSeleccionados.length < 2) {
            request.setAttribute("error", "Debes seleccionar al menos 2 PDFs.");
            request.getRequestDispatcher("unir_pdf.jsp").forward(request, response);
            return;
        }

        // MISMA RUTA QUE uploadpdf
        String uploadsPath = getServletContext().getRealPath("") + "uploads" + File.separator;

        File uploadsDir = new File(uploadsPath);
        if (!uploadsDir.exists()) uploadsDir.mkdirs();

        // Nombre del PDF final
        String nombreFinal = "PDF_Unido_" + System.currentTimeMillis() + ".pdf";
        String rutaFinal = uploadsPath + nombreFinal;

        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(rutaFinal);

            // Añadir PDFs uno por uno
            for (String fileName : archivosSeleccionados) {
                File pdf = new File(uploadsPath + fileName);
                if (pdf.exists()) {
                    merger.addSource(pdf);
                } else {
                    System.out.println("NO ENCONTRADO: " + pdf.getAbsolutePath());
                }
            }

            merger.mergeDocuments(null);

            // Guardar registro en BD
            try (Connection conn = ConexionBD.getConnection()) {
                String sql = "INSERT INTO documentos(nombre, usuario_id, file_path) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombreFinal);
                stmt.setInt(2, usuarioId);
                stmt.setString(3, nombreFinal);
                stmt.executeUpdate();
            }

            request.setAttribute("mensaje", "PDF unido correctamente.");
            request.getRequestDispatcher("unir_pdf.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al unir PDFs: " + e.getMessage());
            request.getRequestDispatcher("unir_pdf.jsp").forward(request, response);
        }
    }
}
