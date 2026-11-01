package co.uniquindio.edu.poo.model;

public class Compra {
    private String idCompra;
    private String fecha;
    private double total;
    private EstadoCompra estado;
    private Cliente cliente;
    private ContextoDescuento contextoDescuento;
    private DetalleCompra detalleCompra;

    /**
     * metodo constructor de la clase Compra para inicializar sus valores usando el patron Builder
     * @param builder constructor con los valores de la compra
     */
    private Compra(Builder builder) {
        this.idCompra = builder.idCompra;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.estado = builder.estado;
        this.cliente = builder.cliente;
        this.contextoDescuento = builder.contextoDescuento;
        this.detalleCompra = builder.detalleCompra;
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

    public DetalleCompra getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(DetalleCompra detalleCompra) {
        this.detalleCompra = detalleCompra;
    }

    /**
     * metodo que sirve para calcular el total de la compra sumando el detalle y aplicando el descuento si existe
     * @return total calculado de la compra
     */
    public double calcularTotal() {
        double suma = 0.0;
        if (detalleCompra != null) {
            suma = detalleCompra.calcularSubtotal();
        }
        if (contextoDescuento != null) {
            double descuento = contextoDescuento.aplicarEstrategia(suma);
            suma -= descuento;
        }
        this.total = suma;
        return this.total;
    }

    /**
     * metodo que sirve para procesar el pago de la compra
     * @return verdadero si el pago fue procesado con exito
     */
    public boolean procesarPago() {
        if (cliente != null && cliente.getTarjeta() != null) {
            boolean exito = cliente.getTarjeta().debitarSaldo(total);
            if (exito) {
                this.estado = EstadoCompra.COMPLETADA;
                return true;
            }
        }
        this.estado = EstadoCompra.CANCELADA;
        return false;
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
        private DetalleCompra detalleCompra;

        public Builder idCompra(String idCompra) {
            this.idCompra = idCompra;
            return this;
        }

        public Builder fecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder total(double total) {
            this.total = total;
            return this;
        }

        public Builder estado(EstadoCompra estado) {
            this.estado = estado;
            return this;
        }

        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder contextoDescuento(ContextoDescuento contextoDescuento) {
            this.contextoDescuento = contextoDescuento;
            return this;
        }

        public Builder detalleCompra(DetalleCompra detalleCompra) {
            this.detalleCompra = detalleCompra;
            return this;
        }

        public Compra build() {
            return new Compra(this);
        }
    }
}