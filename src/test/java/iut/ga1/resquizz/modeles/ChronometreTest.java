/*
 * ChronometreTest.java                                   24 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Chronometre.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class ChronometreTest {

    private final Chronometre CHRONO = new Chronometre();

    /*
     * Initialiser javaFX vu que chronometre utilise timeline qui
     * ne peut s'exécuter que dans une instance de javafx
     */
    @BeforeAll
    static void initJFX() {
        Platform.startup(() -> {

        });
    }

    @Test
    void demarrerChrono() {
        CHRONO.demarrerChrono();
    }

    @Test
    void arreterChrono() {
        CHRONO.arreterChrono();
    }

    @Test
    void reinitialiserChrono() {
        CHRONO.demarrerChrono();
        CHRONO.reinitialiserChrono();
        CHRONO.arreterChrono();
    }

    @Test
    void getTempsEcouleProperty() {
        CHRONO.demarrerChrono();

        assertNotNull(CHRONO.getTempsEcouleProperty());
        assertTrue(CHRONO.getTempsEcouleProperty().get() >= 0); // Assure-toi que la valeur soit >= 0

        CHRONO.arreterChrono();
        assertTrue(CHRONO.getTempsEcouleProperty().get() >= 0); // Vérifie que la propriété à une valeur après l'arrêt
    }

    @Test
    void getTempsEcoule() {
        assertTrue(CHRONO.getTempsEcoule()>=0);
        CHRONO.arreterChrono();
    }
}