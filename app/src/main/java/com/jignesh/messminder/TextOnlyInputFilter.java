package com.jignesh.messminder;

import android.text.InputFilter;
import android.text.Spanned;

public class TextOnlyInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {

            if (Character.isDigit(source.charAt(i))) {
                return "";
            }
        }
        // If no digits found, return null to accept the input
        return null;
    }
}
