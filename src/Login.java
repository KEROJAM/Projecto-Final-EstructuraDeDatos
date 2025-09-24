import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Login {
    private HashTable clientesTable;
    private LinkedList listaClientes;

    public Login(HashTable clientesTable) {
        this.clientesTable = clientesTable;
        this.listaClientes = new LinkedList();
        cargarUsuariosPredefinidos();
    }

    private void cargarUsuariosPredefinidos() {
        // Clientes predefinidos con contraseñas
        String[] tarjetas = {
                "5201169781530257", "4509297861614535", "4555061037596247",
                "4915762317479773", "5161034964107141"
        };
        String[] nombres = {
                "Juan Perez", "Maria Garcia", "Carlos Lopez",
                "Ana Rodriguez", "Pedro Martinez"
        };
        int[] saldos = {7500, 12000, 3500, 9800, 15000};
        String[] contraseñas = {"juan123", "maria456", "carlos789", "ana012", "pedro345"};

        for (int i = 0; i < 5; i++) {
            Cliente cliente = new Cliente(i + 1, nombres[i], saldos[i], tarjetas[i], contraseñas[i], false);
            clientesTable.put(tarjetas[i], cliente);
            addClienteToList(cliente);
        }
    }

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

    public Cliente mostrarMenuPrincipal(BufferedReader reader) {
        try {
            while (true) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║      BANCO FINANCIERO - MENÚ     ║");
                System.out.println("╠══════════════════════════════════╣");
                System.out.println("║  1. Iniciar Sesión               ║");
                System.out.println("║  2. Registrarse                  ║");
                System.out.println("║  3. Opciones Avanzadas           ║");
                System.out.println("║  0. Salir                        ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("  Seleccione una opción: ");

                int opcion = Integer.parseInt(reader.readLine());

                switch (opcion) {
                    case 1:
                        Cliente cliente = iniciarSesion(reader);
                        if (cliente != null) return cliente;
                        break;
                    case 2:
                        registrarNuevoUsuario(reader);
                        break;
                    case 3:
                        mostrarOpcionesAvanzadas(reader);
                        break;
                    case 0:
                        System.out.println("¡Gracias por usar Banco Financiero!");
                        return null;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private Cliente iniciarSesion(BufferedReader reader) {
        try {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║          INICIAR SESIÓN          ║");
            System.out.println("╚══════════════════════════════════╝");

            System.out.print("  Nº Tarjeta (16 dígitos): ");
            String tarjetaUsuario = reader.readLine();

            System.out.print("  Contraseña: ");
            String contraseña = reader.readLine();

            // Validar tarjeta con algoritmo de Luhn
            if (!ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║        TARJETA INVÁLIDA          ║");
                System.out.println("║  Verifique el número ingresado   ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return null;
            }

            // Buscar cliente en la HashTable
            Cliente clienteEncontrado = clientesTable.get(tarjetaUsuario);

            // Verificar si el usuario existe
            if (clienteEncontrado == null) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║      USUARIO NO ENCONTRADO       ║");
                System.out.println("║   Por favor regístrese primero   ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return null;
            }

            // Verificar contraseña
            if (!clienteEncontrado.validarContraseña(contraseña)) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║       CONTRASEÑA INCORRECTA      ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return null;
            }

            // Verificar si ya tiene sesión activa
            if (clienteEncontrado.isSesionActiva()) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║       SESIÓN YA ACTIVA           ║");
                System.out.println("║  No puede iniciar sesión nuevamente║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return null;
            }

            // Iniciar sesión exitosamente
            clienteEncontrado.iniciarSesion();

            String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
            String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║        INICIO EXITOSO            ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ Tarjeta " + tipoTarjeta + " válida");
            System.out.println("║ " + tarjetaEnmascarada);
            System.out.println("║ Bienvenido: " + clienteEncontrado.Nombre);
            System.out.println("║ ID: " + clienteEncontrado.ID + " (interno)");
            System.out.println("╚══════════════════════════════════╝");

            System.out.print("Presione Enter para continuar...");
            reader.readLine();

            return clienteEncontrado;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            try {
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
            } catch (Exception ex) {}
            return null;
        }
    }

    private void registrarNuevoUsuario(BufferedReader reader) {
        try {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║         REGISTRO DE USUARIO      ║");
            System.out.println("╚══════════════════════════════════╝");

            System.out.print("  Nombre completo: ");
            String nombreCompleto = reader.readLine();

            System.out.print("  Nº Tarjeta (16 dígitos): ");
            String tarjetaUsuario = reader.readLine();

            System.out.print("  Contraseña: ");
            String contraseña = reader.readLine();

            System.out.print("  Confirmar contraseña: ");
            String confirmacionContraseña = reader.readLine();

            // Validaciones
            if (!contraseña.equals(confirmacionContraseña)) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║   LAS CONTRASEÑAS NO COINCIDEN   ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            if (contraseña.length() < 4) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║ CONTRASEÑA MUY CORTA (mín. 4)    ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Validar tarjeta
            if (!ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║        TARJETA INVÁLIDA          ║");
                System.out.println("║  Verifique el número ingresado   ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Verificar si la tarjeta ya está registrada
            if (clientesTable.contains(tarjetaUsuario)) {
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║   TARJETA YA REGISTRADA          ║");
                System.out.println("║  Esta tarjeta ya está en uso     ║");
                System.out.println("╚══════════════════════════════════╝");
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
                return;
            }

            // Crear nuevo cliente con saldo inicial de 1000
            int nuevoID = getTotalClientes() + 1;
            Cliente nuevoCliente = new Cliente(nuevoID, nombreCompleto, 1000, tarjetaUsuario, contraseña,false);

            // Agregar a la tabla hash y a la lista
            clientesTable.put(tarjetaUsuario, nuevoCliente);
            addClienteToList(nuevoCliente);

            String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
            String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║       REGISTRO EXITOSO          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ Tarjeta " + tipoTarjeta + " válida");
            System.out.println("║ " + tarjetaEnmascarada);
            System.out.println("║ Usuario: " + nombreCompleto);
            System.out.println("║ ID asignado: " + nuevoID + " (interno)");
            System.out.println("║ Saldo inicial: $1000");
            System.out.println("╚══════════════════════════════════╝");

            System.out.print("Presione Enter para continuar...");
            reader.readLine();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            try {
                System.out.print("Presione Enter para continuar...");
                reader.readLine();
            } catch (Exception ex) {}
        }
    }

    private void mostrarOpcionesAvanzadas(BufferedReader reader) {
        try {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║       OPCIONES AVANZADAS        ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Estado de Sesiones          ║");
            System.out.println("║  2. Cerrar Todas las Sesiones   ║");
            System.out.println("║  3. Ver Usuarios Registrados    ║");
            System.out.println("║  0. Volver al Menú Principal    ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Seleccione una opción: ");

            int opcion = Integer.parseInt(reader.readLine());

            switch (opcion) {
                case 1:
                    mostrarEstadoSesiones();
                    break;
                case 2:
                    cerrarTodasLasSesiones();
                    break;
                case 3:
                    mostrarUsuariosRegistrados();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.print("Presione Enter para continuar...");
            reader.readLine();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Métodos auxiliares (se mantienen igual)
    public void cerrarSesion(Cliente cliente) {
        if (cliente != null) {
            cliente.cerrarSesion();
            System.out.println("Sesión cerrada exitosamente para: " + cliente.Nombre);
        }
    }

    public void cerrarTodasLasSesiones() {
        Node<Cliente> current = listaClientes.firstNode;
        int cerradas = 0;
        while (current != null) {
            Cliente cliente = current.getData();
            if (cliente.isSesionActiva()) {
                cliente.cerrarSesion();
                cerradas++;
            }
            current = current.next;
        }
        System.out.println("Se cerraron " + cerradas + " sesiones activas.");
    }

    public void mostrarEstadoSesiones() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║        ESTADO DE SESIONES       ║");
        System.out.println("╠══════════════════════════════════╣");

        int sesionesActivas = 0;
        Node<Cliente> current = listaClientes.firstNode;
        while (current != null) {
            Cliente cliente = current.getData();
            if (cliente.isSesionActiva()) {
                System.out.println("║ " + cliente.Nombre + " - ACTIVA");
                sesionesActivas++;
            }
            current = current.next;
        }

        if (sesionesActivas == 0) {
            System.out.println("║ No hay sesiones activas");
        }

        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ Total de sesiones activas: " + sesionesActivas);
        System.out.println("╚══════════════════════════════════╝");
    }

    public void mostrarUsuariosRegistrados() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║      USUARIOS REGISTRADOS       ║");
        System.out.println("╠══════════════════════════════════╣");

        Node<Cliente> current = listaClientes.firstNode;
        while (current != null) {
            Cliente cliente = current.getData();
            System.out.println("║ ID: " + cliente.ID + " - " + cliente.Nombre);
            current = current.next;
        }

        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ Total: " + getTotalClientes() + " usuarios");
        System.out.println("╚══════════════════════════════════╝");
    }

    public Cliente obtenerClientePorTarjeta(String numeroTarjeta) {
        return clientesTable.get(numeroTarjeta);
    }

    public boolean existeUsuario(String numeroTarjeta) {
        return clientesTable.contains(numeroTarjeta);
    }

    public LinkedList getListaClientes() {
        return listaClientes;
    }

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