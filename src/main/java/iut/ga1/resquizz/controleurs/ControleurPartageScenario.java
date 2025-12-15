/*
 * ControleurPartageScenario.java                         24 Nov 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.AdresseIP;
import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.modeles.Serveur;
import iut.ga1.resquizz.util.Parametres;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;

/**
 * Contrôleur pour le partage des données
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurPartageScenario {

    /** Identifiant de la zone ou la console est affichée */
    @FXML
    private TextArea consoleLog;

    /** Identifiant du titre de la fenêtre */
    @FXML
    private Label titre;

    /** Identifiant du label des données à partager */
    @FXML
    private Label labelDonneesAPartager;

    /** Identifiant des différents réseaux disponibles */
    @FXML
    private Label libelleReseau;

    /** Identifiant du label du port */
    @FXML
    private Label port;

    /** Identifiant de la zone de texte du port */
    @FXML
    private TextField entreePort;

    /** Identifiant de la bordure de la fenêtre */
    @FXML
    private BorderPane fenetre;

    /** Identifiant du bouton de démarrage et d'arrêt du serveur */
    @FXML
    private Button bouton;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Identifiant du logo 'I' pour Information */
    @FXML
    private ImageView logoInformation;

    /** Identifiant de la checkbox des scénarios */
    @FXML
    private CheckBox partageScenarios;

    /** Identifiant de la checkbox des paramètres */
    @FXML
    private CheckBox partageParametres;

    /** Identifiant de la VBox contenant les différentes ip disponible */
    @FXML
    private VBox interfacesReseau;

    /** Méthode permettant de lancer le retour à la fenêtre précédente */
    @FXML
    public void gererClicRetour() {
        Lanceur.activerFenetreScenario();
        consoleLog.clear();
        Serveur.fermerServeur();
        bouton.setText("Démarrer partage");
    }

    /** Méthode à l'initialisation de l'application */
    @FXML
    public void initialize() {

        //Ajout de toutes les adresses dans le scrollPane
        try {
            for (InetAddress addr : AdresseIP.scanReseau()){
                ajouterLabel(addr);
            }
        } catch (Exception e) {
            System.err.println("Erreur dans le serveur: " + e.getMessage());
        }

        PrintStream console = System.out; // sauvegarde de l'ancienne sortie

        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // écrit dans la console normale
                console.write(b);

                // écrit dans le TextArea
                javafx.application.Platform.runLater(() -> consoleLog.appendText(String.valueOf((char) b)));
            }
        }, true);

        System.setOut(ps);
        System.setErr(ps);
    }

    /** Méthode qui permet d'afficher la fenêtre pop-up des informations */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information partage");
        final String informations = "Connectez vous et choisissez ce que vous voulez partager";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Permet de lancer le partage des données */
    @FXML
    public void clicDemarrerPartage() {
        if (bouton.getText().equals("Démarrer partage")) {
            if ((partageScenarios.isSelected() || partageParametres.isSelected())) {
                try {
                    try {
                        Serveur.changerPort(Integer.parseInt(entreePort.getText()));
                        bouton.setText("Arrêter le partage");
                        Thread thread = new Thread(() -> {
                            try {
                                System.out.println("Démarrage");
                                Serveur.lancerServeur();
                            } catch (IOException ignored) {
                            }
                        });
                        thread.start();
                        gererEnvoiReception();
                    } catch (NumberFormatException e) {
                        Serveur.fermerServeur();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Port invalide");
                        final String informations = "Veuillez rentrer un port valide (ex : 5000)";
                        alert.setContentText(informations);
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    // Port pas possible
                    bouton.setText("Démarrer partage");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Incompatibilité");
                    final String informations = "Le serveur ne peut pas démarrer sur le port choisi (deja utilise)";
                    alert.setContentText(informations);
                    alert.showAndWait();
                }
            } else {
                /* On ne partage rien */
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Pas de donnees partagées");
                alert.setHeaderText("Vous ne partagez rien");
                final String informations = "Le serveur ne démarrera donc pas";
                alert.setContentText(informations);
                alert.showAndWait();
            }
        } else {
            Serveur.fermerServeur();
            bouton.setText("Démarrer partage");
        }
    }

    /** Méthode permettant d'exécuter le bon transfert de fichier */
    private void gererEnvoiReception() {
        Thread thread = new Thread(() -> {
            //Attend que le serveur démarre
            while (!Serveur.isOn()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //vide
                }
            }


            while (Serveur.isOn()) {

                try {
                    //Pour ne pas surcharger le serveur
                    Thread.sleep(100);

                    String message = Serveur.getMessage();
                    if (message != null) {
                        switch (message) {
                            case "Scénarios":
                                if (partageScenarios != null && partageScenarios.isSelected()) {
                                    Serveur.envoyerFichierChiffre(Parametres.getCheminStockage() + "/scenarioFC.json", "SCENARIOS");
                                } else {
                                    Serveur.envoyerMessage("Non partage");
                                }
                                break;
                            case "Paramètres":
                                if (partageParametres != null && partageParametres.isSelected()) {
                                    Serveur.envoyerFichierChiffre(Parametres.getCheminStockage() + "/parametres.ser", "PARAMETRES");
                                } else {
                                    Serveur.envoyerMessage("Non partage");
                                }
                                break;
                            default:
                                Serveur.envoyerMessage("Non partage");
                        }
                    }
                } catch (Exception inutilise) {
                    //catch vide
                    System.err.println(inutilise.getMessage());
                }
            }
        });
        thread.start();
    }


    /**
     * Met à jour les couleurs des boutons de sélection
     *
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
            titre.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
            port.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
            labelDonneesAPartager.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
            partageScenarios.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
            partageParametres.setStyle(ancienStyleTitre + "-fx-text-fill: " + couleur2 + ";");
        }

        if (couleur3) {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info.png"));
        } else {
            logoInformation.setImage(chargerImage("/iut/ga1/resquizz/images/info_blanc.png"));
        }
    }

    /**
     * Charge une image depuis le classpath en évitant les NullPointerException.
     *
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
     * Vide l'affichage des logs de la console
     */
    public void viderConsoleLogs() {
        consoleLog.clear();
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
            String ancienStylePort = port.getStyle();
            port.setStyle(ancienStylePort + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePartageScenarios = partageScenarios.getStyle();
            partageScenarios.setStyle(ancienStylePartageScenarios + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePartageParametres = partageParametres.getStyle();
            partageParametres.setStyle(ancienStylePartageParametres + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLabelDonneesAPartager = labelDonneesAPartager.getStyle();
            labelDonneesAPartager.setStyle(ancienStyleLabelDonneesAPartager + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleBouton = bouton.getStyle();
            bouton.setStyle(ancienStyleBouton + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleEntreePort = entreePort.getStyle();
            entreePort.setStyle(ancienStyleEntreePort + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLibelleReseau = libelleReseau.getStyle();
            libelleReseau.setStyle(ancienStyleLibelleReseau + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {

        String ancienStylePort = port.getStyle();
        port.setStyle(ancienStylePort +"-fx-font-size: " + taillePolice + ";");
        String ancienStylePartageScenarios = partageScenarios.getStyle();
        partageScenarios.setStyle(ancienStylePartageScenarios +"-fx-font-size: " + taillePolice + ";");
        String ancienStylePartageParametres = partageParametres.getStyle();
        partageParametres.setStyle(ancienStylePartageParametres +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLabelDonneesAPartager = labelDonneesAPartager.getStyle();
        labelDonneesAPartager.setStyle(ancienStyleLabelDonneesAPartager +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleBouton = bouton.getStyle();
        bouton.setStyle(ancienStyleBouton +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleEntreePort = entreePort.getStyle();
        entreePort.setStyle(ancienStyleEntreePort +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLibelleReseau = libelleReseau.getStyle();
        libelleReseau.setStyle(ancienStyleLibelleReseau +"-fx-font-size: " + taillePolice + ";");


    }

    /**
     * Permet d'ajout un clic sur un label
     * @param adresse du label
     */
    private void ajouterLabel(InetAddress adresse) {
        Label label = new Label(adresse.getHostName() + " " + adresse.getHostAddress());
        label.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");
        interfacesReseau.getChildren().add(label);
    }

}