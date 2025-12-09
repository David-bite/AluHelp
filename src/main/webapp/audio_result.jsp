<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, com.aluhelp.database.ConexionBD" %>
<html>
    <head>
        <title>PDF A MP3</title>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdn.jsdelivr.net/npm/remixicon@4.3.0/fonts/remixicon.css" rel="stylesheet">

    </head>
    <body class="min-h-screen text-[#101b24]" 
          style="background-image: linear-gradient(90deg, #d1d5db 1px, transparent 1px),
          linear-gradient(#d1d5db 1px, transparent 1px);
          background-size: 30px 30px; padding: 50px;">

    </body>


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

    <div class="max-w-5xl mx-auto px-6 py-16 md:py-24 text-center relative z-10">
        <h2 class="text-4xl font-bold text-center mb-8 text-[#01587a]">Audio generado</h2>

        <audio controls>
            <source src="uploads/<%= request.getAttribute("audioFile")%>" type="audio/mp3">
        </audio>

        <br><br>

        <a href="mis_documentos.jsp" class="w-full block text-center bg-[#01587a] text-white py-2 rounded-lg hover:bg-[#014965] transition">Volver</a>
    </div>


</html>



