// Archivo: Login.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Login {
    private HashTable clientesTable;
    private Map<String, String> usuariosRegistrados;
    private List<Cliente> listaClientes;

    public Login(HashTable clientesTable) {
        this.clientesTable = clientesTable;
        this.usuariosRegistrados = new HashMap<>();
        this.listaClientes = new ArrayList<>();
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
            usuariosRegistrados.put(tarjetas[i], nombres[i]);
            Cliente cliente = new Cliente(i+1, nombres[i], saldos[i], tarjetas[i]);
            clientesTable.put(tarjetas[i], cliente);
            listaClientes.add(cliente);
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

            // Verificar si el usuario está registrado
            if (!usuariosRegistrados.containsKey(tarjetaUsuario)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Usuario no registrado       ║");
                System.out.println("   ║  Por favor regístrese primero║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // Verificar que el nombre coincida
            if (!usuariosRegistrados.get(tarjetaUsuario).equalsIgnoreCase(nombreCompleto)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Nombre y tarjeta no coinciden║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

            // Buscar cliente en la lista
            Cliente clienteEncontrado = null;
            for (Cliente cliente : listaClientes) {
                if (cliente.getNumeroTarjeta().equals(tarjetaUsuario) &&
                        cliente.Nombre.equalsIgnoreCase(nombreCompleto)) {
                    clienteEncontrado = cliente;
                    break;
                }
            }

            if (clienteEncontrado != null) {
                // Verificar si ya tiene una sesión activa
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
            } else {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Error al validar credenciales║");
                System.out.println("   ╚══════════════════════════════╝");
                return null;
            }

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

            // Verificar si la tarjeta ya está registrada
            if (usuariosRegistrados.containsKey(tarjetaUsuario)) {
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║  Esta tarjeta ya está        ║");
                System.out.println("   ║  registrada en el sistema    ║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Registrar nuevo usuario
            usuariosRegistrados.put(tarjetaUsuario, nombreCompleto);

            // Crear nuevo cliente con saldo inicial de 1000
            int nuevoID = listaClientes.size() + 1;
            Cliente nuevoCliente = new Cliente(nuevoID, nombreCompleto, 1000, tarjetaUsuario);

            // Agregar a la tabla hash y a la lista
            clientesTable.put(tarjetaUsuario, nuevoCliente);
            listaClientes.add(nuevoCliente);

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

    // Método para cerrar sesión de un cliente
    public void cerrarSesion(Cliente cliente) {
        if (cliente != null) {
            cliente.cerrarSesion();
            System.out.println("   ║ Sesión cerrada exitosamente para: " + cliente.Nombre);
        }
    }

    // Método para forzar cierre de todas las sesiones (útil para administración)
    public void cerrarTodasLasSesiones() {
        for (Cliente cliente : listaClientes) {
            if (cliente.isSesionActiva()) {
                cliente.cerrarSesion();
            }
        }
        System.out.println("   ║ Todas las sesiones han sido cerradas");
    }

    // Método para verificar estado de sesiones
    public void mostrarEstadoSesiones() {
        System.out.println("   ╔══════════════════════════════╗");
        System.out.println("   ║     ESTADO DE SESIONES       ║");
        System.out.println("   ╠══════════════════════════════╣");

        int sesionesActivas = 0;
        for (Cliente cliente : listaClientes) {
            if (cliente.isSesionActiva()) {
                System.out.println("   ║ " + cliente.Nombre + " - SESIÓN ACTIVA");
                sesionesActivas++;
            }
        }

        if (sesionesActivas == 0) {
            System.out.println("   ║ No hay sesiones activas");
        }

        System.out.println("   ╠══════════════════════════════╣");
        System.out.println("   ║ Total de sesiones activas: " + sesionesActivas);
        System.out.println("   ╚══════════════════════════════╝");
    }

    // Método para obtener cliente por tarjeta
    public Cliente obtenerClientePorTarjeta(String numeroTarjeta) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getNumeroTarjeta().equals(numeroTarjeta)) {
                return cliente;
            }
        }
        return null;
    }

    // Método para obtener cliente por nombre
    public Cliente obtenerClientePorNombre(String nombre) {
        for (Cliente cliente : listaClientes) {
            if (cliente.Nombre.equalsIgnoreCase(nombre)) {
                return cliente;
            }
        }
        return null;
    }

    // Método para verificar si un usuario existe
    public boolean existeUsuario(String numeroTarjeta) {
        return usuariosRegistrados.containsKey(numeroTarjeta);
    }

    // Método para obtener la lista de usuarios registrados
    public Map<String, String> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    // Método para obtener la lista de clientes
    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para obtener el número total de clientes registrados
    public int getTotalClientes() {
        return listaClientes.size();
    }

    // Método para obtener el número de usuarios registrados
    public int getTotalUsuariosRegistrados() {
        return usuariosRegistrados.size();
    }
}