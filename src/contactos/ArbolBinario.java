package contactos;

import java.io.BufferedReader;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArbolBinario {

    private Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    public boolean insertarNodo(Nodo nNuevo) {
        if (raiz == null) {
            raiz = nNuevo;
            return true;
        } else {
            return insertar(raiz, nNuevo);
        }
    }

    private boolean insertar(Nodo n, Nodo nNuevo) {
        if (nNuevo.nombre.toUpperCase().compareTo(n.nombre.toUpperCase()) < 0) {
            //está disoponible?
            if (n.izquierdo == null) {
                n.izquierdo = nNuevo;
                return true;
            } else {
                return insertar(n.izquierdo, nNuevo);
            }
        } else if (nNuevo.nombre.toUpperCase().compareTo(n.nombre.toUpperCase()) > 0) {
            //está disoponible?
            if (n.derecho == null) {
                n.derecho = nNuevo;
                return true;
            } else {
                return insertar(n.derecho, nNuevo);
            }
        } else {
            return false;
        }
    }

    public void desdeArchivo(String nombreArchivo) {
        raiz = null;
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        try {
            String linea = br.readLine();
            while (linea != null) {
                String[] textos = linea.split(";");
                Nodo n = new Nodo(
                        textos[0],
                        textos[1],
                        textos[2],
                        textos[3],
                        textos[4]);
                insertarNodo(n);
                linea = br.readLine();
            }
        } catch (Exception ex) {

        }
    }

    public int obtenerLongitud() {
        return obtenerLongitud(raiz);
    }

    private int obtenerLongitud(Nodo n) {
        if (n == null) {
            return 0;
        } else {
            return obtenerLongitud(n.izquierdo) + 1 + obtenerLongitud(n.derecho);
        }
    }

    public void mostrar(JTable tbl) {
        int filas = obtenerLongitud();
        String[] encabezados = {"Nombre", "Telefono", "Celular", "Direccion", "Correo"};
        String[][] datos = new String[filas][encabezados.length];
        mostrarInOrden(raiz, datos, -1);
        
        DefaultTableModel dtm=new DefaultTableModel(datos, encabezados);
        
        tbl.setModel(dtm);
    }

    private int mostrarInOrden(Nodo n, String[][] datos, int fila){
        if(n!=null){
            fila = mostrarInOrden(n.izquierdo, datos, fila);
            fila++;
            datos[fila][0]=n.nombre;
            datos[fila][1]=n.telefono;
            datos[fila][2]=n.movil;
            datos[fila][3]=n.direccion;
            datos[fila][4]=n.correo;
            fila = mostrarInOrden(n.derecho, datos, fila);
        }
        return fila;
    }
    
}
