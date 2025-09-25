public class PriorityQueueBancaria {
    private PriorityNode<String>[] data;
    private int size;
    private int capacity;
    private static int transaccionCounter = 1;

    @SuppressWarnings("unchecked")
    public PriorityQueueBancaria() {
        capacity = 10;
        data = new PriorityNode[capacity + 1]; // Index 0 is unused
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Agregar transacción bancaria con prioridad
    public void agregarTransaccion(String tipo, float monto, String cliente, int prioridad) {
        String transaccion = String.format("ID:%d %s: $%f - Cliente: %s",
                transaccionCounter++, tipo, monto, cliente);
        push(prioridad, transaccion);
    }

    private void push(int priority, String value) {
        if (size >= capacity) {
            resize();
        }

        PriorityNode<String> nodeToInsert = new PriorityNode<>(priority, value);
        size++;
        data[size] = nodeToInsert;

        // Heapify up - prioridades más bajas tienen mayor prioridad (1 > 2 > 3)
        int currentPosition = size;
        while (currentPosition > 1) {
            int parentPosition = currentPosition / 2;
            if (data[parentPosition].getPriority() > data[currentPosition].getPriority()) {
                swap(parentPosition, currentPosition);
                currentPosition = parentPosition;
            } else {
                break;
            }
        }
    }

    // Procesar siguiente transacción de mayor prioridad
    public String procesarSiguienteTransaccion() {
        if (isEmpty()) {
            return null;
        }

        PriorityNode<String> result = data[1];
        data[1] = data[size];
        data[size] = null;
        size--;

        // Heapify down
        int currentPosition = 1;
        while (currentPosition * 2 <= size) {
            int leftChild = currentPosition * 2;
            int rightChild = currentPosition * 2 + 1;
            int smallestChild = leftChild;

            if (rightChild <= size && data[rightChild].getPriority() < data[leftChild].getPriority()) {
                smallestChild = rightChild;
            }

            if (data[currentPosition].getPriority() > data[smallestChild].getPriority()) {
                swap(currentPosition, smallestChild);
                currentPosition = smallestChild;
            } else {
                break;
            }
        }

        return result.getData();
    }

    // Ver próxima transacción sin procesarla
    public String verProximaTransaccion() {
        if (isEmpty()) return null;
        return data[1].getData();
    }

    // Mostrar todas las transacciones pendientes
    public void mostrarTransaccionesPendientes() {
        if (isEmpty()) {
            System.out.println("   ║ No hay transacciones pendientes");
            return;
        }

        System.out.println("   ║ TRANSACCIONES PENDIENTES (" + size + "):");
        System.out.println("   ║ ═══════════════════════════════════════");

        for (int i = 1; i <= size; i++) {
            if (data[i] != null) {
                String prioridadTexto = getPrioridadTexto(data[i].getPriority());
                System.out.println("   ║ " + i + ". [" + prioridadTexto + "] " + data[i].getData());
            }
        }
    }

    // Procesar todas las transacciones por orden de prioridad
    public void procesarTodasLasTransacciones() {
        if (isEmpty()) {
            System.out.println("   ║ No hay transacciones para procesar");
            return;
        }

        System.out.println("   ║ PROCESANDO TRANSACCIONES POR PRIORIDAD:");
        System.out.println("   ║ ═══════════════════════════════════════");

        int contador = 1;
        while (!isEmpty()) {
            String transaccion = procesarSiguienteTransaccion();
            System.out.println("   ║ " + contador + ". PROCESADA: " + transaccion);
            contador++;
        }
    }

    // Obtener estadísticas de transacciones pendientes
    public void mostrarEstadisticas() {
        if (isEmpty()) {
            System.out.println("   ║ No hay transacciones para analizar");
            return;
        }

        int[] contadorPrioridad = new int[4]; // índices 1, 2, 3 para prioridades
        int montoTotal = 0;

        for (int i = 1; i <= size; i++) {
            if (data[i] != null) {
                contadorPrioridad[data[i].getPriority()]++;
                // Extraer monto de la cadena (formato: "ID:X TIPO: $MONTO - Cliente: NOMBRE")
                String transaccion = data[i].getData();
                try {
                    int inicioMonto = transaccion.indexOf("$") + 1;
                    int finMonto = transaccion.indexOf(" - Cliente:");
                    if (inicioMonto > 0 && finMonto > inicioMonto) {
                        String montoStr = transaccion.substring(inicioMonto, finMonto);
                        montoTotal += Integer.parseInt(montoStr);
                    }
                } catch (Exception e) {
                    // Ignorar errores de parsing
                }
            }
        }

        System.out.println("   ║ ESTADÍSTICAS DE TRANSACCIONES");
        System.out.println("   ║ ═══════════════════════════════");
        System.out.println("   ║ Total pendientes: " + size);
        System.out.println("   ║ Monto total: $" + montoTotal);
        System.out.println("   ║ Prioridad ALTA (1): " + contadorPrioridad[1]);
        System.out.println("   ║ Prioridad MEDIA (2): " + contadorPrioridad[2]);
        System.out.println("   ║ Prioridad BAJA (3): " + contadorPrioridad[3]);
    }

    private void swap(int i, int j) {
        PriorityNode<String> temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        PriorityNode<String>[] newData = new PriorityNode[capacity + 1];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        data = new PriorityNode[capacity + 1];
        size = 0;
    }

    public int getSize() {
        return size;
    }

    private String getPrioridadTexto(int prioridad) {
        switch(prioridad) {
            case 1: return "ALTA";
            case 2: return "MEDIA";
            case 3: return "BAJA";
            default: return "DESCONOCIDA";
        }
    }
}