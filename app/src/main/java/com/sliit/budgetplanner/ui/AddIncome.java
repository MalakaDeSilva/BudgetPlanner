package com.sliit.budgetplanner.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.repository.IncomeRepository;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.util.FBUtil;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddIncome extends AppCompatActivity {
    private static final String TAG = AddIncome.class.getCanonicalName();
    private static final int pic_id = 123;
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
    ImageButton btnCapture;
    Income income = null;
    Bitmap photo = null;
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
        btnCapture = findViewById(R.id.btnCapture);

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
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            IncomeRepository.getInstance().uploadImage(photo, String.valueOf(Timestamp.now().getSeconds())).addOnFailureListener(exception -> {
                progressDialog.dismiss();
            }).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                StorageReference fileRef = taskSnapshot.getMetadata().getReference();

                Income _income = null;
                try {
                    _income = new Income(Float.parseFloat(amount.getText().toString()),
                            purpose.getText().toString(),
                            spinnerPayment.getSelectedItem().toString(),
                            comments.getText().toString(),
                            new Timestamp(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(editDate.getText().toString()))));

                    if (fileRef != null)
                        _income.setFileRef(fileRef.toString());

                    if (income == null)
                        incomeViewModel.addIncome(_income);
                    else {
                        _income.setId(income.getId());
                        incomeViewModel.updateIncome(_income);
                    }

                } catch (ParseException e) {
                    Log.e(TAG, "Add incomes error: " + e.getMessage());
                }
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            });
        });

        btnCapture.setOnClickListener(view -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            photo = (Bitmap) data.getExtras().get("data");
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}