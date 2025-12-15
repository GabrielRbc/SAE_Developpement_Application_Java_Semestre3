/*
 * ControleurRevisionGrille.java                                    15 octobre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.*;
import iut.ga1.resquizz.util.GestionClassement;
import iut.ga1.resquizz.util.GestionParametres;
import iut.ga1.resquizz.util.Parametres;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import iut.ga1.resquizz.util.Pictogramme;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * Contrôleur de la vue "JeuRevisionGrille"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurRevisionGrille {

    /** Ajout d'une grille */
    private RevisionGrille revisionGrille = new RevisionGrille();

    /** Identifiant pour la sauvegarde des points */
    private boolean sauvegarderPoints;

    /** Chronomètre pour mesurer le temps écoulé */
    private final Chronometre chronometre = new Chronometre();

    /** Indique si la grille a été validée */
    private boolean grilleValide = false;

    /** Map qui a pour clé un pictogramme et pour valeur un ImageView */
    private final Map<ImageView, Pictogramme> mapImagePictogramme = new HashMap<>();

    /** Conteneur invisible qui contiendra les pictogrammes pendant le drag and drop */
    @FXML
    public Pane overlayDrag;

    /** Conteneur de toute la structure de la grille */
    @FXML
    private GridPane conteneurGrille;

    /** HBox contenant les pictogrammes proposés */
    @FXML
    private HBox containerPropositions;

    /** Identifiant de la fenêtre pour modifier le style */
    @FXML
    private StackPane fenetre;

    /** Identifiant du logo Parametre */
    @FXML
    private ImageView logoParametre;

    /** Identifiant du logo Information */
    @FXML
    private ImageView logoInformation;

    /** Label pour afficher le chronomètre */
    @FXML
    private Label chronoLabel;

    /** Identifiant du Label des points */
    @FXML
    private Label points;

    /** Identifiant du bouton restart */
    @FXML
    private Button buttonRestart;

    /** Identifiant du bouton valider */
    @FXML
    private Button buttonValider;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Pour lancer la méthode de validation de la grille */
    @FXML
    public void gererClicValider() {

        revisionGrille.calculPoints();
        grilleValide = true;
        mettreAJourFeedback();
        grilleValide = false;
        revisionGrille.calculPoints();
        points.setText("Points : " + revisionGrille.getPoints());

        if (sauvegarderPoints) {
            sauvegarderPoints = false;
            if (revisionGrille.getPoints() > GestionClassement.getPointGrille()) {
                GestionClassement.setPointGrille(revisionGrille.getPoints());
                GestionClassement.setChronoGrille(chronometre.getTempsEcoule());
                GestionParametres.sauvegarderClassement();
            } else if (revisionGrille.getPoints() == GestionClassement.getPointGrille()
                    && chronometre.getTempsEcoule() < GestionClassement.getChronoGrille()) {
                GestionClassement.setChronoGrille(chronometre.getTempsEcoule());
            }
        }

        if (revisionGrille.estBonneReponse()) {
            // Crée un alert sans boutons par défaut
            chronometre.arreterChrono();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Félicitations !");
            alert.setHeaderText("Grille complète");
            alert.setContentText("Bravo ! Vous avez placé tous les pictogrammes correctement.");

            // Créer des boutons personnalisés
            ButtonType restartButton = new ButtonType("Restart");
            ButtonType retourMenuButton = new ButtonType("Retour au menu");

            // Remplacer les boutons par défaut
            alert.getButtonTypes().setAll(restartButton, retourMenuButton);

            // Afficher et attendre la réponse
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == restartButton) {
                    gererClicRestart(); // relance la grille
                } else if (result.get() == retourMenuButton) {
                    gererClicRetourFin(); // retourne au menu
                }
            }
        }
    }

    /** Pour lancer la méthode de réinitialisation de la grille */
    @FXML
    public void gererClicRestart() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirmation de redémarrage");
        alert.setHeaderText("Redémarrer une partie ?");
        alert.setContentText("Votre progression actuelle sera perdue.\n\nSouhaitez-vous vraiment recommencer ?");

        ButtonType boutonConfirmer = new ButtonType("Oui, redémarrer");
        ButtonType boutonAnnuler = new ButtonType("Non, annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(boutonConfirmer, boutonAnnuler);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == boutonConfirmer) {
            // Redémarrer la partie
            reinitialiserJeu();
        }
    }

    /** Pour lancer la fenêtre des informations du jeu */
    @FXML
    public void gererClicInformations() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Révision de la grille");
        final String informations = "Replacez chaque pictogramme au bon endroit sur la grille vide du plan d’intervention.\n" +
                "Consultez à tout moment la signification des pictogrammes.\n" +
                "Les bonnes et mauvaises réponses sont indiquées clairement.\n" +
                "Aucune limite d’erreurs : entraînez-vous librement.";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Pour lancer la fenêtre des paramètres */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("RevisionGrille");
        Lanceur.activerFenetreParametres();
        chronometre.arreterChrono();
    }

    /** Pour lancer la fenêtre des Jeux */
    @FXML
    public void gererClicRetour() {
        if (chronometre.getTempsEcoule() != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Retour au menu");
            alert.setHeaderText("Attention !");
            alert.setContentText("Si vous retournez au menu, vous perdrez votre progression. Êtes-vous sûr ?");

            ButtonType ouiButton = new ButtonType("Oui");
            ButtonType nonButton = new ButtonType("Non");
            alert.getButtonTypes().setAll(ouiButton, nonButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ouiButton) {
                reinitialiserJeu();
                Lanceur.activerFenetreJeux();
            }
        } else {
            Lanceur.activerFenetreJeux();
        }
    }

    /** Pour lancer la fenêtre des Jeux une fois fini */
    @FXML
    public void gererClicRetourFin() {
        chronometre.arreterChrono();
        chronometre.reinitialiserChrono();
        reinitialiserJeu();
        Lanceur.activerFenetreJeux();
    }

    /** Méthode d'initialisation de la grille au démarrage de l'application */
    @FXML
    public void initialize() {

        revisionGrille = new RevisionGrille();
        sauvegarderPoints = true;

        // Liaison de la propriété de temps du chronomètre au Label
        chronometre.getTempsEcouleProperty().addListener((observable, oldValue, newValue) -> {
            // Convertit les millisecondes en format "Min Sec"
            long totalSeconds = newValue.longValue() / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            this.chronoLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });

        for (Node node : conteneurGrille.getChildren()) {
            if (node instanceof StackPane) {
                definirTooltip((StackPane) node);
            }
        }

        actualiserPropositions();
    }

    /**
     * Rend une image view déplaçable avec la souris
     * @param imageView l'ImageView à rendre déplaçable
     *                  Rend une ImageView déplaçable à la souris.
     */
    private void rendreDeplacable(ImageView imageView) {
        final double[] offset = new double[2];
        Parent parent = imageView.getParent();

        imageView.setOnMousePressed(event -> {

            //lancement du chrono si pas déjà fait
            chronometre.demarrerChrono();

            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();
            double layoutX = imageView.localToScene(0, 0).getX();
            double layoutY = imageView.localToScene(0, 0).getY();
            offset[0] = sceneX - layoutX;
            offset[1] = sceneY - layoutY;

            // si l'image vient d'une case de la grille on stocke son id d'origine
            if (conteneurGrille.getChildren().contains(parent) && parent.getId() != null) {
                imageView.getProperties().put("srcCaseId", parent.getId());
                imageView.getProperties().remove("origIndex");
            } else {
                // Si l'image vient des propositions
                int index = containerPropositions.getChildren().indexOf(imageView);
                imageView.getProperties().put("origIndex", index);
                imageView.getProperties().remove("srcCaseId");
            }

            if (parent.getId() != null && imageView.getImage() != null) {
                String lienVide = Objects.requireNonNull(getClass().getResource(
                        "/iut/ga1/resquizz/images/vide.png")).toExternalForm();
                String urlImage = imageView.getImage().getUrl();

                if (urlImage == null || !urlImage.equals(lienVide)) {
                    ImageView caseRemplacement = new ImageView();
                    caseRemplacement.setImage(new Image(lienVide));
                    caseRemplacement.setFitWidth(imageView.getFitWidth());
                    caseRemplacement.setFitHeight(imageView.getFitHeight());
                    caseRemplacement.setPreserveRatio(true);
                    caseRemplacement.setId(imageView.getId());

                    if (parent instanceof StackPane) {
                        StackPane stack = (StackPane) parent;
                        stack.getChildren().add(caseRemplacement);
                    }
                }
            }

            if (!overlayDrag.getChildren().contains(imageView)) {
                overlayDrag.getChildren().add(imageView);
            }

            imageView.setLayoutX(overlayDrag.sceneToLocal(layoutX, layoutY).getX());
            imageView.setLayoutY(overlayDrag.sceneToLocal(layoutX, layoutY).getY());
        });

        imageView.setOnMouseDragged(event -> {
            imageView.setLayoutX(event.getSceneX() - offset[0]);
            imageView.setLayoutY(event.getSceneY() - offset[1]);
        });

        imageView.setOnMouseReleased(event -> dropImage(imageView, event.getSceneX(), event.getSceneY()));
    }

    /**
     * Gère le comportement du jeu lorsqu'une image est lâchée par drag and drop
     * @param img    l'image qui a étée lâchée
     * @param sceneX coordonée de la scène X où l'image a étée lâchée
     * @param sceneY coordonée de la scène Y où l'image a étée lâchée
     */
    private void dropImage(ImageView img, double sceneX, double sceneY) {
        overlayDrag.getChildren().remove(img);

        Pictogramme pictogramme = mapImagePictogramme.get(img);
        if (pictogramme == null) return;

        StackPane cible = trouverStackPane(sceneX, sceneY);

        if (cible != null) {
            int idCible = Integer.parseInt(cible.getId());

            ImageView imageCible = null;

            for (Node n : cible.getChildren()) {
                if (n instanceof ImageView) {
                    imageCible = (ImageView) n;
                }
            }

            if (imageCible != null) {
                // Sauvegarde de l'ancien pictogramme de la case cible
                Pictogramme ancienPictogramme = mapImagePictogramme.get(imageCible);
                Image ancienneImage = imageCible.getImage();

                // Identifiants origine du pictogramme déplacé
                Integer origIndex = (Integer) img.getProperties().get("origIndex");
                String srcCaseId = (String) img.getProperties().get("srcCaseId");

                // Place le pictogramme dans le modèle
                revisionGrille.placerPictogramme(pictogramme, idCible);

                // Met à jour visuellement la case cible
                imageCible.setImage(img.getImage());
                mapImagePictogramme.put(imageCible, pictogramme);
                rendreDeplacable(imageCible);

                // Gère l'ancien pictogramme
                if (ancienPictogramme != null) {
                    if (origIndex != null) {
                        // Vient des propositions : remettre l'ancien dans les propositions
                        if (!revisionGrille.getPropositions().contains(ancienPictogramme)) {
                            revisionGrille.getPropositions().add(origIndex, ancienPictogramme);
                        }
                    } else if (srcCaseId != null) {
                        // Si vient d'une autre case, échange
                        int idOrigine = Integer.parseInt(srcCaseId);
                        revisionGrille.placerPictogramme(ancienPictogramme, idOrigine);

                        for (Node node : conteneurGrille.getChildren()) {
                            if (node instanceof StackPane) {
                                StackPane stack = (StackPane) node;

                                if (srcCaseId.equals(stack.getId())) {
                                    ImageView imageOrigine = null;

                                    for (Node child : stack.getChildren()) {
                                        if (child instanceof ImageView) {
                                            imageOrigine = (ImageView) child;
                                        }
                                    }
                                    if (imageOrigine != null) {
                                        imageOrigine.setImage(ancienneImage);
                                        mapImagePictogramme.put(imageOrigine, ancienPictogramme);
                                        rendreDeplacable(imageOrigine);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else {
            revisionGrille.remettreDansPropositions(pictogramme);
        }
        actualiserPropositions();
    }

    /**
     * Prend en paramètre un StackPane, et lui associe en tooltip la description du pictogramme ayant le même ID
     * @param stack le StackPane sur lequel définir un tooltip
     */
    public void definirTooltip(StackPane stack) {
        Tooltip tooltip = new Tooltip();
        Tooltip.install(stack, tooltip);

        String tooltipText;
        tooltipText = GrilleSST.getGrilleSST().getPictogrammes()[parseInt(stack.getId())].getDescription();
        tooltip.setText(tooltipText);
    }

    /**
     * Actualise la liste visuelle des pictogrammes proposés
     * en fonction de la liste des propositions dans revisionGrille.
     */
    private void actualiserPropositions() {
        containerPropositions.getChildren().clear();

        for (Pictogramme pictogramme : revisionGrille.getPropositions()) {
            if (pictogramme != null) {
                ImageView imageView = new ImageView();
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource(
                            "/iut/ga1/resquizz/" + pictogramme.getLienImage())).toExternalForm());
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.err.println("Image non trouvée : " + pictogramme.getLienImage());
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(
                            "/iut/ga1/resquizz/images/vide.png")).toExternalForm()));
                }

                imageView.setFitWidth(69);
                imageView.setFitHeight(69);
                imageView.setPreserveRatio(true);
                containerPropositions.getChildren().add(imageView);
                rendreDeplacable(imageView);
                mapImagePictogramme.put(imageView, pictogramme);
            }
        }
    }

    /** Réinitialise le modèle, la vue et le chronometre.*/
    public void reinitialiserJeu() {
        sauvegarderPoints = true;

        chronometre.arreterChrono();
        chronometre.reinitialiserChrono();

        for (javafx.scene.Node node : conteneurGrille.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stack = (StackPane) node;
                for (javafx.scene.Node sousNode : stack.getChildren()) {
                    if (sousNode instanceof ImageView) {
                        ImageView imageView = (ImageView) sousNode;
                        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource
                                ("/iut/ga1/resquizz/images/vide.png")).toExternalForm()));
                    }
                }
            }
        }

        containerPropositions.getChildren().clear();
        mapImagePictogramme.clear();
        revisionGrille = new RevisionGrille();
        mettreAJourFeedback();
        points.setText("Points : " + revisionGrille.getPoints());
        sauvegarderPoints = true;
        actualiserPropositions();
    }

    /**
     * Permet de détecter quel StackPane est Présente à certaines coordonées
     * @param sceneX les coordonées X de la scene à verifier
     * @param sceneY les coordonées X de la scene à verifier
     * @return l'image détectée, null si aucune n'est trouvée
     */
    private StackPane trouverStackPane(double sceneX, double sceneY) {
        for (Node node : conteneurGrille.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stack = (StackPane) node;
                for (Node child : stack.getChildren()) {
                    if (child instanceof ImageView) {
                        ImageView img = (ImageView) child;
                        Bounds bounds = img.localToScene(img.getBoundsInLocal());
                        if (bounds.contains(sceneX, sceneY)) {
                            return stack;
                        }
                    }
                }
            }
        }
        return null;
    }

    /** Met à jour le contour des pictogrammes selon la validité des réponses. */
    private void mettreAJourFeedback() {
        for (javafx.scene.Node node : conteneurGrille.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stack = (StackPane) node;
                ImageView imgView = null;
                Rectangle rect = null;

                for (javafx.scene.Node child : stack.getChildren()) {
                    if (child instanceof ImageView) imgView = (ImageView) child;
                    if (child instanceof Rectangle) rect = (Rectangle) child;
                }

                if (imgView != null && rect != null) {
                    int index = parseInt(node.getId());
                    if (grilleValide && revisionGrille.estBonneReponse(index)) {
                        rect.setStroke(Color.LIMEGREEN);
                    } else if (grilleValide && mapImagePictogramme.get(imgView) != null) {
                        rect.setStroke(Color.RED);
                    } else {
                        rect.setStroke(Color.BLACK);
                    }
                }
            }
        }
    }


    /**
     * Met à jour les couleurs des boutons de sélection
     * @param couleur1 de la fenêtre
     * @param couleur2 du titre / du chronomètre
     * @param couleur3 des logos
     */
    public void mettreAJourCouleur(String couleur1, String couleur2, boolean couleur3) {
        if (couleur1 != null) {
            fenetre.setStyle("-fx-background-color: " + couleur1 + ";");
        }
        if (couleur2 != null) {
            chronoLabel.setStyle("-fx-text-fill: " + couleur2 + ";");
        }

        if (couleur3) {
            // noir
            logoParametre.setImage(new Image(Objects.requireNonNull(getClass().getResource
                    ("/iut/ga1/resquizz/images/param.png")).toExternalForm()));
            logoInformation.setImage(new Image(Objects.requireNonNull(getClass().getResource
                    ("/iut/ga1/resquizz/images/info.png")).toExternalForm()));
        } else {
            // blanc
            logoParametre.setImage(new Image(Objects.requireNonNull(getClass().getResource
                    ("/iut/ga1/resquizz/images/param_blanc.png")).toExternalForm()));
            logoInformation.setImage(new Image(Objects.requireNonNull(getClass().getResource
                    ("/iut/ga1/resquizz/images/info_blanc.png")).toExternalForm()));
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {
        String ancienStyleButtonRestart = buttonRestart.getStyle();
        buttonRestart.setStyle(ancienStyleButtonRestart + "-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonValider = buttonValider.getStyle();
        buttonValider.setStyle(ancienStyleButtonValider + "-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-size: " + taillePolice + ";");
        String ancienStylePoints = points.getStyle();
        points.setStyle(ancienStylePoints + "-fx-font-size: " + taillePolice + ";");
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

            String ancienStyleTitre = chronoLabel.getStyle();
            chronoLabel.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleButtonRestart = buttonRestart.getStyle();
            buttonRestart.setStyle(ancienStyleButtonRestart + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonValider = buttonValider.getStyle();
            buttonValider.setStyle(ancienStyleButtonValider + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePoints = points.getStyle();
            points.setStyle(ancienStylePoints + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }
}