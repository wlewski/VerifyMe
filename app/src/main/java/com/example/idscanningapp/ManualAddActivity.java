package com.example.idscanningapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack_String;
import com.example.idscanningapp.Models.Customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ManualAddActivity extends AppCompatActivity {
    public static final String TAG = "manualAddActivity";
    private String token;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etDLN;
    private EditText etDOB;
    private EditText etISS;
    private EditText etEXP;

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_manual_add);
        setTitle("Manually Add Customer");
        Log.d(TAG, "Manual Add Activity");

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");

        initSubmitButton();
        initCancelButton();
        initTakeFrontPicButton();
        initTakeBackPicButton();

        initFirstNameEditText();
        initLastNameEditText();
        initDLNEditText();
        initDOBEditText();
        initISSEditText();
        initEXPEditText();
    }

    //region Initializing of EditTexts
    public void initFirstNameEditText() {
        etFirstName = findViewById(R.id.ManualAddPage_etEnterFirstName);
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etFirstName.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    public void initLastNameEditText() {
        etLastName = findViewById(R.id.ManualAddPage_etEnterLastName);
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etLastName.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    public void initDLNEditText() {
        etDLN = findViewById(R.id.ManualAddPage_etEnterLicenseNum);
        etDLN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etDLN.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    public void initDOBEditText() {
        etDOB = findViewById(R.id.ManualAddPage_etEnterDOB);
        etDOB.setShowSoftInputOnFocus(false); // Prevents the Keyboard from popping up within the DOB EditText

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                etDOB.setTextIsSelectable(true);

                // DatePickerDialog Box that will allow users to select their Date of Birth.
                DatePickerDialog dobPickerDialog = new DatePickerDialog(ManualAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                selectedMonth = selectedMonth + 1;

                                // Check if the number returned for Month or Day is less than 10 and if it is add
                                // a zero in front of the day/month. Set etDOB equal to the date selected.
                                if (selectedMonth < 10 && selectedDay < 10)
                                    etDOB.setText( "0" + selectedMonth + "/0" + selectedDay + "/" + selectedYear);
                                else if (selectedMonth < 10)
                                    etDOB.setText( "0" + selectedMonth + "/" + selectedDay + "/" + selectedYear);
                                else if (selectedDay < 10)
                                    etDOB.setText( selectedMonth + "/0" + selectedDay  + "/" + selectedYear);
                                else
                                    etDOB.setText( selectedMonth + "/" + selectedDay + "/" + selectedYear);
                            }
                        }, year, month, day);

                dobPickerDialog.show();
            }
        });

        etDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etDOB.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    public void initISSEditText() {
        etISS = findViewById(R.id.ManualAddPage_etEnterIss);
        etISS.setShowSoftInputOnFocus(false);

        etISS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                etISS.setShowSoftInputOnFocus(false);

                DatePickerDialog issPickerDialog = new DatePickerDialog(ManualAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                selectedMonth = selectedMonth + 1;

                                // Check if the number returned for Month or Day is less than 10 and if it is add
                                // a zero in front of the day/month. Set etDOB equal to the date selected.
                                if (selectedMonth < 10 && selectedDay < 10)
                                    etISS.setText( "0" + selectedMonth + "/0" + selectedDay + "/" + selectedYear);
                                else if (selectedMonth < 10)
                                    etISS.setText( "0" + selectedMonth + "/" + selectedDay + "/" + selectedYear);
                                else if (selectedDay < 10)
                                    etISS.setText( selectedMonth + "/0" + selectedDay  + "/" + selectedYear);
                                else
                                    etISS.setText( selectedMonth + "/" + selectedDay + "/" + selectedYear);
                            }
                        }, year, month, day);

                issPickerDialog.show();
            }
        });

        etISS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etISS.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    public void initEXPEditText() {
        etEXP = findViewById(R.id.ManualAddPage_etEnterExp);
        etEXP.setShowSoftInputOnFocus(false);

        etEXP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                etEXP.setShowSoftInputOnFocus(false);

                DatePickerDialog expPickerDialog = new DatePickerDialog(ManualAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                selectedMonth = selectedMonth + 1;

                                // Check if the number returned for Month or Day is less than 10 and if it is add
                                // a zero in front of the day/month. Set etDOB equal to the date selected.
                                if (selectedMonth < 10 && selectedDay < 10)
                                    etEXP.setText( "0" + selectedMonth + "/0" + selectedDay + "/" + selectedYear);
                                else if (selectedMonth < 10)
                                    etEXP.setText( "0" + selectedMonth + "/" + selectedDay + "/" + selectedYear);
                                else if (selectedDay < 10)
                                    etEXP.setText( selectedMonth + "/0" + selectedDay  + "/" + selectedYear);
                                else
                                    etEXP.setText( selectedMonth + "/" + selectedDay + "/" + selectedYear);
                            }
                        }, year, month, day);

                expPickerDialog.show();
            }
        });

        etEXP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etEXP.setBackground(getDrawable(R.drawable.edit_text_default_background_border));
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    //endregion

    //region Initializing of Buttons Within Methods
    // private methods to create the onclicklisteners for each button instead of having it all within the onCreate Method.
    private void initSubmitButton() {
        Button btnSubmit = findViewById(R.id.ManualAddPage_btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:" + ((EditText)findViewById(R.id.ManualAddPage_etEnterFirstName)).getText().toString() + "hello" );

                if (etFirstName.getText().toString().isEmpty() ||
                    etLastName.getText().toString().isEmpty() ||
                    etDLN.getText().toString().isEmpty() ||
                    etDOB.getText().toString().isEmpty() ||
                    etISS.getText().toString().isEmpty() ||
                    etEXP.getText().toString().isEmpty() ) {

                    // Check each data entry to make sure data is entered,
                    // If data is not entered check each data entry point and set the background to red if null
                    if (etFirstName.getText().toString().isEmpty())
                        etFirstName.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    if (etLastName.getText().toString().isEmpty())
                        etLastName.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    if (etDLN.getText().toString().isEmpty())
                        etDLN.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    if (etDOB.getText().toString().isEmpty())
                        etDOB.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    if (etISS.getText().toString().isEmpty())
                        etISS.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                    if (etEXP.getText().toString().isEmpty())
                        etEXP.setBackground(getDrawable(R.drawable.edit_text_error_background_border));

                    // Send message to screen that data needs to be entered
                    Toast.makeText(ManualAddActivity.this, "Please Enter Data Into All Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // If all information is entered in then begin sending data to API
                    // Create a new customer and fill it with data
                    Customer customer = new Customer();
                    customer.setFirstName(((EditText)findViewById(R.id.ManualAddPage_etEnterFirstName)).getText().toString());
                    customer.setLastName(((EditText)findViewById(R.id.ManualAddPage_etEnterLastName)).getText().toString());
                    customer.setDLN(((EditText)findViewById(R.id.ManualAddPage_etEnterLicenseNum)).getText().toString());
                    try {
                        customer.setDOB( new SimpleDateFormat("MM/dd/yyyy").parse(((EditText)findViewById(R.id.ManualAddPage_etEnterDOB)).getText().toString()));
                        customer.setISS( new SimpleDateFormat("MM/dd/yyyy").parse(((EditText)findViewById(R.id.ManualAddPage_etEnterIss)).getText().toString()));
                        customer.setEXP( new SimpleDateFormat("MM/dd/yyyy").parse(((EditText)findViewById(R.id.ManualAddPage_etEnterExp)).getText().toString()));
                    }
                    catch (Exception e) {
                        Log.d(TAG, "ERROR: Manual Add of Customer Dates Failed - ERROR MESSAGE: " + e.getMessage());
                    }

                    /*Log.d(TAG, "Customer FirstName: " + customer.getFirstName());
                    Log.d(TAG, "Customer LastName: " + customer.getLastName());
                    Log.d(TAG, "Customer DLN: " + customer.getDLN());
                    Log.d(TAG, "Customer DOB: " + customer.getDOB());
                    Log.d(TAG, "Customer ISS: " + customer.getISS());
                    Log.d(TAG, "Customer EXP: " + customer.getEXP());*/

                    // Send all data about a customer REST CLIENT POST REQUEST
                    RestClient.executeCustomerPostRequest(customer, MainActivity.VERIFYMEAPI + "customer/",
                            token,
                            ManualAddActivity.this,
                            new VolleyCallBack_String() {
                                @Override
                                public void onSuccess(String response) {
                                    if (response == "Success") {
                                        Toast.makeText(ManualAddActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else if (response == "VolleyError")
                                        Toast.makeText(ManualAddActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Toast.makeText(ManualAddActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private void initCancelButton() {
        Button btnCancel = findViewById(R.id.ManualAddPage_btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initTakeFrontPicButton() {
        Button btnTakeFrontPic = findViewById(R.id.ManualAddPage_btnTakeFrontPic);

        btnTakeFrontPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualAddActivity.this, "You Are Adding The Front Image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initTakeBackPicButton() {
        Button btnTakeBackPic = findViewById(R.id.ManualAddPage_btnTakeBackPic);

        btnTakeBackPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualAddActivity.this, "You Are Adding The Back Image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion
}
