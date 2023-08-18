package com.example.idscanningapp.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idscanningapp.Models.Customer;
import com.example.idscanningapp.Models.CustomerCheckinRecord;
import com.example.idscanningapp.Models.CustomerIncident;
import com.example.idscanningapp.Models.Incident;
import com.example.idscanningapp.Models.User;
import com.example.idscanningapp.Models.UserRole;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class RestClient {
    public static final String TAG = "RESTClient";


    private static Date formatDate(String inputtedDateString) {
        // Used to quickly format a date to the format of - yyyy-MM-dd'T'HH:mm:ss
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(inputtedDateString);
        }
        catch (Exception e)
        {
            Log.d(TAG, "ERROR: Parsing Date - " + inputtedDateString + " could not be completed: " + e.getMessage());
            return null;
        }
    }
    private static HashMap<String, String> getheaders(String token){
        // Hash map to add header file with the token to access information
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "bearer " + token);
        return headers;
    }

    //region User REST Client Requests
    public static void executeLoginRequest(String url,
                                           String requestBody,
                                           Context context,
                                           VolleyCallBack_String volleyCallBack_string) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Return the access_token
                            JSONObject object = new JSONObject(response);

                            volleyCallBack_string.onSuccess(object.getString("access_token"));
                        }
                        catch (JSONException e) {
                            volleyCallBack_string.onSuccess("Error");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack_string.onSuccess("Invalid");
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "text/plain; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody() {
                        return requestBody.getBytes();
                    }
                };

        queue.add(stringRequest);
    }
    public static void executeUserGetRequest(String url,
                                             String token,
                                             Context context,
                                             VolleyCallBack_User volleyCallBack_user){
        Log.d(TAG, "ENTERED GET USER");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<User> users = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);

                                    User user = new User();
                                    user.setID(UUID.fromString(object.getString("ID")));
                                    user.setUserRoleID(UUID.fromString(object.getString("UserRoleID")));
                                    user.setUsername(object.getString("Username"));
                                    user.setPassword(object.getString("Password"));
                                    user.setFirstName(object.getString("FirstName"));
                                    user.setLastName(object.getString("LastName"));

                                    users.add(user);

                                    volleyCallBack_user.onSuccess(users);
                                }
                            }
                            catch (JSONException jsone) {
                                Log.d(TAG, "ERROR - executeUserGetRequest JSON Request Error - Message: " + jsone.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "ERROR - executeUserGetRequest Error - Message: " + error.getMessage());
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeUserGetRequest Error - Message: " + e.getMessage());
        }
    }
    public static void executeUserGetOneRequest(String url,
                                                String token,
                                                Context context,
                                                VolleyCallBack_User volleyCallBack_user) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<User> users = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                User user = new User();
                                user.setID(UUID.fromString(object.getString("ID")));
                                user.setUserRoleID(UUID.fromString(object.getString("UserRoleID")));
                                user.setUsername(object.getString("Username"));
                                user.setPassword(object.getString("Password"));
                                user.setFirstName(object.getString("FirstName"));
                                user.setLastName(object.getString("LastName"));

                                users.add(user);

                                volleyCallBack_user.onSuccess(users);
                            } catch (JSONException e) {
                                Log.d(TAG, "ERROR - executeUserGetRequest Error - Message: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "ERROR - executeUserGetRequest Error - Message: " + error.getMessage());
                        }
                    }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getheaders(token);
                    }
                };

            queue.add(stringRequest);
        } catch (Exception e) {
            Log.d(TAG, "ERROR - executeUserGetRequest Error - Message: " + e.getMessage());
        }
    }
    public static void executeUserPostRequest(User user,
                                              String url,
                                              String token,
                                              Context context,
                                              VolleyCallBack_User volleyCallBack_user){
        try {
            executeUserRequest(user, url, token, context, volleyCallBack_user, Request.Method.POST);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - INSERTING USER: " + e.getMessage());
        }
    }
    public static void executeUserPutRequest(User user,
                                             String url,
                                             String token,
                                             Context context,
                                             VolleyCallBack_User volleyCallBack_user){
        try {
            executeUserRequest(user, url, token, context, volleyCallBack_user, Request.Method.PUT);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - UPDATING USER: " + e.getMessage());
        }

    }
    public static void executeUserDeleteRequest(User user,
                                                String url,
                                                String token,
                                                Context context,
                                                VolleyCallBack_User volleyCallBack_user){
        try {
            executeUserRequest(user, url, token ,context, volleyCallBack_user, Request.Method.DELETE);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - DELETING USER: " + e.getMessage());
        }
    }
    public static void executeUserRequest(User user,
                                          String url,
                                          String token,
                                          Context context,
                                          VolleyCallBack_User volleyCallBack_user,
                                          int requestMethod){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            Log.d(TAG, "executeUserRequest: " + user.getID());

            JSONObject object = new JSONObject();
            object.put("ID", null);
            object.put("UserRoleID", user.getUserRoleID());
            object.put("Username", user.getUsername());
            object.put("Password", user.getPassword());
            object.put("FirstName", user.getFirstName());
            object.put("LastName", user.getLastName());

            final String requestBody = object.toString();
            Log.d(TAG, "executeUserRequest: " + object);

            JsonObjectRequest request = new JsonObjectRequest(requestMethod, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "VOLLEY ERROR - USER: " + error.getMessage());
                }
            });/*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getheaders(token);
                }
            };*/

            queue.add(request);
        }
        catch (JSONException e) {
            Log.d(TAG, "ERROR - executeUserRequest: " + e.getMessage());
        }
    }
    //endregion

    //region Customer REST Client Requests
    public static void executeCustomerGetRequest(String url,
                                                 String token,
                                                 Context context,
                                                 VolleyCallBack volleyCallBack)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Customer> customers = new ArrayList<>();

        // Hash map to add header file with the token to access information
        try{
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d(TAG, "Entered executeCustomerGetRequest -> onResponse() - Response String = " + response);
                                
                                // JSON Array that stores all the data from the response string we receive from the GET Method and URL
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    // JSON Object that stores data of a single JSON item from the JSON Array
                                    JSONObject object = items.getJSONObject(i);

                                    // Create a new Customer Object and set properties equal to data from JSON object
                                    Customer customer = new Customer();
                                    customer.setCustomerID(UUID.fromString(object.getString("ID")));
                                    customer.setFirstName(object.getString("FirstName"));
                                    customer.setMiddleName(object.getString("MiddleName"));
                                    customer.setLastName(object.getString("LastName"));
                                    customer.setDOB(formatDate(object.getString("DOB")));
                                    customer.setISS(formatDate(object.getString("ISS")));
                                    customer.setEXP(formatDate(object.getString("EXP")));
                                    customer.setDLN(object.getString("DLN"));
                                    customer.setDD(object.getString("DD"));
                                    customer.setStreet(object.getString("Address"));
                                    customer.setCity(object.getString("City"));
                                    customer.setState(object.getString("State"));
                                    customer.setCountry(object.getString("Country"));
                                    customer.setZip(object.getString("Zip"));
                                    customer.setSex(object.getString("Sex").charAt(0));
                                    customer.setEyeColor(object.getString("EyeColor"));
                                    customer.setHairColor(object.getString("HairColor"));
                                    customer.setHeight(object.getString("Height"));
                                    customer.setWeight(object.getString("Weight"));
                                    //customer.setIDFront(object.getString("IDFront"));
                                    //customer.setIDBack(object.getString("IDBack"));

                                    // Add the Customer object into the list of customers created prior
                                    customers.add(customer);

                                    volleyCallBack.onSuccess(customers);
                                }
                            } catch (JSONException jsone) {
                                Log.d(TAG, "GET JSON ERROR: " + jsone.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "GET VOLLEY ERROR: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getheaders(token);
                }
            };

            queue.add(stringRequest);
        }
        catch(Exception e) {
            Log.d(TAG, "GET ERROR: " + e.getMessage());
        }
    }
    public static void executeCustomerGetOneRequest(String url,
                                                    String token,
                                                    Context context,
                                                    VolleyCallBack volleyCallBack)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Customer> customers = new ArrayList<>();

        try{
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d(TAG, "Entered executeCustomerGetRequest -> onResponse() - Response String = " + response);

                                JSONObject object = new JSONObject(response);

                                Customer customer = new Customer();
                                customer.setCustomerID(UUID.fromString(object.getString("ID")));
                                customer.setFirstName(object.getString("FirstName"));
                                customer.setMiddleName(object.getString("MiddleName"));
                                customer.setLastName(object.getString("LastName"));
                                customer.setDOB(formatDate(object.getString("DOB")));
                                customer.setISS(formatDate(object.getString("ISS")));
                                customer.setEXP(formatDate(object.getString("EXP")));
                                customer.setDLN(object.getString("DLN"));
                                customer.setDD(object.getString("DD"));
                                customer.setStreet(object.getString("Address"));
                                customer.setCity(object.getString("City"));
                                customer.setState(object.getString("State"));
                                customer.setCountry(object.getString("Country"));
                                customer.setZip(object.getString("Zip"));
                                customer.setSex(object.getString("Sex").charAt(0));
                                customer.setEyeColor(object.getString("EyeColor"));
                                customer.setHairColor(object.getString("HairColor"));
                                customer.setHeight(object.getString("Height"));
                                customer.setWeight(object.getString("Weight"));
                                //customer.setIDFront(object.getString("IDFront"));
                                //customer.setIDBack(object.getString("IDBack"));

                                customers.add(customer);

                                volleyCallBack.onSuccess(customers);
                            } catch (JSONException jsone) {
                                Log.d(TAG, "GET JSON ERROR: " + jsone.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "GET VOLLEY ERROR: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getheaders(token);
                }
            };

            queue.add(stringRequest);
        }
        catch(Exception e) {
            Log.d(TAG, "GET ERROR: " + e.getMessage());
        }
    }
    public static void executeCustomerPostRequest(Customer customer,
                                                  String url,
                                                  String token,
                                                  Context context,
                                                  VolleyCallBack_String volleyCallBack_string){
        try {
            executeCustomerRequest(customer, url, token, context, volleyCallBack_string, Request.Method.POST);
        }
        catch (Exception e) {
            Log.d(TAG, "POST ERROR: " + e.getMessage());
        }
    }
    public static void executeCustomerPutRequest(Customer customer,
                                                 String url,
                                                 String token,
                                                 Context context,
                                                 VolleyCallBack_String volleyCallBack_string){
        try {
            executeCustomerRequest(customer, url, token, context, volleyCallBack_string, Request.Method.PUT);
        }
        catch (Exception e) {
            Log.d(TAG, "PUT ERROR: " + e.getMessage());
        }
    }
    public static void executeCustomerDeleteRequest(Customer customer,
                                                    String url,
                                                    String token,
                                                    Context context,
                                                    VolleyCallBack_String volleyCallBack_string){
        try {
            executeCustomerRequest(customer, url, token, context, volleyCallBack_string, Request.Method.DELETE);
        }
        catch (Exception e) {
            Log.d(TAG, "DELETE ERROR: " + e.getMessage());
        }
    }
    public static void executeCustomerRequest(Customer customer,
                                              String url,
                                              String token,
                                              Context context,
                                              VolleyCallBack_String volleyCallBack_string,
                                              int requestMethod){
       try {
           // ERROR - Resulting from the use of a header within the application.
           // Does not update database with the use of header in format x-www-urlencoded, cannot resolve
           // Removed Authorization within the API to use ADD a customer. Will Look at resolving later
           RequestQueue queue = Volley.newRequestQueue(context);

           JSONObject object = new JSONObject();
//           object.put("ID", (customer.getCustomerID() == null) ? "null" : customer.getCustomerID().toString()) ;
           object.put("ID", "");
           object.put("FirstName", customer.getFirstName());
           object.put("MiddleName", customer.getMiddleName());
           object.put("LastName", customer.getLastName());
           object.put("DOB", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getDOB()));
           object.put("ISS", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getISS()));
           object.put("EXP", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getEXP()));
           object.put("DLN", customer.getDLN());
           object.put("DD", customer.getDD());
           object.put("Address", customer.getStreet());
           object.put("City", customer.getCity());
           object.put("State", customer.getState());
           object.put("Country", customer.getCountry());
           object.put("Zip", customer.getZip());
           object.put("Sex", "U");
           //params.put("Sex", ""+customer.getSex());
           object.put("EyeColor", customer.getEyeColor());
           object.put("HairColor", customer.getHairColor());
           object.put("Height", customer.getHeight());
           object.put("Weight", customer.getWeight());
           object.put("IDFront", null);
           object.put("IDBack", null);

           JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                   url,
                   object,
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           Log.d(TAG, "onResponse: " +response);
                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.d(TAG, "onErrorResponse: " + error.getMessage());
               }
           }); /*{
               public Map<String, String> getHeaders() throws AuthFailureError {
                   return getheaders(token);
               }
               public Map<String, String> getParams() throws AuthFailureError {
                   RequestQueue queue = Volley.newRequestQueue(context);

                   Map<String, String> params = new HashMap<>();
                   params.put("Content-Type", "application/json");
                   params.put("ID", (customer.getCustomerID() == null) ? "null" : customer.getCustomerID().toString()) ;
                   params.put("FirstName", customer.getFirstName());
                   params.put("MiddleName", customer.getMiddleName());
                   params.put("LastName", customer.getLastName());
                   params.put("DOB", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getDOB()));
                   params.put("ISS", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getISS()));
                   params.put("EXP", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customer.getEXP()));
                   params.put("DLN", customer.getDLN());
                   params.put("DD", customer.getDD());
                   params.put("Address", customer.getStreet());
                   params.put("City", customer.getCity());
                   params.put("State", customer.getState());
                   params.put("Country", customer.getCountry());
                   params.put("Zip", customer.getZip());
                   params.put("Sex", "D");
                   //params.put("Sex", ""+customer.getSex());
                   params.put("EyeColor", customer.getEyeColor());
                   params.put("HairColor", customer.getHairColor());
                   params.put("Height", customer.getHeight());
                   params.put("Weight", customer.getWeight());
                   params.put("IDFront", "null");
                   params.put("IDBack", null);
                   return params;
               }
           };*/

           queue.add(request);
       }
       catch (JSONException ex) {
           Log.d(TAG, "executeCustomerRequest: " + ex.getMessage());
       }
    }
    //endregion

    //region Incident REST Client Requests
    public static void executeIncidentGetRequest(String url,
                                                 String token,
                                                 Context context,
                                                 VolleyCallBack_Incident volleyCallBack_incident)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Incident> incidents = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);

                                    Incident incident = new Incident();
                                    incident.setID(UUID.fromString(object.getString("ID")));
                                    incident.setName(object.getString("Name"));

                                    incidents.add(incident);
                                }

                                volleyCallBack_incident.onSuccess(incidents);
                            } catch (JSONException e) {
                                Log.d(TAG, "ERROR OCCURRED WITH ExecuteIncidentGetRequest JSON - MESSAGE: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG,"VOLLEY ERROR WITHIN INCIDENT");
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getheaders(token);
                }
            };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR OCCURRED WITH ExecuteIncidentGetRequest - MESSAGE: " + e.getMessage());
        }
    }
    public static void executeIncidentGetOneRequest(String url,
                                                    String token,
                                                    Context context,
                                                    VolleyCallBack_Incident volleyCallBack)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Incident> incidents = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object  = new JSONObject(response);

                                Incident incident = new Incident();
                                incident.setID(UUID.fromString(object.getString("ID")));
                                incident.setName(object.getString("Name"));

                                incidents.add(incident);

                                volleyCallBack.onSuccess(incidents);
                            } catch (JSONException e) {
                                Log.d(TAG, "ERROR OCCURRED WITH ExecuteIncidentGetRequest JSONOBJECT - MESSAGE: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "ERROR OCCURRED WITH ExecuteIncidentGetRequest VOLLEY- MESSAGE: " + error.getMessage());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getheaders(token);
                }
            };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR OCCURRED WITH ExecuteIncidentGetRequest - MESSAGE: " + e.getMessage());
        }
    }
    /* UNNEEDED METHODS
    public static void executeIncidentPostRequest(){

    }
    public static void executeIncidentPutRequest(){

    }
    public static void executeIncidentDeleteRequest(){

    }
    public static void executeIncidentRequest(){

    }*/
    //endregion

    //region UserRole REST Client Requests
    public static void executeUserRoleGetRequest(String url,
                                                 String token,
                                                 Context context,
                                                 VolleyCallBack_UserRole volleyCallBack_userRole){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<UserRole> userRoles = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);

                                    UserRole userRole = new UserRole();
                                    userRole.setID(UUID.fromString(object.getString("ID")));
                                    userRole.setRole(object.getString("Role"));

                                    userRoles.add(userRole);
                                    volleyCallBack_userRole.onSuccess(userRoles);
                                }
                            }
                            catch (JSONException jsone) {
                                Log.d(TAG, "ERROR - JSON Exception UserRole - " + jsone.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeUserRoleGetRequest: " + e.getMessage());
        }
    }
    public static void executeUserRoleGetOneRequest(String url,
                                                    String token,
                                                    Context context,
                                                    VolleyCallBack_UserRole volleyCallBack_userRole){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<UserRole> userRoles = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                UserRole userRole = new UserRole();
                                userRole.setID(UUID.fromString(object.getString("ID")));
                                userRole.setRole(object.getString("Role"));

                                userRoles.add(userRole);
                                volleyCallBack_userRole.onSuccess(userRoles);

                            }
                            catch (JSONException jsone) {
                                Log.d(TAG, "ERROR - JSON Exception UserRole - " + jsone.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeUserRoleGetRequest: " + e.getMessage());
        }
    }
    /* UNNEEDED METHODS AT THE PRESENT MOMENT
    public static void executeUserRolePostRequest(){

    }
    public static void executeUserRolePutRequest(){

    }
    public static void executeUserRoleDeleteRequest(){

    }
    public static void executeUserRoleRequest(){

    }*/
    //endregion

    //region CustomerIncident REST Client Requests
    public static void executeCustomerIncidentGetRequest(String url,
                                                         String token,
                                                         Context context,
                                                         VolleyCallBack_CustomerIncident volleyCallBack_customerIncident){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<CustomerIncident> customerIncidents = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);

                                    CustomerIncident customerIncident = new CustomerIncident();
                                    customerIncident.setID(UUID.fromString(object.getString("ID")));
                                    customerIncident.setIncidentID(UUID.fromString(object.getString("IncidentID")));
                                    customerIncident.setCustomerID(UUID.fromString(object.getString("CustomerID")));
                                    customerIncident.setUserID(UUID.fromString(object.getString("UserID")));
                                    customerIncident.setIncidentDate(formatDate(object.getString("Date")));
                                    customerIncident.setFlagLevel(Integer.parseInt(object.getString("FlagLevel")));
                                    customerIncident.setAdditionalInfo(object.getString("AdditionalInformation"));

                                    customerIncidents.add(customerIncident);
                                    volleyCallBack_customerIncident.onSuccess(customerIncidents);
                                }
                            } catch (JSONException e) {
                                Log.d(TAG, "ERROR - JSON ERROR - CustomerIncidents: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerIncidentGetRequest - " + e.getMessage());
        }
    }
    public static void executeCustomerIncidentGetByCustomerID(String url,
                                                              String token,
                                                              Context context,
                                                              VolleyCallBack_CustomerIncident volleyCallBack_customerIncident){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<CustomerIncident> customerIncidents = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);

                                    Log.d(TAG, "onResponse: " + object.toString());
                                    CustomerIncident customerIncident = new CustomerIncident();
                                    customerIncident.setID(UUID.fromString(object.getString("ID")));
                                    customerIncident.setIncidentID(UUID.fromString(object.getString("IncidentID")));
                                    customerIncident.setIncidentName(object.getString("IncidentName"));
                                    customerIncident.setCustomerID(UUID.fromString(object.getString("CustomerID")));
                                    customerIncident.setUserID(UUID.fromString(object.getString("UserID")));
                                    customerIncident.setUserInitials(object.getString("UserInitials"));
                                    customerIncident.setAdditionalInfo(object.getString("AdditionalInformation"));
                                    customerIncident.setIncidentDate(formatDate(object.getString("Date")));
                                    customerIncident.setFlagLevel(Integer.parseInt(object.getString("FlagLevel")));

                                    customerIncidents.add(customerIncident);
                                    volleyCallBack_customerIncident.onSuccess(customerIncidents);
                                }
                            }
                            catch (JSONException e) {
                                Log.d(TAG, "ERROR: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {

        }
    }
    public static void executeCustomerIncidentGetOneRequest(String url,
                                                            String token,
                                                            Context context,
                                                            VolleyCallBack_CustomerIncident volleyCallBack_customerIncident){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<CustomerIncident> customerIncidents = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                CustomerIncident customerIncident = new CustomerIncident();
                                customerIncident.setID(UUID.fromString(object.getString("ID")));
                                customerIncident.setIncidentID(UUID.fromString(object.getString("IncidentID")));
                                customerIncident.setCustomerID(UUID.fromString(object.getString("CustomerID")));
                                customerIncident.setUserID(UUID.fromString(object.getString("UserID")));
                                customerIncident.setIncidentDate(formatDate(object.getString("Date")));
                                customerIncident.setFlagLevel(Integer.parseInt(object.getString("FlagLevel")));
                                customerIncident.setAdditionalInfo(object.getString("AdditionalInformation"));

                                customerIncidents.add(customerIncident);
                                volleyCallBack_customerIncident.onSuccess(customerIncidents);

                            } catch (JSONException e) {
                                Log.d(TAG, "ERROR - JSON ERROR - CustomerIncidents: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerIncidentGetRequest - " + e.getMessage());
        }
    }
    public static void executeCustomerIncidentPostRequest(CustomerIncident customerIncident,
                                                          String url,
                                                          String token,
                                                          Context context,
                                                          VolleyCallBack_String volleyCallBack_string){
        Log.d(TAG, "executeCustomerIncidentPostRequest: POST");
        try {
            executeCustomerIncidentRequest(customerIncident, url, token, context, volleyCallBack_string, Request.Method.POST);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - CustomerIncident INSERTING: " + e.getMessage());
        }
    }
    public static void executeCustomerIncidentPutRequest(CustomerIncident customerIncident,
                                                         String url,
                                                         String token,
                                                         Context context,
                                                         VolleyCallBack_String volleyCallBack_string){
        try {
            executeCustomerIncidentRequest(customerIncident, url, token, context, volleyCallBack_string, Request.Method.PUT);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - CustomerIncident UPDATING: " + e.getMessage());
        }
    }
    public static void executeCustomerIncidentDeleteRequest(CustomerIncident customerIncident,
                                                            String url,
                                                            String token,
                                                            Context context,
                                                            VolleyCallBack_String volleyCallBack_string){
        try {
            executeCustomerIncidentRequest(customerIncident, url, token, context, volleyCallBack_string, Request.Method.DELETE);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - CustomerIncident DELETING: " + e.getMessage());
        }
    }
    public static void executeCustomerIncidentRequest(CustomerIncident customerIncident,
                                                      String url,
                                                      String token,
                                                      Context context,
                                                      VolleyCallBack_String volleyCallBack_string,
                                                      int requestMethod){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            Map<String, String> map1 = new HashMap<>();
            map1.put("ID", "");
            map1.put("IncidentID", customerIncident.getIncidentID().toString());
            map1.put("CustomerID", customerIncident.getCustomerID().toString());
            map1.put("UserID", customerIncident.getUserID().toString());
            map1.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customerIncident.getDate()));
            map1.put("FlagLevel", Integer.toString(customerIncident.getFlagLevel()));
            map1.put("AdditionalInformation", customerIncident.getAdditionalInfo());
            Log.d(TAG, "executeCustomerIncidentRequest: map1:" + map1);

            StringRequest stringRequest = new StringRequest(requestMethod, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            volleyCallBack_string.onSuccess("Success");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            volleyCallBack_string.onSuccess("VolleyError");
                        }
                     }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Log.d(TAG, "getHeaders: " + getheaders(token));
                            return getheaders(token);
                        }
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", "");
                            map.put("IncidentID", customerIncident.getIncidentID().toString());
                            map.put("CustomerID", customerIncident.getCustomerID().toString());
                            map.put("UserID", customerIncident.getUserID().toString());
                            map.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customerIncident.getDate()));
                            map.put("FlagLevel", Integer.toString(customerIncident.getFlagLevel()));
                            map.put("AdditionalInformation", customerIncident.getAdditionalInfo());
                            Log.d(TAG, "getParams: " + map);
                            return map;
                        }
                    };
            Log.d(TAG, "executeCustomerIncidentRequest: " + stringRequest);

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerIncidentRequest: " + e.getMessage());
        }
    }
    //endregion

    //region CustomerCheckinRecord REST Client Requests
    public static void executeCustomerCheckinRecordGetRequest(String url,
                                                              String token,
                                                              Context context,
                                                              VolleyCallBack_CustomerCheckinRecord volleyCallBack_customerCheckinRecord){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<CustomerCheckinRecord> customerCheckinRecords = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);
                                    Log.d(TAG, "onResponse: " + object.toString());
                                    CustomerCheckinRecord customerCheckinRecord = new CustomerCheckinRecord();

                                    customerCheckinRecord.setID(UUID.fromString(object.getString("ID")));
                                    customerCheckinRecord.setCustomerID(UUID.fromString(object.getString("CustomerID")));
                                    customerCheckinRecord.setCheckinDate(formatDate(object.getString("Date")));

                                    customerCheckinRecords.add(customerCheckinRecord);
                                    volleyCallBack_customerCheckinRecord.onSuccess(customerCheckinRecords);
                                }
                            }
                            catch (JSONException e) {
                                Log.d(TAG, "ERROR - execute Customer Checkin Record JSON ERROR: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                    }
            };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerCheckinRecord Get - " + e.getMessage());
        }
    }
    public static void executeCustomerCheckinRecordGetOneRequest(String url,
                                                                 String token,
                                                                 Context context,
                                                                 VolleyCallBack_CustomerCheckinRecord volleyCallBack_customerCheckinRecord){
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<CustomerCheckinRecord> customerCheckinRecords = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                CustomerCheckinRecord customerCheckinRecord = new CustomerCheckinRecord();

                                customerCheckinRecord.setID(UUID.fromString(object.getString("ID")));
                                customerCheckinRecord.setCustomerID(UUID.fromString(object.getString("CustomerID")));
                                customerCheckinRecord.setCheckinDate(formatDate(object.getString("Date")));

                                customerCheckinRecords.add(customerCheckinRecord);
                                volleyCallBack_customerCheckinRecord.onSuccess(customerCheckinRecords);
                            }
                            catch (JSONException e) {
                                Log.d(TAG, "ERROR - execute Customer Checkin Record JSON ERROR: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(stringRequest);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerCheckinRecord Get - " + e.getMessage());
        }
    }
    public static void executeCustomerCheckinRecordPostRequest(CustomerCheckinRecord customerCheckinRecord,
                                                               String url,
                                                               String token,
                                                               Context context,
                                                               VolleyCallBack_CustomerCheckinRecord volleyCallBack_customerCheckinRecord){
        try {
            executeCustomerCheckinRecordRequest(customerCheckinRecord, url, token, context, volleyCallBack_customerCheckinRecord, Request.Method.POST);
        }
        catch (Exception e) {
            Log.d(TAG, "ERROR - executeCustomerCheckinRecord INSERTING: " + e.getMessage());
        }
    }
   /* UNNEEDED METHODS
    public static void executeCustomerCheckinRecordPutRequest(){

    }
    public static void executeCustomerCheckinRecordDeleteRequest(){

    }*/
    public static void executeCustomerCheckinRecordRequest(CustomerCheckinRecord customerCheckinRecord,
                                                           String url,
                                                           String token,
                                                           Context context,
                                                           VolleyCallBack_CustomerCheckinRecord volleyCallBack_customerCheckinRecord,
                                                           int requestMethod){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JSONObject object = new JSONObject();

            object.put("ID", customerCheckinRecord.getID().toString());
            object.put("CustomerID", customerCheckinRecord.getCustomerID().toString());
            object.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(customerCheckinRecord.getCheckinDate()));

            final String requestBody = object.toString();

            JsonObjectRequest request = new JsonObjectRequest(requestMethod,
                    url,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "executeCustomerCheckinRecordResponse onResponse: " + response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "executeCustomerCheckinRecordResponse onResponse: " + error.getMessage());
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getheaders(token);
                        }
                    };

            queue.add(request);
        }
        catch (JSONException e) {
            Log.d(TAG, "executeCustomerCheckinRecordRequest: " + e.getMessage());
        }
    }
    //endregion
}
