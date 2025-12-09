<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<%
    if (session.getAttribute("usuarioId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Map<String, Object> pregunta = (Map<String, Object>) request.getAttribute("pregunta");
    int index = (Integer) request.getAttribute("index");
    int total = (Integer) request.getAttribute("total");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Examen - Pregunta</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #eef2f7;
            display: flex;
            justify-content: center;
            padding-top: 40px;
        }

        .card {
            background: white;
            width: 500px;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 6px 25px rgba(0,0,0,0.15);
        }

        .contador {
            font-size: 14px;
            color: gray;
            margin-bottom: 15px;
        }

        .pregunta {
            font-size: 17px;
            margin-bottom: 25px;
            line-height: 1.5;
            white-space: pre-line;
        }

        .campo-respuesta {
            width: 100%;
            padding: 12px;
            font-size: 15px;
            border-radius: 8px;
            border: 1px solid #ccc;
            margin-bottom: 20px;
            outline: none;
        }

        .campo-respuesta:focus {
            border-color: #3577ff;
            box-shadow: 0 0 5px rgba(53, 119, 255, 0.4);
        }

        button {
            width: 100%;
            padding: 14px;
            background: #3577ff;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: 0.2s;
        }

        button:hover {
            background: #285fcc;
        }
    </style>
</head>

<body>

<div class="card">

    <p class="contador">Pregunta <%= index + 1 %> de <%= total %></p>

    <div class="pregunta">
        <%= pregunta.get("pregunta") %>
    </div>

    <form action="examen" method="post">

        <!-- 🟦 CAMPO DE TEXTO PARA COMPLETAR -->
        <input type="text" name="respuesta" class="campo-respuesta"
               placeholder="Escribe tu respuesta aquí..." required>

        <input type="hidden" name="index" value="<%= index %>">

        <button type="submit">
            <%= (index == total - 1) ? "Finalizar Examen" : "Siguiente" %>
        </button>

    </form>

</div>

</body>
</html>
