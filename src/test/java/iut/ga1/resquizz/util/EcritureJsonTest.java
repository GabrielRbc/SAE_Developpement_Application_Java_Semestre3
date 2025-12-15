/*
 * EcritureJsonTest.java               21 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class EcritureJsonTest {

    private Path fichierTemporaire;

    @BeforeEach
    void setUp() throws IOException {
        fichierTemporaire = Files.createTempFile("test-ecriture", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(fichierTemporaire);
    }

    @Test
    void testAjoutEtEcriture() throws IOException {
        EcritureJson<String, Object> ecriture = new EcritureJson<>();
        ecriture.ajouterElement("nom", "Dupont");
        ecriture.ajouterElement("age", 30);

        // Écrit le JSON dans le fichier temporaire
        ecriture.ecrireFichier(fichierTemporaire.toString());

        // Lit le contenu réel du fichier
        String contenu = new String(Files.readAllBytes(fichierTemporaire), "UTF-8");

        assertTrue(contenu.contains("nom"));
        assertTrue(contenu.contains("Dupont"));
        assertTrue(contenu.contains("age"));
        assertTrue(contenu.contains("30"));
    }

    @Test
    void testToJson() {
        EcritureJson<String, String> ecriture = new EcritureJson<>();
        ecriture.ajouterElement("cle", "valeur");
        String json = ecriture.toJson();

        assertTrue(json.contains("\"cle\""));
        assertTrue(json.contains("\"valeur\""));
    }

    @Test
    void testAjoutDoublon() {
        EcritureJson<String, Integer> ecriture = new EcritureJson<>();
        assertTrue(ecriture.ajouterElement("x", 1));
        assertFalse(ecriture.ajouterElement("x", 2));
    }
}
