module org.proyectofinal.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens org.proyectofinal.frontend to javafx.fxml;
    exports org.proyectofinal.frontend;
}