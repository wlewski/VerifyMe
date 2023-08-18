package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack;
import com.example.idscanningapp.Models.Customer;
import com.example.idscanningapp.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.UUID;

public class SearchFragment extends Fragment {

    public static final String TAG = "mainActivity";

    private ArrayList<Customer> customers;
    private Customer selectedCustomer;
    private String token;
    private String username;
    private FragmentSearchBinding binding; // Need to have a binding in order to find IDs within the fragments
    private RecyclerView searchResults;
    private SearchAdapter searchAdapter;
    private String searchString; // For the Search Feature

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Grabbing the position of the item clicked within the RecyclingView and the UUID of the Customer selected
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            UUID customerID = customers.get(position).getCustomerID();

            // Creation of a new Activity that will display Customer Information
            Intent intent = new Intent(view.getContext(), CustomerInfoActivity.class);
            intent.putExtra("customerID", customerID.toString()); // Allows the passing of data between activities
            intent.putExtra("token", token);
            intent.putExtra("username", username);

            startActivity(intent);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        token = bundle.getString("token");
        username = bundle.getString("username");

        return binding.getRoot();
    }
    @Override
    public void onResume(){
        super.onResume();
        try {
            // REST Client GET Request to obtain all customer information within the database
            RestClient.executeCustomerGetRequest(MainActivity.VERIFYMEAPI + "customer/", token, this.getActivity(), new VolleyCallBack() {
                @Override
                public void onSuccess(ArrayList<Customer> results) {
                    customers = results; // Returned Customer Data
                    RebindSearch(); // Binding Method that will hook all data about each individual customer to a search_list_item
                }
            });
        }
        catch(Exception e) {
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState) {
        initSearchBar();
    }
    private void initSearchBar() {
        SearchView svSearchBar = binding.SearchPageSvSearchBar;

        svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Check to see if the searchString already has data within it
                // If it does then set the CustomerData setter within searchAdapter back to the default customer data
                if (searchString != null || searchString != "")
                    searchAdapter.setCustomerData(customers);

                searchAdapter.getFilter().filter(s); // Apply filter within the Search Adapter
                searchString = s; // Set the searchString equal to the string we are querying
                return false;
            }
        });
    }
    public void RebindSearch(){
        searchResults = binding.SearchPageRvSearchResults; // Finding the RecyclerView and setting the variable equal to it

        // binding RecyclerView to Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        searchResults.setLayoutManager(layoutManager);

        // Creating a new search adapter to hold customer data in with onclick responses for each item
        searchAdapter = new SearchAdapter(customers, this.getActivity());
        searchAdapter.setOnClickListener(onItemClickListener);

        // Setting the adapter for the RecyclerView equal to the search adapter we created
        searchResults.setAdapter(searchAdapter);
    }
}
