package com.example.instaliter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaliter.R;
import com.example.instaliter.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>  implements Filterable {

    private Context context;
//    private ArrayList<User> arrayList;
    private ArrayList<User> fullArrayList;
    private ArrayList<User> filteredArrayList;
    private UsersAdapterListener listener;

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profileImage;
        TextView tv_instaName;
        TextView tv_userName;

        public UsersViewHolder(View itemView) {
            super(itemView);
            iv_profileImage = itemView.findViewById(R.id.imageview);
            tv_instaName = itemView.findViewById(R.id.textview);
            tv_userName = itemView.findViewById(R.id.textview2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserSelected(filteredArrayList.get(getAdapterPosition()));
                }
            });
        }
    }

    public UsersAdapter(Context context, ArrayList<User> fullArrayList, UsersAdapterListener listener) {
        this.context = context;
        this.fullArrayList = fullArrayList;
        this.filteredArrayList = filteredArrayList;
        this.listener = listener;
    }

//    public UsersAdapter(ArrayList<User> arrayList){
//        this.arrayList = arrayList;
//        fullArrayList = new ArrayList<>(arrayList);
//    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user, parent, false);
        return new UsersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        final User currentUser = filteredArrayList.get(position);

        holder.iv_profileImage.setImageResource(Integer.parseInt(currentUser.getProfileImage()));
        holder.tv_instaName.setText(currentUser.getInstaName());
        holder.tv_userName.setText(currentUser.getUserName());

    }

    @Override
    public int getItemCount() {
        return fullArrayList.size();
    }
//
//    @Override
//    public Filter getFilter() {
//        return userFilter;
//    }


//
//    private Filter userFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<User> filteredList = new ArrayList<>();
//
//            if(constraint == null || constraint.length() == 0){
//                filteredList.addAll(fullArrayList);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (User oneUser: fullArrayList){
//                    if(oneUser.getUserName().toLowerCase().contains(filterPattern)){
//                        filteredList.add(oneUser);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return  results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            arrayList.clear();
//            arrayList = (ArrayList<User>) results.values;
//            notifyDataSetChanged();
//        }
//    };


    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    filteredArrayList = fullArrayList;
                } else {
                    ArrayList<User> filteredList = new ArrayList<>();
                    for (User row: fullArrayList){
                        if (row.getUserName().toLowerCase().contains(charString.toLowerCase()) || row.getInstaName().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }
                    filteredArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredArrayList;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                fullArrayList = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface UsersAdapterListener{
        void onUserSelected(User user);
    }
}
