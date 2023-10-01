package com.sliit.budgetplanner.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sliit.budgetplanner.R;

public class HistoryandReports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyand_reports);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}