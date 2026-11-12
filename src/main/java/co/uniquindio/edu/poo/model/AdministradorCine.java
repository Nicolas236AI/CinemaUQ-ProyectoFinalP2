
package co.uniquindio.edu.poo.model;
/**
 * Representa al administrador encargado de gestionar el cine.
 * Contiene su información personal y la referencia al cine que administra.
 */
public class AdministradorCine {

    private String idAdmin;
    private String nombre;
    private String correo;
    private Cinema ownedByCinema;

    public AdministradorCine(String idAdmin, String nombre, String correo, Cinema ownedByCinema) {
        this.idAdmin = idAdmin;
        this.nombre = nombre;
        this.correo = correo;
        this.ownedByCinema = ownedByCinema;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    @Override
    public String toString() {
        return "AdministradorCine{" +
                "idAdmin='" + idAdmin + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
