package com.jignesh.messminder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button btnRegister;
    TextView tvLoginHere;
    EditText usernameEditText, emailEditText, blockEditText, enrollmentEditText, passwordEditText, confirmPasswordEditText, phonenoEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DBHelper(this);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginHere = findViewById(R.id.tv_login_here);

        usernameEditText = findViewById(R.id.et_name);
        emailEditText = findViewById(R.id.et_email);
        blockEditText = findViewById(R.id.et_block);
        enrollmentEditText = findViewById(R.id.et_enrollment);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        phonenoEditText = findViewById(R.id.phone_num);

        usernameEditText.setFilters(new InputFilter[]{new TextOnlyInputFilter()});

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String block = blockEditText.getText().toString().trim();
                String enrollment = enrollmentEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String phoneno = phonenoEditText.getText().toString().trim();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.insertUser(username, email, enrollment, block, password,phoneno);

                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

        tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}