package com.aluhelp.database;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@WebListener
public class DBInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            // CONEXIÓN AL SERVIDOR (no a una BD)
            String url = "jdbc:postgresql://localhost:5432/";
            String user = "postgres";
            String pass = "123456";

            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();

            // 1. CREAR BD SI NO EXISTE
            st.executeUpdate("CREATE DATABASE aluhelp");

            conn.close();

        } catch (Exception e) {
            System.out.println("BD ya existía, continuando...");
        }

        // 2. CREAR TABLAS
        try {
            String url = "jdbc:postgresql://localhost:5432/aluhelp";
            String user = "postgres";
            String pass = "123456";

            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();

            // TABLAS
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                    id SERIAL PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    correo VARCHAR(255) UNIQUE NOT NULL,
                    contrasena VARCHAR(255) NOT NULL
                )
            """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS documentos (
                    id SERIAL PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    usuario_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
                    file_path VARCHAR(255)
                )
            """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS resumenes (
                    id SERIAL PRIMARY KEY,
                    usuario_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
                    texto_original TEXT NOT NULL,
                    texto_resumido TEXT NOT NULL
                )
            """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS preguntas (
                    id SERIAL PRIMARY KEY,
                    usuario_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
                    pregunta TEXT NOT NULL
)
            """);

            conn.close();
            System.out.println("Tablas verificadas o creadas automáticamente.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
