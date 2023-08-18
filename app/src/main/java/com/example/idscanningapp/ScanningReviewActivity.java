package com.example.idscanningapp;

import static com.example.idscanningapp.MainActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

// Imports for Barcode scanning
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.idscanningapp.API.VolleyCallBack_String;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import java.util.List;
import android.util.Log;

//Imports for API ??
import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack;
import com.example.idscanningapp.Models.Customer;
import com.example.idscanningapp.Models.CustomerIncident;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


    // Todo add Nav menu to top of screen
public class ScanningReviewActivity extends AppCompatActivity {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    // Set up Button Variables
    private Button Flip_Photo_Button_L;
    private Button Flip_Photo_Button_R;
    private Button Accept;
    private Button Reject;
    private Button Retake;

    // Customer
    private ArrayList<Customer> customerData;



    // Class wide Strings
    private String Customer_Flag = "Blank";
    private String File_Location = "/storage/emulated/0/Android/data/com.example.idscanningapp/files/";
    private String File_Name = "Front";

    // Data
    private String Raw_Barcode = "blank";
    private String First_Name = "Joesoph";
    private String Middle_Name = "NonaSmith";
    private String Last_Name = "Smith";
    private String DLN = "H12345678";

    private String ISS = "01/04/2022";
    private String DOB = "08/05/1995";
    private String Sex = "M";
    private String Hair_Color = "Bro";
    private String Eye_Color = "Blu";
    private String Height = "6'-00''";
    private String Weight = "190 lb";
    private String Expiration = "05/06/2031";
    private String Home_Address = "Blank";
    private String Zip_Code = "Blank";

    private String token = "";

// Todo Remove testing string input
    private String testing_String = "ANSI 636026080102DL00410284ZA03250015DLDAQH65865275 " +
            "DCSWRAY " +
            "DDEN " +
            "DACNATHANIEL " +
            "DDFN " +
            "DADERICK " +
            "DDGN " +
            "DCAD " +
            "DCBB " +
            "DCDNONE " +
            "DBD04052022 " +
            "DBB09221985 " +
            "DBA04052030 " +
            "DBC1 " +
            "DAU072 in " +
            "DAYBRO " +
            "DAG22541 E STONECREST DR " +
            "DAIQUEEN CREEK " +
            "DAJAZ " +
            "DAK851425687 " +
            "DCF0272009CDW091605 " +
            "DCGUSA " +
            "DAW195 " +
            "DAZBRO " +
            "DCK22095AZ0159056260301 " +
            "DDAF " +
            "DDB02142014 " +
            "DDL1 " +
            "ZAZAAN " +
            "ZAB " +
            "ZAC";

    private ImageView Image_Preview_Pane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_review);

        // Binding for XML layout
        Image_Preview_Pane = findViewById(R.id.Image_Preview_Pane);
        Accept = findViewById(R.id.Accept_Button);
        Reject = findViewById(R.id.Reject_Button);
        Retake = findViewById(R.id.Retake_Button);
        Flip_Photo_Button_L = findViewById(R.id.Flip_Photo_Button_L);
        Flip_Photo_Button_R = findViewById(R.id.Flip_Photo_Button_R);

        // Set initial photo
        setPhoto();

        // Scans Barcode
        barcodeScan();

        // Displays initial fields
        fieldDisplay();

        // Button Click listeners
        // Todo if photos previously exist in database add between flipping
        Flip_Photo_Button_L.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (File_Name == "Back")
                    {File_Name = "Front";}
                else {File_Name = "Back";}
                setPhoto();;
            };
        });

        Flip_Photo_Button_R.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (File_Name == "Back")
                    {File_Name = "Front";}
                else {File_Name = "Back";}
                setPhoto();
            };
        });

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo call data save to database function
                saveToDatabase();

            }
        });

        // Todo Add pop up Prompt for user to accept
        Retake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ScanningReviewActivity.this, ScanningCameraActivity.class);
                startActivity(intent);
            };
        });

        // Todo Add pop up Prompt for user to accept
        Reject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Debugging Code
                //Customer_Flag = "True";
                //barcodeDataExtractor(testing_String);

                Intent intent = new Intent(ScanningReviewActivity.this, ScanningCameraActivity.class);
                startActivity(intent);
            };
        });

        // Code for accessing file from Storage
        // Change file path to incoming variable
    }

    // Barcode scanning function
    private void barcodeScan (){

        //private static final String TAG = "BarcodeScannerActivity";

            // Load the stored photo as a Bitmap
            Bitmap barcodeBitmap = BitmapFactory.decodeFile(File_Location + "Back" + ".jpg");

            // Create an ML Kit barcode scanner options
            BarcodeScannerOptions options =
                    new BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(Barcode.FORMAT_PDF417)
                            .build();

            // Create an ML Kit barcode scanner
            BarcodeScanner scanner = BarcodeScanning.getClient(options);

            // Create an ML Kit InputImage from the Bitmap
            InputImage image = InputImage.fromBitmap(barcodeBitmap, 0);

            // Pass the input image to the barcode scanner
            scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // Process the detected barcodes
                            for (Barcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                Log.d(TAG, "Detected barcode: " + rawValue);

                                // Return the barcode information to the main activity
                                Raw_Barcode = rawValue;
                                Toast.makeText(ScanningReviewActivity.this, "The Barcode was successfully scanned.", Toast.LENGTH_LONG).show();
                                // Extract data from raw barcode
                                if (Raw_Barcode != "blank") {
                                    barcodeDataExtractor(Raw_Barcode);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the barcode scanning process
                            Toast.makeText(ScanningReviewActivity.this, "The function failed to scan the barcode.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Barcode scanning failed. Error: " + e.getMessage());
                        }
                    });
    }

    private void barcodeDataExtractor (String baseData){
        //String two_Letter_Code = "";
        int count = 0;
        int string_Switcher = 0;

        // Clears Data Variables
        First_Name = "";
        Middle_Name = "";
        Last_Name = "";
        DLN = "";
        DOB = "";
        Sex = "";
        Hair_Color = "";
        Eye_Color = "";
        Height = "";
        Weight = "";
        Expiration = "";
        Home_Address = "";
        Zip_Code = "";
        ISS = "";

        // Loops through the barcode generated string and extracts meaningful data
        while (count < baseData.length() - 1) {

            if (baseData.charAt(count) == 'D' && baseData.charAt(count+1) == 'A'){
                if(baseData.charAt(count+2) == 'C'){
                    // DAC = First_Name;
                    string_Switcher = 1;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "First_Name Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'D') {
                    // DAD = Middle_Name;
                    string_Switcher = 2;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Middle_Name Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'G') {
                    string_Switcher = 12;
                    count = count + 2;
                }
                if(baseData.charAt(count+2) == 'K') {
                    string_Switcher = 13;
                    count = count + 2;
                }
                if(baseData.charAt(count+2) == 'U') {
                    // DAU = Height;
                    string_Switcher = 3;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Height Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'Q') {
                    // DAQ = DLN; Washington
                    string_Switcher = 11;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Weight Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'W') {
                    // DAW = Weight;
                    string_Switcher = 4;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Weight Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'Y') {
                    // DAY = Eye_Color;
                    string_Switcher = 5;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Eye_Color Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'Z') {
                    // DAZ = Hair_Color;
                    string_Switcher = 6;
                    count = count + 3;
                    //Toast.makeText(ScanningReviewActivity.this, "Hair_Color Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                else {count++;}
            }
            else if (baseData.charAt(count) == 'D' && baseData.charAt(count+1) == 'B'){
                if(baseData.charAt(count+2) == 'A') {
                    // DBA = Expiration;
                    string_Switcher = 7;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Expiration Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'B') {
                    // DBB = DOB;
                    string_Switcher = 8;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "DOB Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'C') {
                    // DBC = Sex;
                    string_Switcher = 9;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Sex Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
                }
                if(baseData.charAt(count+2) == 'D') {
                    string_Switcher = 14;
                    count = count + 3;
                }
                else {count++;}
            }
            else if (baseData.charAt(count) == 'D' && baseData.charAt(count+1) == 'C'){
                if(baseData.charAt(count+2) == 'F'){
                    // DCS = Last_Name;
                    string_Switcher = 0;
                    count = count + 2;
                }
                if(baseData.charAt(count+2) == 'S'){
                    // DCS = Last_Name;
                    string_Switcher = 10;
                    count = count + 2;
                    //Toast.makeText(ScanningReviewActivity.this, "Last_Name Else Ran " + string_Switcher + " " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
               }
                if (baseData.charAt(count + 2) == 'K') {
                    string_Switcher = 0;
                    count = count + 2;
                }
                else{count++;}
            }
            else if (baseData.charAt(count) == 'D' && baseData.charAt(count+1) == 'D'){
                if(baseData.charAt(count+2) == 'F'){
                    string_Switcher = 0;
                    count = count + 2;
                }
                if(baseData.charAt(count+2) == 'G'){
                    string_Switcher = 0;
                    count = count + 2;
                }
                else{count++;}
            }
            else if (baseData.charAt(count) == 'D' && baseData.charAt(count+1) == 'E'){
                if(baseData.charAt(count) == 'D'){
                    string_Switcher = 0;
                    count = count + 2;
                }
            }
            // Todo may include ANSI number as part of identifier for database
            else if (baseData.charAt(count) == 'D' && baseData.charAt(count +1) == 'L' && baseData.charAt(count +2) == 'D'
                        && baseData.charAt(count +3) == 'A' && baseData.charAt(count +4) == 'Q') {
                // Q / ? DLN;
                // Todo add secondary check
                // Todo this works for AZ licenses and need to add functionality for others
                string_Switcher = 11;
                count = count + 5;
                //Toast.makeText(ScanningReviewActivity.this, "DLN Else Ran " + string_Switcher + " " + count, Toast.LENGTH_SHORT).show();
            }
            else {
                if (string_Switcher == 1) {First_Name += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "First_Name Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 2) {Middle_Name += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Middle_Name Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 3) {Height += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Height Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 4) {Weight += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Weight Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 5) {Eye_Color += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Eye_Color Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 6) {Hair_Color += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Hair_Color Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 7) {Expiration += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Expiration Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 8) {DOB += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "DOB Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 9) {if (baseData.charAt(count) == '1'){ Sex += 'M';}
                                                else if (baseData.charAt(count) == '0'){ Sex += 'F';}
                                                else { Sex += baseData.charAt(count);}
                        //Toast.makeText(ScanningReviewActivity.this, "Sex Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 10) {Last_Name += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, "Last Name Ran " + count + " " + baseData.charAt(count), Toast.LENGTH_SHORT).show();
                }
                else if (string_Switcher == 11) {DLN += baseData.charAt(count);
                        //Toast.makeText(ScanningReviewActivity.this, DLN + " DLN Ran " + count + " " + baseData.charAt(count) + " " + baseData.length(), Toast.LENGTH_SHORT).show();
                        }
                else if (string_Switcher == 12) {Home_Address += baseData.charAt(count);}
                else if (string_Switcher == 13) {Zip_Code += baseData.charAt(count);}
                else if (string_Switcher == 14) {ISS += baseData.charAt(count);}

                //Toast.makeText(ScanningReviewActivity.this, "It Ran " + count + " times.", Toast.LENGTH_SHORT).show();
                count++;
            }
        }
        Toast.makeText(ScanningReviewActivity.this, "The barcode Data Extraction function ran.", Toast.LENGTH_LONG).show();

        fieldDisplay();
    }

    // Displays scanned inputs
    @SuppressLint("ResourceAsColor")
    private void fieldDisplay(){

        // Create Text views for all fields
        TextView First_Name_Scanned = findViewById(R.id.First_Name_Scanned);
        TextView Middle_Name_Scanned = findViewById(R.id.Middle_Name_Scanned);
        TextView Last_Name_Scanned = findViewById(R.id.Last_Name_Scanned);
        TextView DLN_Scanned = findViewById(R.id.DLN_Scanned);
        TextView DOB_Scanned = findViewById(R.id.DOB_Scanned);
        TextView Sex_Scanned = findViewById(R.id.Sex_Scanned);
        TextView Hair_Color_Scanned = findViewById(R.id.Hair_Color_Scanned);
        TextView Eye_Color_Scanned = findViewById(R.id.Eye_Color_Scanned);
        TextView Height_Scanned = findViewById(R.id.Height_Scanned);
        TextView Weight_Scanned = findViewById(R.id.Weight_Scanned);
        TextView Expiration_Scanned = findViewById(R.id.Expiration_Scanned);
        TextView Customer_Flag_R = findViewById(R.id.Customer_Flag);

        // Sets fields equal to scanned value
        First_Name_Scanned.setText("First Name:\n" + First_Name);
        Middle_Name_Scanned.setText("Middle Name:\n" + Middle_Name);
        Last_Name_Scanned.setText("Last Name:\n" + Last_Name);
        DLN_Scanned.setText("DLN:\n" + DLN);
        DOB_Scanned.setText("DOB:\n" + DOB);
        Sex_Scanned.setText("Sex:\n" + Sex);
        Hair_Color_Scanned.setText("Hair Color:\n" + Hair_Color);
        Eye_Color_Scanned.setText("Eye Color:\n" + Eye_Color);
        Height_Scanned.setText("Height:\n" + Height);
        Weight_Scanned.setText("Weight:\n" + Weight);
        Expiration_Scanned.setText("Expiration:\n" + Expiration);

        if (Customer_Flag != "Blank"){
            Customer_Flag_R.setBackgroundColor(R.color.red);
            Customer_Flag_R.setText("Customer Has Incident Reports!");
            Toast.makeText(ScanningReviewActivity.this, "THIS CUSTOMER HAS A FLAGGED INCIDENT REPORT!", Toast.LENGTH_LONG).show();
        }

    }

    private String formatDates(String date) {
        StringBuilder modDate = new StringBuilder(date);
        String formatedDate = "";

        modDate.insert(2, "/");
        modDate.insert(5, "/");

        formatedDate = modDate.toString();

        return (formatedDate);
    }

    // Todo Add the rest of the scanned fields to submit to the database
    private void saveToDatabase(){

        // If all information is entered in then begin sending data to API
        // Create a new customer and fill it with data
        Customer customer = new Customer();
        customer.setFirstName(First_Name);
        customer.setLastName(Last_Name);
        customer.setDLN(DLN);

        DOB = formatDates(DOB);
        ISS = formatDates(ISS);
        Expiration = formatDates(Expiration);

        try {
            customer.setDOB( new SimpleDateFormat("MM/dd/yyyy").parse(DOB));
            customer.setISS( new SimpleDateFormat("MM/dd/yyyy").parse(ISS));
            customer.setEXP( new SimpleDateFormat("MM/dd/yyyy").parse(Expiration));
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
                ScanningReviewActivity.this,
                new VolleyCallBack_String() {
                    @Override
                    public void onSuccess(String response) {
                        if (response == "Success") {
                            Toast.makeText(ScanningReviewActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if (response == "VolleyError")
                            Toast.makeText(ScanningReviewActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
                    }
                });
        Toast.makeText(ScanningReviewActivity.this, "Your Customer Has Been Submitted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Todo Check database vs DLN function
    private void existingRecordsCheck()
    {
        // Create customer and Incident variables
        Customer c = new Customer();
        CustomerIncident CI = new CustomerIncident();
        String DLN_db = "";

        // Todo pull database DLN array and Search database for DLN number
        DLN_db = c.getDLN();

        // Todo Debug - This is meant to check the scanned customer DLN vs database DLNS
        // It is then to check the UUID against incident report UUID's, if their is a match
        // it turns on the customer flag to alert the scanner
        if (DLN_db == DLN)
        {
            if (c.getCustomerID() == CI.getID()) {
                Customer_Flag = "True";
            }
        }

    }

    // Todo make text recognition function
    private void textPhotoRecognition(){

    }

    // Todo make Comparison function
    private void compareExisting(){

    }


    // Sets the photo to the front or back based off the flip buttons
    // Todo Make file path pass between ScanningCameraActivity and this
    private void setPhoto(){
        File imgFile = new  File(File_Location + File_Name + ".jpg");
        if(imgFile.exists()) {

            //Toast.makeText(ScanningReviewActivity.this, testing_String + " image Exists", Toast.LENGTH_LONG).show();

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.Image_Preview_Pane);
            myImage.setImageBitmap(myBitmap);}
        else {Toast.makeText(ScanningReviewActivity.this, "Sorry Image not found.", Toast.LENGTH_LONG).show();}

        /*
        Intent intent = getIntent();
        lName = intent.getStringExtra("lName");
        File storage = Environment.getExternalStorageDirectory();
        File file = new File(lName);
         */
    }

}
