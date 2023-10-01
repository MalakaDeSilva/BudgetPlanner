package com.sliit.budgetplanner.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Income;

import java.util.List;

public class IncomeAdapter  extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>{
    private List<Income> incomesList;

    public List<Income> getIncomesList() {
        return incomesList;
    }

    public void setIncomesList(List<Income> incomesList) {
        this.incomesList = incomesList;
    }

    @NonNull
    @Override
    public IncomeAdapter.IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new IncomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.IncomeViewHolder holder, int position) {
        Income income = incomesList.get(position);

        holder.incomeTitle.setText(income.getType());
    }

    @Override
    public int getItemCount() {
        return incomesList != null ? incomesList.size() : 0;
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView incomeTitle;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.incomeTitle = itemView.findViewById(R.id.title);
        }
    }
}
