package co.uniquindio.edu.poo.model;

public class HistorialMovimientos {
    private String idHistorial;
    private Tarjeta tarjeta;

    /**
     * metodo constructor de la clase HistorialMovimientos para inicializar sus valores
     * @param idHistorial identificador del historial
     * @param tarjeta tarjeta asociada
     */
    public HistorialMovimientos(String idHistorial, Tarjeta tarjeta) {
        this.idHistorial = idHistorial;
        this.tarjeta = tarjeta;
    }

    public String getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(String idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    /**
     * metodo que sirve para registrar un movimiento en el historial de la tarjeta
     */
    public void registrarMovimiento() {
        System.out.println("Registrando movimiento para la tarjeta: " + tarjeta.getIdTarjeta());
    }

    @Override
    public String toString() {
        return "HistorialMovimientos [idHistorial=" + idHistorial + "]";
    }
}