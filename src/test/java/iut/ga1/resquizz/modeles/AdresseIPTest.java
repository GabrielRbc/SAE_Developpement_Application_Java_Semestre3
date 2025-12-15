/*
 * AdresseIPTest.java                                      26 Nov. 2025
 * IUT de Rodez, Info 2 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe AdresseIP.
 *
 * @author Gabriel Robache
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
class AdresseIPTest {

    @Test
    void testScanReseau() {
        try {
            // Appel de la méthode
            InetAddress[] adresses = AdresseIP.scanReseau();

            // Vérifie que le tableau n'est pas null
            assertNotNull(adresses, "Le tableau d'adresses ne doit pas être null");

            // Vérifie qu'il y a au moins une adresse
            assertTrue(adresses.length > 0, "Le scan réseau doit retourner au moins une adresse IP (localhost)");

            // Vérifie le contenu
            System.out.println("Adresses trouvées sur la machine de test :");
            for (InetAddress adr : adresses) {
                assertNotNull(adr, "Une adresse dans le tableau ne doit pas être null");
                System.out.println(" - " + adr.getHostAddress());
            }

        } catch (UnknownHostException e) {
            // Si le test échoue à cause d'une configuration réseau de la machine (rare, mais possible).
            fail("Le test a échoué car l'hôte est inconnu : " + e.getMessage());
        }
    }

    @Test
    void testLocalHostPresence() throws UnknownHostException {
        InetAddress[] adresses = AdresseIP.scanReseau();
        boolean localFound = false;

        // On récupère l'adresse de localhost standard pour comparer
        String localHostName = InetAddress.getLocalHost().getHostName();

        for (InetAddress adr : adresses) {
            // On vérifie que les adresses retournées correspondent bien au nom d'hôte local
            if (adr.getHostName().equalsIgnoreCase(localHostName)) {
                localFound = true;
                break;
            }
        }

        assertTrue(localFound, "Les adresses retournées doivent correspondre au nom d'hôte local");
    }
}