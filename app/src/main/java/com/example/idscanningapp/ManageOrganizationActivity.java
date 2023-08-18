package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageOrganizationActivity extends AppCompatActivity {
    public static final String TAG = "manageAccountActivity";
    private String token;
    private String username;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_manage_organization);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");

        initUserManagementButton();
        initCustomerManagementButton();
        initAnalyticsButton();
        initAppSettingsButton();
        initBackButton();
    }

    private void initUserManagementButton() {
        Button btnUserDatabaseManagement = findViewById(R.id.ManageOrganization_btnUserDatabaseManagement);
        btnUserDatabaseManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Load User Management");
                Intent intent = new Intent(ManageOrganizationActivity.this, UserManagementActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });
    }
    private void initCustomerManagementButton() {
        Button btnCustomerDatabaseManagement = findViewById(R.id.ManageOrganization_btnCustomerDatabaseManagement);
        btnCustomerDatabaseManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Load Customer Management");
                Intent intent = new Intent(ManageOrganizationActivity.this, CustomerManagementActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });
    }
    private void initAnalyticsButton() {
        Button btnAnalytics = findViewById(R.id.ManageOrganization_btnAnalytics);
        btnAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Load Analytics");
                Intent intent = new Intent(ManageOrganizationActivity.this, AnalyticsActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        });
    }
    private void initAppSettingsButton() {
        Button btnApplicationSettings = findViewById(R.id.ManageOrganization_btnApplicationSettings);
        btnApplicationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Load Settings");
                startActivity(new Intent(ManageOrganizationActivity.this, ApplicationSettingActivity.class));
            }
        });
    }
    private void initBackButton() {
        Button btnReturnToScanningScreen = findViewById(R.id.ManageOrganization_btnReturnToScanningScreen);
        btnReturnToScanningScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Return To Scanning Page");
                finish();
            }
        });
    }
}
