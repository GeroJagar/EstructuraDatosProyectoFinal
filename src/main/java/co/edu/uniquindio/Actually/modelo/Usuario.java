package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    protected String nombre;
    protected String contrasena;
    protected String id;
    protected String correo;

    // Constructor sin argumentos
    public Usuario() {
    }

    // Constructor con todos los parámetros
    public Usuario(String nombre, String contrasena, String id, String correo) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.id = id;
        this.correo = correo;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Método toString
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", id='" + id + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
