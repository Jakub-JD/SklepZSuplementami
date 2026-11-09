package org.example.network;

/**
 * Narzędzie kryptograficzne implementujące symetryczny szyfr Vigenère'a.
 * Algorytm przetwarza wyłącznie litery alfabetu łacińskiego (konwertując je na wielkie).
 * Wszelkie znaki specjalne, cyfry oraz spacje pozostają w tekście niezmienione.
 */

public class VigenereCipher {

    /**
     * @param text tekst źródłowy do zaszyfrowania
     * @param key klucz szyfrujący (wyłącznie litery alfabetu łacińskiego)
     * @return zaszyfrowany ciąg znaków zapisany wielkimi literami
     * @throws NullPointerException gdy parametr text lub key jest wartością null
     */
    public static String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                result.append(c);
                continue;
            }
            result.append((char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
            j = ++j % key.length();
        }
        return result.toString();
    }

    /**
     * @param text zaszyfrowany ciąg znaków
     * @param key klucz odszyfrowujący (wyłącznie litery alfabetu łacińskiego)
     * @return odszyfrowany ciąg znaków zapisany wielkimi literami
     * @throws NullPointerException gdy parametr text lub key jest wartością null
     */
    public static String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                result.append(c);
                continue;
            }
            result.append((char) ((c - key.charAt(j) + 26) % 26 + 'A'));
            j = ++j % key.length();
        }
        return result.toString();
    }
}