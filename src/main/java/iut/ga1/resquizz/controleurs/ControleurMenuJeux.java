/*
 * ControleurMenuJeux.java                                    14 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Lanceur;

import iut.ga1.resquizz.util.LectureJson;
import iut.ga1.resquizz.util.Parametres;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;

/**
 * Contrôleur de la vue "MenuJeux"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurMenuJeux {

    /** Identifiant de la fenêtre pour modifier le style */
    @FXML
    private BorderPane fenetre;

    /** Identifiant du titre pour changer le style */
    @FXML
    public Label titre;

    /** Identifiant du logo paramètre */
    @FXML
    private ImageView logoParametre;

    /** Identifiant du logo Classement */
    @FXML
    private ImageView logoClassement;

    /** Identifiant du logo Information */
    @FXML
    private ImageView logoInformation;

    /** Identifiant du bouton pour lancer le jeu Revision Grille */
    @FXML
    private Button buttonGrille;

    /** Identifiant du bouton pour lancer le jeu QCM */
    @FXML
    private Button buttonQCM;

    /** Identifiant du bouton pour lancer le jeu Scénario Fixe Complet */
    @FXML
    private Button buttonScenarioFixe;

    /** Identifiant du bouton pour lancer le jeu Scénario Aléatoire Pas à pas */
    @FXML
    private Button buttonScenarioAlea;

    /** Identifiant du bouton pour retourner à la page précédente */
    @FXML
    private Button buttonRetour;

    /** Pour lancer la fenêtre des paramètres
     * Sauvegarde le chemin pour revenir à la bonne fenêtre
     */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("MenuJeux");
        Lanceur.activerFenetreParametres();
    }

    /** Pour lancer la fenêtre des informations */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Menu jeux");
        final String informations = "Choisissez le mode de jeu que vous souhaitez lancer.\n" +
                "\n" +
                "Consultez les classements associés à chaque mode via l'icône en bas à gauche.\n" +
                "\n" +
                "Accédez aux paramètres via l’icône en haut à droite.";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Pour lancer la fenêtre du classement */
    @FXML
    public void gererClicClassement() { Lanceur.activerFenetreClassement(); }

    /** Pour lancer le jeu Révision de la grille */
    @FXML
    public void gererClicRevisionGrille() {
        Lanceur.activerFenetreRevisionGrille();
    }

    /** Pour lancer le jeu QCM */
    @FXML
    public void gererClicQCM() {
        Lanceur.activerFenetreQCM();
    }

    /** Pour lancer le jeu Scénario Fixe Complet */
    @FXML
    public void gererClicScenarioFixeComplet() {
        String chemin = "/iut/ga1/resquizz/json/scenariosFC.json";
        HashMap<String, int[]> scenarios;

        try {
            // Lecture du JSON
            LectureJson<String, int[]> lecteurJson = new LectureJson<>();
            Type type = new com.google.gson.reflect.TypeToken<HashMap<String, int[]>>() {}.getType();
            scenarios = lecteurJson.lireFichierRessource(chemin, type);

            // Vérifier si le JSON est vide
            if (scenarios == null || scenarios.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("JSON vide");
                alert.setHeaderText("Impossible de lancer le mode Scénario Fixe Complet");
                alert.setContentText("Le fichier de scénarios est vide ou introuvable.");
                alert.showAndWait();
                return;
            }

        } catch (Exception e) {
            // Si le format est incorrect ou qu'une autre erreur survient
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de lecture JSON");
            alert.setHeaderText("Impossible de lancer le mode Scénario Fixe Complet");
            alert.setContentText("Le fichier de scénarios est corrompu ou au mauvais format");
            alert.showAndWait();
            return;
        }

        // Si tout est bon, lancer le jeu
        Lanceur.activerFenetreSceFixeComplet();
    }

    /** Pour lancer le jeu Scénario Aléatoire Pas à pas*/
    @FXML
    public void gererClicScenarioAleaPaP() {
        String chemin = "/iut/ga1/resquizz/json/scenariosAPaP.json";

        try {
            /* Comme le fichier JSON commence par une accolade '{', il représente un Objet JSON et non une Liste.
             * On utilise donc TypeToken pour dire à GSON de convertir cet objet en une HashMap
             */
            Type typeListe = new com.google.gson.reflect.TypeToken<java.util.HashMap<String, Object>>() {}.getType();

            // La classe LectureJson attend des types génériques <X, Y>
            // On met <Object, Object> car on va utiliser une méthode générique
            // (lireFichierRessource) qui est capable de s'adapter à n'importe quel type.
            LectureJson<Object, Object> lecteurJson = new LectureJson<>();

            // On utilise 'lireFichierRessource' pour charger le fichier depuis le dossier compilé
            // ce qui est indispensable pour que ça marche une fois le projet construit.
            // Le résultat est stocké dans une Map correspondant à la structure de notre JSON.
            java.util.Map<String, Object> scenarios = lecteurJson.lireFichierRessource(chemin, typeListe);

            // Vérification
            if (scenarios == null || scenarios.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("JSON vide");
                alert.setHeaderText("Impossible de lancer le mode Scénario Aléatoire");
                alert.setContentText("Le fichier de scénarios est vide ou introuvable.");
                alert.showAndWait();
                return; // On arrête tout, on ne lance pas la fenêtre
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de lecture JSON");
            alert.setHeaderText("Impossible de lancer le mode Scénario Aléatoire");
            alert.setContentText("Le fichier est corrompu : " + e.getMessage());
            alert.showAndWait();
            return;
        }

        // Si tout est OK, on lance la fenêtre
        Lanceur.activerFenetreSceAleaPap();
    }

    /** Pour retourner au menu principal */
    @FXML
    public void gererClicRetour() {
        Lanceur.activerFenetreMenu();
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
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param.png"));
            logoClassement.setImage(chargerImage("/iut/ga1/resquizz/images/classement.png"));
        } else {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info_blanc.png"));
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param_blanc.png"));
            logoClassement.setImage(chargerImage("/iut/ga1/resquizz/images/classement_blanc.png"));
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
            // On garde aussi le style existant du titre (notamment sa couleur).
            String ancienStyleTitre = titre.getStyle();
            titre.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienButtonGrille = buttonGrille.getStyle();
            buttonGrille.setStyle(ancienButtonGrille + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienButtonQCM = buttonQCM.getStyle();
            buttonQCM.setStyle(ancienButtonQCM + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienButtonScenarioFixe = buttonScenarioFixe.getStyle();
            buttonScenarioFixe.setStyle(ancienButtonScenarioFixe + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienButtonScenarioAlea = buttonScenarioAlea.getStyle();
            buttonScenarioAlea.setStyle(ancienButtonScenarioAlea + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");

        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {

        String ancienButtonGrille = buttonGrille.getStyle();
        buttonGrille.setStyle(ancienButtonGrille +"-fx-font-size: " + taillePolice + ";");
        String ancienButtonQCM = buttonQCM.getStyle();
        buttonQCM.setStyle(ancienButtonQCM +"-fx-font-size: " + taillePolice + ";");
        String ancienButtonScenarioFixe = buttonScenarioFixe.getStyle();
        buttonScenarioFixe.setStyle(ancienButtonScenarioFixe +"-fx-font-size: " + taillePolice + ";");
        String ancienButtonScenarioAlea = buttonScenarioAlea.getStyle();
        buttonScenarioAlea.setStyle(ancienButtonScenarioAlea +"-fx-font-size: " + taillePolice + ";");
        String ancienButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienButtonRetour +"-fx-font-size: " + taillePolice + ";");
    }
}