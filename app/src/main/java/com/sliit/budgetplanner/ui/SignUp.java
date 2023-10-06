package com.sliit.budgetplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.ui.auth.AuthHelper;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private AuthHelper authHelper;
    private Button btnLogin, btnSignUp;
    private EditText email, password, confirmEmail, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        authHelper = new AuthHelper(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(view -> startActivity(new Intent(this, Login.class)));

        email = findViewById(R.id.email);
        confirmEmail = findViewById(R.id.cnfrmEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.cnfrmPassword);

        btnSignUp.setOnClickListener(view -> {
            String _email = email.getText().toString();
            String _password = password.getText().toString();
            String _confirmPassword = confirmPassword.getText().toString();
            String _confirmEmail = confirmEmail.getText().toString();

            boolean emailVerified = !_email.isEmpty() && !_confirmEmail.isEmpty() && _email.equals(_confirmEmail);
            boolean passwordVerified = !_password.isEmpty() && !_confirmPassword.isEmpty() && _password.equals(_confirmPassword);

            if (emailVerified && passwordVerified) {
                authHelper.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(SignUp.this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, Login.class));
                    } else {
                        Toast.makeText(SignUp.this, "Signing up failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(SignUp.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(SignUp.this, "Check email, password again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}