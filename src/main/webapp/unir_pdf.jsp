<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, com.aluhelp.database.ConexionBD" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Unir PDF - AluHelp</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>

    <body class="bg-gray-100 p-10">

        <h1 class="text-4xl font-bold text-center mb-8 text-[#01587a]">Unir documentos PDF</h1>

        <%
            Integer id = (Integer) session.getAttribute("usuarioId");
            if (id == null) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>

        <!-- MENSAJES -->
        <% if (request.getAttribute("mensaje") != null) {%>
        <p class="text-green-600 text-center mb-4 text-xl"><%= request.getAttribute("mensaje")%></p>
        <% } %>

        <% if (request.getAttribute("error") != null) {%>
        <p class="text-red-600 text-center mb-4 text-xl"><%= request.getAttribute("error")%></p>
        <% } %>

        <form action="unirpdf" method="post" class="max-w-xl mx-auto p-6 bg-white rounded-lg shadow">

            <p class="text-lg font-semibold mb-4">Selecciona los PDFs que deseas unir:</p>

            <%
                try (Connection conn = ConexionBD.getConnection()) {

                    String sql = "SELECT nombre, file_path FROM documentos WHERE usuario_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
            %>

            <label class="block p-2 bg-gray-100 rounded mb-2 hover:bg-gray-200 cursor-pointer">
                <input type="checkbox" name="pdfs" value="<%= rs.getString("file_path")%>">
                <%= rs.getString("nombre")%>
            </label>

            <%
                    }
                } catch (Exception e) {
                    out.print("Error: " + e.getMessage());
                }
            %>

            <button type="submit" class="w-full mt-6 bg-[#01587a] text-white py-3 rounded-lg hover:bg-[#014965]">
                Unir documentos seleccionados
            </button>

        </form>

        <div class="text-center mt-8">
            <a href="MisDocumentosServlet" class="text-blue-600 underline">Volver a Mis Documentos</a>
        </div>

    </body>
</html>
