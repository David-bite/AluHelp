<!--/registro.jsp  -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro - AluHelp</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>

    <body class="bg-gray-100 flex items-center justify-center h-screen">

        <div class="bg-white p-8 rounded-2xl shadow-md w-96">

            <h2 class="text-2xl font-bold text-center mb-6">Crear Cuenta</h2>

            <!-- MENSAJE DE ERROR -->
            <% if (request.getAttribute("error") != null) {%>
            <p class="text-red-500 text-sm mb-4 text-center">
                <%= request.getAttribute("error")%>
            </p>
            <% }%>

            <!-- FORMULARIO  -->
            <form action="registro" method="post" class="space-y-4">

                <div>
                    <label class="block text-gray-700">Nombre</label>
                    <input type="text" name="nombre" required
                           class="w-full border rounded-lg px-3 py-2 
                           focus:outline-none focus:ring focus:ring-green-300">
                </div>

                <div>
                    <label class="block text-gray-700">Correo</label>
                    <input type="email" name="correo" required
                           class="w-full border rounded-lg px-3 py-2 
                           focus:outline-none focus:ring focus:ring-green-300">
                </div>

                <div>
                    <label class="block text-gray-700">Contraseña</label>
                    <input type="password" name="contrasena" required
                           class="w-full border rounded-lg px-3 py-2 
                           focus:outline-none focus:ring focus:ring-green-300">
                </div>

                <button type="submit"
                        class="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700">
                    Registrarse
                </button>
            </form>

            <p class="text-sm text-center mt-4">
                ¿Ya tienes cuenta?
                <a href="login.jsp" class="text-blue-600 hover:underline">Inicia sesión</a>
            </p>

        </div>

    </body>
</html>
