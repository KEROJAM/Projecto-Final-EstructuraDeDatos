import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class CSVClientLoader {

    /**
     * Carga clientes desde un archivo CSV a una HashTable.
     * El formato esperado del CSV es: ID,Nombre,Monto,NumeroTarjeta,Contraseña
     * @param rutaArchivo La ruta del archivo CSV.
     * @param clientesTable La tabla hash donde se cargarán los clientes.
     * @return El número de clientes nuevos cargados.
     */
    public static int cargarClientes(String rutaArchivo, HashTable clientesTable) {
        int clientesCargados = 0;
        String linea;
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.err.println("El archivo CSV no existe en la ruta: " + rutaArchivo);
            return 0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            // Omitir la cabecera si existe
            br.readLine();
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    try {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        int monto = Integer.parseInt(datos[2].trim());
                        String numeroTarjeta = datos[3].trim();
                        String contrasena = datos[4].trim();

                        if (!clientesTable.contains(numeroTarjeta)) {
                            Cliente nuevoCliente = new Cliente(id, nombre, monto, numeroTarjeta, contrasena);
                            clientesTable.put(numeroTarjeta, nuevoCliente);
                            clientesCargados++;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en línea CSV: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return clientesCargados;
    }

    /**
     * Guarda un nuevo cliente en el archivo CSV. Si el archivo no existe, lo crea.
     * @param cliente El cliente a guardar.
     * @param rutaArchivo La ruta del archivo CSV.
     */
    public static void guardarCliente(Cliente cliente, String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        boolean existe = archivo.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo, true))) {
            if (!existe || archivo.length() == 0) {
                writer.println("ID,Nombre,Monto,NumeroTarjeta,Contraseña");
            }
            writer.printf("%d,%s,%d,%s,%s\n",
                    cliente.ID,
                    cliente.Nombre,
                    cliente.Monto,
                    cliente.NumeroTarjeta,
                    cliente.getContraseña());
        } catch (IOException e) {
            System.err.println("Error al guardar el cliente en el archivo CSV: " + e.getMessage());
        }
    }


    /**
     * Crea un archivo CSV de ejemplo con datos de clientes, incluyendo contraseñas.
     * @param rutaArchivo La ruta donde se guardará el archivo de ejemplo.
     */
    public static void crearEjemplo(String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.append("ID,Nombre,Monto,NumeroTarjeta,Contraseña\n");
            writer.append("1,Juan Perez,7500,5201169781530257,juan123\n");
            writer.append("2,Maria Garcia,12000,4509297861614535,maria456\n");
            writer.append("3,Carlos Lopez,3500,4555061037596247,carlos789\n");
            writer.append("4,Ana Rodriguez,9800,4915762317479773,ana012\n");
            writer.append("5,Pedro Martinez,15000,5161034964107141,pedro345\n");
        } catch (IOException e) {
            System.err.println("Error al crear el archivo CSV de ejemplo: " + e.getMessage());
        }
    }
}