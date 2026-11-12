
package co.uniquindio.edu.poo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Funcion {

    private String idFuncion;
    private LocalDateTime fechaHora;
    private double precioBase;
    private Pelicula pelicula;
    private Sala sala;
    private List<AsientoFuncionActual> listaAsientosFuncionActual;

    public Funcion() {
        this.listaAsientosFuncionActual = new ArrayList<>();
    }

    public Funcion(String idFuncion, LocalDateTime fechaHora,
                   double precioBase, Pelicula pelicula, Sala sala) {
        this.idFuncion = idFuncion;
        this.fechaHora = fechaHora;
        this.precioBase = precioBase;
        this.pelicula = pelicula;
        this.sala = sala;
        this.listaAsientosFuncionActual = new ArrayList<>();
    }

    public String getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(String idFuncion) {
        this.idFuncion = idFuncion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<AsientoFuncionActual> getListaAsientosFuncionActual() {
        return listaAsientosFuncionActual;
    }

    public void setListaAsientosFuncionActual(
            List<AsientoFuncionActual> listaAsientosFuncionActual) {
        this.listaAsientosFuncionActual = listaAsientosFuncionActual;
    }

    @Override
    public String toString() {
        return "Funcion{" +
                "idFuncion='" + idFuncion + '\'' +
                ", fechaHora=" + fechaHora +
                ", precioBase=" + precioBase +
                ", pelicula=" + pelicula +
                ", sala=" + sala +
                '}';
    }
}
