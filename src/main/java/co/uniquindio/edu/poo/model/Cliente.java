package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente implements IClienteObserver {

    private String idCliente;
    private String nombre;
    private String correo;
    private String contrasena;
    private Tarjeta tarjeta;
    private CuentaPuntos cuentaPuntos;
    private List<Compra> listaCompras;
    private List<String> notificaciones;
    private Cinema ownedByCinema;

    /**
     * Construye un cliente con credenciales por defecto y le asigna automáticamente su tarjeta virtual y cuenta de puntos.
     *
     * @param idCliente     identificador único del cliente.
     * @param nombre        nombre completo del cliente.
     * @param correo        correo electrónico de acceso.
     * @param ownedByCinema referencia al cine al que pertenece.
     */
    public Cliente(String idCliente, String nombre, String correo, Cinema ownedByCinema) {
        this(idCliente, nombre, correo, "1234", ownedByCinema);
    }

    /**
     * Construye un cliente completo con contraseña personalizada, tarjeta virtual activa y cuenta de puntos.
     *
     * @param idCliente     identificador único del cliente.
     * @param nombre        nombre completo del cliente.
     * @param correo        correo electrónico de acceso.
     * @param contrasena    contraseña de inicio de sesión.
     * @param ownedByCinema referencia al cine al que pertenece.
     */
    public Cliente(String idCliente, String nombre, String correo, String contrasena, Cinema ownedByCinema) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.ownedByCinema = ownedByCinema;
        this.listaCompras = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.tarjeta = new Tarjeta("TARJ-" + idCliente, 0.0, true, this, null);
        this.cuentaPuntos = new CuentaPuntos(0, this);
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public void setCuentaPuntos(CuentaPuntos cuentaPuntos) {
        this.cuentaPuntos = cuentaPuntos;
    }

    public List<Compra> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public List<String> getNotificaciones() {
        return notificaciones;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    /**
     * Valida que los datos básicos de registro del cliente sean obligatorios y tengan formato válido.
     */
    public void validarDatosRegistro() {
        if (idCliente == null || idCliente.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (correo == null || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        if (contrasena == null || contrasena.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
    }

    /**
     * Verifica si el correo y la contraseña suministrados coinciden con los del cliente.
     *
     * @param correo     correo ingresado.
     * @param contrasena contraseña ingresada.
     * @return true si las credenciales son correctas.
     */
    public boolean autenticar(String correo, String contrasena) {
        return this.correo != null && this.correo.equalsIgnoreCase(correo)
                && this.contrasena != null && this.contrasena.equals(contrasena);
    }

    /**
     * Consulta la lista de funciones disponibles en la cartelera del cine.
     *
     * @return lista de funciones programadas.
     */
    public List<Funcion> consultarCartelera() {
        if (ownedByCinema == null) {
            throw new IllegalStateException("El cliente no está asociado a ningún cine.");
        }
        return ownedByCinema.getListaFunciones();
    }

    /**
     * Procesa el pago de una compra utilizando la tarjeta virtual del cliente y la registra en el cine.
     *
     * @param compra compra previamente construida con sus detalles.
     * @return true si el pago fue aprobado.
     */
    public boolean realizarCompra(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser nula.");
        }
        compra.setCliente(this);
        compra.calcularTotal();
        boolean pagada = compra.procesarPago();
        if (pagada && ownedByCinema != null) {
            ownedByCinema.registrarCompra(compra);
        }
        return pagada;
    }

    /**
     * Solicita la cancelación de una compra propia aplicando las políticas de tiempo y reembolso.
     *
     * @param idCompra identificador de la compra a cancelar.
     * @return valor reembolsado a la tarjeta virtual.
     */
    public double solicitarCancelacionCompra(String idCompra) {
        for (Compra compra : listaCompras) {
            if (compra.getIdCompra().equals(idCompra)) {
                return compra.cancelarCompra(false);
            }
        }
        throw new IllegalArgumentException("No existe una compra con el ID: " + idCompra);
    }

    /**
     * Registra una solicitud de recarga para que sea aprobada por el administrador del cine.
     *
     * @param monto valor monetario a solicitar.
     * @return objeto Recarga pendiente de aprobación.
     */
    public Recarga solicitarRecargaTarjeta(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a recargar debe ser mayor a cero.");
        }
        if (tarjeta == null) {
            throw new IllegalStateException("El cliente no posee una tarjeta virtual asignada.");
        }
        if (ownedByCinema == null) {
            throw new IllegalStateException("El cliente no está vinculado a un cine.");
        }
        return ownedByCinema.registrarSolicitudRecarga(this, monto);
    }

    /**
     * Consulta el saldo actual disponible en la tarjeta virtual del cliente.
     *
     * @return saldo disponible en la tarjeta.
     */
    public double consultarSaldoTarjeta() {
        if (tarjeta == null) {
            throw new IllegalStateException("El cliente no tiene tarjeta virtual.");
        }
        return tarjeta.getSaldo();
    }

    /**
     * Obtiene el historial de movimientos registrados en la tarjeta virtual del cliente.
     *
     * @return lista de movimientos de compra, recarga y reembolso.
     */
    public List<String> consultarMovimientosTarjeta() {
        if (tarjeta == null || tarjeta.getHistorialMovimientos() == null) {
            return new ArrayList<>();
        }
        return tarjeta.getHistorialMovimientos().getListaMovimientos();
    }

    /**
     * Consulta la cantidad de puntos vigentes disponibles en la cuenta del cliente.
     *
     * @return saldo de puntos vigentes.
     */
    public int consultarPuntos() {
        if (cuentaPuntos == null) {
            return 0;
        }
        return cuentaPuntos.getSaldoPuntos();
    }

    /**
     * Redime puntos acumulados por un beneficio (ENTRADA, PRODUCTO o COMBO).
     *
     * @param tipoBeneficio tipo de beneficio a reclamar.
     */
    public void redimirPuntosPorBeneficio(String tipoBeneficio) {
        if (cuentaPuntos == null) {
            throw new IllegalStateException("El cliente no tiene cuenta de puntos.");
        }
        cuentaPuntos.redimirBeneficio(tipoBeneficio);
    }

    /**
     * Recibe una notificación genérica del centro de notificaciones.
     */
    @Override
    public void actualizar() {
        actualizar("Nueva notificación disponible en CinemaUQ.");
    }

    /**
     * Almacena un mensaje de notificación enviado por el sistema del cine.
     *
     * @param mensaje contenido de la notificación.
     */
    @Override
    public void actualizar(String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            notificaciones.add(mensaje);
        }
    }

    @Override
    public String toString() {
        return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", correo=" + correo + "]";
    }
}