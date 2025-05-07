package co.edu.uniquindio.Actually.utilidades;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileUploader {

    // Método para abrir el FileChooser
    public File abrirSelectorDeArchivo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        // Agregar más filtros según los tipos de archivos que quieras permitir (PDF, PPTX, etc.)
        return fileChooser.showOpenDialog(stage);
    }
}

