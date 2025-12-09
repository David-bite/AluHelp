package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@WebServlet("/pdfToAudio")
public class PdfAudioServlet extends HttpServlet {

    private static final String API_KEY = "745fcfaf5b00484d8545b2edf3fb75bd";

    // ---- FIX SSL ----
    private static void disableSSLVerification() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
    // -------------------

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try { disableSSLVerification(); } 
        catch (Exception e) { e.printStackTrace(); }

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String fileName = request.getParameter("file");
        if (fileName == null) {
            request.setAttribute("error", "Debe seleccionar archivo PDF.");
            request.getRequestDispatcher("mis_documentos.jsp").forward(request, response);
            return;
        }

        String pdfPath = getServletContext().getRealPath("/uploads/") + fileName;

        String texto = "";
        try (PDDocument doc = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            texto = stripper.getText(doc);
        }

        if (texto.trim().isEmpty()) {
            request.setAttribute("error", "El PDF no contiene texto legible.");
            request.getRequestDispatcher("mis_documentos.jsp").forward(request, response);
            return;
        }

        texto = texto.replace("\n", " ").replace("\r", " ");

        int CHUNK = 300;
        int totalChunks = (int) Math.ceil((double) texto.length() / CHUNK);

        ByteArrayOutputStream audioFinal = new ByteArrayOutputStream();

        for (int i = 0; i < totalChunks; i++) {
            int start = i * CHUNK;
            int end = Math.min(start + CHUNK, texto.length());
            String parte = texto.substring(start, end);

            String urlStr =
                    "https://api.voicerss.org/?key=" + API_KEY +
                    "&hl=es-es" +
                    "&src=" + parte.replace(" ", "%20");

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = conn.getInputStream();
            audioFinal.write(in.readAllBytes());
            in.close();
        }

        String audioName = fileName.replace(".pdf", "") + "_audio.mp3";
        String audioPath = getServletContext().getRealPath("/uploads/") + audioName;

        FileOutputStream fos = new FileOutputStream(audioPath);
        fos.write(audioFinal.toByteArray());
        fos.close();

        request.setAttribute("audioFile", audioName);
        request.getRequestDispatcher("audio_result.jsp").forward(request, response);
    }
}
