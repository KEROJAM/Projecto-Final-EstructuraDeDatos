public class BinaryTreeBancario {
    
    // Nodo del árbol binario
    private static class NodoCliente {
        Cliente cliente;
        String sucursal;
        NodoCliente left;
        NodoCliente right;
        
        public NodoCliente(Cliente cliente, String sucursal) {
            this.cliente = cliente;
            this.sucursal = sucursal;
            this.left = null;
            this.right = null;
        }
    }
    
    private NodoCliente root;
    private int totalClientes;
    
    public BinaryTreeBancario() {
        this.root = null;
        this.totalClientes = 0;
    }
    
    // Insertar cliente en el árbol ordenado por ID
    public void insertarCliente(Cliente cliente, String sucursal) {
        root = insertarRecursivo(root, cliente, sucursal);
        totalClientes++;
    }
    
    private NodoCliente insertarRecursivo(NodoCliente nodo, Cliente cliente, String sucursal) {
        // Caso base: si el nodo es null, crear nuevo nodo
        if (nodo == null) {
            return new NodoCliente(cliente, sucursal);
        }
        
        // Insertar recursivamente basado en el ID del cliente
        if (cliente.ID < nodo.cliente.ID) {
            nodo.left = insertarRecursivo(nodo.left, cliente, sucursal);
        } else if (cliente.ID > nodo.cliente.ID) {
            nodo.right = insertarRecursivo(nodo.right, cliente, sucursal);
        }
        // Si el ID es igual, no insertar (evitar duplicados)
        
        return nodo;
    }
    
    // Buscar cliente por ID usando búsqueda binaria
    public Cliente buscarClientePorID(int id) {
        NodoCliente nodo = buscarRecursivo(root, id);
        return nodo != null ? nodo.cliente : null;
    }
    
    private NodoCliente buscarRecursivo(NodoCliente nodo, int id) {
        // Caso base: nodo no encontrado o encontrado
        if (nodo == null || nodo.cliente.ID == id) {
            return nodo;
        }
        
        // Buscar recursivamente en el subárbol apropiado
        if (id < nodo.cliente.ID) {
            return buscarRecursivo(nodo.left, id);
        } else {
            return buscarRecursivo(nodo.right, id);
        }
    }
    
    // Buscar clientes por sucursal
    public void buscarClientesPorSucursal(String sucursal) {
        System.out.println("   ║ CLIENTES EN SUCURSAL: " + sucursal.toUpperCase());
        System.out.println("   ║ ═══════════════════════════════════════");
        int[] contador = {1}; // Array para pasar por referencia
        buscarPorSucursalRecursivo(root, sucursal, contador);
        if (contador[0] == 1) {
            System.out.println("   ║ No se encontraron clientes en esta sucursal");
        }
    }
    
    private void buscarPorSucursalRecursivo(NodoCliente nodo, String sucursal, int[] contador) {
        if (nodo == null) {
            return;
        }
        
        // Recorrer todo el árbol (in-order traversal)
        buscarPorSucursalRecursivo(nodo.left, sucursal, contador);
        
        if (nodo.sucursal.equalsIgnoreCase(sucursal)) {
            System.out.println("   ║ " + contador[0] + ". ID:" + nodo.cliente.ID + 
                             " - " + nodo.cliente.Nombre + " - Saldo: $" + nodo.cliente.Monto);
            contador[0]++;
        }
        
        buscarPorSucursalRecursivo(nodo.right, sucursal, contador);
    }
    
    // Mostrar todos los clientes ordenados por ID (in-order traversal)
    public void mostrarTodosLosClientes() {
        System.out.println("   ║ TODOS LOS CLIENTES (ordenados por ID)");
        System.out.println("   ║ ═══════════════════════════════════════");
        if (root == null) {
            System.out.println("   ║ No hay clientes registrados");
            return;
        }
        int[] contador = {1};
        mostrarInOrder(root, contador);
    }
    
    private void mostrarInOrder(NodoCliente nodo, int[] contador) {
        if (nodo == null) {
            return;
        }
        
        // Recorrer subárbol izquierdo
        mostrarInOrder(nodo.left, contador);
        
        // Procesar nodo actual
        System.out.println("   ║ " + contador[0] + ". ID:" + nodo.cliente.ID + 
                         " - " + nodo.cliente.Nombre + " - Sucursal: " + nodo.sucursal + 
                         " - Saldo: $" + nodo.cliente.Monto);
        contador[0]++;
        
        // Recorrer subárbol derecho
        mostrarInOrder(nodo.right, contador);
    }
    
    // Calcular estadísticas por sucursal usando recursividad
    public void calcularEstadisticasPorSucursal() {
        System.out.println("   ║ ESTADÍSTICAS POR SUCURSAL");
        System.out.println("   ║ ═══════════════════════════");
        
        // Obtener todas las sucursales únicas
        java.util.Set<String> sucursales = new java.util.HashSet<>();
        obtenerSucursales(root, sucursales);
        
        for (String sucursal : sucursales) {
            EstadisticaSucursal stats = new EstadisticaSucursal();
            calcularEstadisticasRecursivo(root, sucursal, stats);
            
            System.out.println("   ║ Sucursal: " + sucursal);
            System.out.println("   ║   - Clientes: " + stats.totalClientes);
            System.out.println("   ║   - Saldo total: $" + stats.saldoTotal);
            System.out.println("   ║   - Saldo promedio: $" + 
                             (stats.totalClientes > 0 ? stats.saldoTotal / stats.totalClientes : 0));
            System.out.println("   ║");
        }
    }
    
    private void obtenerSucursales(NodoCliente nodo, java.util.Set<String> sucursales) {
        if (nodo == null) return;
        
        sucursales.add(nodo.sucursal);
        obtenerSucursales(nodo.left, sucursales);
        obtenerSucursales(nodo.right, sucursales);
    }
    
    private void calcularEstadisticasRecursivo(NodoCliente nodo, String sucursal, EstadisticaSucursal stats) {
        if (nodo == null) return;
        
        if (nodo.sucursal.equalsIgnoreCase(sucursal)) {
            stats.totalClientes++;
            stats.saldoTotal += nodo.cliente.Monto;
        }
        
        calcularEstadisticasRecursivo(nodo.left, sucursal, stats);
        calcularEstadisticasRecursivo(nodo.right, sucursal, stats);
    }
    
    // Encontrar cliente con mayor saldo usando recursividad
    public Cliente encontrarClienteConMayorSaldo() {
        if (root == null) return null;
        
        ClienteMaximo clienteMax = new ClienteMaximo();
        encontrarMaximoRecursivo(root, clienteMax);
        return clienteMax.cliente;
    }
    
    private void encontrarMaximoRecursivo(NodoCliente nodo, ClienteMaximo clienteMax) {
        if (nodo == null) return;
        
        if (nodo.cliente.Monto > clienteMax.saldoMaximo) {
            clienteMax.saldoMaximo = nodo.cliente.Monto;
            clienteMax.cliente = nodo.cliente;
        }
        
        encontrarMaximoRecursivo(nodo.left, clienteMax);
        encontrarMaximoRecursivo(nodo.right, clienteMax);
    }
    
    // Calcular altura del árbol (algoritmo recursivo)
    public int calcularAltura() {
        return calcularAlturaRecursivo(root);
    }
    
    private int calcularAlturaRecursivo(NodoCliente nodo) {
        if (nodo == null) {
            return 0;
        }
        
        int alturaIzquierda = calcularAlturaRecursivo(nodo.left);
        int alturaDerecha = calcularAlturaRecursivo(nodo.right);
        
        return 1 + Math.max(alturaIzquierda, alturaDerecha);
    }
    
    // Contar total de clientes usando recursividad
    public int contarClientes() {
        return contarClientesRecursivo(root);
    }
    
    private int contarClientesRecursivo(NodoCliente nodo) {
        if (nodo == null) {
            return 0;
        }
        
        return 1 + contarClientesRecursivo(nodo.left) + contarClientesRecursivo(nodo.right);
    }
    
    // Mostrar información del árbol
    public void mostrarInformacionArbol() {
        System.out.println("   ║ INFORMACIÓN DEL ÁRBOL BANCARIO");
        System.out.println("   ║ ═══════════════════════════════");
        System.out.println("   ║ Total de clientes: " + contarClientes());
        System.out.println("   ║ Altura del árbol: " + calcularAltura());
        
        Cliente clienteMax = encontrarClienteConMayorSaldo();
        if (clienteMax != null) {
            System.out.println("   ║ Cliente con mayor saldo:");
            System.out.println("   ║   ID:" + clienteMax.ID + " - " + clienteMax.Nombre + 
                             " - $" + clienteMax.Monto);
        }
    }
    
    // Buscar clientes por rango de saldo
    public void buscarClientesPorRangoSaldo(int saldomin, int saldomax) {
        System.out.println("   ║ CLIENTES CON SALDO ENTRE $" + saldomin + " Y $" + saldomax);
        System.out.println("   ║ ═══════════════════════════════════════");
        int[] contador = {1};
        buscarPorRangoRecursivo(root, saldomin, saldomax, contador);
        if (contador[0] == 1) {
            System.out.println("   ║ No se encontraron clientes en este rango");
        }
    }
    
    private void buscarPorRangoRecursivo(NodoCliente nodo, int min, int max, int[] contador) {
        if (nodo == null) return;
        
        buscarPorRangoRecursivo(nodo.left, min, max, contador);
        
        if (nodo.cliente.Monto >= min && nodo.cliente.Monto <= max) {
            System.out.println("   ║ " + contador[0] + ". ID:" + nodo.cliente.ID + 
                             " - " + nodo.cliente.Nombre + " - Saldo: $" + nodo.cliente.Monto);
            contador[0]++;
        }
        
        buscarPorRangoRecursivo(nodo.right, min, max, contador);
    }
    
    // Clases auxiliares para cálculos
    private static class EstadisticaSucursal {
        int totalClientes = 0;
        int saldoTotal = 0;
    }
    
    private static class ClienteMaximo {
        Cliente cliente = null;
        int saldoMaximo = Integer.MIN_VALUE;
    }
}
