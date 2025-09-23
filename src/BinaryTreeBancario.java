public class BinaryTreeBancario {

    private static class NodoTransaccion {
        Transaccion transaccion;
        NodoTransaccion izquierda;
        NodoTransaccion derecha;

        public NodoTransaccion(Transaccion transaccion) {
            this.transaccion = transaccion;
            this.izquierda = null;
            this.derecha = null;
        }
    }

    private NodoTransaccion root;

    public BinaryTreeBancario() {
        this.root = null;
    }

    public void insertarTransaccion(Transaccion transaccion) {
        root = insertarTransaccionRecursivo(root, transaccion);
    }

    private NodoTransaccion insertarTransaccionRecursivo(NodoTransaccion nodo, Transaccion transaccion) {
        if (nodo == null) {
            return new NodoTransaccion(transaccion);
        }

        if (transaccion.getMonto() < nodo.transaccion.getMonto()) {
            nodo.izquierda = insertarTransaccionRecursivo(nodo.izquierda, transaccion);
        } else {
            nodo.derecha = insertarTransaccionRecursivo(nodo.derecha, transaccion);
        }
        return nodo;
    }

    public void mostrarTransaccionesInOrder() {
        mostrarInOrder(root);
    }

    private void mostrarInOrder(NodoTransaccion nodo) {
        if (nodo != null) {
            mostrarInOrder(nodo.izquierda);
            System.out.println("║ " + nodo.transaccion);
            mostrarInOrder(nodo.derecha);
        }
    }

    public void mostrarTransaccionesInOrderReverso() {
        mostrarInOrderReverso(root);
    }

    private void mostrarInOrderReverso(NodoTransaccion nodo) {
        if (nodo != null) {
            mostrarInOrderReverso(nodo.derecha);
            System.out.println("║ " + nodo.transaccion);
            mostrarInOrderReverso(nodo.izquierda);
        }
    }
}