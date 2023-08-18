package com.example.idscanningapp;

import static com.example.idscanningapp.MainActivity.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


// Todo Clean up code
// Todo Add flash functionality
// Todo add flip functionality
// Todo Freeze photo image once taken until accept or reject is hit

// Todo add Nav menu to top right of screen

public class ScanningCameraActivity extends AppCompatActivity {

    //public class MainActivity extends AppCompatActivity {
    ImageButton capture, toggleFlash, flipCamera;
    // Set up Button Variables
    private Button Flip_Photo_Button_L;
    private Button Flip_Photo_Button_R;
    private Button Front_Photo_Button;
    private Button Back_Photo_Button;
    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    // Set up photo taken indicators
    protected int photo_taken_f = 0;
    protected int photo_taken_b = 0;

    public static final String TAG = "scanningCameraActivity";
    public int ID = 1;
    private String token;
    private String username;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                startCamera(cameraFacing);
            }
        }
    });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_camera);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");


        Log.d(TAG, "Nav - " + username);

        previewView = findViewById(R.id.cameraPreview);
        //capture = findViewById(R.id.capture);
        //toggleFlash = findViewById(R.id.toggleFlash);
        //flipCamera = findViewById(R.id.flipCamera);

        // Find the button by its ID
        // Flip_Photo_Button_L = view.findViewById(R.id.Flip_Photo_Button_L);
        // Flip_Photo_Button_R = view.findViewById(R.id.Flip_Photo_Button_R);
        Front_Photo_Button = findViewById(R.id.Front_Photo_Button);
        Back_Photo_Button = findViewById(R.id.Back_Photo_Button);

        // Sets buttons initial text
        Front_Photo_Button.setText("Front");
        Back_Photo_Button.setText("Back");


        if (ContextCompat.checkSelfPermission(ScanningCameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }
        /*
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                    cameraFacing = CameraSelector.LENS_FACING_FRONT;
                } else {
                    cameraFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera(cameraFacing);
            }
        });
        */
    }

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                // Insert if statements for front button click
                Front_Photo_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        if (ContextCompat.checkSelfPermission(ScanningCameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        } else {
                            takePicture(imageCapture);
                        }*/
                        // Takes Photo if clicked
                        if (Front_Photo_Button.getText() == "Front" && photo_taken_b == 0) {
                            // Save photo
                            takePicture(imageCapture, "Front");
                            Front_Photo_Button.setText("Retake");
                            photo_taken_f = 1;
                        }  else if (Front_Photo_Button.getText() == "Retake") {
                            // Retakes photo
                            Front_Photo_Button.setText("Retake");
                            photo_taken_f = 1;
                        } else if (Front_Photo_Button.getText() == "Front" && photo_taken_b == 1) {
                            String path = takePicture(imageCapture, "Front");                  // Change to back only name
                            photo_taken_f = 0;
                            photo_taken_b = 0;
                            Back_Photo_Button.setText("Back");
                            // move to Scanning Activity
                            Toast.makeText(ScanningCameraActivity.this, path +" CA Test.", Toast.LENGTH_LONG);
                            Intent intent = new Intent(ScanningCameraActivity.this, ScanningReviewActivity.class);
                            intent.putExtra("back",path);
                            startActivity(intent);
                        }
                    }
                });

                Back_Photo_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle button click event here
                        if (Back_Photo_Button.getText() == "Back" && photo_taken_f == 0) {
                            // Save photo
                            takePicture(imageCapture, "Back" );                  // Change to back only name
                            Back_Photo_Button.setText("Retake");
                            photo_taken_b = 1;
                        } else if (Back_Photo_Button.getText() == "Retake") {
                            // Retakes photo
                            takePicture(imageCapture, "Back" );
                            Back_Photo_Button.setText("ReTake");
                            photo_taken_b = 1;
                        } else if (Back_Photo_Button.getText() == "Back" && photo_taken_f == 1) {
                            // Save Photo
                            String path = takePicture(imageCapture, "Back");                  // Change to back only name
                            photo_taken_b = 0;
                            photo_taken_f = 0;
                            Front_Photo_Button.setText("Front");
                            // move to Scanning Activity
                            //Toast.makeText(ScanningCameraActivity.this, path +" CA Test.", Toast.LENGTH_LONG);
                            Intent intent = new Intent(ScanningCameraActivity.this, ScanningReviewActivity.class);
                            intent.putExtra("back",path);
                            startActivity(intent);
                            //startActivity(new Intent(view.getContext(), ScanningReviewActivity.class));       // pass file locations to ScanningActivity
                        }
                    }
                });

                /*
                toggleFlash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setFlashIcon(camera);
                    }
                });
                */
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // TODO Return file path and name
    public String takePicture(ImageCapture imageCapture, String Front_Back) {
        final File file = new File(getExternalFilesDir(null), Front_Back + ".jpg");                 //final File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        String fileLocation = file.getAbsolutePath() + file.getAbsoluteFile();
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanningCameraActivity.this, "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "The photo is saved at: " + file.getPath());
                    }
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanningCameraActivity.this, "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startCamera(cameraFacing);
            }
        });
        //Toast.makeText(ScanningCameraActivity.this, "The File location: " + fileLocation, Toast.LENGTH_SHORT).show();
        return (fileLocation);
    }

    /*
    private void setFlashIcon(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                toggleFlash.setImageResource(R.drawable.baseline_flash_off_24);
            } else {
                camera.getCameraControl().enableTorch(false);
                toggleFlash.setImageResource(R.drawable.baseline_flash_on_24);
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ScanningCameraActivity.this, "Flash is not available currently", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
*/
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
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
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            Log.d(TAG, "onOptionsItemSelected: " + intent.getExtras().getString("token"));
            Log.d(TAG, "onOptionsItemSelected: " + intent.getExtras().getString("username"));

            startActivity(intent);

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
}