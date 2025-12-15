/*
 * GestionParametresTest.java       26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GestionParametresTest {

    @TempDir
    Path dossierTemporaire;

    private String ancienCheminStockage;

    @BeforeEach
    void setUp() {
        // Sauvegarde du chemin actuel
        ancienCheminStockage = Parametres.getCheminStockage();

        // Réinitialisation des Singletons
        Parametres.setInstance(new Parametres());
        GestionClassement.setInstance(new GestionClassement());

        // Redirection vers le dossier temporaire sur la NOUVELLE instance
        Parametres.setCheminStockage(dossierTemporaire.toString());
    }

    @AfterEach
    void tearDown() {
        // Restauration du chemin d'origine
        Parametres.setCheminStockage(ancienCheminStockage);
    }

    @Test
    void testSauvegardeEtChargementParametres() {
        Parametres.setHauteur(1080.0);
        Parametres.setCouleurFenetre("#FF0000");

        GestionParametres.sauvegarderParametres();

        Path fichier = dossierTemporaire.resolve("parametres.ser");
        assertTrue(Files.exists(fichier), "Le fichier parametres.ser doit être créé dans le dossier temporaire");

        // Reset pour vérifier le chargement
        Parametres.setInstance(new Parametres());
        // Attention : new Parametres() remet le chemin à "data", il faut le remettre sur temp pour charger le bon fichier !
        Parametres.setCheminStockage(dossierTemporaire.toString());

        assertEquals(720.0, Parametres.getHauteur());

        GestionParametres.chargerParametres();

        assertEquals(1080.0, Parametres.getHauteur());
        assertEquals("#FF0000", Parametres.getCouleurFenetre());
    }

    @Test
    void testSauvegardeEtChargementClassement() {
        GestionClassement.setPointGrille(500);
        GestionClassement.setChronoGrille(12345L);

        GestionParametres.sauvegarderClassement();

        Path fichier = dossierTemporaire.resolve("classement.ser");
        assertTrue(Files.exists(fichier), "Le fichier classement.ser doit être créé");

        GestionClassement.setInstance(new GestionClassement());
        assertEquals(Integer.MIN_VALUE, GestionClassement.getPointGrille());

        GestionParametres.chargerClassement();

        assertEquals(500, GestionClassement.getPointGrille());
        assertEquals(12345L, GestionClassement.getChronoGrille());
    }

    @Test
    void testChargementFichiersInexistants() {
        assertDoesNotThrow(() -> GestionParametres.chargerParametres());
        assertDoesNotThrow(() -> GestionParametres.chargerClassement());

        assertNotNull(Parametres.getInstance());
        assertNotNull(GestionClassement.getInstance());
        assertEquals(720.0, Parametres.getHauteur());
    }
}