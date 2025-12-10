/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.dao;

import com.aluhelp.modelo.Documento;
import java.util.List;


/**
 *
 * @author HP
 */

public interface DocumentoDAO  {

    
    List<Documento> listarPorUsuario(int usuarioId) throws Exception;

    void guardar(Documento documento) throws Exception;

    void borrarDocumento(int usuarioId, String filePath) throws Exception;

    Documento obtenerPorId(int id) throws Exception;
}
