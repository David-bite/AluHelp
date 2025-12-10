/*PreguntasPDFServlet.java*/
package com.aluhelp.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@WebServlet("/preguntasPDF")
public class PreguntasPDFServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(); // agarro la sesion q ya existe (o se crea)
        Integer usuarioId = (Integer) session.getAttribute("usuarioId"); // aca veo si el user esta logueado

        if (usuarioId == null) { // si no hay usuario logeado
            response.sendRedirect("login.jsp"); // lo boto al login xd
            return;
        }

        // Recibir el nombre del archivo
        String fileName = request.getParameter("file"); // esto viene del jsp, es el nombre del pdf

        if (fileName == null) { // si no enviaron el nombre del archivo
            request.setAttribute("error", "No se seleccionó archivo."); // devolvemos mensajito
            request.getRequestDispatcher("mis_documentos.jsp").forward(request, response); // regresamos a documentos
            return;
        }

        // GUARDARLO EN SESIÓN (IMPORTANTE)
        session.setAttribute("archivoPDF", fileName); // guardo el archivo en sesion pa usarlo dspues

        // Ruta correcta al PDF
        String pdfPath = getServletContext().getRealPath("/uploads/") + fileName; // arma la ruta real del pdf (en el server)
        System.out.println("RUTA PDF = " + pdfPath); // imprimo pa ver si esta bien (debug feo pero sirve)

        // Leer texto del PDF
        String texto = "";
        try (InputStream input = new FileInputStream(new File(pdfPath)); PDDocument document = PDDocument.load(input)) { // aca abrimos el pdf

            PDFTextStripper stripper = new PDFTextStripper(); // este compa saca el texto del pdf
            texto = stripper.getText(document); // extraccion del texto, como si fuese copiar/pegar

        } catch (Exception e) { // si algo sale mal leyendo el pdf (muy comun)
            e.printStackTrace(); // se imprime error
            texto = ""; // texto vacio pa no romper todo
        }

        // Generar preguntas
        List<Map<String, Object>> preguntas = generarPreguntasTest(texto); // armamos las preguntitas

        // Guardar preguntas en sesión
        session.setAttribute("preguntasPDF", preguntas); // las guardo pa usarlas en el examen

        // Redirigir al examen
        response.sendRedirect("examen?p=0"); // lo mando al examen, empezando en pregunta 0
    }

    private List<Map<String, Object>> generarPreguntasTest(String textoOriginal) {

        List<Map<String, Object>> preguntas = new ArrayList<>();

        if (textoOriginal == null || textoOriginal.length() < 60) { // si el texto es muy corto no sirve pa preguntas
            return preguntas;
        }

        // 1. Normalizar texto
        String texto = textoOriginal
                .replace("\r", " ") // limpio regresos de carro
                .replace("\n", " ") // limpio saltos de linea
                .replaceAll("\\s+", " ") // unifico espacios
                .trim(); // quito espacios al inicio y fin

        // 2. Dividir en oraciones (., ?, !)
        String[] oraciones = texto.split("(?<=[\\.\\?\\!])\\s+"); // corta el texto por frases normales
        if (oraciones.length == 0) {
            return preguntas; // si no hay oraciones, gg
        }
        // 3. Construir mapa de frecuencia de palabras (para encontrar conceptos importantes)
        Map<String, Integer> frecuencia = new HashMap<>(); // aca cuento palabras importantes

        // Lista simple de stopwords en español
        Set<String> stopwords = new HashSet<>(Arrays.asList(
                // palabras inutiles q no sirven pa preguntas
                "de", "la", "que", "el", "en", "y", "a", "los", "se", "del", "las", "por", "un",
                "para", "con", "no", "una", "su", "al", "lo", "como", "más", "pero", "sus",
                "le", "ya", "o", "fue", "ha", "sí", "porque", "esta", "son", "entre", "cuando",
                "muy", "sin", "sobre", "también", "me", "hasta", "hay", "donde", "quien",
                "desde", "todo", "nos", "durante", "todos", "uno", "les", "ni", "contra",
                "otros", "ese", "eso", "había", "antes", "él", "esto", "mi", "algunos",
                "qué", "unos", "yo", "otro", "otras", "otra", "tanto", "esa", "estos",
                "mucho", "quienes", "nada", "muchos", "cual", "poco", "ella", "estar",
                "estas", "algo", "nosotros", "ustedes", "ellos", "ellas", "ser", "es",
                "era", "eran", "sea", "han", "he", "has", "hay", "haya", "dicho", "dice"
        ));

        // Sacamos palabras del texto para frecuencia
        String textoLower = texto.toLowerCase(); // todo en minuscula pa comparar sin problemas
        String[] palabras = textoLower.split("[^a-záéíóúñü0-9]+"); // separo por símbolos, espacios raros, etc

        for (String w : palabras) {
            if (w.length() < 5) {
                continue;           // muy cortas, no sirven
            }
            if (stopwords.contains(w)) {
                continue;    // palabras comunes q no aportan
            }
            if (w.matches("^[0-9]+$")) {
                continue;    // numeros sueltos tampoco sirven
            }
            frecuencia.put(w, frecuencia.getOrDefault(w, 0) + 1); // incremento contador
        }

        if (frecuencia.isEmpty()) {
            return preguntas; // si no hay palabras utiles, gg
        }
        // 4. Seleccionar las palabras más frecuentes como "conceptos clave"
        List<Map.Entry<String, Integer>> listaFreq = new ArrayList<>(frecuencia.entrySet());
        listaFreq.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // ordeno de mayor a menor frecuencia

        // Tomamos las top 30 como candidatos
        List<String> conceptosClave = new ArrayList<>();
        int limiteConceptos = Math.min(30, listaFreq.size());
        for (int i = 0; i < limiteConceptos; i++) {
            conceptosClave.add(listaFreq.get(i).getKey()); // palabras claves del texto
        }

        // 5. Patrón para años (por si queremos hacer huecos de fechas)
        Pattern fechaPattern = Pattern.compile("\\b(1[0-9]{3}|20[0-9]{2})\\b"); // buscar años: 1900, 2020, etc

        // Para evitar repetir siempre el mismo concepto
        Set<String> usados = new HashSet<>(); // pa no repetir la misma palabra en todas las preguntas

        // 6. Recorrer oraciones y crear preguntas
        for (String oracion : oraciones) {

            String oracionTrim = oracion.trim();
            if (oracionTrim.length() < 60) {
                continue; // si la oracion es muy corta no aporta nada
            }
            String oracionLower = oracionTrim.toLowerCase();

            String objetivo = null;      // palabra a ocultar
            String matchedReal = null;   // palabra exacta tal como aparece en el texto

            // 6.1 Intentar usar un concepto clave del texto
            for (String concepto : conceptosClave) {

                // si ya use esa palabra, la salto
                if (usados.contains(concepto)) {
                    continue;
                }

                // buscamo la palabra exacta, no parte de otra
                Pattern p = Pattern.compile("\\b" + Pattern.quote(concepto) + "\\b",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher m = p.matcher(oracionTrim);

                if (m.find()) {
                    objetivo = concepto;         // palabra clave en minuscula
                    matchedReal = m.group();     // como aparece en el texto (mayusculas etc)
                    break;
                }
            }

            // 6.2 Si no hay concepto clave, intentamos con fechas como fallback
            if (objetivo == null) {
                Matcher mFecha = fechaPattern.matcher(oracionTrim);
                if (mFecha.find()) {
                    objetivo = mFecha.group(); // el año encontrado
                    matchedReal = objetivo;
                }
            }

            // Si no encontramos nada para ocultar, seguimos con la sgte
            if (objetivo == null || matchedReal == null) {
                continue;
            }

            // 6.3 Construir la oración con hueco
            // reemplaza solo la primera coincidencia
            String oracionConHueco = oracionTrim.replaceFirst(
                    Pattern.quote(matchedReal),
                    "_____"
            );

            String preguntaTexto
                    = "Complete la frase según el documento:\n\n\""
                    + oracionConHueco + "\"";

            // 6.4 Armar el mapa de la pregunta
            Map<String, Object> p = new HashMap<>();
            p.put("tipo", "completar");                  // tipo de pregunta
            p.put("pregunta", preguntaTexto);
            p.put("respuesta_correcta", matchedReal.trim()); // exacto como estaba
            p.put("oracion_original", oracionTrim); // pa debug

            preguntas.add(p);
            usados.add(objetivo); // ya use esta palabra, no volver usarla

            // Limitar a 10 preguntas
            if (preguntas.size() == 10) {
                break; // ya listas las 10
            }
        }

        return preguntas; // devolvemos la lista
    }

}
