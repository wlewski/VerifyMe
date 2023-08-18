package com.example.idscanningapp.API;

import com.example.idscanningapp.Models.Customer;

import java.util.ArrayList;

public interface VolleyCallBack {
    void onSuccess(ArrayList<Customer> results);
}

