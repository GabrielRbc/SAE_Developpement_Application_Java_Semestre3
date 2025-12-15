/*
 * Scenario.java                                 21 Nov. 2025
 * IUT de rodez Info 2, 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestion complète des scénarios fixes (scenarioFC.json).
 * - Création automatique du fichier si absent
 * - Lecture / Écriture JSON
 * - Ajout de scénarios avec vérification de doublons et validité
 */
public class Scenario {

    private static final String NOM_FICHIER = "scenarioFC.json";
    private static final Type TYPE_SCENARIOS = new TypeToken<HashMap<String, List<Integer>>>() {}.getType();
    private static final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    private final Path cheminFichier;

    /**
     * Constructeur par défaut : définit le chemin vers le fichier scenarioFC.json
     * dans le dossier de stockage défini par Parametres
     */
    public Scenario() {
        this.cheminFichier = Paths.get(Parametres.getCheminStockage(), NOM_FICHIER);
        creerFichierScenarioSiAbsent(); // utilise la version non statique
    }

    /**
     * Constructeur de scénario
     * @param cheminFichierCustom du fichier personnalisable
     */
    public Scenario(String cheminFichierCustom) {
        this.cheminFichier = Paths.get(cheminFichierCustom);
        creerFichierScenarioSiAbsent(); // utilise la version non statique
    }

    /** Méthode statique générique pour créer le fichier dans un chemin donné */
    public static void creerFichierScenarioSiAbsent(String cheminStockage) {
        Path cheminFichier = Paths.get(cheminStockage, NOM_FICHIER);
        try {
            Files.createDirectories(cheminFichier.getParent());
            if (!Files.exists(cheminFichier)) {
                try (BufferedWriter writer = Files.newBufferedWriter(cheminFichier)) {
                    writer.write("{}");
                }
                System.out.println("scenarioFC.json créé à " + cheminFichier.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erreur création scenarioFC.json : " + e.getMessage());
        }
    }

    /** Version non statique pour créer le fichier à partir de l'instance */
    private void creerFichierScenarioSiAbsent() {
        creerFichierScenarioSiAbsent(this.cheminFichier.getParent().toString());
    }

    /** Permet de fusionner des scénarios dans un fichier */
    public void fusionnerScenariosRecus(Map<String, List<Integer>> recus) {
        Map<String, List<Integer>> existants = chargerScenarios();

        boolean modifie = false;

        for (Map.Entry<String, List<Integer>> entry : recus.entrySet()) {
            String texte = entry.getKey();
            List<Integer> pictos = entry.getValue();

            if (!validerPictos(pictos)) continue;
            if (doublonPictos(existants, pictos)) continue;

            existants.put(texte, pictos);
            modifie = true;
        }

        if (modifie) {
            sauvegarderScenarios(existants);
            System.out.println("Scénarios fusionnés et sauvegardés.");
        } else {
            System.out.println("Aucun nouveau scénario à fusionner.");
        }
    }

    /** Charge tous les scénarios depuis le fichier */
    public Map<String, List<Integer>> chargerScenarios() {
        if (!Files.exists(cheminFichier)) return new HashMap<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(cheminFichier)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return gson.fromJson(sb.toString(), TYPE_SCENARIOS);
        } catch (IOException e) {
            System.err.println("Erreur lecture scenarioFC.json : " + e.getMessage());
            return new HashMap<>();
        }
    }

    /** Sauvegarde tous les scénarios dans le fichier */
    private void sauvegarderScenarios(Map<String, List<Integer>> scenarios) {
        try (BufferedWriter writer = Files.newBufferedWriter(cheminFichier)) {
            writer.write(gson.toJson(scenarios));
        } catch (IOException e) {
            System.err.println("Erreur écriture scenarioFC.json : " + e.getMessage());
        }
    }

    /**
     * Ajoute un scénario si :
     * - 5 pictogrammes valides (0 à 26 inclus).
     * - Non présent dans le fichier existant
     */
    public boolean ajouterScenario(String texte, List<Integer> pictos) {
        if (!validerPictos(pictos)) {
            System.out.println("Un scénario doit contenir exactement 5 pictogrammes valides (0 à 26).");
            return false;
        }

        Map<String, List<Integer>> scenarios = chargerScenarios();
        if (doublonPictos(scenarios, pictos)) {
            System.out.println("Ce scénario existe déjà !");
            return false;
        }

        scenarios.put(texte, pictos);
        sauvegarderScenarios(scenarios);
        System.out.println("Scénario ajouté avec succès !");
        return true;
    }

    /** Vérifie la validité des pictogrammes */
    private boolean validerPictos(List<Integer> pictos) {
        if (pictos.size() != 5) return false;
        for (int p : pictos) if (p < 0 || p > 26) return false;
        return true;
    }

    /** Vérifie les doublons de pictogrammes */
    private boolean doublonPictos(Map<String, List<Integer>> map, List<Integer> pictos) {
        for (List<Integer> existing : map.values()) {
            if (existing.equals(pictos)) return true;
        }
        return false;
    }
}
