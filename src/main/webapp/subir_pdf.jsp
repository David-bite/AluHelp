<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Subir PDF</title>
</head>
<body>

<%
    if (session.getAttribute("usuarioId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<h2>Subir un PDF</h2>

<form action="uploadpdf" method="post" enctype="multipart/form-data">
    <label>Selecciona un archivo PDF:</label><br>
    <input type="file" name="pdf" accept="application/pdf" required><br><br>

    <button type="submit">Subir PDF</button>
</form>

<%
    if (request.getAttribute("mensaje") != null) {
%>
    <p style="color:green;"><%= request.getAttribute("mensaje") %></p>
<%
    }
%>

</body>
</html>
