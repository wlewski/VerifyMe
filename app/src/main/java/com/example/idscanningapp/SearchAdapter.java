package com.example.idscanningapp;

import android.content.Context;
import android.util.Log;
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

public class SearchAdapter extends RecyclerView.Adapter implements Filterable {
    // SearchAdapter is a class that will deal with the binding with the RecyclerView that resides within
    // the Search Fragment/Activity.

    private static final String TAG = "searchAdapter";
    private ArrayList<Customer> customerData;
    private View.OnClickListener onClickListener;
    private Context parentContext;
    private Filter fRecords;

    public void setCustomerData(ArrayList<Customer> customers) { customerData = customers; }
    public void setOnClickListener(View.OnClickListener itemClickListener) { onClickListener = itemClickListener; }

    public SearchAdapter(ArrayList<Customer> customerArrayList, Context context) {
        customerData = customerArrayList;
        parentContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Retrieve and Inflate the search_list_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
        Customer customer = customerData.get(position);

        // Bind to Screen
        searchViewHolder.getTvFirstName().setText(customer.getFirstName());
        searchViewHolder.getTvLastName().setText(customer.getLastName());
        searchViewHolder.getTvDLN().setText(customer.getDLN());
    }

    @Override
    public int getItemCount() {
        if (customerData == null) {
            return 0;
        }
        return customerData.size();
    }

    //region Filter for Search Functionality

    // Check if a filter exists,
    // if not set a new filter from RecordFilter Class to the fRecords variable
    // return the filter
    @Override
    public Filter getFilter() {
        if (fRecords == null) {
            Log.d(TAG, "getFilter: ");
            fRecords = new RecordFilter();
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
                results.values = customerData;
                results.count = customerData.size();
            }
            else {
                // Else we will filter through customers
                // Create a new array of customers that will be used to store the filtered customer data
                ArrayList<Customer> fRecords = new ArrayList<Customer>();

                // For loop to check if the customer firstname, lastname, or drivers license contains either
                // the data within the constraint, if it does then the customer is added to fRecords
                for (Customer c : customerData) {
                    try {
                        if (c.getFirstName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()) ||
                            c.getMiddleName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()) ||
                            c.getLastName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()) ||
                            c.getDLN().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {

                            Log.d(TAG, "performFiltering - The Constraint: " + constraint + " exists");
                            fRecords.add(c);
                        } else
                            Log.d(TAG, "The Constraint: " + constraint + " does not exist");
                    }
                    catch (Exception e) {
                        Log.d(TAG, "performFiltering: ERROR OCCURRED WHILE Checking Filter: " + e.getMessage()  );
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
            customerData = (ArrayList<Customer>) results.values;
            notifyDataSetChanged();
        }
    }
    //endregion

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        // SearchViewHolder is a class that will deal with the creation of the list item for the search data

        private TextView tvFirstName;
        private TextView tvLastName;
        private TextView tvDLN;

        public TextView getTvFirstName() { return tvFirstName; }
        public TextView getTvLastName() { return tvLastName; }
        public TextView getTvDLN() { return tvDLN; }

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFirstName = itemView.findViewById(R.id.SearchListItem_tvFirstName);
            tvLastName = itemView.findViewById(R.id.SearchListItem_tvLastName);
            tvDLN = itemView.findViewById(R.id.SearchListItem_tvDLN);

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }
    }


}
