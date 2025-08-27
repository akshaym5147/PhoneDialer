package com.example.phonedialer.model;

public class Contact {
    private String name;
    private String phone;
    private String photoUri;

    public Contact(String name, String phone, String photoUri) {
        this.name = name;
        this.phone = phone;
        this.photoUri = photoUri;
    }

    // Getter methods for name, phone, and photoUri
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
