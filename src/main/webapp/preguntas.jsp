<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Preguntas del PDF</title>
        <style>
            .pregunta-container {
                margin-bottom: 20px;
                border-bottom: 1px solid #ccc;
                padding-bottom: 10px;
            }
            .enunciado {
                font-weight: bold;
                display: block;
                margin-bottom: 5px;
            }
            .opcion {
                margin-left: 20px;
            }
        </style>
    </head>
    <body>

        <h2>Preguntas generadas del PDF: <%= request.getAttribute("nombreArchivo")%></h2>

        <%
            // CORRECCIÓN 1: Recibimos una Lista de Mapas, no de Strings
            List<Map<String, Object>> listaPreguntas = (List<Map<String, Object>>) request.getAttribute("preguntas");

            if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
                int contador = 1;
                // CORRECCIÓN 2: Iteramos sobre Map<String, Object>
                for (Map<String, Object> p : listaPreguntas) {
                    String enunciado = (String) p.get("pregunta");
                    List<String> opciones = (List<String>) p.get("opciones");
                    String respuestaCorrecta = (String) p.get("correcta"); // Por si quieres mostrarla u ocultarla
        %>

        <div class="pregunta-container">
            <span class="enunciado"><%= contador++%>. <%= enunciado%></span>

            <ul>
                <label>Escribe tu respuesta:</label>
                <input type="text" name="respuesta" required class="campo-respuesta">

            </ul>

            <details>
                <summary>Ver respuesta correcta</summary>
                <p>Correcta: <b><%= respuestaCorrecta%></b></p>
            </details>
        </div>

        <%
            }
        } else {
        %>
        <p>No se pudieron generar preguntas o la lista está vacía.</p>
        <%
            }
        %>

        <br>
        <a href="mis_documentos.jsp">Volver a Mis Documentos</a>

    </body>
</html>