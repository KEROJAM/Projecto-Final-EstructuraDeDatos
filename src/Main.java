import java.io.IOException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static HashTable clientesTable;
    private static GrafoBancario grafoBancario;
    private static Scanner scanner;
    private static Cliente clienteActual;
    private static final String RUTA_CSV = "clientes.csv";

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        clientesTable = new HashTable(100);
        grafoBancario = new GrafoBancario();

        // Cargar clientes desde el archivo CSV
        CSVClientLoader.cargarClientes(RUTA_CSV, clientesTable);

        // Agregamos los clientes cargados al grafo
        for (Cliente cliente : clientesTable.obtenerTodosLosClientes()) {
            if (cliente != null) {
                grafoBancario.agregarCliente(cliente);
            }
        }

        login();
    }

    private static void login() {
        try {
            System.out.println("╔══════════════════════════════════╗");
            System.out.println("║        INICIO DE SESIÓN          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.print("║ Ingrese su numero de tarjeta: ");
            String numeroTarjeta = scanner.nextLine();
            System.out.print("║ Ingrese su contrasena: ");
            String contrasena = scanner.nextLine();
            System.out.println("╚══════════════════════════════════╝");

            clienteActual = clientesTable.get(numeroTarjeta);

            if (clienteActual != null && clienteActual.validarContraseña(contrasena)) {
                System.out.println("\nInicio de sesion exitoso. ¡Bienvenido, " + clienteActual.Nombre + "!\n");
                mostrarMenu();
            } else {
                System.out.println("\nERROR: Numero de tarjeta o contrasena incorrectos.");
                login();
            }
        } catch (Exception e) {
            System.out.println("ERROR inesperado durante el login: " + e.getMessage());
            login();
        }
    }

    private static void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║         MENÚ PRINCIPAL           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. Consultar saldo               ║");
            System.out.println("║ 2. Depositar                     ║");
            System.out.println("║ 3. Transferir                    ║");
            System.out.println("║ 4. Retirar                       ║");
            System.out.println("║ 5. Historial de movimientos      ║");
            System.out.println("║ 6. Actualizar información        ║");
            System.out.println("║ 7. Mostrar Clientes Registrados  ║");
            System.out.println("║ 8. Salir                         ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        consultarSaldo();
                        break;
                    case 2:
                        depositar();
                        break;
                    case 3:
                        transferir();
                        break;
                    case 4:
                        retirar();
                        break;
                    case 5:
                        mostrarHistorialMovimientos();
                        break;
                    case 6:
                        actualizarInfo();
                        break;
                    case 7:
                        mostrarClientesRegistrados();
                        break;
                    case 8:
                        System.out.println("\nGracias por usar nuestros servicios. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer del scanner
                opcion = 0;
            }
        } while (opcion != 8);
    }

    // Método para mostrar los clientes cargados desde el CSV
    private static void mostrarClientesRegistrados() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║      CLIENTES REGISTRADOS        ║");
        System.out.println("╠══════════════════════════════════╣");

        for (Cliente cliente : clientesTable.obtenerTodosLosClientes()) {
            if (cliente != null) {
                System.out.printf("║ ID: %-4d Nombre: %s\n", cliente.ID, cliente.Nombre);
            }
        }

        System.out.println("╚══════════════════════════════════╝");
    }

    private static void consultarSaldo() {
        System.out.printf("\nSaldo actual: $%d\n", clienteActual.Monto);

    }

    private static void depositar() {
        try {
            System.out.print("Ingrese el monto a depositar: $");
            int monto = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (monto > 0) {
                clienteActual.Depositar(monto);
                String movimiento = String.format("Depósito de $%d", monto);
                clienteActual.addMovimiento(movimiento, monto, "DEPOSITO");
                System.out.printf("Depósito de $%d realizado con éxito. Nuevo saldo: $%d\n", monto, clienteActual.Monto);
            } else {
                System.out.println("El monto debe ser un valor positivo.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
            scanner.nextLine();
        }
    }

    private static void transferir() {
        try {
            System.out.print("Ingrese el número de tarjeta del destinatario: ");
            String tarjetaDestino = scanner.nextLine();

            Cliente clienteDestino = clientesTable.get(tarjetaDestino);

            if (clienteDestino == null) {
                System.out.println("No se encontró un cliente con ese número de tarjeta.");
                return;
            }

            if (clienteDestino.getNumeroTarjeta().equals(clienteActual.getNumeroTarjeta())) {
                System.out.println("No puede transferir a su propia cuenta.");
                return;
            }

            System.out.print("Ingrese el monto a transferir: $");
            int monto = scanner.nextInt();
            scanner.nextLine();

            if (monto > 0 && clienteActual.Monto >= monto) {
                clienteActual.Transferir(monto);
                clienteDestino.Depositar(monto);

                // Agregar al historial del emisor
                String movimientoOrigen = String.format("Transferencia a %s: - $%d", clienteDestino.Nombre, monto);
                clienteActual.addMovimiento(movimientoOrigen, -monto, "TRANSFERENCIA");

                // Agregar al historial del receptor
                String movimientoDestino = String.format("Transferencia de %s: + $%d", clienteActual.Nombre, monto);
                clienteDestino.addMovimiento(movimientoDestino, monto, "TRANSFERENCIA");

                // Detección de fraude
                if (grafoBancario.detectarPosibleFraude(clienteActual, LocalDateTime.now())) {
                    System.out.println("\n¡ADVERTENCIA DE SEGURIDAD! Se han detectado múltiples transacciones en un corto periodo. Podría ser un posible fraude.");
                }

                System.out.printf("Transferencia de $%d a %s realizada con éxito. Nuevo saldo: $%d\n", monto, clienteDestino.Nombre, clienteActual.Monto);
            } else {
                System.out.println("Monto inválido o saldo insuficiente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
            scanner.nextLine();
        }
    }

    private static void retirar() {
        try {
            System.out.print("Ingrese el monto a retirar: $");
            int monto = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (monto > 0 && clienteActual.Monto >= monto) {
                clienteActual.Retirar(monto);
                String movimiento = String.format("Retiro de $%d", monto);
                clienteActual.addMovimiento(movimiento, -monto, "RETIRO");
                System.out.printf("Retiro de $%d realizado con éxito. Nuevo saldo: $%d\n", monto, clienteActual.Monto);
            } else {
                System.out.println("Monto inválido o saldo insuficiente.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
            scanner.nextLine();
        }
    }

    private static void mostrarHistorialMovimientos() {
        if (clienteActual.historial.isEmpty()) {
            System.out.println("\nNo hay movimientos en el historial.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║    HISTORIAL DE MOVIMIENTOS      ║");
        System.out.println("╠══════════════════════════════════╣");

        int opcion;
        do {
            System.out.println("║ 1. Ver en orden cronológico      ║");
            System.out.println("║ 2. Ordenar de Mayor a menor      ║");
            System.out.println("║ 3. Ordenar de Menor a mayor      ║");
            System.out.println("║ 4. Volver al menú principal      ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Seleccione una opción de ordenamiento: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        clienteActual.historial.mostrarHistorialCronologico();
                        break;
                    case 2:
                        clienteActual.historial.mostrarHistorialPorMonto(true);
                        break;
                    case 3:
                        clienteActual.historial.mostrarHistorialPorMonto(false);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
                opcion = 0;
            }
        } while (opcion != 4);
    }

    private static void actualizarInfo() {
        System.out.println("\nFuncionalidad de actualización no implementada.");
    }
}