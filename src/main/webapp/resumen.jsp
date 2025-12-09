<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Resumen Generado</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdn.jsdelivr.net/npm/remixicon@4.3.0/fonts/remixicon.css" rel="stylesheet">

    </head>
    <body class="min-h-screen text-[#101b24]" 
          style="background-image: linear-gradient(90deg, #d1d5db 1px, transparent 1px),
          linear-gradient(#d1d5db 1px, transparent 1px);
          background-size: 30px 30px; padding: 50px;">

        <!-- HEADER -->
        <header class="fixed top-0 left-0 w-full z-[1000] bg-[#01587a] text-white shadow-md">
            <div class="max-w-7xl mx-auto flex justify-between items-center px-6 py-4">
                <div class="flex items-center space-x-3">
                    <img src="Imagenes/logoaluhelp.png" alt="logo" class="w-10 h-10">
                    <h1 class="text-2xl font-bold tracking-wide">AluHelp</h1>
                </div>
                <nav>
                    <ul class="flex space-x-12 text-sm font-semibold">
                        <li><a href="logout" class="bg-white text-[#01587a] px-5 py-1 rounded-lg hover:bg-[#e0f2f7] transition">Salir</a></li>
                    </ul>
                </nav>
            </div>
        </header>

        <%
            if (session.getAttribute("usuarioId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>

<div class="max-w-4xl mx-auto my-10">
    <div class="bg-gray-50 border border-gray-200 rounded-xl shadow-sm p-8 md:p-12">
        
        <h2 class="text-3xl md:text-4xl font-bold text-center text-[#121c38] mb-10">
            Resumen Generado
        </h2>

        <div class="mb-10">
            <h3 class="text-xs font-bold text-indigo-900 uppercase tracking-wider mb-3 flex items-center gap-2">
                Resumen
            </h3>
            <div class="text-gray-800 text-lg leading-relaxed font-medium">
                <%= request.getAttribute("resumen")%>
            </div>
        </div>

        <hr class="border-gray-300 my-8">

        <div>
            <h3 class="text-xs font-bold text-gray-500 uppercase tracking-wider mb-3">
                Texto original
            </h3>
            <div class="bg-white p-6 rounded-lg border border-gray-200 shadow-inner">
                <p class="text-gray-600 text-sm leading-relaxed italic">
                    <%= request.getAttribute("original")%>
                </p>
            </div>
        </div>

    </div>
</div>



        <a class="w-full block text-center bg-[#01587a] text-white py-2 rounded-lg hover:bg-[#014965] transition"
           href="mis_documentos.jsp">Volver</a>

    </body>
</html>
