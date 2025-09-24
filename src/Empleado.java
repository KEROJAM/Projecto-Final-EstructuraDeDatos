public class Empleado {
    private String usuario;
    private String contrasena;
    private boolean esAdmin;

    public Empleado(String usuario, String contrasena, boolean esAdmin) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.esAdmin = esAdmin;
    }

    // Getters
    public String getUsuario() {
        return usuario;
    }

    public boolean validarCredenciales(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    public boolean esAdmin() {
        return esAdmin;
    }
}
