package com.example.idscanningapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.Models.CustomerIncident;

import java.util.ArrayList;

public class IncidentPopupAdapter extends RecyclerView.Adapter {

    public static final String TAG = "incidentPopupAdapter";
    private ArrayList<CustomerIncident> incidents;
    private Context parent;

    public IncidentPopupAdapter(ArrayList<CustomerIncident> arrayCustomerIncidents, Context context) {
        Log.d(TAG, "Entered Adapter");

        incidents = arrayCustomerIncidents;
        Log.d(TAG, "IncidentPopupAdapter: " + incidents.size());
        parent = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_incident_popup, parent, false);
        return new IncidentPopupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IncidentPopupViewHolder incidentPopupViewHolder = (IncidentPopupViewHolder) holder;
        CustomerIncident incident = incidents.get(position);
        // Bind to screen
        incidentPopupViewHolder.getTvDate().setText("Date: " + incident.getDate().toString());
        incidentPopupViewHolder.getTvUserInitials().setText("User Initials: " + incident.getUserInitials());
        incidentPopupViewHolder.getTvIncidentName().setText("Incident Type: " + incident.getIncidentName());

        Log.d(TAG, "onBindViewHolder: " + incidentPopupViewHolder.getTvFlagLevel());
        // Flag Level
        int flaglevel = incident.getFlagLevel();
        if (flaglevel == 1)
            incidentPopupViewHolder.getTvFlagLevel().setText("Flag Level: Not Severe");
        else if (flaglevel == 2)
            incidentPopupViewHolder.getTvFlagLevel().setText("Flag Level: Semi Severe");
        else if (flaglevel == 3)
            incidentPopupViewHolder.getTvFlagLevel().setText("Flag Level: Severe");
        else
            incidentPopupViewHolder.getTvFlagLevel().setText("Flag Level: Highly Severe URGENT");

        // Additional Info
        if (incident.getAdditionalInfo().isEmpty())
            incidentPopupViewHolder.getTvAdditionalInfo().setText("Additional Info: NONE");
        else
            incidentPopupViewHolder.getTvAdditionalInfo().setText("Additional Info:\n\t" + incident.getAdditionalInfo());
    }

    @Override
    public int getItemCount() {
        return incidents.size();
    }

    public class IncidentPopupViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvUserInitials;
        private TextView tvIncidentName;
        private TextView tvFlagLevel;
        private TextView tvAdditionalInfo;

        public TextView getTvDate() { return tvDate; }
        public TextView getTvUserInitials() { return tvUserInitials; }
        public TextView getTvIncidentName() { return tvIncidentName; }
        public TextView getTvFlagLevel() { return tvFlagLevel; }
        public TextView getTvAdditionalInfo() { return tvAdditionalInfo; }

        public IncidentPopupViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.IncidentPopup_tvDate);
            tvUserInitials = itemView.findViewById(R.id.IncidentPopup_tvUserInitials);
            tvIncidentName = itemView.findViewById(R.id.IncidentPopup_tvIncidentName);
            tvFlagLevel = itemView.findViewById(R.id.IncidentPopup_tvFlagLevel);
            tvAdditionalInfo = itemView.findViewById(R.id.IncidentPopup_tvAdditionalInfo);

            itemView.setTag(this);
        }
    }
}
