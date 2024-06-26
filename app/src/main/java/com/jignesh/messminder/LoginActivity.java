package com.jignesh.messminder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterHere;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterHere = findViewById(R.id.tv_register_here);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.equals("admin@gmail.com") && password.equals("admin")) {
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                } else {
                    if(dbHelper.loginUser(email,password)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Utilities.storeData(LoginActivity.this, email);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this, "Please enter valid email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                finish();
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}