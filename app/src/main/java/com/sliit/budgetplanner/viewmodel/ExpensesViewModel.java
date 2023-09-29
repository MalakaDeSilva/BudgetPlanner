package com.sliit.budgetplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliit.budgetplanner.model.Expense;
import com.sliit.budgetplanner.repository.ExpenseRepository;
import com.sliit.budgetplanner.ui.ExpenseAdapter;
import com.sliit.budgetplanner.util.Constants;

import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference expensesRef = firestore.collection(Constants.COL_EXPENSES);
    private LiveData<List<Expense>> expensesLiveData;
    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        this.expensesLiveData = ExpenseRepository.getInstance().getExpensesLiveData(expensesRef);
    }

    public LiveData<List<Expense>> getExpensesLiveData() {
        return expensesLiveData;
    }

    public void startDataListener(ExpenseAdapter expenseAdapter) {
        ExpenseRepository.getInstance().startDataListener(expensesRef, expenseAdapter);
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
}
