/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aluhelp.dao;

import com.aluhelp.modelo.Resumen;
import java.util.List;

/**
 *
 * @author HP
 */
public interface ResumenDAO {
    
    void guardar(Resumen resumen) throws Exception;

    List<Resumen> listarPorUsuario(int usuarioId) throws Exception;

    Resumen obtenerPorId(int id) throws Exception;
}
