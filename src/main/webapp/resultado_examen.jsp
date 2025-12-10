<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Resultado del Examen</title>
        <meta charset="UTF-8">

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdn.jsdelivr.net/npm/remixicon@4.3.0/fonts/remixicon.css" rel="stylesheet">

        <style>

            /* esta es la tarjetita principal donde sale el puntaje */
            .card-resumen {
                background: white;
                width: 450px;
                margin: auto;
                padding: 25px;
                border-radius: 14px;
                box-shadow: 0 4px 20px rgba(0,0,0,0.15);
                text-align: center;
                margin-bottom: 40px;
            }

            /* el numerote del puntaje */
            .puntaje {
                font-size: 45px;
                font-weight: bold;
                color: #01587a;
            }

            /* texto “correctas X de Y” */
            .detalle {
                color: gray;
                font-size: 18px;
                margin-top: 5px;
            }

            /* tarjetas que muestran pregunta por pregunta */
            .pregunta-card {
                background: white;
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 20px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.12);
            }

            /* texto de la pregunta */
            .titulo-pregunta {
                font-size: 16px;
                font-weight: bold;
                margin-bottom: 10px;
                white-space: pre-line; /* pa que respete saltos de linea */
            }

            /* texto de la respuesta del alumno */
            .respuesta-usuario {
                margin-top: 10px;
                font-size: 15px;
                color: #333;
            }

            /* estilos pa correcto/incorrecto */
            .correcta {
                color: green;
                font-weight: bold;
            }

            .incorrecta {
                color: red;
                font-weight: bold;
            }

            /* cajita azul con la respuesta correcta */
            .texto-correcta {
                background: #e8f3ff;
                padding: 8px;
                border-radius: 6px;
                margin-top: 8px;
                font-size: 14px;
            }

            /* fragmento original del PDF */
            .original {
                margin-top: 10px;
                font-size: 13px;
                color: #555;
                background: #f7f7f7;
                padding: 8px;
                border-radius: 6px;
            }

            /* boton pa volver */
            .volver {
                display: inline-block;
                margin-top: 20px;
                padding: 12px 18px;
                background: #2b77ff;
                color: white;
                text-decoration: none;
                border-radius: 8px;
                font-size: 15px;
            }

        </style>
    </head>

    <body class="min-h-screen text-[#101b24]" 
          style="background-image: linear-gradient(90deg, #d1d5db 1px, transparent 1px),
          linear-gradient(#d1d5db 1px, transparent 1px);
          background-size: 30px 30px; padding: 50px">

        <!-- TARJETA PRINCIPAL DE PUNTAJE -->
        <div class="card-resumen">
            <!-- aca mostramos el puntaje calculado en el servlet -->
            <div class="puntaje"><%= request.getAttribute("puntaje")%>/100</div>

            <!-- cuantas correctas sacó -->
            <div class="detalle">
                Correctas: <%= request.getAttribute("correctas")%> de <%= request.getAttribute("total")%>
            </div>
        </div>

        <%
            // aca obtenemos la lista con cada pregunta evaluada
            List<Map<String, Object>> detalle = (List<Map<String, Object>>) request.getAttribute("detalle");
            int numero = 1; // pa numerar las preguntas
        %>

        <!-- TARJETAS DE PREGUNTA POR PREGUNTA -->
        <% for (Map<String, Object> d : detalle) {%>

        <div class="pregunta-card">

            <!-- muestro el enunciado -->
            <div class="titulo-pregunta">
                <%= numero++%>. <%= d.get("pregunta")%>
            </div>

            <% if ((boolean) d.get("esCorrecta")) { %>
            <!-- si la respuesta estuvo bien -->
            <p class="correcta">Respuesta correcta</p>

            <% } else {%>
            <!-- si estuvo mal -->
            <p class="incorrecta">Respuesta incorrecta</p>

            <div class="texto-correcta">
                Correcta: <b><%= d.get("respuesta_correcta")%></b>
            </div>
            <% }%>

            <!-- lo que el alumno escribió -->
            <div class="respuesta-usuario">
                Tu respuesta: <b><%= d.get("respuesta_usuario")%></b>
            </div>

            <!-- el fragmento original del PDF donde salia la palabra -->
            <div class="original">
                Fragmento original:<br>
                "<%= d.get("oracion_original")%>"
            </div>

        </div>

        <% }%>

        <!-- boton pa volver -->
        <div style="text-align:center;">
            <a class="px-6 py-2 bg-[#01587a] text-white rounded-lg hover:bg-[#014965] transition transform hover:scale-105 w-full md:w-auto" href="mis_documentos.jsp" class="volver">Volver a mis documentos</a>
        </div>

    </body>
</html>
