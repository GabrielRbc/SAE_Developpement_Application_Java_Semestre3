/*
 * ControleurClassement.java                                    04 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.util.Parametres;
import iut.ga1.resquizz.util.GestionClassement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import javafx.scene.shape.Line;

import java.net.URL;
import java.util.Optional;

/**
 * Contrôleur de la vue "Classement"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurClassement {

    /** Identifiant de la fenêtre pour modifier le style */
    @FXML
    private BorderPane fenetre;

    /** Identifiant du logo Paramètre */
    @FXML
    private ImageView logoParametre;

    /** Identifiant du logo information */
    @FXML
    private ImageView logoInformation;

    /** Identifiant de la grille */
    @FXML
    private GridPane modeQCM;

    /**
     * Identifiant de la ligne Grille
     * Cet identifiant permet de modifier sa transparence et
     * afficher à l'utilisateur, le bon onglet ouvert
     */
    @FXML
    private Line lineGrille;

    /** Identifiant de la ligne QCM */
    @FXML
    private Line lineQCM;

    /** Identifiant de la ligne Scénario Fixe Complet */
    @FXML
    private Line lineScenarioFixe;

    /** Identifiant de la ligne Scénario Aléatoire Pas à pas */
    @FXML
    private Line lineAleaPaP;

    /** Identifiant de du label des scores */
    @FXML
    private Label score;

    /** Identifiant de du label des chronomètres */
    @FXML
    private Label chrono;

    /** Identifiant du label du jeu Revision Grille */
    @FXML
    private Label labelGrille;

    /** Identifiant du label du jeu QCM */
    @FXML
    private Label labelQCM;

    /** Identifiant du label du jeu Scenario Fixe */
    @FXML
    private Label labelScenarioFixe;

    /** Identifiant du label du scenario aleatoire pas a pas*/
    @FXML
    private Label labelScenarioAlea;

    /** Identifiant du rectangle du mode de jeu QCM Pictogramme
     * Permet de l'afficher en bleu selon la sélection de l'utilisateur
     */
    @FXML
    private Rectangle strokePicto;

    /** Identifiant du rectangle du mode de jeu QCM Description */
    @FXML
    private Rectangle strokeDesc;

    /** Identifiant du rectangle du mode de jeu QCM Aléatoire */
    @FXML
    private Rectangle strokeAlea;

    /** Identifiant du bouton reset du jeu Revision Grille
     * Permet de réinitialiser les scores de ce mode de jeu
     */
    @FXML
    private Button resetGrille;

    /** Identifiant du bouton reset du jeu QCM en mode Picto */
    @FXML
    private Button resetPicto;

    /** Identifiant du bouton reset du jeu QCM en mode Description */
    @FXML
    private Button resetDesc;

    /** Identifiant du bouton reset du jeu QCM en mode Aléatoire */
    @FXML
    private Button resetAlea;

    /** Identifiant du bouton reset du jeu Scénario Fixe */
    @FXML
    private Button resetScenarioFixe;

    /** Identifiant du bouton reset du jeu Scénario Aléatoire pas à pas */
    @FXML
    private Button resetAleaPaP;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /**
     * Pour lancer la fenêtre des paramètres
     * Sauvegarde le chemin d'accès pour pouvoir revenir à chaque instant
     */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("Classement");
        Lanceur.activerFenetreParametres();
    }

    /** Pour lancer la fenêtre des informations */
    @FXML
    public void gererClicInformations() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Classement");
        final String informations = "Retrouvez vos meilleurs temps et meilleurs score sur chacun de vos modes de jeu \n" +
                "Vous pouvez réinitialiser vos scores pour chaque modes de jeux ";

        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Pour retourner au menu principal */
    @FXML
    public void gererClicRetour() {
        Lanceur.activerFenetreJeux();
    }

    /**
     * Met à jour les couleurs des boutons de sélection
     * @param couleur1 de la fenêtre
     * @param couleur3 des logos
     */
    public void mettreAJourCouleur(String couleur1, boolean couleur3) {
        // On garde le style existant pour ne pas perdre la police
        String ancienStyleFenetre = fenetre.getStyle();
        if (couleur1 != null) {
            fenetre.setStyle(ancienStyleFenetre + "-fx-background-color: " + couleur1 + ";");
        }

        if (couleur3) {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info.png"));
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param.png"));
        } else {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info_blanc.png"));
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param_blanc.png"));
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
            // On garde le style existant de la fenêtre
            String ancienStyleFenetre = fenetre.getStyle();
            fenetre.setStyle(ancienStyleFenetre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleLabelGrille = labelGrille.getStyle();
            labelGrille.setStyle(ancienStyleLabelGrille + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLabelQCM = labelQCM.getStyle();
            labelQCM.setStyle(ancienStyleLabelQCM + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLabelScenarioFixe = labelScenarioFixe.getStyle();
            labelScenarioFixe.setStyle(ancienStyleLabelScenarioFixe + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLabelScenarioAlea = labelScenarioAlea.getStyle();
            labelScenarioAlea.setStyle(ancienStyleLabelScenarioAlea + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetGrille = resetGrille.getStyle();
            resetGrille.setStyle(ancienStyleResetGrille + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetPicto = resetPicto.getStyle();
            resetPicto.setStyle(ancienStyleResetPicto + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetDesc = resetDesc.getStyle();
            resetDesc.setStyle(ancienStyleResetDesc + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetAlea = resetAlea.getStyle();
            resetAlea.setStyle(ancienStyleResetAlea + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetScenarioFixe = resetScenarioFixe.getStyle();
            resetScenarioFixe.setStyle(ancienStyleResetScenarioFixe + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleResetAleaPaP = resetAleaPaP.getStyle();
            resetAleaPaP.setStyle(ancienStyleResetAleaPaP + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }

    /**
     * Méthode permettant d'afficher l'onglet Revision Grille
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicRevision() {
        modeQCM.setVisible(false);
        modeQCM.setDisable(true);
        lineGrille.setStroke(Color.WHITE);
        lineQCM.setStroke(Color.BLACK);
        lineScenarioFixe.setStroke(Color.BLACK);
        lineAleaPaP.setStroke(Color.BLACK);
        resetGrille.setVisible(true);
        resetPicto.setVisible(false);
        resetDesc.setVisible(false);
        resetAleaPaP.setVisible(false);
        resetScenarioFixe.setVisible(false);
        resetAlea.setVisible(false);
        resetGrille.setDisable(false);
        resetPicto.setDisable(true);
        resetDesc.setDisable(true);
        resetAlea.setDisable(true);
        resetScenarioFixe.setDisable(true);
        resetAleaPaP.setDisable(true);

        int pointGrille = GestionClassement.getPointGrille();
        long chronoGrille = GestionClassement.getChronoGrille();

        if (pointGrille == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointGrille);
        }

        long totalSeconds = chronoGrille / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoGrille == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }

    }

    /**
     * Méthode permettant d'afficher l'onglet QCM
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicQCM() {
        modeQCM.setVisible(true);
        modeQCM.setDisable(false);
        lineGrille.setStroke(Color.BLACK);
        lineQCM.setStroke(Color.WHITE);
        lineScenarioFixe.setStroke(Color.BLACK);
        lineAleaPaP.setStroke(Color.BLACK);

        gererClicPictogramme();
    }

    /**
     * Méthode permettant d'afficher l'onglet Scénario Fixe
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicScenarioFixe(){
        modeQCM.setVisible(false);
        modeQCM.setDisable(true);
        lineGrille.setStroke(Color.BLACK);
        lineQCM.setStroke(Color.BLACK);
        lineScenarioFixe.setStroke(Color.WHITE);
        lineAleaPaP.setStroke(Color.BLACK);
        resetGrille.setVisible(false);
        resetPicto.setVisible(false);
        resetDesc.setVisible(false);
        resetAlea.setVisible(false);
        resetScenarioFixe.setVisible(true);
        resetAleaPaP.setVisible(false);
        resetGrille.setDisable(true);
        resetPicto.setDisable(true);
        resetDesc.setDisable(true);
        resetAlea.setDisable(true);
        resetScenarioFixe.setDisable(false);
        resetAleaPaP.setDisable(true);

        int pointScenarioFixe = GestionClassement.getPointScenarioFC();
        long chronoScenarioFixe = GestionClassement.getChronoScenarioFC();

        if (pointScenarioFixe == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointScenarioFixe);
        }

        long totalSeconds = chronoScenarioFixe / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoScenarioFixe == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }
    }

    /**
     * Méthode permettant d'afficher l'onglet Scénario Aléatoire Pas a pas
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicAleaPaP(){
        modeQCM.setVisible(false);
        modeQCM.setDisable(true);
        lineGrille.setStroke(Color.BLACK);
        lineQCM.setStroke(Color.BLACK);
        lineScenarioFixe.setStroke(Color.BLACK);
        lineAleaPaP.setStroke(Color.WHITE);
        resetGrille.setVisible(false);
        resetPicto.setVisible(false);
        resetDesc.setVisible(false);
        resetAlea.setVisible(false);
        resetScenarioFixe.setVisible(false);
        resetAleaPaP.setVisible(true);
        resetGrille.setDisable(true);
        resetPicto.setDisable(true);
        resetDesc.setDisable(true);
        resetAlea.setDisable(true);
        resetScenarioFixe.setDisable(true);
        resetAleaPaP.setDisable(false);

        int pointAleaPaP = GestionClassement.getPointScenarioAlea();
        long chronoAleaPaP = GestionClassement.getChronoScenarioAlea();

        if (pointAleaPaP == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointAleaPaP);
        }

        long totalSeconds = chronoAleaPaP / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoAleaPaP == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }
    }

    /**
     * Méthode permettant d'afficher l'onglet du mode de jeu QCM Pictogramme
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicPictogramme(){
        strokePicto.setStroke(Color.web("#3369ff"));
        strokeDesc.setStroke(Color.BLACK);
        strokeAlea.setStroke(Color.BLACK);
        resetGrille.setVisible(false);
        resetPicto.setVisible(true);
        resetDesc.setVisible(false);
        resetAlea.setVisible(false);
        resetScenarioFixe.setVisible(false);
        resetAleaPaP.setVisible(false);
        resetGrille.setDisable(true);
        resetPicto.setDisable(false);
        resetDesc.setDisable(true);
        resetAlea.setDisable(true);
        resetScenarioFixe.setDisable(true);
        resetAleaPaP.setDisable(true);
        int pointPictogramme = GestionClassement.getPointQCMPicto();
        long chronoPictogramme = GestionClassement.getChronoQCMPicto();

        if (pointPictogramme == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointPictogramme);
        }

        long totalSeconds = chronoPictogramme / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoPictogramme == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }
    }

    /**
     * Méthode permettant d'afficher l'onglet du mode de jeu QCM Description
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicDescription(){
        strokePicto.setStroke(Color.BLACK);
        strokeDesc.setStroke(Color.web("#3369ff"));
        strokeAlea.setStroke(Color.BLACK);
        resetGrille.setVisible(false);
        resetPicto.setVisible(false);
        resetDesc.setVisible(true);
        resetAlea.setVisible(false);
        resetScenarioFixe.setVisible(false);
        resetAleaPaP.setVisible(false);
        resetGrille.setDisable(true);
        resetPicto.setDisable(true);
        resetDesc.setDisable(false);
        resetAlea.setDisable(true);
        resetScenarioFixe.setDisable(true);
        resetAleaPaP.setDisable(true);
        int pointDescription = GestionClassement.getPointQCMDesc();
        long chronoDescription = GestionClassement.getChronoQCMDesc();

        if (pointDescription == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointDescription);
        }

        long totalSeconds = chronoDescription / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoDescription == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }
    }

    /**
     * Méthode permettant d'afficher l'onglet du mode de jeu QCM Aléatoire
     * La méthode retire l'affichage des autres modes
     */
    public void gererClicAleatoire(){
        strokePicto.setStroke(Color.BLACK);
        strokeDesc.setStroke(Color.BLACK);
        resetGrille.setVisible(false);
        resetPicto.setVisible(false);
        resetDesc.setVisible(false);
        resetAlea.setVisible(true);
        resetScenarioFixe.setVisible(false);
        resetAleaPaP.setVisible(false);
        resetGrille.setDisable(true);
        resetPicto.setDisable(true);
        resetDesc.setDisable(true);
        resetAlea.setDisable(false);
        resetScenarioFixe.setDisable(true);
        resetAleaPaP.setDisable(true);
        strokeAlea.setStroke(Color.web("#3369ff"));
        int pointAlea = GestionClassement.getPointQCMAlea();
        long chronoAlea = GestionClassement.getChronoQCMAlea();

        if (pointAlea == Integer.MIN_VALUE) {
            score.setText("Aucun score enregistré");
        } else {
            score.setText("Meilleur score : " + pointAlea);
        }


        long totalSeconds = chronoAlea / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (chronoAlea == Long.MAX_VALUE) {
            chrono.setText("Aucun temps enregistré");
        } else {
            chrono.setText("Meilleur temps : " + minutes + "min " + seconds + "sec");
        }
    }

    /** Méthode d'affichage d'une pop-up de restart */
    public void alertReset(String categorie){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Réinitialisation");
        alert.setHeaderText("Attention !");
        alert.setContentText("Vous allez perdre toutes vos données concernant ce mode de jeu !\n"
                +"Êtes vous sur de vouloir réinitialiser?");

        // Créer des boutons personnalisés
        ButtonType continueButton = new ButtonType("Continuer");
        ButtonType retourButton = new ButtonType("Retour");

        // Remplacer les boutons par défaut
        alert.getButtonTypes().setAll(continueButton, retourButton);

        // Afficher et attendre la réponse
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == continueButton) {
                GestionClassement.resetCategorie(categorie);
            }
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {

        String ancienStyleLabelGrille = labelGrille.getStyle();
        labelGrille.setStyle(ancienStyleLabelGrille +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLabelQCM = labelQCM.getStyle();
        labelQCM.setStyle(ancienStyleLabelQCM +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLabelScenarioFixe = labelScenarioFixe.getStyle();
        labelScenarioFixe.setStyle(ancienStyleLabelScenarioFixe +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLabelScenarioAlea = labelScenarioAlea.getStyle();
        labelScenarioAlea.setStyle(ancienStyleLabelScenarioAlea +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetGrille = resetGrille.getStyle();
        resetGrille.setStyle(ancienStyleResetGrille +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetPicto = resetPicto.getStyle();
        resetPicto.setStyle(ancienStyleResetPicto +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetDesc = resetDesc.getStyle();
        resetDesc.setStyle(ancienStyleResetDesc +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetAlea = resetAlea.getStyle();
        resetAlea.setStyle(ancienStyleResetAlea +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetScenarioFixe = resetScenarioFixe.getStyle();
        resetScenarioFixe.setStyle(ancienStyleResetScenarioFixe +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleResetAleaPaP = resetAleaPaP.getStyle();
        resetAleaPaP.setStyle(ancienStyleResetAleaPaP +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour +"-fx-font-size: " + taillePolice + ";");
    }

    /** Méthode pour gérer le clic sur le bouton restart du jeu Revision Grille */
    @FXML
    public void gererClicResetGrille() {
        if(GestionClassement.getPointGrille() != Integer.MIN_VALUE) {
            alertReset("grille");
            gererClicRevision();
        } else {
            alert();
        }
    }

    /** Méthode pour gérer le clic sur le bouton restart ddu mode de jeu Pictogramme sur QCM */
    @FXML
    public void gererClicResetPicto() {
        if(GestionClassement.getPointQCMPicto() != Integer.MIN_VALUE) {
            alertReset("picto");
            gererClicPictogramme();
        } else {
            alert();
        }
    }

    /** Méthode pour gérer le clic sur le bouton restart du mode de jeu Description sur QCM */
    @FXML
    public void gererClicResetDesc() {
        if(GestionClassement.getPointQCMDesc() != Integer.MIN_VALUE) {
            alertReset("desc");
            gererClicDescription();
        } else {
            alert();
        }
    }

    /** Méthode pour gérer le clic sur le bouton restart deu mode de jeu Aléatoire sur QCM */
    @FXML
    public void gererClicResetAlea() {
        if(GestionClassement.getPointQCMAlea() != Integer.MIN_VALUE) {
            alertReset("alea");
            gererClicAleatoire();
        } else {
            alert();
        }
    }

    /** Méthode pour gérer le clic sur le bouton restart du jeu Scénario Fixe Complet */
    @FXML
    public void gererClicResetScenarioFixe() {
        if(GestionClassement.getPointScenarioFC() != Integer.MIN_VALUE) {
            alertReset("scenariofixe");
            gererClicScenarioFixe();
        } else {
            alert();
        }
    }

    /** Méthode pour gérer le clic sur le bouton restart du jeu Scénario Aléatoire Pas a pas */
    @FXML
    public void gererClicResetAleaPaP() {
        if(GestionClassement.getPointScenarioAlea() != Integer.MIN_VALUE) {
            alertReset("aleapap");
            gererClicAleatoire();
        } else {
            alert();
        }
    }

    /** Méthode créant une fenêtre de réinitialisation des modes de jeu */
    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Réinitialisation");
        alert.setContentText("Vous ne pouvez pas réinitialiser car aucun score n'a été enregistré");
        alert.showAndWait();
    }

    /** Méthode d'initialisation de la fenêtre Classement */
    @FXML
    public void initialize() {
        gererClicRevision();
    }

}
