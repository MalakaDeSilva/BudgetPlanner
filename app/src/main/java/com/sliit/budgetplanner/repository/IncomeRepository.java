package com.sliit.budgetplanner.repository;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sliit.budgetplanner.model.Income;
import com.sliit.budgetplanner.ui.adapters.IncomeAdapter;
import com.sliit.budgetplanner.util.Constants;
import com.sliit.budgetplanner.util.FBUtil;

import java.util.ArrayList;
import java.util.List;

public class IncomeRepository {
    private static final String TAG = IncomeRepository.class.getCanonicalName();
    private static IncomeRepository instance;
    private String userId;

    public static IncomeRepository getInstance() {
        if (instance == null) {
            instance = new IncomeRepository();
        }
        return instance;
    }

    private IncomeRepository() {
        this.userId = FBUtil.getInstance().getAuth().getCurrentUser().getUid();
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


    public void startDataListener(Context context, CollectionReference incomesRef, IncomeAdapter incomeAdapter) {
        incomesRef.whereEqualTo(Constants.USER_ID, userId).addSnapshotListener((value, e) -> {
            boolean isOffline = Boolean.FALSE;

            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            List<Income> _incomes = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                isOffline = doc.getMetadata().isFromCache();

                Income income = doc.toObject(Income.class);
                income.setId(doc.getId());
                _incomes.add(income);
            }
            incomeAdapter.setIncomesList(_incomes);
            incomeAdapter.notifyDataSetChanged();

            if (isOffline)
                Toast.makeText(context, "Data fetched from offline cache.", Toast.LENGTH_SHORT).show();
        });
    }

    public void getTotalIncome(Context context, CollectionReference incomesRef, TextView txtTotal) {

        incomesRef.whereEqualTo(Constants.USER_ID, userId).addSnapshotListener((value, e) -> {
            boolean isOffline = Boolean.FALSE;

            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            float total = 0;

            for (QueryDocumentSnapshot doc : value) {
                isOffline = doc.getMetadata().isFromCache();

                Income income = doc.toObject(Income.class);
                total += income.getAmount();
            }

            txtTotal.setText(String.valueOf(total));

            if (isOffline)
                Toast.makeText(context, "Data fetched from offline cache.", Toast.LENGTH_SHORT).show();
        });

    }

    public void addIncome(CollectionReference incomesRef, Income income) {
        income.setUserId(userId);
        incomesRef.add(income);
    }

    public void updateIncome(CollectionReference incomesRef, Income income) {
        income.setUserId(userId);
        incomesRef.document(income.getId()).set(income);
    }

    public void deleteIncome(CollectionReference incomesRef, Income income) {
        incomesRef.document(income.getId()).delete();
    }

    public List<Income> getIncomeByDateRange(CollectionReference incomesRef, Timestamp startDate, Timestamp endDate) {
        List<Income> incomes = new ArrayList<>();

        Query query = incomesRef.whereEqualTo(Constants.USER_ID, userId)
                .where(Filter.lessThan(Constants.DATE, endDate))
                .where(Filter.greaterThan(Constants.DATE, startDate));

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
