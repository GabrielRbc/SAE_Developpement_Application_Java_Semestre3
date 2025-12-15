/*
 * ControleurReceptionScenario.java                       17 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.AdresseIP;
import iut.ga1.resquizz.modeles.Client;
import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.util.GestionParametres;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Contrôleur pour la reception des données
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurReceptionScenario {

    /** Identifiant du titre de la fenêtre */
    @FXML
    private Label titre;

    /** Identifiant du label du serveur auquel se connecté */
    @FXML
    private Label machine;

    /** Identifiant du label du port */
    @FXML
    private Label port;

    /** Identifiant des adresses disponibles */
    @FXML
    private Label adresse;

    /** Identifiant des données disponibles */
    @FXML
    private Label labelDonnees;

    /** identifiant de la fenêtre */
    @FXML
    private BorderPane fenetre;

    /** Identifiant du logo 'I' des informations */
    @FXML
    private ImageView logoInformation;

    /** Identifiant du textfield du port */
    @FXML
    private TextField entreePort;

    /** Identifiant du textfield du l'ip à joindre */
    @FXML
    private TextField entreeIp;

    /** Identifiant du bouton connexion et déconnexion */
    @FXML
    private Button bouton;

    /** Identifiant du bouton d'appel des scénarios */
    @FXML
    private Button buttonScenarios;

    /** Identifiant du bouton d'appel des paramètres */
    @FXML
    private Button buttonParametres;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Identifiant de la Vbox contenant la liste des ip disponibles */
    @FXML
    private VBox listeLabels;

    /** Méthode pour retourner à la page précédente */
    @FXML
    public void gererClicRetour() {
        Lanceur.activerFenetreScenario();

        Client.deconnecter();
        bouton.setText("Se connecter");
    }

    /** Méthode d'initialisation au démarrage de l'application */
    @FXML
    public void initialize() {
        //Entree de notre adresse dans le TextField
        try {
            entreeIp.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println("Host unknown exception: " + e.getMessage());
        }

        // Ajout de toutes les adresses dans le scrollPane
        try {
            for (InetAddress addr : AdresseIP.scanReseau()){
                ajouterLabelCliquable(addr);
            }
        } catch (Exception e) {
            System.err.println("Erreur dans le serveur: " + e.getMessage());
        }

    }

    /** Méthode pour se connecter */
    @FXML
    public void seConnecter() {
        if (bouton.getText().equals("Se connecter")) {
            //Si la connexion échoue
            if (!Client.connecter(entreeIp.getText(), Integer.parseInt(entreePort.getText()))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de connexion");
                String informations = "Le client ne peut pas se connecter";
                alert.setContentText(informations);
                alert.showAndWait();
            } else {
                //Le client est connecté donc on change le texte du bouton
                bouton.setText("Se déconnecter");
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ignoree) {

                }
            }
        } else {
            Client.deconnecter();
            bouton.setText("Se connecter");
        }
    }

    /** Méthode pour demander les scénarios */
    @FXML
    public void getScenarios() {
        // Vérifier si on est déjà connecté
        if (!Client.estConnecte()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Non connecté");
            alert.setContentText("Veuillez d'abord vous connecter au serveur.");
            alert.showAndWait();
        } else {
            // Envoie du message codé
            Client.creerMessage("Scénarios");
            System.out.println("Message 'Scénario' envoyé au serveur.");
        }
    }

    /** Méthode pour demander les paramètres */
    @FXML
    public void getParametres() {
        // Vérifier si on est déjà connecté
        if (!Client.estConnecte()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Non connecté");
            alert.setContentText("Veuillez d'abord vous connecter au serveur.");
            alert.showAndWait();
        } else {
            // Envoie du message codé
            Client.creerMessage("Paramètres");
            System.out.println("Message 'Paramètres' envoyé au serveur.");
        }
    }

    /** Permet de relancer les paramètres de l'application avec le partage
     * Evite de relancer toute l'application
     */
    public static void afficherNouveauParametre() {
        GestionParametres.chargerParametres();

        Lanceur.redistribuerCouleur();
        Lanceur.redistribuerResolution();
        Lanceur.redistribuerPolice();
        Lanceur.redistribuerTaillePolice();
    }

    /** Méthode pour afficher le pop-up des informations */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information réception");
        final String informations = "Vous pouvez choisir le port et l'adresse IP de la machine"
                +" dont vous souhaitez recevoir les scénarios ou les paramètres";
        alert.setContentText(informations);
        alert.showAndWait();
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
            port.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
            machine.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
            adresse.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
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
            // On garde le style existant de la fenêtre
            String ancienStyleFenetre = fenetre.getStyle();
            fenetre.setStyle(ancienStyleFenetre + "-fx-font-family: '" + font.getFamily() + "';");

            // On garde aussi le style existant du titre (notamment sa couleur).
            String ancienStyleTitre = titre.getStyle();
            titre.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStylePort = port.getStyle();
            port.setStyle(ancienStylePort + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleAdresse = adresse.getStyle();
            adresse.setStyle(ancienStyleAdresse + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleMachine = machine.getStyle();
            machine.setStyle(ancienStyleMachine + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleBouton = bouton.getStyle();
            bouton.setStyle(ancienStyleBouton + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonScenarios = buttonScenarios.getStyle();
            buttonScenarios.setStyle(ancienStyleButtonScenarios + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonParametres = buttonParametres.getStyle();
            buttonParametres.setStyle(ancienStyleButtonParametres + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleEntreePort = entreePort.getStyle();
            entreePort.setStyle(ancienStyleEntreePort + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleEntreeIp = entreeIp.getStyle();
            entreeIp.setStyle(ancienStyleEntreeIp + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleLabelDonnees= labelDonnees.getStyle();
            labelDonnees.setStyle(ancienStyleLabelDonnees + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {

        String ancienStylePort = port.getStyle();
        port.setStyle(ancienStylePort +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleAdresse = adresse.getStyle();
        adresse.setStyle(ancienStyleAdresse +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleMachine = machine.getStyle();
        machine.setStyle(ancienStyleMachine +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleBouton = bouton.getStyle();
        bouton.setStyle(ancienStyleBouton +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonScenarios = buttonScenarios.getStyle();
        buttonScenarios.setStyle(ancienStyleButtonScenarios +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonParametres = buttonParametres.getStyle();
        buttonParametres.setStyle(ancienStyleButtonParametres +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleEntreePort = entreePort.getStyle();
        entreePort.setStyle(ancienStyleEntreePort +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleEntreeIp = entreeIp.getStyle();
        entreeIp.setStyle(ancienStyleEntreeIp +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleLabelDonnees= labelDonnees.getStyle();
        labelDonnees.setStyle(ancienStyleLabelDonnees +"-fx-font-size: " + taillePolice + ";");
    }

    /**
     * Permet d'ajout un clic sur un label
     * @param adresse du label
     */
    private void ajouterLabelCliquable(InetAddress adresse) {
        Label label = new Label(adresse.getHostName() + " " + adresse.getHostAddress());
        label.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");

        label.setOnMouseClicked(event -> {
            System.out.println("Label cliqué : " + label.getText());
            entreeIp.setText(adresse.getHostAddress());
        });

        listeLabels.getChildren().add(label);
    }
}