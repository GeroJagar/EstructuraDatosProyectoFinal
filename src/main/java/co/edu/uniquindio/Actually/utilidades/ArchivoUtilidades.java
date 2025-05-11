package co.edu.uniquindio.Actually.utilidades;


import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import co.edu.uniquindio.Actually.modelo.TEMA;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

public class ArchivoUtilidades {

    public static ArrayList<String> leerArchivoScanner(String ruta) throws IOException {
        ArrayList<String> lista = new ArrayList<>();
        Scanner sc = new Scanner(new File(ruta));
        while (sc.hasNextLine()) {
            lista.add(sc.nextLine());
        }
        sc.close();
        return lista;
    }

    public static ArrayList<String> leerArchivoBufferedReader(String ruta) throws IOException {
        ArrayList<String> lista = new ArrayList<>();
        FileReader fr = new FileReader(ruta);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        while ((linea = br.readLine()) != null) {
            lista.add(linea);
        }
        br.close();
        fr.close();
        return lista;
    }

    public static void escribirArchivoFormatter(String ruta, List<String> lista) throws IOException {
        Formatter ft = new Formatter(ruta);
        for (String s : lista) {
            ft.format(s + "%n");
        }
        ft.close();
    }

    public static void escribirArchivoBufferedWriter(String ruta, List<String> lista, boolean concat) throws IOException {

        FileWriter fw = new FileWriter(ruta, concat);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String string : lista) {
            bw.write(string);
            bw.newLine();
        }
        bw.close();
        fw.close();
    }

    public static void serializarObjeto(String ruta, Object objeto) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ruta));
        os.writeObject(objeto);
        os.close();
    }

    public static Object deserializarObjeto(String ruta) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(ruta));
        Object objeto = is.readObject();
        is.close();

        return objeto;
    }

    public static void serializarObjetoXML(String ruta, Object objeto) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta));
        encoder.writeObject(objeto);
        encoder.close();
    }

    // Método para leer archivo .txt y crear un objeto ContenidoAcademico
    public ContenidoAcademico procesarArchivoTxt(File archivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(archivo));
        String titulo = null;
        String tema = null;
        String autor = null;
        StringBuilder contenido = new StringBuilder();

        String linea;
        while ((linea = reader.readLine()) != null) {
            // Extraer información del archivo
            if (linea.toLowerCase().startsWith("título:") || linea.toLowerCase().startsWith("titulo:")) {
                titulo = linea.substring(linea.indexOf(":") + 1).trim();
            } else if (linea.toLowerCase().startsWith("tema:")) {
                tema = linea.substring(linea.indexOf(":") + 1).trim();
            } else if (linea.toLowerCase().startsWith("autor:")) {
                autor = linea.substring(linea.indexOf(":") + 1).trim();
            } else {
                contenido.append(linea).append("\n");
            }
        }
        reader.close();

        // Validar si el título o tema es nulo o vacío
        if (titulo == null || titulo.isEmpty()) {
            throw new IOException("El título es obligatorio y no se encontró en el archivo.");
        }

        if (tema == null || tema.isEmpty()) {
            throw new IOException("El tema es obligatorio y no se encontró en el archivo.");
        }

        // Validar si el tema corresponde a un valor válido del enum TEMA
        TEMA temaEnum;
        try {
            temaEnum = TEMA.valueOf(tema.toUpperCase());  // Asegurarse que el tema esté en mayúsculas
        } catch (IllegalArgumentException e) {
            throw new IOException("El tema no es válido en el archivo: " + tema);
        }

        // Crear el objeto ContenidoAcademico
        return new ContenidoAcademico(titulo, temaEnum, autor, contenido.toString(), generarId());
    }


    // Método para generar un ID único para cada contenido (puedes usar UUID o una simple combinación)
    private String generarId() {
        return "contenido-" + System.currentTimeMillis();
    }

}