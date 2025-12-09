<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
    List<String> pdfs = (List<String>) request.getAttribute("pdfs");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Ordenar Archivos</title>
        <script src="https://cdn.tailwindcss.com"></script>

        <style>
            .item {
                padding: 12px;
                background: #e0f7fa;
                margin-bottom: 8px;
                border-radius: 8px;
                cursor: move;
            }
        </style>
    </head>

    <body class="bg-gray-100 p-10">

        <h2 class="text-3xl font-bold mb-4">Ordena los PDFs</h2>

        <ul id="lista" class="max-w-md">
            <% for (String p : pdfs) { %>
            <li class="item" draggable="true"><%= p %></li>
                <% } %>
        </ul>

        <button onclick="unir()" class="px-4 py-2 bg-blue-600 text-white rounded mt-4">
            Unir PDFs en este orden
        </button>

        <form id="finalForm" action="unirOrdenados" method="post">
            <input type="hidden" name="orden" id="ordenInput">
        </form>

        <script>
            const lista = document.getElementById("lista");

            let dragged;

            document.addEventListener("dragstart", e => {
                dragged = e.target;
            });

            document.addEventListener("dragover", e => e.preventDefault());

            document.addEventListener("drop", e => {
                if (e.target.classList.contains("item")) {
                    lista.insertBefore(dragged, e.target);
                }
            });

            function unir() {
                let orden = [];
                document.querySelectorAll(".item").forEach(el => {
                    orden.push(el.textContent.trim());
                });

                document.getElementById("ordenInput").value = JSON.stringify(orden);
                document.getElementById("finalForm").submit();
            }
        </script>

    </body>
</html>
