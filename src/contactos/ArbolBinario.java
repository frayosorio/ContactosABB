package contactos;

import java.io.BufferedReader;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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

    private Nodo nodoBuscado;

    //Metodo para obtener el nodo en una posicion determinada
    private int buscarInOrden(Nodo n, int fila, int posicion) {
        if (n != null) {
            fila = buscarInOrden(n.izquierdo, fila, posicion);
            fila++;
            if (fila == posicion) {
                nodoBuscado = n;
                return fila;
            }
            fila = buscarInOrden(n.derecho, fila, posicion);
        }
        return fila;
    }

    public void actualizar(int fila,
            String nombre,
            String telefono,
            String movil,
            String direccion,
            String correo
    ) {
        buscarInOrden(raiz, -1, fila);
        Nodo n = nodoBuscado;
        //hubo cambio en el nombre?
        if (n.nombre.equals(nombre)) {
            n.asignar(nombre, telefono, movil, direccion, correo);
        } else {
            eliminarNodo(n);
            n = new Nodo(
                    nombre,
                    telefono,
                    movil,
                    direccion,
                    correo);
            insertarNodo(n);
        }

    }

    //Obteneer el nodo más a la izquierda del subarbol derecho
    private Nodo obtenerNodoSucesor(Nodo n) {
        n = n.derecho;
        while (n.izquierdo != null) {
            n = n.izquierdo;
        }
        return n;
    }

    public void eliminarNodo(Nodo n) {
        Nodo apuntador = raiz;
        Nodo padre = null;
        //Buscar el nodo padre del nodo a eliminar
        boolean encontrado = false;
        while (apuntador != null && !encontrado) {
            if (apuntador == n) {
                encontrado = true;
            } else {
                padre = apuntador;
                if (n.nombre.compareTo(apuntador.nombre) < 0) {
                    apuntador = apuntador.izquierdo;
                } else {
                    apuntador = apuntador.derecho;
                }
            }
        }
        if (encontrado) {
            //es un nodo hoja?
            if (apuntador.izquierdo == null && apuntador.derecho == null) {
                //es la raiz?
                if (padre == null && apuntador == raiz) {
                    raiz = null;
                } else {
                    if (padre.izquierdo == apuntador) {
                        padre.izquierdo = null;
                    } else if (padre.derecho == apuntador) {
                        padre.derecho = null;
                    }
                }
            } //es un nodo con hijo izquierdo
            else if (apuntador.izquierdo != null && apuntador.derecho == null) {
                //es la raiz?
                if (padre == null && apuntador == raiz) {
                    raiz = apuntador.izquierdo;
                } else {
                    if (padre.izquierdo == apuntador) {
                        padre.izquierdo = apuntador.izquierdo;
                    } else if (padre.derecho == apuntador) {
                        padre.derecho = apuntador.izquierdo;
                    }
                }
            }//es un nodo con hijo derecho
            else if (apuntador.izquierdo == null && apuntador.derecho != null) {
                //es la raiz?
                if (padre == null && apuntador == raiz) {
                    raiz = apuntador.derecho;
                } else {
                    if (padre.izquierdo == apuntador) {
                        padre.izquierdo = apuntador.derecho;
                    } else if (padre.derecho == apuntador) {
                        padre.derecho = apuntador.derecho;
                    }
                }
            } //es un nodo con 2 hijoz
            else if (apuntador.izquierdo != null && apuntador.derecho != null) {
                //buscar el nodo sucesor
                n = obtenerNodoSucesor(apuntador);
                eliminarNodo(n);
                apuntador.asignar(n.nombre, n.telefono, n.movil, n.direccion, n.correo);
            }
        }
    }

    public void mostrar(JTable tbl) {
        int filas = obtenerLongitud();
        String[] encabezados = {"Nombre", "Telefono", "Celular", "Direccion", "Correo"};
        String[][] datos = new String[filas][encabezados.length];
        mostrarInOrden(raiz, datos, -1);

        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);

        //Inlcuir un evento cuando hayan cambios
        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                //obtener fila actualizada
                int fila = e.getFirstRow();
                //obtener el modelo de datos
                DefaultTableModel dtm = (DefaultTableModel) e.getSource();
                //actualizar el arbol
                actualizar(fila,
                        (String) dtm.getValueAt(fila, 0),
                        (String) dtm.getValueAt(fila, 1),
                        (String) dtm.getValueAt(fila, 2),
                        (String) dtm.getValueAt(fila, 3),
                        (String) dtm.getValueAt(fila, 4)
                );
                mostrar(tbl);
            }
        });

        tbl.setModel(dtm);
    }

    private int mostrarInOrden(Nodo n, String[][] datos, int fila) {
        if (n != null) {
            fila = mostrarInOrden(n.izquierdo, datos, fila);
            fila++;
            datos[fila][0] = n.nombre;
            datos[fila][1] = n.telefono;
            datos[fila][2] = n.movil;
            datos[fila][3] = n.direccion;
            datos[fila][4] = n.correo;
            fila = mostrarInOrden(n.derecho, datos, fila);
        }
        return fila;
    }
    
    //Obtener el nodo ubicado en una posición en INORDEN
    public Nodo obtenerNodoPosicion(int posicion) {
        nodoBuscado = null;
        buscarInOrden(raiz, -1, posicion);
        return nodoBuscado;
    }

}
