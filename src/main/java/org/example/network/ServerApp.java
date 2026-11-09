package org.example.network;

import java.io.*;
import java.net.*;

public class ServerApp {
    private static final String KEY = "TAJNEKLUCZE";
    private static final int PORT = 2020;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer uruchomiony. Oczekiwanie na klienta...");

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader fileReader = new BufferedReader(new FileReader("dane.txt"))) {

                System.out.println("Klient podłączony. Rozpoczęto wysyłanie danych...");
                String line;

                while ((line = fileReader.readLine()) != null) {
                    String encryptedLine = VigenereCipher.encrypt(line, KEY);
                    out.println(encryptedLine);
                }
                System.out.println("Zakończono przesyłanie pliku.");
            }
        } catch (IOException e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }
}