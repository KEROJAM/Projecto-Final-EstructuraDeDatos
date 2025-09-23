import java.time.LocalDateTime;

public class Transaccion {
    private String descripcion;
    private int monto;
    private String tipo;
    private LocalDateTime fecha;

    public Transaccion(String descripcion, int monto, String tipo, LocalDateTime fecha) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return String.format("%s - $%s - %s",
                this.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                this.monto,
                this.descripcion);
    }
}