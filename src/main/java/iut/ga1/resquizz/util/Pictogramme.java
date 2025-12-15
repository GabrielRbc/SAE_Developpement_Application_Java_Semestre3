/*
 * Pictogramme.java                     14 Oct. 2025
 * IUT de rodez Info 2, 2025 - 2026, pas de copyright
 */
package iut.ga1.resquizz.util;

/**
 * Classe permettant de définir des objets Pictogramme.
 * Ils seront composés d'un identifiant, d'un intitulé,
 * d'une description ainsi que du lien menant à leur image.
 *
 * @author Gabriel LE GOFF
 * @author Gabriel Robache
 * @author Tom Killing
 * @author Noé Rebourg
 * @author Esteban Roveri
 * @version 1
 * @since 2025
 */
public class Pictogramme {

    /** Identifiant du pictogramme */
    private final int IDENTIFIANT;

    /** Intitulé du pictogramme */
    private final String INTITULE;

    /** Description du pictogramme */
    private final String DESCRIPTION;

    /** Lien menant à l'image du pictogramme */
    private final String LIEN_IMAGE;

    /**
     * Création d'un Pictogramme
     * @param identifiant du pictogramme
     * @param intitule    du pictogramme
     * @param description du pictogramme
     * @param lienImage   du pictogramme
     * @throws IllegalArgumentException si l'intitule,
     *         la description ou le lien de l'image est null
     */
    public Pictogramme(int identifiant, String intitule, String description, String lienImage) {
        if (intitule == null) {
            throw new IllegalArgumentException("intitule null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description null");
        }
        if (lienImage == null) {
            throw new IllegalArgumentException("lienImage null");
        }
        this.IDENTIFIANT = identifiant;
        this.INTITULE = intitule;
        this.DESCRIPTION = description;
        this.LIEN_IMAGE = lienImage;
    }

    /** Renvoie l'identifiant du pictogramme */
    public int getIdentifiant() {
        return IDENTIFIANT;
    }

    /** Renvoie l'intitule du pictogramme */
    public String getIntitule() {
        return INTITULE;
    }

    /** Renvoie la description du pictogramme */
    public String getDescription() {
        return DESCRIPTION;
    }

    /** Renvoie le lien de l'image correspondant au pictogramme*/
    public String getLienImage() {
        return LIEN_IMAGE;
    }

    /** Renvoie un Pictogramme sous la forme {intitule} : {description} */
    @Override
    public String toString() {
        return getIntitule() + " : " + getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pictogramme that = (Pictogramme) o;

        if (IDENTIFIANT != that.IDENTIFIANT) return false;
        if (!INTITULE.equals(that.INTITULE)) return false;
        if (!DESCRIPTION.equals(that.DESCRIPTION)) return false;
        return LIEN_IMAGE.equals(that.LIEN_IMAGE);
    }

}
