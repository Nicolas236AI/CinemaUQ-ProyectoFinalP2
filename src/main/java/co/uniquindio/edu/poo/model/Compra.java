package co.uniquindio.edu.poo.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private String idCompra;
    private String fecha;
    private double total;
    private double montoReembolsado;
    private boolean reembolsada;
    private EstadoCompra estado;
    private Cliente cliente;
    private ContextoDescuento contextoDescuento;
    private List<DetalleCompra> listaDetalles;

    /**
     * Construye una instancia de Compra a partir de su patrón Builder.
     *
     * @param builder constructor con los atributos configurados de la compra.
     */
    private Compra(Builder builder) {
        this.idCompra = builder.idCompra;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.estado = (builder.estado != null) ? builder.estado : EstadoCompra.PENDIENTE;
        this.cliente = builder.cliente;
        this.contextoDescuento = builder.contextoDescuento;
        this.listaDetalles = builder.listaDetalles;
        this.reembolsada = false;
        this.montoReembolsado = 0.0;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMontoReembolsado() {
        return montoReembolsado;
    }

    public boolean isReembolsada() {
        return reembolsada;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ContextoDescuento getContextoDescuento() {
        return contextoDescuento;
    }

    public void setContextoDescuento(ContextoDescuento contextoDescuento) {
        this.contextoDescuento = contextoDescuento;
    }

    public List<DetalleCompra> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleCompra> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    /**
     * Verifica que la compra no exceda el máximo permitido de 6 entradas por transacción.
     *
     * @return true si la cantidad total de boletos es menor o igual a 6.
     */
    public boolean validarLimiteEntradas() {
        java.util.Map<Funcion, Integer> entradasPorFuncion = new java.util.HashMap<>();
        int sinFuncion = 0;

        for (DetalleCompra detalle : listaDetalles) {
            if (detalle.getBoleto() != null) {
                Funcion funcion = (detalle.getAsientoFuncionActual() != null)
                        ? detalle.getAsientoFuncionActual().getOwnedByFuncion() : null;
                if (funcion != null) {
                    int acumulado = entradasPorFuncion.getOrDefault(funcion, 0) + detalle.getCantidad();
                    if (acumulado > 6) {
                        return false;
                    }
                    entradasPorFuncion.put(funcion, acumulado);
                } else {
                    sinFuncion += detalle.getCantidad();
                    if (sinFuncion > 6) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Valida que todos los asientos incluidos en la compra estén disponibles y no hayan sido vendidos previamente.
     */
    public boolean validarDisponibilidadAsientos() {
        for (DetalleCompra detalle : listaDetalles) {
            AsientoFuncionActual af = detalle.getAsientoFuncionActual();
            if (af != null) {
                if (af.verificarExpiracionReserva() || af.getEstadoAsiento() == EstadoAsiento.DISPONIBLE) {
                    return false;
                }
                if (af.getEstadoAsiento() == EstadoAsiento.OCUPADO) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcula el valor total de la compra sumando sus detalles y aplicando la estrategia de descuento vigente.
     *
     * @return monto total calculado a pagar.
     */
    public double calcularTotal() {
        double suma = 0.0;
        if (listaDetalles != null) {
            for (DetalleCompra detalle : listaDetalles) {
                suma += detalle.calcularSubtotal();
            }
        }
        if (contextoDescuento != null && contextoDescuento.getEstrategia() != null) {
            double descuento = contextoDescuento.aplicarEstrategia(suma);
            suma -= descuento;
        }
        this.total = Math.max(0.0, suma);
        return this.total;
    }

    /**
     * Ejecuta el cobro sobre la tarjeta del cliente, confirma la ocupación de los asientos y acredita los puntos ganados.
     *
     * @return true si el pago fue aprobado y completado.
     */
    public boolean procesarPago() {
        if (!validarLimiteEntradas()) {
            this.estado = EstadoCompra.CANCELADA;
            throw new IllegalStateException("No se pueden comprar más de 6 entradas en una sola transacción.");
        }
        if (listaDetalles != null && !listaDetalles.isEmpty()) {
            validarDisponibilidadAsientos();
            if (this.total <= 0) {
                calcularTotal();
            }
        }
        if (cliente == null || cliente.getTarjeta() == null) {
            this.estado = EstadoCompra.CANCELADA;
            throw new IllegalStateException("La compra no tiene un cliente con tarjeta virtual asociada.");
        }

        cliente.getTarjeta().validarDisponibilidadPago(total);
        boolean exito = cliente.getTarjeta().debitarSaldo(total);
        if (exito) {
            this.estado = EstadoCompra.COMPLETADA;
            if (listaDetalles != null) {
                for (DetalleCompra detalle : listaDetalles) {
                    if (detalle.getAsientoFuncionActual() != null) {
                        detalle.getAsientoFuncionActual().setEstadoAsiento(EstadoAsiento.OCUPADO);
                        detalle.getAsientoFuncionActual().setDetalleCompra(detalle);
                    }
                }
            }
            if (cliente.getCuentaPuntos() != null) {
                int puntosGanados = cliente.getCuentaPuntos().calcularPuntosPorCompra(total);
                if (puntosGanados > 0) {
                    cliente.getCuentaPuntos().acumularPuntos(puntosGanados);
                }
            }
            if (!cliente.getListaCompras().contains(this)) {
                cliente.getListaCompras().add(this);
            }
            return true;
        }
        this.estado = EstadoCompra.CANCELADA;
        return false;
    }

    /**
     * Obtiene la fecha y hora de la función asociada a los boletos de esta compra.
     *
     * @return fecha y hora de la función, o null si la compra no incluye entradas.
     */
    public LocalDateTime obtenerFechaFuncion() {
        if (listaDetalles != null) {
            for (DetalleCompra detalle : listaDetalles) {
                if (detalle.getAsientoFuncionActual() != null && detalle.getAsientoFuncionActual().getOwnedByFuncion() != null) {
                    return detalle.getAsientoFuncionActual().getOwnedByFuncion().getFechaHora();
                }
            }
        }
        return null;
    }

    /**
     * Valida si la compra cumple las condiciones de estado y tiempo para ser cancelada.
     *
     * @param canceladaPorCine indica si la cancelación la ordena el administrador del cine.
     */
    public void validarCancelacion(boolean canceladaPorCine) {
        if (reembolsada) {
            throw new IllegalStateException("El reembolso para esta compra ya fue ejecutado previamente.");
        }
        if (estado != EstadoCompra.COMPLETADA) {
            throw new IllegalStateException("Solo es posible cancelar compras que estén en estado COMPLETADA.");
        }
        if (!canceladaPorCine) {
            LocalDateTime fechaFuncion = obtenerFechaFuncion();
            if (fechaFuncion != null) {
                long horasRestantes = ChronoUnit.HOURS.between(LocalDateTime.now(), fechaFuncion);
                if (horasRestantes < 2) {
                    throw new IllegalStateException("No se permite cancelar compras con menos de 2 horas de anticipación.");
                }
            }
        }
    }

    /**
     * Cancela la compra, calcula el porcentaje de devolución (100% o 80%), reembolsa a la tarjeta y libera los asientos.
     *
     * @param canceladaPorCine true si el cine canceló la función (devuelve 100%), false si cancela el cliente.
     * @return monto efectivamente reembolsado a la tarjeta virtual.
     */
    public double cancelarCompra(boolean canceladaPorCine) {
        validarCancelacion(canceladaPorCine);
        double montoADevolver = total;
        if (!canceladaPorCine) {
            LocalDateTime fechaFuncion = obtenerFechaFuncion();
            if (fechaFuncion != null) {
                long horasRestantes = ChronoUnit.HOURS.between(LocalDateTime.now(), fechaFuncion);
                if (horasRestantes <= 24) {
                    montoADevolver = total * 0.80;
                }
            }
        }
        if (cliente != null && cliente.getTarjeta() != null) {
            cliente.getTarjeta().acreditarReembolso(montoADevolver, idCompra);
        }
        if (listaDetalles != null) {
            for (DetalleCompra detalle : listaDetalles) {
                if (detalle.getAsientoFuncionActual() != null) {
                    detalle.getAsientoFuncionActual().liberarAsiento();
                    detalle.getAsientoFuncionActual().setDetalleCompra(null);
                }
            }
        }
        this.montoReembolsado = montoADevolver;
        this.reembolsada = true;
        this.estado = EstadoCompra.CANCELADA;
        return montoADevolver;
    }

    @Override
    public String toString() {
        return "Compra [idCompra=" + idCompra + ", fecha=" + fecha + ", total=" + total + ", estado=" + estado + "]";
    }

    public static class Builder {

        private String idCompra;
        private String fecha;
        private double total;
        private EstadoCompra estado;
        private Cliente cliente;
        private ContextoDescuento contextoDescuento;
        private List<DetalleCompra> listaDetalles = new ArrayList<>();

        /**
         * Asigna el identificador único de la compra.
         *
         * @param idCompra código de la compra.
         * @return instancia actual del Builder.
         */
        public Builder idCompra(String idCompra) {
            this.idCompra = idCompra;
            return this;
        }

        /**
         * Asigna la fecha en que se realiza la compra.
         *
         * @param fecha fecha de la compra.
         * @return instancia actual del Builder.
         */
        public Builder fecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        /**
         * Asigna el valor total inicial de la compra.
         *
         * @param total monto total.
         * @return instancia actual del Builder.
         */
        public Builder total(double total) {
            this.total = total;
            return this;
        }

        /**
         * Asigna el estado inicial de la compra.
         *
         * @param estado estado de la compra.
         * @return instancia actual del Builder.
         */
        public Builder estado(EstadoCompra estado) {
            this.estado = estado;
            return this;
        }

        /**
         * Asigna el cliente comprador.
         *
         * @param cliente cliente que realiza la compra.
         * @return instancia actual del Builder.
         */
        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        /**
         * Asigna el contexto de estrategia de descuento aplicable.
         *
         * @param contextoDescuento contexto de descuento.
         * @return instancia actual del Builder.
         */
        public Builder contextoDescuento(ContextoDescuento contextoDescuento) {
            this.contextoDescuento = contextoDescuento;
            return this;
        }

        /**
         * Asigna la lista completa de detalles de la compra.
         *
         * @param listaDetalles lista de ítems comprados.
         * @return instancia actual del Builder.
         */
        public Builder listaDetalles(List<DetalleCompra> listaDetalles) {
            this.listaDetalles = listaDetalles;
            return this;
        }

        /**
         * Agrega un ítem individual (boleto o confitería) a la compra en construcción.
         *
         * @param detalle detalle a agregar.
         * @return instancia actual del Builder.
         */
        public Builder agregarDetalle(DetalleCompra detalle) {
            this.listaDetalles.add(detalle);
            return this;
        }

        /**
         * Construye y retorna la instancia final de Compra.
         *
         * @return objeto Compra configurado.
         */
        public Compra build() {
            return new Compra(this);
        }
    }
}