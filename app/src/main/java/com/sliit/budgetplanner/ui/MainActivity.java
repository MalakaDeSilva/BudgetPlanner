package com.sliit.budgetplanner.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.sliit.budgetplanner.R;

public class MainActivity extends AppCompatActivity {
    private LinearLayout income, cred_limit, history_reports, expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        income = findViewById(R.id.income);
        cred_limit = findViewById(R.id.spend_limit);
        history_reports = findViewById(R.id.history_reports);
        expenses = findViewById(R.id.expenses);

        income.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, Incomes.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        cred_limit.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, Analysis.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        history_reports.setOnClickListener(view -> {

            Intent histnrep = new Intent(MainActivity.this, HistoryandReports.class);
            startActivity(histnrep);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        expenses.setOnClickListener(view -> {

            Intent expenses = new Intent(MainActivity.this, Expenses.class);
            startActivity(expenses);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}