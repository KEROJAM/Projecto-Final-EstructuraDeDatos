import java.util.Stack;

public class Cliente {
    public int ID;
    public String Nombre;
    public int Monto;
    public String NumeroTarjeta;
    public int montoAhorros;
    public boolean sesionActiva;
    public String contraseña;
    public Stack<String> pilaHistorial;
    private static GrafoTransacciones grafoTransacciones = new GrafoTransacciones();
    private boolean posibleFraude; // Bandera para indicar posible actividad fraudulenta
    public transient BancoUI ui; // Referencia a la interfaz de usuario (transient para evitar problemas de serialización)
    private boolean tarjetaBloqueada; // Bandera para indicar si la tarjeta está bloqueada

    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
        this.NumeroTarjeta = "";
        this.sesionActiva = false;
        this.contraseña = "";
        this.pilaHistorial = new Stack<>(); // AÑADIDO: Se inicializa el historial
        this.tarjetaBloqueada = false;
    }

    public Cliente(int ID, String Nombre, int Monto, String NumeroTarjeta, String contraseña) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Monto = Monto;
        this.NumeroTarjeta = NumeroTarjeta;
        this.sesionActiva = false;
        this.contraseña = contraseña;
        this.pilaHistorial = new Stack<>(); // AÑADIDO: Se inicializa el historial
        this.tarjetaBloqueada = false;
    }

    public Cliente(int monto, String transaccion, int i, String s) {
    }

    // AÑADIDO: Método para acceder al historial del cliente
    public Stack<String> getPilaHistorial() {
        return this.pilaHistorial;
    }

    public boolean Depositar(int monto) {
        if (this.tarjetaBloqueada) {
            System.out.println("La tarjeta está bloqueada");
            return false;
        }
        
        Transaccion transaccion = new Transaccion(
            "DEP-" + System.currentTimeMillis(),
            this.NumeroTarjeta,
            monto,
            "DEPOSITO"
        );
        
        // Verificar posible fraude
        if (grafoTransacciones.agregarTransaccion(transaccion)) {
            this.posibleFraude = true;
            System.out.println("¡ADVERTENCIA: Se detectó actividad inusual en su cuenta!");
        }
        
        this.Monto += monto;
        return this.posibleFraude;
    }

    public boolean Transferir(int monto) {
        if (this.tarjetaBloqueada) {
            System.out.println("La tarjeta está bloqueada");
            return false;
        }
        
        if (monto > this.Monto) {
            System.out.println("Fondos insuficientes");
            return false;
        }
        
        Transaccion transaccion = new Transaccion(
            "TRA-" + System.currentTimeMillis(),
            this.NumeroTarjeta,
            monto,
            "TRANSFERENCIA"
        );
        
        // Verificar posible fraude
        if (grafoTransacciones.agregarTransaccion(transaccion)) {
            this.posibleFraude = true;
            System.out.println("¡ADVERTENCIA: Se detectó actividad inusual en su cuenta!");
        }
        
        this.Monto -= monto;
        this.pilaHistorial.push("Transferencia de $" + monto);
        return this.posibleFraude;
    }

    public boolean validarContraseña(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }
    
    public boolean hayPosibleFraude() {
        return this.posibleFraude;
    }
    
    public void resetearAlertaFraude() {
        this.posibleFraude = false;
    }
    
    // Getters y setters para los atributos privados
    public int getID() { return ID; }
    public String getNombre() { return Nombre; }
    public int getMonto() { return Monto; }
    public int getMontoAhorros() { return montoAhorros; }
    public void setMontoAhorros(int monto) { this.montoAhorros = monto; }
    public void setSesionActiva(boolean activa) { this.sesionActiva = activa; }

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
    
    // Establece la referencia a la interfaz de usuario
    public void setUI(BancoUI ui) {
        this.ui = ui;
    }
    
    // Muestra una alerta de fraude en la interfaz de usuario si está disponible
    private void mostrarAlertaFraude(String titulo, String mensaje) {
        if (ui != null) {
            ui.mostrarAlertaFraude(titulo, mensaje);
        }
    }

    public void bloquearTarjeta() {
        this.tarjetaBloqueada = true;
    }

    public void desbloquearTarjeta() {
        this.tarjetaBloqueada = false;
    }

    public boolean isTarjetaBloqueada() {
        return this.tarjetaBloqueada;
    }
}
