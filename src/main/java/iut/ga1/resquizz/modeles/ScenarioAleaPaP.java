/*
 * ScenarioAleaPaP.java               17 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import com.google.gson.reflect.TypeToken;
import iut.ga1.resquizz.util.EtapeScenario;
import iut.ga1.resquizz.util.LectureJson;
import iut.ga1.resquizz.util.Pictogramme;
import iut.ga1.resquizz.util.ScenarioDonnees;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Classe représentant le mode de jeu "Scénario Pas à Pas aléatoire".
 * <p>
 * Dans ce mode, un scénario est sélectionné aléatoirement parmi une
 * liste prédéfinie. Chaque scénario est composé de plusieurs étapes,
 * chacune décrivant une situation précise à laquelle l'utilisateur doit
 * réagir en sélectionnant le pictogramme correspondant au bon geste.
 * L'utilisateur doit ainsi retrouver la séquence correcte étape par étape.
 * </p>
 *
 * <p>
 * Le scénario avance uniquement si le bon pictogramme est choisi.
 * Le système comptabilise également le nombre d'erreurs commises.
 * Si le joueur choisit un pictogramme incorrect, il perd 50 points.
 * Chaque pictogramme correctement placé rapporte 100 points.
 * </p>
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class ScenarioAleaPaP {

    /** Liste complète des pictogrammes disponibles dans la grille SST */
    private final List<Pictogramme> tousLesPictogrammes;

    /** Séquence correcte à suivre pour le scénario sélectionné */
    private final List<Pictogramme> sequenceCorrecte;

    /** Tableau de descriptions textuelles correspondant aux étapes du scénario */
    private final String[] descriptionsScenario;

    /** Index de l'étape actuelle (commence à 0) */
    private int etapeActuelle;

    /** Générateur aléatoire */
    private final Random random = new Random();

    /** Nombre d'erreurs commises par l'utilisateur */
    private int erreurs;

    /** Nombre de points obtenus par l'utilisateur */
    private int points;

    /**
     * Constructeur par défaut : sélectionne un scénario et initialise les données.
     */
    public ScenarioAleaPaP() {
        this.sequenceCorrecte = new ArrayList<>();
        this.etapeActuelle = 0;
        this.erreurs = 0;

        // Récupère la grille SST contenant tous les pictogrammes
        GrilleSST grilleSST = GrilleSST.getGrilleSST();
        this.tousLesPictogrammes = Arrays.asList(grilleSST.getPictogrammes());

        // Charge et sélectionne un scénario aléatoire
        ScenarioDonnees scenarioChoisi = chargerEtSelectionnerScenario();

        this.descriptionsScenario = scenarioChoisi.getDescription();

        // Construction de la séquence correcte de pictogrammes
        initialiserSequenceCorrecte(scenarioChoisi.getIdsCorrects());
    }

    /**
     * Charge le fichier JSON des scénarios, en sélectionne un au hasard,
     * et génère une variation unique (choix aléatoire des phrases).
     *
     * @return Un objet ScenarioDonnees contenant les descriptions et les IDs
     * prêts à être utilisés par le jeu.
     * @throws RuntimeException Si le fichier est introuvable ou illisible.
     */
    private ScenarioDonnees chargerEtSelectionnerScenario() {
        String chemin = "/iut/ga1/resquizz/json/scenariosAPaP.json";

        /*
         * On utilise TypeToken pour conserver ces types génériques à l'exécution.
         * Le JSON est une Map avec
         * – Clé : le nom du scénario (String).
         * – Valeur : une liste d'étapes (List<EtapeScenario>)
         */
        Type typeListe = new TypeToken<HashMap<String, List<EtapeScenario>>>(){}.getType();

        // Instanciation de la lecture JSON
        LectureJson<String, List<EtapeScenario>> lecture = new LectureJson<>();

        // Lire tout le fichier JSON
        Map<String, List<EtapeScenario>> tousLesScenarios;
        try {
            tousLesScenarios = lecture.lireFichierRessource(chemin, typeListe);
        } catch (Exception e) {
            throw new RuntimeException("Erreur critique chargement scénario : " + e.getMessage());
        }

        if (tousLesScenarios == null || tousLesScenarios.isEmpty()) {
            throw new RuntimeException("Le fichier JSON est vide ou mal formé !");
        }

        // Choisir un scénario au hasard (ex : "arrêt_cardiaque")
        List<String> keys = new ArrayList<>(tousLesScenarios.keySet());
        String nomScenarioChoisi = keys.get(random.nextInt(keys.size()));

        // Récupérer la liste des 5 étapes
        List<EtapeScenario> etapes = tousLesScenarios.get(nomScenarioChoisi);

        // Construire les données finales
        List<String> descriptionsChoisies = new ArrayList<>();
        List<Integer> idsChoisis = new ArrayList<>();

        for (EtapeScenario etape : etapes) {
            // L'ID est fixe pour cette étape
            idsChoisis.add(etape.pictogramme);

            // On choisit une phrase au hasard parmi celles disponibles
            if (etape.textes != null && !etape.textes.isEmpty()) {
                String texteHasard = etape.textes.get(random.nextInt(etape.textes.size()));
                descriptionsChoisies.add(texteHasard);
            } else {
                descriptionsChoisies.add("Description manquante...");
            }
        }

        /*
         * On convertit nos listes dynamiques en tableaux simples (String[] et int[])
         * car c'est le format attendu par la classe ScenarioDonnees.
         */
        return new ScenarioDonnees(
                descriptionsChoisies.toArray(new String[0]),
                idsChoisis.stream().mapToInt(i -> i).toArray()
        );
    }

    /**
     * Initialise la séquence de pictogrammes correcte en fonction des IDs définis pour le scénario.
     */
    private void initialiserSequenceCorrecte(int[] idsCorrects) {
        for (int id : idsCorrects) {
            for (Pictogramme p : tousLesPictogrammes) {
                if (p.getIdentifiant() == id) {
                    sequenceCorrecte.add(p);
                    break;
                }
            }
        }
    }

    /**
     * Calcule les points en fonction de la correction de la réponse.
     * @param estCorrecte paramètre indiquant si la réponse est correcte
     */
    public void calculPoints(boolean estCorrecte) {
        if (estCorrecte) {
            points += 100; // Bonne réponse : +100 points
        } else {
            points -= 50;  // Mauvaise réponse : -50 points
        }
    }

    /**
     * Vérifie si le pictogramme choisi par l'utilisateur correspond à celui attendu.
     *
     * @param reponse pictogramme sélectionné
     * @return true si la réponse est correcte, false sinon
     */
    public boolean verifierReponse(Pictogramme reponse) {
        return reponse.equals(sequenceCorrecte.get(etapeActuelle));
    }

    /**
     * Passe à l'étape suivante du scénario.
     *
     * @return true si l'on peut avancer, false si on est déjà à la fin
     */
    public boolean etapeSuivante() {
        if (etapeActuelle < descriptionsScenario.length - 1) {
            etapeActuelle++;
            return true;
        }
        return false; // Dernière étape atteinte
    }

    /**
     * Réinitialise le scénario sans en sélectionner un nouveau.
     * Remet l'étape à zéro et le nombre d'erreurs à 0.
     */
    public void reinitialiser() {
        this.etapeActuelle = 0;
        this.erreurs = 0;
        this.points = 0;
    }

    /**
     * @return la description textuelle correspondant à l'étape actuelle
     */
    public String getDescriptionEtapeActuelle() {
        return descriptionsScenario[etapeActuelle];
    }

    /**
     * @return true si l'étape actuelle est la dernière
     */
    public boolean estDerniereEtape() {
        return etapeActuelle == descriptionsScenario.length - 1;
    }

    /**
     * @return le pictogramme correct attendu à l'étape actuelle
     */
    public Pictogramme getReponseCorrecte() {
        return sequenceCorrecte.get(etapeActuelle);
    }

    /**
     * Incrémente le compteur d'erreurs.
     */
    public void incrementerErreurs() {
        erreurs++;
    }

    /**
     * @return le nombre total d'erreurs commises
     */
    public int getErreurs() {
        return erreurs;
    }

    /**
     * Retourne une liste de 4 pictogrammes mélangés pour l'étape actuelle
     * – La bonne réponse
     * – 3 autres pictogrammes choisis aléatoirement
     */
    public List<Pictogramme> getPropositions() {
        List<Pictogramme> propositions = new ArrayList<>();

        // Récupérer la bonne réponse pour l'étape en cours
        Pictogramme bonneReponse = getReponseCorrecte();
        propositions.add(bonneReponse);

        // Créer une liste temporaire pour choisir les pictogrammes aléatoires
        // On copie toute la liste pour ne pas modifier l'originale
        List<Pictogramme> pictogrammesAleatoire = new ArrayList<>(tousLesPictogrammes);

        // On retire la bonne réponse pour ne pas la sélectionner deux fois
        pictogrammesAleatoire.remove(bonneReponse);

        // On mélange les propositions restantes
        Collections.shuffle(pictogrammesAleatoire);

        // Sélectionner les 3 premiers pictogrammes aléatoires
        for (int i = 0; i < 3 && i < pictogrammesAleatoire.size(); i++) {
            propositions.add(pictogrammesAleatoire.get(i));
        }

        // Mélanger la liste finale (pour que la bonne réponse ne soit pas toujours au début)
        Collections.shuffle(propositions);

        return propositions;
    }

    /**
     * @return le nombre de points obtenus
     */
    public int getPoints() {
        return points;
    }
}