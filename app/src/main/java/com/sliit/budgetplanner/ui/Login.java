package com.sliit.budgetplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.ui.auth.AuthHelper;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private AuthHelper authHelper;
    private Button btnLogin, btnSignUp;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        AuthHelper authHelper = new AuthHelper(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(view -> startActivity(new Intent(this, SignUp.class)));

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btnLogin.setOnClickListener(view -> {
            String _email = email.getText().toString();
            String _password = password.getText().toString();

            boolean emailVerified = !_email.isEmpty();
            boolean passwordVerified = !_password.isEmpty();

            if (emailVerified && passwordVerified) {
                authHelper.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(Login.this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, Home.class));
                    } else {
                        Toast.makeText(Login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Login.this, "Email/Password cannot be empty.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}