/*
 * ControleurMenuParametres.java                           14 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.controleurs;

import iut.ga1.resquizz.modeles.Lanceur;
import iut.ga1.resquizz.util.GestionParametres;
import iut.ga1.resquizz.util.Parametres;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Contrôleur de la vue "MenuParametres"
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Esteban Roveri
 * @author Noé Rebourg
 */
public class ControleurMenuParametres {

    /** Constantes des différentes tailles proposées */
    private static final String PETITE_TAILLE = "1280 x 720";
    private static final String MOYENNE_TAILLE = "1360 x 768";
    private static final String GRANDE_TAILLE = "1440 x 900";
    private static final String ENORME_TAILLE = "1680 x 1050";

    /** Constantes des différentes couleurs proposées */
    private static final String COULEUR1 = "Nouvelle Lune";
    private static final String COULEUR2 = "Beige";
    private static final String COULEUR3 = "Framboise";
    private static final String COULEUR4 = "Bleu Lagon";
    private static final String COULEUR5 = "Hibiscus";
    private static final String COULEUR6 = "Lavande";
    private static final String COULEUR7 = "Crépuscule";
    private static final String COULEUR8 = "Citron";
    private static final String COULEUR9 = "Nature";

    /** Constantes des différentes tailles de fenêtre disponible (Hauteur, Largeur) */
    private static final double[] VALEUR_PETITE_TAILLE = {1280, 720};
    private static final double[] VALEUR_MOYENNE_TAILLE = {1360, 768};
    private static final double[] VALEUR_GRANDE_TAILLE = {1440, 900};
    private static final double[] VALEUR_ENORME_TAILLE = {1680, 1050};

    /**
     * Ordre dans le tableau :
     * couleurFenetre
     * couleurEcriture
     * couleurFondBouton
     * couleurInterne
     * autres
     * <p>
     * Ordre des couleurs :
     * Nouvelle Lune
     * Beige
     * Framboise
     * Bleu Lagon
     * Hibiscus
     * Lavande
     * Crépuscule
     * Citron
     * Nature
     */
    private static final String COULEUR_SOMBRE = "#293133";
    private static final String COULEUR_BEIGE = "#E8DCCA";
    private static final String COULEUR_ROUGE_PASTEL = "#E06969";
    private static final String COULEUR_BLEU_PASTEL = "#BBD3DB";
    private static final String COULEUR_ROSE_PASTEL = "#EBD6E9";
    private static final String COULEUR_VIOLET_PASTEL = "#BB9FCB";
    private static final String COULEUR_ORANGE_PASTEL = "#FFD280";
    private static final String COULEUR_JAUNE_PASTEL = "#FCFFD0";
    private static final String COULEUR_VERT_PASTEL = "#A8DB9E";

    /** Couleur du titre */
    private static final String COULEUR_TITRE_SOMBRE = "#293133";
    private static final String COULEUR_TITRE_CLAIR = "#FFFFFF";

    /** Chemin des polices */
    private static final String POLICE_LORA = "/iut/ga1/resquizz/fonts/Lora-Regular.ttf";
    private static final String POLICE_MERRIWEATHER = "/iut/ga1/resquizz/fonts/Merriweather-Regular.ttf";
    private static final String POLICE_MONTSERRAT = "/iut/ga1/resquizz/fonts/Montserrat-Regular.ttf";
    private static final String POLICE_OPENSANS = "/iut/ga1/resquizz/fonts/OpenSans-Regular.ttf";
    private static final String POLICE_RALEWAY = "/iut/ga1/resquizz/fonts/Raleway-Regular.ttf";

    /* Identifiant des labels de la fenêtre */
    /** Identifiant du label de la Résolution */
    @FXML
    private Label labelResolution;

    /** Identifiant du label du Thème de l'application */
    @FXML
    private Label labelTheme;

    /** Identifiant du label de la police d'écriture */
    @FXML
    private Label labelPolice;

    /** Identifiant du label de la taille de la police */
    @FXML
    private Label labelTaillePolice;

    /** Identifiant de la fenêtre */
    @FXML
    private BorderPane fenetre;

    /* Identifiant des boutons */
    /** Identifiant du bouton du réseau */
    @FXML
    private Button buttonReseau;

    /** Identifiant du bouton du choix de stockage */
    @FXML
    private Button buttonStockage;

    /** Identifiant du bouton retour */
    @FXML
    private Button buttonRetour;

    /** Titre de la fenêtre */
    @FXML
    private Label titre;

    /** Liste déroulante des différentes couleurs disponibles */
    @FXML
    private ComboBox<String> CouleurTheme;

    /** Liste déroulante des Polices disponibles */
    @FXML
    private ComboBox<String> ComboPolice;

    /** Liste déroulante des Tailles de police disponibles */
    @FXML
    private ComboBox<String> ComboTaille;

    /** Liste déroulante des résolutions disponibles */
    @FXML
    private ComboBox<String> ComboResolution;

    /** Identifiant de logo Information */
    @FXML
    private ImageView logoInformation;

    /** Initialisation au départ */
    @FXML
    public void initialize() {
        /* Ajoute des valeurs aux combos box de la fenêtre */
        ComboResolution.getItems().addAll(PETITE_TAILLE, MOYENNE_TAILLE, GRANDE_TAILLE,
                ENORME_TAILLE);

        CouleurTheme.getItems().addAll(COULEUR1, COULEUR2, COULEUR3, COULEUR4, COULEUR5, COULEUR6,
                COULEUR7, COULEUR8, COULEUR9);

        ComboPolice.getItems().addAll("Open Sans", "Lora", "Montserrat", "Merriweather", "Raleway");

        ComboTaille.getItems().addAll("10", "11", "12", "13", "14", "15", "16", "17", "18", "20", "21", "22",
                "23", "24");

        /* Permet d'afficher la bonne valeur dans la combo box au lancement de l'application */
        if (Objects.equals(Parametres.getPolice(), POLICE_OPENSANS)) {
            ComboPolice.setPromptText("Open Sans");
        } else if (Objects.equals(Parametres.getPolice(), POLICE_LORA)) {
            ComboPolice.setPromptText("Lora");
        } else if (Objects.equals(Parametres.getPolice(), POLICE_MONTSERRAT)) {
            ComboPolice.setPromptText("Montserrat");
        } else if (Objects.equals(Parametres.getPolice(), POLICE_MERRIWEATHER)) {
            ComboPolice.setPromptText("Merriweather");
        } else if (Objects.equals(Parametres.getPolice(), POLICE_RALEWAY)) {
            ComboPolice.setPromptText("Raleway");
        }

        /* Permet d'afficher la bonne valeur dans la combo box au lancement de l'application */
        if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_SOMBRE)) {
            CouleurTheme.setPromptText(COULEUR1);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_BEIGE)) {
            CouleurTheme.setPromptText(COULEUR2);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_ROUGE_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR3);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_BLEU_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR4);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_ROSE_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR5);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_VIOLET_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR6);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_ORANGE_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR7);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_JAUNE_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR8);
        } else if (Objects.equals(Parametres.getCouleurFenetre(), COULEUR_VERT_PASTEL)) {
            CouleurTheme.setPromptText(COULEUR9);
        }

        /* Permet d'afficher la bonne valeur dans la combo box au lancement de l'application */
        if (Objects.equals(Parametres.getHauteur(), VALEUR_PETITE_TAILLE[1])) {
            ComboResolution.setPromptText(PETITE_TAILLE);
        } else if (Objects.equals(Parametres.getHauteur(), VALEUR_MOYENNE_TAILLE[1])) {
            ComboResolution.setPromptText(MOYENNE_TAILLE);
        } else if (Objects.equals(Parametres.getHauteur(), VALEUR_GRANDE_TAILLE[1])) {
            ComboResolution.setPromptText(GRANDE_TAILLE);
        } else if (Objects.equals(Parametres.getHauteur(), VALEUR_ENORME_TAILLE[1])) {
            ComboResolution.setPromptText(ENORME_TAILLE);
        }

        /* Permet d'afficher la bonne valeur dans la combo box au lancement de l'application */
        if (Objects.equals(Parametres.getTaillePolice(), 10)) {
            ComboTaille.setPromptText("10");
        } else if (Objects.equals(Parametres.getTaillePolice(), 11)) {
            ComboTaille.setPromptText("11");
        } else if (Objects.equals(Parametres.getTaillePolice(), 12)) {
            ComboTaille.setPromptText("12");
        } else if (Objects.equals(Parametres.getTaillePolice(), 13)) {
            ComboTaille.setPromptText("13");
        } else if (Objects.equals(Parametres.getTaillePolice(), 14)) {
            ComboTaille.setPromptText("14");
        } else if (Objects.equals(Parametres.getTaillePolice(), 15)) {
            ComboTaille.setPromptText("15");
        } else if (Objects.equals(Parametres.getTaillePolice(), 16)) {
            ComboTaille.setPromptText("16");
        } else if (Objects.equals(Parametres.getTaillePolice(), 17)) {
            ComboTaille.setPromptText("17");
        } else if (Objects.equals(Parametres.getTaillePolice(), 18)) {
            ComboTaille.setPromptText("18");
        } else if (Objects.equals(Parametres.getTaillePolice(), 19)) {
            ComboTaille.setPromptText("19");
        } else if (Objects.equals(Parametres.getTaillePolice(), 20)) {
            ComboTaille.setPromptText("20");
        } else if (Objects.equals(Parametres.getTaillePolice(), 21)) {
            ComboTaille.setPromptText("21");
        } else if (Objects.equals(Parametres.getTaillePolice(), 22)) {
            ComboTaille.setPromptText("22");
        } else if (Objects.equals(Parametres.getTaillePolice(), 23)) {
            ComboTaille.setPromptText("23");
        } else if (Objects.equals(Parametres.getTaillePolice(), 24)) {
            ComboTaille.setPromptText("24");
        }
    }

    /** Pour retourner au menu principal */
    @FXML
    public void gererClicRetour() {

        switch (Parametres.getChemin()) {
            case "MenuPrincipal":
                Lanceur.activerFenetreMenu();
                break;
            case "RevisionGrille":
                Lanceur.activerFenetreRevisionGrille();
                break;
            case "MenuJeux":
                Lanceur.activerFenetreJeux();
                break;
            case "QCM":
                Lanceur.activerFenetreQCM();
                break;
            case "ScenarioFixe":
                Lanceur.activerFenetreSceFixeComplet();
                break;
            case "ScenarioAlea":
                Lanceur.activerFenetreSceAleaPap();
                break;
            case "Classement":
                Lanceur.activerFenetreClassement();
                break;
        }
    }

    /** Pour afficher les informations de la page */
    @FXML
    public void gererClicInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Paramètres");
        final String informations = "Gérez ici les paramètres de l’application :\n" +
                "Personnalisez le thème (couleur de fond, police).\n" +
                "Ajustez la résolution de la fenêtre.\n" +
                "Choisissez la zone de stockage des données (scores, paramètres, etc.).\n" +
                "Importez ou exportez vos paramètres.\n" +
                "Accédez au partage de scénarios.";
        alert.setContentText(informations);
        alert.showAndWait();
    }

    /** Pour afficher les couleurs disponibles */
    @FXML
    public void gererClicCouleur() {
        String CouleurSelectionnee = CouleurTheme.getValue();

        // Si aucune option n'est sélectionnée, on ne fait rien
        if (CouleurSelectionnee == null) {
            return;
        }

        switch (CouleurSelectionnee) {
            case COULEUR1:
                Parametres.setCouleurFenetre(COULEUR_SOMBRE);
                Parametres.setCouleurTitre(COULEUR_TITRE_CLAIR);
                Parametres.setCouleurLogo(false);
                break;
            case COULEUR2:
                Parametres.setCouleurFenetre(COULEUR_BEIGE);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR3:
                Parametres.setCouleurFenetre(COULEUR_ROUGE_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR4:
                Parametres.setCouleurFenetre(COULEUR_BLEU_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR5:
                Parametres.setCouleurFenetre(COULEUR_ROSE_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR6:
                Parametres.setCouleurFenetre(COULEUR_VIOLET_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR7:
                Parametres.setCouleurFenetre(COULEUR_ORANGE_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR8:
                Parametres.setCouleurFenetre(COULEUR_JAUNE_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
            case COULEUR9:
                Parametres.setCouleurFenetre(COULEUR_VERT_PASTEL);
                Parametres.setCouleurTitre(COULEUR_TITRE_SOMBRE);
                Parametres.setCouleurLogo(true);
                break;
        }
        Lanceur.redistribuerCouleur();

        sauvegarder();
    }

    /** Pour afficher les polices disponibles */
    @FXML
    public void gererClicPolice() {
        String PoliceSelectionnee = ComboPolice.getValue();

        // Si aucune option n'est sélectionnée, on ne fait rien
        if (PoliceSelectionnee == null) {
            return;
        }
        //"Open Sans", "Lora", "Montserrat", "Merriweather", "Raleway"
        switch (PoliceSelectionnee) {
            case "Open Sans":
                Parametres.setPolice(POLICE_OPENSANS);
                break;
            case "Lora":
                Parametres.setPolice(POLICE_LORA);
                break;
            case "Montserrat":
                Parametres.setPolice(POLICE_MONTSERRAT);
                break;
            case "Merriweather":
                Parametres.setPolice(POLICE_MERRIWEATHER);
                break;
            case "Raleway":
                Parametres.setPolice(POLICE_RALEWAY);
                break;
        }
        Lanceur.redistribuerPolice();

        sauvegarder();
    }

    /** Permet de gerer la taille de la police d'écriture */
    @FXML
    public void gererTaillePolice() {

        String TailleSelectionnee = ComboTaille.getValue();

        // Si aucune option n'est sélectionnée, on ne fait rien
        if (TailleSelectionnee == null) {
            return;
        }
        // entre 10 et 24
        switch (TailleSelectionnee) {
            case "10":
                Parametres.setTaillePolice(10);
                break;
            case "11":
                Parametres.setTaillePolice(11);
                break;
            case "12":
                Parametres.setTaillePolice(12);
                break;
            case "13":
                Parametres.setTaillePolice(13);
                break;
            case "14":
                Parametres.setTaillePolice(14);
                break;
            case "15":
                Parametres.setTaillePolice(15);
                break;
            case "16":
                Parametres.setTaillePolice(16);
                break;
            case "17":
                Parametres.setTaillePolice(17);
                break;
            case "18":
                Parametres.setTaillePolice(18);
                break;
            case "19":
                Parametres.setTaillePolice(19);
                break;
            case "20":
                Parametres.setTaillePolice(20);
                break;
            case "21":
                Parametres.setTaillePolice(21);
                break;
            case "22":
                Parametres.setTaillePolice(22);
                break;
            case "23":
                Parametres.setTaillePolice(23);
                break;
            case "24":
                Parametres.setTaillePolice(24);
                break;
        }
        Lanceur.redistribuerTaillePolice();
        sauvegarder();

    }

    /** Pour afficher les résolutions disponibles */
    @FXML
    public void gererClicResolution() {
        String ResolutionSelectionne = ComboResolution.getValue();

        if (ResolutionSelectionne == null) {
            return;
        }

        switch (ResolutionSelectionne) {
            case PETITE_TAILLE:
                Parametres.setLargeur(VALEUR_PETITE_TAILLE[0]);
                Parametres.setHauteur(VALEUR_PETITE_TAILLE[1]);
                break;
            case MOYENNE_TAILLE:
                Parametres.setLargeur(VALEUR_MOYENNE_TAILLE[0]);
                Parametres.setHauteur(VALEUR_MOYENNE_TAILLE[1]);
                break;
            case GRANDE_TAILLE:
                Parametres.setLargeur(VALEUR_GRANDE_TAILLE[0]);
                Parametres.setHauteur(VALEUR_GRANDE_TAILLE[1]);
                break;
            case ENORME_TAILLE:
                Parametres.setLargeur(VALEUR_ENORME_TAILLE[0]);
                Parametres.setHauteur(VALEUR_ENORME_TAILLE[1]);
                break;
        }

        Lanceur.fenetreActuelle.setWidth(Parametres.getLargeur());
        Lanceur.fenetreActuelle.setHeight(Parametres.getHauteur());
        Lanceur.fenetreActuelle.centerOnScreen();
        Lanceur.redistribuerResolution();

        sauvegarder();
    }

    /** Pour modifier la zone de stockage */
    public void gererClicStockage() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir la zone de stockage");

        // On ouvre dans le dossier actuel si possible
        File dossierActuel = new File(Parametres.getCheminStockage());
        if (dossierActuel.exists() && dossierActuel.isDirectory()) {
            chooser.setInitialDirectory(dossierActuel);
        }

        File dossierChoisi = chooser.showDialog(null);

        if (dossierChoisi != null) {
            try {
                GestionParametres.deplacerDonneesVers(dossierChoisi.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors du déplacement des données : " + e.getMessage());
            }
        }
    }

    /** Pour partager des scénarios */
    @FXML
    public void gererClicPartageScenario() {
        Lanceur.activerFenetreScenario();
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
            labelResolution.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
            labelPolice.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
            labelTaillePolice.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
            labelTheme.setStyle(ancienStyleTitre +"-fx-text-fill: " + couleur2 + ";");
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

    /** Permet de sauvegarder les paramètres */
    private void sauvegarder() {
        GestionParametres.sauvegarderParametres();
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

            // On garde aussi le style existant du titre (notamment sa couleur)
            String ancienStyleTitre = titre.getStyle();
            titre.setStyle(ancienStyleTitre + "-fx-font-family: '" + font.getFamily() + "';");


            String ancienStyleCouleurTheme = CouleurTheme.getStyle();
            CouleurTheme.setStyle(ancienStyleCouleurTheme + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleComboResolution = ComboResolution.getStyle();
            ComboResolution.setStyle(ancienStyleComboResolution + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleComboPolice = ComboPolice.getStyle();
            ComboPolice.setStyle(ancienStyleComboPolice + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleComboTaille = ComboTaille.getStyle();
            ComboTaille.setStyle(ancienStyleComboTaille + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonReseau = buttonReseau.getStyle();
            buttonReseau.setStyle(ancienStyleButtonReseau + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonStockage = buttonStockage.getStyle();
            buttonStockage.setStyle(ancienStyleButtonStockage + "-fx-font-family: '" + font.getFamily() + "';");
            String ancienStyleButtonRetour = buttonRetour.getStyle();
            buttonRetour.setStyle(ancienStyleButtonRetour + "-fx-font-family: '" + font.getFamily() + "';");
        }
    }

    /**
     * Permet de mettre à jour la taille de la police
     * @param taillePolice nouvelle taille de police
     */
    public void mettreAJourTaillePolice(int taillePolice) {

        String ancienStyleCouleurTheme = CouleurTheme.getStyle();
        CouleurTheme.setStyle(ancienStyleCouleurTheme +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleComboResolution = ComboResolution.getStyle();
        ComboResolution.setStyle(ancienStyleComboResolution +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleComboPolice = ComboPolice.getStyle();
        ComboPolice.setStyle(ancienStyleComboPolice +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonReseau = buttonReseau.getStyle();
        buttonReseau.setStyle(ancienStyleButtonReseau +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonStockage = buttonStockage.getStyle();
        buttonStockage.setStyle(ancienStyleButtonStockage +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleButtonRetour = buttonRetour.getStyle();
        buttonRetour.setStyle(ancienStyleButtonRetour +"-fx-font-size: " + taillePolice + ";");
        String ancienStyleTaillePolice = ComboTaille.getStyle();
        ComboTaille.setStyle(ancienStyleTaillePolice +"-fx-font-size: " + taillePolice + ";");
    }
}