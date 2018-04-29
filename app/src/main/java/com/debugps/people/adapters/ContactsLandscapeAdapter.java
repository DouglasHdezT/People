package com.debugps.people.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.debugps.people.R;
import com.debugps.people.data.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsLandscapeAdapter extends RecyclerView.Adapter<ContactsLandscapeAdapter.ContactsViewHolder> {

    private ArrayList<Contact> contacts_list;

    public ContactsLandscapeAdapter(ArrayList<Contact> contacts_list) {
        this.contacts_list = contacts_list;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView name;
        TextView phone;
        ImageButton starFavorite;
        CircleImageView profilePhoto;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_landscape);
            name = itemView.findViewById(R.id.card_view_landscape_name);
            phone=  itemView.findViewById(R.id.card_view_landscape_phone_number);
            starFavorite =  itemView.findViewById(R.id.card_view_landscape_star);
            profilePhoto =  itemView.findViewById(R.id.card_view_landscape_profile_photo);

        }
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_landscape,parent, false);
        return (new ContactsViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, final int position) {
        final boolean isFavorited = contacts_list.get(position).isFavorite();

        if(isFavorited){
            holder.starFavorite.setImageResource(R.drawable.star_pressed);
        }else{
            holder.starFavorite.setImageResource(R.drawable.star_default);
        }

        if(contacts_list.get(position).getProfileImage()==null){
            holder.profilePhoto.setImageResource(R.drawable.ic_person);
        }else{
            holder.profilePhoto.setImageBitmap(contacts_list.get(position).getProfileImage());
        }

        holder.name.setText(contacts_list.get(position).getName());
        holder.phone.setText(contacts_list.get(position).getPhoneNumbers());

        holder.starFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorited){
                    contacts_list.get(position).setFavorite(false);
                }else{
                    contacts_list.get(position).setFavorite(true);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }


}
