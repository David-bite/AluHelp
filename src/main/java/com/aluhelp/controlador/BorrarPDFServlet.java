/*BorrarPDFServlet.java*/
package com.aluhelp.controlador;

import com.aluhelp.dao.DocumentoDAO;
import com.aluhelp.daoimpl.DocumentoDAOImpl;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;

/**
 *
 * @author HP
 */
@WebServlet(name = "BorrarPDFServlet", urlPatterns = {"/BorrarPDFServlet"})
public class BorrarPDFServlet extends HttpServlet {

    private DocumentoDAO documentoDAO = new DocumentoDAOImpl();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String fileName = request.getParameter("file");
        if (fileName == null) {
            response.sendRedirect("mis_documentos.jsp");
        }

        try  {
            documentoDAO.borrarDocumento(usuarioId,fileName);


        } catch (Exception e) {
            e.printStackTrace();
        }

        //BORRAR FISICO
        String uploadPath = getServletContext().getRealPath("") + "uploads" + File.separator + fileName;

        File archivo = new File(uploadPath);

        if (archivo.exists()) {
            archivo.delete();
        }

        response.sendRedirect("mis_documentos.jsp");
    }

}
