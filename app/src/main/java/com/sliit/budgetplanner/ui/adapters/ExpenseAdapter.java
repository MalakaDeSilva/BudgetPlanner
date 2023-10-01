package com.sliit.budgetplanner.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sliit.budgetplanner.R;
import com.sliit.budgetplanner.model.Expense;

import java.util.Calendar;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expense.getDate().toDate());

        holder.expenseTitle.setText(expense.getType());
        holder.expenseDate.setText(calendar.get(Calendar.DAY_OF_MONTH) +"/" + calendar.get(Calendar.MONTH));
        holder.expenseAmount.setText(String.valueOf(expense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTitle;
        TextView expenseDate;
        TextView expenseAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.expenseTitle = itemView.findViewById(R.id.title);
            this.expenseDate = itemView.findViewById(R.id.date);
            this.expenseAmount = itemView.findViewById(R.id.amount);
        }
    }
}
