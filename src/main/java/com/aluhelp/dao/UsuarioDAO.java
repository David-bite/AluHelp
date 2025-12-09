/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.dao;

import com.aluhelp.modelo.Usuario;

/**
 *
 * @author HP
 */
public interface UsuarioDAO {
    
    // Para login
    Usuario buscarPorCorreoYContrasena(String correo, String contrasena) throws Exception;

    // Ver si ya existe un correo (para registro)
    boolean existeCorreo(String correo) throws Exception;

    // Registrar nuevo usuario
    void registrar(Usuario usuario) throws Exception;

}
