<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, com.aluhelp.database.ConexionBD" %>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>AluHelp - Mis Documentos</title>
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

            int usuarioId = (Integer) session.getAttribute("usuarioId");
        %>


        <div class="max-w-5xl mx-auto px-6 py-16 md:py-24 text-center relative z-10">
            <h2 class="text-7xl md:text-7xl font-bold text-[#121c38] mb-3">Mis Documentos</h2>

            <a href="index.jsp" 
               class="px-6 py-2 bg-[#01587a] text-white rounded-lg hover:bg-[#014965] transition transform hover:scale-105 w-full md:w-auto">
                Regresar</a>

            <table class="w-full border-collapse border border-black mt-6 bg-[#F2F2F2]">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="p-3 text-left border border-black text-base">Nombre</th>
                        <th class="p-3 text-left border border-black text-base">Descargar</th>
                        <th class="p-3 text-left border border-black text-base">Acciones</th>
                        <th class="p-3 text-center border border-black text-base w-24">Eliminar</th> </tr>
                </thead>

                <tbody>
                    <%
                        try (Connection conn = ConexionBD.getConnection()) {
                            String sql = "SELECT nombre, file_path FROM documentos WHERE usuario_id = ?";
                            PreparedStatement stmt = conn.prepareStatement(sql);
                            stmt.setInt(1, usuarioId);
                            ResultSet rs = stmt.executeQuery();

                            while (rs.next()) {
                    %>
                    <tr class="hover:bg-gray-100">

                        <td class="p-3 border border-black align-middle text-base">
                            <%= rs.getString("nombre")%>
                        </td>

                        <td class="p-3 border border-black align-middle">
                            <a href="uploads/<%= rs.getString("file_path")%>" target="_blank" 
                               class="block w-full text-center bg-[#448C9C] text-white py-2 rounded hover:bg-[#4FA1B3] transition font-medium">
                                Ver PDF
                            </a>
                        </td>

                        <td class="p-3 border border-black align-middle">
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-2">

                                <form action="resumen" method="post" class="w-full">
                                    <input type="hidden" name="file" value="<%= rs.getString("file_path")%>">
                                    <button type="submit" class="w-full py-2 bg-[#448C9C] text-white text-sm rounded hover:bg-[#4FA1B3] transition">
                                        Generar Resumen
                                    </button>
                                </form>

                                <form action="preguntasPDF" method="post" class="w-full">
                                    <input type="hidden" name="file" value="<%= rs.getString("file_path")%>">
                                    <button type="submit" class="w-full py-2 bg-[#448C9C] text-white text-sm rounded hover:bg-[#4FA1B3] transition">
                                        Generar Quiz
                                    </button>
                                </form>

                                <form action="unir_pdf.jsp" method="get" class="w-full">
                                    <button type="submit" class="w-full py-2 bg-[#448C9C] text-white text-sm rounded hover:bg-[#4FA1B3] transition">
                                        Unir PDF
                                    </button>
                                </form>

                                <form action="pdfToAudio" method="post" class="w-full">
                                    <input type="hidden" name="file" value="<%= rs.getString("file_path")%>">
                                    <button type="submit" class="w-full py-2 bg-[#448C9C] text-white text-sm rounded hover:bg-[#4FA1B3] transition">
                                        PDF a MP3
                                    </button>
                                </form>
                            </div>
                        </td>

                        <td class="p-3 border border-black align-middle text-center">
                            <form action="BorrarPDFServlet" method="post" onsubmit="return confirm('¿Seguro que deseas eliminar este documento?');">
                                <input type="hidden" name="file" value="<%= rs.getString("file_path")%>">
                                <button type="submit" class="p-2 bg-red-600 text-white rounded hover:bg-red-700 transition transform hover:scale-110 shadow-md" title="Eliminar Documento">
                                    <i class="ri-delete-bin-6-line text-xl"></i>
                                </button>
                            </form>
                        </td>

                    </tr>
                    <%
                            }
                        } catch (Exception e) {
                            out.println("<tr><td colspan='4' class='p-4 text-red-600 border border-black'>Error: " + e.getMessage() + "</td></tr>");
                        }
                    %>
                </tbody>
            </table>
        </div>


    </body>
</html>
