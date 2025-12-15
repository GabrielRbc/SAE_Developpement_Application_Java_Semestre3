/*
 * Chronometre.java               23 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

/**
 * Classe qui représente un chronomètre et permet la
 * gestion du temps dans les differents modes de jeu.
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class Chronometre {
    private final LongProperty TEMPS_ECOULE;
    private boolean enCours;
    Timeline timeline;

    /**
     * Constructeur de la classe Chronometre.
     * Initialise le chronomètre à zéro et non démarré.
     */
    public Chronometre() {
        this.TEMPS_ECOULE = new SimpleLongProperty();
        this.enCours = false;
        this.timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    // Incrémente le temps écoulé de 1000ms
                    TEMPS_ECOULE.set(TEMPS_ECOULE.get() + 1000);
                })
        );
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Démarre le chronomètre.
     */
    public void demarrerChrono() {
        if (!enCours) {
            timeline.play();
            this.enCours = true;
        }
    }

    /**
     * Arrête le chronomètre.
     */
    public void arreterChrono() {
        if (enCours) {
            timeline.pause();
            this.enCours = false;
        }
    }

    /** Réinitialise le chronomètre à zéro. */
    public void reinitialiserChrono() {
        this.arreterChrono();
        this.TEMPS_ECOULE.set(0);
    }

    /**
     * Obtient la propriété du temps écoulé.
     *
     * @return La propriété du temps écoulé en millisecondes.
     */
    public LongProperty getTempsEcouleProperty() {
        return TEMPS_ECOULE;
    }

    /**
     * Obtient le temps écoulé en millisecondes.
     *
     * @return Le temps écoulé en millisecondes.
     */
    public long getTempsEcoule() {
        return TEMPS_ECOULE.get();
    }
}