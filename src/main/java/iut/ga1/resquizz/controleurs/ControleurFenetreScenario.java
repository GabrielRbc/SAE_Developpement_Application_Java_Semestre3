/*
 * ControleurFenetreScenario.java                                13 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Lanceur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.net.URL;

/**
 * Contrôleur de la fenêtre de choix entre partage et réception de scénarios
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurFenetreScenario {

    /**
     * Identifiant du label du titre de l'interface
     * Utiliser pour la gestion des paramètres
     */
    @FXML
    private Label titre;

    /**
     * Identifiant du borderPane de la fenêtre de l'interface
     * Utiliser pour la gestion des paramètres
     */
    @FXML
    private BorderPane fenetre;

    /**
     * Identifiant de l'imageView du logo 'I' Information de l'interface
     * Utiliser pour la gestion des paramètres
     */
    @FXML
    private ImageView logoInformation;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Identifiant du bouton pour recevoir les données */
    @FXML
    private Button buttonRecevoir;

    /** Identifiant du bouton pour partager les données */
    @FXML
    private Button buttonPartager;

    /** Méthode pour afficher un pop-up des informations */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information partage/réception");
        final String informations = "Choisissez si vous souhaiter partager ou recevoir des scénarios ou des paramètres";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Méthode pour revenir à la page précédente */
    @FXML
    public void gererClicRetour() {
        Lanceur.activerFenetreParametres();
    }

    /** Méthode pour lancer la fenêtre 'Partage' */
    @FXML
    public void partageScenario() {
        Lanceur.activerPartageScenario();
    }

    /** Méthode pour lancer la fenêtre 'Reception' */
    @FXML
    public void receptionScenario() {
        Lanceur.activerReceptionScenario();
    }

    /**
     * Met à jour les couleurs des boutons de sélection
     * @param couleur1 de la fenêtre
     * @param couleur2 du titre
     * @param couleur3 des logos
     */
    public void mettreAJourCouleur(String couleur1, String couleur2, boolean couleur3) {
        // On garde le style existant pour ne pas perdre la police
        String ancienStyleFenetre = fenetre.getStyle();
        String ancienStyleTitre = titre.getStyle();

        if (couleur1 != null) {
            fenetre.setStyle(ancienStyleFenetre + "-fx-background-color: " + couleur1 + ";");
        }
        if (couleur2 != null) {
            titre.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
        }

        if (couleur3) {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info.png"));
        } else {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info_blanc.png"));
        }
    }

    /**
     * Charge une image depuis le classpath en évitant les NullPointerException.
     * @param chemin chemin vers la ressource (ex: "/iut/ga1/resquizz/images/info.png")
     * @return l'image si trouvée, sinon null
     */
    private Image chargerImage(String chemin) {
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.err.println("Ressource introuvable : " + chemin);
            return null;
        }
        return new Image(url.toExternalForm());
    }

    /**
     * Permet de modifier la police de la fenêtre
     * @param cheminPolice à modifier
     */
    public void mettreAJourPolice(String cheminPolice) {
        Font font = Font.loadFont(getClass().getResourceAsStream(cheminPolice), 14);

        if (font != null) {
            String ancienStyleFenetre = fenetre.getStyle();
            fenetre.setStyle(ancienStyleFenetre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleTitre = titre.getStyle();
            titre.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRecevoir = buttonRecevoir.getStyle();
            buttonRecevoir.setStyle(ancienStyleButtonRecevoir + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonPartager = buttonPartager.getStyle();
            buttonPartager.setStyle(ancienStyleButtonPartager + "-fx-font-family: '" + font.getFamily() + "';");

        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRecevoir = buttonRecevoir.getStyle();
        buttonRecevoir.setStyle(ancienStyleButtonRecevoir +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonPartager = buttonPartager.getStyle();
        buttonPartager.setStyle(ancienStyleButtonPartager +"-fx-font-size: " + taillePolice + ";");
    }
}
