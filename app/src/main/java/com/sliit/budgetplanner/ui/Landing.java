package com.sliit.budgetplanner.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.sliit.budgetplanner.R;

import java.util.Objects;

public class Landing extends AppCompatActivity {

    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Objects.requireNonNull(getSupportActionBar()).hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        getStarted = findViewById(R.id.getStarted);

        getStarted.setOnClickListener(view -> startActivity(new Intent(this, Login.class)));

        View ovalView = findViewById(R.id.oval_view);
        LinearLayout landingTxt = findViewById(R.id.landingTxt);

        ovalView.setPivotX(0);
        ovalView.setPivotX(0);

        ObjectAnimator scaleAnimator = new ObjectAnimator();

        scaleAnimator.setTarget(ovalView);
        scaleAnimator.setPropertyName("scaleX");
        scaleAnimator.setPropertyName("scaleY");
        scaleAnimator.setFloatValues(0f, 1f);
        scaleAnimator.setDuration(600);
        scaleAnimator.start();

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(landingTxt, "alpha", 0f, 1f);

        alphaAnimation.setDuration(700);
        alphaAnimation.setStartDelay(700);
        alphaAnimation.start();
    }
}