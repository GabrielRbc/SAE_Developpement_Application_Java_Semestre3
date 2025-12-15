/*
 * LectureJsonTest.java               21 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour {@link LectureJson}.
 */
class LectureJsonTest {

    private Path fichierTemporaire;

    @BeforeEach
    void setUp() throws IOException {
        fichierTemporaire = Files.createTempFile("test-lecture", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(fichierTemporaire);
    }

    @Test
    void testLectureJsonSimple() throws IOException {
        String contenu = "{ \"nom\": \"Dupont\", \"age\": 25 }";

        // Écrit le JSON dans le fichier temporaire
        Files.write(fichierTemporaire, contenu.getBytes("UTF-8"));

        LectureJson<String, Object> lecteur = new LectureJson<>();
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> map = lecteur.lireFichier(fichierTemporaire.toString(), type);

        assertEquals("Dupont", map.get("nom"));
        assertEquals(25.0, map.get("age")); // Gson convertit les int en doubl
    }

    @Test
    void testLectureDepuisTexte() {
        String contenu = "{ \"ville\": \"Paris\", \"code\": 75 }";

        LectureJson<String, Object> lecteur = new LectureJson<>();
        HashMap<String, Object> map = lecteur.lireDepuisTexte(contenu);

        assertEquals("Paris", map.get("ville"));
        assertEquals(75.0, map.get("code"));
    }

    @Test
    void testJsonInvalide() throws IOException {
        String contenu = "{ nom: 'invalide' "; // JSON incorrect

        // Écrit le contenu invalide dans le fichier
        Files.write(fichierTemporaire, contenu.getBytes("UTF-8"));

        LectureJson<String, Object> lecteur = new LectureJson<>();
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        assertThrows(RuntimeException.class, () -> lecteur.lireFichier(fichierTemporaire.toString(), type));
    }

    @Test
    void testFichierInexistant() {
        LectureJson<String, Object> lecteur = new LectureJson<>();
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        assertThrows(RuntimeException.class, () -> lecteur.lireFichier("fichier_qui_n_existe_pas.json", type));
    }
}
