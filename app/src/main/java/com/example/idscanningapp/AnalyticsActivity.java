package com.example.idscanningapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack_CustomerCheckinRecord;
import com.example.idscanningapp.Models.CustomerCheckinRecord;

import java.util.ArrayList;

public class AnalyticsActivity extends AppCompatActivity {
    private ArrayList<CustomerCheckinRecord> recordData;
    private String token;
    private RecyclerView recordResults;
    private AnalyticsAdapter analyticsAdapter;
    public static final String TAG = "analyticsActivity";
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        setTitle("Customer Analytics");

        Log.d(TAG, "onCreate: Before");
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "onCreate: During");
        token = bundle.getString("token");
        Log.d(TAG, "onCreate: After");
        Log.d(TAG, "onCreate: " + token);

        initBackButton();
        initCheckinRecords();
        initSearchBar();
    }

    private void initBackButton() {
        Button btnBack = findViewById(R.id.Analytics_btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initCheckinRecords() {
        RestClient.executeCustomerCheckinRecordGetRequest(MainActivity.VERIFYMEAPI + "customercheckinrecord/",
                token,
                this,
                new VolleyCallBack_CustomerCheckinRecord() {
            @Override
            public void onSuccess(ArrayList<CustomerCheckinRecord> results) {
                Log.d(TAG, "onSuccess: " + results.get(0).getCheckinDate().toString());
                recordData = results;

                if (recordData != null) {
                    RebindScreen();
                }
            }
        });
    }
    private void initSearchBar() {
        SearchView svSearchBar = findViewById(R.id.Analytics_svSearchbar);

        svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (searchString != null)
                    analyticsAdapter.setCheckinRecordData(recordData);

                analyticsAdapter.getFilter().filter(s);
                searchString = s;
                return false;
            }
        });
    }
    private void RebindScreen() {
        recordResults = findViewById(R.id.Analytics_rvSearchResults);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recordResults.setLayoutManager(layoutManager);

        analyticsAdapter = new AnalyticsAdapter(recordData, this);

        recordResults.setAdapter(analyticsAdapter);
    }
}
