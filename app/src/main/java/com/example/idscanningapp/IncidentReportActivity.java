package com.example.idscanningapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack;
import com.example.idscanningapp.API.VolleyCallBack_Incident;
import com.example.idscanningapp.API.VolleyCallBack_String;
import com.example.idscanningapp.API.VolleyCallBack_User;
import com.example.idscanningapp.Models.Customer;
import com.example.idscanningapp.Models.CustomerIncident;
import com.example.idscanningapp.Models.Incident;
import com.example.idscanningapp.Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class IncidentReportActivity extends AppCompatActivity {
    public final static String TAG = "reportIncidentActivity";
    private String token;
    private String username;
    private String userId;
    //Region
    private Spinner spIncidents;
    private Spinner spFlagLevels;
    private EditText etAdditionalInformation;
    //endregion

    ArrayList<Incident> incidentList;
    User operatingUser;
    Customer selectedCustomer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report);
        setTitle("Report Customer Incident");

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");
        Log.d(TAG, "onCreate: " + username);

        // Initialize Information
        initCustomerInformation();
        initUserInfo();

        // Initialize Inputs
        initIncidentSpinner();
        initFlagLevelSpinner();
        initAdditionalInfoEditText();
        initReportIncidentButton();
        initCancelButton();
    }

    //region Initializing of Information
    private void initCustomerInformation() {
        selectedCustomer = new Customer();
        try {
            // Get information about the customer that was selected
            getIntent().getExtras();
            Bundle bundle = getIntent().getExtras();
            String id = bundle.getString("customerID");

            // Get the Customer Information from API
            RestClient.executeCustomerGetOneRequest(MainActivity.VERIFYMEAPI + "customer/" + id,
                    token,
                    IncidentReportActivity.this,
                    new VolleyCallBack() {
                        @Override
                        public void onSuccess(ArrayList<Customer> results) {
                            if (!results.isEmpty()) {
                                selectedCustomer = results.get(0);
                                initFullNameTextView();
                            }
                            else
                                Toast.makeText(IncidentReportActivity.this, "Cannot Find Customer", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR OCCURRED WHEN INITIALIZING CUSTOMER DATA - MESSAGE: " + e.getMessage());
        }
    }
    private void initUserInfo() {

        Log.d(TAG, "initUserInfo: " + username);
        Log.d(TAG, "initUserInfo: " + MainActivity.VERIFYMEAPI + "user/byusername/" + username );
        // Initialize User information based off of username

        RestClient.executeUserGetOneRequest(MainActivity.VERIFYMEAPI + "user/byusername/" + username,
                token,
                IncidentReportActivity.this,
                new VolleyCallBack_User() {
                    @Override
                    public void onSuccess(ArrayList<User> results) {
                        userId = results.get(0).getID().toString();
                        /*User user = new User();
                        if (!user.getID().equals(null)) {
                            userId = user.getID().toString();
                            Log.d(TAG, "onSuccess: " + userId.toString());
                        }*/
                    }
                }
        );
    }
    //endregion

    //region Initializing of Interactive Components i.e. Spinner and Buttons
    private void initFullNameTextView(){
        TextView customerFullName = findViewById(R.id.ReportIncidentPage_tvCustomerFullName);
        customerFullName.setText(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
    }
    private void initAdditionalInfoEditText() {
        etAdditionalInformation = findViewById(R.id.ReportIncidentPage_etAdditionalInformation);
    }
    private void initFlagLevelSpinner() {
        spFlagLevels = findViewById(R.id.ReportIncidentPage_spFlagLevel);
        ArrayList<String> flagLevels = new ArrayList<>();
        flagLevels.add(0, "SELECT A FLAG LEVEL");
        flagLevels.add("1 - Not Severe");
        flagLevels.add("2 - Semi Severe");
        flagLevels.add("3 - Severe");
        flagLevels.add("4 - Highly Severe URGENT");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncidentReportActivity.this,
                android.R.layout.simple_spinner_dropdown_item, flagLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFlagLevels.setAdapter(adapter);

        // Reset background when a user clicks on a new item within the spinner
        spFlagLevels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spFlagLevels.setBackground(getDrawable(R.drawable.default_background_border));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
    private void initIncidentSpinner() {
        try {
            // Use REST Client to grab incidents from the database
            RestClient.executeIncidentGetRequest(MainActivity.VERIFYMEAPI + "incident/",
                    token,
                    IncidentReportActivity.this,
                    new VolleyCallBack_Incident() {
                        @Override
                        public void onSuccess(ArrayList<Incident> results) {
                            Log.d(TAG, "onSuccess: " + results.toString());
                            incidentList = results;
                            incidentList.add(0, new Incident(UUID.fromString("00000000-0000-0000-0000-000000000000"), "SELECT AN INCIDENT"));

                            // Create a Spinner and find the spinner within the ReportIncidentPage
                            spIncidents = findViewById(R.id.ReportIncidentPage_spIncidents);

                            // Create Adapter to bind the Spinner to the Incidents
                            ArrayAdapter<Incident> adapter = new ArrayAdapter<Incident>(IncidentReportActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, results);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Attach Adapter to Spinner
                            spIncidents.setAdapter(adapter);

                            // Reset background when a user clicks on a new item within the spinner
                            spIncidents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spIncidents.setBackground(getDrawable(R.drawable.default_background_border));
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) { }
                            });
                        }
                    });
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR OCCURRED WITH INITIALIZING INCIDENT SPINNER - MESSAGE: " + e.getMessage());
        }
    }
    private void initReportIncidentButton() {
        Button btnReportIncident = findViewById(R.id.ReportIncidentPage_btnReportIncident);

        btnReportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + userId);
                Log.d(TAG, "onClick: " + incidentList.get(Integer.parseInt(String.valueOf(spIncidents.getSelectedItemId()))).getID());
                Log.d(TAG, "onClick: " + spFlagLevels.getId());

                CustomerIncident customerIncident = new CustomerIncident();
                customerIncident.setCustomerID(selectedCustomer.getCustomerID());

                customerIncident.setUserID(UUID.fromString(userId));
                customerIncident.setIncidentID(incidentList.get(Integer.parseInt(String.valueOf(spIncidents.getSelectedItemId()))).getID());
                customerIncident.setFlagLevel(Integer.parseInt(String.valueOf(spFlagLevels.getSelectedItemId())));
                customerIncident.setIncidentDate(new Date());
                customerIncident.setAdditionalInfo(etAdditionalInformation.getText().toString());

                // Check if the incident or flag level spinners are filled with data.
                // If they are then add an error background
                if (customerIncident.getIncidentID().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")) ||
                    customerIncident.getFlagLevel() == 0) {

                    if (customerIncident.getIncidentID().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")))
                        spIncidents.setBackground(getDrawable(R.drawable.error_background_border));
                    if (customerIncident.getFlagLevel() == 0)
                        spFlagLevels.setBackground(getDrawable(R.drawable.error_background_border));

                    Toast.makeText(IncidentReportActivity.this, "Please Fill Out Required Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Complete the Steps of inputting the incident
                    RestClient.executeCustomerIncidentPostRequest(customerIncident, MainActivity.VERIFYMEAPI + "customerincident/",
                            token,
                            IncidentReportActivity.this,
                            new VolleyCallBack_String() {
                                @Override
                                public void onSuccess(String response) {
                                    if (response == "Success") {
                                        Toast.makeText(IncidentReportActivity.this, "Record Has Been Saved!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else if (response == "VolleyError")
                                        Toast.makeText(IncidentReportActivity.this, "An Error Occurred... Please Try Again Later.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    private void initCancelButton() {
        Button btnCancel = findViewById(R.id.ReportIncidentPage_btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //endregion

}
