/*
 * Lanceur.java                                                   14 octobre 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.net.URL;

import iut.ga1.resquizz.controleurs.*;

/**
 * Classe principale de l'application ResQuizz.
 * Gère les différentes fenêtres et leurs contrôleurs.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class Lanceur extends Application {

    /* Scènes */
    private static Scene sceneMenu;
    private static Scene sceneJeux;
    private static Scene sceneParametres;
    private static Scene sceneJeuGrille;
    private static Scene sceneJeuQCM;
    private static Scene sceneScenarioFixeComplet;
    private static Scene sceneScenarioAleaPaP;
    private static Scene sceneClassement;
    private static Scene sceneFenetreScenario;
    private static Scene scenePartageScenario;
    private static Scene sceneReceptionScenario;

    /* Contrôleurs */
    private static ControleurMenuJeux controleurMenuJeux;
    private static ControleurMenuParametres controleurMenuParametres;
    private static ControleurMenuPrincipal controleurMenuPrincipal;
    private static ControleurRevisionGrille controleurRevisionGrille;
    private static ControleurQCM controleurQCM;
    private static ControleurScenarioFixeComplet controleurScenarioFixeComplet;
    private static ControleurScenarioAleaPaP controleurScenarioAleaPaP;
    private static ControleurClassement controleurClassement;
    private static ControleurFenetreScenario controleurFenetreScenario;
    private static ControleurPartageScenario controleurPartageScenario;
    private static ControleurReceptionScenario controleurReceptionScenario;

    /** Stage principal */
    public static Stage fenetreActuelle;

    /** S'assure que le serveur est arrêté ainsi que le client (afin d'éviter de laisser des processus tourner */
    @Override
    public void stop() throws Exception {
        Client.deconnecter();
        Serveur.fermerServeur();
        super.stop();
    }

    /** Méthode permettant de redistribuer la résolution du jeu */
    public static void redistribuerResolution() {
        if (fenetreActuelle == null) return;

        Scene scene = fenetreActuelle.getScene();
        if (scene == null) return;

        // Appliquer la mise à l’échelle sur le contenu
        UIUtils.appliquerEchelleGlobale(scene, Parametres.getLargeur(), Parametres.getHauteur());


        // Important : changer la taille du Stage après autosize

        fenetreActuelle.setMinWidth(Parametres.getLargeur());
        fenetreActuelle.setMinHeight(Parametres.getHauteur());
        fenetreActuelle.setWidth(Parametres.getLargeur());
        fenetreActuelle.setHeight(Parametres.getHauteur());

        // Recentrer proprement
        fenetreActuelle.centerOnScreen();

    }

    @Override
    public void start(Stage primaryStage) {

        String cheminStockage = StorageConfig.chargerCheminStockage();
        Parametres.setCheminStockage(cheminStockage);

        GestionParametres.chargerParametres();
        GestionParametres.chargerClassement();

        fenetreActuelle = primaryStage;

        String couleur1 = Parametres.getCouleurFenetre();
        String couleur2 = Parametres.getCouleurTitre();
        boolean couleur3 = Parametres.getCouleurLogo();
        String police = Parametres.getPolice();
        int taillePolice = Parametres.getTaillePolice();

        try {
            // --- Menu principal ---
            controleurMenuPrincipal = chargerFenetre(
                    "/iut/ga1/resquizz/vues/MenuPrincipal.fxml",
                    ref -> sceneMenu = ref,
                    ControleurMenuPrincipal.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Menu des jeux ---
            controleurMenuJeux = chargerFenetre(
                    "/iut/ga1/resquizz/vues/MenuJeux.fxml",
                    ref -> sceneJeux = ref,
                    ControleurMenuJeux.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Menu des paramètres ---
            controleurMenuParametres = chargerFenetre(
                    "/iut/ga1/resquizz/vues/MenuParametres.fxml",
                    ref -> sceneParametres = ref,
                    ControleurMenuParametres.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Jeu Révision de la grille ---
            controleurRevisionGrille = chargerFenetre(
                    "/iut/ga1/resquizz/vues/JeuRevisionGrille.fxml",
                    ref -> sceneJeuGrille = ref,
                    ControleurRevisionGrille.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Jeu QCM ---
            controleurQCM = chargerFenetre(
                    "/iut/ga1/resquizz/vues/JeuQCM.fxml",
                    ref -> sceneJeuQCM = ref,
                    ControleurQCM.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Scénario fixe complet ---
            controleurScenarioFixeComplet = chargerFenetre(
                    "/iut/ga1/resquizz/vues/JeuScenarioFixeComplet.fxml",
                    ref -> sceneScenarioFixeComplet = ref,
                    ControleurScenarioFixeComplet.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Scénario aléatoire pas à pas ---
            controleurScenarioAleaPaP = chargerFenetre(
                    "/iut/ga1/resquizz/vues/JeuScenarioAleaPaP.fxml",
                    ref -> sceneScenarioAleaPaP = ref,
                    ControleurScenarioAleaPaP.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Classement ---
            controleurClassement = chargerFenetre(
                    "/iut/ga1/resquizz/vues/Classement.fxml",
                    ref -> sceneClassement = ref,
                    ControleurClassement.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            controleurFenetreScenario = chargerFenetre(
                    "/iut/ga1/resquizz/vues/FenetreScenario.fxml",
                    ref -> sceneFenetreScenario = ref,
                    ControleurFenetreScenario.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            controleurPartageScenario = chargerFenetre(
                    "/iut/ga1/resquizz/vues/PartageScenario.fxml",
                    ref -> scenePartageScenario = ref,
                    ControleurPartageScenario.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            controleurReceptionScenario = chargerFenetre(
                    "/iut/ga1/resquizz/vues/ReceptionScenario.fxml",
                    ref -> sceneReceptionScenario = ref,
                    ControleurReceptionScenario.class,
                    couleur1, couleur2, couleur3, police, taillePolice);

            // --- Fenêtre principale ---
            if (sceneMenu != null) {
                primaryStage.setScene(sceneMenu);
            } else if (sceneJeux != null) {
                // fallback si la scène du menu n’a pas chargé
                primaryStage.setScene(sceneJeux);
            } else {
                System.err.println("Aucune scène n’a pu être chargée !");
                return;
            }

            //Sauvegarde des paramètres afin de ne pas avoir de problèmes lors du partage si ceux cis n'ont pas été modifiés
            Parametres.chargerCheminStockageDepuisFichier();
            GestionParametres.sauvegarderParametres();
            Scenario.creerFichierScenarioSiAbsent(Parametres.getCheminStockage());

            // PrimaryStage
            primaryStage.setTitle("ResQuizz");
            primaryStage.setWidth(Parametres.getLargeur());
            primaryStage.setHeight(Parametres.getHauteur());
            //primaryStage.setResizable(false);
            primaryStage.setScene(sceneMenu);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des fenêtres : " + e.getMessage());
        }
    }

    /** Méthode utilitaire pour charger une fenêtre FXML + CSS + initialiser le contrôleur. */
    private <T> T chargerFenetre(
            String cheminFXML,
            java.util.function.Consumer<Scene> affecterScene,
            Class<T> typeControleur,
            String couleur1, String couleur2, boolean couleur3, String police, int taillePolice) {

        try {
            URL fxmlUrl = getClass().getResource(cheminFXML);
            if (fxmlUrl == null) {
                System.err.println("FXML introuvable : " + cheminFXML);
                return null;
            }

            FXMLLoader chargeur = new FXMLLoader(fxmlUrl);
            Parent conteneur = chargeur.load();
            if (conteneur == null) {
                System.err.println("Impossible de charger le contenu du FXML : " + cheminFXML);
                return null;
            }

            Scene scene = new Scene(conteneur, Parametres.getLargeur(), Parametres.getHauteur());

            // Chargement du CSS
            URL cssUrl = getClass().getResource("/iut/ga1/resquizz/css/Style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Fichier CSS introuvable !");
            }

            affecterScene.accept(scene); // affecte la scène statique correspondante

            // Récupération du contrôleur
            Object controller = chargeur.getController();
            if (controller == null) {
                System.err.println("Aucun contrôleur pour : " + cheminFXML);
                return null;
            }

            if (!typeControleur.isInstance(controller)) {
                System.err.println("Type de contrôleur inattendu pour " + cheminFXML);
                return null;
            }

            T ctrl = typeControleur.cast(controller);

            // Mise à jour des couleurs (si la méthode existe)
            try {
                if (ctrl instanceof ControleurMenuPrincipal) {
                    ((ControleurMenuPrincipal) ctrl).mettreAJourCouleur(couleur1, couleur2);
                    ((ControleurMenuPrincipal) ctrl).mettreAJourPolice(police);
                    ((ControleurMenuPrincipal) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurMenuJeux) {
                    ((ControleurMenuJeux) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurMenuJeux) ctrl).mettreAJourPolice(police);
                    ((ControleurMenuJeux) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurMenuParametres) {
                    ((ControleurMenuParametres) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurMenuParametres) ctrl).mettreAJourPolice(police);
                    ((ControleurMenuParametres) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurRevisionGrille) {
                    ((ControleurRevisionGrille) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurRevisionGrille) ctrl).mettreAJourPolice(police);
                    ((ControleurRevisionGrille) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurQCM) {
                    ((ControleurQCM) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurQCM) ctrl).mettreAJourPolice(police);
                    ((ControleurQCM) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurScenarioFixeComplet) {
                    ((ControleurScenarioFixeComplet) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurScenarioFixeComplet) ctrl).mettreAJourPolice(police);
                    ((ControleurScenarioFixeComplet) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurScenarioAleaPaP) {
                    ((ControleurScenarioAleaPaP) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurScenarioAleaPaP) ctrl).mettreAJourPolice(police);
                    ((ControleurScenarioAleaPaP) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurClassement) {
                    ((ControleurClassement) ctrl).mettreAJourCouleur(couleur1, couleur3);
                    ((ControleurClassement) ctrl).mettreAJourPolice(police);
                    ((ControleurClassement) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurFenetreScenario) {
                    ((ControleurFenetreScenario) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurFenetreScenario) ctrl).mettreAJourPolice(police);
                    ((ControleurFenetreScenario) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurPartageScenario) {
                    ((ControleurPartageScenario) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurPartageScenario) ctrl).mettreAJourPolice(police);
                    ((ControleurPartageScenario) ctrl).mettreAJourTaillePolice(taillePolice);
                } else if (ctrl instanceof ControleurReceptionScenario) {
                    ((ControleurReceptionScenario) ctrl).mettreAJourCouleur(couleur1, couleur2, couleur3);
                    ((ControleurReceptionScenario) ctrl).mettreAJourPolice(police);
                    ((ControleurReceptionScenario) ctrl).mettreAJourTaillePolice(taillePolice);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour des couleurs : " + e.getMessage());
            }

            return ctrl;
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de " + cheminFXML + " : " + e.getMessage());
            return null;
        }
    }

    /** Méthode pour changer la scene */
    private static void changerScene(Scene scene, String nomScene) {
        if (fenetreActuelle == null) {
            System.err.println("fenetreActuelle null lors de l'activation de " + nomScene);
            return;
        }
        if (scene == null) {
            System.err.println("Scene " + nomScene + " non initialisée !");
            return;
        }
        UIUtils.appliquerEchelleGlobale(scene, Parametres.getLargeur(), Parametres.getHauteur());
        fenetreActuelle.setScene(scene);
    }

    /* Permet d'activer l'affichage des fenêtres */
    public static void activerFenetreMenu() { changerScene(sceneMenu, "Menu"); }
    public static void activerFenetreJeux() { changerScene(sceneJeux, "Jeux"); }
    public static void activerFenetreParametres() { changerScene(sceneParametres, "Parametres"); }
    public static void activerFenetreRevisionGrille() { changerScene(sceneJeuGrille, "JeuRevisionGrille"); }
    public static void activerFenetreQCM() { changerScene(sceneJeuQCM, "JeuQCM"); controleurQCM.choisirJeuLancement(); }
    public static void activerFenetreSceFixeComplet() { changerScene(sceneScenarioFixeComplet, "ScenarioFixeComplet"); }
    public static void activerFenetreSceAleaPap() { changerScene(sceneScenarioAleaPaP, "ScenarioAleaPaP"); }
    public static void activerFenetreClassement() {
        changerScene(sceneClassement, "Classement");
        controleurClassement.gererClicRevision();
    }
    public static void activerFenetreScenario() { changerScene(sceneFenetreScenario, "FenêtreScenario"); }
    public static void activerPartageScenario() {
        changerScene(scenePartageScenario, "PartageScenario");
        controleurPartageScenario.viderConsoleLogs();
    }
    public static void activerReceptionScenario() { changerScene(sceneReceptionScenario, "ReceptionScenario"); }

    /** Redistribue les couleurs des joueurs à tous les contrôleurs. */
    public static void redistribuerCouleur() {
        String couleur1 = Parametres.getCouleurFenetre();
        String couleur2 = Parametres.getCouleurTitre();
        boolean couleur3 = Parametres.getCouleurLogo();

        if (controleurMenuPrincipal != null) controleurMenuPrincipal.mettreAJourCouleur(couleur1, couleur2);
        if (controleurMenuJeux != null) controleurMenuJeux.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurMenuParametres != null) controleurMenuParametres.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurRevisionGrille != null) controleurRevisionGrille.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurQCM != null) controleurQCM.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurScenarioFixeComplet != null) controleurScenarioFixeComplet.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurScenarioAleaPaP != null) controleurScenarioAleaPaP.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurClassement != null) controleurClassement.mettreAJourCouleur(couleur1, couleur3);
        if (controleurFenetreScenario != null) controleurFenetreScenario.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurPartageScenario != null) controleurPartageScenario.mettreAJourCouleur(couleur1, couleur2, couleur3);
        if (controleurReceptionScenario != null) controleurReceptionScenario.mettreAJourCouleur(couleur1, couleur2, couleur3);
    }

    /** Permet de redistribuer la police dans toutes les fenêtres */
    public static void redistribuerPolice() {
        String police = Parametres.getPolice();

        if (controleurMenuPrincipal != null) controleurMenuPrincipal.mettreAJourPolice(police);
        if (controleurMenuJeux != null) controleurMenuJeux.mettreAJourPolice(police);
        if (controleurMenuParametres != null) controleurMenuParametres.mettreAJourPolice(police);
        if (controleurRevisionGrille != null) controleurRevisionGrille.mettreAJourPolice(police);
        if (controleurQCM != null) controleurQCM.mettreAJourPolice(police);
        if (controleurScenarioFixeComplet != null) controleurScenarioFixeComplet.mettreAJourPolice(police);
        if (controleurScenarioAleaPaP != null) controleurScenarioAleaPaP.mettreAJourPolice(police);
        if (controleurClassement != null) controleurClassement.mettreAJourPolice(police);
        if (controleurFenetreScenario != null) controleurFenetreScenario.mettreAJourPolice(police);
        if (controleurPartageScenario != null) controleurPartageScenario.mettreAJourPolice(police);
        if (controleurReceptionScenario != null) controleurReceptionScenario.mettreAJourPolice(police);
    }

    /**
     * Permet de mettre à jour la taille de la police
     */
    public static void redistribuerTaillePolice() {
        int taillePolice = Parametres.getTaillePolice();

        if (controleurMenuPrincipal != null) controleurMenuPrincipal.mettreAJourTaillePolice(taillePolice);
        if (controleurMenuJeux != null) controleurMenuJeux.mettreAJourTaillePolice(taillePolice);
        if (controleurMenuParametres != null) controleurMenuParametres.mettreAJourTaillePolice(taillePolice);
        if (controleurRevisionGrille != null) controleurRevisionGrille.mettreAJourTaillePolice(taillePolice);
        if (controleurQCM != null) controleurQCM.mettreAJourTaillePolice(taillePolice);
        if (controleurScenarioFixeComplet != null) controleurScenarioFixeComplet.mettreAJourTaillePolice(taillePolice);
        if (controleurScenarioAleaPaP != null) controleurScenarioAleaPaP.mettreAJourTaillePolice(taillePolice);
        if (controleurClassement != null) controleurClassement.mettreAJourTaillePolice(taillePolice);
        if (controleurPartageScenario != null) controleurPartageScenario.mettreAJourTaillePolice(taillePolice);
        if (controleurReceptionScenario != null) controleurReceptionScenario.mettreAJourTaillePolice(taillePolice);
        if (controleurFenetreScenario != null) controleurFenetreScenario.mettreAJourTaillePolice(taillePolice);
    }

    @SuppressWarnings("unused") // Pour enlever le warning au lancement
    public static void main(String[] args) {
        launch(args);
    }
}