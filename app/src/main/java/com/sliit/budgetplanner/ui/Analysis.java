package com.sliit.budgetplanner.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.viewmodel.ExpensesViewModel;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Analysis extends AppCompatActivity {
    private static final String TAG = "Analysis";
    private IncomeViewModel incomeViewModel;
    private ExpensesViewModel expensesViewModel;
    private BarChart chart1, chart2;
    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    private Button fromInc, toInc, applyInc, fromExp, toExp, applyExp;
    private Timestamp fromD, toD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        chart1 = findViewById(R.id.chart1);
        chart2 = findViewById(R.id.chart2);
        fromInc = findViewById(R.id.fromInc);
        toInc = findViewById(R.id.toInc);
        applyInc = findViewById(R.id.applyInc);
        fromExp = findViewById(R.id.fromExp);
        toExp = findViewById(R.id.toExp);
        applyExp = findViewById(R.id.applyExp);

        List<Income> incomes = new ArrayList<>();
        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);

        List<Expense> expenses = new ArrayList<>();
        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);

        createBarChart(chart1, Color.GREEN);
        createBarChart(chart2, Color.RED);

        initializeDatePickers(this, fromInc, toInc);
        initializeDatePickers(this, fromExp, toExp);

        applyInc.setOnClickListener(view -> {
            if (fromD != null) {
                if (toD == null)
                    toD = Timestamp.now();

                incomes.addAll(incomeViewModel.getIncomeByDateRange(fromD, toD));

                fromD = null;
                toD = null;
            }
        });

        applyExp.setOnClickListener(view -> {
            if (fromD != null) {
                if (toD == null)
                    toD = Timestamp.now();

                expenses.addAll(expensesViewModel.getExpensesByDateRange(fromD, toD));

                fromD = null;
                toD = null;
            }
        });
    }

    private void createBarChart(BarChart chart, int color) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        setData(chart, 5, 100, color);
    }

    private void setData(BarChart chart, int count, float range, int color) {
        float start = 1f;
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));
            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val));
            } else {
                values.add(new BarEntry(i, val));
            }
        }
        BarDataSet set1;
        set1 = new BarDataSet(values, "");
        set1.setDrawIcons(false);
        set1.setColor(color);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        chart.setData(data);
    }

    @SuppressLint("SetTextI18n")
    private void initializeDatePickers(Context context, Button from, Button to) {

        calendar = Calendar.getInstance();

        int _year = calendar.get(Calendar.YEAR);
        int _month = calendar.get(Calendar.MONTH);
        int _day = calendar.get(Calendar.DAY_OF_MONTH);

        from.setOnClickListener(view -> {

            datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, day) -> {
                from.setText(day+ "/" + month +"/" + year);
                try {
                    fromD = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(day+ "/" + month +"/" + year)));
                } catch (ParseException e) {
                    Log.e(TAG, "Parse error: " + e.getMessage());
                }
            }, _year, _month, _day);
            datePickerDialog.show();
        });

        to.setOnClickListener(view -> {

            datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, day) -> {
                to.setText( day+ "/" + month +"/" + year);
                try {
                    toD = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(day+ "/" + month +"/" + year)));
                } catch (ParseException e) {
                    Log.e(TAG, "Parse error: " + e.getMessage());
                }
            }, _year, _month, _day);
            datePickerDialog.show();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}