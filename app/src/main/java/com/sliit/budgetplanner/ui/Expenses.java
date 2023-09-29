package com.sliit.budgetplanner.ui;

import android.annotation.SuppressLint;
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
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.viewmodel.ExpensesViewModel;

import java.text.SimpleDateFormat;

public class Expenses extends AppCompatActivity {
    private static final String TAG = Expenses.class.getCanonicalName();
    private RecyclerView mRecyclerView;
    private ExpensesViewModel expensesViewModel;
    private ExpenseAdapter expenseAdapter;
    private FloatingActionButton btnAddExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        btnAddExpenses = findViewById(R.id.btnAddExpenses);
        mRecyclerView = findViewById(R.id.expensesList);

        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);

        expenseAdapter = new ExpenseAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(expenseAdapter);

        expensesViewModel.startDataListener(expenseAdapter);

        btnAddExpenses.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddExpenses.class);
            startActivity(intent);
        });

        setUpItemTouchHelper(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        expenseAdapter.notifyDataSetChanged();
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
                ExpenseAdapter expenseAdapter = (ExpenseAdapter) mRecyclerView.getAdapter();
                Expense expense = expenseAdapter.getExpenseList().get(swipedPosition);

                if (swipeDir == ItemTouchHelper.RIGHT) {
                    new AlertDialog.Builder(context)
                            .setTitle("Remove Expense!")
                            .setMessage("Do you really want to remove the expense?")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) ->
                                    expensesViewModel.deleteExpense(expense)
                            )
                            .setNegativeButton(android.R.string.cancel, (dialog, whichButton) -> {
                            }).show();
                } else if (swipeDir == ItemTouchHelper.LEFT) {
                    Intent intent = new Intent(context, AddExpenses.class);

                    intent.putExtra(Constants.ID, expense.getId());
                    intent.putExtra(Constants.AMOUNT, expense.getAmount());
                    intent.putExtra(Constants.TYPE, expense.getType());
                    if (expense.getDate() != null)
                        intent.putExtra(Constants.DATE, new SimpleDateFormat("dd/MM/yyyy").format(expense.getDate().toDate()));
                    intent.putExtra(Constants.PAYMENT_METHOD, expense.getPaymentMethod());
                    intent.putExtra(Constants.COMMENTS, expense.getComments());

                    startActivity(intent);
                }

                expenseAdapter.notifyDataSetChanged();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}