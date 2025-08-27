// Archivo: Cliente.java (modificado)
public class Cliente {
    int ID;
    String Nombre;
    int Monto;
    String NumeroTarjeta;  // Agregamos este campo

    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
        this.NumeroTarjeta = "";
    }

    public Cliente(int ID, String Nombre, int Monto, String NumeroTarjeta) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Monto = Monto;
        this.NumeroTarjeta = NumeroTarjeta;
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
}