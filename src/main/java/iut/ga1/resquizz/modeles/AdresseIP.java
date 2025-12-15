/*
 * AdresseIP.java               12 Oct. 2025
 * IUT de Rodez, pas de copyright
 */
package iut.ga1.resquizz.modeles;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * La classe {@code AdresseIP} est une classe utilitaire permettant d'effectuer
 * des opérations liées au réseau, notamment la récupération des adresses IP
 * disponibles sur la machine locale.
 *
 * <p>Elle repose sur l'utilisation de la classe standard {@link java.net.InetAddress}.
 *
 * @author Gabriel Le Goff
 * @author Tom Killing
 * @author Gabriel Robache
 * @author Noé Rebourg
 * @author Esteban Roveri
 */
public class AdresseIP {

    /**
     * Scanne et retourne toutes les adresses IP associées à l'hôte local.
     * <p>
     * Cette méthode récupère le nom d'hôte de la machine locale, puis demande
     * au système toutes les adresses IP (IPv4 et IPv6) qui y sont assignées.
     * </p>
     *
     * @return Un tableau d'objets {@code InetAddress} contenant toutes les adresses IP
     * trouvées pour la machine locale.
     * @throws UnknownHostException si le nom de l'hôte local ne peut pas être résolu.
     */
    public static InetAddress[] scanReseau() throws UnknownHostException {
        return InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
    }
}
