package contactos;

public class Nodo {

    //datos a almacenar
    String nombre;
    String telefono;
    String movil;
    String direccion;
    String correo;
    //enlaces
    Nodo izquierdo;
    Nodo derecho;

    public Nodo(String nombre,
            String telefono,
            String movil,
            String direccion,
            String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.movil = movil;
        this.direccion = direccion;
        this.correo = correo;
    }

    public void asignar(String nombre,
            String telefono,
            String movil,
            String direccion,
            String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.movil = movil;
        this.direccion = direccion;
        this.correo = correo;
    }

}
