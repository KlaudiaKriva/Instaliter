package com.example.instaliter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaliter.R;
import com.example.instaliter.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>  implements Filterable {

    private ArrayList<User> arrayList;
    private ArrayList<User> fullArrayList;

    public UsersAdapter(ArrayList<User> arrayList){
        this.arrayList = arrayList;
        fullArrayList = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user, parent, false);
        return new UsersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User currentUser = arrayList.get(position);

        holder.iv_profileImage.setImageResource(Integer.parseInt(currentUser.getProfileImage()));
        holder.tv_instaName.setText(currentUser.getInstaName());
        holder.tv_userName.setText(currentUser.getUserName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profileImage;
        TextView tv_instaName;
        TextView tv_userName;

        public UsersViewHolder(View itemView) {
            super(itemView);
            iv_profileImage = itemView.findViewById(R.id.imageview);
            tv_instaName = itemView.findViewById(R.id.textview);
            tv_userName = itemView.findViewById(R.id.textview2);
        }
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(fullArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User oneUser: fullArrayList){
                    if(oneUser.getUserName().toLowerCase().contains(filterPattern)){
                        filteredList.add(oneUser);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    };




}
