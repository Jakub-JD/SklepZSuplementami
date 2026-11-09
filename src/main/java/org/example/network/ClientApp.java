package org.example.network;

import java.io.*;
import java.net.*;

public class ClientApp {
    private static final String KEY = "TAJNEKLUCZE";
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2020;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Połączono z serwerem. Odbieranie danych:\n");
            String encryptedLine;

            while ((encryptedLine = in.readLine()) != null) {
                String decryptedLine = VigenereCipher.decrypt(encryptedLine, KEY);
                System.out.println("ZASZYFROWANE: " + encryptedLine);
                System.out.println("ODSZYFROWANE: " + decryptedLine + "\n");
            }
        } catch (IOException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }
}