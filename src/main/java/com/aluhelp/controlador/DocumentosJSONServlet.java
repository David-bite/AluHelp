package com.aluhelp.controlador;

import com.aluhelp.dao.DocumentoDAO;
import com.aluhelp.daoimpl.DocumentoDAOImpl;
import com.aluhelp.modelo.Documento;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/documentos")
public class DocumentosJSONServlet extends HttpServlet {

    private DocumentoDAO dao = new DocumentoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            resp.setStatus(401);
            return;
        }

        String q = req.getParameter("q");
        if (q == null) q = "";

        try {
            List<Documento> lista = dao.buscarPorUsuarioYNombre(usuarioId, q);

            JSONArray array = new JSONArray();
            for (Documento d : lista) {
                JSONObject obj = new JSONObject();
                obj.put("nombre", d.getNombre());
                obj.put("file", d.getFilePath());
                array.put(obj);
            }

            PrintWriter out = resp.getWriter();
            out.print(array.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
