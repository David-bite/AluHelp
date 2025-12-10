/*ResumenServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.ResumenDAO;
import com.aluhelp.daoimpl.ResumenDAOImpl;
import com.aluhelp.database.ConexionBD;
import com.aluhelp.modelo.Resumen;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@WebServlet("/resumen")
public class ResumenServlet extends HttpServlet {
    
    private ResumenDAO resumenDAO = new ResumenDAOImpl();

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
            request.setAttribute("error", "No se seleccionó archivo.");
            request.getRequestDispatcher("mis_documentos.jsp").forward(request, response);
            return;
        }

        // Ruta completa al archivo PDF
        String pdfPath = getServletContext().getRealPath("") + "uploads/" + fileName;

        // Extraer texto del PDF
        String textoOriginal = "";

        try (InputStream is = new FileInputStream(pdfPath); 
                PDDocument document = PDDocument.load(is)) {

            PDFTextStripper stripper = new PDFTextStripper();
            textoOriginal = stripper.getText(document);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Generar resumen 
        String resumen = generarResumen(textoOriginal);

        // Guardar en la BD
        try {
            Resumen resumenfinal = new Resumen(usuarioId, textoOriginal, resumen);
            resumenDAO.guardar(resumenfinal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mostrar resumen
        request.setAttribute("original", textoOriginal);
        request.setAttribute("resumen", resumen);

        request.getRequestDispatcher("resumen.jsp").forward(request, response);
    }

    // Resumen easy
    private String generarResumen(String textoOriginal) {

        if (textoOriginal == null || textoOriginal.length() < 80) {
            return textoOriginal;
        }

        // 1. Normalizar texto
        String texto = textoOriginal
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
                .trim();

        // 2. Dividir en oraciones
        String[] oraciones = texto.split("(?<=[\\.\\?\\!])\\s+");
        if (oraciones.length < 3) {
            return textoOriginal;
        }

        // 3. Contar palabras importantes
        Map<String, Integer> frecuencia = new HashMap<>();
        String[] palabras = texto.toLowerCase().split("[^a-záéíóúñ]+");

        List<String> stopwords = Arrays.asList(
                "el", "la", "los", "las", "de", "del", "y", "o", "en", "un", "una", "que",
                "a", "para", "con", "por", "se", "al", "su", "sus", "es", "son", "como",
                "pero", "este", "esta", "esto", "estos", "estas"
        );

        for (String p : palabras) {
            if (p.length() > 3 && !stopwords.contains(p)) {
                frecuencia.put(p, frecuencia.getOrDefault(p, 0) + 1);
            }
        }

        // 4. Puntuación de oraciones
        Map<String, Double> puntuacion = new HashMap<>();

        for (String oracion : oraciones) {
            String[] palabrasOracion = oracion.toLowerCase().split(" ");
            int score = 0;

            for (String palabra : palabrasOracion) {
                score += frecuencia.getOrDefault(palabra.replaceAll("[^a-záéíóúñ]", ""), 0);
            }

            puntuacion.put(oracion, (double) score);
        }

        // 5. Seleccionar top 30% de oraciones
        int cantidad = Math.max(1, (int) (oraciones.length * 0.3));

        List<String> mejores = puntuacion.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(cantidad)
                .map(Map.Entry::getKey)
                .sorted() // Mantener orden original
                .toList();

        // 6. Unir
        StringBuilder resumen = new StringBuilder();
        for (String r : mejores) {
            resumen.append(r).append(" ");
        }

        return resumen.toString().trim();
    }

}
