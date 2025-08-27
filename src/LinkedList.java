
public class LinkedList {
    Node firstNode;
    int ListSize;

    public LinkedList() {
        this.firstNode = null;
        this.ListSize = 0;
    }

    /**
     * Crea una lista con un primer nodo dado.
     * @param firstNode nodo inicial
     */
    public LinkedList(Node firstNode) {
        this.firstNode = firstNode;
        this.ListSize = 1;
    }


    /**
     * Imprime el contenido de la lista en orden directo ("-->") o inverso ("<--").
     * @param FlowList cadena que indica el sentido de impresión: "-->" o "<--"
     */
    public void printList( String FlowList) {
        switch (FlowList) {
            case "-->":
                if (firstNode == null) {
                    System.out.println("El historial de movimientos esta vacio");
                } else {
                    Node currNode = this.firstNode;
                    while (currNode != null) {
                        System.out.print(currNode.Data);
                        if (currNode.next != null) {
                            System.out.print(" " + FlowList + " ");
                        }
                        currNode = currNode.next;
                    }
                    System.out.println();
                }
                break;
            case "<--":
                if (firstNode == null) {
                    System.out.println("El historial de movimientos esta vacio");
                    return;
                }
                Node temp = this.firstNode.tail;
                if (temp == null) {
                    // Fallback: find the tail manually
                    temp = this.firstNode;
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                }
                
                while (temp != null) {
                    System.out.print(temp.Data);
                    if (temp.last != null) {
                        System.out.print(" " + FlowList + " ");
                    }
                    temp = temp.last;
                }
                System.out.println();
                break;
        }
    }

    /**
     * Establece el primer nodo (cabeza) de la lista.
     * @param firstNode nuevo primer nodo
     */
    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }
}
