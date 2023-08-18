package com.example.idscanningapp;

import android.app.Activity;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.API.RestClient;
import com.example.idscanningapp.API.VolleyCallBack_User;
import com.example.idscanningapp.API.VolleyCallBack_UserRole;
import com.example.idscanningapp.Models.User;
import com.example.idscanningapp.Models.UserRole;

import java.util.ArrayList;
import java.util.UUID;

public class UserManagementActivity extends AppCompatActivity {

    public static final String TAG = "userManagementActivity";
    private ArrayList<User> userData;

    private ArrayList<UserRole> userRoleData;
    private String token;
    private String username;
    private RecyclerView recordResults;
    private UserManagementAdapter userManagementAdapter;
    private String searchString;
    private User selecteduser;

    private View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            UUID customerID = userData.get(position).getID();


            RestClient.executeUserGetOneRequest(MainActivity.VERIFYMEAPI + "user/" + customerID,
                    token,
                    UserManagementActivity.this,
                    new VolleyCallBack_User() {
                        @Override
                        public void onSuccess(ArrayList<User> results) {
                            selecteduser = results.get(0);
                        }
            });
        }
    };
    private View.OnClickListener btnViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: Clicked View");
        }
    };
    private View.OnClickListener btnUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            initPopupCUD(view, "update");
        }
    };
    private View.OnClickListener btnDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            initPopupCUD(view, "delete");
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_user_management);
        setTitle("User Management");

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        username = bundle.getString("username");

        initLoadUserData();
        initLoadUserRoleData();
        initAddButton();
        initBackButton();

    }
    private void initLoadUserData() {
        RestClient.executeUserGetRequest(MainActivity.VERIFYMEAPI + "user/",
                token,
                this,
                new VolleyCallBack_User() {
                    @Override
                    public void onSuccess(ArrayList<User> results) {
                        Log.d(TAG, "onSuccess: Here");
                        userData = results;

                        if (userData != null) {
                            Log.d(TAG, "onSuccess: HERE NOT NULL");
                            RebindScreen();
                        }
                    }
                });
    }
    private void initLoadUserRoleData() {
        RestClient.executeUserRoleGetRequest(MainActivity.VERIFYMEAPI + "userrole/",
                token,
                UserManagementActivity.this,
                new VolleyCallBack_UserRole() {
                    @Override
                    public void onSuccess(ArrayList<UserRole> results) {
                        if (results != null)
                            userRoleData = results;
                        else
                            userRoleData = null;
                    }
                }
        );
    }

    private void initAddButton() {
        Button btnAdd = findViewById(R.id.UserManagement_btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupCUD(view, "create");
                Toast.makeText(UserManagementActivity.this, "You Are Adding A User", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initBackButton() {
        Button btnBack = findViewById(R.id.UserManagement_btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initPopupCUD(View parentView, String cudOp) {
        if(cudOp == "create" || cudOp == "update") {

            // Create A Popup Window That Will Execute CUD Operations On Users
            LayoutInflater inflater = LayoutInflater.from(UserManagementActivity.this);
            View popupView = inflater.inflate(R.layout.activity_user_management_cud, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            Activity currentActivity = UserManagementActivity.this;

            popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);



            Button btnCreate = popupView.findViewById(R.id.UserManagement_CUD_btnCreate);
            Button btnUpdate = popupView.findViewById(R.id.UserManagement_CUD_btnUpdate);
            Button btnDelete = popupView.findViewById(R.id.UserManagement_CUD_btnDelete);
            Button btnCancel = popupView.findViewById(R.id.UserManagement_CUD_btnCancel);

            EditText etFirstName = popupView.findViewById(R.id.UserManagement_CUD_etFirstName);
            EditText etLastName = popupView.findViewById(R.id.UserManagement_CUD_etLastName);
            EditText etUsername = popupView.findViewById(R.id.UserManagement_CUD_etUsername);
            EditText etPassword = popupView.findViewById(R.id.UserManagement_CUD_etPassword);
            EditText etPasswordConfirm = popupView.findViewById(R.id.UserManagement_CUD_etPasswordConfirm);

            //ERROR OCCURRING WHEN TRYING TO UTILIZE THE SPINNER WITH USER ROLES. UNABLE TO RESOLVE. LEAVING OUT OF FINAL DESIGN
            /* Spinner spUserRole = popupView.findViewById(R.id.UserManagement_CUD_spUserRole);

            if (!userRoleData.isEmpty()) {
                Log.d(TAG, "initPopupCUD: " + userRoleData.size());
                Log.d(TAG, "initPopupCUD: " + userRoleData.get(0).getID() + " " + userRoleData.get(0).getRole());

                userRoleData.add(0, new UserRole(UUID.fromString("00000000-0000-0000-0000-000000000000"), "SELECT A USER ROLE"));

                // Create Adapter to bind the Spinner to the Incidents
                ArrayAdapter<UserRole> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, userRoleData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Attach Adapter to Spinner
                spUserRole.setAdapter(adapter);

                Log.d(TAG, "initPopupCUD: " + spUserRole.getSelectedItem().toString());
                // Reset background when a user clicks on a new item within the spinner
                spUserRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //spUserRole.setBackground(getDrawable(R.drawable.default_background_border));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }*/


            if (cudOp.equals("create")){
                // Execute Code To Make activity_user_management_add_edit_delete Able To Create New User
                btnCreate.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);

                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etFirstName.getText().toString().isEmpty() ||
                                etLastName.getText().toString().isEmpty() ||
                                etUsername.getText().toString().isEmpty() ||
                                etPassword.getText().toString().isEmpty() ||
                                etPasswordConfirm.getText().toString().isEmpty()) {
                            // Send ERROR Message and make screen red

                            etFirstName.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                            etLastName.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                            etUsername.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                            etPassword.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                            etPasswordConfirm.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                            Toast.makeText(UserManagementActivity.this, "One or More of The Fields is Null", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
                                Log.d(TAG, "onClick: Passwords Correct");
                                // If the Passwords match
                                User user = new User();
                                user.setFirstName(etFirstName.getText().toString());
                                user.setLastName(etLastName.getText().toString());
                                user.setUsername(etUsername.getText().toString());
                                user.setPassword(etPassword.getText().toString());
                                user.setUserRoleID(userRoleData.get(0).getID());

                                Log.d(TAG, "DoRequest: HERE");
                                RestClient.executeUserPostRequest(user,
                                        MainActivity.VERIFYMEAPI + "user/",
                                        token,
                                        UserManagementActivity.this,
                                        new VolleyCallBack_User() {
                                            @Override
                                            public void onSuccess(ArrayList<User> results) {

                                            }
                                        });
                                Toast.makeText(UserManagementActivity.this, "Customer Has Been Submitted", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }
                            else {
                                etPassword.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                                etPasswordConfirm.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                                Toast.makeText(UserManagementActivity.this, "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
            else if (cudOp.equals("update")) {
                // Execute Code To Make activity_user_management_add_edit_delete Able To Update Existing User
                btnCreate.setVisibility(View.INVISIBLE);
                btnUpdate.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);



                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(UserManagementActivity.this, "UPDATE USER", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
        }
        else if (cudOp.equals("delete")) {
            // Execute Code To Make activity_user_management_add_edit_delete Able To Delete Existing User

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_user_management_delete, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);


            Toast.makeText(UserManagementActivity.this, "DELETE USER", Toast.LENGTH_SHORT).show();

            Button btnYes = popupView.findViewById(R.id.PopupDelete_btnYes);
            Button btnNo = popupView.findViewById(R.id.PopupDelete_tvCancel);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RestClient.executeUserDeleteRequest(selecteduser,
                            MainActivity.VERIFYMEAPI + "user/" + selecteduser.getID(),
                            token,
                            UserManagementActivity.this,
                            new VolleyCallBack_User() {
                                @Override
                                public void onSuccess(ArrayList<User> results) {

                                }
                            });
                    Toast.makeText(UserManagementActivity.this, "User Has Been Deleted", Toast.LENGTH_SHORT);
                    popupWindow.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
        }

    }
    private void DoRequest(EditText firstname,
                           EditText lastname,
                           EditText username,
                           EditText password,
                           EditText passwordconfirm,
                           String requesttype) {
        Log.d(TAG, "DoRequest: HERE");
        if (firstname.getText().toString().isEmpty() ||
            lastname.getText().toString().isEmpty() ||
            username.getText().toString().isEmpty() ||
            password.getText().toString().isEmpty() ||
            passwordconfirm.getText().toString().isEmpty()) {
            // Send ERROR Message and make screen red

            firstname.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
            lastname.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
            username.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
            password.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
            passwordconfirm.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
            Toast.makeText(UserManagementActivity.this, "One or More of The Fields is Null", Toast.LENGTH_SHORT).show();
        }
        else {
            if (password.getText().toString().equals(passwordconfirm.getText().toString())){
                // If the Passwords match
                User user = new User();
                user.setFirstName(firstname.getText().toString());
                user.setLastName(lastname.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setUserRoleID(userRoleData.get(0).getID());

                Log.d(TAG, "DoRequest: HERE");
                if (requesttype == "post") {
                    RestClient.executeUserPostRequest(user,
                            MainActivity.VERIFYMEAPI + "user/",
                            token,
                            UserManagementActivity.this,
                            new VolleyCallBack_User() {
                                @Override
                                public void onSuccess(ArrayList<User> results) {
                                    Toast.makeText(UserManagementActivity.this, "Customer Has Been Submitted", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else if (requesttype == "put") {
                    RestClient.executeUserPutRequest(user,
                            MainActivity.VERIFYMEAPI + "user/" + user.getID().toString(),
                            token,
                            UserManagementActivity.this,
                            new VolleyCallBack_User() {
                                @Override
                                public void onSuccess(ArrayList<User> results) {
                                    Toast.makeText(UserManagementActivity.this, "Customer Has Been Submitted", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            else {
                password.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                passwordconfirm.setBackground(getDrawable(R.drawable.edit_text_error_background_border));
                Toast.makeText(UserManagementActivity.this, "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void RebindScreen() {
        recordResults = findViewById(R.id.UserManagement_rvSearchResults);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recordResults.setLayoutManager(layoutManager);

        userManagementAdapter = new UserManagementAdapter(userData, this);
        userManagementAdapter.setItemOnClickListener(itemOnClickListener);
        userManagementAdapter.setBtnViewOnClickListener(btnViewOnClickListener);
        userManagementAdapter.setBtnUpdateOnClickListener(btnUpdateOnClickListener);
        userManagementAdapter.setBtnDeleteOnClickListener(btnDeleteOnClickListener);

        recordResults.setAdapter(userManagementAdapter);
    }
}
