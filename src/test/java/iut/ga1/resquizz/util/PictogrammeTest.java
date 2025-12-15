/*
 * LectureJsonTest.java               24 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PictogrammeTest {

    @Test
    void getIdentifiant() {
        Pictogramme p = new Pictogramme(1, "intitule", "description",
                "lien/image.png");
        int id = p.getIdentifiant();

        assertEquals(1, id, "L'identifiant retourné n'est pas celui attendu");
    }

    @Test
    void getIntitule() {
        Pictogramme p = new Pictogramme(1, "intitule", "description",
                "lien/image.png");
        String actual = p.getIntitule();

        assertNotNull(actual, "L'intitulé ne devrait pas être null");
        assertEquals("intitule", actual, "L'intitulé retourné n'est pas celui attendu");
    }

    @Test
    void getDescription() {
        Pictogramme p = new Pictogramme(1, "intitule", "description",
                "lien/image.png");
        String actual = p.getDescription();

        assertNotNull(actual, "La description ne devrait pas être null");
        assertEquals("description", actual, "La description retournée n'est pas celle attendue");
    }

    @Test
    void getLienImage() {
        Pictogramme p = new Pictogramme(1, "intitule", "description",
                "lien/image.png");
        String actual = p.getLienImage();

        assertNotNull(actual, "Le lien de l'image ne devrait pas être null");
        assertEquals("lien/image.png", actual, "Le lien retourné n'est pas celui attendu");
    }

    @Test
    void toStringAffichage() {
        Pictogramme p = new Pictogramme(1, "intitule", "description",
                "Lien/image.png");

        String result = p.toString();
        assertNotNull(result, "toString() ne devrait pas retourner null");
        assertEquals("intitule : description", result,
                "La chaîne retournée par toString() n'est pas celle attendue"
        );
    }

    @SuppressWarnings("all")
    @Test
    void testEquals() {
        Pictogramme p1 = new Pictogramme(1, "intitule1", "Description1", "lien/image1.png");
        Pictogramme p2 = new Pictogramme(1, "intitule1", "Description1", "lien/image1.png");
        Pictogramme p3 = new Pictogramme(2, "intitule2", "Description2", "lien/image2.png");

        assertEquals(p1, p2, "Deux pictogrammes identiques doivent être égaux");
        assertNotEquals(p1, p3, "Deux pictogrammes différents ne doivent pas être égaux");

        assertEquals(p1, p1, "Un pictogramme doit être égal à lui-même");

        // Inverser l'ordre pour les assertions de non-égalité
        assertNotEquals(null, p1, "Un pictogramme ne doit pas être égal à null");

        // Vérification explicite d'égalité avec un autre type (comme une chaîne)
        assertNotEquals("autre type", p1, "Un pictogramme ne doit pas être égal à un autre type");
    }
}