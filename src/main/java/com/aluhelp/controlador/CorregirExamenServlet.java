/*CorregirExamenServlet.java*/
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

        HttpSession session = request.getSession(); // saco la sesion pa agarrar las preguntas y respuestas del alumno

        // aca estaban guardadas las preguntas generadas desde el PDF
        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");

        // aca estaban guardadas las respuestas q el alumno escribio
        Map<Integer, String> respuestas = (Map<Integer, String>) session.getAttribute("respuestasExamen");

        // si no hay preguntas o no hay respuestas, pues no hay nada q corregir y lo regresamos
        if (preguntas == null || respuestas == null) {
            response.sendRedirect("mis_documentos.jsp");
            return;
        }

        int total = preguntas.size(); // cuantas preguntas hay en total
        int correctas = 0; // contador pa saber cuantas el alumno acertó

        // esta lista es pa mostrar el detalle de cada pregunta en la pagina de resultados
        List<Map<String, Object>> resultadoDetalle = new ArrayList<>();

        for (int i = 0; i < total; i++) {

            Map<String, Object> p = preguntas.get(i);

            // respuesta correcta (normalizada pa comparar)
            String correcta = normalizar((String) p.get("respuesta_correcta"));

            // lo q escribio el usuario (tambien normalizado pa comparar)
            String usuario = normalizar(respuestas.getOrDefault(i, ""));

            // aca vemos si es correcta usando similitud (no es exacto, es inteligente jeje)
            boolean esCorrecta = compararSimilitud(usuario, correcta);

            if (esCorrecta) {
                correctas++; // sumamos un punto si la pego
            }

            // armamos el detalle de esta pregunta pa el jsp
            Map<String, Object> detalle = new HashMap<>();
            detalle.put("pregunta", p.get("pregunta"));
            detalle.put("respuesta_correcta", p.get("respuesta_correcta"));
            detalle.put("respuesta_usuario", respuestas.getOrDefault(i, ""));
            detalle.put("esCorrecta", esCorrecta);
            detalle.put("oracion_original", p.get("oracion_original"));

            resultadoDetalle.add(detalle);
        }

        // calculamos el puntaje total (porcentaje)
        int puntaje = (correctas * 100) / total;

        // mandamos los datos al jsp de resultados
        request.setAttribute("puntaje", puntaje);
        request.setAttribute("correctas", correctas);
        request.setAttribute("total", total);
        request.setAttribute("detalle", resultadoDetalle);

        request.getRequestDispatcher("resultado_examen.jsp").forward(request, response);
    }



    //    esto basicamente quita tildes, pone todo en minuscula,
    //    y limpia el texto pa que comparar sea justo


    private String normalizar(String s) {
        if (s == null) return "";
        s = s.toLowerCase().trim(); // todo en minuscula, sin espacios raros
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // quita tildes y ñs raras
        return s;
    }



    //    esta funcion no exige q la respuesta sea EXACTA,
    //    sino q tenga como 80% de similitud. sirve pa errores ortográficos


    private boolean compararSimilitud(String a, String b) {

        if (a.equals(b)) return true; // si son iguales exactitas ya fue, es correcta de una

        int dist = distanciaLevenshtein(a, b); // distancia de edicion (cuantos cambios pa volverse iguales)

        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return false;

        // calculamos porcentaje de similitud
        double similitud = 1 - ((double) dist / maxLen);

        return similitud >= 0.80; // si es 80% parecida, la damos por buena
    }


    private int distanciaLevenshtein(String a, String b) {
        // matriz pa calcular distancia
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        // inicializamos la primera fila y columna
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        // aca vamos caracter por caracter viendo cuantos cambios se necesitan
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {

                int costo = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), // insertar o eliminar
                        dp[i - 1][j - 1] + costo // reemplazar si es distinto
                );
            }
        }

        return dp[a.length()][b.length()]; // distancia total
    }
}
