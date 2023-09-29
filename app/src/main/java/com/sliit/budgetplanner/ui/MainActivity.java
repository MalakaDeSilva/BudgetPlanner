package com.sliit.budgetplanner.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                income.startAnimation(animation);

                Intent intent = new Intent(MainActivity.this, AddIncome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cred_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                cred_limit.startAnimation(animation);

                Intent intent = new Intent(MainActivity.this, SpendingLimit.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        history_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                history_reports.startAnimation(animation);

                Intent histnrep = new Intent(MainActivity.this, HistoryandReports.class);
                startActivity(histnrep);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                expenses.startAnimation(animation);

                Intent expenses = new Intent(MainActivity.this, Expenses.class);
                startActivity(expenses);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}