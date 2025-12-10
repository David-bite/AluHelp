/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.aluhelp.modelo;

/**
 *
 * @author HP
 */
public class Resumen {

    private int id;
    private int usuarioId;
    private String textoOriginal;
    private String textoResumido;

    public Resumen(int id, int usuarioId, String textoOriginal, String textoResumido) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.textoOriginal = textoOriginal;
        this.textoResumido = textoResumido;
    }

    public Resumen(int usuarioId, String textoOriginal, String textoResumido) {
        this.usuarioId = usuarioId;
        this.textoOriginal = textoOriginal;
        this.textoResumido = textoResumido;
    }

    public Resumen() {
    }

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public String getTextoResumido() {
        return textoResumido;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public void setTextoResumido(String textoResumido) {
        this.textoResumido = textoResumido;
    }
    
    
}
