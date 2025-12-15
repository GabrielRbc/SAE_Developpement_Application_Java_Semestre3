/*
 * ControleurScenarioAleaPaP.java                        15 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Chronometre;
import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.modeles.ScenarioAleaPaP;
import iut.ga1.resquizz.util.GestionClassement;
import iut.ga1.resquizz.util.GestionParametres;
import iut.ga1.resquizz.util.Parametres;
import iut.ga1.resquizz.util.Pictogramme;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur associé au mode de jeu « Scénario Aléatoire Pas à Pas ».
 * <p>
 * Cette classe gère toute la logique de l’interface graphique pour ce mode :
 * affichage des pictogrammes proposés, gestion des clics sur les emplacements,
 * suivi de la progression dans le scénario, feedback visuel sur les réponses,
 * ainsi que l’interaction avec le chronomètre et les fenêtres externes
 * (paramètres, informations, retour au menu des jeux).
 * </p>
 *
 * <p>
 * Le joueur doit sélectionner un pictogramme parmi plusieurs propositions
 * et le placer dans le slot actif. Chaque étape du scénario correspond à une
 * action spécifique, et le joueur doit retrouver la bonne réponse pour
 * avancer. La première erreur affiche et corrige automatiquement la bonne
 * réponse, tandis que la seconde met fin à la partie.
 * </p>
 *
 * <p>Fonctionnalités principales :
 * <ul>
 *   <li>Affichage dynamique des pictogrammes proposés</li>
 *   <li>Gestion du placement étape par étape dans une grille de 5 slots</li>
 *   <li>Suivi des erreurs et correction automatique de la première</li>
 *   <li>Feedback visuel via couleurs (bleu, vert, rouge, gris)</li>
 *   <li>Gestion des points</li>
 *   <li>Intégration avec un chronomètre affiché en temps réel</li>
 *   <li>Accès aux fenêtres d’informations, paramètres et retour au menu</li>
 * </ul>
 * </p>
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * Esteban Roveri
 */
public class ControleurScenarioAleaPaP {

    /** Label contenant la description du scénario à résoudre */
    @FXML
    private Label scenarioTextLabel;

    /** Texte qui contient le chronometre*/
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

    /** identifiant du bouton valider */
    @FXML
    private Button buttonValider;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Conteneur des pictogrammes proposés (en bas de la fenêtre) */
    @FXML
    private HBox containerPropositions;

    /** Identifiant de la fenêtre pour le style */
    @FXML
    private BorderPane fenetre;

    /** Label affichant les points du joueur */
    @FXML
    private Label pointsLabel;

    /** Label affichant le nombre d'erreurs du joueur */
    @FXML
    private Label labelErreur;

    /** Bouton pour afficher la fenetre des parametres */
    @FXML
    private ImageView logoParametre;

    /** Bouton pour afficher la fenetre des informations du jeu*/
    @FXML
    private ImageView logoInformation;

    /** Identifiant permettant de sauvegarder les points */
    private boolean sauvegarderPoints;

    /** Chronomètre utilisé pour mesurer la durée de la partie */
    private final Chronometre CHRONOMETRE = new Chronometre();

    /** Tableau contenant les 5 slots d'images */
    private ImageView[] emplacementsImageViews;

    /** Scénario actuel du mode de jeu */
    private ScenarioAleaPaP scenarioAleaPaP;

    /** Liste représentant la séquence placée par l'utilisateur */
    private static List<Pictogramme> sequenceUtilisateur;

    /** Pictogramme actuellement sélectionné dans les propositions */
    private Pictogramme pictogrammeSelectionne = null;

    /** Index du prochain slot disponible pour placer un pictogramme */
    private int prochainSlotDisponible;

    /** Méthode d'initialisation au lancement de l'application */
    @FXML
    public void initialize() {
        scenarioAleaPaP = new ScenarioAleaPaP();
        sequenceUtilisateur = new ArrayList<>();
        prochainSlotDisponible = 0;

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

        scenarioTextLabel.setText(scenarioAleaPaP.getDescriptionEtapeActuelle());

        initialiserSlotsGrille();
        afficherPropositions();
        mettreAJourSlotsActifs();

        sauvegarderPoints = true;
    }

    /** Affiche tous les pictogrammes disponibles dans la HBox. */
    private void afficherPropositions() {
        containerPropositions.getChildren().clear();
        for (Pictogramme p : scenarioAleaPaP.getPropositions()) {
            ImageView pictogramme = creerImageViewProposition(p);
            containerPropositions.getChildren().add(pictogramme);
        }
    }

    /** Initialise les slots de la grille pour qu'ils soient vides */
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

    /** Permet d'afficher en bleu, l'image sélectionné */
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
     * Gère le clic sur un emplacement de la grille.
     * Si un pictogramme est sélectionné, on le place.
     * Sinon, on retire le pictogramme déjà présent.
     * @param event clic souris sur un slot
     */
    @FXML
    private void gererClicSlot(javafx.scene.input.MouseEvent event) {
        ImageView slotClicked = (ImageView) event.getSource();

        // Vérifie si c'est le bon slot qui doit être cliqué
        int indexSlot = getIndexSlot(slotClicked);
        if (indexSlot != prochainSlotDisponible) {
            return; // Ce n'est pas le bon slot, on ignore le clic
        }

        // Cas où un pictogramme a été sélectionné
        if (pictogrammeSelectionne != null) {
            if (slotClicked.getUserData() instanceof Pictogramme) {
                Pictogramme ancienPictogramme = (Pictogramme) slotClicked.getUserData();
                remettrePictogrammeDansPropositions(ancienPictogramme);

                // Retirer l'ancien pictogramme de la séquence utilisateur
                sequenceUtilisateur.remove(ancienPictogramme);
            }
            // Placer le pictogramme sélectionné
            slotClicked.setImage(chargerImage(pictogrammeSelectionne.getLienImage()));
            slotClicked.setUserData(pictogrammeSelectionne);

            // Supprime le pictogramme placé de la HBox
            containerPropositions.getChildren().removeIf(node ->
                    node.getUserData().equals(pictogrammeSelectionne)
            );

            // Met à jour la séquence utilisateur
            sequenceUtilisateur.add(pictogrammeSelectionne);

            // Réinitialiser la sélection
            pictogrammeSelectionne = null;

            mettreAJourSlotsActifs();
        }
    }

    /** Retourne l'index d'un slot à partir de son ImageView */
    private int getIndexSlot(ImageView slot) {
        for (int i = 0; i < emplacementsImageViews.length; i++) {
            if (emplacementsImageViews[i] == slot) {
                return i;
            }
        }
        return -1;
    }

    /** Met à jour l'apparence des slots pour indiquer lequel est actif */
    private void mettreAJourSlotsActifs() {
        for (int i = 0; i < emplacementsImageViews.length; i++) {
            Rectangle rect = getRectangleSlot(i);
            if (rect != null) {
                javafx.scene.paint.Color strokeColor = (javafx.scene.paint.Color) rect.getStroke();
                if (strokeColor != javafx.scene.paint.Color.GREEN && strokeColor != javafx.scene.paint.Color.RED) {
                    // Seulement changer la couleur si ce n'est pas un feedback
                    if (i == prochainSlotDisponible) {
                        // Slot actif = contour bleu
                        rect.setStroke(javafx.scene.paint.Color.BLUE);
                        rect.setStrokeWidth(3.0);
                    } else if (i < prochainSlotDisponible) {
                        // Slot déjà rempli = contour noir normal
                        rect.setStroke(javafx.scene.paint.Color.BLACK);
                        rect.setStrokeWidth(2.0);
                    } else {
                        // Slot futur = contour gris
                        rect.setStroke(javafx.scene.paint.Color.GRAY);
                        rect.setStrokeWidth(1.0);
                    }
                    }
            }
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
     * Remet un pictogramme retiré dans la HBox des propositions.
     * @param p pictogramme à remettre
     */
    private void remettrePictogrammeDansPropositions(Pictogramme p) {
        ImageView pictogrammeSelect = creerImageViewProposition(p);
        // Remet le pictogramme à la fin de la HBox
        containerPropositions.getChildren().add(pictogrammeSelect);
    }

    /** Méthode pour afficher le feedback des bonnes et mauvaises réponses */
    private void afficherFeedbackSucces() {
        // Met en vert le rectangle correspondant
        int slotactuel = prochainSlotDisponible;
        if (slotactuel >= 0) {
            Rectangle rectCorrect = getRectangleSlot(slotactuel);
            if (rectCorrect != null) {
                rectCorrect.setStroke(javafx.scene.paint.Color.GREEN);
                rectCorrect.setStrokeWidth(3.0);
            }
        }
    }

    /** Remplace le dernier pictogramme placé par la bonne réponse */
    private void corrigerDernierPictogramme() {
        int slotActuel = prochainSlotDisponible;
        if (slotActuel >= 0 && slotActuel < emplacementsImageViews.length) {
            ImageView slot = emplacementsImageViews[slotActuel];
            Pictogramme bonneReponse = scenarioAleaPaP.getReponseCorrecte();

            slot.setImage(chargerImage(bonneReponse.getLienImage()));
            slot.setUserData(bonneReponse);

            // Met à jour la séquence utilisateur avec la bonne réponse
            if (sequenceUtilisateur.size() > slotActuel) {
                sequenceUtilisateur.set(slotActuel, bonneReponse);
            }

            // Met le rectangle en vert pour indiquer la correction
            Rectangle rect = getRectangleSlot(slotActuel);
            if (rect != null) {
                rect.setStroke(javafx.scene.paint.Color.GREEN);
                rect.setStrokeWidth(3.0);
            }
        }
    }

    /** Gère une erreur de réponse */
    private void gererErreur() {
        scenarioAleaPaP.incrementerErreurs();

        int dernierSlotUtilise = prochainSlotDisponible;

        if (dernierSlotUtilise >= 0) {
            Rectangle rectIncorrect = getRectangleSlot(dernierSlotUtilise);
            if (rectIncorrect != null) {
                rectIncorrect.setStroke(javafx.scene.paint.Color.RED);
            }
        }

        if (scenarioAleaPaP.getErreurs() == 1) {
            // Première erreur — on continue après avoir corrigé
            afficherAlerte("Mauvaise réponse",
                    "La bonne réponse était : " + scenarioAleaPaP.getReponseCorrecte().getIntitule() +
                            "\nLe pictogramme sera corrigé automatiquement.");

            corrigerDernierPictogramme();

            prochainSlotDisponible++;

            if (scenarioAleaPaP.etapeSuivante()) {
                preparerEtapeSuivante();
            } else {
                afficherFinScenario();
            }
        } else {
            // Deuxième erreur — fin de partie
            afficherAlerte("Partie terminée",
                    "Deux erreurs commises.\n" +
                            "Nous vous conseillons d'utiliser un mode de jeu plus facile " +
                            " et de réviser les documents de formation.");
            scenarioAleaPaP = new ScenarioAleaPaP();
            scenarioAleaPaP.reinitialiser();
            reinitialiserInterface();

            if (labelErreur != null) {
                labelErreur.setText("Nombre d'erreurs : 0");
            }

            if (pointsLabel != null) {
                pointsLabel.setText("Points : 0");
            }
        }
        if (labelErreur != null) {
            labelErreur.setText("Nombre d'erreurs : " + scenarioAleaPaP.getErreurs());
        }
    }

    /** Permet d'afficher les pictogrammes et l'étape suivante */
    private void preparerEtapeSuivante() {

        // Met à jour la description du scénario
        scenarioTextLabel.setText(scenarioAleaPaP.getDescriptionEtapeActuelle());

        // Met à jour les propositions
        afficherPropositions();

        // Réinitialise la sélection
        pictogrammeSelectionne = null;
        mettreAJourSlotsActifs();
    }

    /** Réinitialise complètement l'interface */
    private void reinitialiserInterface() {
        reinitialiserSlots();
        scenarioAleaPaP = new ScenarioAleaPaP();
        scenarioAleaPaP.reinitialiser();
        scenarioTextLabel.setText(scenarioAleaPaP.getDescriptionEtapeActuelle());
        afficherPropositions();
        pictogrammeSelectionne = null;
        sequenceUtilisateur.clear();
        CHRONOMETRE.arreterChrono();
        CHRONOMETRE.reinitialiserChrono();
        prochainSlotDisponible = 0;
        mettreAJourSlotsActifs();
        if (labelErreur != null) {
            labelErreur.setText("Nombre d'erreurs : 0");
        }

        if (pointsLabel != null) {
            pointsLabel.setText("Points : 0");
        }
    }

    /** Réinitialise les slots d'images */
    private void reinitialiserSlots() {
        sauvegarderPoints = true;
        for (int i = 0; i < emplacementsImageViews.length; i++) {
            ImageView slot = emplacementsImageViews[i];
            if (slot != null) {
                slot.setImage(null);
                slot.setUserData(null);
            }
            // Réinitialise le rectangle correspondant
            Rectangle rect = getRectangleSlot(i);
            if (rect != null) {
                rect.setStroke(javafx.scene.paint.Color.BLACK);
            }
        }
    }

    /** Récupère le rectangle correspondant à un slot */
    private Rectangle getRectangleSlot(int index) {
        switch (index) {
            case 0: return rect1;
            case 1: return rect2;
            case 2: return rect3;
            case 3: return rect4;
            case 4: return rect5;
            default: return null;
        }
    }

    /** Affiche la fin du scénario */
    private void afficherFinScenario() {

        int points = scenarioAleaPaP.getPoints();

        if (sauvegarderPoints && sequenceUtilisateur.size() == 5) {
            sauvegarderPoints = false;
            if (points > GestionClassement.getPointScenarioAlea()) {
                GestionClassement.setPointScenarioAlea(points);
                GestionClassement.setChronoScenarioAlea(CHRONOMETRE.getTempsEcoule());
                GestionParametres.sauvegarderClassement();
                System.out.println("Points : " + points);
                System.out.println("Points : " + GestionClassement.getPointScenarioAlea());
            } else if (points == GestionClassement.getPointScenarioAlea()
                    && CHRONOMETRE.getTempsEcoule() < GestionClassement.getChronoScenarioAlea()) {
                GestionClassement.setChronoScenarioAlea(CHRONOMETRE.getTempsEcoule());
            }
        }

        CHRONOMETRE.arreterChrono();

        String message = "Félicitations ! Vous avez terminé le scénario.\n\n" +
        "Erreurs : " + scenarioAleaPaP.getErreurs() + "\n" +
        "Points : " + scenarioAleaPaP.getPoints();

        // Créer une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Scénario terminé");
        alert.setHeaderText("Félicitations !");
        alert.setContentText(message);

        // Créer les boutons
        ButtonType boutonRejouer = new ButtonType("Rejouer");
        ButtonType boutonMenu = new ButtonType("Retour au menu");

        // Ajouter les boutons à l'alerte
        alert.getButtonTypes().setAll(boutonRejouer, boutonMenu);

        // Afficher l'alerte et gérer la réponse
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == boutonRejouer) {
                reinitialiserInterface();
            } else if (result.get() == boutonMenu) {
                reinitialiserInterface();
                Lanceur.activerFenetreJeux();
            }
        }
    }

    /** Utilitaire pour afficher des alertes */
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Pour lancer la méthode de validation de la grille */
    @FXML
    public void gererClicValider() {
        // Vérifie que le slot actuel est rempli
        if (sequenceUtilisateur.size() <= prochainSlotDisponible) {
            afficherAlerte("Aucune réponse", "Veuillez sélectionner un pictogramme avant de valider.");
            return;
        }

        // Calcul des points
        Pictogramme derniereReponse = sequenceUtilisateur.get(sequenceUtilisateur.size() - 1);
        boolean correct = scenarioAleaPaP.verifierReponse(derniereReponse);
        scenarioAleaPaP.calculPoints(correct);
        int points = scenarioAleaPaP.getPoints();
        pointsLabel.setText("Points : " + points);

        // Gère les cas d'erreur ou de bonne réponse
        // (size - 1) Utiliser le dernier pictogramme placé
        Pictogramme reponse = sequenceUtilisateur.get(sequenceUtilisateur.size() - 1);
        if (scenarioAleaPaP.verifierReponse(reponse)) {
            // Bonne réponse
            afficherFeedbackSucces();

            prochainSlotDisponible++;

            if (scenarioAleaPaP.estDerniereEtape()) {
                // Fin du scénario
                afficherFinScenario();
            } else {
                // Passage à l'étape suivante
                scenarioAleaPaP.etapeSuivante();
                preparerEtapeSuivante();
            }
        } else {
            // Mauvaise réponse
            gererErreur();
        }


    }

    /** Pour lancer la méthode de réinitialisation de la grille */
    @FXML
    public void gererClicRestart() {
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
            scenarioAleaPaP = new ScenarioAleaPaP();
            scenarioAleaPaP.reinitialiser();
            reinitialiserInterface();

            if (labelErreur != null) {
                labelErreur.setText("Nombre d'erreurs : 0");
            }

            if (pointsLabel != null) {
                pointsLabel.setText("Points : 0");
            }
        }
    }

    /** Pour lancer la fenêtre des informations du jeu */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Scénario Aléatoire pas à pas");
        final String informations = "Faites face à un scénario d’accident généré aléatoirement.\n" +
                "À chaque étape, choisissez le pictogramme approprié parmi plusieurs propositions.\n" +
                "\n" +
                "Si vous répondez juste : le scénario continue.\n" +
                "\n" +
                "En cas d’erreur : la bonne réponse est affichée.\n" +
                "\n" +
                "À la deuxième erreur, la partie s’arrête avec un conseil pour réviser.";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Pour lancer la fenêtre des paramètres */
    @FXML
    public void gererClicParametres() {
        Parametres.setChemin("ScenarioAlea");
        CHRONOMETRE.arreterChrono();
        Lanceur.activerFenetreParametres();
    }

    /** Pour lancer la fenêtre des Jeux */
    @FXML
    public void gererClicRetour() {
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

                reinitialiserInterface();
                Lanceur.activerFenetreJeux(); // retour au menu
                // else
                // L'utilisateur annule → ne fait rien
                // (la fenêtre reste ouverte)
            }
        } else {
            reinitialiserInterface();
            Lanceur.activerFenetreJeux();
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
            labelErreur.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
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
            // On garde le style existant de la fenêtre
            String ancienStyleFenetre = fenetre.getStyle();
            fenetre.setStyle(ancienStyleFenetre + "-fx-font-family: '" + font.getFamily() + "';");

            // On garde aussi le style existant du titre (notamment sa couleur).
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