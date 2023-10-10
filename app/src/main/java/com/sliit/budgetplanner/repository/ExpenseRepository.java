package com.sliit.budgetplanner.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.ui.adapters.ExpenseAdapter;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.util.FBUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private static final String TAG = ExpenseRepository.class.getCanonicalName();
    private static ExpenseRepository instance;
    private String userId;
    public static ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new ExpenseRepository();
        }
        return instance;
    }

    private ExpenseRepository() {
        this.userId = FBUtil.getInstance().getAuth().getCurrentUser().getUid();
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


    public void startDataListener(Context context, CollectionReference expensesRef, ExpenseAdapter expenseAdapter) {
        expensesRef.whereEqualTo(Constants.USER_ID, userId).addSnapshotListener((value, e) -> {
            boolean isOffline = Boolean.FALSE;
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            List<Expense> _expenses = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                isOffline = doc.getMetadata().isFromCache();
                Expense expense = doc.toObject(Expense.class);
                expense.setId(doc.getId());
                _expenses.add(expense);
            }
            expenseAdapter.setExpenseList(_expenses);
            expenseAdapter.notifyDataSetChanged();

            if (isOffline)
                Toast.makeText(context, "Data fetched from offline cache.", Toast.LENGTH_SHORT).show();
        });
    }

    public void addExpense(CollectionReference expensesRef, Expense expense) {
        expense.setUserId(userId);
        expensesRef.add(expense);
    }

    public void updateExpense(CollectionReference expensesRef, Expense expense) {
        expense.setUserId(userId);
        expensesRef.document(expense.getId()).set(expense);
    }

    public void deleteExpense(CollectionReference expensesRef, Expense expense) {
        expensesRef.document(expense.getId()).delete();
    }

    public List<Expense> getExpensesByDateRange(CollectionReference expensesRef, Timestamp startDate, Timestamp endDate) {
        List<Expense> expenses = new ArrayList<>();

        Query query = expensesRef.whereEqualTo(Constants.USER_ID, userId)
                .where(Filter.lessThan(Constants.DATE, endDate))
                .where(Filter.greaterThan(Constants.DATE, startDate));

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Expense expenseObj = document.toObject(Expense.class);
                    expenseObj.setId(document.getId());
                    expenses.add(expenseObj);
                }
            }
        });

        return expenses;
    }
}
