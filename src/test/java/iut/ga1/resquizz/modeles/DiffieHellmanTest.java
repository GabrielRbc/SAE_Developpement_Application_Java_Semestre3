/*
 * DiffieHellmanTest.java               26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe DiffieHellman.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class DiffieHellmanTest {

    @Test
    void testIsPremierValide() {
        assertTrue(DiffieHellman.isPremier(2), "2 est premier");
        assertTrue(DiffieHellman.isPremier(3), "3 est premier");
        assertTrue(DiffieHellman.isPremier(5), "5 est premier");
        assertTrue(DiffieHellman.isPremier(7), "7 est premier");
        assertTrue(DiffieHellman.isPremier(13), "13 est premier");
        assertTrue(DiffieHellman.isPremier(1009), "1009 est premier");
    }

    @Test
    void testIsPremierInvalide() {
        assertFalse(DiffieHellman.isPremier(0), "0 n'est pas premier");
        assertFalse(DiffieHellman.isPremier(1), "1 n'est pas premier");
        assertFalse(DiffieHellman.isPremier(4), "4 n'est pas premier");
        assertFalse(DiffieHellman.isPremier(10), "10 n'est pas premier");
        assertFalse(DiffieHellman.isPremier(15), "15 n'est pas premier");
        assertFalse(DiffieHellman.isPremier(-5), "Les négatifs ne sont pas premiers");
    }

    @Test
    void testGenererNombrePremier() {
        for (int i = 0; i < 50; i++) { // Test répété pour l'aléatoire
            long p = DiffieHellman.genererNombrePremier();
            assertTrue(p >= 1000 && p <= 1999, "Le nombre doit être entre 1000 et 1999");
            assertTrue(DiffieHellman.isPremier(p), "Le nombre généré doit être premier");
        }
    }

    @Test
    void testIsGenerateur() {
        // Pour p = 7, les générateurs sont 3 et 5.
        // 2 n'est pas un générateur (ordre 3)
        long p = 7;

        assertTrue(DiffieHellman.isGenerateur(3, p), "3 devrait être un générateur de 7");
        assertTrue(DiffieHellman.isGenerateur(5, p), "5 devrait être un générateur de 7");

        assertFalse(DiffieHellman.isGenerateur(1, p), "1 n'est jamais un générateur");
        assertFalse(DiffieHellman.isGenerateur(2, p), "2 n'est pas un générateur de 7");
        assertFalse(DiffieHellman.isGenerateur(4, p), "4 n'est pas un générateur de 7");
    }

    @Test
    void testIsGenerateurException() {
        assertThrows(IllegalArgumentException.class, () -> {
            DiffieHellman.isGenerateur(2, 6); // 6 n'est pas premier
        });
    }

    @Test
    void testGenererGenerateur() {
        long p = 17; // Petit nombre premier pour le test
        long g = DiffieHellman.genererGenerateur(p);

        assertTrue(g > 0 && g < p, "Le générateur doit être compris entre 1 et p-1");
        assertTrue(DiffieHellman.isGenerateur(g, p), "Le nombre retourné doit être un générateur de p");
    }

    @Test
    void testConstructeurDefaut() {
        DiffieHellman dh = new DiffieHellman();

        long p = dh.getPremier();
        long g = dh.getGenerateur();

        assertTrue(DiffieHellman.isPremier(p), "p doit être premier");
        assertTrue(DiffieHellman.isGenerateur(g, p), "g doit être un générateur de p");
    }

    @Test
    void testConstructeurParametre() {
        long p = 23;
        long g = 5; // 5 est un générateur de 23

        DiffieHellman dh = new DiffieHellman(p, g);
        assertEquals(p, dh.getPremier());
        assertEquals(g, dh.getGenerateur());
    }

    @Test
    void testConstructeurParametreInvalideP() {
        assertThrows(IllegalArgumentException.class, () -> new DiffieHellman(10, 3));
    }

    @Test
    void testConstructeurParametreInvalideG() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DiffieHellman(7, 2); // 2 n'est pas gen de 7
        });
    }

    @Test
    void testEchangeDeCles() {
        // Alice initialise (choisit p et g)
        DiffieHellman alice = new DiffieHellman();

        // Bob reçoit p et g d'Alice
        DiffieHellman bob = new DiffieHellman(alice.getPremier(), alice.getGenerateur());

        // Chacun génère sa "clé publique" (A = g^a mod p, B = g^b mod p)
        long alicePublic = alice.getNombreCree();
        long bobPublic = bob.getNombreCree();

        // Vérification que les clés publiques sont bien calculées (>0)
        assertTrue(alicePublic > 0);
        assertTrue(bobPublic > 0);

        // Échange : Alice reçoit B, Bob reçoit A
        alice.setNombreRecu(bobPublic);
        bob.setNombreRecu(alicePublic);

        // Vérification que la clé reçue est bien stockée
        assertEquals(bobPublic % alice.getPremier(), alice.getNombreRecu());
        assertEquals(alicePublic % bob.getPremier(), bob.getNombreRecu());

        // Le secret partagé doit être identique
        long secretAlice = alice.getNombreSecret();
        long secretBob = bob.getNombreSecret();

        System.out.println("Secret Alice : " + secretAlice);
        System.out.println("Secret Bob   : " + secretBob);

        assertEquals(secretAlice, secretBob, "Les secrets calculés doivent être identiques");

        // Le secret ne devrait pas être 0 (très improbable).
        assertNotEquals(0, secretAlice);
    }
}