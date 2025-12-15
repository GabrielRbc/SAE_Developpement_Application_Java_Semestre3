/*
 * ScenarioFixeCompletTest.java         24 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe ScenarioFixeComplet.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class ScenarioFixeCompletTest {

    private ScenarioFixeComplet scenario;

    @BeforeEach
    void setUp() {
        // ⚠️ Le constructeur charge un scénario aléatoire depuis un JSON réel.
        // Le test vérifiera seulement les comportements déterministes.
        scenario = new ScenarioFixeComplet();
    }

    // -----------------------------------------------------------
    // TEST 1 : Le constructeur initialise correctement l’objet
    // -----------------------------------------------------------
    @Test
    void testConstructeurInitialisation() {
        assertNotNull(scenario.getDescriptionScenario(),
                "La description du scénario ne doit pas être nulle.");

        assertNotNull(scenario.getPropositions(),
                "La liste des propositions ne doit pas être nulle.");

        assertFalse(scenario.getPropositions().isEmpty(),
                "La liste des propositions doit contenir des pictogrammes.");

        assertNotNull(scenario.getSequenceCorrecte(),
                "La séquence correcte ne doit pas être nulle.");

        assertFalse(scenario.getSequenceCorrecte().isEmpty(),
                "La séquence correcte doit contenir au moins un pictogramme.");

        assertEquals(27, scenario.getPropositions().size(),
                "La grille SST contient 27 pictogrammes fixés dans GrilleSST.");
    }

    // -----------------------------------------------------------
    // TEST 2 : Score avec séquence entièrement correcte
    // -----------------------------------------------------------
    @Test
    void testCalculPointsSequenceCorrecte() {
        List<Pictogramme> sequenceUtilisateur =
                new ArrayList<>(scenario.getSequenceCorrecte());

        scenario.calculPoints(sequenceUtilisateur);

        int expected = sequenceUtilisateur.size() * 100;
        assertEquals(expected, scenario.getPoints(),
                "Une séquence 100% correcte doit rapporter 100 points par pictogramme.");
    }

    // -----------------------------------------------------------
    // TEST 3 : Score avec un pictogramme incorrect
    // -----------------------------------------------------------
    @Test
    void testCalculPointsAvecErreur() {
        List<Pictogramme> sequenceCorrecte = scenario.getSequenceCorrecte();
        List<Pictogramme> sequenceUtilisateur = new ArrayList<>(sequenceCorrecte);

        // Remplace un pictogramme par un autre (faux)
        Pictogramme faux = scenario.getPropositions().get(0);
        if (faux.equals(sequenceCorrecte.get(0))) {
            faux = scenario.getPropositions().get(1);
        }
        sequenceUtilisateur.set(0, faux);

        scenario.calculPoints(sequenceUtilisateur);

        int expected = (sequenceCorrecte.size() - 1) * 100 - 50;
        assertEquals(expected, scenario.getPoints(),
                "Une erreur doit retirer 50 points.");
    }

    // -----------------------------------------------------------
    // TEST 4 : Score avec séquence trop courte (pictogrammes manquants)
    // -----------------------------------------------------------
    @Test
    void testCalculPointsSequenceTropCourte() {
        int taille = scenario.getSequenceCorrecte().size();

        // L'utilisateur donne une séquence vide
        List<Pictogramme> sequenceUtilisateur = new ArrayList<>();

        scenario.calculPoints(sequenceUtilisateur);

        int expected = -50 * taille;
        assertEquals(expected, scenario.getPoints(),
                "Chaque pictogramme manquant doit retirer 50 points.");
    }

    // -----------------------------------------------------------
    // TEST 5 : Les propositions contiennent tous les pictogrammes
    // -----------------------------------------------------------
    @Test
    void testPropositionsContiennentTousPictogrammes() {
        List<Pictogramme> props = scenario.getPropositions();
        Pictogramme[] grille = GrilleSST.getGrilleSST().getPictogrammes();

        for (Pictogramme p : grille) {
            assertTrue(props.contains(p),
                    "Les propositions doivent contenir tous les pictogrammes de la grille SST.");
        }
    }
}
