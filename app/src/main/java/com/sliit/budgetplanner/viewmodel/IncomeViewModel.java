package com.sliit.budgetplanner.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.repository.IncomeRepository;
import com.sliit.budgetplanner.ui.adapters.IncomeAdapter;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.util.FBUtil;

import java.util.ArrayList;
import java.util.List;

public class IncomeViewModel extends AndroidViewModel {
    private FirebaseFirestore firestore = FBUtil.getInstance().getDB();
    private CollectionReference incomesRef = firestore.collection(Constants.COL_INCOMES);
    private LiveData<List<Income>> incomesLiveData;

    public IncomeViewModel(@NonNull Application application) {
        super(application);
        this.incomesLiveData = IncomeRepository.getInstance().getIncomesLiveData(incomesRef);
    }

    public LiveData<List<Income>> getIncomesLiveData() {
        return incomesLiveData;
    }

    public void startDataListener(Context context, IncomeAdapter incomeAdapter) {
        IncomeRepository.getInstance().startDataListener(context, incomesRef, incomeAdapter);
    }

    public void addIncome(Income income) {
        IncomeRepository.getInstance().addIncome(incomesRef, income);
    }

    public void updateIncome(Income income) {
        IncomeRepository.getInstance().updateIncome(incomesRef, income);
    }

    public void deleteIncome(Income income) {
        IncomeRepository.getInstance().deleteIncome(incomesRef, income);
    }

    public List<Income> getIncomeByDateRange(Timestamp startDate, Timestamp endDate) {
        return IncomeRepository.getInstance().getIncomeByDateRange(incomesRef, startDate, endDate);
    }

    public void getTotalIncome(Context context, TextView txtTotal) {
        IncomeRepository.getInstance().getTotalIncome(context, incomesRef, txtTotal);
    }

    public Task<QuerySnapshot> getIncomes() {
        return IncomeRepository.getInstance().getIncomes(incomesRef);
    }
}
