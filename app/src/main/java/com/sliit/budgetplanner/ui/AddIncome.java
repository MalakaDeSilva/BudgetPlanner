package com.sliit.budgetplanner.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.Timestamp;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class AddIncome extends AppCompatActivity {
    private static final String TAG = AddIncome.class.getCanonicalName();
    private IncomeViewModel incomeViewModel;
    EditText editDate, amount, comments, purpose;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    Spinner spinnerPayment;
    ImageButton selectDate;
    Button btnSave;
    Income income = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);

        selectDate = findViewById(R.id.calimgbtn);
        editDate = findViewById(R.id.editDate);
        purpose = findViewById(R.id.editpurpose);
        spinnerPayment = findViewById(R.id.spinpayment);
        amount = findViewById(R.id.editAmount);
        comments = findViewById(R.id.editComment);
        btnSave = findViewById(R.id.btnsave);

        try {
            income = getIncomeFromIntent(getIntent());
        } catch (ParseException e) {
            Log.e(TAG, "Parse Intent error: " + e.getMessage());
        }

        Income finalIncome = income;
        selectDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();

            if (finalIncome != null && finalIncome.getDate() != null) {
                calendar.setTime(finalIncome.getDate().toDate());
            }

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(AddIncome.this,
                    (datePicker, year, month, day) -> editDate.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
            datePickerDialog.show();
        });

        ArrayAdapter<CharSequence> adapterPayment = ArrayAdapter.createFromResource(this, R.array.payment_method, android.R.layout.simple_spinner_item);
        adapterPayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(adapterPayment);

        if (income != null)
            setValues(income);

        btnSave.setOnClickListener(view -> {

            Income _income = null;
            try {
                _income = new Income(Float.parseFloat(amount.getText().toString()),
                        purpose.getText().toString(),
                        spinnerPayment.getSelectedItem().toString(),
                        comments.getText().toString(),
                        new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(editDate.getText().toString()))));

                if (income == null)
                    incomeViewModel.addIncome(_income);
                else {
                    _income.setId(income.getId());
                    incomeViewModel.updateIncome(_income);
                }

            } catch (ParseException e) {
                Log.e(TAG, "Add incomes error: " + e.getMessage());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private Income getIncomeFromIntent(Intent intent) throws ParseException {
        Income income = null;
        Bundle extras = intent.getExtras();

        if (extras != null) {
            income = new Income();

            for (String key : extras.keySet()) {
                switch (key) {
                    case Constants.ID:
                        income.setId(extras.getString(key));
                        break;
                    case Constants.AMOUNT:
                        income.setAmount(extras.getFloat(key));
                        break;
                    case Constants.DATE:
                        income.setDate(new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(extras.get(key).toString()))));
                        break;
                    case Constants.TYPE:
                        income.setType(extras.getString(key));
                        break;
                    case Constants.PAYMENT_METHOD:
                        income.setPaymentMethod(extras.getString(key));
                        break;
                    case Constants.COMMENTS:
                        income.setComments(extras.getString(key));
                        break;
                }
            }
        }

        return income;
    }

    private void setValues(Income income) {
        ArrayList<String> paymentMethods = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.payment_method)));

        if (editDate != null && income.getDate() != null)
            editDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(income.getDate().toDate()));

        if (amount != null)
            amount.setText(String.valueOf(income.getAmount()));

        if (purpose != null)
            purpose.setText(income.getType());

        if (spinnerPayment != null)
            spinnerPayment.setSelection(paymentMethods.indexOf(income.getPaymentMethod()));

        if (comments != null)
            comments.setText(income.getComments());
    }
}