/*
 * DiffieHellman.java               05 Nov. 2025
 * IUT de Rodez, pas de copyright
 */

package iut.ga1.resquizz.modeles;

import java.util.HashSet;
import java.util.Random;

/**
 * Implémentation de l’algorithme d’échange de clé de Diffie-Hellman.
 * <p>
 * Cette classe permet à deux parties de générer un secret commun partagé
 * sans le transmettre directement. L’échange repose sur un nombre premier {@code p}
 * et un générateur {@code g} de ce nombre premier.
 * </p>
 *
 * <p>
 * Exemple d’utilisation :
 * <pre>{@code
 * DiffieHellman alice = new DiffieHellman();
 * DiffieHellman bob = new DiffieHellman(alice.getPremier(), alice.getGenerateur());
 *
 * long aPub = alice.getNombreCree();
 * long bPub = bob.getNombreCree();
 *
 * alice.setNombreRecu(bPub);
 * bob.setNombreRecu(aPub);
 *
 * System.out.println(alice.getNombreSecret() == bob.getNombreSecret()); // true
 * }</pre>
 * </p>
 *
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class DiffieHellman {

    /** Le nombre premier utilisé dans l’échange. */
    private final long p;

    /** Le générateur du nombre premier. */
    private final long g;

    /** L’exposant secret privé de l’utilisateur. */
    private long a;

    /** La clé publique calculée (g^a mod p). */
    private long ga;

    /** La clé publique reçue de l’autre partie. */
    private long gb;

    /** Le secret partagé calculé. */
    private long nombreSecret;

    /**
     * Crée un objet Diffie-Hellman avec un nombre premier et un générateur donnés.
     *
     * @param p un nombre premier (de préférence grand)
     * @param g un générateur de {@code p}
     * @throws IllegalArgumentException si {@code p} n’est pas premier
     *                                  ou si {@code g} n’est pas un générateur de {@code p}
     */
    public DiffieHellman(long p, long g) {
        if (!isPremier(p) || !isGenerateur(g, p)) {
            throw new IllegalArgumentException("Arguments invalides");
        }
        this.p = p;
        this.g = g;
    }

    /**
     * Crée un objet Diffie-Hellman avec des valeurs générées automatiquement.
     * <p>
     * Le nombre premier {@code p} est compris entre 1000 et 1999,
     * et le générateur {@code g} est choisi aléatoirement parmi les générateurs valides.
     * </p>
     */
    public DiffieHellman() {
        super();
        p = DiffieHellman.genererNombrePremier();
        g = DiffieHellman.genererGenerateur(p);
    }

    /**
     * Génère un générateur pour le nombre premier {@code p}.
     *
     * @param p un nombre premier
     * @return un générateur valide pour {@code p}
     * @throws IllegalArgumentException si {@code p} n’est pas premier
     */
    public static long genererGenerateur(long p) {
        if (!isPremier(p)) {
            throw new IllegalArgumentException("Le nombre mis en argument n'est pas premier");
        }
        Random r = new Random();
        long g;
        do {
            g = Math.abs(r.nextLong() % (p - 2)) + 1; // éviter 0
        } while (!isGenerateur(g, p));

        return g;
    }

    /**
     * Vérifie si {@code g} est un générateur du groupe multiplicatif modulo {@code p}.
     *
     * @param g le candidat générateur
     * @param p le nombre premier associé
     * @return {@code true} si {@code g} est un générateur de {@code p}, {@code false} sinon
     * @throws IllegalArgumentException si {@code p} n’est pas premier
     */
    public static boolean isGenerateur(long g, long p) {
        if (!isPremier(p)) {
            throw new IllegalArgumentException("Le nombre mis en argument n'est pas premier");
        }
        HashSet<Long> liste = new HashSet<>();
        long compteur = 1;
        long valeur = 1;
        while (compteur < p) {
            valeur = (valeur * g) % p;
            if (!liste.add(valeur)) {
                return false;
            }
            compteur++;
        }
        return true;
    }

    /**
     * Effectue une exponentiation modulaire rapide.
     * <p>
     * Calcule {@code a^exposant mod modulo} efficacement en utilisant la méthode
     * d’exponentiation binaire.
     * </p>
     *
     * @param a        la base
     * @param exposant l’exposant
     * @param modulo   le modulo
     * @return le résultat de {@code a^exposant mod modulo}
     */
    private long calculRapide(long a, long exposant, long modulo) {
        long resultat = 1;
        while (exposant > 0) {
            if (exposant % 2 == 1) {
                resultat = (resultat * a) % modulo;
            }
            a = (a * a) % modulo;
            exposant = exposant / 2;
        }
        return resultat;
    }

    /**
     * Génère un nombre premier positif aléatoire compris entre 1000 et 1999.
     *
     * @return un nombre premier aléatoire
     */
    public static long genererNombrePremier() {
        Random rand = new Random();
        long num = 1000 + rand.nextInt(1000);

        while (!isPremier(num)) {
            num++;
        }
        return num;
    }

    /**
     * Vérifie si un nombre est premier.
     *
     * @param num le nombre à vérifier
     * @return {@code true} si {@code num} est premier, {@code false} sinon
     */
    public static boolean isPremier(long num) {
        if (num < 2) return false;
        if (num % 2 == 0) return num == 2;
        long sqrt = (long) Math.sqrt(num);
        for (long i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }

    /**
     * Renvoie le nombre premier utilisé par l’algorithme.
     *
     * @return le nombre premier {@code p}
     */
    public long getPremier() {
        return p;
    }

    /**
     * Renvoie le générateur utilisé par l’algorithme.
     *
     * @return le générateur {@code g}
     */
    public long getGenerateur() {
        return g;
    }

    /**
     * Génère la clé publique de l'utilisateur (g^a mod p).
     * <p>
     * Si {@code a} n’a pas encore été généré, un exposant aléatoire est créé.
     * </p>
     *
     * @return la clé publique {@code g^a mod p}
     */
    public long getNombreCree() {
        if (a == 0) {
            a = (long) ((double) (p - 1) * Math.random()) + 1; // éviter 0
            ga = calculRapide(g, a, p);
        }
        return ga;
    }

    /**
     * Reçoit la clé publique de l’autre partie et calcule le secret partagé.
     *
     * @param gb la clé publique reçue de l’autre partie
     */
    public void setNombreRecu(long gb) {
        this.gb = gb % p;
        nombreSecret = calculRapide(this.gb, this.a, p);
        getNombreCree();
    }

    /**
     * Renvoie la clé publique reçue de l’autre participant.
     *
     * @return la clé publique {@code gb}
     */
    public long getNombreRecu() {
        return gb;
    }

    /**
     * Renvoie le nombre secret partagé calculé.
     * Renvoie 0 s'il n'a pas été setNombreRecu avant
     * @return le secret partagé {@code nombreSecret}
     */
    public long getNombreSecret() {
        return nombreSecret;
    }
}
