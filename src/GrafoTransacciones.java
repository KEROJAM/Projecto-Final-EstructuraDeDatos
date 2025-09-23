import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GrafoTransacciones {
    private Map<String, List<Transaccion>> grafo;
    private static final int UMBRAL_TRANSACCIONES = 4; // Número de transacciones para alerta
    private static final int TIEMPO_ALERTA_MINUTOS = 5; // Ventana de tiempo en minutos
    
    public GrafoTransacciones() {
        this.grafo = new HashMap<>();
    }
    
    /**
     * Agrega una transacción al grafo y verifica si hay actividad sospechosa
     * @param transaccion Transacción a agregar
     * @return true si se detecta actividad sospechosa, false en caso contrario
     */
    public boolean agregarTransaccion(Transaccion transaccion) {
        String tarjeta = transaccion.getNumeroTarjeta();
        
        // Inicializar la lista de transacciones si no existe
        grafo.putIfAbsent(tarjeta, new LinkedList<>());
        
        // Agregar la nueva transacción
        grafo.get(tarjeta).add(transaccion);
        
        // Verificar actividad sospechosa
        return verificarActividadSospechosa(tarjeta);
    }
    
    /**
     * Verifica si hay actividad sospechosa para una tarjeta dada
     * @param numeroTarjeta Número de tarjeta a verificar
     * @return true si se detecta actividad sospechosa, false en caso contrario
     */
    private boolean verificarActividadSospechosa(String numeroTarjeta) {
        List<Transaccion> transacciones = grafo.get(numeroTarjeta);
        if (transacciones == null || transacciones.size() < UMBRAL_TRANSACCIONES) {
            return false;
        }
        
        // Obtener las últimas N transacciones
        int startIndex = Math.max(0, transacciones.size() - UMBRAL_TRANSACCIONES);
        List<Transaccion> ultimasTransacciones = new ArrayList<>(
            transacciones.subList(startIndex, transacciones.size()));
        
        // Verificar si las últimas transacciones están dentro de la ventana de tiempo
        if (ultimasTransacciones.size() < UMBRAL_TRANSACCIONES) {
            return false;
        }
        
        Transaccion primera = ultimasTransacciones.get(0);
        Transaccion ultima = ultimasTransacciones.get(ultimasTransacciones.size() - 1);
        
        long minutosEntreTransacciones = Duration.between(
            primera.getFechaHora(), ultima.getFechaHora()).toMinutes();
            
        return minutosEntreTransacciones <= TIEMPO_ALERTA_MINUTOS;
    }
    
    /**
     * Obtiene el historial de transacciones de una tarjeta
     * @param numeroTarjeta Número de tarjeta
     * @return Lista de transacciones de la tarjeta
     */
    public List<Transaccion> obtenerHistorial(String numeroTarjeta) {
        return new ArrayList<>(grafo.getOrDefault(numeroTarjeta, new LinkedList<>()));
    }
    
    /**
     * Limpia las transacciones antiguas (más de 24 horas)
     */
    public void limpiarTransaccionesAntiguas() {
        LocalDateTime ahora = LocalDateTime.now();
        
        for (List<Transaccion> transacciones : grafo.values()) {
            transacciones.removeIf(transaccion -> 
                Duration.between(transaccion.getFechaHora(), ahora).toHours() > 24
            );
        }
        
        // Eliminar tarjetas sin transacciones
        grafo.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
