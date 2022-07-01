package contactos;

import java.io.BufferedReader;

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
}
