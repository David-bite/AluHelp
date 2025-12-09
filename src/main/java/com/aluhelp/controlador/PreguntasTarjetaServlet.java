package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/examen")
public class PreguntasTarjetaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Obtener preguntas generadas
        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");

        // Si no hay preguntas, regresar
        if (preguntas == null || preguntas.isEmpty()) {
            response.sendRedirect("mis_documentos.jsp");
            return;
        }

        // Obtener índice (pregunta actual)
        int index = 0;
        try {
            index = Integer.parseInt(request.getParameter("p"));
        } catch (Exception ignored) {}

        // Control de límites
        if (index < 0) index = 0;
        if (index >= preguntas.size()) index = preguntas.size() - 1;

        // Enviar datos al JSP
        request.setAttribute("index", index);
        request.setAttribute("total", preguntas.size());
        request.setAttribute("pregunta", preguntas.get(index));

        request.getRequestDispatcher("preguntas_tarjeta.jsp").forward(request, response);
    }


    // =====================================
    //   PROCESAR RESPUESTA DEL ALUMNO
    // =====================================

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");

        int index = Integer.parseInt(request.getParameter("index"));
        String respuestaEscrita = request.getParameter("respuesta");

        // Guardar o actualizar la respuesta del usuario
        Map<Integer, String> respuestas = (Map<Integer, String>) session.getAttribute("respuestasExamen");

        if (respuestas == null) {
            respuestas = new HashMap<>();
        }

        respuestas.put(index, respuestaEscrita);
        session.setAttribute("respuestasExamen", respuestas);

        // Si ya terminó → corregir
        if (index == preguntas.size() - 1) {
            response.sendRedirect("corregir");
            return;
        }

        // Siguiente pregunta
        response.sendRedirect("examen?p=" + (index + 1));
    }
}
