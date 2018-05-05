package com.debugps.people.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;
import com.debugps.people.dialogs.DialogContactShow;
import com.debugps.people.fragments.LandscapeViewFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class ContactsDefaultAdapter extends RecyclerView.Adapter<ContactsDefaultAdapter.ContactsViewHolder>{

    /*
    Lista principal contacts; la otra es de apoyo y acceso para modificaciones.
     */

    private ArrayList<Contact> contacts;
    private boolean isLandscape;
    private FragmentManager fragmentManager;
    private Context context;


    public  class ContactsViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageButton starFavorite;
        ImageView profilePhoto;
        CircleImageView circleProfilePhoto;
        TextView phoneNumber;
        TextView name;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_default);
            name = itemView.findViewById(R.id.card_view_default_name);
            starFavorite = itemView.findViewById(R.id.card_view_default_star);
            profilePhoto = itemView.findViewById(R.id.card_view_default_image);
            circleProfilePhoto = itemView.findViewById(R.id.card_view_default_profile_photo_circle);
            phoneNumber = itemView.findViewById(R.id.card_view_default_phone_number);

        }
    }

    protected ContactsDefaultAdapter(ArrayList<Contact> contacts, boolean isLandscape, FragmentManager fragmentManager, Context context) {
        this.contacts = contacts;
        this.isLandscape = isLandscape;
        this.fragmentManager = fragmentManager;
        this.context = context;
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

        if(isLandscape){
            if(contacts.get(position).getProfileImage()==null){
                holder.circleProfilePhoto.setImageResource(R.drawable.ic_person);
            }else{
                holder.circleProfilePhoto.setImageBitmap(MainActivity.getBitmapFromUri(contacts.get(position).getProfileImage(),context));
            }
            holder.phoneNumber.setText(contacts.get(position).getPhoneNumbers().size() == 0 ? "":contacts.get(position).getPhoneNumber(0));
        }else{
            if(contacts.get(position).getProfileImage()==null){
                holder.profilePhoto.setImageResource(R.drawable.ic_person);
            }else{
                holder.profilePhoto.setImageBitmap(MainActivity.getBitmapFromUri(contacts.get(position).getProfileImage(),context));
            }
        }

        holder.name.setText(contacts.get(position).getName());

        holder.starFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorited){
                    contacts.get(position).setFavorite(false);
                    remover(position);
                }else{
                    contacts.get(position).setFavorite(true);
                    notifyDataSetChanged();
                    agregar(position);
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLandscape){
                    MainActivity.showContactLandscape(contacts.get(position),fragmentManager);
                }else{
                    MainActivity.showContactPotrait(contacts.get(position),fragmentManager);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public abstract void agregar(int index);
    public abstract void remover(int index);

}
