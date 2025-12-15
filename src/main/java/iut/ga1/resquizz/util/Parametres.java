/*
 * Parametres.java                                              22 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */

package iut.ga1.resquizz.util;

import java.io.*;

/**
 *  Classe qui permet de sauvegarder l'emplacement d'activation des options
 *  Soit depuis Multijoueur, soit depuis Ordinateur ou soit depuis le Menu
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class Parametres implements Serializable {

    /** Serial UID */
    private static final long serialVersionUID = 1L;

    /** Instance unique des paramètres (Singleton) */
    private static Parametres instance = new Parametres();

    /**
     * Retourne l'unique instance des paramètres
     * @return l'instance unique
     */
    public static Parametres getInstance() {
        return instance;
    }

    /**
     * Définit une nouvelle instance (utile lors du chargement par sérialisation)
     * @param nouvelleInstance l'instance à appliquer
     */
    public static void setInstance(Parametres nouvelleInstance) {
        instance = nouvelleInstance;
    }

    /** String qui permettait de sauvegarder le chemin */
    private String chemin = "";

    /** Hauteur et largeur de la fenêtre par défaut */
    private double hauteur = 720;
    private double largeur = 1280;

    private String couleurFenetre = "#293133"; // couleur par défaut
    private String couleurTitre = "#FFFFFF";   // couleur par défaut
    private boolean couleurLogo = false;       // noir par défaut, blanc sinon

    /** Chemin d'accès au dossier data */
    private String cheminStockage = "data"; // par défaut

    /** Permet de sauvegarder la police sélectionnée et sa taille */
    private String police = "/iut/ga1/resquizz/fonts/OpenSans-Regular.ttf";
    private int taillePolice = 18;

    public String _getCheminStockage() { return cheminStockage; }
    public void _setCheminStockage(String nouveauCheminStockage) { cheminStockage = nouveauCheminStockage; }

    /**
     * getter du chemin
     * @return chemin Le chemin d'accès
     */
    public String _getChemin() {
        return chemin;
    }

    /**
     * Setter du chemin
     * @param nouveauChemin Définit le nouveau chemin d'accès
     */
    public void _setChemin(String nouveauChemin) {
        chemin = nouveauChemin;
    }

    /*---- Résolution ----*/

    /** Getter de la hauteur de la fenêtre de l'application */
    public double _getHauteur() {
        return hauteur;
    }

    /**
     * Modifie la hauteur de la fenêtre de l'application
     * @param nouvelleHauteur de la fenêtre de l'application
     */
    public void _setHauteur(double nouvelleHauteur) {
        hauteur = nouvelleHauteur;
    }

    /** Getter de la largeur de la fenêtre de l'application */
    public double _getLargeur() {
        return largeur;
    }

    /**
     * Modifie la valeur de la fenêtre de l'application
     * @param nouvelleLargeur de la fenêtre
     */
    public void _setLargeur(double nouvelleLargeur) {
        largeur = nouvelleLargeur;
    }

    /**
     * Getter de la couleur du titre
     * @return la couleur
     */
    public String _getCouleurTitre() {
        return couleurTitre;
    }

    /**
     * Setter de la couleur du titre
     * @param couleurTitre à modifier
     */
    public void _setCouleurTitre(String couleurTitre) {
        this.couleurTitre = couleurTitre;
    }

    /**
     * Getter de la couleur des fenêtres
     * @return la couleur
     */
    public String _getCouleurFenetre() {
        return couleurFenetre;
    }

    /**
     * Setter de la couleur des fenêtres
     * @param couleurFenetre à modifier
     */
    public void _setCouleurFenetre(String couleurFenetre) {
        this.couleurFenetre = couleurFenetre;
    }

    /**
     * Getter de la couleur des logos
     * @return true si logo -> noir, false si -> blanc
     */
    public boolean _getCouleurLogo() {
        return couleurLogo;
    }

    /**
     * Setter de la couleur du logo (noir ou blanc)
     * @param couleurLogo à modifier
     */
    public void _setCouleurLogo(boolean couleurLogo) {
        this.couleurLogo = couleurLogo;
    }

    /**
     * Setter de la police
     * @param police à modifier
     */
    public void _setPolice(String police) {
        this.police = police;
    }

    /**
     * Getter de la police
     * @return la police actuelle
     */
    public String _getPolice() {
        return police;
    }

    public void _setTaillePolice(int taillePolice) {this.taillePolice = taillePolice; }
    public int _getTaillePolice() { return taillePolice; }

    public static String getChemin() { return instance._getChemin(); }
    public static void setChemin(String c) { instance._setChemin(c); }

    public static double getHauteur() { return instance._getHauteur(); }
    public static void setHauteur(double h) { instance._setHauteur(h); }

    public static double getLargeur() { return instance._getLargeur(); }
    public static void setLargeur(double l) { instance._setLargeur(l); }

    public static String getCouleurFenetre() { return instance._getCouleurFenetre(); }
    public static void setCouleurFenetre(String c) { instance._setCouleurFenetre(c); }

    public static String getCouleurTitre() { return instance._getCouleurTitre(); }
    public static void setCouleurTitre(String c) { instance._setCouleurTitre(c); }

    public static boolean getCouleurLogo() { return instance._getCouleurLogo(); }
    public static void setCouleurLogo(boolean b) { instance._setCouleurLogo(b); }

    public static String getPolice() { return instance._getPolice(); }
    public static void setPolice(String p) { instance._setPolice(p); }

    public static int getTaillePolice() {
        return instance._getTaillePolice();
    }
    public static void setTaillePolice(int t) {
        instance._setTaillePolice(t);
    }

    public static String getCheminStockage() { return instance._getCheminStockage(); }
    public static void setCheminStockage(String c) { instance._setCheminStockage(c); }

    /**
     * Méthode pour lancer l'application dans le bon dossier
     */
    public static void chargerCheminStockageDepuisFichier() {
        File f = new File("storage.txt");
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String chemin = br.readLine().trim();
                if (!chemin.isEmpty()) {
                    setCheminStockage(chemin);
                }
            } catch (IOException e) {
                System.err.println("Impossible de lire storage.txt : " + e.getMessage());
            }
        }
    }
}
