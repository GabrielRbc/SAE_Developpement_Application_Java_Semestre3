/*
 * ScenarioDonnees.java               23 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

/**
 * Classe représentant les données d'un scénario.
 * Utilisée pour les scénarios pas à pas et fixes complets.
 */
public class ScenarioDonnees {

    /** Description des étapes du scénario */
    private String[] description;

    /** Identifiants des réponses correctes pour chaque étape */
    private int[] idsCorrects;

    /**
     * Constructeur par défaut vide.
     */
    public ScenarioDonnees() {}

    public ScenarioDonnees(String[] description, int[] idsCorrects) {
        this.description = description;
        this.idsCorrects = idsCorrects;
    }

    /**
     * @return la description des étapes du scénario
     */
    public String[] getDescription() {
        return description;
    }

    /**
     * @return les identifiants des réponses correctes
     */
    public int[] getIdsCorrects() {
        return idsCorrects;
    }
}