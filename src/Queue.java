import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Queue<E> {
    public LinkedList transferencias;
    private int size;

    public Queue() {
        this.transferencias = new LinkedList();
        this.size = 0;
    }

    /**
     * Indica si la cola está vacía.
     * @return true si no hay elementos, false en caso contrario
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Devuelve el número de elementos en la cola.
     * @return tamaño de la cola
     */
    public int size() {
        return this.size;
    }

    /**
     * Inserta un nuevo elemento al final de la cola. El dato se lee de la entrada estándar
     * y se almacena como String dentro de un {@link Node}.
     * @throws IOException si ocurre un error al leer desde la entrada
     */
    public void enqueue(float Monto) throws IOException {
        Node newNode = new Node(Monto);

        if (this.transferencias.firstNode == null) {
            this.transferencias.firstNode = newNode;
            this.transferencias.firstNode.setTail(newNode);
        } else {
            // Find the current tail (last element)
            Node currentTail = this.transferencias.firstNode.tail;
            if (currentTail == null) {
                // Fallback: traverse to find tail
                currentTail = this.transferencias.firstNode;
                while (currentTail.next != null) {
                    currentTail = currentTail.next;
                }
            }

            // Add new node at the end
            currentTail.setNext(newNode);
            newNode.setLast(currentTail);
            this.transferencias.firstNode.setTail(newNode);
        }
        this.size++;
    }

    /**
     * Remueve y devuelve el primer elemento de la cola.
     * @return representación en String del elemento removido
     * @throws Exception si la cola está vacía
     */
    public String dequeue() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("La Cola esta vacia");
        }

        String result = this.transferencias.firstNode.Data.toString();

        Node oldFirst = this.transferencias.firstNode;
        Node newFirst = oldFirst.next;

        if (newFirst == null) {
            this.transferencias.firstNode = null;
        } else {
            oldFirst.setNext(null);
            oldFirst.setHead(null);
            newFirst.setLast(null);
            newFirst.setHead(newFirst);
            newFirst.setTail(oldFirst.tail != null ? oldFirst.tail : newFirst.tail);
            this.transferencias.firstNode = newFirst;
        }

        this.size--;
        return result;
    }

    /**
     * Obtiene el primer elemento de la cola sin removerlo.
     * @return representación en String del primer elemento
     * @throws Exception si la cola está vacía
     */
    public String peek() throws Exception{
        if (this.isEmpty()){
            throw new Exception("La Cola esta vacia");
        }
        return this.transferencias.firstNode.getData().toString();
    }

    /**
     * Muestra todos los elementos de la cola con su posición.
     * @throws Exception si la cola está vacía
     */
    public void show() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("La Cola esta Vacia");
        }

        System.out.println("Procesos en la cola:");
        Node current = this.transferencias.firstNode;
        int position = 1;

        while (current != null) {
            System.out.println("Proceso " + position + ": " + current.Data);
            current = current.next;
            position++;
        }
    }

    /**
     * Devuelve el conteo de procesos en la cola.
     * @return número de procesos
     */
    public int getProcessCount() {
        return this.size;
    }

    /**
     * Elimina todos los elementos de la cola y reinicia su tamaño a 0.
     */
    public void clear() {
        this.transferencias.firstNode = null;
        this.size = 0;
    }

    public void offer(E clienteOrigen) {
    }

    public E poll() {
        return null;
    }
}