package com.example.idscanningapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idscanningapp.Models.User;

import java.util.ArrayList;

public class UserManagementAdapter extends RecyclerView.Adapter implements Filterable {
    public static final String TAG = "userManagementAdapter";
    private ArrayList<User> userData;
    private View.OnClickListener itemOnClickListener;
    private View.OnClickListener btnViewOnClickListener;
    private View.OnClickListener btnUpdateOnClickListener;
    private View.OnClickListener btnDeleteOnClickListener;
    private Context parentContext;
    private Filter fRecords;

    public void setUserData(ArrayList<User> users) { userData = users; }
    public void setItemOnClickListener(View.OnClickListener itemClickListener) { itemOnClickListener = itemClickListener; }
    public void setBtnViewOnClickListener(View.OnClickListener btnclickListener) { btnViewOnClickListener = btnclickListener; }
    public void setBtnUpdateOnClickListener(View.OnClickListener btnclickListener) { btnUpdateOnClickListener = btnclickListener; }
    public void setBtnDeleteOnClickListener(View.OnClickListener btnclickListener) { btnDeleteOnClickListener = btnclickListener; }
    public UserManagementAdapter(ArrayList<User> userArrayList, Context context) {
        userData = userArrayList;
        parentContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user_management_userinfo, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        User user = userData.get(position);

        userViewHolder.getTvUsername().setText("Username: " + user.getUsername());
    }

    @Override
    public int getItemCount() {
        if (userData == null) {
            return 0;
        }
        return userData.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private Button btnView;
        private Button btnUpdate;
        private Button btnDelete;
        public TextView getTvUsername() { return tvUsername; }

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.UserListItem_tvUsername);
            btnView = itemView.findViewById(R.id.UserListItem_btnView);
            btnUpdate = itemView.findViewById(R.id.UserListItem_btnUpdate);
            btnDelete = itemView.findViewById(R.id.UserListItem_btnDelete);

            btnView.setOnClickListener(btnViewOnClickListener);
            btnUpdate.setOnClickListener(btnUpdateOnClickListener);
            btnDelete.setOnClickListener(btnDeleteOnClickListener);

            itemView.setTag(this);
            itemView.setOnClickListener(itemOnClickListener);
        }
    }

    // Filter
    @Override
    public Filter getFilter() {
        if (fRecords == null)
            fRecords = new RecordFilter();
        return fRecords;
    }
    private class RecordFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = userData;
                results.count = userData.size();
            }
            else {
                ArrayList<User> fRecords = new ArrayList<>();

                for (User u : userData) {
                    try {
                        if (u.getUsername().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()))
                            fRecords.add(u);
                        else
                            Log.d(TAG, "performFiltering: NO RECORD");
                    }
                    catch (Exception e) {
                        Log.d(TAG, "performFiltering: " + e.getMessage());
                    }

                    results.values = fRecords;
                    results.count = fRecords.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            userData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }


}
