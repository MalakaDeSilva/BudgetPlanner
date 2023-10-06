package com.sliit.budgetplanner.ui.auth;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {
    private FirebaseAuth firebaseAuth;
    private Activity activity;

    public AuthHelper(Activity activity) {
        this.activity = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public FirebaseAuth getFbAuth() {
        return firebaseAuth;
    }

}
