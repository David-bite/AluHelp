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

        <!-- titulo donde sale el nombre del archivo -->
        <h2>Preguntas generadas del PDF: <%= request.getAttribute("nombreArchivo")%></h2>

        <%
            // aca recibimos la lista de preguntas, q en realidad es una lista de Mapas
            // cada pregunta es un Map<String, Object> q tiene: "pregunta", "opciones", "correcta", etc
            List<Map<String, Object>> listaPreguntas = (List<Map<String, Object>>) request.getAttribute("preguntas");

            // validamos si hay preguntas o si esta vacia la lista
            if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
                int contador = 1; // pa numerar cada pregunta 1,2,3...

                // recorremos cada Map q representa una pregunta
                for (Map<String, Object> p : listaPreguntas) {

                    // agarro el texto de la pregunta
                    String enunciado = (String) p.get("pregunta");

                    // agarro la lista de opciones (aunque en este caso no se usa mucho)
                    List<String> opciones = (List<String>) p.get("opciones");

                    // agarro la respuesta correcta pa mostrarla en el <details>
                    String respuestaCorrecta = (String) p.get("correcta");
        %>

        <div class="pregunta-container">
            <!-- el enunciado de la pregunta, con numero -->
            <span class="enunciado"><%= contador++%>. <%= enunciado%></span>

            <ul>
                <!-- aca es donde el alumno escribe su respuesta -->
                <label>Escribe tu respuesta:</label>
                <input type="text" name="respuesta" required class="campo-respuesta">
            </ul>

            <!-- esto se puede abrir para ver la respuesta correcta -->
            <details>
                <summary>Ver respuesta correcta</summary>
                <p>Correcta: <b><%= respuestaCorrecta%></b></p>
            </details>
        </div>

        <%
            } // fin del for
        } else {
        %>

        <!-- mensaje si no hay preguntas -->
        <p>No se pudieron generar preguntas o la lista está vacía.</p>

        <%
            } // fin del else
%>

        <br>
        <!-- link pa regresar a mis documentos -->
        <a href="mis_documentos.jsp">Volver a Mis Documentos</a>

    </body>
</html>
