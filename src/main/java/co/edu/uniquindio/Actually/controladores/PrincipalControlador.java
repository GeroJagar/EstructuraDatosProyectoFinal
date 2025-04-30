package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class PrincipalControlador {

    private final Actually actually = Actually.getInstance();

    @FXML
    public void salirAlLogin(ActionEvent event) {
        actually.loadStage("/ventanas/login.fxml", event);
    }

}
