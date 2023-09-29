package com.sliit.budgetplanner.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.ui.ExpenseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private static final String TAG = ExpenseRepository.class.getCanonicalName();
    private static ExpenseRepository instance;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private ExpenseRepository() {
        Log.i(TAG, "ExpenseRepository is created.");
    }

    public static ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new ExpenseRepository();
        }
        return instance;
    }

    public LiveData<List<Expense>> getExpensesLiveData(CollectionReference expensesRef) {
        final MutableLiveData<List<Expense>> data = new MutableLiveData<>();
        expensesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Expense> expenses = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Expense expenseObj = document.toObject(Expense.class);
                    expenseObj.setId(document.getId());
                    expenses.add(expenseObj);
                }
                data.setValue(expenses);
            } else {
                data.setValue(null);
            }
        });

        return data;
    }


    public void startDataListener(CollectionReference expensesRef, ExpenseAdapter expenseAdapter) {
        expensesRef.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            List<Expense> _expenses = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                Expense expense = doc.toObject(Expense.class);
                expense.setId(doc.getId());
                _expenses.add(expense);
            }
            expenseAdapter.setExpenseList(_expenses);
            expenseAdapter.notifyDataSetChanged();
        });
    }

    public void addExpense(CollectionReference expensesRef, Expense expense) {
        expensesRef.add(expense);
    }

    public void updateExpense(CollectionReference expensesRef, Expense expense) {
        expensesRef.document(expense.getId()).set(expense);
    }

    public void deleteExpense(CollectionReference expensesRef, Expense expense) {
        expensesRef.document(expense.getId()).delete();
    }
}
