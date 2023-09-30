package com.sliit.budgetplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.repository.IncomeRepository;
import com.sliit.budgetplanner.ui.IncomeAdapter;
import com.sliit.budgetplanner.util.Constants;

import java.util.List;

public class IncomeViewModel extends AndroidViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference incomesRef = firestore.collection(Constants.COL_INCOMES);
    private LiveData<List<Income>> incomesLiveData;

    public IncomeViewModel(@NonNull Application application) {
        super(application);
        this.incomesLiveData = IncomeRepository.getInstance().getIncomesLiveData(incomesRef);
    }

    public LiveData<List<Income>> getIncomesLiveData() {
        return incomesLiveData;
    }

    public void startDataListener(IncomeAdapter incomeAdapter) {
        IncomeRepository.getInstance().startDataListener(incomesRef, incomeAdapter);
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
}
