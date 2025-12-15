/*
 * RevisionGrille.java               17 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Chronometre;

import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.modeles.ScenarioFixeComplet;
import iut.ga1.resquizz.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur du mode de jeu "Scénario fixe complet".
 * <p>
 * Ce mode demande au joueur de placer correctement des pictogrammes
 * dans l'ordre d'une séquence logique pour
 * résoudre un scénario d'accident.
 * </p>
 *
 * <p>Fonctionnalités principales :</p>
 * <ul>
 *     <li>Gestion du chronomètre</li>
 *     <li>Affichage dynamique des pictogrammes</li>
 *     <li>Validation de la séquence de jeu</li>
 *     <li>Navigation (retour, redémarrage, paramètres, info)</li>
 * </ul>
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class ControleurScenarioFixeComplet {

    /** Label contenant la description du scénario à résoudre */
    @FXML
    private Label scenarioTextLabel;

    /** Label du chronomètre */
    @FXML
    private Label chronoLabel;

    /** Les 5 emplacements pour déposer les pictogrammes */
    @FXML private ImageView imageView1;
    @FXML private ImageView imageView2;
    @FXML private ImageView imageView3;
    @FXML private ImageView imageView4;
    @FXML private ImageView imageView5;

    /** Rectangles derrière les ImageViews */
    @FXML private Rectangle rect1;
    @FXML private Rectangle rect2;
    @FXML private Rectangle rect3;
    @FXML private Rectangle rect4;
    @FXML private Rectangle rect5;

    /** Identifiant du bouton restart */
    @FXML
    private Button buttonRestart;

    /** Identifiant du bouton valider */
    @FXML
    private Button buttonValider;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Conteneur des pictogrammes proposés (en bas de la fenêtre) */
    @FXML
    private HBox containerPropositions;

    /** Fenêtre principale (pour changer le style et la couleur) */
    @FXML
    private BorderPane fenetre;

    /** Label affichant les points du joueur */
    @FXML
    private Label pointsLabel;

    /** Logo du bouton d'information */
    @FXML
    private ImageView logoInformation;

    /** Logo du bouton des paramètres */
    @FXML
    private ImageView logoParametre;

    /** Activer la sauvegarde des points ou non (false après la première validation */
    private boolean sauvegarderPoints;

    /** Chronomètre utilisé pour mesurer la durée de la partie */
    private final Chronometre CHRONOMETRE = new Chronometre();

    /** Tableau contenant les 5 slots d'images */
    private ImageView[] emplacementsImageViews;

    /** Scénario actuel du mode de jeu */
    private ScenarioFixeComplet scenarioFixeComplet;

    /** Liste représentant la séquence placée par l'utilisateur */
    private List<Pictogramme> sequenceUtilisateur;

    /** Pictogramme actuellement sélectionné dans les propositions */
    private Pictogramme pictogrammeSelectionne = null;

    /**
     * Méthode appelée automatiquement à l'ouverture de la vue FXML.
     * Initialise le scénario, la grille et les écouteurs.
     */
    @FXML
    public void initialize() {
        scenarioFixeComplet = new ScenarioFixeComplet();
        sequenceUtilisateur = new ArrayList<>();
        sauvegarderPoints = true;

        // Initialise les 5 slots dans un tableau
        emplacementsImageViews = new ImageView[] {
                imageView1, imageView2, imageView3, imageView4, imageView5
        };

        // Mise à jour du label du chrono en temps réel
        CHRONOMETRE.getTempsEcouleProperty().addListener((obs, oldV, newV) -> {
            long totalSeconds = newV.longValue() / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            chronoLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });

        // Affiche la description du scénario au lancement
        if (scenarioTextLabel != null) {
            scenarioTextLabel.setText(
                    scenarioFixeComplet.getDescriptionScenario());
        }

        // Initialise les slots vides et affiche les pictogrammes proposés
        initialiserSlotsGrille();
        afficherPropositions();
    }

    /**
     * Sélectionne un pictogramme dans la liste de propositions.
     * @param imageClicked pictogramme cliqué dans la HBox
     */
    private void selectionnerPictogramme(ImageView imageClicked) {
        /*
         * Récupération du pictogramme associé à l’image sur laquelle
         * l’utilisateur vient de cliquer.
         */
        pictogrammeSelectionne = (Pictogramme) imageClicked.getUserData();

        // Supprime le style des autres pictogrammes (désélection)
        for (javafx.scene.Node node : containerPropositions.getChildren()) {
            node.setStyle("");
        }

        // Met un contour bleu sur le pictogramme sélectionné
        imageClicked.setStyle(
                "-fx-effect: dropshadow(gaussian, blue, 5, 0.5, 0, 0); " +
                        "-fx-border-color: blue; " +
                        "-fx-border-width: 2;"
        );

        CHRONOMETRE.demarrerChrono();
    }

    /**
     * Initialise les slots de la grille (vides et cliquables).
     */
    private void initialiserSlotsGrille() {
        for (ImageView slot : emplacementsImageViews) {
            if (slot != null) {
                // Gestion du clic sur chaque emplacement
                slot.setOnMouseClicked(this::gererClicSlot);
                slot.setImage(null);
                slot.setUserData(null);
                slot.setStyle("");
            }
        }
    }

    /**
     * Gère le clic sur un emplacement de la grille.
     * Si un pictogramme est sélectionné, on le place.
     * Sinon, on retire le pictogramme déjà présent.
     * @param event clic souris sur un slot
     */
    @FXML
    private void gererClicSlot(javafx.scene.input.MouseEvent event) {
         // Identifier quelle image l’utilisateur vient de cliquer
        ImageView slotClicked = (ImageView) event.getSource();

        // On récupère le rectangle correspondant au slot
        Rectangle rectActuel = null;
        switch (slotClicked.getId()) {
            case "imageView1":
                rectActuel = rect1;
                break;
            case "imageView2":
                rectActuel = rect2;
                break;
            case "imageView3":
                rectActuel = rect3;
                break;
            case "imageView4":
                rectActuel = rect4;
                break;
            case "imageView5":
                rectActuel = rect5;
                break;
        }

        // Cas où un pictogramme a été sélectionné
        if (pictogrammeSelectionne != null) {
            // Si le slot contient déjà un pictogramme, on le remet dans la liste
            if (slotClicked.getUserData() instanceof Pictogramme) {
                remettrePictogrammeDansPropositions(
                        (Pictogramme) slotClicked.getUserData());
            }

            // Placer le pictogramme sélectionné
            slotClicked.setImage(chargerImage(
                    pictogrammeSelectionne.getLienImage()));
            slotClicked.setUserData(pictogrammeSelectionne);

            // Supprime le pictogramme placé de la HBox
            containerPropositions.getChildren().removeIf(node ->
                    node.getUserData().equals(pictogrammeSelectionne)
            );

            // Réinitialiser la sélection
            pictogrammeSelectionne = null;

            // Met à jour la séquence utilisateur
            sequenceUtilisateur.clear();
            for (ImageView slot : emplacementsImageViews) {
                if (slot.getUserData() instanceof Pictogramme) {
                    sequenceUtilisateur.add((Pictogramme) slot.getUserData());
                }
            }
        } else if (slotClicked.getUserData() instanceof Pictogramme) {
            /*
             * Cas où clique sur un slot déjà rempli
             * sans avoir sélectionné un pictogramme
             */
            Pictogramme p = (Pictogramme) slotClicked.getUserData();
            sequenceUtilisateur.remove(p);
            slotClicked.setImage(null);
            slotClicked.setUserData(null);
            remettrePictogrammeDansPropositions(p);

            // Remet le contour noir au rectangle
            if (rectActuel != null) {
                rectActuel.setStroke(javafx.scene.paint.Color.BLACK);
            }
        }
    }

    /**
     * Remet un pictogramme retiré dans la HBox des propositions.
     * @param p pictogramme à remettre
     */
    private void remettrePictogrammeDansPropositions(Pictogramme p) {
        ImageView pictogrammeSelect = creerImageViewProposition(p);
        // Remet le pictogramme à la fin de la HBox
        containerPropositions.getChildren().add(pictogrammeSelect);
    }

    /**
     * Met à jour l’affichage des 5 slots de la grille
     * selon la séquence utilisateur.
     */
    private void mettreAJourAffichageGrille() {
        // Vide les slots
        for (ImageView slot : emplacementsImageViews) {
            slot.setImage(null);
            slot.setUserData(null);
        }

        // Ré-affiche les pictogrammes de la séquence
        for (int i = 0; i < sequenceUtilisateur.size() &&
                i < emplacementsImageViews.length; i++) {
            Pictogramme p = sequenceUtilisateur.get(i);
            ImageView slot = emplacementsImageViews[i];
            slot.setImage(chargerImage(p.getLienImage()));
            slot.setUserData(p);
        }

        for (Rectangle rect : new Rectangle[]
                {rect1, rect2, rect3, rect4, rect5}) {
            rect.setStroke(javafx.scene.paint.Color.BLACK);
        }
    }

    /**
     * Crée un ImageView cliquable pour afficher un pictogramme dans les propositions.
     * @param p pictogramme à afficher
     * @return ImageView configuré
     */
    private ImageView creerImageViewProposition(Pictogramme p) {
        ImageView pictogrammeSelect = new ImageView(
                chargerImage(p.getLienImage()));
        pictogrammeSelect.setFitHeight(69);
        pictogrammeSelect.setFitWidth(69);
        pictogrammeSelect.setUserData(p);

        // Définir le comportement de clic (placer dans la grille)
        pictogrammeSelect.setOnMouseClicked(event ->
                selectionnerPictogramme((ImageView) event.getSource())
        );

        return pictogrammeSelect;
    }

    /**
     * Affiche tous les pictogrammes disponibles dans la HBox.
     */
    private void afficherPropositions() {
        containerPropositions.getChildren().clear();
        for (Pictogramme p : scenarioFixeComplet.getPropositions()) {
            ImageView pictogrammeIV = creerImageViewProposition(p);
            containerPropositions.getChildren().add(pictogrammeIV);
        }
    }

    /**
     * Gère le clic sur le logo "Informations".
     * Affiche les règles du mode de jeu.
     */
    @FXML
    void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Scénario fixe complet");
        final String informations = "Placez les pictogrammes dans l’ordre " +
                " correct pour traiter le scénario d’accident.";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /**
     * Ouvre la fenêtre des paramètres du jeu.
     */
    @FXML
    void gererClicParametres() {
        Parametres.setChemin("ScenarioFixe");
        Lanceur.activerFenetreParametres();
        CHRONOMETRE.arreterChrono();
    }

    /** Réinitialise la partie : scénario, points, chrono et grille. */
    @FXML
    void gererClicRestart() {
        sauvegarderPoints = true;
        // Afficher un popup de confirmation avec icône d'avertissement
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
            reinitialiserJeu();
        }
    }

    /** Méthode permettant de réinitialiser le jeu */
    public void reinitialiserJeu() {
        // Redémarrer la partie
        scenarioFixeComplet = new ScenarioFixeComplet();

        // Réinitialiser la séquence
        sequenceUtilisateur.clear();
        mettreAJourAffichageGrille();

        // Re afficher le texte du scénario choisi
        scenarioTextLabel.setText(scenarioFixeComplet.getDescriptionScenario());

        // Re afficher les pictogrammes proposés
        afficherPropositions();

        CHRONOMETRE.arreterChrono();
        CHRONOMETRE.reinitialiserChrono();

        if (pointsLabel != null) {
            pointsLabel.setText("Points : 0");
        }
    }

    /** Permet de retourner à la vue précédente */
    @FXML
    void gererClicRetour() {
        if (CHRONOMETRE.getTempsEcoule() != 0) {

            // Créer un alert de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Retour au menu");
            alert.setHeaderText("Attention !");
            alert.setContentText("Si vous retournez au menu, "+
                    " vous perdrez votre progression. Êtes-vous sûr ?");

            // Boutons personnalisés Oui / Non
            ButtonType ouiButton = new ButtonType("Oui");
            ButtonType nonButton = new ButtonType("Non");

            alert.getButtonTypes().setAll(ouiButton, nonButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ouiButton) {
                // L'utilisateur confirme → réinitialiser la grille et revenir au menu
                reinitialiserJeu();
                Lanceur.activerFenetreJeux(); // retour au menu
                // else
                // L'utilisateur annule → ne fait rien
                // (la fenêtre reste ouverte)
            }
        } else {
            reinitialiserJeu();
            Lanceur.activerFenetreJeux();
        }
    }

    /**
     * Gère le clic sur "Valider".
     * Calcule le score et affiche le résultat.
     */
    @FXML
    void gererClicValider() {
        if (sequenceUtilisateur.size() <
                scenarioFixeComplet.getSequenceCorrecte().size()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Action impossible");
            alert.setHeaderText("Grille incomplète");
            alert.setContentText("Vous devez remplir les 5 emplacements avant de valider !");
            alert.showAndWait();
            return;
        }

        // Si on arrive ici, c'est que les 5 slots sont remplis

        // Calcul des points
        scenarioFixeComplet.calculPoints(sequenceUtilisateur);
        int points = scenarioFixeComplet.getPoints();

        // On récupère le score maximum possible
        int scoreMax = scenarioFixeComplet.getSequenceCorrecte().size() * 100;

        // Met à jour le label des points
        pointsLabel.setText("Points : " + points);

        // Met à jour le classement si nouveau record
        if (sauvegarderPoints) {
            sauvegarderPoints = false;
            if (points > GestionClassement.getPointScenarioFC()) {
                GestionClassement.setPointScenarioFC(points);
                GestionClassement.setChronoScenarioFC(CHRONOMETRE.getTempsEcoule());
                GestionParametres.sauvegarderClassement();
                System.out.println("Points : " + points);
                System.out.println("Points : " + GestionClassement.getPointScenarioFC());
            } else if (points == GestionClassement.getPointScenarioFC()
                    && CHRONOMETRE.getTempsEcoule() < GestionClassement.getChronoScenarioFC()) {
                GestionClassement.setChronoScenarioFC(CHRONOMETRE.getTempsEcoule());
            }
        }


        // Vérification pictogramme par pictogramme
        List<Pictogramme> sequenceCorrecte = scenarioFixeComplet.getSequenceCorrecte();
        for (int i = 0; i < emplacementsImageViews.length; i++) {
            ImageView slot = emplacementsImageViews[i];
            Rectangle[] rectangles = new Rectangle[]
                    {rect1, rect2, rect3, rect4, rect5};
            Rectangle rect = rectangles[i];

            if (rect != null) {
                if (slot.getUserData() instanceof Pictogramme) {
                    Pictogramme p = (Pictogramme) slot.getUserData();
                    if (i < sequenceCorrecte.size() && p.equals(sequenceCorrecte.get(i))) {
                        rect.setStroke(javafx.scene.paint.Color.GREEN);
                    } else {
                        rect.setStroke(javafx.scene.paint.Color.RED);
                    }
                } else {
                    rect.setStroke(javafx.scene.paint.Color.BLACK);
                }
            }
        }

        // Vérification de la victoire
        if (points == scoreMax) {
            CHRONOMETRE.arreterChrono();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Scénario réussi !");
            alert.setHeaderText("Vous avez obtenu " + points + " points !");
            alert.setContentText("Que souhaitez-vous faire ?");

            ButtonType restartButton = new ButtonType("Recommencer");
            ButtonType menuButton = new ButtonType("Retour au menu");

            alert.getButtonTypes().setAll(restartButton, menuButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == restartButton) {
                    reinitialiserJeu();
                } else if (result.get() == menuButton) {
                    reinitialiserJeu();
                    Lanceur.activerFenetreJeux();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Résultat");
            alert.setHeaderText("Grille mauvaise");
            alert.setContentText("Certains pictogrammes sont mal placés " +
                    "ou ne sont pas les bons. Veuillez réessayer.");
            alert.showAndWait();
        }
    }

    /**
     * Charge une image depuis le classpath en évitant les NullPointerException.
     * @param chemin chemin vers la ressource (ex: "/iut/ga1/resquizz/images/info.png")
     * @return l'image si trouvée, sinon null
     */
    private Image chargerImage(String chemin) {
        URL url = getClass().getResource("/iut/ga1/resquizz/" + chemin);
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

            String ancienStyleTitre = chronoLabel.getStyle();
            chronoLabel.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");

            String ancienStyleButtonRestart = buttonRestart.getStyle();
            buttonRestart.setStyle(ancienStyleButtonRestart + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonValider = buttonValider.getStyle();
            buttonValider.setStyle(ancienStyleButtonValider + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePointsLabel = pointsLabel.getStyle();
            pointsLabel.setStyle(ancienStylePointsLabel + "-fx-font-family: '" + font.getFamily() + "';");
        }
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
        String ancienStyleTitre = chronoLabel.getStyle();
        if (couleur1 != null) {
            fenetre.setStyle(ancienStyleFenetre + "-fx-background-color: " + couleur1 + ";");
        }
        if (couleur2 != null) {
            chronoLabel.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
        }

        if (couleur3) {
            logoInformation.setImage(chargerImage("images/info.png"));
            logoParametre.setImage(chargerImage("images/param.png"));
        } else {
            logoInformation.setImage(chargerImage("images/info_blanc.png"));
            logoParametre.setImage(chargerImage("images/param_blanc.png"));
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
        String ancienStylePointsLabel = pointsLabel.getStyle();
        pointsLabel.setStyle(ancienStylePointsLabel + "-fx-font-size: " + taillePolice + ";");
    }
}