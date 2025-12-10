/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.modelo;

/**
 *
 * @author HP
 */
public class Documento {

    private int id;
    private int usuarioId;
    private String nombre;
    private String filePath;

    public Documento() {
    }

    public Documento(int id, int usuarioId, String nombre, String filePath) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.filePath = filePath;
    }

    public Documento(int usuarioId, String nombre, String filePath) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.filePath = filePath;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
