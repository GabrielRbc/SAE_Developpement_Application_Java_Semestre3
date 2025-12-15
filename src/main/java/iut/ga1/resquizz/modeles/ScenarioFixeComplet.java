/*
 * RevisionGrille.java               17 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import com.google.gson.reflect.TypeToken;
import iut.ga1.resquizz.util.LectureJson;
import iut.ga1.resquizz.util.Parametres;
import iut.ga1.resquizz.util.Pictogramme;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Classe représentant le mode de jeu "Scénario fixe complet".
 * <p>
 * Dans ce mode, un scénario aléatoire est sélectionné parmi une
 * liste prédéfinie, et le joueur doit placer les pictogrammes
 * correspondant aux bons gestes dans le bon ordre.
 * Chaque pictogramme correctement placé rapporte des points,
 * et une erreur en retire.
 * </p>
 *
 * <p>Barème de points :
 * <ul>
 *   <li>+100 points par pictogramme bien placé</li>
 *   <li>– 50 points par pictogramme mal placé</li>
 * </ul>
 *</p>
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class ScenarioFixeComplet {

    /** Liste des scénarios chargés depuis le fichier JSON */
    private final HashMap<String, int[]> scenarios;

    /** Description du scénario sélectionné aléatoirement */
    private final String descriptionScenario;

    /** Générateur aléatoire pour choisir un scénario */
    private static final Random RANDOM = new Random();

    /** Liste des pictogrammes proposés au joueur */
    private final ArrayList<Pictogramme> listePropositions;

    /** Liste complète des pictogrammes de la grille SST */
    private final List<Pictogramme> tousLesPictogrammes;

    /** Séquence correcte à reproduire pour le scénario choisi */
    private final List<Pictogramme> sequenceCorrecte;

    /** Score actuel du joueur */
    private int points;

    /**
     * Constructeur qui :
     * sélectionne un scénario aléatoire et
     * initialise les pictogrammes.
     */
    public ScenarioFixeComplet() {
        // Chemins fichiers

        String cheminBase = "/iut/ga1/resquizz/json/scenariosFC.json";
        String cheminData = Parametres.getCheminStockage() + "/scenarioFC.json";

        LectureJson<String, int[]> lecteurJson = new LectureJson<>();
        Type type = new TypeToken<HashMap<String, int[]>>() {}.getType();

        // Charge les scénarios du fichier de base
        HashMap<String, int[]> scenariosBase;
        try {
            scenariosBase = lecteurJson.lireFichierRessource(cheminBase, type);
        } catch (RuntimeException e) {
            System.err.println("Impossible de lire le fichier de base : " + e.getMessage());
            scenariosBase = new HashMap<>();
        }

        // Charge les scénarios du fichier data s'il existe
        HashMap<String, int[]> scenariosData = new HashMap<>();
        File fichierData = new File(cheminData);
        if (fichierData.exists()) {
            try {
                scenariosData = lecteurJson.lireFichierRessource(cheminData, type);
            } catch (RuntimeException e) {
                System.err.println("Impossible de lire le fichier data : " + e.getMessage());
            }
        }

        // Fusionne les deux fichiers
        this.scenarios = new HashMap<>();
        this.scenarios.putAll(scenariosBase);
        this.scenarios.putAll(scenariosData);

        // Récupère la grille contenant tous les pictogrammes disponibles
        GrilleSST grilleSST = GrilleSST.getGrilleSST();
        this.tousLesPictogrammes = Arrays.asList(grilleSST.getPictogrammes());
        this.listePropositions = new ArrayList<>();
        this.sequenceCorrecte = new ArrayList<>();

        // Sélectionne un scénario aléatoire
        Map.Entry<String, int[]> scenarioAleatoire = getScenarioAleatoire();
        this.descriptionScenario = scenarioAleatoire.getKey();

        // Associe chaque identifiant d’action au pictogramme correspondant
        for (int id : scenarioAleatoire.getValue()) {
            for (Pictogramme p : tousLesPictogrammes) {
                if (p.getIdentifiant() == id) {
                    sequenceCorrecte.add(p);
                }
            }
        }
        initialiserPropositions();
    }

    /**
     * Sélectionne un scénario aléatoire parmi les scénarios prédéfinis.
     *
     * @return une entrée (clé = description, valeur = séquence correcte d'id)
     */
    private Map.Entry<String, int[]> getScenarioAleatoire() {
        // Transforme les scénarios en liste pour pouvoir choisir par index
        List<Map.Entry<String, int[]>> liste =
                new ArrayList<>(scenarios.entrySet());

        // Retourne un scénario au hasard
        return liste.get(RANDOM.nextInt(liste.size()));
    }

    /**
     * Initialise la liste des pictogrammes proposés au joueur.
     * Tous les pictogrammes de la grille sont affichés.
     */
    private void initialiserPropositions() {
        this.listePropositions.clear();
        this.listePropositions.addAll(tousLesPictogrammes);
    }

    /**
     * Calcule le score du joueur en comparant la séquence qu’il a proposée
     * à la séquence correcte du scénario sélectionné.
     */
    public void calculPoints(List<Pictogramme> sequenceUtilisateur) {
        points = 0;

        int tailleCorrecte = sequenceCorrecte.size();
        int tailleUtilisateur = sequenceUtilisateur.size();

        // Compare chaque position entre la séquence correcte et celle du joueur
        for (int i = 0; i < tailleCorrecte; i++) {
            if (i < tailleUtilisateur) {
                if (sequenceUtilisateur.get(i).getIdentifiant() ==
                        sequenceCorrecte.get(i).getIdentifiant()) {
                    points += 100; // Bon pictogramme à la bonne position
                } else {
                    points -= 50; // Mauvais pictogramme ou mal placé
                }
            } else {
                points -= 50; // Pictogramme manquant
            }
        }
    }

    /**
     * @return le score actuel du joueur
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return la liste des pictogrammes proposés
     */
    public ArrayList<Pictogramme> getPropositions() {
        return listePropositions;
    }

    /**
     * @return la description du scénario choisi
     */
    public String getDescriptionScenario() {
        return descriptionScenario;
    }

    /**
     * @return la séquence des pictogrammes corrects
     */
    public List<Pictogramme> getSequenceCorrecte() {
        return sequenceCorrecte;
    }
}