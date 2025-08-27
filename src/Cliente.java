public class Cliente {
    int ID;
    String Nombre;
    int Monto;
    public Cliente() {
        this.ID = 0;
        this.Nombre = "";
        this.Monto = 0;
    }

    public void Depositar(int Monto){
        this.Monto += Monto;
    }

    public void Transferir(int Monto){
        this.Monto -= Monto;
    }
}
