/*
 * StorageConfigTest.java           26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class StorageConfigTest {

    // On doit donc utiliser ce nom exact pour le test.
    private static final String FICHIER_CONFIG = "storage.txt";
    private static final String FICHIER_BACKUP = "storage.txt.bak";

    private Path pathConfig;
    private Path pathBackup;

    @BeforeEach
    void setUp() throws IOException {
        pathConfig = Paths.get(FICHIER_CONFIG);
        pathBackup = Paths.get(FICHIER_BACKUP);

        // Si un vrai fichier de config existe déjà (celui de l'utilisateur), on le sauvegarde
        if (Files.exists(pathConfig)) {
            Files.move(pathConfig, pathBackup, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Nettoyage après le test : on supprime le fichier de test créé
        Files.deleteIfExists(pathConfig);

        // Si on avait sauvegardé un vrai fichier utilisateur, on le restaure
        if (Files.exists(pathBackup)) {
            Files.move(pathBackup, pathConfig, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    void testChargerCheminStockage_Defaut() {
        // Assurons-nous que le fichier n'existe pas
        assertFalse(Files.exists(pathConfig), "Le fichier ne doit pas exister pour ce test");

        String resultat = StorageConfig.chargerCheminStockage();

        assertEquals("data", resultat, "Si le fichier manque, on doit récupérer 'data'");
    }

    @Test
    void testChargerCheminStockage_FichierExiste() throws IOException {
        // Le fichier existe avec un chemin personnalisé.
        String cheminTest = "C:/MonDossier/Test";
        Files.write(pathConfig, cheminTest.getBytes());

        String resultat = StorageConfig.chargerCheminStockage();

        assertEquals(cheminTest, resultat, "On doit récupérer le contenu exact du fichier");
    }
}