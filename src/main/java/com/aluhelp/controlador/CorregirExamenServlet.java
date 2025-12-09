package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

@WebServlet("/corregir")
public class CorregirExamenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");
        Map<Integer, String> respuestas = (Map<Integer, String>) session.getAttribute("respuestasExamen");

        if (preguntas == null || respuestas == null) {
            response.sendRedirect("mis_documentos.jsp");
            return;
        }

        int total = preguntas.size();
        int correctas = 0;

        // Lista para mostrar en resultados
        List<Map<String, Object>> resultadoDetalle = new ArrayList<>();

        for (int i = 0; i < total; i++) {

            Map<String, Object> p = preguntas.get(i);

            String correcta = normalizar((String) p.get("respuesta_correcta"));
            String usuario = normalizar(respuestas.getOrDefault(i, ""));

            boolean esCorrecta = compararSimilitud(usuario, correcta);

            if (esCorrecta) {
                correctas++;
            }

            Map<String, Object> detalle = new HashMap<>();
            detalle.put("pregunta", p.get("pregunta"));
            detalle.put("respuesta_correcta", p.get("respuesta_correcta"));
            detalle.put("respuesta_usuario", respuestas.getOrDefault(i, ""));
            detalle.put("esCorrecta", esCorrecta);
            detalle.put("oracion_original", p.get("oracion_original"));

            resultadoDetalle.add(detalle);
        }

        int puntaje = (correctas * 100) / total;

        request.setAttribute("puntaje", puntaje);
        request.setAttribute("correctas", correctas);
        request.setAttribute("total", total);
        request.setAttribute("detalle", resultadoDetalle);

        request.getRequestDispatcher("resultado_examen.jsp").forward(request, response);
    }


    // ============================================
    // 🔵 Normalización para comparar respuestas
    // ============================================

    private String normalizar(String s) {
        if (s == null) return "";
        s = s.toLowerCase().trim();
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return s;
    }

    // ============================================
    // 🔵 Comparación por similitud (Levenshtein)
    // ============================================

    private boolean compararSimilitud(String a, String b) {

        if (a.equals(b)) return true;

        int dist = distanciaLevenshtein(a, b);

        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return false;

        double similitud = 1 - ((double) dist / maxLen);

        return similitud >= 0.80; // 80% parecido = correcto
    }


    private int distanciaLevenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {

                int costo = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + costo
                );
            }
        }

        return dp[a.length()][b.length()];
    }}