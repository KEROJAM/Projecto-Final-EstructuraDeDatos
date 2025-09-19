import java.io.*;
import java.util.Scanner;

/**
 * Clase simple para cargar clientes desde archivos CSV
 * Formato del CSV: ID,Nombre,Monto,NumeroTarjeta
 */
public class CSVClientLoader {
    
    /**
     * Carga clientes desde un archivo CSV
     * @param archivo Ruta del archivo CSV
     * @param tabla HashTable donde almacenar los clientes
     * @return Número de clientes cargados
     */
    public static int cargarClientes(String archivo, HashTable tabla) {
        int clientesCargados = 0;
        
        try {
            File file = new File(archivo);
            Scanner scanner = new Scanner(file);
            
            // Saltar la primera línea (encabezados)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // Leer cada línea del archivo
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                
                if (linea.isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                
                try {
                    // Dividir la línea por comas
                    String[] datos = linea.split(",");
                    
                    if (datos.length >= 4) {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        int monto = Integer.parseInt(datos[2].trim());
                        String tarjeta = datos[3].trim();
                        
                        // Crear nuevo cliente
                        Cliente cliente = new Cliente(id, nombre, monto, tarjeta);
                        
                        // Si hay un quinto campo (monto ahorros), agregarlo
                        if (datos.length >= 5) {
                            try {
                                int ahorros = Integer.parseInt(datos[4].trim());
                                cliente.montoAhorros = ahorros;
                            } catch (NumberFormatException e) {
                                // Si no se puede parsear, dejar en 0
                            }
                        }
                        
                        // Agregar a la tabla si no existe
                        if (!tabla.contains(tarjeta)) {
                            tabla.put(tarjeta, cliente);
                            clientesCargados++;
                            System.out.println("Cliente cargado: " + nombre + " (" + tarjeta + ")");
                        } else {
                            System.out.println("Cliente ya existe: " + nombre + " (" + tarjeta + ")");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error procesando línea: " + linea);
                }
            }
            
            scanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + archivo);
            return 0;
        } catch (Exception e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
            return 0;
        }
        
        return clientesCargados;
    }
    
    /**
     * Crea un archivo CSV de ejemplo
     * @param rutaArchivo Donde crear el archivo
     */
    public static void crearEjemplo(String rutaArchivo) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo));
            
            // Escribir encabezados
            writer.println("ID,Nombre,Monto,NumeroTarjeta,MontoAhorros");
            
            // Escribir datos de ejemplo
            writer.println("6,Ana Martinez,5000,4532015112830366,1000");
            writer.println("7,Luis Garcia,7500,5555555555554444,500");
            writer.println("8,Carmen Lopez,3200,4111111111111111,0");
            writer.println("9,Pedro Silva,9800,4000000000000002,2000");
            writer.println("10,Maria Ruiz,6400,4242424242424242,800");
            
            writer.close();
            System.out.println("Archivo de ejemplo creado: " + rutaArchivo);
            
        } catch (IOException e) {
            System.out.println("Error creando archivo de ejemplo: " + e.getMessage());
        }
    }
}
