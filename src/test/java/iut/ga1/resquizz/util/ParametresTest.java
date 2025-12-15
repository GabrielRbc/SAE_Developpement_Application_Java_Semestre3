/*
 * ParametresTest.java                                                   22 octobre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */

package iut.ga1.resquizz.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ParametresTest {

    private Parametres param;

    @BeforeEach
    void setUp() {
        // On réinitialise l'instance avant chaque test pour éviter l'effet singleton
        Parametres.setInstance(new Parametres());
        param = Parametres.getInstance();
    }

    @Test
    void testSingletonInstance() {
        Parametres firstInstance = Parametres.getInstance();
        Parametres secondInstance = Parametres.getInstance();
        assertSame(firstInstance, secondInstance, "Les instances doivent être identiques (singleton)");
    }

    @Test
    void testChemin() {
        param._setChemin("nouveau/chemin");
        assertEquals("nouveau/chemin", param._getChemin());

        Parametres.setChemin("chemin/static");
        assertEquals("chemin/static", Parametres.getChemin());
    }

    @Test
    void testHauteurEtLargeur() {
        param._setHauteur(900);
        param._setLargeur(1600);

        assertEquals(900, param._getHauteur());
        assertEquals(1600, param._getLargeur());

        Parametres.setHauteur(800);
        Parametres.setLargeur(1400);
        assertEquals(800, Parametres.getHauteur());
        assertEquals(1400, Parametres.getLargeur());
    }

    @Test
    void testCouleurFenetreEtTitre() {
        param._setCouleurFenetre("#FFFFFF");
        param._setCouleurTitre("#000000");

        assertEquals("#FFFFFF", param._getCouleurFenetre());
        assertEquals("#000000", param._getCouleurTitre());

        Parametres.setCouleurFenetre("#111111");
        Parametres.setCouleurTitre("#222222");

        assertEquals("#111111", Parametres.getCouleurFenetre());
        assertEquals("#222222", Parametres.getCouleurTitre());
    }

    @Test
    void testCouleurLogo() {
        param._setCouleurLogo(true);
        assertTrue(param._getCouleurLogo());

        Parametres.setCouleurLogo(false);
        assertFalse(Parametres.getCouleurLogo());
    }

    @Test
    void testPoliceEtTaille() {
        param._setPolice("/chemin/police.ttf");
        param._setTaillePolice(24);

        assertEquals("/chemin/police.ttf", param._getPolice());
        assertEquals(24, param._getTaillePolice());

        Parametres.setPolice("/autre/police.ttf");
        Parametres.setTaillePolice(30);

        assertEquals("/autre/police.ttf", Parametres.getPolice());
        assertEquals(30, Parametres.getTaillePolice());
    }

    @Test
    void testCheminStockage() {
        param._setCheminStockage("nouveauData");
        assertEquals("nouveauData", param._getCheminStockage());

        Parametres.setCheminStockage("dataStatic");
        assertEquals("dataStatic", Parametres.getCheminStockage());
    }
}
