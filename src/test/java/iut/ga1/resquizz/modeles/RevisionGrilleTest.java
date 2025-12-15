/*
 * RevisionGrilleTest.java           26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe RevisionGrille.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class RevisionGrilleTest {

    private RevisionGrille revision;
    private final int NB_PICTOS = 5;

    /**
     * Méthode utilitaire pour injecter une fausse GrilleSST via réflexion.
     */
    private void injecterFausseGrille(Pictogramme[] pictos) {
        try {
            Constructor<GrilleSST> constructor = GrilleSST.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            GrilleSST fakeGrille = constructor.newInstance();

            Field fListe = GrilleSST.class.getDeclaredField("listePictogrammes");
            fListe.setAccessible(true);
            fListe.set(fakeGrille, pictos);

            Field fInstance = GrilleSST.class.getDeclaredField("grilleSST");
            fInstance.setAccessible(true);
            fInstance.set(null, fakeGrille);

        } catch (Exception e) {
            fail("Erreur lors de l'injection du mock GrilleSST : " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        // Création de données de test
        Pictogramme[] pictosDeTest = new Pictogramme[NB_PICTOS];
        for (int i = 0; i < NB_PICTOS; i++) {
            pictosDeTest[i] = new Pictogramme(i, "Nom" + i, "Desc" + i, "Path" + i);
        }

        // Injection du Singleton
        injecterFausseGrille(pictosDeTest);

        // Création du jeu
        revision = new RevisionGrille();
    }

    @Test
    void testInitialisation() {
        // Au début, listePlaces doit contenir des nulls (taille 5).
        assertEquals(NB_PICTOS, revision.getPlaces().size());
        for (Pictogramme p : revision.getPlaces()) {
            assertNull(p, "Les emplacements de la grille doivent être vides au départ");
        }

        // Au début, listePropositions doit contenir tous les pictos (taille 5).
        assertEquals(NB_PICTOS, revision.getPropositions().size());
        assertFalse(revision.getPropositions().contains(null), "Les propositions ne doivent pas contenir de null");
    }

    @Test
    void testPlacerPictogramme() {
        // On récupère un picto à placer et on le place
        Pictogramme aPlacer = revision.getPropositions().get(0);
        int indexCible = 2;
        revision.placerPictogramme(aPlacer, indexCible);

        // Vérification
        assertEquals(aPlacer, revision.getPlaces().get(indexCible), "Le picto doit être dans la grille à l'index 2");
        assertFalse(revision.getPropositions().contains(aPlacer), "Le picto ne doit plus être dans les propositions");
    }

    @Test
    void testDeplacerPictogrammeInterne() {
        // Test : Déplacer un picto déjà dans la grille vers une autre case
        Pictogramme p = revision.getPropositions().get(0);

        // On le place en case 1
        revision.placerPictogramme(p, 1);
        assertEquals(p, revision.getPlaces().get(1));

        // On le déplace en case 3
        revision.placerPictogramme(p, 3);

        // Vérification
        assertEquals(p, revision.getPlaces().get(3), "Le picto doit être arrivé en case 3");
        assertNull(revision.getPlaces().get(1), "L'ancienne case (1) doit être libérée (null)");
    }

    @Test
    void testRemplacementPictogramme() {
        // Test : Écraser un picto existant par un nouveau
        Pictogramme p1 = revision.getPropositions().get(0);
        Pictogramme p2 = revision.getPropositions().get(1);

        // On place P1 en case 0
        revision.placerPictogramme(p1, 0);

        // On place P2 en case 0 (à la place de P1)
        revision.placerPictogramme(p2, 0);

        // Vérifications
        assertEquals(p2, revision.getPlaces().get(0), "La case 0 contient maintenant P2");
        assertTrue(revision.getPropositions().contains(p1), "P1 doit être retourné dans les propositions");
        assertFalse(revision.getPropositions().contains(p2), "P2 n'est plus dans les propositions");
    }

    @Test
    void testRemettreDansPropositions() {
        Pictogramme p = revision.getPropositions().get(0);

        // On le place d'abord puis on le retire
        revision.placerPictogramme(p, 0);
        assertFalse(revision.getPropositions().contains(p));
        revision.remettreDansPropositions(p);

        // Vérification
        assertNull(revision.getPlaces().get(0), "La case grille doit être vide");
        assertTrue(revision.getPropositions().contains(p), "Le picto est revenu dans les propositions");
    }

    @Test
    void testCalculPoints() {
        // Placement correct
        Pictogramme p0 = trouverPictoParId(0);
        revision.placerPictogramme(p0, 0);

        // Placement incorrect
        Pictogramme p1 = trouverPictoParId(1);
        revision.placerPictogramme(p1, 2);

        // Calcul
        revision.calculPoints();

        // Vérification
        assertEquals(50, revision.getPoints());
    }

    @Test
    void testEstBonneReponse() {
        // Grille vide ou incomplète → Faux
        assertFalse(revision.estBonneReponse());

        // Grille pleine, mais avec erreurs
        // On remplit tout en décalé (ID 0 en place 1, ID 1 en place 2, etc.)
        for (int i = 0; i < NB_PICTOS; i++) {
            Pictogramme p = trouverPictoParId(i);
            revision.placerPictogramme(p, (i + 1) % NB_PICTOS);
        }
        assertFalse(revision.estBonneReponse());

        // Grille Parfaite
        for (int i = 0; i < NB_PICTOS; i++) {
            Pictogramme p = trouverPictoParId(i);
            revision.placerPictogramme(p, i); // Place i à l'index i
        }
        assertTrue(revision.estBonneReponse());
    }

    @Test
    void testEstBonneReponseIndividuelle() {
        Pictogramme p0 = trouverPictoParId(0);
        Pictogramme p1 = trouverPictoParId(1);

        // Placement correct pour 0
        revision.placerPictogramme(p0, 0);
        // Placement incorrect pour 1 (mis en 2)
        revision.placerPictogramme(p1, 2);

        assertTrue(revision.estBonneReponse(0), "L'index 0 est correct");
        assertFalse(revision.estBonneReponse(2), "L'index 2 est incorrect (contient picto 1)");
        assertFalse(revision.estBonneReponse(1), "L'index 1 est vide donc incorrect");
    }

    /**
     * Méthode pour retrouver un pictogramme spécifique dans les propositions,
     * car elles sont mélangées à l'initialisation.
     */
    private Pictogramme trouverPictoParId(int id) {
        for (Pictogramme p : revision.getPropositions()) {
            if (p.getIdentifiant() == id) {
                return p;
            }
        }
        // Recherche aussi dans la grille si déjà c'est placé
        for (Pictogramme p : revision.getPlaces()) {
            if (p != null && p.getIdentifiant() == id) {
                return p;
            }
        }
        return null;
    }
}