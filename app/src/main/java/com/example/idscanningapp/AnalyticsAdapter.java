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

import com.example.idscanningapp.Models.CustomerCheckinRecord;

import java.util.ArrayList;

public class AnalyticsAdapter extends RecyclerView.Adapter implements Filterable {

    private Filter fRecords;
    private Context parent;
    private ArrayList<CustomerCheckinRecord> checkinRecordData;
    public static final String TAG = "analyticsAdapter";
    public void setCheckinRecordData(ArrayList<CustomerCheckinRecord> arrayCheckinData) { checkinRecordData = arrayCheckinData; }

    public AnalyticsAdapter(ArrayList<CustomerCheckinRecord> arrayCustomerCheckinRecords, Context context) {
        checkinRecordData = arrayCustomerCheckinRecords;
        parent = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_analytics, parent, false);
        return new AnalyticsAdapter.AnalyticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AnalyticsViewHolder analyticsViewHolder = (AnalyticsViewHolder) holder;
        CustomerCheckinRecord checkinRecord = checkinRecordData.get(position);

        //analyticsViewHolder.getTvCustomerInitials().setText(checkinRecord.getCustomerInitials());
        analyticsViewHolder.getTvCustomerInitials().setText("Customer Initials: ");
        analyticsViewHolder.getTvDate().setText(("Checkin Date: " + checkinRecord.getCheckinDate()));
    }

    @Override
    public int getItemCount() {
        if (checkinRecordData == null){
            return 0;
        }
        return checkinRecordData.size();
    }

    public class AnalyticsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerInitials;
        private TextView tvDate;

        public TextView getTvCustomerInitials() { return tvCustomerInitials; }
        public TextView getTvDate() { return tvDate; }

        public AnalyticsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.AnalyticsListItem_tvDate);
            tvCustomerInitials = itemView.findViewById(R.id.AnalyticsListItem_tvCustomerInitials);

            itemView.setTag(this);
        }
    }

    //region Filter for Search Functionality

    // Check if a filter exists,
    // if not set a new filter from RecordFilter Class to the fRecords variable
    // return the filter
    @Override
    public Filter getFilter() {
        if (fRecords == null) {
            fRecords = new AnalyticsAdapter.RecordFilter();
        }
        return fRecords;
    }
    private class RecordFilter extends Filter {
        // Filter Logic
        // takes in a variable called constraint that will be used to create filter
        // returns filtered results
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // Filter Logic
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                // if the constraint equals null or the length is zero then return default customer data
                results.values = checkinRecordData;
                results.count = checkinRecordData.size();
            }
            else {
                // Else we will filter through customers
                // Create a new array of customers that will be used to store the filtered customer data
                ArrayList<CustomerCheckinRecord> fRecords = new ArrayList<CustomerCheckinRecord>();

                // For loop to check if the customer firstname, lastname, or drivers license contains either
                // the data within the constraint, if it does then the customer is added to fRecords
                for (CustomerCheckinRecord r : checkinRecordData) {
                    try {
                        if (r.getCustomerInitials().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()) ||
                            r.getCheckinDate().toString().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                            fRecords.add(r);
                        }
                        //else
                    }
                    catch (Exception e) {

                    }

                    results.values = fRecords;
                    results.count = fRecords.size();
                }
            }

            return results;
        }
        // Will set the customerData equal to the filtered results to refresh the screen
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            checkinRecordData = (ArrayList<CustomerCheckinRecord>) results.values;
            notifyDataSetChanged();
        }
    }
    //endregion
}
