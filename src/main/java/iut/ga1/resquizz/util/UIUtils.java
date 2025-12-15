/*
 * UIUtils.java                                           19 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import javafx.scene.Scene;
import javafx.scene.transform.Scale;

/**
 * Classe utilitaire pour les opérations liées à l'interface utilisateur.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class UIUtils {

    /**
     * Applique une mise à l’échelle globale à la scène donnée
     * en fonction des dimensions spécifiées.
     *
     * @param scene   La scène JavaFX à mettre à l’échelle.
     * @param largeur La largeur cible pour la mise à l’échelle.
     * @param hauteur La hauteur cible pour la mise à l’échelle.
     */
    public static void appliquerEchelleGlobale(Scene scene, double largeur, double hauteur) {
        if (scene == null || scene.getRoot() == null) return;

        double scaleX = largeur / Parametres.getLargeur();
        double scaleY = hauteur / Parametres.getHauteur();
        double scale = Math.min(scaleX, scaleY);

        // Supprime les anciennes transformations pour éviter les cumuls
        scene.getRoot().getTransforms().clear();

        // Applique la mise à l’échelle centrée
        Scale s = new Scale(scale, scale, 0, 0);
        scene.getRoot().getTransforms().add(s);
    }
}
