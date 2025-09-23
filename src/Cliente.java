import java.util.LinkedList;
import java.time.LocalDateTime;

public class Cliente {
    int ID;
    String Nombre;
    int Monto;
    String NumeroTarjeta;
    boolean sesionActiva;
    String contraseña;
    public HistorialMovimientos historial; // Nuevo: Ahora es una clase
    private LinkedList<LocalDateTime> transaccionesRecientes; // Nuevo: para detección de fraude

    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
        this.NumeroTarjeta = "";
        this.sesionActiva = false;
        this.contraseña = "";
        this.historial = new HistorialMovimientos();
        this.transaccionesRecientes = new LinkedList<>();
    }

    public Cliente(int ID, String Nombre, int Monto, String NumeroTarjeta, String contraseña) {
        this.ID = ID;
        this.Nombre = Monto; // Monto no se usa aquí, creo que era un error.
        this.Nombre = Nombre;
        this.Monto = Monto;
        this.NumeroTarjeta = NumeroTarjeta;
        this.sesionActiva = false;
        this.contraseña = contraseña;
        this.historial = new HistorialMovimientos();
        this.transaccionesRecientes = new LinkedList<>();
    }

    public void Depositar(int Monto){
        this.Monto += Monto;
    }

    public void Transferir(int Monto){
        this.Monto -= Monto;
    }

    public void Retirar(int Monto){
        this.Monto -= Monto;
    }

    public boolean validarContraseña(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }

    public String getNumeroTarjeta() {
        return NumeroTarjeta;
    }

    public String getNombre() {
        return Nombre;
    }

    // Nuevo: Método para añadir un movimiento con fecha y hora
    public void addMovimiento(String descripcion, int monto, String tipo) {
        historial.add(descripcion, monto, tipo, LocalDateTime.now());
    }

    // Nuevo: Método para registrar una transacción reciente
    public void addTransaccionReciente(LocalDateTime tiempo) {
        transaccionesRecientes.add(tiempo);
        if (transaccionesRecientes.size() > 4) { // Limitar a las 4 últimas transacciones
            transaccionesRecientes.removeFirst();
        }
    }

    // Nuevo: Método para obtener las transacciones recientes
    public LinkedList<LocalDateTime> getTransaccionesRecientes() {
        return transaccionesRecientes;
    }
}