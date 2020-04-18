package com.fmanda.inventoryapp.ui.item;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.ItemAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelItem;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemFragment extends Fragment {

    private ItemViewModel mViewModel;
    private RecyclerView rvItems;
    ItemAdapter itemAdapter;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_item, container, false);

        itemAdapter = new ItemAdapter(getContext(), mViewModel.projects);
        rvItems = root.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvItems.setAdapter(itemAdapter);
        loadItems();

        return root;
    }

    private void loadItems() {
        try {
            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelItem[] items = (ModelItem[]) obj;
                    loadItemsView(items);
                }
                @Override
                public void onError(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            cr.DownloadItems();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadItemsView(ModelItem[] items){
        mViewModel.projects.clear();
        mViewModel.projects.addAll( new ArrayList<ModelItem>(Arrays.asList(items)) );
        itemAdapter.notifyDataSetChanged();

    }
}
