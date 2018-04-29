package com.debugps.people.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.people.R;
import com.debugps.people.data.Contact;

import java.util.ArrayList;

public class ContactsDefaultAdapter extends RecyclerView.Adapter<ContactsDefaultAdapter.ContactsViewHolder> {

    /*
    Lista principal contacts; la otra es de apoyo y acceso para modificaciones.
     */

    private ArrayList<Contact> contacts;
    private ArrayList<Contact> contactsFav;


    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageButton starFavorite;
        ImageView profilePhoto;
        TextView name;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_default);
            name = itemView.findViewById(R.id.card_view_default_name);
            starFavorite = itemView.findViewById(R.id.card_view_default_star);
            profilePhoto = itemView.findViewById(R.id.card_view_default_image);

        }
    }

    public ContactsDefaultAdapter(ArrayList<Contact> contacts, ArrayList<Contact> contactsFav) {
        this.contacts = contacts;
        this.contactsFav = contactsFav;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_default, parent, false);
        return (new ContactsViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, final int position) {
        final boolean isFavorited = contacts.get(position).isFavorite();

        if(isFavorited){
            holder.starFavorite.setImageResource(R.drawable.star_pressed);
        }else{
            holder.starFavorite.setImageResource(R.drawable.star_default);
        }

        holder.profilePhoto.setImageBitmap(contacts.get(position).getProfileImage());
        holder.name.setText(contacts.get(position).getName());

        holder.starFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorited){
                    contacts.get(position).setFavorite(false);
                }else{
                    contacts.get(position).setFavorite(true);
                }

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

}
