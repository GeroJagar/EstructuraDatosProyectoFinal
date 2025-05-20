package co.edu.uniquindio.Actually.modelo;

public enum TEMA {
    MATEMATICAS("Matematicas"),
    PROGRAMACION("Programacion"),
    FISICA("Fisica"),
    QUIMICA("Quimica"),
    BIOLOGIA("Biologia"),
    HISTORIA("Historia"),
    LENGUAJE("Lenguaje"),
    FILOSOFIA("Filosofia");

    private String name;

    TEMA(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
