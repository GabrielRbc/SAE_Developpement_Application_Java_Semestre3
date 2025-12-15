/*
 * EtapeScenarioTest.java               26 Nov. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EtapeScenarioTest {

    @Test
    void testConstructeur() {
        EtapeScenario etape = new EtapeScenario();
        assertNotNull(etape, "L'instance ne doit pas être null");
        // Les champs ne sont pas initialisés, donc pictogramme est 0 (int) et textes est null
        assertEquals(0, etape.pictogramme);
        assertNull(etape.textes);
    }

    @Test
    void testPictogramme() {
        EtapeScenario etape = new EtapeScenario();
        int idPicto = 12;

        etape.pictogramme = idPicto;

        assertEquals(idPicto, etape.pictogramme, "Le champ pictogramme doit conserver la valeur assignée");
    }

    @Test
    void testTextes() {
        EtapeScenario etape = new EtapeScenario();
        List<String> textes = Arrays.asList("Texte 1", "Texte 2");

        etape.textes = textes;

        assertNotNull(etape.textes, "La liste de textes ne doit pas être null après affectation");
        assertEquals(2, etape.textes.size(), "La taille de la liste doit correspondre");
        assertEquals("Texte 1", etape.textes.get(0));
        assertEquals("Texte 2", etape.textes.get(1));
    }
}