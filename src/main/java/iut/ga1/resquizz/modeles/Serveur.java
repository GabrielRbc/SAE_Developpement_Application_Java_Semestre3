/*
 * Serveur.java                    10 Nov. 2025
 * IUT de Rodez, pas de copyright
 */

package iut.ga1.resquizz.modeles;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * La classe {@code Serveur} représente la partie serveur de l’application
 * pair-à-pair utilisée pour l’échange sécurisé des paramètres et des scénarios
 * entre deux machines du même réseau local.
 *
 * <p>Ce serveur utilise le protocole TCP et ne gère qu'une seule connexion
 * cliente à la fois. Son numéro
 * de port peut être configuré tant qu’il n’est pas actif.</p>
 *
 * <p>Lorsqu’un client se connecte :
 * <ul>
 *     <li>Le serveur initialise un échange de clef selon le protocole
 *     de Diffie-Hellman en envoyant au client son nombre premier, son générateur
 *     et sa valeur calculée.</li>
 *     <li>Le client répond avec sa propre valeur, permettant au serveur de calculer
 *     le secret partagé.</li>
 *     <li>Ce secret est ensuite utilisé pour générer une clef de chiffrement via
 *     {@link GenerateurClef}, qui sert à initialiser le chiffrement Vigenère.</li>
 * </ul></p>
 *
 * <p>Une fois la connexion sécurisée établie, les échanges entre le serveur et le client
 * sont chiffrés et déchiffrés avec la clef Vigenère commune. Le serveur est capable
 * de recevoir des messages tels que "Paramètres" ou "Scénario" et de répondre avec
 * des données correspondantes simulées.</p>
 *
 * <p>Le serveur affiche sur la console :
 * <ul>
 *     <li>Les informations de connexion (port, IP du client)</li>
 *     <li>Les messages reçus et déchiffrés</li>
 * </ul></p>
 *
 * <p>Principales classes impliquées :
 * <ul>
 *     <li>{@link DiffieHellman} : pour la génération et l’échange de clefs</li>
 *     <li>{@link GenerateurClef} : pour la création d’une clef textuelle à partir du secret partagé</li>
 *     <li>{@link Vigenere} : pour le chiffrement et le déchiffrement des messages échangés</li>
 * </ul></p>
 *
 * <p><b>Remarques :</b>
 * <ul>
 *     <li>Le serveur ne gère qu’un seul client à la fois.</li>
 *     <li>Les données échangées sont textuelles et chiffrées par Vigenère après l’établissement de la clef secrète.</li>
 *     <li>Le mode d’adressage peut être dynamique ou statique selon la configuration réseau.</li>
 * </ul></p>
 *
 * @author GA1
 * @version 10 novembre 2025
 */

public class Serveur {

    /** Instance unique de Diffie-Hellman pour l’échange de clef. */
    private static DiffieHellman dh;

    /** Générateur de clef basé sur le secret partagé. */
    private static GenerateurClef generateurClef;

    /** Instance de Vigenère pour le chiffrement/déchiffrement des messages. */
    private static Vigenere vigenere;

    /** Port sur lequel le serveur écoute les connexions entrantes. */
    private static int port = 5000;

    /** Socket serveur pour accepter les connexions clientes. */
    private static ServerSocket serverSocket;

    /** Indicateur de l’état du serveur */
    private static boolean isOn = false;

    /** Socket client connecté. */
    private static Socket client;

    /** Flux d’entrée pour lire les messages du client. */
    private static BufferedReader in;

    /** Flux de sortie pour envoyer des messages au client. */
    private static PrintWriter out;

    /** Message reçu du client. */
    private static String messageRecu;

    /** Retourne l’état actuel du serveur (démarré/arrêté). */
    public static boolean isOn() {
        return isOn;
    }

    /** Récupère le dernier message reçu du client et le réinitialise à null. */
    public static String getMessage() {
        String copie = messageRecu;
        messageRecu = null;
        return copie;
    }

    /** Change le port d’écoute du serveur si celui-ci est arrêté.
     *
     * @param portAChanger le nouveau port à utiliser
     * @return {@code true} si le port a été changé avec succès,
     *         {@code false} si le serveur est déjà en cours d’exécution
     */
    public static boolean changerPort(int portAChanger) {
        if (!isOn) {
            port = portAChanger;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Lance le serveur sur le port actuellement configuré (par défaut 5000)
     * et attend les connexions entrantes d’un unique client à la fois.
     *
     * <p>Cette méthode initialise le protocole Diffie-Hellman pour préparer
     * un échange de clef sécurisé entre le serveur et le client. Une fois
     * le serveur démarré, la méthode {@link #receptionEnvoiMessages()} est
     * appelée pour gérer la communication chiffrée avec le client.</p>
     *
     * @throws IOException si une erreur survient lors de la création
     *         du {@link java.net.ServerSocket} ou de la gestion des flux réseau.
     */
    public static void lancerServeur() throws IOException {
        // Création du serveur et liaison au port 5000 par défaut
        serverSocket = new ServerSocket(port);
        isOn = true;
        System.out.println("Serveur démarre sur le port " + port);

        // Création de Diffie-Hellman
        dh = new DiffieHellman();

        receptionEnvoiMessages();
    }

    /**
     * Gère la réception et l’envoi des messages entre le serveur et un client connecté.
     *
     * <p>Cette méthode bloque le thread principal et attend une connexion entrante.
     * Dès qu’un client est accepté, le serveur :</p>
     * <ul>
     *     <li>Envoie ses paramètres Diffie-Hellman (nombre premier, générateur, valeur calculée) au client,</li>
     *     <li>Reçoit la valeur du client et calcule le secret partagé,</li>
     *     <li>Génère une clef de chiffrement via {@link GenerateurClef} et initialise un objet {@link Vigenere},</li>
     *     <li>Déchiffre les messages reçus et envoie les réponses correspondantes.</li>
     * </ul>
     *
     * <p>Les messages échangés après l’établissement du secret partagé sont chiffrés avec la méthode de Vigenère.
     * Le serveur affiche chaque message reçu et déchiffré sur la console. Si le client envoie les chaînes
     * "Paramètres" ou "Scénario", le serveur renvoie une réponse adaptée.</p>
     *
     * @throws IOException si une erreur survient lors de la lecture ou de l’écriture
     *         sur les flux réseau ou lors de la fermeture de la connexion.
     */
    private static void receptionEnvoiMessages() throws IOException {
        int compteur = 0;

        while (isOn) {
            // Acceptation d'une unique connexion cliente
            client = serverSocket.accept();
            System.out.println("Client connecte : " + client.getInetAddress());

            // Création des canaux input/output
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            out.println(dh.getPremier() + ";" + dh.getGenerateur() + ";" + dh.getNombreCree());

            // Boucle d'écho (lecture/écriture)
            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println("Reçu : " + ligne);
                if (compteur == 0) {
                    dh.setNombreRecu(Long.parseLong(ligne));
                    generateurClef = new GenerateurClef(dh.getNombreSecret());
                    vigenere = new Vigenere(generateurClef.getClef());
                    compteur++;
                } else {
                    if (ligne.equals("Fermeture client")) {
                        System.out.println("Le client a demande la fermeture.");
                        break; // sortir de la boucle
                    }

                    try {
                        String dechiffre = vigenere.dechiffrer(ligne);
                        System.out.println("Déchiffre : " + dechiffre);
                        messageRecu = dechiffre;
                    } catch (Exception e) {
                        System.err.println("Message non déchiffrable : " + ligne);
                    }
                }
            }

            // Fermeture de la connexion client
            System.out.println("Client déconnecté");
            in.close();
            out.close();
            client.close();

            compteur = 0; // Permettre une connexion après la déconnexion
            dh = new DiffieHellman(); // Réinitialisation de Diffie-Hellman pour plus de sécurité
        }
    }

    public static void envoyerMessage(String message) throws IOException {
        String chiffre = vigenere.chiffrer(message);
        String enveloppe = Base64.getEncoder().encodeToString(
                chiffre.getBytes(StandardCharsets.UTF_8)
        );
        out.println(enveloppe);
    }

    /**
     * Ferme proprement la connexion du serveur et libère toutes les ressources réseau associées.
     *
     * <p>Cette méthode :
     * <ul>
     *     <li>Envoie un message de fermeture au client connecté (si présent),</li>
     *     <li>Ferme les flux d’entrée/sortie et les sockets,</li>
     *     <li>Met à jour l’état du serveur pour indiquer qu’il est arrêté.</li>
     * </ul></p>
     *
     * <p>Elle est sûre à appeler même si certaines ressources ne sont pas encore initialisées.
     * Aucune exception non gérée ne sera levée si un des flux est déjà fermé ou nul.</p>
     *
     */
    public static void fermerServeur() {
        System.out.println("Fermeture du serveur...");

        // Envoi d’un message de fermeture si possible
        if (out != null) {
            out.println("Fermeture serveur");
            out.flush();
        }

        // Fermeture sécurisée des flux
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du flux d’entrée : " + e.getMessage());
        }

        try {
            if (out != null) out.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture du flux de sortie : " + e.getMessage());
        }

        // Fermeture du socket client
        try {
            if (client != null && !client.isClosed()) client.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du client : " + e.getMessage());
        }

        // Fermeture du socket serveur
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
        }

        isOn = false;
        System.out.println("Serveur arrêté avec succès.");
    }

    /** Méthode principale pour lancer le serveur. */
    public static void main(String[] args) throws IOException {
        Serveur.lancerServeur();
    }

    /** Envoie un fichier chiffré au client connecté.
     *
     * @param cheminFichier Chemin complet ou relatif du fichier à envoyer.
     * @param type Type de contenu (ex. "Parametres" ou "Scenario").
     */
    public static void envoyerFichierChiffre(String cheminFichier, String type) {
        try {
            InputStream is;
            if (cheminFichier.startsWith("/")) {
                is = Serveur.class.getResourceAsStream(cheminFichier);
            } else {
                File file = new File(cheminFichier);
                is = new FileInputStream(file);
            }

            if (is == null) {
                System.err.println("Fichier introuvable : " + cheminFichier);
                return;
            }

            byte[] bytes = is.readAllBytes();
            is.close();

            String base64 = Base64.getEncoder().encodeToString(bytes);

            // Étape 1 : on chiffre en Vigenère
            String chiffre = vigenere.chiffrer(type + "|" + base64);

            // Étape 2 (Solution 1) : on encode le résultat en Base64
            String enveloppe = Base64.getEncoder().encodeToString(
                    chiffre.getBytes(StandardCharsets.UTF_8)
            );

            // Étape 3 : on peut envoyer en println(), sans risque
            out.println(enveloppe);
            System.out.println(type + " envoyé.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
