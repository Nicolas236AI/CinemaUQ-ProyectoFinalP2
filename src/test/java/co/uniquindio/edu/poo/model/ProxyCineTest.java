package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProxyCineTest {

    /**
     * Prueba que el administrador registrado en el cine pueda gestionar el sistema mediante el proxy.
     */
    @Test
    public void testAdministradorConPermisos() {
        Cinema cinema = Cinema.getInstancia();
        AdministradorCine admin = new AdministradorCine("A001", "Admin", "admin@cinemauq.com", cinema);
        cinema.setAdministradorCine(admin);

        ProxyCine proxy = new ProxyCine(admin);
        assertTrue(proxy.validarPermisos());
        assertDoesNotThrow(proxy::gestionarSistema);
    }

    /**
     * Prueba que un usuario no autorizado o un proxy sin administrador sea bloqueado con SecurityException.
     */
    @Test
    public void testUsuarioSinPermisos() {
        Cinema cinema = Cinema.getInstancia();
        cinema.setAdministradorCine(new AdministradorCine("A001", "Admin", "admin@cinemauq.com", cinema));

        ProxyCine proxyIntruso = new ProxyCine(new AdministradorCine("X999", "Intruso", "x@x.com", cinema));
        assertFalse(proxyIntruso.validarPermisos());
        assertThrows(SecurityException.class, proxyIntruso::gestionarSistema);

        ProxyCine proxyNulo = new ProxyCine(null);
        assertFalse(proxyNulo.validarPermisos());
        assertThrows(SecurityException.class, proxyNulo::gestionarSistema);
    }

    /**
     * Prueba que ServicioCineReal bloquee la ejecución cuando sus permisos operativos están desactivados.
     */
    @Test
    public void testServicioCineRealPermisosDesactivados() {
        ServicioCineReal servicio = new ServicioCineReal();
        assertDoesNotThrow(servicio::gestionarSistema);

        servicio.setPermisosActivos(false);
        assertThrows(SecurityException.class, servicio::gestionarSistema);
    }

    /**
     * Prueba las operaciones protegidas de AdministradorCine: gestión de salas y aprobación de recargas.
     */
    @Test
    public void testOperacionesAdministradorProtegidas() {
        Cinema cinema = Cinema.getInstancia();
        cinema.getListaSalas().clear();
        cinema.getSolicitudesRecarga().clear();

        AdministradorCine admin = new AdministradorCine("A001", "Admin", "admin@cinemauq.com", cinema);
        cinema.setAdministradorCine(admin);

        Sala sala = new Sala(1, 1, 20, cinema);
        admin.gestionarSalas(sala);
        assertEquals(1, cinema.getListaSalas().size());
        assertEquals(20, sala.getListaAsientos().size());

        Cliente cliente = new Cliente("C-50", "Diego", "diego@mail.com", cinema);
        Recarga recarga = cinema.registrarSolicitudRecarga(cliente, 40000.0);
        admin.aprobarRecargaTarjeta(recarga);
        assertEquals(40000.0, cliente.consultarSaldoTarjeta(), 0.01);
        assertTrue(cinema.getSolicitudesRecarga().isEmpty());
    }
}