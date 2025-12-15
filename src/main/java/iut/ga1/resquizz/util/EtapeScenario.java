/*
 * EtapeScenario.java               24 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import java.util.List;

/**
 * Représente une étape d'un scénario (un pictogramme + une liste de textes possibles).
 * Cette classe est utilisée pour le mode de jeu "Scénario aléatoire pas à pas".
 */
public class EtapeScenario {

    /** Identifiant du pictogramme associé à cette étape */
    public int pictogramme;

    /** Liste des textes associés à cette étape */
    public List<String> textes;

    /**
     * Constructeur par défaut
     */
    public EtapeScenario() {}
}