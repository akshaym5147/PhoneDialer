package com.example.phonedialer.ui.favourite;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.example.phonedialer.R;
import com.example.phonedialer.helper.InitialsDrawable;
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

        Drawable fallbackDrawable = new InitialsDrawable(contact.getName());

        Glide.with(context)
                .load(contact.getPhotoUri())
                .placeholder(fallbackDrawable)   // shown while loading
                .error(fallbackDrawable)         // shown if no image
                .circleCrop()                    // make it circular
                .into(holder.image);

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

    private Drawable getInitialsDrawable(Context context, String name) {
        String initials = "";
        if (name != null && !name.isEmpty()) {
            String[] parts = name.trim().split("\\s+");
            if (parts.length >= 2) {
                initials = ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
            } else {
                initials = ("" + parts[0].charAt(0)).toUpperCase();
            }
        }

        // Create a simple bitmap with initials
        int size = 150; // px
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Background circle
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        // Draw text
        paint.setColor(Color.BLACK);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float x = size / 2f;
        float y = size / 2f - (fm.ascent + fm.descent) / 2;
        canvas.drawText(initials, x, y, paint);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
