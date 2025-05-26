package co.edu.uniquindio.Actually.modelo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
Esta clase gestiona los chats entre estudiantes, asegurando que
exista un único chat para cada par de estudiantes.
Implementa el patrón Singleton para que haya una única instancia global.
 */
public class GestorChats implements Serializable {
    // Instancia única de GestorChats (Singleton)
    private static GestorChats instance;

    // Mapa que almacena los chats, donde la clave es una cadena que identifica la pareja de estudiantes
    private Map<String, Chat> chats;

    // Constructor privado para impedir instanciación externa
    private GestorChats() {
        chats = new HashMap<>();
    }

    /**
     * Devuelve la instancia única de GestorChats.
     * Si no existe, la crea.
     */
    public static GestorChats getInstance() {
        if (instance == null) {
            instance = new GestorChats();
        }
        return instance;
    }

    /**
     * Obtiene el chat entre dos estudiantes.
     * Si no existe, lo crea y lo almacena.
     *
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     * @return Chat correspondiente a esos dos estudiantes
     */
    public Chat obtenerChat(Estudiante e1, Estudiante e2) {
        String key = generarClave(e1, e2);

        // Si no existe el chat con esa clave, se crea uno nuevo y se guarda
        if (!chats.containsKey(key)) {
            chats.put(key, new Chat(e1, e2));
        }

        return chats.get(key);
    }

    /**
     * Genera una clave única para identificar el chat entre dos estudiantes.
     * La clave se genera ordenando los IDs para evitar duplicados (e1_e2 o e2_e1).
     *
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     * @return Clave única para identificar el chat
     */
    private String generarClave(Estudiante e1, Estudiante e2) {
        String id1 = e1.getId(); // o getUsername(), según el modelo de Estudiante
        String id2 = e2.getId();

        // Ordena los IDs para asegurar que la clave sea la misma independientemente del orden de los estudiantes
        return (id1.compareTo(id2) < 0) ? id1 + "_" + id2 : id2 + "_" + id1;
    }

    /**
     * Establece el mapa de chats con un conjunto de chats cargados.
     * Si el parámetro es null, se inicializa un mapa vacío.
     *
     * @param chatsCargados Mapa de chats cargados
     */
    public void setChats(Map<String, Chat> chatsCargados) {
        if (chatsCargados != null) {
            this.chats = chatsCargados;
        } else {
            this.chats = new HashMap<>();
        }
    }

    /**
     * Guarda todos los chats en un archivo mediante serialización.
     *
     * @param rutaArchivo Ruta del archivo donde se guardarán los chats
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    public void guardarChats(String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(chats);
        }
    }

    /**
     * Carga los chats desde un archivo mediante deserialización.
     *
     * @param rutaArchivo Ruta del archivo desde donde se cargarán los chats
     * @throws IOException Si ocurre un error al leer el archivo
     * @throws ClassNotFoundException Si no se encuentra la clase Chat durante la deserialización
     */
    public void cargarChats(String rutaArchivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Map<String, Chat> chatsCargados = (Map<String, Chat>) ois.readObject();
            setChats(chatsCargados);
        }
    }

    /**
     * Devuelve el mapa completo de chats.
     *
     * @return Mapa con todos los chats existentes
     */
    public Map<String, Chat> getChats() {
        return chats;
    }

}
