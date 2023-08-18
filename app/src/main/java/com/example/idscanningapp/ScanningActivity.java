package com.example.idscanningapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

// Imports for Barcode scanning
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Log;
import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

// Imports for Text recognition
import android.app.Activity;
import java.io.IOException;
import android.net.Uri;
//import androidx.annotation.NonNull;                       // redundant
//import com.google.android.gms.tasks.OnFailureListener;    // redundant
//import com.google.android.gms.tasks.OnSuccessListener;    // redundant
//import com.google.mlkit.vision.common.InputImage;         // redundant
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

// String data extraction
import java.util.ArrayList;
import java.util.List;

public class ScanningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_scanning);

    }


//Connect scanning activity to scanning XML File
//Create buttons needed
//Integrate functions
//Link buttons to XML

// Create function that calls to database to check DLN vs current records

//Create function for storing information to the data base
//If rejected store in log

    // Create function that extracts DL information from string created by scanning PDF417 barcode
 //   public void DataExtraction {

        public String[] extractStrings(String input) {
            List<String> extractedStrings = new ArrayList<>();
            int startIndex = 0;

            while (startIndex < input.length()) {
                int ddIndex = input.indexOf("dd", startIndex);

                if (ddIndex == -1) {
                    break; // No more "dd" markers found
                }

                int nextMarkerIndex = input.indexOf("dd", ddIndex + 2);

                if (nextMarkerIndex == -1) {
                    nextMarkerIndex = input.length();
                }

                String extractedString = input.substring(ddIndex + 2, nextMarkerIndex);
                extractedStrings.add(extractedString);

                startIndex = nextMarkerIndex;
            }

            return extractedStrings.toArray(new String[0]);
        }
 //   }

    //Create function for comparing barcode input to text recognition - can additionally be used
// to compare database information to scanned information
//    class TextComparison {
        public boolean compareStrings(String str1, String str2) {
            // Compare the two strings and return true if they are the same, false otherwise
            return str1.equals(str2);
        }
 //   }

/*
//Create function for text recognition - Integrate and resolve errors
public class TextRecognitionActivity extends Activity {

    private String extractTextFromImage(String imagePath) {
        // Create an InputImage object from the provided image path
        InputImage image;
        try {
            image = InputImage.fromFilePath(getApplicationContext(), Uri.parse(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return empty string if image path is invalid
        }

        // Create a TextRecognizer using ML Kit TextRecognition.getClient()
        TextRecognizer recognizer = TextRecognition.getClient();

        // Process the image and extract text
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        // Handle text recognition success
                        List<Text.TextBlock> blocks = text.getTextBlocks();
                        StringBuilder extractedText = new StringBuilder();
                        for (Text.TextBlock block : blocks) {
                            List<Text.Line> lines = block.getLines();
                            for (Text.Line line : lines) {
                                extractedText.append(line.getText()).append("\n");
                            }
                        }

                        // Store extracted text to a string and return it
                        String extractedString = extractedText.toString().trim();
                        // You can do further processing or store the string to any variable you need
                        // For example: SomeClass.setRecognizedText(extractedString);
                        // ...

                        // Log the extracted text for testing
                        Log.d("Text Recognition", extractedString);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle text recognition failure
                        e.printStackTrace();
                    }
                });

        return ""; // Return empty string immediately after starting the process (text will be returned asynchronously)
    }
}

*/

/*
//Create function for scanning barcode - Integrate and resolve errors
public class BarcodeScannerActivity extends AppCompatActivity {
    private static final String TAG = "BarcodeScannerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        // Load the stored photo as a Bitmap
        Bitmap barcodeBitmap = BitmapFactory.decodeFile("/path/to/photo.jpg");

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
                            returnBarcodeInformation(rawValue);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occur during the barcode scanning process
                        Log.e(TAG, "Barcode scanning failed. Error: " + e.getMessage());
                    }
                });
    }

    private void returnBarcodeInformation(String barcodeInformation) {
        // Return the barcode information to the main activity
        // You can use an intent or any other suitable method to return the string.
    }
}
*/

}
