/*
 * GrilleSSTTest.java              01 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import org.junit.jupiter.api.Test;
import iut.ga1.resquizz.util.Pictogramme;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe GrilleSST.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class GrilleSSTTest {

    @Test
    void getGrilleSST() {
        GrilleSST grille = GrilleSST.getGrilleSST();
        assertNotNull(grille);
    }

    @Test
    void getPictogrammes() {
        Pictogramme[] listePicto =  GrilleSST.getGrilleSST().getPictogrammes();
        assertNotNull(listePicto);
        assertEquals(27, listePicto.length);
        for (Pictogramme p : listePicto) {
            assertNotNull(p);
        }
    }

    @Test
    void isBienPositionne() {
        assertEquals(0, GrilleSST.getGrilleSST().getPictogrammes()[0].getIdentifiant());
        assertEquals(26, GrilleSST.getGrilleSST().getPictogrammes()[26].getIdentifiant());
    }
}