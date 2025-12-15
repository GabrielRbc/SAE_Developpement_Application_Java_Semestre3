/*
 * GenerateurClefTest.java              26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe GenerateurClef.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class GenerateurClefTest {

    @Test
    void testConstructeurValide() {
        // Le nombre d'itérations est déterminé par entierSecret % 1000.
        // Donc pour 123, la clé doit avoir une longueur de 123 caractères.
        long secret = 123;
        GenerateurClef generateur = new GenerateurClef(secret);

        assertNotNull(generateur.getClef(), "La clé ne doit pas être null");
        assertEquals(123, generateur.getClef().length(), "La longueur de la clé doit être égale à secret % 1000");
    }

    @Test
    void testDeterminisme() {
        long secret = 987654321L;

        GenerateurClef gen1 = new GenerateurClef(secret);
        GenerateurClef gen2 = new GenerateurClef(secret);

        assertEquals(gen1.getClef(), gen2.getClef(), "Deux générateurs avec le même secret doivent produire la même clé");
    }

    @Test
    void testSetEntierSecret() {
        GenerateurClef gen = new GenerateurClef(10);
        String clefInitiale = gen.getClef();
        assertEquals(10, clefInitiale.length());

        // Changement du secret
        gen.setEntierSecret(50);
        String nouvelleClef = gen.getClef();

        assertEquals(50, nouvelleClef.length());
        assertNotEquals(clefInitiale, nouvelleClef, "La clé doit changer si le secret change");
    }

    @Test
    void testCasLimitesVides() {
        // On teste manuellement plusieurs valeurs dans une boucle
        long[] valeursATester = {0, 1000, 2000, -123};

        for (long secret : valeursATester) {
            GenerateurClef gen = new GenerateurClef(secret);

            assertNotNull(gen.getClef(), "La clé ne doit pas être null pour le secret : " + secret);
            assertEquals("", gen.getClef(), "La clé devrait être vide pour le secret : " + secret);
            assertEquals(0, gen.getClef().length(), "La longueur devrait être 0 pour le secret : " + secret);
        }
    }

    @Test
    void testGrandEntier() {
        // 123456789L % 1000 = 789
        long grandSecret = 123456789L;
        GenerateurClef gen = new GenerateurClef(grandSecret);

        assertEquals(789, gen.getClef().length());
    }
}