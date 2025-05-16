package co.edu.uniquindio.Actually.utilidades;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ArchivoUtilidades {

    // Métodos de serialización (necesarios para persistencia)
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

    // Métodos básicos para manejo de archivos
    public String leerArchivoComoTexto(File archivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        return contenido.toString();
    }

    public void copiarArchivo(File origen, File destino) throws IOException {
        Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static String generarId() {
        return "contenido-" + System.currentTimeMillis();
    }
}