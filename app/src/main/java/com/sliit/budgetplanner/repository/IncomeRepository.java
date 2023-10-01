package com.sliit.budgetplanner.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.ui.adapters.IncomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class IncomeRepository {
    private static final String TAG = IncomeRepository.class.getCanonicalName();
    private static IncomeRepository instance;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static IncomeRepository getInstance() {
        if (instance == null) {
            instance = new IncomeRepository();
        }
        return instance;
    }

    public LiveData<List<Income>> getIncomesLiveData(CollectionReference incomesRef) {
        final MutableLiveData<List<Income>> data = new MutableLiveData<>();
        incomesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Income> incomes = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Income incomeObj = document.toObject(Income.class);
                    incomeObj.setId(document.getId());
                    incomes.add(incomeObj);
                }
                data.setValue(incomes);
            } else {
                data.setValue(null);
            }
        });

        return data;
    }


    public void startDataListener(CollectionReference incomesRef, IncomeAdapter incomeAdapter) {
        incomesRef.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            List<Income> _incomes = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                Income income = doc.toObject(Income.class);
                income.setId(doc.getId());
                _incomes.add(income);
            }
            incomeAdapter.setIncomesList(_incomes);
            incomeAdapter.notifyDataSetChanged();
        });
    }

    public void addIncome(CollectionReference incomesRef, Income income) {
        incomesRef.add(income);
    }

    public void updateIncome(CollectionReference incomesRef, Income income) {
        incomesRef.document(income.getId()).set(income);
    }

    public void deleteIncome(CollectionReference incomesRef, Income income) {
        incomesRef.document(income.getId()).delete();
    }

    public List<Income> getIncomeByDateRange(CollectionReference incomesRef, Timestamp startDate, Timestamp endDate) {
        List<Income> incomes = new ArrayList<>();

        Query query = incomesRef.where(Filter.lessThan("date", endDate)).where(Filter.greaterThan("date", startDate));

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Income incomeObj = document.toObject(Income.class);
                    incomeObj.setId(document.getId());
                    incomes.add(incomeObj);
                }
            }
        });

        return incomes;
    }
}
