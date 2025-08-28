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
                clearConsole();

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
                    Thread.sleep(2000);
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
                System.out.println("   ║ 6. Cerrar sesion             ║");
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
                        System.out.println("   ║        TRANSFERENCIA        ║");
                        System.out.println("   ╠══════════════════════════════╣");
                        System.out.print("   ║ ID destinatario: ");
                        int idDestinatario = Integer.parseInt(reader.readLine());
                        System.out.print("   ║ Monto a transferir: ");
                        int montoTransferencia = Integer.parseInt(reader.readLine());

                        if (montoTransferencia <= clienteSesion.Monto) {
                            clienteSesion.Transferir(montoTransferencia);
                            colaTransferencias.enqueue(montoTransferencia);
                            System.out.println("   ║ Transferencia realizada     ");
                        } else {
                            System.out.println("   ║ Fondos insuficientes!       ");
                        }
                        System.out.println("   ╚══════════════════════════════╝");
                        pause(reader);
                        break;
                    case 4:
                        System.out.println("\n   ╔══════════════════════════════╗");
                        System.out.println("   ║        TRANSFERENCIA         ║");
                        System.out.println("   ╠══════════════════════════════╣");
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
                        System.out.println("   ╚═══════════════════════════════");
                        pause(reader);
                        break;
                    case 6:
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
                System.out.println("   ║    Error: Entrada invalida    ║");
                System.out.println("   ╚══════════════════════════════╝");
                pause(reader);
            } catch (Exception e) {
                System.out.println("\n   ╔══════════════════════════════╗");
                System.out.println("   ║      Error inesperado         ║");
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
}
