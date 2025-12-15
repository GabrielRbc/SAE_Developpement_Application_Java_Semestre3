/*
 * VigenereTest.java              26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la classe Vigenere.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class VigenereTest {

    private Vigenere vigenere;

    @BeforeEach
    void setUp() {
        vigenere = new Vigenere("CLEF");
    }

    @Test
    void testConstructeurAvecClef() {
        assertEquals("CLEF", vigenere.getClef());
    }

    @Test
    void testSetClef() {
        vigenere.setClef("ABC");
        assertEquals("ABC", vigenere.getClef());
    }

    @Test
    void testChiffrerDechiffrer() {
        String message = "BONJOUR";
        String clef = "CLE";
        vigenere.setClef(clef);

        String chiffre = vigenere.chiffrer(message);
        String dechiffre = vigenere.dechiffrer(chiffre);

        assertEquals(message, dechiffre);
    }

    @Test
    void testUnicode() {
        String message = "éàïΩ≈→漢字";
        vigenere.setClef("cléÜ");

        String chiffre = vigenere.chiffrer(message);
        String dechiffre = vigenere.dechiffrer(chiffre);

        assertEquals(message, dechiffre);
    }

    @Test
    void testMessageVide() {
        String message = "";
        String clef = "ABC";
        vigenere.setClef(clef);

        assertEquals("", vigenere.chiffrer(message));
        assertEquals("", vigenere.dechiffrer(message));
    }

    @Test
    void testChiffrementNonTrivial() {
        String message = "HELLO";
        vigenere.setClef("KEY");

        String chiffre = vigenere.chiffrer(message);

        assertNotEquals(message, chiffre,
                "Le message chiffré ne doit pas être identique au message d'origine");
    }

    @Test
    void testClefNonDefinie() {
        Vigenere v = new Vigenere();

        assertThrows(NullPointerException.class, () -> v.chiffrer("TEST"));
        assertThrows(NullPointerException.class, () -> v.dechiffrer("TEST"));
    }

    @Test
    void testClefVide() {
        vigenere.setClef("");

        assertThrows(ArithmeticException.class, () -> vigenere.chiffrer("TEST"),
                "Division by zero ou clé invalide attendue");
    }
}
