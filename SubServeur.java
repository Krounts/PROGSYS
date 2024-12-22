import java.io.*;
import java.net.*;

public class SubServeur {
    public void startSubServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Sous-serveur démarré sur le port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur sous-serveur sur le port " + port + ": " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             FileOutputStream fos = new FileOutputStream("upload/" + dis.readUTF())) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            System.out.println("Fichier reçu par le sous-serveur.");
        } catch (IOException e) {
            System.err.println("Erreur lors du traitement du client : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 23456; // Port par défaut
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new SubServeur().startSubServer(port);
    }
}
