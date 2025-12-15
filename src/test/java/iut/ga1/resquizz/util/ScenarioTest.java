/*
 * ScenarioTest.java               24 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTest {

    private Scenario scenario;

    // Pictogrammes factices pour tests sûrs (pas présents dans le fichier de base)
    private static final List<Integer> PICTOS_TEST_1 = Arrays.asList(1, 10, 3, 4, 0);
    private static final List<Integer> PICTOS_TEST_2 = Arrays.asList(1, 2, 3, 0, 5);

    // Pictogrammes réels déjà présents dans le fichier de base (pour tests anti-doublons)
    private static final List<Integer> PICTOS_BASE = Arrays.asList(1, 11, 2, 21, 3);

    private static final List<Integer> PICTOS_FAUX = Arrays.asList(1, 2, 3, 4); // moins de 5
    private static final List<Integer> PICTOS_HORS_LIMITE = Arrays.asList(-1, 0, 5, 10, 27);

    private static final String TEXTE_SCENARIO_1 = "Scénario test 1";
    private static final String TEXTE_SCENARIO_2 = "Scénario test 2";
    private static final String TEXTE_SCENARIO_DOUBLON = "Scénario doublon";

    @TempDir
    Path tempDir;

    private File dataDir;
    private File fichierData;

    @BeforeEach
    void setup() throws Exception {

        // Répertoire data temporaire
        dataDir = tempDir.resolve("data").toFile();
        dataDir.mkdirs();

        // Création fichier JSON avec contenu vide
        fichierData = new File(dataDir, "scenarioFC.json");
        try (FileWriter fw = new FileWriter(fichierData)) {
            fw.write("{}");
        }

        // INDISPENSABLE : instancier Scenario avec ton vrai fichier
        scenario = new Scenario(fichierData.getAbsolutePath());
    }

    // ----------------------------------------
    // Tests avec pictos factices (tests sûrs)
    // ----------------------------------------

    @Test
    void testPictosNonValides() {
        boolean result = scenario.ajouterScenario(TEXTE_SCENARIO_1, PICTOS_FAUX);
        assertFalse(result, "Doit refuser si le scénario ne contient pas exactement 5 pictogrammes");
    }

    @Test
    void testAjoutScenarioNeuf() {
        boolean result = scenario.ajouterScenario(TEXTE_SCENARIO_1, PICTOS_TEST_1);
        assertTrue(result, "Doit accepter un scénario avec 5 pictos valides");
    }

    @Test
    void testAjoutScenarioDifférent() {
        scenario.ajouterScenario("Scénario 1", PICTOS_TEST_1);

        boolean result = scenario.ajouterScenario("Scénario 2", PICTOS_TEST_2);
        assertTrue(result, "Doit accepter un scénario avec des pictogrammes différents");
    }

    // ----------------------------------------
    // Tests avec pictos réels du fichier de base
    // ----------------------------------------

    @Test
    void testDoublonDansBase() throws Exception {

        File base = tempDir.resolve("scenarioFC_base.json").toFile();
        try (FileWriter fw = new FileWriter(base)) {
            fw.write("{\"Scénario base\": [1,11,2,21,3]}");
        }

        Scenario scenarioAvecBase = new Scenario(base.getAbsolutePath());

        boolean result = scenarioAvecBase.ajouterScenario(TEXTE_SCENARIO_DOUBLON, PICTOS_BASE);
        assertFalse(result, "Doit refuser si les pictos existent déjà dans le fichier de base");
    }

    @Test
    void testDoublonDansData() throws Exception {

        try (FileWriter fw = new FileWriter(fichierData)) {
            fw.write("{\"Scénario existant\": [1,11,2,21,3]}");
        }

        // Obligation : recharger Scenario pour prendre en compte les données
        scenario = new Scenario(fichierData.getAbsolutePath());

        boolean result = scenario.ajouterScenario(TEXTE_SCENARIO_DOUBLON, PICTOS_BASE);
        assertFalse(result, "Doit refuser si les pictos existent déjà dans le fichier data");
    }

    // ----------------------------------------
    // Tests de limites des IDs des pictogrammes
    // ----------------------------------------

    @Test
    void testPictosHorsLimite() {
        boolean result = scenario.ajouterScenario("Scénario hors limite", PICTOS_HORS_LIMITE);
        assertFalse(result, "Doit refuser un scénario contenant des pictogrammes hors plage 0-26");
    }

    @Test
    void testPictosLimiteValide() {
        List<Integer> pictosLimite = Arrays.asList(0, 5, 10, 20, 26);
        boolean result = scenario.ajouterScenario("Scénario limite valide", pictosLimite);
        assertTrue(result, "Doit accepter un scénario avec des pictogrammes dans la plage 0-26");
    }
}