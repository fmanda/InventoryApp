package com.fmanda.inventoryapp.ui.transheader;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.ui.item.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListTransHeader extends Fragment {

    private ListTransHeaderViewModel mViewModel;
    private FloatingActionButton fbAdd;

    public static ListTransHeader newInstance() {
        return new ListTransHeader();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ListTransHeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listtransheader, container, false);
        fbAdd = root.findViewById(R.id.fbAdd);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrans(null);
            }
        });

        return root;
    }

    private void addTrans(ModelTransHeader trans){
        ListTransHeaderDirections.ActionNavListtransToTransHeaderFragment action = ListTransHeaderDirections.actionNavListtransToTransHeaderFragment();
        action.setModeltransheader(trans);
        Navigation.findNavController(getView()).navigate(action);
    }
}
