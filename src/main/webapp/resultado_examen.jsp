<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>Resultado del Examen</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #eef2f7;
            padding: 30px;
        }

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

        .puntaje {
            font-size: 45px;
            font-weight: bold;
            color: #2c6bff;
        }

        .detalle {
            color: gray;
            font-size: 18px;
            margin-top: 5px;
        }

        /* Tarjetas de detalle */
        .pregunta-card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 20px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.12);
        }

        .titulo-pregunta {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 10px;
            white-space: pre-line;
        }

        .respuesta-usuario {
            margin-top: 10px;
            font-size: 15px;
            color: #333;
        }

        .correcta {
            color: green;
            font-weight: bold;
        }

        .incorrecta {
            color: red;
            font-weight: bold;
        }

        .texto-correcta {
            background: #e8f3ff;
            padding: 8px;
            border-radius: 6px;
            margin-top: 8px;
            font-size: 14px;
        }

        .original {
            margin-top: 10px;
            font-size: 13px;
            color: #555;
            background: #f7f7f7;
            padding: 8px;
            border-radius: 6px;
        }

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
<body>

<!-- TARJETA PRINCIPAL -->
<div class="card-resumen">
    <div class="puntaje"><%= request.getAttribute("puntaje") %>/100</div>
    <div class="detalle">
        Correctas: <%= request.getAttribute("correctas") %> de <%= request.getAttribute("total") %>
    </div>
</div>

<%
    List<Map<String, Object>> detalle = (List<Map<String, Object>>) request.getAttribute("detalle");
    int numero = 1;
%>

<!-- DETALLE DE PREGUNTA POR PREGUNTA -->
<% for (Map<String, Object> d : detalle) { %>

    <div class="pregunta-card">

        <div class="titulo-pregunta">
            <%= numero++ %>. <%= d.get("pregunta") %>
        </div>

        <% if ((boolean) d.get("esCorrecta")) { %>
            <p class="correcta">✔ Respuesta correcta</p>
        <% } else { %>
            <p class="incorrecta">✘ Respuesta incorrecta</p>

            <div class="texto-correcta">
                Correcta: <b><%= d.get("respuesta_correcta") %></b>
            </div>
        <% } %>

        <div class="respuesta-usuario">
            Tu respuesta: <b><%= d.get("respuesta_usuario") %></b>
        </div>

        <div class="original">
            Fragmento original:<br>
            "<%= d.get("oracion_original") %>"
        </div>

    </div>

<% } %>

<div style="text-align:center;">
    <a href="mis_documentos.jsp" class="volver">Volver a mis documentos</a>
</div>

</body>
</html>
