package com.sliit.budgetplanner.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.viewmodel.ExpensesViewModel;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryandReports extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Button generate;
    private DatePickerDialog picker;
    private EditText fromDate, toDate;
    private CheckBox chkA4, chkA5, chkPortrait, chkLandscape;

    private ExpensesViewModel expensesViewModel;
    private IncomeViewModel incomeViewModel;

    private Task<QuerySnapshot> incomesTask;
    private Task<QuerySnapshot> expensesTask;

    boolean isIncomesLoaded, isExpensesLoaded = false;
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyand_reports);

        generate = findViewById(R.id.btnGenerate);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        chkA4 = findViewById(R.id.chkA4);
        chkA5 = findViewById(R.id.chkA5);
        chkLandscape = findViewById(R.id.chkLandscape);
        chkPortrait = findViewById(R.id.chkPortrait);

        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);

        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(HistoryandReports.this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            fromDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(HistoryandReports.this,
                    (view, year12, monthOfYear, dayOfMonth) ->
                            toDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12), year, month, day);
            picker.show();
        });

        generate.setOnClickListener(view -> {

            Animation animation = AnimationUtils.loadAnimation(HistoryandReports.this, R.anim.fadein);

            generate.startAnimation(animation);

            incomesTask = incomeViewModel.getIncomes();
            expensesTask = expensesViewModel.getExpenses();

            incomesTask.addOnCompleteListener(task -> {
                incomes.clear();
                if (task.isSuccessful()) {
                    setIncomesLoaded(true);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Income incomeObj = document.toObject(Income.class);
                        incomeObj.setId(document.getId());
                        incomes.add(incomeObj);
                    }
                    generatePdfReport(getPageSize(), getPageOrientation());
                }
            });

            expensesTask.addOnCompleteListener(task -> {
                expenses.clear();
                if (task.isSuccessful()) {
                    setExpensesLoaded(true);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Expense expenseObj = document.toObject(Expense.class);
                        expenseObj.setId(document.getId());
                        expenses.add(expenseObj);
                    }
                    generatePdfReport(getPageSize(), getPageOrientation());
                }
            });
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void generatePdfReport(String pageSize, String orientation) {
        if (isExpensesLoaded() && isIncomesLoaded()) {
            try {
                String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/Budget Planner Reports/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdirs();
                }

                // Create a PDF file
                PdfWriter writer = new PdfWriter(directory_path + "BP_" + Timestamp.now().getSeconds() + ".pdf");
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                if (pageSize.equalsIgnoreCase("A4") && orientation.equalsIgnoreCase("LANDSCAPE"))
                    pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
                else if (pageSize.equalsIgnoreCase("A4") && orientation.equalsIgnoreCase("PORTRAIT"))
                    pdfDocument.setDefaultPageSize(PageSize.A4);
                else if (pageSize.equalsIgnoreCase("A5") && orientation.equalsIgnoreCase("LANDSCAPE"))
                    pdfDocument.setDefaultPageSize(PageSize.A5.rotate());
                else if (pageSize.equalsIgnoreCase("A5") && orientation.equalsIgnoreCase("PORTRAIT"))
                    pdfDocument.setDefaultPageSize(PageSize.A5);

                // Create a table
                Table incomeTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1}));
                incomeTable.setWidth(UnitValue.createPercentValue(100));

                // Add a header row to the table
                incomeTable.addCell(createCell("Income", true));
                incomeTable.addCell(createCell("Date", true));
                incomeTable.addCell(createCell("Amount (LKR)", true));

                incomes.forEach(income -> {
                    try {
                        incomeTable.addCell(createCell(income.getType(), false));
                        incomeTable.addCell(createCell(formatDate(income.getDate(), "/"), false));
                        incomeTable.addCell(createCell(String.valueOf(income.getAmount()), false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // Create a table
                Table expensesTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1}));
                expensesTable.setWidth(UnitValue.createPercentValue(100));

                // Add a header row to the table
                expensesTable.addCell(createCell("Expenses", true));
                expensesTable.addCell(createCell("Date", true));
                expensesTable.addCell(createCell("Amount (LKR)", true));

                expenses.forEach(expense -> {
                    try {
                        expensesTable.addCell(createCell(expense.getType(), false));
                        expensesTable.addCell(createCell(formatDate(expense.getDate(), "/"), false));
                        expensesTable.addCell(createCell(String.valueOf(expense.getAmount()), false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // Add the table to the document
                document.add(new Paragraph("Incomes"));
                document.add(incomeTable);
                document.add(new Paragraph("Expenses"));
                document.add(expensesTable);

                // Close the document
                document.close();
                Log.i("PDF", "Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Cell createCell(String content, boolean isHeader) throws IOException {
        Cell cell = new Cell();
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Set the font (you can customize the font and size)
        PdfFont font = PdfFontFactory.createFont();
        cell.setFont(font);

        if (isHeader) {
            cell.setBackgroundColor(new DeviceRgb(192, 192, 192)); // Gray background for headers
        }

        // Create a paragraph and add it to the cell
        Paragraph paragraph = new Paragraph(content);
        cell.add(paragraph);

        return cell;
    }

    private String formatDate(Timestamp date, String seperator) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());

        String sDate = calendar.get(Calendar.YEAR) + seperator + calendar.get(Calendar.MONTH) + seperator + calendar.get(Calendar.DAY_OF_MONTH);
        return sDate;
    }

    private String getPageSize() {
        if (chkA5.isChecked())
            return "A5";
        else
            return "A4";
    }

    private String getPageOrientation() {
        if (chkLandscape.isChecked())
            return "LANDSCAPE";
        else
            return "PORTRAIT";
    }

    public boolean isIncomesLoaded() {
        return isIncomesLoaded;
    }

    public void setIncomesLoaded(boolean incomesLoaded) {
        isIncomesLoaded = incomesLoaded;
    }

    public boolean isExpensesLoaded() {
        return isExpensesLoaded;
    }

    public void setExpensesLoaded(boolean expensesLoaded) {
        isExpensesLoaded = expensesLoaded;
    }
}