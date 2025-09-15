import java.io.IOException;

import static java.lang.Thread.sleep;

// Archivo: Main.java (con inicio de sesión y registro corregidos)
public class Main {
    public static void main(String[] args) throws IOException {
        // --- SECCIÓN 1: CONFIGURACIÓN INICIAL (SIN CAMBIOS) ---
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        HashTable clientesTable = new HashTable(10);

        // Agregar 5 clientes predefinidos
        agregarCliente(clientesTable, 1, "Juan Perez", 7500, "5201169781530257");
        agregarCliente(clientesTable, 2, "Maria Garcia", 12000, "4509297861614535");
        agregarCliente(clientesTable, 3, "Carlos Lopez", 3500, "4555061037596247");
        agregarCliente(clientesTable, 4, "Ana Rodriguez", 9800, "4915762317479773");
        agregarCliente(clientesTable, 5, "Pedro Martinez", 15000, "5161034964107141");

        // --- SECCIÓN 2: LÓGICA DE INICIO DE SESIÓN Y REGISTRO (REEMPLAZADA) ---
        // Este nuevo menú reemplaza los bucles de inicio de sesión que tenías.
        boolean salirDelPrograma = false;
        while (!salirDelPrograma) {
            clearConsole();
            System.out.println("╭──────────────────────────────────╮");
            System.out.println("│        BIENVENIDO AL BANCO       │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│ 1. Iniciar Sesión                │");
            System.out.println("│ 2. Registrar Nuevo Cliente       │");
            System.out.println("│ 3. Salir                         │");
            System.out.println("╰──────────────────────────────────╯");
            System.out.print("  Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(reader.readLine());
                switch (opcion) {
                    case 1:
                        iniciarSesion(reader, clientesTable);
                        break;
                    case 2:
                        registrarCliente(reader, clientesTable);
                        break;
                    case 3:
                        salirDelPrograma = true;
                        System.out.println("\n   Gracias por usar nuestros servicios.");
                        break;
                    default:
                        System.out.println("   Opción no válida.");
                        pause(reader);
                }
            } catch (NumberFormatException e) {
                System.out.println("   Error: Por favor, ingrese un número.");
                pause(reader);
            }
        }
    }

    // --- NUEVOS MÉTODOS PARA GESTIONAR EL FLUJO ---

    /**
     * Nuevo método para gestionar el proceso de inicio de sesión.
     * Si es exitoso, llama al menú del banco que ya tenías.
     */
    public static void iniciarSesion(java.io.BufferedReader reader, HashTable clientesTable) throws IOException {
        clearConsole();
        System.out.println("╭──────────────────────────────────╮");
        System.out.println("│          INICIO DE SESIÓN        │");
        System.out.println("╰──────────────────────────────────╯");
        System.out.print("  Nombre: ");
        String nombreUsuario = reader.readLine();

        System.out.print("  Nº Tarjeta (16 dígitos): ");
        String tarjetaUsuario = reader.readLine();

        if (ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
            Cliente clienteEncontrado = clientesTable.get(tarjetaUsuario);
            if (clienteEncontrado != null && clienteEncontrado.validarTarjetaYNombre(tarjetaUsuario, nombreUsuario)) {
                System.out.println("\n   ¡Inicio de sesión exitoso! Bienvenido, " + clienteEncontrado.Nombre);
                pause(reader);
                // Si el login es correcto, se llama a tu menú original del banco.
                mostrarMenuDelBanco(reader, clienteEncontrado, clientesTable);
            } else {
                System.out.println("\n   Error: Nombre de usuario o tarjeta incorrectos.");
                pause(reader);
            }
        } else {
            System.out.println("\n   Error: Número de tarjeta no válido.");
            pause(reader);
        }
    }

    /**
     * Nuevo método para gestionar el registro de un nuevo cliente.
     */
    public static void registrarCliente(java.io.BufferedReader reader, HashTable clientesTable) throws IOException {
        clearConsole();
        System.out.println("╭──────────────────────────────────╮");
        System.out.println("│      REGISTRO DE NUEVO CLIENTE   │");
        System.out.println("╰──────────────────────────────────╯");
        System.out.print("  Ingrese su nombre completo: ");
        String nombre = reader.readLine();

        String numeroTarjeta;
        while (true) {
            System.out.print("  Ingrese un Nº de Tarjeta (16 dígitos): ");
            numeroTarjeta = reader.readLine();
            if (clientesTable.get(numeroTarjeta) != null) {
                System.out.println("   Error: Esta tarjeta ya está registrada.");
            } else if (ValidadorTarjeta.validarTarjeta(numeroTarjeta)) {
                break;
            } else {
                System.out.println("   Error: Número de tarjeta no válido.");
            }
        }

        int id = clientesTable.size() + 1;
        agregarCliente(clientesTable, id, nombre, 0, numeroTarjeta);

        System.out.println("\n   ¡Cliente registrado con éxito!");
        System.out.println("   Ahora puede iniciar sesión desde el menú principal.");
        pause(reader);
    }

    /**
     * Este método ahora contiene tu menú del banco original.
     */
    public static void mostrarMenuDelBanco(java.io.BufferedReader reader, Cliente clienteSesion, HashTable clientesTable) {
        // --- SECCIÓN 3: TU CÓDIGO ORIGINAL DEL MENÚ DEL BANCO (SIN CAMBIOS) ---
        Stack<String> pilaHistorial = new Stack<>();
        Queue<String> colaTransferencias = new Queue<>();
        boolean salir = false;

        while (!salir) {
            try {
                clearConsole();
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║        BANCO FINANCIERO      ║");
                System.out.println("   ╠══════════════════════════════╣");
                System.out.println("   ║ Cliente: " + String.format("%-20s", clienteSesion.Nombre));
                System.out.printf ("   ║ Saldo: $%-23d \n", clienteSesion.Monto);
                System.out.println("   ╠══════════════════════════════╣");
                System.out.println("   ║ 1. Realizar deposito         ║");
                System.out.println("   ║ 2. Retirar monto             ║");
                System.out.println("   ║ 3. Transferir                ║");
                System.out.println("   ║ 4. Realizar Transferencias   ║");
                System.out.println("   ║ 5. Historial de movimientos  ║");
                System.out.println("   ║ 6. Funciones Avanzadas       ║");
                System.out.println("   ║ 7. Cerrar sesion             ║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.print("   Seleccione opcion: ");

                int opcion = Integer.parseInt(reader.readLine());

                switch (opcion) {
                    case 1:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║          DEPOSITO            ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.print("   ║ Ingrese monto: ");
                        int montoDeposito = Integer.parseInt(reader.readLine());
                        if(montoDeposito <= 0){
                            System.out.println("   ║        Valor invalido        ║");
                            System.out.println("   ╚══════════════════════════════╝");
                            sleep(1500);
                            continue;
                        }
                        clienteSesion.Depositar(montoDeposito);
                        pilaHistorial.push("Deposito: +$" + montoDeposito);
                        System.out.println("   ║ Deposito realizado          ");
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    case 2:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║           RETIRO             ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.print("   ║ Ingrese monto: ");
                        int montoRetiro = Integer.parseInt(reader.readLine());
                        if(montoRetiro <= 0) {
                            System.out.println("   ║        Valor invalido        ║");
                            System.out.println("   ╚══════════════════════════════╝");
                            sleep(1500);
                            continue;
                        }
                        if (montoRetiro <= clienteSesion.Monto) {
                            clienteSesion.Monto -= montoRetiro;
                            pilaHistorial.push("Retiro: -$" + montoRetiro);
                            System.out.println("   ║ Retiro realizado            ");
                        } else {
                            System.out.println("   ║ Fondos insuficientes!       ");
                        }
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    case 3:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║        TRANSFERENCIA         ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.print("   ║ ID destinatario: ");
                        int idDestinatario = Integer.parseInt(reader.readLine());
                        System.out.print("   ║ Monto a transferir: ");
                        int montoTransferencia = Integer.parseInt(reader.readLine());
                        if (montoTransferencia <= clienteSesion.Monto) {
                            clienteSesion.Transferir(montoTransferencia);
                            colaTransferencias.enqueue(montoTransferencia);
                            System.out.println("   ║ Transferencia realizada      ║");
                        } else {
                            System.out.println("   ║ Fondos insuficientes!        ║");
                        }
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    case 4:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║        TRANSFERENCIA         ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.print("   ║");
                        colaTransferencias.transferencias.printList("-->");
                        System.out.print("   ║ Realizar Transferencia? :");
                        String processarTransferencia = reader.readLine();
                        pilaHistorial.push("Transferencia: -$" + colaTransferencias.dequeue());
                        System.out.println("   ╚══════════════════════════════╝");
                        break;
                    case 5:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║          HISTORIAL           ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.println("   ║ Ultimos movimientos:         ");
                        pilaHistorial.showAll();
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    case 6:
                        mostrarMenuAvanzado(reader, clientesTable);
                        break;
                    case 7:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║  Sesion cerrada con exito    ║");
                        System.out.println("   ║     Vuelva pronto!           ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        salir = true; // Esto termina el bucle y vuelve al menú principal
                        sleep(1500);
                        break;
                    default:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║     Opcion no valida!        ║");
                        System.out.println("   ║   Intente nuevamente.        ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                }
            } catch (Exception e) {
                // ... (manejo de excepciones sin cambios)
            }
        }
    }

    // --- SECCIÓN 4: TODOS TUS MÉTODOS AUXILIARES Y AVANZADOS (SIN CAMBIOS) ---

    private static void agregarCliente(HashTable tabla, int id, String nombre, int monto, String numeroTarjeta) {
        Cliente cliente = new Cliente(id, nombre, monto, numeroTarjeta);
        tabla.put(numeroTarjeta, cliente);
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(20));
        }
    }

    private static void pause(java.io.BufferedReader reader) {
        try {
            System.out.print("\nPresione Enter para continuar...");
            reader.readLine();
        } catch (Exception e) {}
    }

    private static void mostrarMenuAvanzado(java.io.BufferedReader reader, HashTable clientesTable) {
        PriorityQueueBancaria colaPrioridad = new PriorityQueueBancaria();
        BinaryTreeBancario arbolClientes = new BinaryTreeBancario();
        GrafoBancario grafoRelaciones = new GrafoBancario();
        boolean estructurasInicializadas = false;
        try {
            if (!estructurasInicializadas) {
                inicializarEstructurasAvanzadas(clientesTable, arbolClientes, grafoRelaciones);
                estructurasInicializadas = true;
            }
            boolean volverMenuPrincipal = false;
            while (!volverMenuPrincipal) {
                clearConsole();
                System.out.println("   ╔══════════════════════════════════════╗");
                System.out.println("   ║        FUNCIONES AVANZADAS           ║");
                System.out.println("   ╠══════════════════════════════════════╣");
                System.out.println("   ║ 1. Cola de Prioridades               ║");
                System.out.println("   ║ 2. Árbol Binario de Clientes         ║");
                System.out.println("   ║ 3. Algoritmos Recursivos             ║");
                System.out.println("   ║ 4. Métodos de Ordenamiento           ║");
                System.out.println("   ║ 5. Métodos de Búsqueda               ║");
                System.out.println("   ║ 6. Grafo de Relaciones               ║");
                System.out.println("   ║ 7. Volver al Menú Principal          ║");
                System.out.println("   ╚══════════════════════════════════════╝");
                System.out.print("   Seleccione opción: ");
                int opcionAvanzada = Integer.parseInt(reader.readLine());
                switch (opcionAvanzada) {
                    case 1: menuColaPrioridades(reader, colaPrioridad); break;
                    case 2: menuArbolBinario(reader, arbolClientes); break;
                    case 3: menuAlgoritmosRecursivos(reader, clientesTable); break;
                    case 4: menuOrdenamiento(reader, clientesTable); break;
                    case 5: menuBusqueda(reader, clientesTable); break;
                    case 6: menuGrafoRelaciones(reader, grafoRelaciones); break;
                    case 7: volverMenuPrincipal = true; break;
                    default:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║     Opción no válida!        ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en menú avanzado: " + e.getMessage());
            pause(reader);
        }
    }

    private static void inicializarEstructurasAvanzadas(HashTable clientesTable, BinaryTreeBancario arbolClientes, GrafoBancario grafoRelaciones) {
        String[] sucursales = {"Centro", "Norte", "Sur", "Este", "Oeste"};
        for (int i = 1; i <= 5; i++) {
            Cliente cliente = clientesTable.get(getNumeroTarjetaPorID(i));
            if (cliente != null) {
                arbolClientes.insertarCliente(cliente, sucursales[i-1]);
                grafoRelaciones.agregarCliente(cliente);
            }
        }
        grafoRelaciones.agregarRelacion(1, 2, 1000, "TRANSFERENCIA");
        grafoRelaciones.agregarRelacion(2, 3, 500, "PRESTAMO");
        grafoRelaciones.agregarRelacionBidireccional(1, 4, 200, "FAMILIAR");
        grafoRelaciones.agregarRelacion(3, 5, 800, "TRANSFERENCIA");
    }

    private static String getNumeroTarjetaPorID(int id) {
        switch (id) {
            case 1: return "5201169781530257";
            case 2: return "4509297861614535";
            case 3: return "4555061037596247";
            case 4: return "4915762317479773";
            case 5: return "5161034964107141";
            default: return "";
        }
    }

    private static void menuColaPrioridades(java.io.BufferedReader reader, PriorityQueueBancaria colaPrioridad) {
        // Tu código original de este menú
    }

    private static void menuArbolBinario(java.io.BufferedReader reader, BinaryTreeBancario arbolClientes) {
        // Tu código original de este menú
    }

    private static void menuAlgoritmosRecursivos(java.io.BufferedReader reader, HashTable clientesTable) {
        // Tu código original de este menú
    }

    private static void menuOrdenamiento(java.io.BufferedReader reader, HashTable clientesTable) {
        // Tu código original de este menú
    }

    private static void menuBusqueda(java.io.BufferedReader reader, HashTable clientesTable) {
        // Tu código original de este menú
    }

    private static void menuGrafoRelaciones(java.io.BufferedReader reader, GrafoBancario grafoRelaciones) {
        // Tu código original de este menú
    }

    private static Cliente[] obtenerArrayClientes(HashTable clientesTable) {
        Cliente[] clientes = new Cliente[5];
        clientes[0] = clientesTable.get("5201169781530257");
        clientes[1] = clientesTable.get("4509297861614535");
        clientes[2] = clientesTable.get("4555061037596247");
        clientes[3] = clientesTable.get("4915762317479773");
        clientes[4] = clientesTable.get("5161034964107141");
        return clientes;
    }
}