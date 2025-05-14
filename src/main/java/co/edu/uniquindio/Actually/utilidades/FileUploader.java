package co.edu.uniquindio.Actually.utilidades;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileUploader {

    public File abrirSelectorDeArchivo(Stage stage, String tipo) {
        FileChooser fileChooser = new FileChooser();

        switch(tipo.toLowerCase()) {
            case "texto":
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
                break;
            case "pdf":
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
                break;
            case "video":
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Videos MP4", "*.mp4"));
                break;
            default:
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        }

        return fileChooser.showOpenDialog(stage);
    }
}