package com.example.idscanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack_String;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "loginActivity";
    private EditText etUsername;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        initLoginButton();
        initTextViews();
    }
    private void initTextViews() {
        etUsername = findViewById(R.id.LoginPage_etUsername);
        etPassword = findViewById(R.id.LoginPage_etPassword);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etUsername.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPassword.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    //region Initialization of Buttons
    private void initLoginButton() {
        Button btnLogin = findViewById(R.id.LoginPage_btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUsername.getText().toString().trim().isEmpty() && !etPassword.getText().toString().trim().isEmpty()) {
                    // Send the request body with the grant_type, username, and password into the REST Client
                    String requestBody = "grant_type=password&username=" +etUsername.getText().toString().trim()+ "&password=" +etPassword.getText().toString().trim();
                    RestClient.executeLoginRequest("https://idscanningverifymeapi.azurewebsites.net/login",
                            requestBody,
                            LoginActivity.this,
                            new VolleyCallBack_String() {
                                @Override
                                public void onSuccess(String response) {
                                    Log.d(TAG, "onSuccess: " + response);
                                    if (response != "Invalid") {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService((LAYOUT_INFLATER_SERVICE));
                                        View popupView = inflater.inflate(R.layout.popup_eula, null);

                                        int width = LinearLayout.LayoutParams.MATCH_PARENT;
                                        int height = ViewGroup.LayoutParams.MATCH_PARENT;
                                        boolean focusable = false;

                                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                        Button btnAccept = popupView.findViewById(R.id.PopupEULA_btnAccept);
                                        Button btnDeny = popupView.findViewById(R.id.PopupEULA_btnDeny);

                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);

                                        btnAccept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // Allow User to Login
                                                // Pass the token and UserId
                                                Intent intent = new Intent(LoginActivity.this, ScanningCameraActivity.class);
                                                intent.putExtra("token", response);
                                                intent.putExtra("username", etUsername.getText().toString());

                                                startActivity(intent);
                                            }
                                        });
                                        btnDeny.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(LoginActivity.this, "Sorry Could Not Login...", Toast.LENGTH_SHORT).show();
                                                popupWindow.dismiss();
                                            }
                                        });



                                    }
                                    else {
                                        // Deny user login
                                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
                else {
                    // Display Error Background On EditText Fields
                    if (etUsername.getText().toString().isEmpty())
                        etUsername.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    else
                        etPassword.setBackground(getDrawable(R.drawable.edit_text_error_background_border));

                    Toast.makeText(LoginActivity.this, "Please Enter Your Login Information Into The Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //endregion
}
