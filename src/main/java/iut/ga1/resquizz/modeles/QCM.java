/*
 * QCM.java                                                        6 novembre. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Classe qui s'occupe du mode de jeu "QCM"
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class QCM {

    /** Pictogrammes actuellement placés dans la grille */
    private final ArrayList<Pictogramme> listePlaces;

    /** Pictogrammes proposés (non encore placés) */
    private final ArrayList<Pictogramme> listePropositions;

    /** Instance du singleton GrilleSST */
    private final GrilleSST grilleSST;

    /** Liste des pictogrammes utilisés pour cette partie (10 aléatoires) */
    private List<Pictogramme> pictogrammesJeu;

    /** Score du joueur */
    private int points;

    /** Constructeur de QCM */
    public QCM() {
        this.listePlaces = new ArrayList<>();
        this.listePropositions = new ArrayList<>();

        this.grilleSST = GrilleSST.getGrilleSST();
        remplirGrille();
    }

    /** Remplit la liste des propositions à partir du Singleton GrilleSST */
    public void remplirGrille() {
        listePropositions.clear();
        listePropositions.addAll(Arrays.asList(grilleSST.getPictogrammes()));
    }

    /**
     * Méthode qui calcule les points
     */
    public void calculPoints() {
        int compteur = 0;

        int taille;
        if (listePlaces != null && !listePlaces.isEmpty()) {
            taille = listePlaces.size();
        } else if (pictogrammesJeu != null) {
            taille = pictogrammesJeu.size();
        } else {
            taille = grilleSST.getPictogrammes().length;
        }

        for (int i = 0; i < taille; i++) {
            Pictogramme p = (listePlaces != null && i < listePlaces.size()) ? listePlaces.get(i) : null;
            if (p != null && estBonneReponse(i, p)) {
                compteur+= 100;
            } else if (p != null && !estBonneReponse(i, p)) {
                compteur-=50;
            }
        }
        points = compteur;
    }

    /** Vérifie si toute la grille est correcte (toutes les réponses attendues placées) */
    public boolean estBonneReponse() {
        int nbAttendus;
        if (pictogrammesJeu != null) {
            nbAttendus = pictogrammesJeu.size();
        } else {
            nbAttendus = grilleSST.getPictogrammes().length;
        }
        return points == nbAttendus*100;
    }

    /**
     * Vérifie si la case à l’indice donné est correcte en comparant
     * le pictogramme placé à celui attendu pour la partie actuelle.
     *
     * @param index index de la case
     * @param pictogrammePlace pictogramme placé par le joueur
     * @return true si correct
     */
    public boolean estBonneReponse(int index, Pictogramme pictogrammePlace) {
        if (pictogrammePlace == null) return false;

        List<Pictogramme> selectionJeu = getPictogrammesJeu();
        if (selectionJeu != null && index >= 0 && index < selectionJeu.size()) {
            Pictogramme attendu = selectionJeu.get(index);
            if (attendu == null) return false;

            return Objects.equals(attendu.getIdentifiant(), pictogrammePlace.getIdentifiant());
        }

        return false;
    }

    /**
     * Getter des points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter des pictogrammes placés
     * @return la liste des pictogrammes placés
     */
    public ArrayList<Pictogramme> getPlaces() {
        return listePlaces;
    }

    /**
     * Getter des pictogrammes proposés
     * @return la liste des pictogrammes proposés
     */
    public ArrayList<Pictogramme> getPropositions() {
        return listePropositions;
    }

    /**
     * Getter des pictogrammes joués
     * @return l
     */
    public List<Pictogramme> getPictogrammesJeu() {
        return pictogrammesJeu;
    }

    /**
     * Setter des pictogrammes joués
     * @param pictogrammesJeu Liste de pictogrammes
     */
    public void setPictogrammesJeu(List<Pictogramme> pictogrammesJeu) {
        this.pictogrammesJeu = pictogrammesJeu;
    }

}