/*
 * GenerateurClef.java              7 Nov. 2025
 * IUT de Rodez, pas de copyright
 */

package iut.ga1.resquizz.modeles;

/**
 * La classe GenerateurClef permet de générer une clé grâce à un entier secret
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class GenerateurClef {

    /** La clé générée sous forme de chaîne de caractères. */
    private String clef;

    /**
     * Constructeur
     * Génère la clé à partir de l'entier secret fourni.
     * @param entierSecret la valeur numérique servant de base pour la génération de la clé.
     */
    public GenerateurClef(long entierSecret) {
        super();
        genererClef(entierSecret);
    }

    /**
     * Génère une clé pseudo-aléatoire à partir d'un entier secret en appliquant un
     * algorithme déterministe basé sur des opérations modulo, une extraction cyclique
     * des chiffres du nombre d'entrée et une transformation en caractères Unicode.
     *
     * <p><strong>Principe général :</strong><br>
     * À partir d’un entier secret, la méthode construit progressivement une chaîne
     * de caractères. Le nombre total de caractères générés correspond au reste de
     * la division de l’entier secret par 1000. À chaque itération, un nouvel entier
     * intermédiaire est calculé puis converti en caractère, avec des mécanismes de
     * correction pour garantir que le caractère obtenu est imprimable.
     *
     * <p><strong>Détails de l’algorithme :</strong>
     * <ul>
     *   <li>Initialise deux accumulateurs :
     *       <ul>
     *         <li><code>valeurTotale</code> : valeur numérique cumulée utilisée pour dériver un caractère.</li>
     *         <li><code>reste</code> : copie cyclique de l’entier secret utilisée pour extraire des chiffres.</li>
     *       </ul>
     *   </li>
     *
     *   <li>Détermine le nombre d'itérations à effectuer via <code>entierSecret % 1000</code>.
     *       Ce nombre détermine la longueur finale de la clé.</li>
     *
     *   <li>À chaque itération :
     *       <ul>
     *         <li>Ajoute à <code>valeurTotale</code> la somme de l'entier secret et du dernier chiffre de
     *         <code>reste</code>, puis applique un modulo 65536 pour rester dans la plage Unicode valide.</li>
     *
     *         <li>Convertit <code>valeurTotale</code> en caractère.</li>
     *
     *         <li>Si le caractère obtenu est :
     *             <ul>
     *               <li>le caractère nul (<code>'\0'</code>), ou</li>
     *               <li>un caractère de la plage des Surrogates UTF-16 (de 0xD800 à 0xDFFF),</li>
     *             </ul>
     *             alors il est remplacé par un caractère imprimable ASCII calculé via
     *             <code>33 + (valeurTotale % 94)</code>, afin de garantir une sortie sûre et lisible.
     *         </li>
     *
     *         <li>Ajoute le caractère final au tampon de construction de la clé.</li>
     *
     *         <li>Met à jour <code>reste</code> en le divisant par 10 afin de parcourir ses chiffres.
     *         Si <code>reste</code> tombe à 0, il est réinitialisé à <code>entierSecret</code> pour boucler
     *         sur les chiffres du nombre.</li>
     *       </ul>
     *   </li>
     *
     *   <li>À la fin des itérations, la chaîne de caractères construite est affectée
     *   à la variable d'instance <code>clef</code>.</li>
     * </ul>
     *
     * @param entierSecret l’entier utilisé comme base pour la génération de la clé.
     */
    private void genererClef(long entierSecret) {
        StringBuilder sb = new StringBuilder();
        long valeurTotale = 0;
        long reste = entierSecret;

        for (long incrementNegatif = entierSecret % 1000; incrementNegatif > 0; incrementNegatif--) {
            valeurTotale = (valeurTotale + entierSecret + (reste % 10)) % 65536;
            char c = (char) valeurTotale;

            if (c == 0 || (c >= 0xD800 && c <= 0xDFFF)) {
                c = (char) (33 + (valeurTotale % 94));
            }

            sb.append(c);

            reste /= 10;
            if (reste == 0) {
                reste = entierSecret;
            }
        }
        this.clef = sb.toString();
    }

    /**
     * Getter de la clé générée.
     * @return la clé générée
     */
    public String getClef() {
        return clef;
    }

    /**
     * Setter de la clé
     * @param entierSecret le nouvel entier secret servant à générer une nouvelle clé.
     */
    public void setEntierSecret(long entierSecret) {
        genererClef(entierSecret);
    }
}