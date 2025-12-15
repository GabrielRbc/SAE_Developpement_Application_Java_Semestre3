/*
 * ScenarioAleaPaPTest.java         26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe ScenarioAleaPaP.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class ScenarioAleaPaPTest {

    private ScenarioAleaPaP scenario;
    private Pictogramme[] pictosDeTest;

    /**
     * Méthode utilitaire pour injecter une fausse GrilleSST via réflexion.
     * Cela permet de contourner le chargement des images réelles et d'avoir des données maîtrisées.
     */
    private void injecterFausseGrille(Pictogramme[] pictos) {
        try {
            // Instancier une GrilleSST (constructeur privé)
            Constructor<GrilleSST> constructor = GrilleSST.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            GrilleSST fakeGrille = constructor.newInstance();

            // Remplir son tableau interne 'listePictogrammes'
            Field fListe = GrilleSST.class.getDeclaredField("listePictogrammes");
            fListe.setAccessible(true);
            fListe.set(fakeGrille, pictos);

            // Remplacer l'instance singleton statique 'grilleSST'
            Field fInstance = GrilleSST.class.getDeclaredField("grilleSST");
            fInstance.setAccessible(true);
            fInstance.set(null, fakeGrille);

        } catch (Exception e) {
            fail("Erreur lors de l'injection du mock GrilleSST : " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        // On fabrique 27 "faux" pictogrammes pour le test.
        // Comme ça, si le scénario demande le picto n°5, il existera bien.
        pictosDeTest = new Pictogramme[27];
        for (int i = 0; i < 27; i++) {
            // On crée un picto simple : ID, nom, description, chemin
            pictosDeTest[i] = new Pictogramme(i, "Picto" + i, "Desc" + i, "Path" + i);
        }

        // Injection de la fausse grille avant de créer le scénario
        injecterFausseGrille(pictosDeTest);

        // Création de l'instance à tester
        scenario = new ScenarioAleaPaP();
    }

    @Test
    void testGetPropositions() {
        List<Pictogramme> props = scenario.getPropositions();
        Pictogramme bonneReponse = scenario.getReponseCorrecte();

        assertNotNull(props);
        assertEquals(4, props.size(), "Il doit y avoir exactement 4 propositions");
        assertTrue(props.contains(bonneReponse), "La liste des propositions doit contenir la bonne réponse");

        // Vérification d'unicité (pas de doublons)
        long countDistinct = props.stream().distinct().count();
        assertEquals(4, countDistinct, "Les 4 propositions doivent être distinctes");
    }

    @Test
    void testVerifierReponse() {
        Pictogramme correct = scenario.getReponseCorrecte();

        // Trouver un picto incorrect (n'importe lequel qui n'est pas la bonne réponse)
        Pictogramme incorrect = null;
        for (Pictogramme p : pictosDeTest) {
            if (p.getIdentifiant() != correct.getIdentifiant()) {
                incorrect = p;
                break;
            }
        }

        assertTrue(scenario.verifierReponse(correct), "verifierReponse doit retourner true pour le bon picto");
        assertFalse(scenario.verifierReponse(incorrect), "verifierReponse doit retourner false pour un mauvais picto");
    }

    @Test
    void testCalculPoints() {
        // Bonne réponse
        scenario.calculPoints(true);
        assertEquals(100, scenario.getPoints());

        // Mauvaise réponse
        scenario.calculPoints(false);
        assertEquals(50, scenario.getPoints()); // 100 - 50 = 50

        // Encore une mauvaise
        scenario.calculPoints(false);
        assertEquals(0, scenario.getPoints());

        // Score négatif possible selon la logique actuelle (-50)
        scenario.calculPoints(false);
        assertEquals(-50, scenario.getPoints());
    }

    @Test
    void testGestionErreurs() {
        assertEquals(0, scenario.getErreurs());

        scenario.incrementerErreurs();
        assertEquals(1, scenario.getErreurs());

        scenario.incrementerErreurs();
        assertEquals(2, scenario.getErreurs());
    }

    @Test
    void testNavigationEtapes() {
        // On parcourt tout le scénario
        while (!scenario.estDerniereEtape()) {
            Pictogramme etapeAvant = scenario.getReponseCorrecte();
            boolean aAvance = scenario.etapeSuivante();

            assertTrue(aAvance, "etapeSuivante devrait retourner true tant qu'on n'est pas à la fin");
            assertNotEquals(etapeAvant, scenario.getReponseCorrecte(), "L'étape (et donc la réponse attendue) doit changer");
        }

        // On est à la dernière étape
        assertTrue(scenario.estDerniereEtape());
        assertFalse(scenario.etapeSuivante(), "etapeSuivante doit retourner false quand le scénario est fini");
    }

    @Test
    void testReinitialiser() {
        // On joue un peu pour modifier l'état
        scenario.calculPoints(true);
        scenario.incrementerErreurs();
        if (!scenario.estDerniereEtape()) {
            scenario.etapeSuivante();
        }

        // Action
        scenario.reinitialiser();

        // Vérification
        assertEquals(0, scenario.getPoints(), "Score doit être remis à 0");
        assertEquals(0, scenario.getErreurs(), "Erreurs doivent être remises à 0");
    }
}