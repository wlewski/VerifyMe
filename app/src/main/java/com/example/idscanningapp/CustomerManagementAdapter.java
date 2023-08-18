package com.example.idscanningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.Models.Customer;

import java.util.ArrayList;

public class CustomerManagementAdapter extends RecyclerView.Adapter implements Filterable {

    // UNNEEDED ADAPTER
    private ArrayList<Customer> customerData;
    private Context parent;
    private View.OnClickListener onClickListener;
    private Filter fRecords;

    public void setCustomerData(ArrayList<Customer> customers) { customerData = customers; }

    public void setOnClickListener(View.OnClickListener itemOnClickListener) { onClickListener = itemOnClickListener; }

    public CustomerManagementAdapter(ArrayList<Customer> customers, Context context) {
        customerData = customers;
        parent = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        return new CustomerManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class CustomerManagementViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFirstName;
        private TextView tvLastName;
        private TextView tvDLN;

        public CustomerManagementViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /*public TextView getTvFirstName() { return tvFirstName; }
        public TextView getTvLastName() { return tvLastName; }
        public TextView getTvDLN() { return tvDLN; }

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFirstName = itemView.findViewById(R.id.SearchListItem_tvFirstName);
            tvLastName = itemView.findViewById(R.id.SearchListItem_tvLastName);
            tvDLN = itemView.findViewById(R.id.SearchListItem_tvDLN);

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }*/
    }
}
