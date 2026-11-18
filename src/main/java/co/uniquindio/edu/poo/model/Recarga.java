package co.uniquindio.edu.poo.model;

public class Recarga {
    private String idRecarga;
    private double monto;
    private String fecha;
    private Tarjeta tarjeta;

    /**
     * metodo constructor de la clase Recarga para inicializar sus valores
     * @param idRecarga identificador de la recarga
     * @param monto monto a recargar
     * @param fecha fecha de la recarga
     * @param tarjeta tarjeta a recargar
     */
    public Recarga(String idRecarga, double monto, String fecha, Tarjeta tarjeta) {
        this.idRecarga = idRecarga;
        this.monto = monto;
        this.fecha = fecha;
        this.tarjeta = tarjeta;
    }

    public String getIdRecarga() {
        return idRecarga;
    }

    public void setIdRecarga(String idRecarga) {
        this.idRecarga = idRecarga;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    /**
     * metodo que sirve para ejecutar la recarga acreditando saldo a la tarjeta
     */
    public void ejecutarRecarga() {
        if (tarjeta != null) {
            tarjeta.acreditarSaldo(monto);
        }
    }

    @Override
    public String toString() {
        return "Recarga [idRecarga=" + idRecarga + ", monto=" + monto + ", fecha=" + fecha + "]";
    }
}