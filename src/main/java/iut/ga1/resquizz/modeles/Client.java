/*
 * Client.java          10 Nov. 2025
 * IUT de Rodez, pas de copyright
 */

package iut.ga1.resquizz.modeles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iut.ga1.resquizz.controleurs.ControleurReceptionScenario;
import iut.ga1.resquizz.util.Parametres;
import iut.ga1.resquizz.util.Scenario;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * La classe {@code Client} représente la partie cliente de la communication pair-à-pair.
 *
 * <p>Elle permet de se connecter à un serveur TCP (par défaut localhost:5000),
 * d’effectuer un échange de clef sécurisé via Diffie-Hellman, puis de communiquer
 * avec chiffrement Vigenère.</p>
 *
 * <p>La logique de connexion, de réception et d’envoi est entièrement gérée
 * par des méthodes internes. Le point d’entrée {@code main()} ne sert qu’à
 * illustrer l’utilisation de la classe.</p>
 *
 * @version 10 novembre 2025
 * @author GA1
 */
public class Client {

    /** Instance Diffie-Hellman pour l’échange de clef. */
    private static DiffieHellman dh;

    /** Générateur de clef Vigenère basé sur le secret partagé. */
    private static GenerateurClef generateurClef;

    /** Instance Vigenère pour le chiffrement/déchiffrement des messages. */
    protected static Vigenere vigenere;

    /** La clef de chiffrement partagée. */
    private static String clef;

    /** Le dernier message reçu et déchiffré. */
    private static String messageRecu;

    /** Adresse du serveur (par défaut localhost). */
    private static String host = "localhost";

    /** Le message a envoyé au serveur. */
    private static String message;

    /** Socket de communication avec le serveur. */
    private static Socket socket;

    /** Flux d’écriture vers le serveur. */
    private static PrintWriter out;

    /** Indique si le client est connecté au serveur. */
    public static boolean connected = false;

    /**
     * Définit les paramètres du protocole Diffie-Hellman envoyés par le serveur
     * et génère la clef de chiffrement partagée.
     *
     * @param p  le nombre premier choisi par le serveur
     * @param g  le générateur choisi par le serveur
     * @param ga la valeur calculée du serveur (g^a mod p)
     */
    protected static void setDh(long p, long g, long ga) {
        dh = new DiffieHellman(p, g);
        message = Long.toString(dh.getNombreCree()); // g^b mod p
        dh.setNombreRecu(ga);                        // pour calculer le secret partagé
        generateurClef = new GenerateurClef(dh.getNombreSecret());
        clef = generateurClef.getClef();
        vigenere = new Vigenere(clef);
    }

    /**
     * Tente de se connecter au serveur TCP spécifié.
     *
     * @param hote  adresse IP ou nom d’hôte du serveur
     * @param port  numéro de port d’écoute du serveur
     * @return {@code true} si la connexion réussit, sinon {@code false}
     */
    public static boolean connecter(String hote, int port) {
        try {
            host = hote;

            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connecte au serveur : " + host + ":" + port);
            connected = true;

            // Lancer la réception des messages dans un thread séparé
            new Thread(new ReceptionHandler(socket)).start();

            // Lancer le thread d’attente et d’envoi automatique
            new Thread(Client::attendreMessage).start();

            return true;

        } catch (IOException e) {
            System.err.println("Échec de connexion : " + e.getMessage());
            connected = false;
            return false;
        }
    }

    /** Méthode pour savoir si un client est connecté au serveur */
    public static boolean estConnecte() {
        return connected;
    }

    /**
     * Ferme la connexion au serveur proprement.
     */
    public static void deconnecter() {
        try {
            if (out != null) out.println("Fermeture client");
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();

            connected = false;
            System.out.println("Déconnexion réussie.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

    /**
     * Crée et envoie un message chiffré via Vigenère au serveur.
     *
     * @param messageAEcrire le message à envoyer
     */
    public static void creerMessage(String messageAEcrire) {
        if (vigenere == null || out == null) {
            System.err.println("Erreur : clef non initialisée ou socket ferme.");
            return;
        }
        message = vigenere.chiffrer(messageAEcrire);
    }

    /**
     * Envoie automatiquement des messages à intervalles réguliers
     * une fois la clef partagée établie.
     */
    private static void attendreMessage() {
        try {
            while (connected) {
                if (message != null) {
                    out.println(message);
                    message = null;
                }

                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            System.err.println("Thread d’envoi interrompu : " + e.getMessage());
        }
    }

    /**
     * Déchiffre et stocke le message reçu du serveur.
     *
     * @param message le message chiffré reçu
     */
    protected static void setMessageRecu(String message) {
        if (message == null) return;

        // On retire l’enveloppe Base64
        byte[] decode = Base64.getDecoder().decode(message);

        // On récupère le texte chiffré Vigenère
        String chiffre = new String(decode, StandardCharsets.UTF_8);

        // Déchiffrement Vigenère comme avant
        String dechiffre = vigenere.dechiffrer(chiffre);

        // Si c'est un message fichier
        if (dechiffre.contains("|")) {
            gererMessageServeur(dechiffre);
        } else {
            messageRecu = dechiffre;
        }
    }

    /** Méthode pour afficher un pop-up */
    private static void afficherPopup(String titre, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /** Permet de gérer la création de paramètre ou l'ajout de scénario */
    protected static void gererMessageServeur(String message) {
        System.out.println("Message déchiffre : " + message);

        try {
            // Séparer type et Base64
            String[] split = message.split("\\|", 2);
            String type = split[0].trim();
            String base64 = split[1].replaceAll("\\s+", "");

            // Décoder Base64
            byte[] bytes = java.util.Base64.getDecoder().decode(base64);

            // Préparer le dossier de stockage
            String dossier = iut.ga1.resquizz.util.Parametres.getCheminStockage();
            File dir = new File(dossier);
            if (!dir.exists()) dir.mkdirs();

            String nom;
            File fichier;

            switch (type.toUpperCase()) {

                case "PARAMETRES":
                    nom = "parametres.ser";
                    fichier = new File(dir, nom);

                    try (FileOutputStream fos = new FileOutputStream(fichier)) {
                        fos.write(bytes);
                    }

                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                        Parametres.setInstance((Parametres) ois.readObject());
                    }

                    System.out.println("Fichier paramètres reçu : " + fichier.getAbsolutePath());
                    afficherPopup("Paramètres reçus", "Les paramètres ont été mis à jour avec succès !");
                    javafx.application.Platform.runLater(ControleurReceptionScenario::afficherNouveauParametre);
                    break;

                case "SCENARIOS":
                    nom = "scenarioFC.json";
                    fichier = new File(dir, nom);

                    // Convertir les bytes → JSON String
                    String json = new String(bytes, StandardCharsets.UTF_8);

                    // Parser JSON reçu
                    Gson gson = new Gson();
                    Type typeMap = new TypeToken<Map<String, List<Integer>>>() {}.getType();
                    Map<String, List<Integer>> recus = gson.fromJson(json, typeMap);

                    // Fusionner avec fichier local
                    Scenario gestion = new Scenario(); // utilise le chemin actuel de Parametres
                    gestion.fusionnerScenariosRecus(recus);

                    System.out.println("Fusion des scénarios réussie, fichier : " +
                            Paths.get(dossier, nom));
                    afficherPopup("Scénarios reçus",
                            "Les scénarios ont été ajoutés et fusionnés avec succès !");
                    break;
                default:
                    nom = "fichier_recu.bin";
                    fichier = new File(dir, nom);

                    try (FileOutputStream fos = new FileOutputStream(fichier)) {
                        fos.write(bytes);
                    }

                    System.out.println("Fichier binaire reçu : " + fichier.getAbsolutePath());
                    break;
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : Base64 invalide.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * Classe interne gérant la réception des messages depuis le serveur.
 */
class ReceptionHandler implements Runnable {

    /** Socket de communication avec le serveur. */
    private final Socket socket;

    /**
     * Constructeur avec socket.
     * @param socket le socket connectée au serveur
     */
    public ReceptionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        int compteur = 0;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println("Reçu du serveur : " + ligne);

                if (compteur == 0) {
                    // Premier message : paramètres Diffie-Hellman
                    String[] lSplit = ligne.split(";");
                    Client.setDh(
                            Long.parseLong(lSplit[0]),
                            Long.parseLong(lSplit[1]),
                            Long.parseLong(lSplit[2])
                    );
                    compteur++;
                } else {
                    Client.setMessageRecu(ligne);
                }
            }
        } catch (SocketException e) {
            System.out.println("Connexion fermée");
            Client.deconnecter();
        } catch (Exception e) {
            System.err.println("Erreur de reception : " + e.getMessage());
            e.printStackTrace();
        } finally {
            Client.connected = false;
        }
    }
}