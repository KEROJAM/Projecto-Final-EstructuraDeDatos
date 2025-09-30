import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class CSVClientLoader {

    /**
     * Carga clientes desde un CSV con 7 columnas.
     * Formato: ID,Nombre,Monto,MontoAhorros,NumeroTarjeta,password,TarjetaBloqueada
     */
    public static int cargarClientes(String rutaArchivo, HashTable clientesTable) {
        int clientesCargados = 0;
        String linea;
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.err.println("ERROR: El archivo CSV no existe en la ruta: " + rutaArchivo);
            System.err.println("Directorio de Trabajo actual: " + System.getProperty("user.dir"));
            System.err.println("Ruta absoluta buscada: " + archivo.getAbsolutePath());
            return 0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Omitir la cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                // CAMBIO: Ahora busca exactamente 7 columnas, como en tu archivo.
                if (datos.length == 7) {
                    try {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        int monto = Integer.parseInt(datos[2].trim());
                        int montoAhorros = Integer.parseInt(datos[3].trim()); // Nuevo campo leído
                        String numeroTarjeta = datos[4].trim();
                        String contrasena = datos[5].trim();
                        Boolean tarjetaBloqueada = Boolean.parseBoolean(datos[6].trim());

                        if (!clientesTable.contains(numeroTarjeta)) {
                            // IMPORTANTE: El constructor de Cliente DEBE aceptar 'montoAhorros'.
                            Cliente nuevoCliente = new Cliente(id, nombre, monto, montoAhorros, numeroTarjeta, contrasena, tarjetaBloqueada);
                            clientesTable.put(numeroTarjeta, nuevoCliente);
                            clientesCargados++;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en línea CSV: " + linea);
                    }
                } else {
                    System.err.println("ADVERTENCIA: Se omitió una línea con formato incorrecto (no tiene 7 columnas): " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        System.out.println("Carga finalizada. Clientes cargados: " + clientesCargados);
        return clientesCargados;
    }
    // EN TU CLASE CSVClientLoader.java

    public static void guardarCliente(Cliente cliente, String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        boolean existe = archivo.exists();

        // Asegurar que el directorio exista antes de escribir
        CSVPathResolver.crearDirectorioSiNoExiste(rutaArchivo);

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo, true))) {
            if (!existe || archivo.length() == 0) {
                // Cabecera de 7 columnas
                writer.println("ID,Nombre,Monto,MontoAhorros,NumeroTarjeta,password,TarjetaBloqueada");
            }

            // CORRECCIÓN: Se añade cliente.MontoAhorros para guardar los 7 campos
            writer.printf("%d,%s,%f,%f,%s,%s,%b\n",
                    cliente.ID,
                    cliente.Nombre,
                    cliente.Monto,
                    cliente.montoAhorros, // Este es el campo que faltaba
                    cliente.NumeroTarjeta,
                    cliente.getContraseña(),
                    cliente.tarjetaBloqueada);

        } catch (IOException e) {
            System.err.println("Error al guardar el cliente en el archivo CSV: " + e.getMessage());
        }
    }
}