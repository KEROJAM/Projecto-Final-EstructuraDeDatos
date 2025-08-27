public class Main {
    public static void main(String[] args) {
        // Datos iniciales del banco
        String nombreBanco = "✦ BANCO FINANCIERO ✦";
        int montoInicial = 5000;
        int opcion;
        boolean salir = false;
        boolean sesionIniciada = false;
        String nombreUsuario = "";
        String tarjetaUsuario = "";

        // Crear instancias necesarias
        Cliente cliente = new Cliente();
        cliente.Monto = montoInicial;
        Stack<String> pilaHistorial = new Stack<>();
        Queue<String> colaTransferencias = new Queue<>();

        // Buffer para lectura de entrada
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        // Pantalla de inicio de sesión
        while (!sesionIniciada) {
            try {
                clearConsole();

                System.out.println("┌──────────────────────────────────┐");
                System.out.println("│        " + nombreBanco + "        │");
                System.out.println("├──────────────────────────────────┤");
                System.out.println("│        INICIO DE SESIÓN         │");
                System.out.println("├──────────────────────────────────┤");
                System.out.print("│ Nombre: ");
                nombreUsuario = reader.readLine();

                System.out.print("│ Nº Tarjeta (16 dígitos): ");
                tarjetaUsuario = reader.readLine();

                // Validar tarjeta
                if (ValidadorTarjeta.validarTarjeta(tarjetaUsuario)) {
                    String tipoTarjeta = ValidadorTarjeta.obtenerTipoTarjeta(tarjetaUsuario);
                    String tarjetaEnmascarada = ValidadorTarjeta.enmascararTarjeta(tarjetaUsuario);

                    System.out.println("├──────────────────────────────────┤");
                    System.out.println("│ Tarjeta " + tipoTarjeta + " válida ✓       │");
                    System.out.println("│ " + tarjetaEnmascarada + "         │");
                    System.out.println("└──────────────────────────────────┘");
                    System.out.print("Presione Enter para continuar...");
                    reader.readLine();

                    sesionIniciada = true;
                } else {
                    System.out.println("├──────────────────────────────────┤");
                    System.out.println("│    Tarjeta inválida! ❌         │");
                    System.out.println("│  Verifique el número ingresado  │");
                    System.out.println("└──────────────────────────────────┘");
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
                System.out.println("┌──────────────────────────────────┐");
                System.out.println("│           " + nombreBanco + "           │");
                System.out.println("├──────────────────────────────────┤");
                System.out.println("│ Cliente: " + String.format("%-20s", nombreUsuario) + "│");
                System.out.printf ("│ Saldo: $%-23d │\n", cliente.Monto);
                System.out.println("├──────────────────────────────────┤");
                System.out.println("│ 1. 💰  Realizar depósito         │");
                System.out.println("│ 2. 💸  Retirar monto             │");
                System.out.println("│ 3. 🔄  Transferir                │");
                System.out.println("│ 4. 📋  Historial de movimientos  │");
                System.out.println("│ 5. 🚪  Cerrar sesión             │");
                System.out.println("└──────────────────────────────────┘");
                System.out.print("Seleccione opción: ");

                opcion = Integer.parseInt(reader.readLine());

                switch (opcion) {
                    case 1:
                        System.out.println("\n┌────────── DEPÓSITO ──────────┐");
                        System.out.print("│ Ingrese monto: ");
                        int montoDeposito = Integer.parseInt(reader.readLine());
                        cliente.Depositar(montoDeposito);
                        pilaHistorial.push("Depósito: +$" + montoDeposito);
                        System.out.println("│ Depósito realizado ✓        │");
                        System.out.println("└──────────────────────────────┘");
                        pause(reader);
                        break;

                    case 2:
                        System.out.println("\n┌────────── RETIRO ────────────┐");
                        System.out.print("│ Ingrese monto: ");
                        int montoRetiro = Integer.parseInt(reader.readLine());
                        if (montoRetiro <= cliente.Monto) {
                            cliente.Monto -= montoRetiro;
                            pilaHistorial.push("Retiro: -$" + montoRetiro);
                            System.out.println("│ Retiro realizado ✓          │");
                        } else {
                            System.out.println("│ Fondos insuficientes! ❌    │");
                        }
                        System.out.println("└──────────────────────────────┘");
                        pause(reader);
                        break;

                    case 3:
                        System.out.println("\n┌───────── TRANSFERENCIA ──────┐");
                        System.out.print("│ ID destinatario: ");
                        int idDestinatario = Integer.parseInt(reader.readLine());
                        System.out.print("│ Monto a transferir: ");
                        int montoTransferencia = Integer.parseInt(reader.readLine());

                        if (montoTransferencia <= cliente.Monto) {
                            cliente.Monto -= montoTransferencia;
                            pilaHistorial.push("Transferencia: -$" + montoTransferencia + " a ID:" + idDestinatario);
                            System.out.println("│ Transferencia realizada ✓   │");
                        } else {
                            System.out.println("│ Fondos insuficientes! ❌    │");
                        }
                        System.out.println("└──────────────────────────────┘");
                        pause(reader);
                        break;

                    case 4:
                        System.out.println("\n┌─────── HISTORIAL ───────┐");
                        System.out.println("│ Últimos movimientos:    │");
                        if (pilaHistorial.isEmpty()) {
                            System.out.println("│ No hay movimientos      │");
                        } else {
                            // Mostrar historial en orden inverso (más reciente primero)
                            System.out.println("│ - " + pilaHistorial.peek() + "       │");
                        }
                        System.out.println("└─────────────────────────┘");
                        pause(reader);
                        break;

                    case 5:
                        System.out.println("\n┌─────────────────────────┐");
                        System.out.println("│  Sesión cerrada con éxito │");
                        System.out.println("│   ¡Vuelva pronto! ✨      │");
                        System.out.println("└─────────────────────────┘");
                        salir = true;
                        break;

                    default:
                        System.out.println("\n┌─────────────────────────┐");
                        System.out.println("│   Opción no válida!     │");
                        System.out.println("│  Intente nuevamente.    │");
                        System.out.println("└─────────────────────────┘");
                        pause(reader);
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n┌─────────────────────────┐");
                System.out.println("│   Error: Entrada inválida │");
                System.out.println("└─────────────────────────┘");
                pause(reader);
            } catch (Exception e) {
                System.out.println("\n┌─────────────────────────┐");
                System.out.println("│     Error inesperado     │");
                System.out.println("└─────────────────────────┘");
                pause(reader);
            }
        }
    }

    // Método para limpiar la consola
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

    // Método para pausar la ejecución
    private static void pause(java.io.BufferedReader reader) {
        try {
            System.out.print("Presione Enter para continuar...");
            reader.readLine();
        } catch (Exception e) {
            // Ignorar errores en la pausa
        }
    }
}