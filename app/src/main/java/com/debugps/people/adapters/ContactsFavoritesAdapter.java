package com.debugps.people.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.debugps.people.R;
import com.debugps.people.data.Contact;

import java.util.ArrayList;

public abstract class ContactsFavoritesAdapter extends RecyclerView.Adapter<ContactsFavoritesAdapter.ContactsViewHolder>{

    private ArrayList<Contact> contacts_list;

    public ContactsFavoritesAdapter(ArrayList<Contact> contacts_list) {
        this.contacts_list = contacts_list;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        LinearLayout background;
        TextView name;
        TextView capLetter;
        ImageButton starFavorite;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_favorites);
            background =  itemView.findViewById(R.id.card_view_favorites_background);
            name = itemView.findViewById(R.id.card_view_favorites_name);
            capLetter = itemView.findViewById(R.id.card_view_favorites_cap_letter);
            starFavorite = itemView.findViewById(R.id.card_view_favorites_star);

        }
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_favorite,parent,false);
        return (new ContactsViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, final int position) {
        boolean isFavorited =  contacts_list.get(position).isFavorite();

        if(isFavorited){
            holder.starFavorite.setImageResource(R.drawable.star_pressed);
        }else{
            holder.starFavorite.setImageResource(R.drawable.star_default);
        }

        holder.background.setBackgroundResource(contacts_list.get(position).getColorId());
        holder.capLetter.setText(contacts_list.get(position).getName().charAt(0)+"");
        holder.name.setText(contacts_list.get(position).getName());

        holder.starFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remover(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }

    public abstract void remover(int index);
}
