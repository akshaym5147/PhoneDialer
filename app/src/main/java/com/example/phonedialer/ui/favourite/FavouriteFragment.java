package com.example.phonedialer.ui.favourite;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonedialer.R;
import com.example.phonedialer.databinding.FragmentFavouriteBinding;
import com.example.phonedialer.helper.GridSpacingItemDecoration;
import com.example.phonedialer.model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private static final int REQUEST_CONTACTS_PERMISSION = 100;

    private FragmentFavouriteBinding binding;
    private FavoritesAdapter adapter;
    private RecyclerView recyclerView;
    int numberOfColumns = 3;

    private List<Contact> contacts = Arrays.asList(
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
                    new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null),
            new Contact("Akshay", "7083325829", null)
    );


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavouriteViewModel favouriteViewModel =
                new ViewModelProvider(this).get(FavouriteViewModel.class);

        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerFavorites;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, true));

        adapter = new FavoritesAdapter(contacts, getContext());
        recyclerView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CONTACTS_PERMISSION);
        } else {
            loadStarredContacts();
        }

//        loadStarredContacts();


//        final TextView textView = binding.textHome;
//        favouriteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadStarredContacts();
            } else {
                Toast.makeText(getContext(), "Contacts permission is required to show favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadStarredContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver resolver = getContext().getContentResolver();

        // Query starred contacts from ContactsContract
        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts.STARRED + "=?",
                new String[]{"1"},
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String contactId = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                );

                String phoneNumber = null;
                int hasPhone = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                if (hasPhone > 0) {
                    Cursor phoneCursor = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null
                    );
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        phoneNumber = phoneCursor.getString(
                                phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        );
                        phoneCursor.close();
                    }
                }

                    String photoUri = cursor.getString(
                                cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI)
                    );


                contacts.add(new Contact(name, phoneNumber, photoUri));
            } while (cursor.moveToNext());

            cursor.close();
        }

        for (Contact contact : contacts) {
            System.out.println("Starred contact: " + contact.getName() + " - " + contact.getPhotoUri());
        }

        // Update adapter
        adapter.setContacts(contacts);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}