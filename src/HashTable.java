// Archivo: HashTable.java (simplificado)
import java.util.LinkedList;
import java.util.List;

public class HashTable {
    private LinkedList<Cliente>[] table;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(String tarjeta, Cliente cliente) {
        int index = hash(tarjeta);
        LinkedList<Cliente> list = table[index];

        // Verificar si la tarjeta ya existe
        for (Cliente c : list) {
            if (c.getNumeroTarjeta().equals(tarjeta)) {
                return; // Ya existe, no hacer nada
            }
        }

        list.add(cliente);
        size++;
    }

    public Cliente get(String tarjeta) {
        int index = hash(tarjeta);
        LinkedList<Cliente> list = table[index];

        for (Cliente cliente : list) {
            if (cliente.getNumeroTarjeta().equals(tarjeta)) {
                return cliente;
            }
        }

        return null;
    }

    public boolean contains(String tarjeta) {
        return get(tarjeta) != null;
    }

    public int size() {
        return size;
    }

    /**
     * Obtiene todos los clientes almacenados en la tabla hash
     * @return Lista con todos los clientes
     */
    public List<Cliente> getAllValues() {
        List<Cliente> allClients = new java.util.ArrayList<>();
        for (LinkedList<Cliente> list : table) {
            allClients.addAll(list);
        }
        return allClients;
    }
}
