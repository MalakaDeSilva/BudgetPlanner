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
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.viewmodel.ExpensesViewModel;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class AddExpenses extends AppCompatActivity {
    private static final String TAG = AddExpenses.class.getCanonicalName();
    private ExpensesViewModel expensesViewModel;
    EditText editDate, amount, comments;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    Spinner spinnerPurpose, spinnerPayment;
    ImageButton selectDate;
    Button btnSave;
    Expense expense = null;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);

        selectDate = findViewById(R.id.calimgbtn);
        editDate = findViewById(R.id.editDate);
        spinnerPurpose = findViewById(R.id.spinpurpose);
        spinnerPayment = findViewById(R.id.spinpayment);
        amount = findViewById(R.id.editAmount);
        comments = findViewById(R.id.editComment);
        btnSave = findViewById(R.id.btnsave);


        try {
            expense = getExpenseFromIntent(getIntent());
        } catch (ParseException e) {
            Log.e(TAG, "Parse Intent error: " + e.getMessage());
        }

        Expense finalExpense = expense;
        selectDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();

            if (finalExpense != null && finalExpense.getDate() != null) {
                calendar.setTime(finalExpense.getDate().toDate());
            }

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(AddExpenses.this,
                    (datePicker, year, month, day) -> editDate.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
            datePickerDialog.show();
        });

        ArrayAdapter<CharSequence> adapterPurpose = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        adapterPurpose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurpose.setAdapter(adapterPurpose);

        ArrayAdapter<CharSequence> adapterPayment = ArrayAdapter.createFromResource(this, R.array.payment_method, android.R.layout.simple_spinner_item);
        adapterPayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(adapterPayment);

        if (expense != null)
            setValues(expense);

        btnSave.setOnClickListener(view -> {

            Expense _expense = null;
            try {
                _expense = new Expense(Float.parseFloat(amount.getText().toString()),
                        spinnerPurpose.getSelectedItem().toString(),
                        spinnerPayment.getSelectedItem().toString(),
                        comments.getText().toString(),
                        new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(editDate.getText().toString()))));

                if (expense == null)
                    expensesViewModel.addExpense(_expense);
                else {
                    _expense.setId(expense.getId());
                    expensesViewModel.updateExpense(_expense);
                }

            } catch (ParseException e) {
                Log.e(TAG, "Add expenses error: " + e.getMessage());
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private Expense getExpenseFromIntent(Intent intent) throws ParseException {
        Expense expense = null;
        Bundle extras = intent.getExtras();

        if (extras != null) {
            expense = new Expense();

            for (String key : extras.keySet()) {
                switch (key) {
                    case Constants.ID:
                        expense.setId(extras.getString(key));
                        break;
                    case Constants.AMOUNT:
                        expense.setAmount(extras.getFloat(key));
                        break;
                    case Constants.DATE:
                        expense.setDate(new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(extras.get(key).toString()))));
                        break;
                    case Constants.TYPE:
                        expense.setType(extras.getString(key));
                        break;
                    case Constants.PAYMENT_METHOD:
                        expense.setPaymentMethod(extras.getString(key));
                        break;
                    case Constants.COMMENTS:
                        expense.setComments(extras.getString(key));
                        break;
                }
            }
        }

        return expense;
    }

    private void setValues(Expense expense) {
        ArrayList<String> purposes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.purpose)));
        ArrayList<String> paymentMethods = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.payment_method)));

        if (editDate != null && expense.getDate() != null)
            editDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(expense.getDate().toDate()));

        if (amount != null)
            amount.setText(String.valueOf(expense.getAmount()));

        if (spinnerPurpose != null)
            spinnerPurpose.setSelection(purposes.indexOf(expense.getType()));

        if (spinnerPayment != null)
            spinnerPayment.setSelection(paymentMethods.indexOf(expense.getPaymentMethod()));

        if (comments != null)
            comments.setText(expense.getComments());
    }
}