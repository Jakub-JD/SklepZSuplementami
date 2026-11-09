package org.example.network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Aplikacja serwerowa (węzeł TCP) udostępniająca pliki lokalne z wykorzystaniem szyfrowania.
 * Serwer działa w pętli nieskończonej, obsługując żądania klientów sekwencyjnie.
 * Przed wysłaniem zawartości, każda linia pliku jest zabezpieczana szyfrem Vigenère'a.
 */

public class ServerApp {
    private static final int PORT = 2020;
    private static final String KEY = "TajnyKluczJF";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Serwer uruchomiony na porcie " + PORT + ". Oczekiwanie na klientów...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("\nKlient podłączony.");

                    String requestedFile = in.readLine();
                    System.out.println("Klient żąda pliku: " + requestedFile);

                    File file = new File(requestedFile);
                    if (!file.exists() || file.isDirectory()) {
                        out.println("ERROR: Plik nie istnieje na serwerze.");
                        System.out.println("Odrzucono - plik nie istnieje.");
                        continue;
                    }

                    try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = fileReader.readLine()) != null) {
                            String encryptedLine = VigenereCipher.encrypt(line, KEY);
                            out.println(encryptedLine);
                        }
                    }
                    System.out.println("Zakończono przesyłanie pliku.");

                } catch (IOException e) {
                    System.err.println("Błąd komunikacji z klientem: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd uruchamiania serwera: " + e.getMessage());
        }
    }
}