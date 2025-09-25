import java.time.LocalDateTime;

public class Transaccion {
    private String idTransaccion;
    private String numeroTarjeta;
    private float monto;
    private LocalDateTime fechaHora;
    private String tipo; // "DEPOSITO", "RETIRO", "TRANSFERENCIA"
    
    public Transaccion(String idTransaccion, String numeroTarjeta, float monto, String tipo) {
        this.idTransaccion = idTransaccion;
        this.numeroTarjeta = numeroTarjeta;
        this.monto = monto;
        this.tipo = tipo;
        this.fechaHora = LocalDateTime.now();
    }
    
    // Getters
    public String getIdTransaccion() { return idTransaccion; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public double getMonto() { return monto; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getTipo() { return tipo; }
    
    @Override
    public String toString() {
        return String.format("Transacción %s: %s de $%.2f el %s", 
            idTransaccion, tipo, monto, fechaHora.toString());
    }
}
