/*/MisDocumentosServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.DocumentoDAO;
import com.aluhelp.daoimpl.DocumentoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import com.aluhelp.modelo.Documento;
import java.util.*;

@WebServlet("/MisDocumentosServlet")
public class MisDocumentosServlet extends HttpServlet {

    private DocumentoDAO documentoDAO = new DocumentoDAOImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            
            List<Documento> docs = documentoDAO.listarPorUsuario(usuarioId);
            request.setAttribute("docs", docs);
            

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "ERROR");
        }

        request.getRequestDispatcher("mis_documentos.jsp").forward(request, response);
    }
}
