package com.example.phonedialer.ui.favourite;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonedialer.R;
import com.example.phonedialer.model.Contact;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<Contact> contacts;
    private final Context context;

    public FavoritesAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    public void setContacts(List<Contact> newContacts) {
        this.contacts = newContacts;
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_contact, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoriteViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.getName());

        if(contact.getPhotoUri() != null) {

            holder.image.setImageURI(Uri.parse(contact.getPhotoUri()));
        } else {
            holder.image.setImageResource(R.drawable.outline_account_circle_24);
        }

        holder.itemView.setOnClickListener(v -> {
            String phoneNumber = contact.getPhone(); // from your Contact model
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));

                // Check runtime permission
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE}, 200);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageContact);
            name = itemView.findViewById(R.id.textContactName);
        }
    }
}
