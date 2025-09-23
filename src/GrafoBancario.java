import java.util.*;

public class GrafoBancario {

    // Representación del grafo usando lista de adyacencia
    private Map<Integer, List<Conexion>> grafo;
    private Map<Integer, Cliente> clientes;

    // Clase para representar una conexión entre cuentas
    private static class Conexion {
        int clienteDestino;
        int montoTransferencia;
        String tipoRelacion; // "TRANSFERENCIA", "PRESTAMO", "FAMILIAR", etc.

        public Conexion(int clienteDestino, int montoTransferencia, String tipoRelacion) {
            this.clienteDestino = clienteDestino;
            this.montoTransferencia = montoTransferencia;
            this.tipoRelacion = tipoRelacion;
        }

        @Override
        public String toString() {
            return "-> Cliente " + clienteDestino + " ($" + montoTransferencia + " - " + tipoRelacion + ")";
        }
    }

    public GrafoBancario() {
        this.grafo = new HashMap<>();
        this.clientes = new HashMap<>();
    }

    // Agregar cliente al grafo
    public void agregarCliente(Cliente cliente) {
        clientes.put(cliente.ID, cliente);
        grafo.putIfAbsent(cliente.ID, new ArrayList<>());
    }

    // Agregar relación entre dos clientes (arista dirigida)
    public void agregarRelacion(int clienteOrigen, int clienteDestino, int monto, String tipoRelacion) {
        if (!grafo.containsKey(clienteOrigen)) {
            System.out.println("   ║ Error: Cliente origen " + clienteOrigen + " no existe");
            return;
        }

        if (!grafo.containsKey(clienteDestino)) {
            System.out.println("   ║ Error: Cliente destino " + clienteDestino + " no existe");
            return;
        }

        grafo.get(clienteOrigen).add(new Conexion(clienteDestino, monto, tipoRelacion));
        System.out.println("   ║ Relación agregada: Cliente " + clienteOrigen + " -> Cliente " + clienteDestino);
    }

    // Agregar relación bidireccional (para relaciones familiares, etc.)
    public void agregarRelacionBidireccional(int cliente1, int cliente2, int monto, String tipoRelacion) {
        agregarRelacion(cliente1, cliente2, monto, tipoRelacion);
        agregarRelacion(cliente2, cliente1, monto, tipoRelacion);
    }

    // Mostrar todas las conexiones del grafo
    public void mostrarGrafoCompleto() {
        System.out.println("   ║ GRAFO BANCARIO COMPLETO");
        System.out.println("   ║ ═══════════════════════════════════");

        if (grafo.isEmpty()) {
            System.out.println("   ║ No hay clientes en el grafo");
            return;
        }

        for (Map.Entry<Integer, List<Conexion>> entrada : grafo.entrySet()) {
            int clienteID = entrada.getKey();
            List<Conexion> conexiones = entrada.getValue();

            Cliente cliente = clientes.get(clienteID);
            String nombreCliente = (cliente != null) ? cliente.Nombre : "Desconocido";

            System.out.println("   ║ Cliente " + clienteID + " (" + nombreCliente + "):");

            if (conexiones.isEmpty()) {
                System.out.println("   ║   Sin conexiones");
            } else {
                for (Conexion conexion : conexiones) {
                    Cliente clienteDestino = clientes.get(conexion.clienteDestino);
                    String nombreDestino = (clienteDestino != null) ? clienteDestino.Nombre : "Desconocido";
                    System.out.println("   ║   " + conexion.toString() + " (" + nombreDestino + ")");
                }
            }
            System.out.println("   ║");
        }
    }

    // Buscar camino entre dos clientes usando BFS (Breadth-First Search)
    public void buscarCaminoBFS(int clienteOrigen, int clienteDestino) {
        System.out.println("   ║ BÚSQUEDA DE CAMINO (BFS)");
        System.out.println("   ║ Desde Cliente " + clienteOrigen + " hasta Cliente " + clienteDestino);
        System.out.println("   ║ ═══════════════════════════════════");

        if (!grafo.containsKey(clienteOrigen) || !grafo.containsKey(clienteDestino)) {
            System.out.println("   ║ Error: Uno o ambos clientes no existen");
            return;
        }

        Queue<Integer> cola = new Queue<>();
        Map<Integer, Integer> padre = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();

        cola.offer(clienteOrigen);
        visitados.add(clienteOrigen);
        padre.put(clienteOrigen, -1);

        boolean encontrado = false;

        while (!cola.isEmpty() && !encontrado) {
            int clienteActual = cola.poll();

            if (clienteActual == clienteDestino) {
                encontrado = true;
                break;
            }

            for (Conexion conexion : grafo.get(clienteActual)) {
                if (!visitados.contains(conexion.clienteDestino)) {
                    visitados.add(conexion.clienteDestino);
                    padre.put(conexion.clienteDestino, clienteActual);
                    cola.offer(conexion.clienteDestino);
                }
            }
        }

        if (encontrado) {
            System.out.println("   ║ Camino encontrado:");
            mostrarCamino(padre, clienteOrigen, clienteDestino);
        } else {
            System.out.println("   ║ No existe camino entre los clientes");
        }
    }

    // Buscar camino usando DFS (Depth-First Search)
    public void buscarCaminoDFS(int clienteOrigen, int clienteDestino) {
        System.out.println("   ║ BÚSQUEDA DE CAMINO (DFS)");
        System.out.println("   ║ Desde Cliente " + clienteOrigen + " hasta Cliente " + clienteDestino);
        System.out.println("   ║ ═══════════════════════════════════");

        if (!grafo.containsKey(clienteOrigen) || !grafo.containsKey(clienteDestino)) {
            System.out.println("   ║ Error: Uno o ambos clientes no existen");
            return;
        }

        Set<Integer> visitados = new HashSet<>();
        List<Integer> camino = new ArrayList<>();

        if (dfsRecursivo(clienteOrigen, clienteDestino, visitados, camino)) {
            System.out.println("   ║ Camino encontrado:");
            for (int i = 0; i < camino.size(); i++) {
                Cliente cliente = clientes.get(camino.get(i));
                String nombre = (cliente != null) ? cliente.Nombre : "Desconocido";
                System.out.print("   ║ Cliente " + camino.get(i) + " (" + nombre + ")");
                if (i < camino.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        } else {
            System.out.println("   ║ No existe camino entre los clientes");
        }
    }

    private boolean dfsRecursivo(int actual, int destino, Set<Integer> visitados, List<Integer> camino) {
        visitados.add(actual);
        camino.add(actual);

        if (actual == destino) {
            return true;
        }

        for (Conexion conexion : grafo.get(actual)) {
            if (!visitados.contains(conexion.clienteDestino)) {
                if (dfsRecursivo(conexion.clienteDestino, destino, visitados, camino)) {
                    return true;
                }
            }
        }

        camino.remove(camino.size() - 1); // Backtrack
        return false;
    }

    // Encontrar clientes más conectados (mayor número de conexiones salientes)
    public void encontrarClientesMasConectados() {
        System.out.println("   ║ CLIENTES MÁS CONECTADOS");
        System.out.println("   ║ ═══════════════════════════════════");

        if (grafo.isEmpty()) {
            System.out.println("   ║ No hay clientes en el grafo");
            return;
        }

        List<Map.Entry<Integer, Integer>> conexionesPorCliente = new ArrayList<>();

        for (Map.Entry<Integer, List<Conexion>> entrada : grafo.entrySet()) {
            int clienteID = entrada.getKey();
            int numConexiones = entrada.getValue().size();
            conexionesPorCliente.add(new AbstractMap.SimpleEntry<>(clienteID, numConexiones));
        }

        // Ordenar por número de conexiones (descendente)
        conexionesPorCliente.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(5, conexionesPorCliente.size()); i++) {
            int clienteID = conexionesPorCliente.get(i).getKey();
            int numConexiones = conexionesPorCliente.get(i).getValue();
            Cliente cliente = clientes.get(clienteID);
            String nombre = (cliente != null) ? cliente.Nombre : "Desconocido";

            System.out.println("   ║ " + (i + 1) + ". Cliente " + clienteID + " (" + nombre +
                    ") - " + numConexiones + " conexiones");
        }
    }

    // Analizar flujo de dinero en el grafo
    public void analizarFlujoDinero() {
        System.out.println("   ║ ANÁLISIS DE FLUJO DE DINERO");
        System.out.println("   ║ ═══════════════════════════════════");

        Map<Integer, Integer> dineroSaliente = new HashMap<>();
        Map<Integer, Integer> dineroEntrante = new HashMap<>();

        // Inicializar mapas
        for (int clienteID : grafo.keySet()) {
            dineroSaliente.put(clienteID, 0);
            dineroEntrante.put(clienteID, 0);
        }

        // Calcular flujos
        for (Map.Entry<Integer, List<Conexion>> entrada : grafo.entrySet()) {
            int clienteOrigen = entrada.getKey();

            for (Conexion conexion : entrada.getValue()) {
                // Sumar dinero saliente del cliente origen
                dineroSaliente.put(clienteOrigen,
                        dineroSaliente.get(clienteOrigen) + conexion.montoTransferencia);

                // Sumar dinero entrante al cliente destino
                dineroEntrante.put(conexion.clienteDestino,
                        dineroEntrante.getOrDefault(conexion.clienteDestino, 0) + conexion.montoTransferencia);
            }
        }

        System.out.println("   ║ FLUJO POR CLIENTE:");
        for (int clienteID : grafo.keySet()) {
            Cliente cliente = clientes.get(clienteID);
            String nombre = (cliente != null) ? cliente.Nombre : "Desconocido";
            int saliente = dineroSaliente.get(clienteID);
            int entrante = dineroEntrante.get(clienteID);
            int balance = entrante - saliente;

            System.out.println("   ║ Cliente " + clienteID + " (" + nombre + "):");
            System.out.println("   ║   Saliente: $" + saliente + " | Entrante: $" + entrante +
                    " | Balance: $" + balance);
        }
    }

    // Detectar ciclos en el grafo (usando DFS)
    public void detectarCiclos() {
        System.out.println("   ║ DETECCIÓN DE CICLOS");
        System.out.println("   ║ ═══════════════════════════════════");

        Set<Integer> visitados = new HashSet<>();
        Set<Integer> enProceso = new HashSet<>();
        boolean hayciclo = false;

        for (int clienteID : grafo.keySet()) {
            if (!visitados.contains(clienteID)) {
                if (detectarCicloRecursivo(clienteID, visitados, enProceso)) {
                    hayciclo = true;
                    break;
                }
            }
        }

        if (hayciclo) {
            System.out.println("   ║ Se detectaron ciclos en el grafo");
            System.out.println("   ║ Esto puede indicar transferencias circulares");
        } else {
            System.out.println("   ║ No se detectaron ciclos en el grafo");
        }
    }

    private boolean detectarCicloRecursivo(int cliente, Set<Integer> visitados, Set<Integer> enProceso) {
        visitados.add(cliente);
        enProceso.add(cliente);

        for (Conexion conexion : grafo.get(cliente)) {
            if (!visitados.contains(conexion.clienteDestino)) {
                if (detectarCicloRecursivo(conexion.clienteDestino, visitados, enProceso)) {
                    return true;
                }
            } else if (enProceso.contains(conexion.clienteDestino)) {
                return true; // Ciclo detectado
            }
        }

        enProceso.remove(cliente);
        return false;
    }

    // Mostrar estadísticas del grafo
    public void mostrarEstadisticasGrafo() {
        System.out.println("   ║ ESTADÍSTICAS DEL GRAFO");
        System.out.println("   ║ ═══════════════════════════════════");

        int totalClientes = grafo.size();
        int totalConexiones = 0;
        int montoTotalTransferencias = 0;

        for (List<Conexion> conexiones : grafo.values()) {
            totalConexiones += conexiones.size();
            for (Conexion conexion : conexiones) {
                montoTotalTransferencias += conexion.montoTransferencia;
            }
        }

        System.out.println("   ║ Total de clientes: " + totalClientes);
        System.out.println("   ║ Total de conexiones: " + totalConexiones);
        System.out.println("   ║ Monto total en transferencias: $" + montoTotalTransferencias);

        if (totalClientes > 0) {
            double densidad = (double) totalConexiones / (totalClientes * (totalClientes - 1));
            System.out.println("   ║ Densidad del grafo: " + String.format("%.2f%%", densidad * 100));
        }
    }

    // Método auxiliar para mostrar camino
    private void mostrarCamino(Map<Integer, Integer> padre, int origen, int destino) {
        List<Integer> camino = new ArrayList<>();
        int actual = destino;

        while (actual != -1) {
            camino.add(actual);
            actual = padre.get(actual);
        }

        Collections.reverse(camino);

        for (int i = 0; i < camino.size(); i++) {
            Cliente cliente = clientes.get(camino.get(i));
            String nombre = (cliente != null) ? cliente.Nombre : "Desconocido";
            System.out.print("   ║ Cliente " + camino.get(i) + " (" + nombre + ")");
            if (i < camino.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
