/*
 * ControleurQCM.java                                    10 novembre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Chronometre;
import iut.ga1.resquizz.modeles.GrilleSST;
import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.modeles.QCM;
import iut.ga1.resquizz.util.GestionParametres;
import iut.ga1.resquizz.util.Parametres;
import iut.ga1.resquizz.util.GestionClassement;
import iut.ga1.resquizz.util.Pictogramme;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

/**
 * Contrôleur de la vue "JeuQCM"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurQCM {

    /** Identifiant de la fenêtre pour modifier le style */
    @FXML private BorderPane fenetre;
    /** Identifiant du logo Parametre */
    @FXML private ImageView logoParametre;
    /** Identifiant du logo Information */
    @FXML private ImageView logoInformation;
    /** Identifiant du Label des points */
    @FXML private Label points;
    /** Label pour afficher le chronomètre */
    @FXML private Label chronoLabel;
    /** Conteneur de toute la structure de la grille */
    @FXML private GridPane conteneurGrille;
    /** Identifiant du bouton de restart */
    @FXML
    private Button buttonRestart;
    /** Identifiant du bouton de validation */
    @FXML
    private Button buttonValider;
    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Déclaration des identifiants des pictogrammes */
    @FXML private ImageView Picto0, Picto1, Picto2, Picto3, Picto4, Picto5, Picto6, Picto7, Picto8, Picto9;
    /** Déclaration des identifiants labels */
    @FXML private Label label0, label1, label2, label3, label4, label5, label6, label7, label8, label9;
    /** Déclaration des identifiants des rectangles des pictogrammes */
    @FXML private Rectangle rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9;
    /** Déclaration des identifiants des rectangles des labels */
    @FXML private Rectangle rectLabel0, rectLabel1, rectLabel2, rectLabel3, rectLabel4, rectLabel5, rectLabel6, rectLabel7, rectLabel8, rectLabel9;

    /** création d'une grille pour QCM */
    private final QCM QCM = new QCM();

    /** Chronomètre pour mesurer le temps écoulé */
    private final Chronometre CHRONOMETRE = new Chronometre();

    /** List des modes de jeu */
    private List<Boolean> modes; // true = description visible, false = pictogramme visible

    /** Indique si le mode de jeu a déjà été choisi au démarrage du jeu */
    public boolean modeDejaChoisi = false;

    /** Savoir si l'utilisateur à cliquer pour la premiere fois sur le bouton valider */
    private boolean sauvegarderPoints;
    private String modeSelectionne;

    /** Méthode qui s'éxecute au lancement de l'application */
    @FXML
    public void initialize() {
        initialiserQCM();
    }

    /** Initialise ou redémarre le QCM */
    private void initialiserQCM() {

        CHRONOMETRE.arreterChrono();
        CHRONOMETRE.reinitialiserChrono();

        viderLabelsDescription();

        reinitialiserCouleursRectangles();
        reinitialiserPlaces();

        sauvegarderPoints = true;

        points.setText("Points : 0");

        CHRONOMETRE.getTempsEcouleProperty().addListener((obs, oldV, newV) -> {
            long totalSeconds = newV.longValue() / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            chronoLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });

        List<Pictogramme> pictos = new ArrayList<>(Arrays.asList(GrilleSST.getGrilleSST().getPictogrammes()));
        Collections.shuffle(pictos);
        QCM.setPictogrammesJeu(pictos.subList(0, 10));

        QCM.getPlaces().clear();
        for (int i = 0; i < 10; i++) {
            QCM.getPlaces().add(null);
        }
    }

    /** Permet de choisir le mode jeu souhaité */
    public void choisirJeu() {

        Alert mode = new Alert(Alert.AlertType.INFORMATION);
        mode.setTitle("Choix du mode de jeu");
        mode.setHeaderText("Sélection du mode de jeu");

        Label contenu = getLabel();

        mode.getDialogPane().setContent(contenu);

        ButtonType PictoButton = new ButtonType("Pictogramme");
        ButtonType DescButton = new ButtonType("Description");
        ButtonType deuxButton = new ButtonType("Aléatoirement");
        mode.getButtonTypes().setAll(PictoButton, DescButton, deuxButton);

        mode.getDialogPane().setMinWidth(420);

        Optional<ButtonType> result = mode.showAndWait();
        if (result.isPresent() && result.get() == PictoButton) {
            modes = jeuPictogramme();
            modeSelectionne = "Pictogramme";
        } else if (result.isPresent() && result.get() == DescButton) {
            modes = jeuDescription();
            modeSelectionne = "Description";
        } else if (result.isPresent() && result.get() == deuxButton) {
            modes = jeuAleatoire();
            modeSelectionne = "Aleatoire";
        }
            afficherElementsSelonMode(modes);
    }

    /** Permet de choisir le mode jeu souhaité */
    public void choisirJeuLancement() {

        Alert mode = new Alert(Alert.AlertType.INFORMATION);
        mode.setTitle("Choix du mode de jeu");
        mode.setHeaderText("Sélection du mode de jeu");

        Label contenu = getLabel();

        mode.getDialogPane().setContent(contenu);

        ButtonType AnnulerButton = new ButtonType("Annuler");
        ButtonType PictoButton = new ButtonType("Pictogramme");
        ButtonType DescButton = new ButtonType("Description");
        ButtonType deuxButton = new ButtonType("Aléatoirement");
        mode.getButtonTypes().setAll(AnnulerButton, PictoButton, DescButton, deuxButton);

        mode.getDialogPane().setMinWidth(420);

        Stage stage = (Stage) mode.getDialogPane().getScene().getWindow();
        //permet de gérer le clic sur la croix de la fenêtre
        stage.setOnCloseRequest(event -> mode.setResult(AnnulerButton));

        Optional<ButtonType> result = mode.showAndWait();
        if (result.isPresent() && result.get() == PictoButton) {
            modes = jeuPictogramme();
            modeSelectionne = "Pictogramme";
            afficherElementsSelonMode(modes);
        } else if (result.isPresent() && result.get() == DescButton) {
            modes = jeuDescription();
            modeSelectionne = "Description";
            afficherElementsSelonMode(modes);
        } else if (result.isPresent() && result.get() == deuxButton) {
            modes = jeuAleatoire();
            modeSelectionne = "Aleatoire";
            afficherElementsSelonMode(modes);
        } else if (result.isPresent() && result.get() == AnnulerButton) {
            Lanceur.activerFenetreJeux();
        } else {
            Lanceur.activerFenetreJeux();
        }

    }

    /** Label des différents modes de jeu */
    private static Label getLabel() {
        String explication =
                "Trois modes de jeu sont disponibles :\n\n"
                        + "• Pictogramme : vous devez associer la bonne image à chaque description affichée.\n"
                        + "   → Idéal pour apprendre à reconnaître les pictogrammes à partir de leur signification.\n\n"
                        + "• Description : vous devez associer la bonne description à chaque pictogramme affiché.\n"
                        + "   → Parfait pour tester vos connaissances sur la signification des pictogrammes.\n\n"
                        + "• Aléatoirement : le mode est choisi au hasard pour chaque ligne.\n"
                        + "   → Un mélange des deux modes pour un défi complet !";

        Label contenu = new Label(explication);
        contenu.setWrapText(true);
        return contenu;
    }

    /** Permet de réinitialiser les places */
    private void reinitialiserPlaces() {
        QCM.getPlaces().clear();
        for (int i = 0; i < 10; i++) {
            QCM.getPlaces().add(null);
        }
    }

    /**
     * Permet de choisir le mode de jeu à lancer pour chaque ligne
     * Le jeu pictogramme se lance avec une arraylist contenant uniquement des 'true'
     * @return list une ArrayList<Boolean>
     */
    private ArrayList<Boolean> jeuPictogramme() {
        ArrayList<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(true);
        }
        return list;
    }

    /**
     * Permet de choisir le mode de jeu à lancer pour chaque ligne
     * Le jeu description se lance avec une arraylist contenant uniquement des 'false'
     * @return list une ArrayList<Boolean>
     */
    private ArrayList<Boolean> jeuDescription() {
        ArrayList<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(false);
        }
        return list;
    }

    /**
     * Permet de choisir le mode de jeu à lancer pour chaque ligne
     * Le jeu aléatoire se lance avec une arraylist contenant aléatoirement des 'true' et des 'false'
     * @return list une ArrayList<Boolean>
     */
    private ArrayList<Boolean> jeuAleatoire() {
        ArrayList<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(Math.random() < 0.5);
        }
        return list;
    }

    /**
     * Permet d'afficher les éléments du jeu en fonction du mode sélectionné
     * @param modes le modes du jeu
     */
    public void afficherElementsSelonMode(List<Boolean> modes) {
        Label[] labels = { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9 };
        ImageView[] images = { Picto0, Picto1, Picto2, Picto3, Picto4, Picto5, Picto6, Picto7, Picto8, Picto9 };
        Rectangle[] rects = { rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9 };
        Rectangle[] rectsLabel = { rectLabel0, rectLabel1, rectLabel2, rectLabel3, rectLabel4, rectLabel5, rectLabel6, rectLabel7, rectLabel8, rectLabel9 };
        List<Pictogramme> selection = QCM.getPictogrammesJeu();

        for (int i = 0; i < 10; i++) {
            // Remise à zéro systématique de l'état visuel et des handlers
            rects[i].setStroke(Color.BLACK);
            rects[i].setStrokeWidth(1);
            rectsLabel[i].setStroke(Color.BLACK);
            rectsLabel[i].setStrokeWidth(1);


            images[i].setOnMouseClicked(null);
            labels[i].setOnMouseClicked(null);

            if (modes.get(i)) {

                labels[i].setText(selection.get(i).getDescription());
                images[i].setImage(new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/iut/ga1/resquizz/images/vide.png"))));
                ajouterClicsImageView(images[i], i);

                rects[i].setStrokeWidth(3);

                labels[i].setTextFill(Color.BLACK);
                rectsLabel[i].setStrokeWidth(1);
            } else {

                labels[i].setText("???");
                images[i].setImage(new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/iut/ga1/resquizz/" + selection.get(i).getLienImage()))));
                ajouterClicsLabel(labels[i], i);

                rectsLabel[i].setStrokeWidth(3);

                images[i].setOnMouseClicked(null);
                rects[i].setStrokeWidth(1);
            }
        }
    }

    /**
     * Permet d'ajouter à une image, le fait de pouvoir cliquer et de choisir un pictogramme
     * @param imageView l'image à cliquer
     * @param index de l'image
     */
    private void ajouterClicsImageView(ImageView imageView, int index) {
        imageView.setOnMouseClicked(null); // enlever ancien handler si existant
        imageView.setOnMouseClicked(e -> gererClicImageView(imageView, index));
    }

    /**
     * Permet de gerer le clic de l'image
     * @param imageView l'image cliquée
     * @param index de l'image
     */
    private void gererClicImageView(ImageView imageView, int index) {
        CHRONOMETRE.demarrerChrono();

        Dialog<Pictogramme> fenetre = new Dialog<>();
        fenetre.setTitle("Choisissez un pictogramme");
        fenetre.setHeaderText("Sélectionnez le pictogramme correspondant à la description");

        GridPane grid = new GridPane();
        grid.setHgap(10); // espace horizontal entre les images
        grid.setVgap(10); // espace vertical entre les images
        grid.setPadding(new Insets(10));

        int colonnes = 9;
        int ligne = 0;
        int col = 0;

        for (int i = 0; i < QCM.getPropositions().size(); i++) {
            Pictogramme p = QCM.getPropositions().get(i);
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/iut/ga1/resquizz/" + p.getLienImage()))));
            iv.setFitHeight(60);
            iv.setPreserveRatio(true);
            iv.setCursor(Cursor.HAND);

            iv.setOnMouseClicked(e -> fenetre.setResult(p)); // clic sur l'image choisit le pictogramme

            grid.add(iv, col, ligne);

            col++;
            if (col >= colonnes) {
                col = 0;
                ligne++;
            }
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(210); // hauteur du viewport pour voir plusieurs lignes

        fenetre.getDialogPane().setContent(scroll);
        fenetre.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        fenetre.setResultConverter(button -> {
            if (button == ButtonType.CANCEL) return null;
            return fenetre.getResult();
        });

        Optional<Pictogramme> result = fenetre.showAndWait();
        result.ifPresent(choisi -> {
            imageView.setImage(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/iut/ga1/resquizz/" + choisi.getLienImage()))));
            QCM.getPlaces().set(index, choisi);
        });
    }

    /**
     * Permet d'ajouter à un label, le fait de pouvoir cliquer et de choisir une description
     * @param label le label à cliquer
     * @param index du label
     */
    private void ajouterClicsLabel(Label label, int index) {
        label.setOnMouseClicked(null);
        label.setOnMouseClicked(e -> gererClicLabel(label, index));
    }

    /**
     * Permet de gerer le clic de la description
     * @param label la description à cliquer
     * @param index de la description
     */
    private void gererClicLabel(Label label, int index) {
        CHRONOMETRE.demarrerChrono();

        Dialog<String> fenetre = new Dialog<>();
        fenetre.setTitle("Choisissez une description");
        fenetre.setHeaderText("Sélectionnez la bonne description pour ce pictogramme");

        // Création de la grille 3 colonnes × n lignes
        GridPane grid = new GridPane();
        grid.setHgap(10); // espace horizontal entre les cellules
        grid.setVgap(10); // espace vertical entre les cellules
        grid.setPadding(new Insets(10));

        int colonnes = 3;
        for (int i = 0; i < QCM.getPropositions().size(); i++) {
            String desc = QCM.getPropositions().get(i).getDescription();

            // Utilisation d'un Label pour gérer le texte long
            Label lbl = new Label(desc);
            lbl.setWrapText(true); // texte multi-lignes
            lbl.setMaxWidth(180); // largeur maximale de la cellule
            lbl.setPrefWidth(180);
            lbl.setPrefHeight(Region.USE_COMPUTED_SIZE); // hauteur auto selon le texte
            lbl.setAlignment(Pos.CENTER_LEFT); // alignement du texte
            lbl.setPadding(new Insets(5));

            // StackPane cliquable autour du Label
            StackPane cell = new StackPane(lbl);
            cell.setStyle("-fx-border-color: black; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
            cell.setOnMouseClicked(e -> fenetre.setResult(desc));

            // Ajout à la grille
            grid.add(cell, i % colonnes, i / colonnes);
        }

        // ScrollPane pour rendre la grille scrollable si nécessaire
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(600);
        scroll.setPrefViewportHeight(500);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // pas de scroll horizontal
        fenetre.getDialogPane().setContent(scroll);

        // Ajouter le bouton Annuler
        fenetre.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        fenetre.setResultConverter(button -> {
            if (button == ButtonType.CANCEL) return null;
            return fenetre.getResult();
        });

        Optional<String> result = fenetre.showAndWait();
        result.ifPresent(s -> {
            label.setText(s);
            QCM.getPropositions().stream()
                    .filter(p -> p.getDescription().equals(s))
                    .findFirst()
                    .ifPresent(choisi -> QCM.getPlaces().set(index, choisi));
        });
    }

    /**
     * Permet de modifier la police de la fenêtre
     * @param cheminPolice à modifier
     */
    public void mettreAJourPolice(String cheminPolice) {
        Font font = Font.loadFont(getClass().getResourceAsStream(cheminPolice), 16);

        Label[] labelPo = { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9 };

        if (font != null) {
            fenetre.setStyle(fenetre.getStyle() + "-fx-font-family: '" + font.getFamily() + "';");
            chronoLabel.setStyle(chronoLabel.getStyle() + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleButtonRestart = buttonRestart.getStyle();
            buttonRestart.setStyle(ancienStyleButtonRestart + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonValider = buttonValider.getStyle();
            buttonValider.setStyle(ancienStyleButtonValider + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePoints = points.getStyle();
            points.setStyle(ancienStylePoints + "-fx-font-family: '" + font.getFamily() + "';");

            for (Label label : labelPo) {
                label.setStyle("-fx-font-family: '" + font.getFamily() + "'; -fx-font-size: 16px;");
            }

        }
    }

    /**
     * Met à jour les couleurs des boutons de sélection
     * @param couleur1 de la fenêtre
     * @param couleur2 du titre
     * @param couleur3 des logos
     */
    public void mettreAJourCouleur(String couleur1, String couleur2, boolean couleur3) {
        if (couleur1 != null)
            fenetre.setStyle(fenetre.getStyle() + "-fx-background-color: " + couleur1 + ";");
        if (couleur2 != null)
            chronoLabel.setStyle(chronoLabel.getStyle() + "-fx-text-fill: " + couleur2 + ";");

        if (couleur3) {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info.png"));
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param.png"));
        } else {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info_blanc.png"));
            logoParametre.setImage(chargerImage("/iut/ga1/resquizz/images/param_blanc.png"));
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     *
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
     * Charge une image depuis le classpath en évitant les NullPointerException.
     * @param chemin chemin vers la ressource (ex: "/iut/ga1/resquizz/images/info.png")
     * @return l'image si trouvée, sinon null
     */
    private Image chargerImage(String chemin) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(chemin)));
    }

    /** Permet de réinitialiser la couleur des rectangles */
    private void reinitialiserCouleursRectangles() {
        Rectangle[] rects = { rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9 };
        Label[] labels = { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9 };

        for (Rectangle r : rects) {
            r.setStroke(Color.BLACK);
            r.setStrokeWidth(1);
        }
        for (Label l : labels) {
            l.setTextFill(Color.BLACK);
        }
    }

    /** Pour lancer la fenêtre des Jeux une fois fini */
    @FXML
    public void gererClicRetourFin() {
        CHRONOMETRE.arreterChrono();
        CHRONOMETRE.reinitialiserChrono();
        modeDejaChoisi = false;
        Lanceur.activerFenetreJeux();
    }

    /** Pour lancer la fenêtre des Jeux */
    @FXML
    public void gererClicRetour() {
        if (CHRONOMETRE.getTempsEcoule() != 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Retour au menu");
            alert.setHeaderText("Attention !");
            alert.setContentText("Si vous retournez au menu, vous perdrez votre progression. Êtes-vous sûr ?");

            ButtonType ouiButton = new ButtonType("Oui");
            ButtonType nonButton = new ButtonType("Non");
            alert.getButtonTypes().setAll(ouiButton, nonButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ouiButton) {
                CHRONOMETRE.arreterChrono();
                CHRONOMETRE.reinitialiserChrono();
                modeDejaChoisi = false;
                redemarrerJeu();
                Lanceur.activerFenetreJeux();
            }
        } else {
            modeDejaChoisi = false;
            redemarrerJeu();
            Lanceur.activerFenetreJeux();
        }
    }

    /** Pour lancer la fenêtre des informations du jeu */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Jeu QCM");
        alert.setContentText(
                "Répondez à un questionnaire à choix multiples."
                +" Associez chaque pictogramme à sa définition, ou l’inverse."
                +" Idéal pour tester et renforcer vos connaissances.");
        alert.showAndWait();
    }

    /** Pour lancer la fenêtre des paramètres */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("QCM");
        Lanceur.activerFenetreParametres();
        CHRONOMETRE.arreterChrono();
    }


    /** Pour lancer la méthode de réinitialisation de la grille */
    @FXML
    public void gererClicRestart() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirmation de redémarrage");
        alert.setHeaderText("Redémarrer la partie ?");
        alert.setContentText("Votre progression actuelle sera perdue.\n\nSouhaitez-vous vraiment recommencer ?");

        ButtonType boutonConfirmer = new ButtonType("Oui, redémarrer");
        ButtonType boutonAnnuler = new ButtonType("Non, annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(boutonConfirmer, boutonAnnuler);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == boutonConfirmer) {
            // Redémarrer la partie
            redemarrerJeu();
            choisirJeu();
        }
    }

    /** Remet tous les labels de description à vide */
    private void viderLabelsDescription() {
        Label[] labels = { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9 };
        for (Label l : labels) {
            l.setText("");
        }
    }

    /** Méthode permettant de redémarrer le jeu */
    public void redemarrerJeu() {
        for (javafx.scene.Node node : conteneurGrille.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stack = (StackPane) node;
                for (javafx.scene.Node child : stack.getChildren()) {
                    if (child instanceof ImageView) {
                        ImageView img = (ImageView) child;
                        img.setImage(new Image(Objects.requireNonNull(
                                getClass().getResourceAsStream("/iut/ga1/resquizz/images/vide.png"))));
                        // désactiver le clic par défaut
                        img.setOnMouseClicked(null);
                    } else if (child instanceof Rectangle) {
                        Rectangle rect = (Rectangle) child;
                        rect.setStroke(Color.BLACK);
                        rect.setStrokeWidth(1);
                    } else if (child instanceof Label) {
                        child.setOnMouseClicked(null);
                    }
                }
            }
        }

        initialiserQCM();
        CHRONOMETRE.arreterChrono();
        CHRONOMETRE.reinitialiserChrono();
        points.setText("Points : 0");
    }

    /** Pour lancer la méthode de validation de la grille */
    @FXML
    public void gererClicValider() {
        Label[] labels = { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9 };
        Rectangle[] rects = { rect0, rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9 };
        Rectangle[] rectsLabel = { rectLabel0, rectLabel1, rectLabel2, rectLabel3, rectLabel4, rectLabel5, rectLabel6, rectLabel7, rectLabel8, rectLabel9 };

        QCM.calculPoints();
        points.setText("Points : " + QCM.getPoints());

        if(sauvegarderPoints) {
            if (Objects.equals(modeSelectionne, "Pictogramme")) {
                sauvegarderPoints = false;
                if (QCM.getPoints() > GestionClassement.getPointQCMPicto()) {
                    GestionClassement.setPointQCMPicto(QCM.getPoints());
                    GestionClassement.setChronoQCMPicto(CHRONOMETRE.getTempsEcoule());
                    GestionParametres.sauvegarderClassement();
                } else if (QCM.getPoints() == GestionClassement.getPointQCMPicto()
                    && CHRONOMETRE.getTempsEcoule() < GestionClassement.getChronoQCMPicto()) {
                    GestionClassement.setChronoQCMPicto(CHRONOMETRE.getTempsEcoule());
                    GestionParametres.sauvegarderClassement();
                }
            }
            if (Objects.equals(modeSelectionne, "Description")) {
                sauvegarderPoints = false;
                if (QCM.getPoints() > GestionClassement.getPointQCMDesc()) {
                    GestionClassement.setPointQCMDesc(QCM.getPoints());
                    GestionClassement.setChronoQCMDesc(CHRONOMETRE.getTempsEcoule());
                    GestionParametres.sauvegarderClassement();
                } else if (QCM.getPoints() == GestionClassement.getPointQCMDesc()
                        && CHRONOMETRE.getTempsEcoule() < GestionClassement.getChronoQCMDesc()) {
                    GestionClassement.setChronoQCMDesc(CHRONOMETRE.getTempsEcoule());
                    GestionParametres.sauvegarderClassement();
                }
            }
            if (Objects.equals(modeSelectionne, "Aleatoire")) {
                sauvegarderPoints = false;
                if (QCM.getPoints() > GestionClassement.getPointQCMAlea()) {
                    GestionClassement.setPointQCMAlea(QCM.getPoints());
                    GestionClassement.setChronoQCMAlea(CHRONOMETRE.getTempsEcoule());
                    GestionParametres.sauvegarderClassement();
                } else if (QCM.getPoints() == GestionClassement.getPointQCMAlea()
                        && CHRONOMETRE.getTempsEcoule() < GestionClassement.getChronoQCMAlea()) {
                        GestionClassement.setChronoQCMAlea(CHRONOMETRE.getTempsEcoule());
                        GestionParametres.sauvegarderClassement();
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            Pictogramme place = (i < QCM.getPlaces().size()) ? QCM.getPlaces().get(i) : null;
            boolean bonne = QCM.estBonneReponse(i, place);
            Color couleur = bonne ? Color.LIMEGREEN : Color.RED;

            if (modes.get(i)) {
                // Mode : description visible → clic sur pictogramme
                rects[i].setStroke(couleur);
                rects[i].setStrokeWidth(3);
                labels[i].setTextFill(Color.BLACK); // le label reste noir
            } else {
                // Mode inverse : pictogramme visible → clic sur label
                rectsLabel[i].setStroke(couleur);
                rectsLabel[i].setStrokeWidth(3);
                labels[i].setTextFill(couleur);
            }
        }

        if (QCM.estBonneReponse()) {
            CHRONOMETRE.arreterChrono();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Félicitations !");
            alert.setHeaderText("QCM complet");
            alert.setContentText("Bravo ! Vous avez placé tous les pictogrammes correctement.");

            ButtonType restartButton = new ButtonType("Restart");
            ButtonType retourMenuButton = new ButtonType("Retour au menu");
            alert.getButtonTypes().setAll(restartButton, retourMenuButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == restartButton) gererClicRestart();
                else if (result.get() == retourMenuButton) gererClicRetourFin();
            }
        }
    }
}