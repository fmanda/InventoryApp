package com.fmanda.inventoryapp.ui.warehouse;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.WarehouseAdapter;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;

public class WarehouseFragment extends Fragment {

    private WarehouseViewModel mViewModel;
    private RecyclerView rvProject;
    WarehouseAdapter warehouseAdapter;

    public static WarehouseFragment newInstance() {
        return new WarehouseFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        warehouseAdapter = new WarehouseAdapter(getContext(), mViewModel.projects);

        View root = inflater.inflate(R.layout.fragment_warehouse, container, false);


        rvProject = root.findViewById(R.id.rvProject);
        rvProject.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvProject.setAdapter(warehouseAdapter);
        loadProjects();
        return root;
    }

    private void loadProjects(){
        ControllerWarehouse controllerWarehouse = new ControllerWarehouse(getContext());
        mViewModel.projects.clear();
        mViewModel.projects.addAll(controllerWarehouse.getWarehouses());
        warehouseAdapter.notifyDataSetChanged();

    }

}
