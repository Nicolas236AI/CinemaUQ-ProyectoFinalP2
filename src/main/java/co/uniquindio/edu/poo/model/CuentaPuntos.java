package co.uniquindio.edu.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuentaPuntos {

    public static final double PESOS_POR_PUNTO = 100.0;
    public static final int PUNTOS_REDENCION_ENTRADA = 500;
    public static final int PUNTOS_REDENCION_PRODUCTO = 250;
    public static final int PUNTOS_REDENCION_COMBO = 700;

    private int saldoPuntos;
    private Cliente ownedByCliente;
    private List<Integer> puntosPorLote;
    private List<LocalDate> fechasObtencion;
    private List<LocalDate> fechasVencimiento;

    /**
     * Inicializa la cuenta de puntos de un cliente con un saldo inicial vigente por un año.
     *
     * @param saldoPuntos    puntos iniciales de la cuenta.
     * @param ownedByCliente cliente propietario de la cuenta.
     */
    public CuentaPuntos(int saldoPuntos, Cliente ownedByCliente) {
        this.ownedByCliente = ownedByCliente;
        this.puntosPorLote = new ArrayList<>();
        this.fechasObtencion = new ArrayList<>();
        this.fechasVencimiento = new ArrayList<>();
        if (saldoPuntos > 0) {
            acumularPuntosConFecha(saldoPuntos, LocalDate.now());
        }
    }

    public int getSaldoPuntos() {
        return calcularSaldoVigente(LocalDate.now());
    }

    public void setSaldoPuntos(int saldoPuntos) {
        this.puntosPorLote.clear();
        this.fechasObtencion.clear();
        this.fechasVencimiento.clear();
        if (saldoPuntos > 0) {
            acumularPuntosConFecha(saldoPuntos, LocalDate.now());
        }
    }

    public Cliente getOwnedByCliente() {
        return ownedByCliente;
    }

    public void setOwnedByCliente(Cliente ownedByCliente) {
        this.ownedByCliente = ownedByCliente;
    }

    public List<LocalDate> getFechasObtencion() {
        return fechasObtencion;
    }

    public List<LocalDate> getFechasVencimiento() {
        return fechasVencimiento;
    }

    /**
     * Calcula los puntos obtenidos según el monto pagado en una compra.
     *
     * @param montoCompra valor total pagado en la compra.
     * @return cantidad entera de puntos generados.
     */
    public int calcularPuntosPorCompra(double montoCompra) {
        if (montoCompra <= 0) {
            return 0;
        }
        return (int) Math.floor(montoCompra / PESOS_POR_PUNTO);
    }

    /**
     * Acumula puntos en la cuenta con fecha actual y vigencia de un año.
     *
     * @param puntos cantidad de puntos a acreditar.
     */
    public void acumularPuntos(int puntos) {
        acumularPuntosConFecha(puntos, LocalDate.now());
    }

    /**
     * Acumula puntos especificando la fecha de obtención y calculando su vencimiento a un año.
     *
     * @param puntos         cantidad de puntos a acreditar.
     * @param fechaObtencion fecha en la que se obtuvieron los puntos.
     */
    public void acumularPuntosConFecha(int puntos, LocalDate fechaObtencion) {
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos a acumular debe ser mayor a cero.");
        }
        if (fechaObtencion == null) {
            throw new IllegalArgumentException("La fecha de obtención no puede ser nula.");
        }
        puntosPorLote.add(puntos);
        fechasObtencion.add(fechaObtencion);
        fechasVencimiento.add(fechaObtencion.plusYears(1));
        this.saldoPuntos = calcularSaldoVigente(LocalDate.now());
    }

    /**
     * Suma únicamente los puntos que no han superado su fecha de vencimiento.
     *
     * @param fechaReferencia fecha contra la cual se evalúa la vigencia.
     * @return total de puntos disponibles y vigentes.
     */
    public int calcularSaldoVigente(LocalDate fechaReferencia) {
        int total = 0;
        for (int i = 0; i < puntosPorLote.size(); i++) {
            if (!fechaReferencia.isAfter(fechasVencimiento.get(i))) {
                total += puntosPorLote.get(i);
            }
        }
        this.saldoPuntos = total;
        return total;
    }

    /**
     * Valida que existan suficientes puntos vigentes antes de ejecutar una redención.
     *
     * @param puntosRequeridos cantidad de puntos que se desea redimir.
     * @param fechaReferencia  fecha en la que se realiza la validación.
     */
    public void validarPuntosDisponibles(int puntosRequeridos, LocalDate fechaReferencia) {
        if (puntosRequeridos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos a redimir debe ser positiva.");
        }
        int disponibles = calcularSaldoVigente(fechaReferencia);
        if (disponibles < puntosRequeridos) {
            throw new IllegalStateException("Puntos vigentes insuficientes. Disponibles: " + disponibles + ", Requeridos: " + puntosRequeridos);
        }
    }

    /**
     * Redime puntos consumiendo primero los acumulados más próximos a vencer.
     *
     * @param puntos cantidad de puntos a redimir.
     * @return true si la redención fue exitosa.
     */
    public boolean redimirPuntos(int puntos) {
        return redimirPuntosEnFecha(puntos, LocalDate.now());
    }

    /**
     * Redime puntos en una fecha específica priorizando los lotes más cercanos a expirar.
     *
     * @param puntos          cantidad de puntos a redimir.
     * @param fechaReferencia fecha en la que se solicita la redención.
     * @return true si se descontaron los puntos correctamente.
     */
    public boolean redimirPuntosEnFecha(int puntos, LocalDate fechaReferencia) {
        if (puntos <= 0 || calcularSaldoVigente(fechaReferencia) < puntos) {
            return false;
        }
        int pendientes = puntos;
        while (pendientes > 0) {
            int indiceMasProximo = -1;
            LocalDate menorFechaVencimiento = null;
            for (int i = 0; i < puntosPorLote.size(); i++) {
                if (!fechaReferencia.isAfter(fechasVencimiento.get(i)) && puntosPorLote.get(i) > 0) {
                    if (menorFechaVencimiento == null || fechasVencimiento.get(i).isBefore(menorFechaVencimiento)) {
                        menorFechaVencimiento = fechasVencimiento.get(i);
                        indiceMasProximo = i;
                    }
                }
            }
            if (indiceMasProximo == -1) {
                break;
            }
            int disponiblesLote = puntosPorLote.get(indiceMasProximo);
            if (disponiblesLote <= pendientes) {
                pendientes -= disponiblesLote;
                puntosPorLote.set(indiceMasProximo, 0);
            } else {
                puntosPorLote.set(indiceMasProximo, disponiblesLote - pendientes);
                pendientes = 0;
            }
        }
        this.saldoPuntos = calcularSaldoVigente(fechaReferencia);
        return true;
    }

    /**
     * Redime un beneficio del catálogo (ENTRADA, PRODUCTO o COMBO) validando los puntos vigentes.
     *
     * @param tipoBeneficio nombre del beneficio a redimir.
     */
    public void redimirBeneficio(String tipoBeneficio) {
        if (tipoBeneficio == null) {
            throw new IllegalArgumentException("El tipo de beneficio no puede ser nulo.");
        }
        int costo;
        switch (tipoBeneficio.trim().toUpperCase()) {
            case "ENTRADA":
                costo = PUNTOS_REDENCION_ENTRADA;
                break;
            case "PRODUCTO":
                costo = PUNTOS_REDENCION_PRODUCTO;
                break;
            case "COMBO":
                costo = PUNTOS_REDENCION_COMBO;
                break;
            default:
                throw new IllegalArgumentException("Beneficio no válido para redención: " + tipoBeneficio);
        }
        validarPuntosDisponibles(costo, LocalDate.now());
        redimirPuntos(costo);
    }

    @Override
    public String toString() {
        return "CuentaPuntos [saldoPuntos=" + getSaldoPuntos() + "]";
    }
}