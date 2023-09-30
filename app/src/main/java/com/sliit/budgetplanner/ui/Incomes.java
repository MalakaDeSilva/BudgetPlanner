package com.sliit.budgetplanner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.viewmodel.IncomeViewModel;

import java.text.SimpleDateFormat;

public class Incomes extends AppCompatActivity {
    private static final String TAG = Incomes.class.getCanonicalName();
    private RecyclerView mRecyclerView;
    private IncomeViewModel incomeViewModel;
    private IncomeAdapter incomeAdapter;
    private FloatingActionButton btnAddIncomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        btnAddIncomes = findViewById(R.id.btnAddIncomes);
        mRecyclerView = findViewById(R.id.incomeList);

        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);

        incomeAdapter = new IncomeAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(incomeAdapter);

        incomeViewModel.startDataListener(incomeAdapter);

        btnAddIncomes.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddIncome.class);
            startActivity(intent);
        });

        setUpItemTouchHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        incomeAdapter.notifyDataSetChanged();
    }

    private void setUpItemTouchHelper(Context context) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                IncomeAdapter incomeAdapter = (IncomeAdapter) mRecyclerView.getAdapter();
                Income income = incomeAdapter.getIncomesList().get(swipedPosition);

                if (swipeDir == ItemTouchHelper.RIGHT) {
                    new AlertDialog.Builder(context)
                            .setTitle("Remove Income!")
                            .setMessage("Do you really want to remove the income?")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) ->
                                    incomeViewModel.deleteIncome(income)
                            )
                            .setNegativeButton(android.R.string.cancel, (dialog, whichButton) -> {
                            }).show();
                } else if (swipeDir == ItemTouchHelper.LEFT) {
                    Intent intent = new Intent(context, AddIncome.class);

                    intent.putExtra(Constants.ID, income.getId());
                    intent.putExtra(Constants.AMOUNT, income.getAmount());
                    intent.putExtra(Constants.TYPE, income.getType());
                    if (income.getDate() != null)
                        intent.putExtra(Constants.DATE, new SimpleDateFormat("dd/MM/yyyy").format(income.getDate().toDate()));
                    intent.putExtra(Constants.PAYMENT_METHOD, income.getPaymentMethod());
                    intent.putExtra(Constants.COMMENTS, income.getComments());

                    startActivity(intent);
                }

                incomeAdapter.notifyDataSetChanged();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}