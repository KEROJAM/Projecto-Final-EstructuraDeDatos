public class Cliente {
    int ID;
    String Nombre;
    int Monto;
    String NumeroTarjeta;
    public int montoAhorros;
    boolean sesionActiva;
    String contraseña;
    Stack<String> pilaHistorial; // AÑADIDO: Cada cliente tiene su propio historial

    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
        this.NumeroTarjeta = "";
        this.sesionActiva = false;
        this.contraseña = "";
        this.pilaHistorial = new Stack<>(); // AÑADIDO: Se inicializa el historial
    }

    public Cliente(int ID, String Nombre, int Monto, String NumeroTarjeta, String contraseña) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Monto = Monto;
        this.NumeroTarjeta = NumeroTarjeta;
        this.sesionActiva = false;
        this.contraseña = contraseña;
        this.pilaHistorial = new Stack<>(); // AÑADIDO: Se inicializa el historial
    }

    public Cliente(int monto, String transaccion, int i, String s) {
    }

    // AÑADIDO: Método para acceder al historial del cliente
    public Stack<String> getPilaHistorial() {
        return this.pilaHistorial;
    }

    public void Depositar(int Monto){
        this.Monto += Monto;
    }

    public void Transferir(int Monto){
        this.Monto -= Monto;
    }

    public boolean validarContraseña(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }

    public void cambiarContraseña(String nuevaContraseña) {
        this.contraseña = nuevaContraseña;
    }

    public boolean validarTarjetaYNombre(String tarjeta, String nombre) {
        return this.NumeroTarjeta.equals(tarjeta) && this.Nombre.equalsIgnoreCase(nombre);
    }

    public String getNumeroTarjeta() {
        return NumeroTarjeta;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean coincideConNombre(String nombre) {
        return this.Nombre.equalsIgnoreCase(nombre);
    }

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