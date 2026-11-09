package org.example.network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Klient TCP pobierający zaszyfrowane strumienie danych od serwera.
 * Klasa odpowiada za nawiązanie połączenia, żądanie konkretnego zasobu (pliku)
 * oraz deszyfrowanie odebranych pakietów tekstowych.
 * Rozpoznaje i obsługuje komunikaty błędów (np. brak pliku na serwerze).
 */

public class ClientApp {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2020;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Podaj nazwę pliku, który chcesz pobrać z serwera (np. dane.txt): ");
            String fileName = scanner.nextLine();

            out.println(fileName);

            System.out.print("Podaj klucz do deszyfrowania: ");
            String key = scanner.nextLine();

            System.out.println("\n--- Odbieranie danych z serwera ---");
            String encryptedLine;

            while ((encryptedLine = in.readLine()) != null) {
                if (encryptedLine.startsWith("ERROR:")) {
                    System.out.println("Komunikat z serwera: " + encryptedLine);
                    break;
                }

                String decryptedLine = VigenereCipher.decrypt(encryptedLine, key);
                System.out.println("ZASZYFROWANE: " + encryptedLine);
                System.out.println("ODSZYFROWANE: " + decryptedLine + "\n");
            }
        } catch (IOException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }
}