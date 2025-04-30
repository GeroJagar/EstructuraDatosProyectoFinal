package co.edu.uniquindio.Actually.utilidades;


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

}