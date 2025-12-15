/*
 * LectureJson.java                                    24 Oct. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Classe générique permettant de lire un fichier JSON et de le convertir
 * en une HashMap.
 * Cette classe utilise {@code Gson}
 */
public class LectureJson<X, Y> {

    /** Instance Gson pour la désérialisation JSON. */
    private final Gson gson;

    /**
     * Constructeur par défaut.
     */
    public LectureJson() {
        super();
        this.gson = new Gson();
    }

    /**
     * Lit un fichier JSON et le convertit en {@link HashMap}.
     *
     * @param cheminFichier chemin complet ou relatif du fichier JSON à lire
     * @param type type générique exact à désérialiser (ex. HashMap<String, int[]>)
     * @return un {@link HashMap} contenant les paires clé/valeur du JSON
     * @throws RuntimeException si le fichier est introuvable ou invalide
     */
    public HashMap<X, Y> lireFichier(String cheminFichier, Type type) {
        try (FileReader fr = new FileReader(cheminFichier)) {
            return gson.fromJson(fr, type);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON : " + e.getMessage(), e);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Le fichier JSON est invalide : " + e.getMessage(), e);
        }
    }

    /**
     * Lit un fichier JSON situé dans les ressources du projet
     * <p>
     * Cette méthode est indispensable pour que l'application fonctionne une fois compilée,
     * car elle ne dépend pas des chemins de fichiers du disque dur.
     * </p>
     *
     * @param cheminRessource Le chemin relatif à la racine des ressources
     * @param type Le type Java complexe (via TypeToken) dans lequel convertir le JSON.
     * @param <T> Le type de retour attendu (déduit automatiquement).
     * @return L'objet Java contenant les données du JSON.
     * @throws RuntimeException Si le fichier est introuvable ou le JSON invalide.
     */
    public <T> T lireFichierRessource(String cheminRessource, Type type) {
        // On cherche le fichier dans le dossier des classes compilées
        InputStream is = getClass().getResourceAsStream(cheminRessource);

        if (is == null) {
            throw new RuntimeException("Ressource introuvable : " + cheminRessource);
        }

        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException | JsonSyntaxException e) {
            throw new RuntimeException("Erreur lecture JSON ressource : " + e.getMessage(), e);
        }
    }

    /**
     * Lit une chaîne JSON (au lieu d’un fichier).
     *
     * @param contenuJson texte JSON brut
     * @return un {@link HashMap} représentant le JSON
     */
    public HashMap<X, Y> lireDepuisTexte(String contenuJson) {
        try {
            return gson.fromJson(contenuJson, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Le contenu JSON est invalide : " + e.getMessage(), e);
        }
    }
}
