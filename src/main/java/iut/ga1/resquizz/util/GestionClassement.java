/*
 * GestionClassement.java                                12 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import java.io.Serializable;

/**
 * Classe permettant de sauvegarder les points et les chronomètres de tous les jeux
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class GestionClassement implements Serializable {

    /** Serial UID */
    private static final long serialVersionUID = 1L;

    /** Instance unique des paramètres */
    private static GestionClassement instance = new GestionClassement();

    /** Retourne l'unique instance des paramètres */
    public static GestionClassement getInstance() {
        return instance;
    }

    /** Définit une nouvelle instance (utile lors du chargement par sérialisation) */
    public static void setInstance(GestionClassement nouvelleInstance) {
        instance = nouvelleInstance;
    }

    /* Attributs de points */
    private int pointGrille = Integer.MIN_VALUE;
    private int pointQCMPicto = Integer.MIN_VALUE;
    private int pointQCMDesc = Integer.MIN_VALUE;
    private int pointQCMAlea = Integer.MIN_VALUE;
    private int pointScenarioFC = Integer.MIN_VALUE;
    private int pointScenarioAlea = Integer.MIN_VALUE;

    /* Attributs de chronomètres */
    private Long chronoGrille = Long.MAX_VALUE;
    private Long chronoQCMPicto = Long.MAX_VALUE;
    private Long chronoQCMDesc = Long.MAX_VALUE;
    private Long chronoQCMAlea = Long.MAX_VALUE;
    private Long chronoScenarioFC = Long.MAX_VALUE;
    private Long chronoScenarioAlea = Long.MAX_VALUE;

    /* Méthodes d'accès aux attributs */
    public void _setPointGrille(int points) {
        pointGrille = points;
    }

    public int _getPointGrille() {
        return pointGrille;
    }

    public void _setPointQCMPicto(int points) {
        pointQCMPicto = points;
    }

    public int _getPointQCMPicto() {
        return pointQCMPicto;
    }

    public void _setPointQCMDesc(int points) {
        pointQCMDesc = points;
    }

    public int _getPointQCMDesc() {
        return pointQCMDesc;
    }

    public void _setPointQCMAlea(int points) {
        pointQCMAlea = points;
    }

    public int _getPointQCMAlea() {
        return pointQCMAlea;
    }

    public void _setPointScenarioFC(int points) {
        pointScenarioFC = points;
    }

    public int _getPointScenarioFC() {
        return pointScenarioFC;
    }

    public void _setPointScenarioAlea(int points) {
        pointScenarioAlea = points;
    }

    public int _getPointScenarioAlea() {
        return pointScenarioAlea;
    }

    public void _setChronoGrille(Long chrono) {
        chronoGrille = chrono;
    }

    public Long _getChronoGrille() {
        return chronoGrille;
    }

    public void _setChronoQCMPicto(Long chrono) {
        chronoQCMPicto = chrono;
    }

    public Long _getChronoQCMPicto() {
        return chronoQCMPicto;
    }

    public void _setChronoQCMDesc(Long chrono) {
        chronoQCMDesc = chrono;
    }

    public Long _getChronoQCMDesc() {
        return chronoQCMDesc;
    }

    public void _setChronoQCMAlea(Long chrono) {
        chronoQCMAlea = chrono;
    }

    public Long _getChronoQCMAlea() {
        return chronoQCMAlea;
    }

    public void _setChronoScenarioFC(Long chrono) {
        chronoScenarioFC = chrono;
    }

    public Long _getChronoScenarioFC() {
        return chronoScenarioFC;
    }

    public void _setChronoScenarioAlea(Long chrono) {
        chronoScenarioAlea = chrono;
    }

    public Long _getChronoScenarioAlea() {
        return chronoScenarioAlea;
    }

    /* Instance */
    public static void setPointGrille(int p) {
        instance._setPointGrille(p);
    }
    public static int getPointGrille() {
        return instance._getPointGrille();
    }
    public static void setPointQCMPicto(int p) {
        instance._setPointQCMPicto(p);
    }
    public static int getPointQCMPicto() {
        return instance._getPointQCMPicto();
    }
    public static void setPointQCMDesc(int p) {
        instance._setPointQCMDesc(p);
    }
    public static int getPointQCMDesc() {
        return instance._getPointQCMDesc();
    }
    public static void setPointQCMAlea(int p) {
        instance._setPointQCMAlea(p);
    }
    public static int getPointQCMAlea() {
        return instance._getPointQCMAlea();
    }
    public static void setPointScenarioFC(int p) {
        instance._setPointScenarioFC(p);
    }
    public static int getPointScenarioFC() {
        return instance._getPointScenarioFC();
    }
    public static void setPointScenarioAlea(int p) {
        instance._setPointScenarioAlea(p);
    }
    public static int getPointScenarioAlea() {
        return instance._getPointScenarioAlea();
    }

    public static void setChronoGrille(Long c) {
        instance._setChronoGrille(c);
    }
    public static Long getChronoGrille() {
        return instance._getChronoGrille();
    }
    public static void setChronoQCMPicto(Long c) {
        instance._setChronoQCMPicto(c);
    }
    public static Long getChronoQCMPicto() {
        return instance._getChronoQCMPicto();
    }
    public static void setChronoQCMDesc(Long c) {
        instance._setChronoQCMDesc(c);
    }
    public static Long getChronoQCMDesc() {
        return instance._getChronoQCMDesc();
    }
    public static void setChronoQCMAlea(Long c) {
        instance._setChronoQCMAlea(c);
    }
    public static Long getChronoQCMAlea() {
        return instance._getChronoQCMAlea();
    }
    public static void setChronoScenarioFC(Long c) {
        instance._setChronoScenarioFC(c);
    }
    public static Long getChronoScenarioFC() {
        return instance._getChronoScenarioFC();
    }
    public static void setChronoScenarioAlea(Long c) {
        instance._setChronoScenarioAlea(c);
    }
    public static Long getChronoScenarioAlea() {
        return instance._getChronoScenarioAlea();
    }

    /**
     * Réinitialise uniquement une catégorie spécifique.
     */
    public static void resetCategorie(String categorie) {
        GestionClassement c = getInstance();

        switch (categorie.toLowerCase()) {
            case "grille":
                c.pointGrille = Integer.MIN_VALUE;
                c.chronoGrille = Long.MAX_VALUE;
                break;

            case "picto":
                c.pointQCMPicto = Integer.MIN_VALUE;
                c.chronoQCMPicto = Long.MAX_VALUE;
                break;

            case "desc":
                c.pointQCMDesc = Integer.MIN_VALUE;
                c.chronoQCMDesc = Long.MAX_VALUE;
                break;

            case "alea":
                c.pointQCMAlea = Integer.MIN_VALUE;
                c.chronoQCMAlea = Long.MAX_VALUE;
                break;

            case "scenariofixe":
                c.pointScenarioFC = Integer.MIN_VALUE;
                c.chronoScenarioFC = Long.MAX_VALUE;
                break;

            case "aleapap":
                c.pointScenarioAlea = Integer.MIN_VALUE;
                c.chronoScenarioAlea = Long.MAX_VALUE;
                break;

            default:
                System.err.println("Catégorie inconnue : " + categorie);
                break;
        }
        GestionParametres.sauvegarderClassement();
    }
}
