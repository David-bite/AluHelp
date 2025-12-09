<!-- /login.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>

    <body class="bg-gray-100 flex items-center justify-center h-screen">

        <div class="bg-white p-8 rounded-2xl shadow-md w-96">

            <h2 class="text-2xl font-bold text-center mb-6">Iniciar Sesión</h2>

            <!-- error si existe -->
            <% if (request.getAttribute("error") != null) {%>
            <p class="text-red-500 text-sm mb-4 text-center">
                <%= request.getAttribute("error")%>
            </p>
            <% } %>

            <!-- mensaje de registro exitoso -->
            <% if (request.getParameter("exito") != null) { %>
            <p class="text-green-600 text-sm mb-4 text-center">
                Registro exitoso. Ahora puedes iniciar sesión.
            </p>
            <% }%>

            <!-- FORMULARIO -->
            <form action="login" method="post" class="space-y-4">

                <div>
                    <label class="block text-gray-700">Correo</label>
                    <input type="email" name="correo" required
                           class="w-full border rounded-lg px-3 py-2 
                           focus:outline-none focus:ring focus:ring-blue-300">
                </div>

                <div>
                    <label class="block text-gray-700">Contraseña</label>
                    <input type="password" name="contrasena" required
                           class="w-full border rounded-lg px-3 py-2 
                           focus:outline-none focus:ring focus:ring-blue-300">
                </div>

                <button type="submit"
                        
                        class="px-6 py-2 bg-[#01587a] text-white rounded-lg hover:bg-[#014965] transition transform w-full ">
                    Entrar
                </button>
            </form>

            <p class="text-sm text-center mt-4">
                ¿No tienes cuenta?
                <a href="registro.jsp" class="text-blue-600 hover:underline">Regístrate aquí</a>
            </p>

        </div>

    </body>
</html>
