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
// PEGA ESTE CÓDIGO ACTUALIZADO
    public static void iniciarSesion(java.io.BufferedReader reader, HashTable clientesTable) throws IOException {
        clearConsole();
        System.out.println("╭──────────────────────────────────╮");
        System.out.println("│          INICIO DE SESIÓN        │");
        System.out.println("╰──────────────────────────────────╯");

        // 1. Pedir el número de tarjeta primero para buscar al cliente
        System.out.print("  Nº Tarjeta (16 dígitos): ");
        String tarjetaUsuario = reader.readLine();

        // Validamos la tarjeta y la buscamos en la tabla hash
        if (!ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
            System.out.println("\n   Error: Formato de tarjeta no válido.");
            pause(reader);
            return; // Termina el intento de login
        }

        Cliente clienteEncontrado = clientesTable.get(tarjetaUsuario);

        // Si la tarjeta no existe, no continuamos
        if (clienteEncontrado == null) {
            System.out.println("\n   Error: Credenciales incorrectas.");
            pause(reader);
            return;
        }

        // 2. Si la tarjeta existe, pedimos el resto de datos para confirmar
        try {
            System.out.print("  ID de Cliente: ");
            int idUsuario = Integer.parseInt(reader.readLine());

            System.out.print("  Nombre: ");
            String nombreUsuario = reader.readLine();

            System.out.print("  Apellido: ");
            String apellidoUsuario = reader.readLine();
            String nombreCompleto = nombreUsuario + " " + apellidoUsuario;

            // 3. Validar que TODOS los datos coincidan con el cliente encontrado
            if (clienteEncontrado.ID == idUsuario && clienteEncontrado.Nombre.equalsIgnoreCase(nombreCompleto)) {
                System.out.println("\n   ¡Inicio de sesión exitoso! Bienvenido, " + clienteEncontrado.Nombre);
                pause(reader);
                mostrarMenuDelBanco(reader, clienteEncontrado, clientesTable);
            } else {
                System.out.println("\n   Error: Credenciales incorrectas.");
                pause(reader);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n   Error: El ID debe ser un número.");
            pause(reader);
        }
    }

    /**
     * Nuevo método para gestionar el registro de un nuevo cliente.
     */
// PEGA ESTE CÓDIGO EN LUGAR DEL MÉTODO ANTERIOR
    public static void registrarCliente(java.io.BufferedReader reader, HashTable clientesTable) throws IOException {
        clearConsole();
        System.out.println("╭──────────────────────────────────╮");
        System.out.println("│      REGISTRO DE NUEVO CLIENTE   │");
        System.out.println("╰──────────────────────────────────╯");

        // 1. Pedir el nombre
        System.out.print("  Ingrese su nombre: ");
        String nombre = reader.readLine();

        // 2. Pedir el apellido
        System.out.print("  Ingrese su apellido: ");
        String apellido = reader.readLine();

        // Concatenar nombre y apellido para el nombre completo
        String nombreCompleto = nombre + " " + apellido;

        // 3. Pedir y validar el número de tarjeta
        String numeroTarjeta;
        while (true) {
            System.out.print("  Ingrese un Nº de Tarjeta (16 dígitos): ");
            numeroTarjeta = reader.readLine();

            if (clientesTable.get(numeroTarjeta) != null) {
                System.out.println("   Error: Esta tarjeta ya está registrada.");
            } else if (ValidadorTarjeta.validarTarjeta(numeroTarjeta)) {
                break; // Tarjeta válida y no registrada, salir del bucle
            } else {
                System.out.println("   Error: Número de tarjeta no válido, intente de nuevo.");
            }
        }

        // 4. Asignar un ID secuencial. Si hay 5 clientes, el nuevo ID será 6.
        int nuevoID = clientesTable.size() + 1;

        // Agregar el nuevo cliente con saldo inicial de 0
        agregarCliente(clientesTable, nuevoID, nombreCompleto, 0, numeroTarjeta);

        System.out.println("\n   ¡Cliente '" + nombreCompleto + "' registrado con ID: " + nuevoID + "!");
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
// REEMPLAZA CON ESTE CÓDIGO
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║        BANCO FINANCIERO      ║");
                System.out.println("   ╠══════════════════════════════╣");
                System.out.println("   ║ Cliente: " + String.format("%-20s", clienteSesion.Nombre));
                System.out.println("   ║ ID Cliente: " + String.format("%-17d", clienteSesion.ID)); // Línea añadida
                System.out.printf ("   ║ Saldo: $%-23d \n", clienteSesion.Monto);
                System.out.println("   ╠══════════════════════════════╣");
                System.out.println("   ║ 1. Realizar deposito         ║");
                System.out.println("   ║ 2. Retirar monto             ║");
                System.out.println("   ║ 3. Transferir                ║");
                System.out.println("   ║ 4. Realizar Transferencias   ║");
                System.out.println("   ║ 5. Historial de movimientos  ║");
                System.out.println("   ║ 6. Caja de Inversion         ║");
                System.out.println("   ║ 7. Funciones Avanzadas       ║");
                System.out.println("   ║ 8. Cerrar sesion             ║");
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
// PEGA ESTE CÓDIGO EN LUGAR DEL BLOQUE ANTERIOR
                    case 3:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║        TRANSFERENCIA         ║");
                        System.out.println("   ╠══════════════════════════════╣");

                        // 1. Pedir el número de tarjeta del destinatario
                        System.out.print("   ║ Nº Tarjeta destinatario: ");
                        String tarjetaDestinatario = reader.readLine();

                        // 2. Usar la HashTable para encontrar al cliente de forma eficiente
                        Cliente destinatario = clientesTable.get(tarjetaDestinatario);

                        // 3. Verificar si el cliente con esa tarjeta existe
                        if (destinatario != null) {
                            // 4. Si existe, pedir el ID para una segunda validación
                            System.out.print("   ║ ID destinatario: ");
                            int idDestinatario = Integer.parseInt(reader.readLine());

                            // 5. Comprobar que el ID introducido coincide con el del cliente encontrado
                            if (destinatario.ID == idDestinatario) {
                                // 6. Si ambos datos son correctos, pedir el monto a transferir
                                System.out.print("   ║ Monto a transferir: ");
                                int montoTransferencia = Integer.parseInt(reader.readLine());

                                // 7. Validar el monto y los fondos del usuario
                                if (montoTransferencia > 0 && montoTransferencia <= clienteSesion.Monto) {
                                    // Realizar la transferencia
                                    clienteSesion.Monto -= montoTransferencia; // Resta del origen
                                    destinatario.Depositar(montoTransferencia); // Suma al destino
                                    colaTransferencias.enqueue(montoTransferencia);
                                    pilaHistorial.push("Transferencia a " + destinatario.Nombre + ": -$" + montoTransferencia);
                                    System.out.println("   ║ Transferencia realizada      ║");

                                } else if (montoTransferencia <= 0) {
                                    System.out.println("   ║ Monto no válido              ║");

                                } else {
                                    System.out.println("   ║ Fondos insuficientes!        ║");
                                }
                            } else {
                                // Error si el ID no corresponde a la tarjeta
                                System.out.println("   ║ El ID no corresponde al titular  ║");
                            }
                        } else {
                            // Error si la tarjeta no fue encontrada
                            System.out.println("   ║ Nº de Tarjeta no encontrado    ║");
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
// REEMPLAZA TU case 5 CON ESTA NUEVA VERSIÓN
                    case 5:
                        clearConsole();
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║     HISTORIAL DE MOVIMIENTOS   ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.println("   ║ 1. Ver en orden cronológico  ║");
                        System.out.println("   ║ 2. Filtrar por monto (menor a mayor) ║");
                        System.out.println("   ╚══════════════════════════════╝");
                        System.out.print("   Seleccione una opción: ");

                        try {
                            int opcionHistorial = Integer.parseInt(reader.readLine());
                            System.out.println("   ╠══════════════════════════════╣");

                            if (opcionHistorial == 1) {
                                System.out.println("   ║ Últimos movimientos:         ");
                                pilaHistorial.showAll();

                            } else if (opcionHistorial == 2) {
                                System.out.println("   ║ Movimientos ordenados por monto: ");
                                if (pilaHistorial.isEmpty()) {
                                    System.out.println("   ║ No hay movimientos para ordenar.");
                                } else {
                                    // 1. Usar tu árbol de clientes existente
                                    BinaryTreeBancario arbolMontos = new BinaryTreeBancario();

                                    // 2. Recorrer la pila
// 2. Recorrer la pila
                                    Node<String> actual = pilaHistorial.historial.firstNode;
                                    while (actual != null) {
                                        int monto = extraerMontoDeHistorial(actual.getData());
                                        if (monto != 0) {
                                            // Crear un Cliente "falso" con el monto en el ID
                                            Cliente clienteFalso = new Cliente(monto, "Transaccion", 0, "N/A");

                                            // CORRECCIÓN: Añadir el segundo argumento para la sucursal
                                            arbolMontos.insertarCliente(clienteFalso, "Historial");
                                        }
                                        actual = actual.next;
                                    }
                                    // 4. Mostrar los IDs ordenados (que son nuestros montos)
                                    arbolMontos.mostrarIdsEnOrden(); // Necesitaremos crear este método
                                }
                            } else {
                                System.out.println("   ║ Opción no válida.");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("   ║ Error: Ingrese un número válido.");
                        }

                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    // Archivo: Main.java, dentro del switch

                    case 6:
                        boolean salirCaja = false;
                        final int LIMITE_AHORROS = 500000;

                        while (!salirCaja) {
                            clearConsole();
                            System.out.println("   ╔══════════════════════════════╗");
                            System.out.println("   ║         CAJA DE AHORROS      ║");
                            System.out.println("   ╠══════════════════════════════╣");
                            System.out.printf("   ║ Saldo Principal: $%,-13d ║%n", clienteSesion.Monto);
                            System.out.printf("   ║ Saldo Ahorrado:  $%,-13d ║%n", clienteSesion.montoAhorros);
                            System.out.printf("   ║ (Límite Ahorro: $%,d)      ║%n", LIMITE_AHORROS);
                            System.out.println("   ╠══════════════════════════════╣");
                            System.out.println("   ║ 1. Depositar en Ahorros      ║");
                            System.out.println("   ║ 2. Retirar de Ahorros        ║");
                            System.out.println("   ║ 3. Ver Proyección (7%% Anual) ║");
                            System.out.println("   ║ 4. Volver al Menú Principal  ║");
                            System.out.println("   ╚══════════════════════════════╝");
                            System.out.print("   Seleccione una opción: ");

                            try {
                                int opcionCaja = Integer.parseInt(reader.readLine());
                                switch (opcionCaja) {
                                    case 1: { // <--- LLAVE DE APERTURA
                                        System.out.print("   > Monto a depositar (entero, mín. $100): ");
                                        montoDeposito = Integer.parseInt(reader.readLine());
                                        if (montoDeposito < 100) {
                                            System.out.println("   > Error: El depósito mínimo es de $100.");
                                        } else if (montoDeposito > clienteSesion.Monto) {
                                            System.out.println("   > Error: Fondos insuficientes en cuenta principal.");
                                        } else if (clienteSesion.montoAhorros + montoDeposito > LIMITE_AHORROS) {
                                            System.out.println("   > Error: Esta operación superaría el límite de $500,000.");
                                        } else {
                                            clienteSesion.Monto -= montoDeposito;
                                            clienteSesion.montoAhorros += montoDeposito;
                                            pilaHistorial.push(String.format("Depósito Ahorros: -$%d", montoDeposito));
                                            System.out.println("   > Depósito exitoso.");
                                        }
                                        pause(reader);
                                        break;
                                    } // <--- LLAVE DE CIERRE

                                    case 2: { // <--- LLAVE DE APERTURA
                                        System.out.print("   > Monto a retirar (entero): ");
                                        montoRetiro = Integer.parseInt(reader.readLine());
                                        if (montoRetiro > 0 && montoRetiro <= clienteSesion.montoAhorros) {
                                            clienteSesion.montoAhorros -= montoRetiro;
                                            clienteSesion.Monto += montoRetiro;
                                            pilaHistorial.push(String.format("Retiro Ahorros: +$%d", montoRetiro));
                                            System.out.println("   > Retiro exitoso.");
                                        } else {
                                            System.out.println("   > Error: Monto no válido o fondos insuficientes.");
                                        }
                                        pause(reader);
                                        break;
                                    } // <--- LLAVE DE CIERRE

                                    case 3: { // <--- (Opcional, pero buena práctica)
                                        if (clienteSesion.montoAhorros > 0) {
                                            final double TASA_ANUAL = 0.07;
                                            final double TASA_MENSUAL = TASA_ANUAL / 12.0;
                                            double en1Mes = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 1);
                                            double en6Meses = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 6);
                                            double en1Anio = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 12);

                                            System.out.printf("   > Proyección para $%d:%n", clienteSesion.montoAhorros);
                                            System.out.printf("   > - En 1 mes:   $%,d%n", (long)en1Mes);
                                            System.out.printf("   > - En 6 meses: $%,d%n", (long)en6Meses);
                                            System.out.printf("   > - En 1 año:   $%,d%n", (long)en1Anio);
                                        } else {
                                            System.out.println("   > No hay fondos en la caja para proyectar.");
                                        }
                                        pause(reader);
                                        break;
                                    } // <--- LLAVE DE CIERRE

                                    case 4: // Salir
                                        salirCaja = true;
                                        break;
                                    default:
                                        System.out.println("   > Opción no válida.");
                                        pause(reader);
                                }
                            } catch (Exception e) {
                                System.out.println("   > Error, por favor ingrese un número entero válido.");
                                pause(reader);
                            }
                        }
                        break; // Fin del case 6
                    case 7:
                        mostrarMenuAvanzado(reader, clientesTable);
                        break;
                    case 8:
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

    /**
     * Extrae el valor numérico de un string de transacción del historial.
     * Ej: "Deposito: +$500" -> 500
     * @param transaccion El string completo del historial.
     * @return El monto como un entero.
     */
    private static int extraerMontoDeHistorial(String transaccion) {
        try {
            int indiceSigno = transaccion.indexOf('$');
            if (indiceSigno != -1) {
                // 1. Obtener la subcadena después del '$' y convertirla a número
                String montoStr = transaccion.substring(indiceSigno + 1);
                int monto = Integer.parseInt(montoStr.trim());

                // 2. Revisar si la transacción original es un retiro o transferencia
                //    Buscamos un signo de resta "-" en cualquier parte del texto.
                if (transaccion.contains("-")) {
                    // 3. Si es negativa, convertir el monto a negativo
                    return -monto;
                } else {
                    // 4. Si no, devolver el monto positivo (para depósitos)
                    return monto;
                }
            }
        } catch (NumberFormatException e) {
            return 0; // Si hay un error, no hacer nada
        }
        return 0; // Si no se encuentra el '$', retornar 0
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

    /**
     * Calcula el crecimiento de una inversión de forma recursiva, aplicando una tasa de interés mensual.
     * @param capital El monto inicial de la inversión.
     * @param tasaMensual La tasa de interés que se aplica cada mes.
     * @param meses El número de meses restantes para calcular.
     * @return El capital final después de 'meses'.
     */
    private static double calcularInversionMensual(double capital, double tasaMensual, int meses) {
        // Caso base: Si ya no quedan meses, devolvemos el capital acumulado.
        if (meses == 0) {
            return capital;
        }
        // Llamada recursiva: Calculamos el capital para el mes siguiente, reduciendo en 1 el total de meses.
        return calcularInversionMensual(capital * (1 + tasaMensual), tasaMensual, meses - 1);
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

    // Archivo: Main.java (debe contener este método)

    private static double calcularCrecimientoAhorros(double capital, double tasaMensual, int meses) {
        if (meses == 0) return capital;
        return calcularCrecimientoAhorros(capital * (1 + tasaMensual), tasaMensual, meses - 1);
    }


}