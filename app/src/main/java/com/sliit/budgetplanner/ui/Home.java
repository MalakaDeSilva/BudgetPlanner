package com.sliit.budgetplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.viewmodel.ExpensesViewModel;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private LinearLayout income, cred_limit, history_reports, expenses;
    private TextView txtRemainder, txtIncome, txtExpenses;
    private IncomeViewModel incomeViewModel;
    private ExpensesViewModel expensesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        income = findViewById(R.id.income);
        cred_limit = findViewById(R.id.spend_limit);
        history_reports = findViewById(R.id.history_reports);
        expenses = findViewById(R.id.expenses);
        txtRemainder = findViewById(R.id.txtRemainder);
        txtIncome = findViewById(R.id.txtIncome);
        txtExpenses = findViewById(R.id.txtExpenses);

        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);
        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);

        incomeViewModel.getTotalIncome(this, txtIncome);
        expensesViewModel.getTotalExpenses(this, txtExpenses);
        new Handler(getMainLooper()).postDelayed(this::setRemain, 2000);

        income.setOnClickListener(view -> {

            Intent intent = new Intent(Home.this, Incomes.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        cred_limit.setOnClickListener(view -> {

            Intent intent = new Intent(Home.this, Analysis.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        history_reports.setOnClickListener(view -> {

            Intent histnrep = new Intent(Home.this, HistoryandReports.class);
            startActivity(histnrep);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        expenses.setOnClickListener(view -> {

            Intent expenses = new Intent(Home.this, Expenses.class);
            startActivity(expenses);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        incomeViewModel.getTotalIncome(this, txtIncome);
        expensesViewModel.getTotalExpenses(this, txtExpenses);

        new Handler(getMainLooper()).postDelayed(this::setRemain, 2000);
    }

    private void setRemain() {
        String sIncome = txtIncome.getText().toString();
        String sExpense = txtExpenses.getText().toString();

        try {
            float fIncome = Float.parseFloat(sIncome);
            float fExpense = Float.parseFloat(sExpense);

            float remain = fIncome - fExpense;

            txtRemainder.setText(String.valueOf(remain));
        } catch (Exception e) {
            Log.e(TAG, "Parse error: " + e.getMessage());
        }
    }
}