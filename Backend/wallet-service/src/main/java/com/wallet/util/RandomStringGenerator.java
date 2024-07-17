package com.wallet.util;
import java.security.SecureRandom;
import java.util.Random;

public class RandomStringGenerator {

    // Define the characters that can be used in the random string
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String CHARACTERS = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + DIGITS;

    // Use SecureRandom for better randomness
    private static final Random RANDOM = new SecureRandom();

    public static String generateRandomPassword(int minLength, int maxLength) {
        if (minLength < 8 || maxLength > 30 || minLength > maxLength) {
            throw new IllegalArgumentException("Length must be between 8 and 30 characters.");
        }

        int length = RANDOM.nextInt((maxLength - minLength) + 1) + minLength;

        StringBuilder sb = new StringBuilder(length);

        // Ensure at least one character from each category
        sb.append(UPPERCASE_CHARACTERS.charAt(RANDOM.nextInt(UPPERCASE_CHARACTERS.length())));
        sb.append(LOWERCASE_CHARACTERS.charAt(RANDOM.nextInt(LOWERCASE_CHARACTERS.length())));
        sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        // Fill the rest of the string with random characters
        for (int i = 3; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        // Shuffle the result to avoid predictable positions
        return shuffleString(sb.toString());
    }

    private static String shuffleString(String input) {

        //StringBuilder sb: This is used to build the shuffled string.
        //boolean[] used: This array keeps track of which characters (by index) from the input string have already been added to the StringBuilder.
        StringBuilder sb = new StringBuilder(input.length());
        boolean[] used = new boolean[input.length()];

        while (sb.length() < input.length()) {
            int randomIndex = RANDOM.nextInt(input.length());

            //if (!used[randomIndex]): Checks if the character at randomIndex has already been used.
            //If it has not been used, the character at randomIndex is appended to sb,
            // and used[randomIndex] is set to true to mark it as used.
            if (!used[randomIndex]) {
                sb.append(input.charAt(randomIndex));
                used[randomIndex] = true;
            }
        }
        return sb.toString();
    }

    public static String generateRandomUserName(int minLength, int maxLength) {
        if (minLength < 6 || maxLength > 16 || minLength > maxLength) {
            throw new IllegalArgumentException("Length must be between 6 and 16 characters.");
        }

        int length = RANDOM.nextInt((maxLength - minLength) + 1) + minLength;
        boolean hasLetter;
        boolean hasDigit;
        StringBuilder sb;

        do {
            sb = new StringBuilder(length);
            hasLetter = false;
            hasDigit = false;

            for (int i = 0; i < length; i++) {
                char randomChar = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
                sb.append(randomChar);
                if (Character.isLetter(randomChar)) {
                    hasLetter = true;
                } else if (Character.isDigit(randomChar)) {
                    hasDigit = true;
                }
            }
        } while (!hasLetter || !hasDigit);

        return sb.toString();
    }

}
