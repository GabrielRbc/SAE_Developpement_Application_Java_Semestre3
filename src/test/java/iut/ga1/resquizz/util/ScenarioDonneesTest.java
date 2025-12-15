/*
 * ScenarioDonneesTest.java               26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioDonneesTest {

    @Test
    void testConstructeurDefaut() {
        ScenarioDonnees sd = new ScenarioDonnees();

        // Comme les champs sont des objets (tableaux), ils sont initialisés à null par Java
        assertNull(sd.getDescription(), "La description devrait être null par défaut");
        assertNull(sd.getIdsCorrects(), "Les IDs corrects devraient être null par défaut");
    }

    @Test
    void testConstructeurParametre() {
        String[] desc = {"Etape 1 : Alerte", "Etape 2 : Action"};
        int[] ids = {1, 5};

        ScenarioDonnees sd = new ScenarioDonnees(desc, ids);

        assertArrayEquals(desc, sd.getDescription(), "Le tableau de description doit correspondre à celui fourni");
        assertArrayEquals(ids, sd.getIdsCorrects(), "Le tableau d'IDs doit correspondre à celui fourni");
    }

    @Test
    void testTableauxVides() {
        String[] desc = {};
        int[] ids = {};

        ScenarioDonnees sd = new ScenarioDonnees(desc, ids);

        assertNotNull(sd.getDescription());
        assertEquals(0, sd.getDescription().length, "La longueur du tableau description doit être 0");

        assertNotNull(sd.getIdsCorrects());
        assertEquals(0, sd.getIdsCorrects().length, "La longueur du tableau IDs doit être 0");
    }

    @Test
    void testConstructeurAvecNull() {
        ScenarioDonnees sd = new ScenarioDonnees(null, null);

        assertNull(sd.getDescription(), "La description doit rester null si on passe null");
        assertNull(sd.getIdsCorrects(), "Les IDs doivent rester null si on passe null");
    }
}