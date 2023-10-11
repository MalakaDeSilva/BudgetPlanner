package com.sliit.budgetplanner.ui;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.ui.auth.AuthHelper;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private AuthHelper authHelper;
    private Button btnLogin, btnSignUp;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (!checkPermission())
            requestPermission();

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

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE,
                MANAGE_EXTERNAL_STORAGE,
                CAMERA
        }, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {

        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        int permission4 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return permission1 == PackageManager.PERMISSION_GRANTED
                && permission2 == PackageManager.PERMISSION_GRANTED
                && permission3 == PackageManager.PERMISSION_GRANTED
                && permission4 == PackageManager.PERMISSION_GRANTED;
    }
}