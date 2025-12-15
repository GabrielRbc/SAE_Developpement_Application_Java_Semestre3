/*
 * GestionParametres.java                               11 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import java.io.*;
import java.nio.file.*;

/**
 * Classe utilitaire pour gérer la sauvegarde et le chargement
 * des paramètres de l'application (via sérialisation).
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class GestionParametres {

    /** Noms des fichiers dans la zone de stockage */
    private static final String NOM_PARAMETRES = "parametres.ser";
    private static final String NOM_CLASSEMENT = "classement.ser";

    /** Le dossier de stockage défini dans Parametres */
    private static Path getDossierStockage() {
        return Paths.get(Parametres.getCheminStockage());
    }

    /**
     * Getter du fichier des paramètres
     * @return le nom du fichier
     */
    private static Path getFichierParametres() {
        return getDossierStockage().resolve(NOM_PARAMETRES);
    }

    /**
     * Getter du fichier des classements
     * @return le nom du fichier
     */
    private static Path getFichierClassement() {
        return getDossierStockage().resolve(NOM_CLASSEMENT);
    }

    /**
     * Méthode permettant de sauvegarder les paramètres du jeu dans un fichier
     */
    public static void sauvegarderParametres() {
        try {
            Files.createDirectories(getDossierStockage());
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(getFichierParametres()))) {
                oos.writeObject(Parametres.getInstance());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données paramètres : " + e.getMessage());
        }
    }

    /**
     * Méthode de la sauvegarde des classements
     */
    public static void sauvegarderClassement() {
        try {
            Files.createDirectories(getDossierStockage());
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(getFichierClassement()))) {
                oos.writeObject(GestionClassement.getInstance());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données classements : " + e.getMessage());
        }
    }

    /**
     * Méthode de chargement du fichier Paramètre
     */
    public static void chargerParametres() {
        Path fichier = getFichierParametres();

        if (!Files.exists(fichier)) {
            System.out.println("Aucun parametres.ser, valeurs par défaut.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(fichier))) {
            Parametres.setInstance((Parametres) ois.readObject());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données paramètres : " + e.getMessage());
        }
    }

    /**
     * Méthode de chargement des classements
     */
    public static void chargerClassement() {
        Path fichier = getFichierClassement();

        if (!Files.exists(fichier)) {
            System.out.println("Aucun classement.ser, valeurs par défaut.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(fichier))) {
            GestionClassement.setInstance((GestionClassement) ois.readObject());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données classement : " + e.getMessage());
        }
    }

    /**
     * Déplace tous les fichiers du stockage actuel vers un nouveau dossier,
     * puis met à jour Parametres.cheminStockage et sauvegarde l'état.
     */
    public static void deplacerDonneesVers(String nouveauDossier) throws IOException {

        Path ancien = getDossierStockage();
        Path nouveau = Paths.get(nouveauDossier);

        Files.createDirectories(nouveau);

        if (Files.exists(ancien)) {

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(ancien)) {

                for (Path fichier : stream) {
                    Files.move(
                            fichier,
                            nouveau.resolve(fichier.getFileName()),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }
            }
        }

        // Mise à jour
        Parametres.setCheminStockage(nouveauDossier);
        StorageConfig.sauvegarderCheminStockage(nouveauDossier);
        sauvegarderParametres();
    }
}
