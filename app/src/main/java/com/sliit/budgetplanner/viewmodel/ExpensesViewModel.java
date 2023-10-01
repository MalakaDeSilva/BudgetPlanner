package com.sliit.budgetplanner.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.repository.ExpenseRepository;
import com.sliit.budgetplanner.ui.adapters.ExpenseAdapter;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.util.FBUtil;

import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {
    private FirebaseFirestore firestore = FBUtil.getInstance().getDB();
    private CollectionReference expensesRef = firestore.collection(Constants.COL_EXPENSES);
    private LiveData<List<Expense>> expensesLiveData;
    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        this.expensesLiveData = ExpenseRepository.getInstance().getExpensesLiveData(expensesRef);
    }

    public LiveData<List<Expense>> getExpensesLiveData() {
        return expensesLiveData;
    }

    public void startDataListener(Context context, ExpenseAdapter expenseAdapter) {
        ExpenseRepository.getInstance().startDataListener(context, expensesRef, expenseAdapter);
    }

    public void addExpense(Expense expense) {
        ExpenseRepository.getInstance().addExpense(expensesRef, expense);
    }

    public void updateExpense(Expense expense) {
        ExpenseRepository.getInstance().updateExpense(expensesRef, expense);
    }

    public void deleteExpense(Expense expense) {
        ExpenseRepository.getInstance().deleteExpense(expensesRef, expense);
    }

    public List<Expense> getExpensesByDateRange(Timestamp startDate, Timestamp endDate) {
        return ExpenseRepository.getInstance().getExpensesByDateRange(expensesRef, startDate, endDate);
    }
}
