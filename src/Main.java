import static java.lang.Thread.sleep;

// Archivo: Main.java (modificado)
public class Main{
    public static void main(String[] args) {
        // Datos iniciales del banco
        int opcion;
        boolean salir = false;
        boolean sesionIniciada = false;
        String nombreUsuario = "";
        String tarjetaUsuario = "";

        // Crear tabla hash con clientes predefinidos
        HashTable clientesTable = new HashTable(10);

        // Agregar 5 clientes predefinidos
        agregarCliente(clientesTable, 1, "Juan Perez", 7500, "5201169781530257");
        agregarCliente(clientesTable, 2, "Maria Garcia", 12000, "4509297861614535");
        agregarCliente(clientesTable, 3, "Carlos Lopez", 3500, "4555061037596247");
        agregarCliente(clientesTable, 4, "Ana Rodriguez", 9800, "4915762317479773");
        agregarCliente(clientesTable, 5, "Pedro Martinez", 15000, "5161034964107141");

        // Crear instancia de cliente para la sesión actual
        Cliente clienteSesion = new Cliente();
        Stack<String> pilaHistorial = new Stack<>();
        Queue<String> colaTransferencias = new Queue<>();

        // Buffer para lectura de entrada
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        // Pantalla de inicio de sesión
        while (!sesionIniciada) {
            try {
                ///clearConsole();

                System.out.println("╭──────────────────────────────────╮");
                System.out.println("│          BANCO FINANCIERO        │");
                System.out.println("├──────────────────────────────────┤");
                System.out.println("│          INICIO DE SESIÓN        │");
                System.out.println("╰──────────────────────────────────╯");
                System.out.print("  Nombre: ");


                nombreUsuario = reader.readLine();

                System.out.print("  Nº Tarjeta (16 dígitos): ");
                tarjetaUsuario = reader.readLine();

                // Validar tarjeta con algoritmo de Luhn
                if (ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                    // Verificar en la tabla hash si la tarjeta pertenece al usuario
                    Cliente clienteEncontrado = clientesTable.get(tarjetaUsuario);

                    if (clienteEncontrado != null && clienteEncontrado.validarTarjetaYNombre(tarjetaUsuario, nombreUsuario)) {
                        String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
                        String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

                        // Copiar los datos del cliente encontrado al cliente de la sesión
                        clienteSesion.ID = clienteEncontrado.ID;
                        clienteSesion.Nombre = clienteEncontrado.Nombre;
                        clienteSesion.Monto = clienteEncontrado.Monto;
                        clienteSesion.NumeroTarjeta = clienteEncontrado.NumeroTarjeta;

                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.println("   ║ Tarjeta " + tipoTarjeta + " valida       ");
                        System.out.println("   ║ " + tarjetaEnmascarada + "         ");
                        System.out.println("   ║ Bienvenido: " + String.format("%-15s", clienteEncontrado.Nombre));
                        System.out.println("   ╚══════════════════════════════╝");
                        System.out.print("Presione Enter para continuar...");
                        reader.readLine();

                        sesionIniciada = true;
                    } else {
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.println("   ║  Tarjeta no pertenece al     ║");
                        System.out.println("   ║  usuario o no existe!        ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        System.out.print("Presione Enter para intentar nuevamente...");
                        reader.readLine();
                    }
                } else {
                    System.out.println("   ╠══════════════════════════════ ╣");
                    System.out.println("   ║      Tarjeta invalida!        ║");
                    System.out.println("   ║  Verifique el numero ingresado║");
                    System.out.println("   ╚══════════════════════════════ ╝");
                    System.out.print("Presione Enter para intentar nuevamente...");
                    reader.readLine();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {}
            }
        }

        // Menú principal (solo accesible después de iniciar sesión)
        while (!salir) {
            try {
                clearConsole();

                // Encabezado del banco
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

                opcion = Integer.parseInt(reader.readLine());

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
                    case 5: // HISTORIAL
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
                        salir = true;
                        break;

                    default:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║     Opcion no valida!        ║");
                        System.out.println("   ║   Intente nuevamente.        ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n   ╔══════════════════════════════╗");
                System.out.println("   ║    Error: Entrada invalida   ║");
                System.out.println("   ╚══════════════════════════════╝");
                pause(reader);
            } catch (Exception e) {
                System.out.println("\n   ╔══════════════════════════════╗");
                System.out.println("   ║      Error inesperado        ║");
                System.out.println("   ╚══════════════════════════════╝");
                pause(reader);
            }
        }
    }

    // Método auxiliar para agregar clientes sin duplicar el número de tarjeta
    private static void agregarCliente(HashTable tabla, int id, String nombre, int monto, String numeroTarjeta) {
        Cliente cliente = new Cliente(id, nombre, monto, numeroTarjeta);
        tabla.put(numeroTarjeta, cliente);
    }

    // Métodos clearConsole() y pause() permanecen igual...
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
            System.out.print("Presione Enter para continuar...");
            reader.readLine();
        } catch (Exception e) {
            // Ignorar errores en la pausa
        }
    }

    // Menú avanzado con todas las estructuras de datos implementadas
    private static void mostrarMenuAvanzado(java.io.BufferedReader reader, HashTable clientesTable) {
        // Inicializar estructuras de datos avanzadas
        static PriorityQueueBancaria colaPrioridad = new PriorityQueueBancaria();
        static BinaryTreeBancario arbolClientes = new BinaryTreeBancario();
        static GrafoBancario grafoRelaciones = new GrafoBancario();
        static boolean estructurasInicializadas = false;
        
        try {
            // Inicializar estructuras si es la primera vez
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
                    case 1:
                        menuColaPrioridades(reader, colaPrioridad);
                        break;
                    case 2:
                        menuArbolBinario(reader, arbolClientes);
                        break;
                    case 3:
                        menuAlgoritmosRecursivos(reader, clientesTable);
                        break;
                    case 4:
                        menuOrdenamiento(reader, clientesTable);
                        break;
                    case 5:
                        menuBusqueda(reader, clientesTable);
                        break;
                    case 6:
                        menuGrafoRelaciones(reader, grafoRelaciones);
                        break;
                    case 7:
                        volverMenuPrincipal = true;
                        break;
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

    // Inicializar estructuras avanzadas con datos existentes
    private static void inicializarEstructurasAvanzadas(HashTable clientesTable, 
                                                       BinaryTreeBancario arbolClientes, 
                                                       GrafoBancario grafoRelaciones) {
        // Agregar clientes predefinidos al árbol y grafo
        String[] sucursales = {"Centro", "Norte", "Sur", "Este", "Oeste"};
        
        for (int i = 1; i <= 5; i++) {
            Cliente cliente = clientesTable.get(getNumeroTarjetaPorID(i));
            if (cliente != null) {
                arbolClientes.insertarCliente(cliente, sucursales[i-1]);
                grafoRelaciones.agregarCliente(cliente);
            }
        }
        
        // Agregar algunas relaciones de ejemplo en el grafo
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

    // Menú para Cola de Prioridades
    private static void menuColaPrioridades(java.io.BufferedReader reader, PriorityQueueBancaria colaPrioridad) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║        COLA DE PRIORIDADES           ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Agregar Transacción               ║");
            System.out.println("   ║ 2. Procesar Siguiente Transacción    ║");
            System.out.println("   ║ 3. Ver Transacciones Pendientes      ║");
            System.out.println("   ║ 4. Procesar Todas las Transacciones  ║");
            System.out.println("   ║ 5. Ver Estadísticas                  ║");
            System.out.println("   ║ 6. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            
            switch (opcion) {
                case 1:
                    System.out.print("   Tipo de transacción (DEPOSITO/RETIRO/TRANSFERENCIA): ");
                    String tipo = reader.readLine();
                    System.out.print("   Monto: ");
                    int monto = Integer.parseInt(reader.readLine());
                    System.out.print("   Cliente: ");
                    String cliente = reader.readLine();
                    System.out.print("   Prioridad (1=Alta, 2=Media, 3=Baja): ");
                    int prioridad = Integer.parseInt(reader.readLine());
                    
                    colaPrioridad.agregarTransaccion(tipo, monto, cliente, prioridad);
                    System.out.println("   ║ Transacción agregada exitosamente");
                    pause(reader);
                    break;
                case 2:
                    String transaccion = colaPrioridad.procesarSiguienteTransaccion();
                    if (transaccion != null) {
                        System.out.println("   ║ Transacción procesada: " + transaccion);
                    } else {
                        System.out.println("   ║ No hay transacciones pendientes");
                    }
                    pause(reader);
                    break;
                case 3:
                    colaPrioridad.mostrarTransaccionesPendientes();
                    pause(reader);
                    break;
                case 4:
                    colaPrioridad.procesarTodasLasTransacciones();
                    pause(reader);
                    break;
                case 5:
                    colaPrioridad.mostrarEstadisticas();
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Menú para Árbol Binario
    private static void menuArbolBinario(java.io.BufferedReader reader, BinaryTreeBancario arbolClientes) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║         ÁRBOL BINARIO                ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Mostrar Todos los Clientes        ║");
            System.out.println("   ║ 2. Buscar Cliente por ID             ║");
            System.out.println("   ║ 3. Buscar por Sucursal               ║");
            System.out.println("   ║ 4. Estadísticas por Sucursal         ║");
            System.out.println("   ║ 5. Información del Árbol             ║");
            System.out.println("   ║ 6. Buscar por Rango de Saldo         ║");
            System.out.println("   ║ 7. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            
            switch (opcion) {
                case 1:
                    arbolClientes.mostrarTodosLosClientes();
                    pause(reader);
                    break;
                case 2:
                    System.out.print("   Ingrese ID del cliente: ");
                    int id = Integer.parseInt(reader.readLine());
                    Cliente cliente = arbolClientes.buscarClientePorID(id);
                    if (cliente != null) {
                        System.out.println("   ║ Cliente encontrado: " + cliente.Nombre + " - Saldo: $" + cliente.Monto);
                    } else {
                        System.out.println("   ║ Cliente no encontrado");
                    }
                    pause(reader);
                    break;
                case 3:
                    System.out.print("   Ingrese nombre de sucursal: ");
                    String sucursal = reader.readLine();
                    arbolClientes.buscarClientesPorSucursal(sucursal);
                    pause(reader);
                    break;
                case 4:
                    arbolClientes.calcularEstadisticasPorSucursal();
                    pause(reader);
                    break;
                case 5:
                    arbolClientes.mostrarInformacionArbol();
                    pause(reader);
                    break;
                case 6:
                    System.out.print("   Saldo mínimo: ");
                    int min = Integer.parseInt(reader.readLine());
                    System.out.print("   Saldo máximo: ");
                    int max = Integer.parseInt(reader.readLine());
                    arbolClientes.buscarClientesPorRangoSaldo(min, max);
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Menú para Algoritmos Recursivos
    private static void menuAlgoritmosRecursivos(java.io.BufferedReader reader, HashTable clientesTable) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║      ALGORITMOS RECURSIVOS           ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Calcular Interés Compuesto        ║");
            System.out.println("   ║ 2. Estadísticas Completas            ║");
            System.out.println("   ║ 3. Proyección de Crecimiento          ║");
            System.out.println("   ║ 4. Procesar Transacciones Masivas    ║");
            System.out.println("   ║ 5. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            Cliente[] clientes = obtenerArrayClientes(clientesTable);
            
            switch (opcion) {
                case 1:
                    System.out.print("   Tasa de interés anual (ej: 0.05 para 5%): ");
                    double tasa = Double.parseDouble(reader.readLine());
                    System.out.print("   Número de años: ");
                    int años = Integer.parseInt(reader.readLine());
                    AlgoritmosBancarios.calcularInteresesClientes(clientes, tasa, años);
                    pause(reader);
                    break;
                case 2:
                    AlgoritmosBancarios.mostrarEstadisticasCompletas(clientes);
                    pause(reader);
                    break;
                case 3:
                    System.out.print("   Número de meses para proyección (máx 12): ");
                    int meses = Integer.parseInt(reader.readLine());
                    AlgoritmosBancarios.calcularProyeccionCrecimiento(meses);
                    pause(reader);
                    break;
                case 4:
                    // Ejemplo de transacciones masivas
                    String[] tipos = {"DEPOSITO", "RETIRO", "TRANSFERENCIA", "DEPOSITO", "RETIRO"};
                    int[] montos = {1000, 500, 750, 2000, 300};
                    String[] clientesNombres = {"Juan", "Maria", "Carlos", "Ana", "Pedro"};
                    AlgoritmosBancarios.procesarTransaccionesMasivas(tipos, montos, clientesNombres);
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Menú para Ordenamiento
    private static void menuOrdenamiento(java.io.BufferedReader reader, HashTable clientesTable) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║       MÉTODOS DE ORDENAMIENTO        ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Ordenar por Saldo (QuickSort)     ║");
            System.out.println("   ║ 2. Ordenar por Nombre (MergeSort)    ║");
            System.out.println("   ║ 3. Ordenar por ID (QuickSort)        ║");
            System.out.println("   ║ 4. Comparar Algoritmos               ║");
            System.out.println("   ║ 5. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            Cliente[] clientes = obtenerArrayClientes(clientesTable);
            
            switch (opcion) {
                case 1:
                    OrdenamientoBancario.mostrarClientesOrdenadosPorSaldo(clientes);
                    pause(reader);
                    break;
                case 2:
                    OrdenamientoBancario.mostrarClientesOrdenadosPorNombre(clientes);
                    pause(reader);
                    break;
                case 3:
                    OrdenamientoBancario.mostrarClientesOrdenadosPorID(clientes);
                    pause(reader);
                    break;
                case 4:
                    OrdenamientoBancario.compararAlgoritmos(clientes);
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Menú para Búsqueda
    private static void menuBusqueda(java.io.BufferedReader reader, HashTable clientesTable) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║        MÉTODOS DE BÚSQUEDA           ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Búsqueda Avanzada                ║");
            System.out.println("   ║ 2. Comparar Métodos de Búsqueda     ║");
            System.out.println("   ║ 3. Buscar por Rango de Saldo        ║");
            System.out.println("   ║ 4. Estadísticas de Búsqueda         ║");
            System.out.println("   ║ 5. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            Cliente[] clientes = obtenerArrayClientes(clientesTable);
            
            switch (opcion) {
                case 1:
                    System.out.print("   Criterio (id/nombre/saldo): ");
                    String criterio = reader.readLine();
                    System.out.print("   Valor a buscar: ");
                    String valor = reader.readLine();
                    BusquedaBancaria.busquedaAvanzada(clientes, criterio, valor);
                    pause(reader);
                    break;
                case 2:
                    System.out.print("   ID del cliente a buscar: ");
                    int id = Integer.parseInt(reader.readLine());
                    BusquedaBancaria.compararMetodosBusqueda(clientes, id);
                    pause(reader);
                    break;
                case 3:
                    System.out.print("   Saldo mínimo: ");
                    int min = Integer.parseInt(reader.readLine());
                    System.out.print("   Saldo máximo: ");
                    int max = Integer.parseInt(reader.readLine());
                    Cliente[] resultado = BusquedaBancaria.buscarClientesPorRangoSaldo(clientes, min, max);
                    System.out.println("   ║ Clientes encontrados: " + resultado.length);
                    for (Cliente c : resultado) {
                        System.out.println("   ║ " + c.Nombre + " - Saldo: $" + c.Monto);
                    }
                    pause(reader);
                    break;
                case 4:
                    BusquedaBancaria.mostrarEstadisticasBusqueda(clientes);
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Menú para Grafo de Relaciones
    private static void menuGrafoRelaciones(java.io.BufferedReader reader, GrafoBancario grafoRelaciones) {
        try {
            clearConsole();
            System.out.println("   ╔══════════════════════════════════════╗");
            System.out.println("   ║       GRAFO DE RELACIONES            ║");
            System.out.println("   ╠══════════════════════════════════════╣");
            System.out.println("   ║ 1. Mostrar Grafo Completo            ║");
            System.out.println("   ║ 2. Buscar Camino (BFS)               ║");
            System.out.println("   ║ 3. Buscar Camino (DFS)               ║");
            System.out.println("   ║ 4. Clientes Más Conectados           ║");
            System.out.println("   ║ 5. Análisis de Flujo de Dinero       ║");
            System.out.println("   ║ 6. Detectar Ciclos                   ║");
            System.out.println("   ║ 7. Estadísticas del Grafo            ║");
            System.out.println("   ║ 8. Volver                            ║");
            System.out.println("   ╚══════════════════════════════════════╝");
            System.out.print("   Seleccione opción: ");
            
            int opcion = Integer.parseInt(reader.readLine());
            
            switch (opcion) {
                case 1:
                    grafoRelaciones.mostrarGrafoCompleto();
                    pause(reader);
                    break;
                case 2:
                    System.out.print("   Cliente origen: ");
                    int origen = Integer.parseInt(reader.readLine());
                    System.out.print("   Cliente destino: ");
                    int destino = Integer.parseInt(reader.readLine());
                    grafoRelaciones.buscarCaminoBFS(origen, destino);
                    pause(reader);
                    break;
                case 3:
                    System.out.print("   Cliente origen: ");
                    int origenDFS = Integer.parseInt(reader.readLine());
                    System.out.print("   Cliente destino: ");
                    int destinoDFS = Integer.parseInt(reader.readLine());
                    grafoRelaciones.buscarCaminoDFS(origenDFS, destinoDFS);
                    pause(reader);
                    break;
                case 4:
                    grafoRelaciones.encontrarClientesMasConectados();
                    pause(reader);
                    break;
                case 5:
                    grafoRelaciones.analizarFlujoDinero();
                    pause(reader);
                    break;
                case 6:
                    grafoRelaciones.detectarCiclos();
                    pause(reader);
                    break;
                case 7:
                    grafoRelaciones.mostrarEstadisticasGrafo();
                    pause(reader);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause(reader);
        }
    }

    // Método auxiliar para obtener array de clientes desde HashTable
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
