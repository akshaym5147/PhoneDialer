package com.example.phonedialer.ui.dialer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.phonedialer.databinding.FragmentDialerBinding;

public class DialerFragment extends Fragment {

    private FragmentDialerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DialerViewModel dialerViewModel =
                new ViewModelProvider(this).get(DialerViewModel.class);

        binding = FragmentDialerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.numberDisplay;
        dialerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}