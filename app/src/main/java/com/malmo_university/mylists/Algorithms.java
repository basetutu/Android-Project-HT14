package com.malmo_university.mylists;

/**
 * This function is meant to contain all algorithms that I develop during normal development.
 * The functions must be declared as static if they must be included in this class.
 *
 * Created by Saeed on 05-01-15.
 */
public class Algorithms {

    protected static String removeAllSpacesBeforeAndAfterString(String sequence) {
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

    protected static String removeAllSpaces(String sequence){
        if (sequence.contains(" ")) {
            sequence = sequence.replaceAll(" ", "");
        }
        return sequence;
    }

}
