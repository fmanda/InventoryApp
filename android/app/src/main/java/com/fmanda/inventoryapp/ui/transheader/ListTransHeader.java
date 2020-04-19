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
import com.fmanda.inventoryapp.ui.item.ItemViewModel;

public class ListTransHeader extends Fragment {

    private ListTransHeaderViewModel mViewModel;

    public static ListTransHeader newInstance() {
        return new ListTransHeader();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ListTransHeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listtransheader, container, false);
        return root;
    }


}
