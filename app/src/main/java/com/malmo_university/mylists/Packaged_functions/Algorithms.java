package com.malmo_university.mylists.Packaged_functions;

/**
 * This function is meant to contain all algorithms that I develop during normal development.
 * The functions must be declared as static if they must be included in this class.
 * <p/>
 * Created by Saeed on 05-01-15.
 */
public class Algorithms {

    private static final String TAG = "Algorithms";

    public static String transformEmailToKey(String email) {
        if (email != null && email.contains("@")) {
            email = email.replace("@", "-");
            email = email.replace(".", "-");
        }
        return email;
    }

    protected static String removeAtSignFromEmail(String email) {
        String[] a = new String[2];
        if (email != null && email.contains("@")) {
            a = email.split("@");
        }
        return a[0] + a[1];
    }

    public static String removeAllSpacesBeforeAndAfterString(String sequence) {
        int beginning = 0;
        int end = sequence.length();
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == ' ') {
                beginning++;
            } else {
                break;
            }
        }
        if (beginning != end) {
            for (int i = sequence.length() - 1; i > 0; i--) {
                if (sequence.charAt(i) == ' ') {
                    end--;
                } else {
                    break;
                }
            }
        }
        sequence = sequence.substring(beginning, end);
        return sequence;
    }

    public static String removeAllSpaces(String sequence) {
        if (sequence.contains(" ")) {
            sequence = sequence.replaceAll(" ", "");
        }
        return sequence;
    }

}
