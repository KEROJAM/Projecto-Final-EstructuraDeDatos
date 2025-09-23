import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class GrafoBancario {
    private Map<Integer, Cliente> clientes;
    private static final int TIEMPO_MAXIMO_MINUTOS = 1;

    public GrafoBancario() {
        this.clientes = new HashMap<>();
    }

    // Agregar cliente al grafo
    public void agregarCliente(Cliente cliente) {
        clientes.put(cliente.ID, cliente);
    }

    // Nuevo: Método para detectar posible fraude
    public boolean detectarPosibleFraude(Cliente cliente, LocalDateTime tiempoActual) {
        cliente.addTransaccionReciente(tiempoActual);
        LinkedList<LocalDateTime> transacciones = cliente.getTransaccionesRecientes();

        if (transacciones.size() >= 4) {
            // Se calcula la diferencia entre la primera y la última transacción
            LocalDateTime primeraTransaccion = transacciones.getFirst();
            Duration duracion = Duration.between(primeraTransaccion, tiempoActual);

            // Si 4 transacciones se hicieron en menos de 1 minuto, se considera fraude
            return duracion.toMinutes() < TIEMPO_MAXIMO_MINUTOS;
        }
        return false;
    }
}