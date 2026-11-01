package co.uniquindio.edu.poo.model;

import java.util.List;
import java.util.ArrayList;

public class Compra {
    private String idCompra;
    private String fecha;
    private double total;
    private EstadoCompra estado;
    private Cliente cliente;
    private ContextoDescuento contextoDescuento;
    private List<DetalleCompra> listaDetalles;

    /**
     * Metodo constructor de la clase compra Builder
     * @param builder constructor con los valores de la compra
     */
    private Compra(Builder builder) {
        this.idCompra = builder.idCompra;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.estado = builder.estado;
        this.cliente = builder.cliente;
        this.contextoDescuento = builder.contextoDescuento;
        this.listaDetalles = builder.listaDetalles;
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

    public List<DetalleCompra> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleCompra> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    /**
     * Valida que el cliente no compre más de 6 entradas en esta transacción
     * recorriendo los detalles y sumando solo los que corresponden a boletos.
     * @return true si la cantidad total de entradas es menor o igual a 6.
     */
    public boolean validarLimiteEntradas() {
        int totalEntradas = 0;
        if (listaDetalles != null){
    for (DetalleCompra detalle : listaDetalles) {
           if (detalle.getBoleto() != null) {
                    totalEntradas += detalle.getCantidad();
                }
            }
        }
        return totalEntradas <= 6;
    }

    /**
     *metodo que sirve para  calucular el Subtotal de la compra y aplicar el descuento si es el caso
     * @return total calculado de la compra
     */
    public double calcularTotal() {
        double suma = 0.0;
        if (listaDetalles != null) {
            for (DetalleCompra detalle : listaDetalles) {
                suma += detalle.calcularSubtotal();
            }
        }
        if (contextoDescuento != null) {
            double descuento = contextoDescuento.aplicarEstrategia(suma);
            suma -= descuento;
        }
        this.total = suma;
        return this.total;
    }

    /**
     * metodo que sirve para validar el proceso del pago validando el limite de entradas
     * @return verdadero si el pago fue procesado con éxito
     */
    public boolean procesarPago() {
        if (!validarLimiteEntradas()) {
            this.estado = EstadoCompra.CANCELADA;
            throw new IllegalStateException("No se pueden comprar más de 6 entradas para una misma función en una sola transacción.");
        }

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
        private List<DetalleCompra> listaDetalles = new ArrayList<>();

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

        public Builder listaDetalles(List<DetalleCompra> listaDetalles) {
            this.listaDetalles = listaDetalles;
            return this;
        }

        public Builder agregarDetalle(DetalleCompra detalle) {
            this.listaDetalles.add(detalle);
            return this;
        }

        public Compra build() {
            return new Compra(this);
        }
    }
}