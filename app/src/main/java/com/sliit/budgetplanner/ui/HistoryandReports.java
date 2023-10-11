package com.sliit.budgetplanner.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class HistoryandReports extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Button generate;
    private DatePickerDialog picker;
    private EditText fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyand_reports);

        generate = findViewById(R.id.btnGenerate);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);

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

            generatePdfReport();
        });


        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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

    public static void generatePdfReport() {
        try {
            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/Budget Planner Reports/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }

            // Create a PDF file
            PdfWriter writer = new PdfWriter(directory_path + "sample_report.pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Create a table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add a header row to the table
            table.addCell(createCell("Column 1", true));
            table.addCell(createCell("Column 2", true));
            table.addCell(createCell("Column 3", true));

            // Add data rows to the table
            for (int i = 1; i <= 10; i++) {
                table.addCell(createCell("Row " + i + ", Cell 1", false));
                table.addCell(createCell("Row " + i + ", Cell 2", false));
                table.addCell(createCell("Row " + i + ", Cell 3", false));
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Cell createCell(String content, boolean isHeader) throws IOException {
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
}