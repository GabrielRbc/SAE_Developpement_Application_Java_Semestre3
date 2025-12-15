/*
 * QCMTest.java                   26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe QCM.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class QCMTest {

    private QCM qcm;
    private Pictogramme[] liste10;

    /** Injection d’un faux singleton GrilleSST */
    private void injectFakeGrille(Pictogramme[] pictos) {
        try {
            // Récupérer le constructeur privé
            Constructor<GrilleSST> c = GrilleSST.class.getDeclaredConstructor();
            c.setAccessible(true);

            // Créer une instance même si constructeur privé
            GrilleSST fake = c.newInstance();

            // Remplacer le tableau interne 'listePictogrammes'
            Field fListe = GrilleSST.class.getDeclaredField("listePictogrammes");
            fListe.setAccessible(true);
            fListe.set(fake, pictos);

            // Remplacer l’instance du singleton
            Field instance = GrilleSST.class.getDeclaredField("grilleSST");
            instance.setAccessible(true);
            instance.set(null, fake);

        } catch (Exception e) {
            fail("Impossible d’injecter la fausse grille : " + e);
        }
    }

    @BeforeEach
    void setUp() {

        // 🔹 10 Pictogrammes avec identifiant int
        liste10 = new Pictogramme[10];
        for (int i = 0; i < 10; i++) {
            liste10[i] = new Pictogramme(
                    i,                    // identifiant INT
                    "Nom" + (i + 1),
                    "desc" + (i + 1),
                    "img" + (i + 1)
            );
        }

        // Injection du faux singleton
        injectFakeGrille(liste10);

        qcm = new QCM();
        qcm.setPictogrammesJeu(Arrays.asList(liste10));
    }

    @Test
    void testRemplirGrille10Elements() {
        assertEquals(10, qcm.getPropositions().size());
    }

    @Test
    void testEstBonneReponseCorrectePour10() {
        for (int i = 0; i < 10; i++) {
            assertTrue(qcm.estBonneReponse(i, liste10[i]));
        }
    }

    @Test
    void testEstBonneReponseIncorrecte() {
        assertFalse(qcm.estBonneReponse(0, liste10[1]));
        assertFalse(qcm.estBonneReponse(5, liste10[0]));
        assertFalse(qcm.estBonneReponse(8, null));
    }

    @Test
    void testCalculPointsParfait() {
        qcm.getPlaces().addAll(Arrays.asList(liste10));
        qcm.calculPoints();
        assertEquals(10 * 100, qcm.getPoints());
        assertTrue(qcm.estBonneReponse());
    }

    @Test
    void testCalculPointsMoitieMauvais() {
        // 5 bons / 5 mauvais
        for (int i = 0; i < 5; i++) qcm.getPlaces().add(liste10[i]);
        for (int i = 0; i < 5; i++) qcm.getPlaces().add(liste10[(i + 1) % 10]);

        qcm.calculPoints();

        assertEquals((5 * 100) - (5 * 50), qcm.getPoints());
        assertFalse(qcm.estBonneReponse());
    }

    @Test
    void testCalculPointsPlacesVide() {
        qcm.calculPoints();
        assertEquals(0, qcm.getPoints());
    }
}
