package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack;
import com.example.idscanningapp.Models.Customer;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerManagementActivity extends AppCompatActivity {
    private ArrayList<Customer> customerData;
    private String token;
    private String username;
    private RecyclerView customerResults;
    private SearchAdapter searchAdapter;
    private String searchString;
    public static final String TAG = "customerManagementActivity";
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Grabbing the position of the item clicked within the RecyclingView and the UUID of the Customer selected
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            UUID customerID = customerData.get(position).getCustomerID();

            // Creation of a new Activity that will display Customer Information
            Intent intent = new Intent(view.getContext(), CustomerInfoActivity.class);
            intent.putExtra("customerID", customerID.toString()); // Allows the passing of data between activities
            intent.putExtra("token", token);
            intent.putExtra("username", username);

            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_management);
        setTitle("Customer Management");

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");
        Log.d(TAG, "onCreate: " + token);

        initAddButton();
        initBackButton();
        initCustomerData();
        initSearchBar();
    }
    private void initAddButton() {
        Button btnAdd = findViewById(R.id.CustomerManagement_btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display popup asking if the user would like to either scan or manually add an id
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = inflater.inflate(R.layout.popup_customer_add, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                Button btnScan = popupView.findViewById(R.id.CustomerAddPopup_btnScan);
                Button btnManual = popupView.findViewById(R.id.CustomerAddPopup_btnManual);
                btnScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CustomerManagementActivity.this, ScanningCameraActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("username", username);

                        // Start a scanning activity
                        startActivity(intent);
                    }
                });
                btnManual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Start a Manual Add Activity
                        Intent intent = new Intent(CustomerManagementActivity.this, ManualAddActivity.class);
                        intent.putExtra("token", token);

                        // Start a scanning activity
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private void initBackButton() {
        Button btnBack = findViewById(R.id.CustomerManagement_btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initSearchBar() {
        SearchView svSearchBar = findViewById(R.id.CustomerManagement_svSearchbar);

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
                    searchAdapter.setCustomerData(customerData);

                searchAdapter.getFilter().filter(s); // Apply filter within the Search Adapter
                searchString = s; // Set the searchString equal to the string we are querying
                return false;
            }
        });
    }
    private void initCustomerData() {
        RestClient.executeCustomerGetRequest(MainActivity.VERIFYMEAPI + "customer/",
                token,
                this,
                new VolleyCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Customer> results) {
                        if (!results.isEmpty()) {
                            customerData = results;
                            RebindScreen();
                        }
                        else {
                            Toast.makeText(CustomerManagementActivity.this, "An Error Occurred. Please Retry Logging In.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void RebindScreen() {
        customerResults = findViewById(R.id.CustomerManagement_rvSearchResults); // Finding the RecyclerView and setting the variable equal to it

        // binding RecyclerView to Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        customerResults.setLayoutManager(layoutManager);

        // Creating a new search adapter to hold customer data in with onclick responses for each item
        searchAdapter = new SearchAdapter(customerData, this);
        searchAdapter.setOnClickListener(onItemClickListener);

        // Setting the adapter for the RecyclerView equal to the search adapter we created
        customerResults.setAdapter(searchAdapter);
    }

}
