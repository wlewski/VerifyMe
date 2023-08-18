package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.idscanningapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Issue with importing binding. Resolved by adding <Layout> lag within
    // the activity_main.xml file, then adding in dataBinding true and
    // viewbinding ture within gradle.

    public static final String VERIFYMEAPI = "https://idscanningverifymeapi.azurewebsites.net/api/";
    private ActivityMainBinding binding;
    public static final String TAG = "mainActivity";
    public int ID = 1;
    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");

        // Search Fragment
        Fragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);

        replaceFragment(searchFragment);
        //startActivity(new Intent(this, ScanningCameraActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates the navigation bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ID = item.getItemId();
        Intent intent;

        if (ID == R.id.nav_scanning_page)
        {
            // Scanning Activity
            intent = new Intent(this, ScanningCameraActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);

            startActivity(intent);
            return true;
        }
        if (ID == R.id.nav_manual_add_page)
        {
            // Manual Add Activity
            intent = new Intent(this, ManualAddActivity.class);
            intent.putExtra("token", token);

            startActivity(intent);
            return true;
        }
        if (ID == R.id.nav_search_bar_page)
        {
            // Search Fragment
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            bundle.putString("username", username);

            Fragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);

            replaceFragment(searchFragment);
            return true;
        }
        if (ID == R.id.nav_mangage_account_page)
        {
            // Manage Account Activity
            intent = new Intent(this, ManageOrganizationActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);

            startActivity(intent);
            return true;
        }
        if (ID == R.id.nav_logout) {
            // Return to Login Screen
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }


}
