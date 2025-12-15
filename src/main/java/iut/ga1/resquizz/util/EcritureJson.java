/*
 * EcritureJson.java                   23 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Classe générique permettant d’écrire un fichier JSON à partir d’un conteneur
 * de paires clé/valeur. Utilise la bibliothèque {@code Gson} pour garantir
 * un format JSON valide et lisible.
 *
 * @author Gabriel Le Goff
 * @author Gabriel Robache
 * @author Tom Killing
 * @author Noe Rebourg
 * @author Esteban Roveri
 */
public class EcritureJson<X, Y> {

    /** Conteneur interne des paires clé/valeur. */
    private final HashMap<X, Y> conteneur;

    /** Instance Gson pour la sérialisation JSON. */
    private final Gson gson;

    /**
     * Constructeur par défaut : crée un conteneur vide et un {@link Gson} formaté joliment.
     */
    public EcritureJson() {
        super();
        this.conteneur = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Ajoute un élément (clé, valeur) au conteneur si la clé n’existe pas déjà.
     *
     * @param key   la clé à insérer
     * @param value la valeur associée
     * @return {@code true} si l’ajout a réussi, {@code false} si la clé existe déjà
     */
    public boolean ajouterElement(X key, Y value) {
        if (!conteneur.containsKey(key)) {
            conteneur.put(key, value);
            return true;
        }
        return false;
    }

    /**
     * Génère une chaîne JSON lisible représentant le contenu du conteneur.
     *
     * @return une chaîne JSON bien formée
     */
    public String toJson() {
        return gson.toJson(conteneur);
    }

    /**
     * Écrit le contenu du conteneur dans un fichier JSON.
     *
     * @param cheminFichier chemin complet ou relatif du fichier à écrire
     * @throws RuntimeException si une erreur d’écriture survient
     */
    public void ecrireFichier(String cheminFichier) {
        try (FileWriter fw = new FileWriter(cheminFichier)) {
            gson.toJson(conteneur, fw);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

}
