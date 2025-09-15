// Archivo: Cliente.java (modificado)
public class Cliente {
    int ID;
    String Nombre;
    int Monto;
    String NumeroTarjeta;
    boolean sesionActiva;  // Nuevo campo para controlar el estado de sesión

    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
        this.NumeroTarjeta = "";
        this.sesionActiva = false;
    }

    public Cliente(int ID, String Nombre, int Monto, String NumeroTarjeta) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Monto = Monto;
        this.NumeroTarjeta = NumeroTarjeta;
        this.sesionActiva = false;
    }

    public void Depositar(int Monto){
        this.Monto += Monto;
    }

    public void Transferir(int Monto){
        this.Monto -= Monto;
    }

    public boolean validarTarjetaYNombre(String tarjeta, String nombre) {
        return this.NumeroTarjeta.equals(tarjeta) && this.Nombre.equalsIgnoreCase(nombre);
    }

    public String getNumeroTarjeta() {
        return NumeroTarjeta;
    }

    // Método para verificar si coincide con nombre completo
    public boolean coincideConNombre(String nombre) {
        return this.Nombre.equalsIgnoreCase(nombre);
    }

    // Métodos para control de sesión
    public boolean isSesionActiva() {
        return sesionActiva;
    }

    public void iniciarSesion() {
        this.sesionActiva = true;
    }

    public void cerrarSesion() {
        this.sesionActiva = false;
    }
}