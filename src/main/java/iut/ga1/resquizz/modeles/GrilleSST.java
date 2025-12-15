/*
 * GrilleSST.java               16 Oct. 2025
 * IUT de Rodez, pas de copyright
 */

package iut.ga1.resquizz.modeles;

import iut.ga1.resquizz.util.Pictogramme;

/**
 * Singleton contenant l'ensemble des pictogrammes
 * d'une grille de SST (sauveteur secouriste du travail)
 *
 * @author Gabriel Le Goff
 * @author Noé Rebourg
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Esteban Roveri
 */
public class GrilleSST {

    /** Instance unique du singleton */
    private static GrilleSST grilleSST;

    /** Tableau de pictogrammes de la grille */
    private final Pictogramme[] listePictogrammes;

    /** Constructeur privé pour empêcher l'instanciation directe (pattern Singleton).*/
    private GrilleSST() {
        super();
        this.listePictogrammes = initialiserPictogrammes();
    }

    /**
     * Retourne l'unique instance de la grille SST (singleton, lazy).
     * @return l'instance unique de {@code GrilleSST}
     */
    public static synchronized GrilleSST getGrilleSST() {
        if (grilleSST == null) {
            grilleSST = new GrilleSST();
        }
        return grilleSST;
    }

    /**
     * Initialise la liste complète des pictogrammes de la grille SST.
     * @return le tableau de pictogrammes initialisé
     */
    private Pictogramme[] initialiserPictogrammes() {

        Pictogramme p0 = new Pictogramme(0,"Protéger les lieux de l'accident","Si possible, faire cesser la source du danger, si ce n’est pas possible, empêcher la survenue d’un sur accident, si cela n’est toujours pas possible, évacuer la ou les victimes dans le dernier cas","images/Proteger_lieu.png");
        Pictogramme p1 = new Pictogramme(1,"Examiner la victime","S’approcher de la victime et l’examiner pour connaître son état de santé, afin de prendre les mesures adaptées, et de pouvoir ainsi le communiquer aux services de secours si nécessaire.","images/Examiner_victime.png");
        Pictogramme p2 = new Pictogramme(2,"Appel secours","Avant de secourir la victime, appeler ou idéalement faire appeler par une personne tierce un numéro d’urgence (15, 18, 112). Si nécessaire, lancer l’appel et poser son téléphone au sol, en haut-parleur pour commencer à secourir la victime le plus rapidement.","images/Appel_secours.png");
        Pictogramme p3 = new Pictogramme(3,"Numéros d'urgence","Les secours pourront vous aider à affiner l’examen, et vous guider à distance pour exécuter les gestes de premier secours en attendant l’arrivée des secours.","images/Numero_urgence.png");
        Pictogramme p4 = new Pictogramme(4,"Victime inconsciente","Dans le cas où la victime ne répondrait à aucune stimulation, il faut vérifier si la victime respire","images/Victime_inconsciente.png");
        Pictogramme p5 = new Pictogramme(5,"Parler","Après avoir essayé de parler à la victime, en lui tenant les deux mains, elle a répondu en parlant, hochant de la tête, en serrant les mains, etc.","images/Parler.png");
        Pictogramme p6 = new Pictogramme(6,"Danger mécanique","Dangers mécaniques (outils, chutes, accident de la circulation)","images/Danger_mecanique.png");
        Pictogramme p7 = new Pictogramme(7,"Danger électrique","Dangers électriques (installation/appareil électrique dangereux, etc.)","images/Danger_electrique.png");
        Pictogramme p8 = new Pictogramme(8,"Danger thermique","Dangers chimiques (produits toxiques, nocifs, acides, irritants, etc.)","images/Danger_thermique.png");
        Pictogramme p9 = new Pictogramme(9,"Atmosphere toxique","atmosphère suffocante (fumée d’incendie, monoxyde de carbone, etc.)","images/Atmosphere_toxique.png");
        Pictogramme p10 = new Pictogramme(10,"Saignement important","La victime saigne abondamment et cela nécessite une intervention immédiate, sans se préoccuper de l’état de conscience de la victime.","images/Saignement_important.png");
        Pictogramme p11 = new Pictogramme(11,"Étouffement","La victime n’est pas capable d’émettre de son avec sa voix, et aucun air ne sort de bouche.","images/Etouffement.png");
        Pictogramme p12 = new Pictogramme(12,"Victime ne respirant pas","La victime ne respire pas, l’air ne sort pas de sa bouche, son ventre est immobile.","images/Victime_ne_respire_pas.png");
        Pictogramme p13 = new Pictogramme(13,"Victime qui respire","La victime respire, de l’air sort de sa bouche, son ventre est mobile.","images/Victime_respire.png");
        Pictogramme p14 = new Pictogramme(14,"Victime se plaint de malaise","La victime est confuse, ses propos n’ont pas de sens, elle ne sait pas où elle se trouve, qui elle est, ou ne réussit pas à répondre à des questions simples.","images/Victime_se_plaint_de_malaise.png");
        Pictogramme p15 = new Pictogramme(15,"Victime se plaint de brûlure","Qu’il s’agisse d’une brûlure d’origine thermique, ou chimique, la victime devrait être capable de vous indiquer l’origine de la blessure et la zone à soigner.","images/Victime_se_plaint_de_brulure.png");
        Pictogramme p16 = new Pictogramme(16,"Blessure visible","Qu’il s’agisse d’une fracture visible ou non, ou d’un saignement non abondant, la victime devrait être capable de vous indiquer l’origine de la blessure et la zone à soigner.","images/Blessure_visible.png");
        Pictogramme p17 = new Pictogramme(17,"Supprimer le danger identifié","Faire cesser le danger (ex : couper le courant si électrisation/électrocution)","images/Supprimer_le_danger_identifie.png");
        Pictogramme p18 = new Pictogramme(18,"Isoler le danger","Baliser le danger (ex : poser un triangle si accident de la circulation)","images/Isoler_le_danger.png");
        Pictogramme p19 = new Pictogramme(19,"Evacuation d'urgence","Éloigner la victime du danger (ex : dégagement d’urgence d’une victime inconsciente dans une atmosphère enfumée)","images/Evacuation_urgence.png");
        Pictogramme p20 = new Pictogramme(20,"Compression du saignement","Faire cesser le saignement : garrot ou garrot improvisé, bande compressive, comprimer la plaie. La faire allonger et la couvrir.","images/Compression_saignement.png");
        Pictogramme p21 = new Pictogramme(21,"Manoeuvre de Heimlich","Pratiquer les gestes de désobstruction (répéter 5 claques vigoureuses dans le dos et méthode de Heimlich)","images/Manoeuvre_de_Heimlich.png");
        Pictogramme p22 = new Pictogramme(22,"Massage cardiaque","Pratiquer un massage cardiaque avec insufflations jusqu’à l’arrivée des secours.","images/Massage_cardiaque.png");
        Pictogramme p23 = new Pictogramme(23,"Position latérale de sécurité","Mettre la victime en PLS, la couvrir et attendre les secours.","images/Position_laterale_de_securite.png");
        Pictogramme p24 = new Pictogramme(24,"Détection AVC","La faire s’asseoir ou s’allonger et attendre les secours.","images/Detection_AVC.png");
        Pictogramme p25 = new Pictogramme(25,"Éviter aggravation brûlure","Passer la zone brûlée sous l’eau tiède pendant 20 minutes.","images/Eviter_aggravation_brulure.png");
        Pictogramme p26 = new Pictogramme(26,"Éviter le traumatisme","La couvrir, appliquer de la glace, immobiliser les membres fracturés.","images/Eviter_le_traumatisme.png");

        return new Pictogramme[]{
                p0, p1, p2, p3, p4, p5, p6, p7, p8, p9,
                p10, p11, p12, p13, p14, p15, p16, p17, p18, p19,
                p20, p21, p22, p23, p24, p25, p26
        };
    }

    /** Retourne une copie du tableau de pictogrammes de la grille.
     * @return un tableau contenant une copie des pictogrammes de la grille
     */
    public Pictogramme[] getPictogrammes() {
        return listePictogrammes.clone();
    }
}