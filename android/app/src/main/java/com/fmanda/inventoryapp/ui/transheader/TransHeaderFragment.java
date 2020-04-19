package com.fmanda.inventoryapp.ui.transheader;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmanda.inventoryapp.R;

public class TransHeaderFragment extends Fragment {

    private TransHeaderViewModel mViewModel;

    public static TransHeaderFragment newInstance() {
        return new TransHeaderFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TransHeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transheader, container, false);
        return root;
    }



}
