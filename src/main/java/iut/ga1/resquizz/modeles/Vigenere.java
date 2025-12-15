/*
 * Vigenere.java                   7 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Classe Vigenere permettant de chiffrer et déchiffrer des messages
 * selon le chiffrement de Vigenère.
 * <p>
 * Cette implémentation fonctionne sur tous les caractères Unicode
 * et utilise une clé (clef) pour effectuer le chiffrement et le déchiffrement.
 * </p>
 *
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class Vigenere {

    /** La clé utilisée pour le chiffrement et le déchiffrement. */
    private String clef;

    /**
     * Constructeur par défaut.
     * Initialise un objet Vigenere sans clé.
     */
    public Vigenere() {}

    /**
     * Constructeur avec clé
     * @param clef La clé utilisée pour le chiffrement et déchiffrement.
     */
    public Vigenere(String clef) {
        this.clef = clef;
    }

    /**
     * Définit la clé utilisée pour le chiffrement et le déchiffrement.
     * @param clef La nouvelle clé.
     */
    public void setClef(String clef) {
        this.clef = clef;
    }

    /**
     * Retourne la clé actuelle.
     * @return La clé utilisée pour le chiffrement et déchiffrement.
     */
    public String getClef() {
        return clef;
    }

    /**
     * Chiffrement sécurisé sur octets
     */
    public String chiffrer(String clair) {
        byte[] data = clair.getBytes(StandardCharsets.UTF_8);
        byte[] key = clef.getBytes(StandardCharsets.UTF_8);
        byte[] out = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) ((data[i] + key[i % key.length]) & 0xFF);
        }

        return Base64.getEncoder().encodeToString(out);
    }

    /**
     * Déchiffrement sécurisé sur octets
     */
    public String dechiffrer(String chiffreBase64) {
        byte[] data = Base64.getDecoder().decode(chiffreBase64);
        byte[] key = clef.getBytes(StandardCharsets.UTF_8);
        byte[] out = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) ((data[i] - key[i % key.length]) & 0xFF);
        }

        return new String(out, StandardCharsets.UTF_8);
    }

    /**
     * Méthode principale de test.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        String message = "1234567890°+&é\"'(-è_çà)='azertyuiop^$*qsdfghjklmùwxcvbn,;:!AZERTYUIOP¨£µQSDFGHJKLM%WXCVBN?./§";
        String clef = "Voici la clef:,;:;,é:";

        Vigenere v = new Vigenere(clef);
        System.out.println(v.clef);
        System.out.println("message chiffre : " + v.chiffrer(message));
        System.out.println("message déchiffré : " + v.dechiffrer(v.chiffrer(message)));
        System.out.println(v.dechiffrer(v.chiffrer(message)).equals(message));
    }
}
