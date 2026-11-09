package org.example.network;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class VigenereCipherTest {

    @Test
    public void shouldEncryptTextCorrectly() {
        // Podstawowy test matematyki szyfru:
        // A(0)+A(0)=A, B(1)+B(1)=C(2), C(2)+C(2)=E(4)
        Assertions.assertEquals("ACE", VigenereCipher.encrypt("ABC", "ABC"));
    }

    @Test
    public void shouldDecryptTextCorrectly() {
        // Podstawowy test matematyki szyfru:
        // A(0)-A(0)=A, C(2)-B(1)=B(1), E(4)-C(2)=C(2)
        Assertions.assertEquals("ABC", VigenereCipher.decrypt("ACE", "ABC"));
    }

    @Test
    public void usingDiffrentKeyShouldGiveDiffrentText() {
        String text = "ABCDE";
        String key = "ABC";

        assertNotEquals(text, VigenereCipher.decrypt(text, "ACB"));
    }

    @Test
    public void shouldIgnoreSpecialCharactersAndSpaces() {
        String text = "ABC! 123 ???";
        String key = "ABC";
        String expected = "ACE! 123 ???";

        Assertions.assertEquals(expected, VigenereCipher.encrypt(text, key));
    }

    @Test
    public void encryptedThenDecryptedTextShouldBeSameAsOriginal() {
        String originalText = "TESTJEDNOSTKOWYDZIALANIAKODOWANIA";
        String key = "SKOMPLIKOWANYKLUCZ";

        String encrypted = VigenereCipher.encrypt(originalText, key);
        String decrypted = VigenereCipher.decrypt(encrypted, key);

        Assertions.assertEquals(originalText, decrypted);
    }

    @Test
    public void shouldThrowExceptionWhenTextIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            VigenereCipher.encrypt(null, "KLUCZ");
        });
    }

    @Test
    public void shouldThrowExceptionWhenKeyIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            VigenereCipher.encrypt("TEXT", null);
        });
    }



    @ParameterizedTest
    @CsvSource({
            "PROGRAMOWANIE, TAJNYKLUCZ",
            "STUDENT, KOT",
            "POLITECHNIKA, LUBELSKA",
            "ZN@KI SPECJALNE%$ @ TE$T, KLUCZ"
    })
    void shouldDecryptToOriginalText(String text, String key) {
        String encrypted = VigenereCipher.encrypt(text, key);
        String decrypted = VigenereCipher.decrypt(encrypted, key);

        Assertions.assertEquals(text, decrypted);
    }

    public static String[][] cipherData() {
        return new String[][]{
                {"PROGRAMOWANIE", "TAJNYKLUCZ"},
                {"STU DENT", "KOT"},
                {"POLITECHNIKA", "LUBELSKA"},
                {"ZN@KI SPECJALNE%$ @ TE$T", "KLUCZ"}
        };
    }
    @TestFactory
    Stream<DynamicTest> shouldChangeTextAfterEncryption() {
        String[][] data = cipherData();

        return Arrays.stream(data).map(entry -> {
            String text = entry[0];
            String key = entry[1];

            String testName = String.format("Szyfrowanie zmienia tekst: '%s' z kluczem '%s'", text, key);

            return dynamicTest(testName, () -> {
                String encrypted = VigenereCipher.encrypt(text, key);

                assertNotEquals(text, encrypted);
            });
        });
    }
}