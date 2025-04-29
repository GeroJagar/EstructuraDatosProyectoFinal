package org.proyectofinal.backend.model;

public class Actually {

    private static Actually actually;

    public static Actually getInstance() {
        if (actually == null) {
            actually = new Actually();
        }
        return actually;
    }

    public void initialize() {

    }
}
