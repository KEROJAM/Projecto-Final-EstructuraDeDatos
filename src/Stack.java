import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementación de una pila (LIFO) genérica basada en {@link LinkedList}.
 * Permite apilar comandos con {@link #push()}, desapilar con {@link #pop()} y consultar con {@link #peek()}.
 */
public class Stack<E> {
    public LinkedList historial;
    private int size;
    
    public Stack(){
        this.historial = new LinkedList();
        this.size = 0;
    }

    /**
     * Indica si la pila está vacía.
     * @return true si no hay elementos
     */
    public boolean isEmpty(){
        return (this.size == 0);
    }

    /**
     * Devuelve el número de elementos en la pila.
     * @return tamaño de la pila
     */
    public int size(){
        return (this.size);
    }

    /**
     * Inserta (empuja) un nuevo elemento al tope de la pila.
     * El dato se lee de la entrada estándar y se guarda como String en un {@link Node}.
     * @throws IOException si falla la lectura de la entrada
     */
    public void push(String Data) throws IOException {
        Node newNode = new Node(Data);

        if (this.historial.firstNode == null) {
            // First element
            this.historial.firstNode = newNode;
            this.historial.firstNode.setTail(newNode);
        } else {
            // Find the current tail (last element)
            Node currentTail = this.historial.firstNode.tail;
            if (currentTail == null) {
                // Fallback: traverse to find tail
                currentTail = this.historial.firstNode;
                while (currentTail.next != null) {
                    currentTail = currentTail.next;
                }
            }

            // Add new node at the end (for stack, this becomes the new top)
            currentTail.setNext(newNode);
            newNode.setLast(currentTail);
            this.historial.firstNode.setTail(newNode);
        }
        this.size++;
    }

    /**
     * Elimina (desapila) el último elemento insertado (tope) y devuelve un mensaje.
     * Nota: devuelve un mensaje genérico de ejecución, no el dato eliminado.
     * @return mensaje de confirmación de ejecución del comando
     * @throws Exception si la pila está vacía
     */
    public String pop() throws Exception{
        if (this.isEmpty()){
            throw new Exception("La Pila esta Vacia");
        }
        // El stack debe eliminar el último elemento insertado (tail)
        Node tail = this.historial.firstNode.tail;
        // Fallback si por alguna razón no está seteada la cola
        if (tail == null) {
            tail = this.historial.firstNode;
            while (tail.next != null) {
                tail = tail.next;
            }
        }

        if (tail == this.historial.firstNode) {
            this.historial.setFirstNode(null);
        } else {
            Node prev = tail.last;
            if (prev != null) {
                prev.setNext(null);
            }
            this.historial.firstNode.setTail(prev);
            tail.setLast(null);
            tail.setNext(null);
        }

        this.size--;
        return "| Comando Ejecutado!           |";
    }

    /**
     * Obtiene el primer elemento de la estructura subyacente sin eliminar.
     * Nota: en esta implementación devuelve el dato del primer nodo, no del tope real.
     * @return representación en String del elemento
     * @throws Exception si la pila está vacía
     */
    public String peek() throws Exception{
        if (this.isEmpty()){
            throw new Exception("La Pila esta vacia");
        }
        return this.historial.firstNode.getData().toString();
    }
}
