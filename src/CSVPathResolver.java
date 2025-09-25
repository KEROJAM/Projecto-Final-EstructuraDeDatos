import java.io.File;

/**
 * Utilidad para resolver la ruta del archivo CSV de manera que funcione en cualquier equipo.
 * Esta clase busca el archivo clientes.csv en diferentes ubicaciones posibles.
 */
public class CSVPathResolver {
    
    /**
     * Obtiene la ruta del archivo CSV de manera que funcione en cualquier equipo.
     * Busca el archivo clientes.csv en diferentes ubicaciones:
     * 1. Directorio actual de trabajo
     * 2. Carpeta src/
     * 3. Mismo directorio que las clases compiladas
     * 
     * @return La ruta del archivo CSV más apropiada
     */
    public static String obtenerRutaCSV() {
        try {
            // Opción 1: Buscar en el directorio actual de trabajo
            File csvEnDirectorioActual = new File("clientes.csv");
            if (csvEnDirectorioActual.exists()) {
                System.out.println("CSV encontrado en directorio actual: " + csvEnDirectorioActual.getAbsolutePath());
                return "clientes.csv";
            }
            
            // Opción 2: Buscar en la carpeta src/
            File csvEnSrc = new File("src/clientes.csv");
            if (csvEnSrc.exists()) {
                System.out.println("CSV encontrado en src/: " + csvEnSrc.getAbsolutePath());
                return "src/clientes.csv";
            }
            
            // Opción 3: Buscar relativo al directorio de las clases
            try {
                String rutaClase = CSVPathResolver.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                File directorioClase = new File(rutaClase).getParentFile();
                File csvEnDirectorioClase = new File(directorioClase, "clientes.csv");
                if (csvEnDirectorioClase.exists()) {
                    System.out.println("CSV encontrado en directorio de clases: " + csvEnDirectorioClase.getAbsolutePath());
                    return csvEnDirectorioClase.getAbsolutePath();
                }
            } catch (Exception e) {
                System.out.println("No se pudo determinar el directorio de clases: " + e.getMessage());
            }
            
            // Opción 4: Buscar en el directorio padre (por si se ejecuta desde src/)
            File csvEnDirectorioPadre = new File("../clientes.csv");
            if (csvEnDirectorioPadre.exists()) {
                System.out.println("CSV encontrado en directorio padre: " + csvEnDirectorioPadre.getAbsolutePath());
                return "../clientes.csv";
            }
            
            // Si no se encuentra en ningún lado, usar ruta por defecto
            System.out.println("CSV no encontrado, usando ruta por defecto: clientes.csv");
            return "clientes.csv";
            
        } catch (Exception e) {
            System.out.println("Error al buscar CSV, usando ruta por defecto: " + e.getMessage());
            return "clientes.csv";
        }
    }
    
    /**
     * Verifica si el archivo CSV existe en la ruta especificada.
     * 
     * @param ruta La ruta del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     */
    public static boolean existeCSV(String ruta) {
        File archivo = new File(ruta);
        return archivo.exists() && archivo.isFile();
    }
    
    /**
     * Crea el directorio padre si no existe para la ruta especificada.
     * 
     * @param ruta La ruta del archivo
     * @return true si el directorio existe o se creó exitosamente
     */
    public static boolean crearDirectorioSiNoExiste(String ruta) {
        try {
            File archivo = new File(ruta);
            File directorioPadre = archivo.getParentFile();
            if (directorioPadre != null && !directorioPadre.exists()) {
                return directorioPadre.mkdirs();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear directorio: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método de prueba para verificar la funcionalidad.
     */
    public static void main(String[] args) {
        System.out.println("=== Prueba de CSVPathResolver ===");
        String rutaCSV = obtenerRutaCSV();
        System.out.println("Ruta CSV seleccionada: " + rutaCSV);
        System.out.println("¿Existe el archivo?: " + existeCSV(rutaCSV));
        
        // Mostrar directorio de trabajo actual
        System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
    }
}
