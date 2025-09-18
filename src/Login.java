// Archivo: Login.java
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Login {
    private HashTable clientesTable;
    // Se elimina la dependencia de Map y se usará HashTable para todo
    // private Map<String, String> usuariosRegistrados;

    // Se reemplaza List<Cliente> por la clase LinkedList proporcionada
    private LinkedList listaClientes;

    public Login(HashTable clientesTable) {
        this.clientesTable = clientesTable;
        // this.usuariosRegistrados = new HashMap<>(); // Eliminado
        this.listaClientes = new LinkedList(); // Se usa la LinkedList personalizada
        cargarUsuariosPredefinidos();
    }

    private void cargarUsuariosPredefinidos() {
        // Cargar los 5 clientes predefinidos del sistema
        String[] tarjetas = {
                "5201169781530257", "4509297861614535", "4555061037596247",
                "4915762317479773", "5161034964107141"
        };
        String[] nombres = {
                "Juan Perez", "Maria Garcia", "Carlos Lopez",
                "Ana Rodriguez", "Pedro Martinez"
        };
        int[] saldos = {7500, 12000, 3500, 9800, 15000};

        for (int i = 0; i < 5; i++) {
            Cliente cliente = new Cliente(i + 1, nombres[i], saldos[i], tarjetas[i]);
            clientesTable.put(tarjetas[i], cliente);

            // Lógica para agregar a la LinkedList personalizada
            addClienteToList(cliente);
        }
    }

    /**
     * Método auxiliar para agregar un cliente a nuestra LinkedList personalizada.
     * Simula el comportamiento de .add() de un ArrayList.
     */
    private void addClienteToList(Cliente cliente) {
        Node<Cliente> newNode = new Node<>(cliente);
        if (listaClientes.firstNode == null) {
            listaClientes.firstNode = newNode;
        } else {
            Node current = listaClientes.firstNode;
            while (current.next != null) {
                current = current.next;
            }
            current.setNext(newNode);
        }
    }

    public Cliente iniciarSesion(BufferedReader reader) {
        try {
            System.out.println("╭──────────────────────────────────╮");
            System.out.println("│          INICIO DE SESIÓN        │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│ 1. Ya estoy registrado           │");
            System.out.println("│ 2. Registrarme como nuevo usuario│");
            System.out.println("╰──────────────────────────────────╯");
            System.out.print("  Seleccione opción: ");

            int opcion = Integer.parseInt(reader.readLine());

            switch (opcion) {
                case 1:
                    return loginUsuarioExistente(reader);
                case 2:
                    registrarNuevoUsuario(reader);
                    return null; // Siempre retorna null después de registro
                default:
                    System.out.println("Opción no válida");
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private Cliente loginUsuarioExistente(BufferedReader reader) {
        try {
            System.out.println("\n╭──────────────────────────────────╮");
            System.out.println("│        INICIAR SESIÓN            │");
            System.out.println("╰──────────────────────────────────╯");

            System.out.print("  Nombre completo: ");
            String nombreCompleto = reader.readLine();

            System.out.print("  Nº Tarjeta (16 dígitos): ");
            String tarjetaUsuario = reader.readLine();

            // Validar tarjeta con algoritmo de Luhn
            if (!ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║      Tarjeta inválida!       ║");
                System.out.println("   ║ Verifique el número ingresado║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // Se usa el método get de HashTable para buscar al cliente.
            Cliente clienteEncontrado = clientesTable.get(tarjetaUsuario);

            // Verificar si el usuario está registrado
            if (clienteEncontrado == null) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Usuario no registrado       ║");
                System.out.println("   ║  Por favor regístrese primero║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // Verificar que el nombre coincida con el cliente encontrado en la HashTable
            if (!clienteEncontrado.Nombre.equalsIgnoreCase(nombreCompleto)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Nombre y tarjeta no coinciden║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // A este punto, el cliente es válido. Ahora verificamos la sesión.
            if (clienteEncontrado.isSesionActiva()) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Sesión ya activa            ║");
                System.out.println("   ║  No puede iniciar sesión     ║");
                System.out.println("   ║  nuevamente                  ║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // Marcar sesión como activa
            clienteEncontrado.iniciarSesion();

            String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
            String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

            System.out.println("   ╠══════════════════════════════╣");
            System.out.println("   ║ Tarjeta " + tipoTarjeta + " válida       ");
            System.out.println("   ║ " + tarjetaEnmascarada + "         ");
            System.out.println("   ║ Bienvenido: " + String.format("%-15s", nombreCompleto));
            System.out.println("   ╚══════════════════════════════╝");

            System.out.print("Presione Enter para continuar...");
            reader.readLine();

            return clienteEncontrado;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private void registrarNuevoUsuario(BufferedReader reader) {
        try {
            System.out.println("\n╭──────────────────────────────────╮");
            System.out.println("│        REGISTRO DE USUARIO       │");
            System.out.println("╰──────────────────────────────────╯");

            System.out.print("  Nombre completo: ");
            String nombreCompleto = reader.readLine();

            System.out.print("  Nº Tarjeta (16 dígitos): ");
            String tarjetaUsuario = reader.readLine();

            // Validar tarjeta con algoritmo de Luhn
            if (!ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║      Tarjeta inválida!       ║");
                System.out.println("   ║ Verifique el número ingresado║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Verificar si la tarjeta ya está registrada usando la HashTable
            if (clientesTable.contains(tarjetaUsuario)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Esta tarjeta ya está        ║");
                System.out.println("   ║  registrada en el sistema    ║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Crear nuevo cliente con saldo inicial de 1000
            int nuevoID = getTotalClientes() + 1;
            Cliente nuevoCliente = new Cliente(nuevoID, nombreCompleto, 1000, tarjetaUsuario);

            // Agregar a la tabla hash y a la lista personalizada
            clientesTable.put(tarjetaUsuario, nuevoCliente);
            addClienteToList(nuevoCliente);

            String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
            String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

            System.out.println("   ╔══════════════════════════════╗");
            System.out.println("   ║     REGISTRO EXITOSO         ║");
            System.out.println("   ╠══════════════════════════════╣");
            System.out.println("   ║ Tarjeta " + tipoTarjeta + " válida       ");
            System.out.println("   ║ " + tarjetaEnmascarada + "         ");
            System.out.println("   ║ Bienvenido: " + String.format("%-15s", nombreCompleto));
            System.out.println("   ║ Saldo inicial: $1000         ");
            System.out.println("   ╚══════════════════════════════╝");

            System.out.println("   ║ Será redirigido al inicio de sesión");
            System.out.print("   ║ Presione Enter para continuar...");
            reader.readLine();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            try {
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
            } catch (Exception ex) {}
        }
    }

    // ----- MÉTODOS ADAPTADOS PARA USAR LA LINKEDLIST PERSONALIZADA -----

    // Método para cerrar sesión de un cliente
    public void cerrarSesion(Cliente cliente) {
        if (cliente != null) {
            cliente.cerrarSesion();
            System.out.println("   ║ Sesión cerrada exitosamente para: " + cliente.Nombre);
        }
    }

    // Método para forzar cierre de todas las sesiones
    public void cerrarTodasLasSesiones() {
        Node<Cliente> current = listaClientes.firstNode;
        while (current != null) {
            Cliente cliente = current.getData();
            if (cliente.isSesionActiva()) {
                cliente.cerrarSesion();
            }
            current = current.next;
        }
        System.out.println("   ║ Todas las sesiones han sido cerradas");
    }

    // Método para verificar estado de sesiones
    public void mostrarEstadoSesiones() {
        System.out.println("   ╔══════════════════════════════╗");
        System.out.println("   ║     ESTADO DE SESIONES       ║");
        System.out.println("   ╠══════════════════════════════╣");

        int sesionesActivas = 0;
        Node<Cliente> current = listaClientes.firstNode;
        while (current != null) {
            Cliente cliente = current.getData();
            if (cliente.isSesionActiva()) {
                System.out.println("   ║ " + cliente.Nombre + " - SESIÓN ACTIVA");
                sesionesActivas++;
            }
            current = current.next;
        }

        if (sesionesActivas == 0) {
            System.out.println("   ║ No hay sesiones activas");
        }

        System.out.println("   ╠══════════════════════════════╣");
        System.out.println("   ║ Total de sesiones activas: " + sesionesActivas);
        System.out.println("   ╚══════════════════════════════╝");
    }

    // Método para obtener cliente por tarjeta (usa HashTable, más eficiente)
    public Cliente obtenerClientePorTarjeta(String numeroTarjeta) {
        return clientesTable.get(numeroTarjeta);
    }

    // Método para obtener cliente por nombre
    public Cliente obtenerClientePorNombre(String nombre) {
        Node<Cliente> current = listaClientes.firstNode;
        while (current != null) {
            Cliente cliente = current.getData();
            if (cliente.Nombre.equalsIgnoreCase(nombre)) {
                return cliente;
            }
            current = current.next;
        }
        return null;
    }

    // Método para verificar si un usuario existe
    public boolean existeUsuario(String numeroTarjeta) {
        return clientesTable.contains(numeroTarjeta);
    }

    // Método para obtener la lista de clientes (devuelve la LinkedList)
    public LinkedList getListaClientes() {
        return listaClientes;
    }

    // Método para obtener el número total de clientes registrados
    public int getTotalClientes() {
        int count = 0;
        Node current = listaClientes.firstNode;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}