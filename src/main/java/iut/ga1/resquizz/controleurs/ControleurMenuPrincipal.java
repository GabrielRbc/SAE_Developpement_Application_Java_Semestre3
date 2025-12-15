/*
 * ControleurMenuPrincipal.java                                    14 octobre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Lanceur;

import java.util.Optional;

import iut.ga1.resquizz.util.Parametres;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

/**
 * Contrôleur de la vue "menuPrincipal"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurMenuPrincipal {

    /**
     * Identifiant de la fenêtre
     */
    @FXML
    private BorderPane fenetre;

    /**
     * Identifiant du titre pour changer la couleur
     */
    @FXML
    private Label titre;

    /** Identifiant du bouton du menu jeux */
    @FXML
    private Button buttonJeux;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Identifiant du bouton paramètres */
    @FXML
    private Button buttonParametres;

    /**
     * Pour aller dans le menu des Jeux
     */
    @FXML
    public void gererClicJeux() {
        Lanceur.activerFenetreJeux();
    }

    /**
     * Pour aller dans le Menu des paramètres
     */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("MenuPrincipal");
        Lanceur.activerFenetreParametres();
    }

    /**
     * Permet de quitter l'application
     */
    @FXML
    public void gererClicQuitter() {
        Alert boiteAlerte = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir quitter ?",
                ButtonType.YES, ButtonType.NO);
        boiteAlerte.setTitle("Quitter le jeu");

        Optional<ButtonType> option = boiteAlerte.showAndWait();
        option.ifPresent(reponse -> {
            if (reponse == ButtonType.YES) {
                Platform.exit();
            }
        });
    }

    /**
     * Met à jour les couleurs des boutons de sélection
     *
     * @param couleur1 de la fenêtre
     * @param couleur2 du titre
     */
    public void mettreAJourCouleur(String couleur1, String couleur2) {
        // On garde le style existant pour ne pas perdre la police
        String ancienStyleFenetre = fenetre.getStyle();
        String ancienStyleTitre = titre.getStyle();
        if (couleur1 != null) {
            fenetre.setStyle(ancienStyleFenetre + "-fx-background-color: " + couleur1 + ";");
        }
        if (couleur2 != null) {
            titre.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
        }
    }


    /**
     * Permet de modifier la police de la fenêtre
     * @param cheminPolice à modifier
     */
    public void mettreAJourPolice(String cheminPolice) {
        Font font = Font.loadFont(getClass().getResourceAsStream(cheminPolice), 14);

        if (font != null) {
            // On garde le style existant de la fenêtre
            String ancienStyleFenetre = fenetre.getStyle();
            fenetre.setStyle(ancienStyleFenetre + "-fx-font-family: '" + font.getFamily() + "';");

            // On garde aussi le style existant du titre (notamment sa couleur).
            String ancienStyleTitre = titre.getStyle();
            titre.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonJeux = buttonJeux.getStyle();
            buttonJeux.setStyle(ancienStyleButtonJeux + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonParametres = buttonParametres.getStyle();
            buttonParametres.setStyle(ancienStyleButtonParametres + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonJeux = buttonJeux.getStyle();
        buttonJeux.setStyle(ancienStyleButtonJeux + "-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonParametres = buttonParametres.getStyle();
        buttonParametres.setStyle(ancienStyleButtonParametres + "-fx-font-size: " + taillePolice + ";");
    }
}