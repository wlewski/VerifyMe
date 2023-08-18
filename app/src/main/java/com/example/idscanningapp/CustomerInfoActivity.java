package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack;
import com.example.idscanningapp.API.VolleyCallBack_CustomerIncident;
import com.example.idscanningapp.Models.Customer;
import com.example.idscanningapp.Models.CustomerIncident;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomerInfoActivity extends AppCompatActivity {
    private Customer selectedCustomerInformation;
    private ArrayList<CustomerIncident> selectedCustomerIncidents;
    private String token;
    private String username;
    private RecyclerView incidentResults;
    private IncidentPopupAdapter incidentAdapter;
    private boolean additionalInfo;
    public static final String TAG = "customerInfoActivity";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_customer_info);
        setTitle("Customer Information");

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");

        additionalInfo = false;

        initCustomerInformation();
        initReportButton();
        initAdditionalInfoButton();
    }
    @Override
    public void onResume(){
        super.onResume();

        initAdditionalInfo();
        initAdditionalInfoButton();
    }

    //region Initializing CustomerInfo Activity Information
    private void initCustomerInformation() {
        selectedCustomerInformation = new Customer();

        try {
            // Will allow the grabbing and storage of the selected customerID that can be used to obtain the Customer information
            Bundle bundle = getIntent().getExtras();
            String id = bundle.getString("customerID");

            // REST Client use of GetOneRequest which will return data for the Customer based off its ID
            RestClient.executeCustomerGetOneRequest(MainActivity.VERIFYMEAPI + "customer/" + id,
                    token, CustomerInfoActivity.this, new VolleyCallBack() {
                @Override
                public void onSuccess(ArrayList<Customer> results) {
                    // Grab the results from the VolleyCallback and set selectedCustomerInformation equal to it
                    selectedCustomerInformation = results.get(0);

                    // Method to initialize all textviews with data within the CustomerInfo Activity
                    initTextViews();

                    if (selectedCustomerInformation.getIDFront() == null) {
                        ImageView ivCustomerImage = findViewById(R.id.CustomerInfoPage_ivCustomerImage);
                        ivCustomerImage.setBackgroundColor(getColor(R.color.grey));
                        ivCustomerImage.setForeground(getDrawable(R.drawable.vector_asset_default_user_img));
                    }
                    else {

                    }

                    initAdditionalInfo(); // Attempt to load any additional information about a user

                    // Log information about customer into the Log.d console
                    /*Log.d(TAG, "Customer ID: " + selectedCustomerInformation.getCustomerID());
                    Log.d(TAG, "Customer FirstName: " + selectedCustomerInformation.getFirstName());
                    Log.d(TAG, "Customer MiddleName: " + selectedCustomerInformation.getMiddleName());
                    Log.d(TAG, "Customer LastName: " + selectedCustomerInformation.getLastName());
                    Log.d(TAG, "Customer DOB: " + selectedCustomerInformation.getDOB());
                    Log.d(TAG, "Customer Street Address: " + selectedCustomerInformation.getStreet());
                    Log.d(TAG, "Customer City: " + selectedCustomerInformation.getCity());
                    Log.d(TAG, "Customer State: " + selectedCustomerInformation.getState());
                    Log.d(TAG, "Customer Country: " + selectedCustomerInformation.getCountry());
                    Log.d(TAG, "Customer Zip: " + selectedCustomerInformation.getZip());*/
                }
            });
        }
        catch (Exception e) {
            Log.d(TAG, "initCustomerInformation: " + e.getMessage());
        }
    }
    private void initAdditionalInfo() {
        // Grab data from the database that has information regarding a customers possible incidents
        RestClient.executeCustomerIncidentGetByCustomerID(MainActivity.VERIFYMEAPI + "customerincident/getallbycustomerid/" + selectedCustomerInformation.getCustomerID(),
                token,
                CustomerInfoActivity.this,
                new VolleyCallBack_CustomerIncident() {
                    @Override
                    public void onSuccess(ArrayList<CustomerIncident> results) {
                        selectedCustomerIncidents = results;

                        additionalInfo = true; // if we can get to the volley call back then there is data within the customer incident page.
                    }
                });
    }
    public void initTextViews() {
        ///// NOTE NEED TO PARSE DATES WITHIN REST CLIENT TO DISPLAY WITHIN APPLICATION /////

        // Finding all TextViews by their IDs
        TextView tvFirstName = findViewById(R.id.CustomerInfoPage_tvFirstName);
        TextView tvMiddleName = findViewById(R.id.CustomerInfoPage_tvMiddleName);
        TextView tvLastName = findViewById(R.id.CustomerInfoPage_tvLastName);
        TextView tvDOB = findViewById(R.id.CustomerInfoPage_tvDOB);
        TextView tvSex = findViewById(R.id.CustomerInfoPage_tvSex);
        TextView tvEyeColor = findViewById(R.id.CustomerInfoPage_tvEyeColor);
        TextView tvHairColor = findViewById(R.id.CustomerInfoPage_tvHairColor);
        TextView tvHeight = findViewById(R.id.CustomerInfoPage_tvHeight);
        TextView tvWeight = findViewById(R.id.CustomerIntoPage_tvWeight);
        TextView tvStreetAddress = findViewById(R.id.CustomerInfoPage_tvStreetAddress);
        TextView tvCity = findViewById(R.id.CustomerInfoPage_tvCity);
        TextView tvState = findViewById(R.id.CustomerInfoPage_tvState);
        TextView tvCountry = findViewById(R.id.CustomerInfoPage_tvCountry);
        TextView tvZip = findViewById(R.id.CustomerInfoPage_tvZip);

        TextView tvDLN = findViewById(R.id.CustomerInfoPage_tvDLN);
        TextView tvDD = findViewById(R.id.CustomerInfoPage_tvDD);
        TextView tvISS = findViewById(R.id.CustomerInfoPage_tvISS);
        TextView tvEXP = findViewById(R.id.CustomerInfoPage_tvEXP);
        TextView tvIDFront = findViewById(R.id.CustomerInfoPage_tvIDFront);
        TextView tvIDBack = findViewById(R.id.CustomerInfoPage_tvIDBack);

        // Setting data for TextViews equal to data within selectedCutstomerInformation variable
        tvFirstName.setText("First Name: " + selectedCustomerInformation.getFirstName());
        tvMiddleName.setText("Middle Name: " + selectedCustomerInformation.getMiddleName());
        tvLastName.setText("Last Name: " + selectedCustomerInformation.getLastName());
        tvDOB.setText("Date of Birth: " + new SimpleDateFormat("MM/dd/yyyy").format(selectedCustomerInformation.getDOB()));
        tvSex.setText("Sex: " + selectedCustomerInformation.getSex());
        tvEyeColor.setText("Eye Color: " + selectedCustomerInformation.getEyeColor());
        tvHairColor.setText("Hair Color: " + selectedCustomerInformation.getHairColor());
        tvHeight.setText("Height: " + selectedCustomerInformation.getHeight());
        tvWeight.setText("Weight: " + selectedCustomerInformation.getWeight());
        tvStreetAddress.setText("Street Address: " + selectedCustomerInformation.getStreet());
        tvCity.setText("City: " + selectedCustomerInformation.getCity());
        tvState.setText("State: " + selectedCustomerInformation.getState());
        tvCountry.setText("Country: " + selectedCustomerInformation.getCountry());
        tvZip.setText("Zip Code: " + selectedCustomerInformation.getZip());

        tvDLN.setText("DLN: " + selectedCustomerInformation.getDLN());
        tvDD.setText("DD: " + selectedCustomerInformation.getDD());
        tvISS.setText("ISS: " + new SimpleDateFormat("MM/dd/yyyy").format(selectedCustomerInformation.getISS()));
        tvEXP.setText("EXP: " + new SimpleDateFormat("MM/dd/yyyy").format(selectedCustomerInformation.getEXP()));
        tvIDFront.setText("ID Front: " + selectedCustomerInformation.getIDFront());
        tvIDBack.setText("ID Back: " + selectedCustomerInformation.getIDBack());
    }
    //endregion

    //region Initialization of Buttons
    private void initAdditionalInfoButton() {
        // Code for Loading all Customer Incidents
        Button btnAdditionalInfo = findViewById(R.id.CustomerInfoPage_btnAdditionalInfo);
        btnAdditionalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (additionalInfo) {
                    // Inflate the Popup Window
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    // Create a new view that holds the Popup Window Layout
                    View popupView = inflater.inflate(R.layout.popup_customer_incident, null);

                    // Create the height and width of the popup
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // Taps outside of popup dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable); // Create the popup object

                    // Binding data to the Recycler view
                    incidentResults =  popupView.findViewById(R.id.CustomerIncidentPopup_rvIncidents);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CustomerInfoActivity.this);
                    incidentResults.setLayoutManager(layoutManager);

                    incidentAdapter = new IncidentPopupAdapter(selectedCustomerIncidents, CustomerInfoActivity.this);

                    incidentResults.setAdapter(incidentAdapter);

                    // Display popup
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    // Close button for popup
                    Button btnClose = popupView.findViewById(R.id.CustomerIncidentPopup_btnClose);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                }
                else
                    Toast.makeText(CustomerInfoActivity.this, "Customer Has No Incident Data", Toast.LENGTH_SHORT).show();

                // Dismisses the popup by touching it
                /*popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //popupWindow.dismiss();
                        //return true;

                        return false;
                    }
                });*/

            }
        });
    }
    private void initReportButton() {
        Button btnReport = findViewById(R.id.CustomerInfoPage_btnReportIncident);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), IncidentReportActivity.class);
                intent.putExtra("customerID", selectedCustomerInformation.getCustomerID().toString());
                intent.putExtra("token", token);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });
    }
    //endregion
}
