/*
 * StorageConfig.java                                                   19 novembre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.*;

/**
 * Classe utilitaire permet de garder le chemin d'accès du dossier Data
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class StorageConfig {

    /** Nom du fichier de configuration */
    private static final String CONFIG_FILE = "storage.txt";

    /** Charge le dossier de stockage depuis storage.txt */
    public static String chargerCheminStockage() {
        Path txt = Paths.get(CONFIG_FILE);

        if (!Files.exists(txt)) {
            return "data"; // valeur par défaut
        }

        try {
            return new String(Files.readAllBytes(txt)).trim();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du chemin de stockage: " + e.getMessage());
            return "data";
        }
    }

    /** Sauvegarde le dossier de stockage dans storage.cfg */
    public static void sauvegarderCheminStockage(String chemin) {
        try {
            Files.write(Paths.get(CONFIG_FILE), chemin.getBytes());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zone de stockage");
            alert.setHeaderText("Réussite");
            alert.setContentText("Votre zone de stockage à bien été modifiée");
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zone de stockage");
            alert.setHeaderText("Erreur");
            alert.setContentText("Un problème est survenu et votre zone de stockage n'a pas été prise en compte");
            alert.showAndWait();

            System.err.println("Erreur lors de la sauvegarde du chemin de stockage : " + e.getMessage());
        }
    }
}
