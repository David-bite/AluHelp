/*PreguntasTarjetaServlet.java*/
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

        HttpSession session = request.getSession(); // saco la sesion pa ver si ya hay preguntas guardadas

        // Obtener preguntas generadas
        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");
        // aca deberian estar las preguntas generadas del pdf

        // Si no hay preguntas, regresar
        if (preguntas == null || preguntas.isEmpty()) {
            // si se intento entrar al examen sin generar preguntas, lo mando a documentos nomas
            response.sendRedirect("mis_documentos.jsp");
            return;
        }

        // Obtener índice (pregunta actual)
        int index = 0;
        try {
            // agarro el numero de la pregunta actual (?p=0, ?p=1, etc)
            index = Integer.parseInt(request.getParameter("p"));
        } catch (Exception ignored) {
            // si no mandaron nada, lo dejo en 0 nomas
        }

        // Control de límites
        if (index < 0) index = 0; // no se puede ir antes de la primera
        if (index >= preguntas.size()) index = preguntas.size() - 1; // ni despues de la ultima

        // Enviar datos al JSP
        request.setAttribute("index", index); // numero de pregunta actual
        request.setAttribute("total", preguntas.size()); // total de preguntas
        request.setAttribute("pregunta", preguntas.get(index)); // la pregunta actual a mostrar

        // mando al jsp q pinta la tarjeta
        request.getRequestDispatcher("preguntas_tarjeta.jsp").forward(request, response);
    }



    //   PROCESAR RESPUESTA DEL ALUMNO
    //   (cuando el alumno envia lo que escribio en la cajita de texto del examen)

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(); // otra vez la sesion pa recuperar info

        List<Map<String, Object>> preguntas = (List<Map<String, Object>>) session.getAttribute("preguntasPDF");
        // preguntas del examen guardadas en la sesion

        int index = Integer.parseInt(request.getParameter("index")); 
        // agarro el numero de pregunta en la q está el alumno ahora

        String respuestaEscrita = request.getParameter("respuesta");
        // lo q escribio el alumno en la caja de texto

        // Guardar o actualizar la respuesta del usuario
        Map<Integer, String> respuestas = (Map<Integer, String>) session.getAttribute("respuestasExamen");

        if (respuestas == null) {
            // si aun no se ha creado el map, lo creo aca
            respuestas = new HashMap<>();
        }

        respuestas.put(index, respuestaEscrita); // guardo la respuesta de esa pregunta
        session.setAttribute("respuestasExamen", respuestas); // la meto de vuelta en sesion

        // Si ya terminó → corregir
        if (index == preguntas.size() - 1) {
            // si estamos en la ultima pregunta y ya respondio →
            // lo mando al servlet q corrige todo
            response.sendRedirect("corregir");
            return;
        }

        // Siguiente pregunta
        // si aun no termina, lo mando a la siguiente pregunta nomas
        response.sendRedirect("examen?p=" + (index + 1));
    }
}
