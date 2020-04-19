package com.fmanda.inventoryapp.ui.transheader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.ItemAdapter;
import com.fmanda.inventoryapp.adapter.PickItemAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelItems;
import com.fmanda.inventoryapp.model.ModelTransDetail;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.ui.item.ItemFragmentDirections;
import com.fmanda.inventoryapp.ui.item.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class PickItemFragment extends Fragment {

    private ItemViewModel mViewModel;
    private RecyclerView rvItems;
    private SearchView searchItem;
    private Button btnNext;
    private PickItemAdapter itemAdapter;
    public ModelTransHeader modelTransHeader;

    public static PickItemFragment newInstance() {
        return new PickItemFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pickitem, container, false);

        searchItem = root.findViewById(R.id.searchItem);
        btnNext = root.findViewById(R.id.btnNext);

        itemAdapter = new PickItemAdapter(getContext(), mViewModel.items);
        rvItems = root.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvItems.setAdapter(itemAdapter);

        itemAdapter.setItemClickListener(new PickItemAdapter.ItemClickListener() {
            @Override
            public void onClick(ModelItem modelItem) {
                updateQty(modelItem);
            }
        });

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterItem(s);
                return false;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNext(modelTransHeader);
            }
        });

        if (getArguments() != null){
            PickItemFragmentArgs args = PickItemFragmentArgs.fromBundle(getArguments());
            if (args.getModeltransheader() != null) {
                modelTransHeader = args.getModeltransheader();
            }else{
                Toast.makeText(getContext(), "ModelTransHeader is null", Toast.LENGTH_SHORT).show();
                btnNext.setVisibility(View.GONE);
            }
        }

        loadItems();
        return root;
    }

    private void processNext(ModelTransHeader modelTransHeader) {
        PickItemFragmentDirections.ActionNavPickitemToNavTransreview action = PickItemFragmentDirections.actionNavPickitemToNavTransreview();
        action.setModeltransheader(modelTransHeader);

        ModelItems modelItems = new ModelItems();
        modelItems.items.addAll(mViewModel.items);
        action.setModelitems(modelItems);
        Navigation.findNavController(getView()).navigate(action);
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
        mViewModel.allitems.clear();
        mViewModel.allitems.addAll( new ArrayList<ModelItem>(Arrays.asList(items)) );
        filterItem(""); //basically call items.addAll(allitems);

        for (ModelTransDetail modelTransDetail : modelTransHeader.getItems()) {
            for (ModelItem modelItem : mViewModel.items) {
                if (modelTransDetail.getItem_id() != modelItem.getId()) continue;
                modelItem.qty = (int) modelTransDetail.getQty();
            }
        }
    }

    private void filterItem(String s){
        mViewModel.items.clear();
        if (s.equals("")){
            mViewModel.items.addAll(mViewModel.allitems);
        }else{
            for (ModelItem item : mViewModel.allitems){
                if (item.getItemname().toLowerCase().contains(s.toLowerCase())){
                    mViewModel.items.add(item);
                }
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void updateQty(ModelItem modelItem){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final DialogQtyFragment dialogQtyFragment =  new DialogQtyFragment();
        dialogQtyFragment.setItem(modelItem);
        dialogQtyFragment.setQtyUpdateListener(new DialogQtyFragment.QtyUpdateListener() {
            @Override
            public void onFinishUpdate(ModelItem modelItem, int qty) {
                modelItem.qty = qty;
                itemAdapter.notifyDataSetChanged();
                updateTransDetail();
                dialogQtyFragment.dismiss();
            }
        });
        dialogQtyFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        dialogQtyFragment.show(fm, "Pilih Modifier");
    }

    private void updateTransDetail(){
        modelTransHeader.getItems().clear();
        for (ModelItem modelItem : mViewModel.items){
            if (modelItem.qty <= 0) continue;
            ModelTransDetail modelTransDetail = new ModelTransDetail();
            modelTransDetail.setItem_id(modelItem.getId());
            modelTransDetail.setWarehouse_id(modelTransHeader.getWarehouse_id());
            modelTransDetail.setHeader_flag(modelTransHeader.getHeader_flag());
            modelTransDetail.setPurchaseprice(modelItem.getPurchaseprice());
            modelTransDetail.setSellingprice(modelItem.getSellingprice());
            modelTransDetail.setQty(modelItem.qty);

            modelTransHeader.getItems().add(modelTransDetail);
        }
    }

}