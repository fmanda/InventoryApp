package com.fmanda.inventoryapp.ui.transheader;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.SummaryItemAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelItems;
import com.fmanda.inventoryapp.model.ModelTransDetail;
import com.fmanda.inventoryapp.model.ModelTransHeader;

import java.util.ArrayList;
import java.util.List;

public class TransReviewFragment extends Fragment {

    ModelTransHeader modelTransHeader;
    Button btnSave;
    RecyclerView rvItems;
    TextView txtTransType;
    TextView txtWarehouse;
    TextView txtDestWarehouse;
    SummaryItemAdapter itemAdapter;
    ModelItems modelItems;

    private TransReviewViewModel mViewModel;

    public static TransReviewFragment newInstance() {
        return new TransReviewFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TransReviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transreview, container, false);

        btnSave = root.findViewById(R.id.btnSave);
        rvItems = root.findViewById(R.id.rvItems);
        txtTransType = root.findViewById(R.id.txtTransType);
        txtWarehouse = root.findViewById(R.id.txtWarehouse);
        txtDestWarehouse = root.findViewById(R.id.txtDestWarehouse);

        itemAdapter = new SummaryItemAdapter(getContext(), mViewModel.items);
        rvItems = root.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvItems.setAdapter(itemAdapter);

        if (getArguments() != null){
            TransReviewFragmentArgs args = TransReviewFragmentArgs.fromBundle(getArguments());
            if (args.getModeltransheader() != null) {
                modelTransHeader = args.getModeltransheader();
            }else{
                Toast.makeText(getContext(), "ModelTransHeader is null", Toast.LENGTH_SHORT).show();
                btnSave.setVisibility(View.GONE);
            }

            if (args.getModelitems() != null) {
                modelItems = args.getModelitems();
            }else{
                Toast.makeText(getContext(), "ModelItems is null", Toast.LENGTH_SHORT).show();
                btnSave.setVisibility(View.GONE);
            }
        }


        if (modelTransHeader != null){
            loadData(modelTransHeader);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return root;
    }

    private void loadData(ModelTransHeader modelTransHeader){
        ControllerWarehouse controllerWarehouse = new ControllerWarehouse(getContext());

        txtWarehouse.setText(controllerWarehouse.getWarehouse(modelTransHeader.getWarehouse_id()).getWarehousename());
        txtDestWarehouse.setText(controllerWarehouse.getWarehouse(modelTransHeader.getDest_warehouse_id()).getWarehousename());

        if (modelTransHeader.getHeader_flag() == 3){
            txtWarehouse.setText("Gudang Asal : " + txtWarehouse.getText());
            txtDestWarehouse.setText("Gudang Tujuan : " + txtWarehouse.getText());
            txtDestWarehouse.setVisibility(View.VISIBLE);
        }else{
            txtWarehouse.setText("Gudang : " + txtWarehouse.getText());
            txtDestWarehouse.setVisibility(View.INVISIBLE);
        }

        txtTransType.setText("Jenis Transaksi : " +  modelTransHeader.getHeaderFlagName());

        mViewModel.items.clear();
        for (ModelItem modelItem : modelItems.items){
            if (modelItem.qty == 0) continue;
            mViewModel.items.add(modelItem);
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void saveData(){
        reupdateTransDetail();

        ControllerRest cr = new ControllerRest(getContext());
        cr.setListener(new ControllerRest.Listener() {
            @Override
            public void onSuccess(String msg) {
                Toast.makeText(getContext(), "yuhu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onProgress(String msg) {

            }
        });
        cr.UpdateTransHeader(modelTransHeader);
    }

    private void reupdateTransDetail(){
        //set all warehouse
        int ifactor = 1;
        if (modelTransHeader.getHeader_flag() == 2){
            ifactor = -1;
        }

        List<ModelTransDetail> trfDetails = new ArrayList<>();

        for (ModelTransDetail modelTransDetail : modelTransHeader.getItems()){
            modelTransDetail.setWarehouse_id(modelTransHeader.getWarehouse_id());
            modelTransDetail.setQty(ifactor * Math.abs(modelTransDetail.getQty()));
            modelTransDetail.setHeader_flag(modelTransHeader.getHeader_flag());

            if (modelTransHeader.getHeader_flag() == 3){
                ModelTransDetail newTd = new ModelTransDetail();
                newTd.copyObject(modelTransDetail);
                newTd.setWarehouse_id(modelTransHeader.getDest_warehouse_id());
                newTd.setQty(-1 * ( modelTransDetail.getQty()));
                trfDetails.add(newTd);
            }
        }

        modelTransHeader.getItems().addAll(trfDetails);

    }
}
