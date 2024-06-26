package com.jignesh.messminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    private EditText monMenu, tueMenu, wedMenu, thuMenu, friMenu, satMenu, sunMenu;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        monMenu = findViewById(R.id.monmenu);
        tueMenu = findViewById(R.id.tuemenu);
        wedMenu = findViewById(R.id.wedmenu);
        thuMenu = findViewById(R.id.thumenu);
        friMenu = findViewById(R.id.frimenu);
        satMenu = findViewById(R.id.satmenu);
        sunMenu = findViewById(R.id.sunmenu);

        DBHelper db = new DBHelper(this);

        monMenu.setText(db.getSetting("monday"));
        tueMenu.setText(db.getSetting("tuesday"));
        wedMenu.setText(db.getSetting("wednesday"));
        thuMenu.setText(db.getSetting("thursday"));
        friMenu.setText(db.getSetting("friday"));
        satMenu.setText(db.getSetting("saturday"));
        sunMenu.setText(db.getSetting("sunday"));

        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String monday = monMenu.getText().toString();
            String tuesday = tueMenu.getText().toString();
            String wednesday = wedMenu.getText().toString();
            String thursday = thuMenu.getText().toString();
            String friday = friMenu.getText().toString();
            String saturday = satMenu.getText().toString();
            String sunday = sunMenu.getText().toString();

            db.updateSetting("monday", monday);
            db.updateSetting("tuesday",tuesday);
            db.updateSetting("wednesday",wednesday);
            db.updateSetting("thursday",thursday);
            db.updateSetting("friday",friday);
            db.updateSetting("saturday",saturday);
            db.updateSetting("sunday",sunday);

            Toast.makeText(this,"Schedule change successfully", Toast.LENGTH_SHORT).show();
        });
    }
}