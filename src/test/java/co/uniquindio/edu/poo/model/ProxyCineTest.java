package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProxyCineTest {

    /**
     * Prueba que el administrador registrado en el cine pueda gestionar el sistema.
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
     * Prueba que alguien que no es el administrador registrado sea bloqueado.
     */
    @Test
    public void testUsuarioSinPermisos() {
        Cinema cinema = Cinema.getInstancia();
        cinema.setAdministradorCine(new AdministradorCine("A001", "Admin", "admin@cinemauq.com", cinema));

        ProxyCine proxy = new ProxyCine(new AdministradorCine("X999", "Intruso", "x@x.com", cinema));
        assertFalse(proxy.validarPermisos());
        assertThrows(SecurityException.class, proxy::gestionarSistema);
    }
}
