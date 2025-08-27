package com.example.phonedialer.ui.dialer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DialerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DialerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dialer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}