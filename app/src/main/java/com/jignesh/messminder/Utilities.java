package com.jignesh.messminder;

import android.content.Context;
import android.content.SharedPreferences;

public class Utilities {

    public static void storeData(Context context, String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences("EmailPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();

        myEditor.putString("email", email);
        myEditor.apply();
    }

    public static String getEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("EmailPreference", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        return email;
    }
}
