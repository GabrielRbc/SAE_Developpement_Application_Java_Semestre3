/*
 * GestionClassementTest.java       26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestionClassementTest {

    @BeforeEach
    void setUp() {
        // Réinitialisation du Singleton avant chaque test
        GestionClassement.setInstance(new GestionClassement());
    }

    @Test
    void testValeursInitiales() {
        // Les points sont initialisés au minimum
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointGrille());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointQCMPicto());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointQCMDesc());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointQCMAlea());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointScenarioFC());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointScenarioAlea());

        // Les chronos sont initialisés au maximum
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoGrille());
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoQCMPicto());
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoQCMDesc());
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoQCMAlea());
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoScenarioFC());
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoScenarioAlea());
    }

    @Test
    void testSetGetPoints() {
        GestionClassement.setPointGrille(100);
        assertEquals(100, GestionClassement.getPointGrille());

        GestionClassement.setPointQCMPicto(50);
        assertEquals(50, GestionClassement.getPointQCMPicto());

        GestionClassement.setPointScenarioFC(200);
        assertEquals(200, GestionClassement.getPointScenarioFC());

        // Vérifie que modifier l'un ne change pas les autres
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointQCMDesc());
    }

    @Test
    void testSetGetChronos() {
        GestionClassement.setChronoGrille(12000L);
        assertEquals(12000L, GestionClassement.getChronoGrille());

        GestionClassement.setChronoQCMAlea(5000L);
        assertEquals(5000L, GestionClassement.getChronoQCMAlea());

        // Vérifie que modifier l'un ne change pas les autres
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoScenarioAlea());
    }

    @Test
    void testSingleton() {
        GestionClassement instance1 = GestionClassement.getInstance();
        assertNotNull(instance1);

        GestionClassement.setPointGrille(999);

        // Une deuxième récupération doit voir les mêmes données
        GestionClassement instance2 = GestionClassement.getInstance();
        assertEquals(999, instance2._getPointGrille());
        assertSame(instance1, instance2, "getInstance doit retourner la même référence objet");
    }

    @Test
    void testResetCategorie() {
        // Préparation : on met des scores partout
        GestionClassement.setPointGrille(100);
        GestionClassement.setChronoGrille(10L);

        GestionClassement.setPointQCMPicto(50); // Celui-ci ne doit pas bouger

        // Action : Reset uniquement "grille"
        try {
            GestionClassement.resetCategorie("grille");
        } catch (Exception e) {
            // On ignore l'erreur de sauvegarde fichier ici, on teste juste la mémoire
            System.out.println("Avertissement test : sauvegarde fichier échouée (normal en test unitaire)");
        }

        // Vérification : Grille est revenue aux valeurs par défaut
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointGrille(), "Points grille reset");
        assertEquals(Long.MAX_VALUE, GestionClassement.getChronoGrille(), "Chrono grille reset");

        // Vérification : QCM Picto n'a pas bougé
        assertEquals(50, GestionClassement.getPointQCMPicto(), "Les autres catégories ne doivent pas être affectées");
    }
}