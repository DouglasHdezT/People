package com.debugps.people.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;
import com.debugps.people.intefaces.OnSettingContact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsRecentAdapter extends RecyclerView.Adapter<ContactsRecentAdapter.ContactsViewHolder> {

    private boolean isLandscape;
    private ArrayList<Contact> contacts_list;
    private Context context;
    private FragmentManager fragmentManager;
    private OnSettingContact onSettingContact;

    public ContactsRecentAdapter(ArrayList<Contact> contacts_list, FragmentManager fragmentManager, Context context, boolean isLandscape) {
        this.contacts_list = contacts_list;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.isLandscape = isLandscape;
        this.onSettingContact = (OnSettingContact) context;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePhoto;
        ImageButton callButton;

        TextView name;
        TextView phone;

        CardView cardView;


        public ContactsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_recent);
            profilePhoto = itemView.findViewById(R.id.card_view_recent_profile_photo);
            callButton = itemView.findViewById(R.id.card_view_recent_call);
            name = itemView.findViewById(R.id.card_view_recent_name);
            phone= itemView.findViewById(R.id.card_view_recent_phone_number);
        }
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_recent,parent,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, final int position) {
        if(contacts_list.get(position).getProfileImage() == null){
            holder.profilePhoto.setImageResource(R.drawable.ic_person);
        }else{
            holder.profilePhoto.setImageURI(contacts_list.get(position).getProfileImage());
        }

        if(contacts_list.get(position).getCantCalls() <= 1){
            holder.name.setText(contacts_list.get(position).getName());
        }else{
            String str =contacts_list.get(position).getName()+"("+contacts_list.get(position).getCantCalls()+")";
            holder.name.setText(str);
        }

        holder.phone.setText(contacts_list.get(position).getLastCalled());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLandscape){
                    MainActivity.showContactLandscape(contacts_list.get(position),fragmentManager);
                }else{
                    MainActivity.showContactPotrait(contacts_list.get(position),fragmentManager);
                }
            }
        });

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingContact.callContact(contacts_list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }
}
