/*
 * RevisionGrille.java               17 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe qui s'occupe du mode de jeu "Révision de la grille"
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class RevisionGrille {

    /** Pictogrammes actuellement placés dans la grille */
    private final ArrayList<Pictogramme> listePlaces;

    /** Pictogrammes proposés (non encore placés) */
    private final ArrayList<Pictogramme> listePropositions;

    /** Instance du singleton GrilleSST */
    private final GrilleSST grilleSST;

    private int points;

    public RevisionGrille() {
        this.grilleSST = GrilleSST.getGrilleSST();
        // Stocke la liste complète des pictogrammes
        this.listePlaces = new ArrayList<>();
        this.listePropositions = new ArrayList<>();
        remplirGrille();
        Collections.shuffle(listePropositions);
    }

    /**
     * Méthode qui calcule les points
     */
    public void calculPoints() {
        int resultat = 0;
        for (int i = 0; i < listePlaces.size(); i++) {
            Pictogramme pictogramme = listePlaces.get(i);
            if (pictogramme != null) {
                if (pictogramme.getIdentifiant() == i) {
                    resultat += 100;
                } else {
                    resultat -= 50;
                }
            }
        }
        points = resultat;
    }

    /**
     * @return les points actuels
     */
    public int getPoints() {
        return points;
    }

    /**
     * Vérifie si toute la grille est correcte.
     * @return true si toutes les réponses sont justes.
     */
    public boolean estBonneReponse() {
        int totalCorrect = 0;
        for (int i = 0; i < listePlaces.size(); i++) {
            Pictogramme pic = listePlaces.get(i);
            if (pic != null && pic.getIdentifiant() == i) {
                totalCorrect++;
            }
        }
        return totalCorrect == listePlaces.size();
    }

    /**
     * Vérifie si la case à l’indice donné est correcte.
     * (Utilisé pour le feedback visuel dans le contrôleur)
     */
    public boolean estBonneReponse(int index) {
        if (index < 0 || index >= listePlaces.size()) return false;
        Pictogramme pictogramme = listePlaces.get(index);
        return pictogramme != null && pictogramme.getIdentifiant() == index;
    }

    /**
     * Place un pictogramme à un emplacement spécifié dans listePlaces,
     * et le retire de listePropositions.
     *
     * @param pictogramme pictogramme à placer
     * @param place       l'indice où placer le pictogramme
     */
    public void placerPictogramme(Pictogramme pictogramme, int place) {
        if (place < 0 || place >= listePlaces.size()) {
            System.err.println("Index hors limites : " + place);
            return;
        }

        int ancienIndex = listePlaces.indexOf(pictogramme);
        if (ancienIndex != -1) {
            listePlaces.set(ancienIndex, null);
        }
        Pictogramme ancienPic = listePlaces.get(place);
        if (ancienPic != null && !listePropositions.contains(ancienPic)) {
            listePropositions.add(ancienPic);
        }
        listePropositions.remove(pictogramme);
        listePlaces.set(place, pictogramme);
    }

    /**
     * Retire un pictogramme de la grille (listePlaces) s'il y est présent,
     * et le remet dans la liste des propositions s'il n'y est pas déjà.
     */
    public void remettreDansPropositions(Pictogramme pictogramme) {

        int id = listePlaces.indexOf(pictogramme);
        if (id != -1) {
            listePlaces.set(id, null);
        }

        if (!listePropositions.contains(pictogramme)) {
            listePropositions.add(pictogramme);
        }
    }

    /**
     * Remplit la liste de propositions à partir du Singleton GrilleSST et
     * ajoute un null dans listePlaces pour chacun d'entre eux. Les deux listes
     * sont d'abord vidées.
     */
    public void remplirGrille() {
        listePlaces.clear();
        listePropositions.clear();
        for (Pictogramme p : grilleSST.getPictogrammes()) {
            listePlaces.add(null);
            listePropositions.add(p);
        }
        Collections.shuffle(listePropositions);
    }

    /**
     * Retourne la liste des pictogrammes placés dans la grille
     */
    public ArrayList<Pictogramme> getPlaces() {
        return listePlaces;
    }

    /**
     * Retourne la liste des pictogrammes proposés
     */
    public ArrayList<Pictogramme> getPropositions() {
        return listePropositions;
    }
}
