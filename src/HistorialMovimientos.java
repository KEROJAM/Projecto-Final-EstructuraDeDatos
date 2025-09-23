import java.util.Comparator;
import java.util.Stack;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalDateTime;

public class HistorialMovimientos {
    private Stack<Transaccion> movimientos;

    public HistorialMovimientos() {
        this.movimientos = new Stack<>();
    }

    public void add(String descripcion, int monto, String tipo, LocalDateTime fecha) {
        movimientos.push(new Transaccion(descripcion, monto, tipo, fecha));
    }

    public boolean isEmpty() {
        return movimientos.isEmpty();
    }

    public void mostrarHistorialCronologico() {
        if (isEmpty()) {
            System.out.println("No hay movimientos en el historial.");
            return;
        }

        System.out.println("\n--- Historial Cronológico (más reciente primero) ---");
        // Convertir la pila a una lista para iterar
        List<Transaccion> listaTemporal = new LinkedList<>(movimientos);

        // Imprimir en orden inverso para que sea cronológico
        for (int i = listaTemporal.size() - 1; i >= 0; i--) {
            Transaccion t = listaTemporal.get(i);
            System.out.printf("║ %s\n", t.toString());
        }
        System.out.println("-----------------------------------------------------");
    }

    public void mostrarHistorialPorMonto(boolean mayorAMenor) {
        if (isEmpty()) {
            System.out.println("No hay movimientos en el historial.");
            return;
        }

        BinaryTreeBancario tree = new BinaryTreeBancario();
        for (Transaccion transaccion : movimientos) {
            tree.insertarTransaccion(transaccion);
        }

        if (mayorAMenor) {
            System.out.println("\n--- Historial por Monto (de mayor a menor) ---");
            tree.mostrarTransaccionesInOrderReverso();
        } else {
            System.out.println("\n--- Historial por Monto (de menor a mayor) ---");
            tree.mostrarTransaccionesInOrder();
        }
        System.out.println("-----------------------------------------------------");
    }
}