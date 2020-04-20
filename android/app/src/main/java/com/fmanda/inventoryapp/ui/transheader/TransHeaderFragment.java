package com.fmanda.inventoryapp.ui.transheader;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.controller.ControllerSetting;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;
import com.fmanda.inventoryapp.model.ModelTransDetail;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.model.ModelWarehouse;
import com.fmanda.inventoryapp.ui.item.UpdateItemFragmentArgs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class TransHeaderFragment extends Fragment {

    private TransHeaderViewModel mViewModel;
    private RadioGroup rbTrans;
    private RadioButton rbPurchase;
    private RadioButton rbSelling;
    private RadioButton rbTransfer;
    private TextView lbWarehouse;
    private TextView txtTransNo;
    private TextView lbDestWarehouse;
    private Spinner spWarehouse;
    private Spinner spDestWarehouse;
    private Button btnNext;
    private Button btnDelete;
    public ModelTransHeader modelTransHeader = new ModelTransHeader();

//    boolean spWarehouseInit = true;
//    boolean spDestWarehouseInit = true;

    ArrayAdapter<String> spWarehouseAdapter;
    ArrayAdapter<String> spDestWarehouseAdapter;

    List<ModelWarehouse> warehouses = new ArrayList<>();
    ControllerSetting cs;

    public static TransHeaderFragment newInstance() {
        return new TransHeaderFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TransHeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transheader, container, false);

        cs = new ControllerSetting(getContext());
        rbTrans = root.findViewById(R.id.rbTrans);
        rbPurchase = root.findViewById(R.id.rbPurchase);
        rbSelling = root.findViewById(R.id.rbSelling);
        rbTransfer = root.findViewById(R.id.rbTransfer);
        lbWarehouse = root.findViewById(R.id.lbWarehouse);
        lbDestWarehouse = root.findViewById(R.id.lbDestWarehouse);
        txtTransNo = root.findViewById(R.id.txtTransNo);
        spWarehouse = root.findViewById(R.id.spWarehouse);
        spDestWarehouse = root.findViewById(R.id.spDestWarehouse);
        btnNext = root.findViewById(R.id.btnNext);
        btnDelete = root.findViewById(R.id.btnDelete);

        spWarehouseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        spWarehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWarehouse.setAdapter(spWarehouseAdapter);

        spDestWarehouseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        spDestWarehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestWarehouse.setAdapter(spDestWarehouseAdapter);
        reinitWarehouse();

        rbTrans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbTransfer){
                    lbDestWarehouse.setVisibility(View.VISIBLE);
                    spDestWarehouse.setVisibility(View.VISIBLE);
                }else{
                    lbDestWarehouse.setVisibility(View.INVISIBLE);
                    spDestWarehouse.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNext();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrans();
            }
        });

        rbTrans.check(R.id.rbSelling);

        modelTransHeader.generateTransNo();
        txtTransNo.setText(modelTransHeader.getTransno());

        btnDelete.setVisibility(View.INVISIBLE);
        if (getArguments() != null){
            TransHeaderFragmentArgs args = TransHeaderFragmentArgs.fromBundle(getArguments());
            if (args.getModeltransheader() != null) {
                modelTransHeader = args.getModeltransheader();
                loadData(modelTransHeader);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }

        return root;
    }

    private void deleteTrans(){
        if (modelTransHeader == null){
            Snackbar.make(getView(), "Tidak ada data yang akan Dihapus", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Anda yaking menghapus transaksi ini?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                ControllerRest cr = new ControllerRest(getContext());
                cr.setListener(new ControllerRest.Listener() {
                    @Override
                    public void onSuccess(String msg) {
                        Toast.makeText(getContext(), "Data transaksi berhasil dihapus", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.nav_listtrans);
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String msg) {

                    }
                });
                cr.DeleteTransHeader(modelTransHeader.getId());

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadData(ModelTransHeader modelTransHeader) {
        txtTransNo.setText(modelTransHeader.getTransno());
        //hilangkan lawan transaksi transfer
        if (modelTransHeader.getHeader_flag() == 3) {
            Iterator<ModelTransDetail> it = modelTransHeader.getItems().iterator();
            while (it.hasNext()) {
                ModelTransDetail td = it.next();
                if (td.getQty() < 0) {
                    it.remove();
                }
            }
        }
        //update abs
        for (ModelTransDetail modelTransDetail : modelTransHeader.getItems()){
            modelTransDetail.setQty( Math.abs(modelTransDetail.getQty()));
        }

        if (modelTransHeader.getHeader_flag() == 1){
            rbTrans.check(R.id.rbPurchase);
        }else if (modelTransHeader.getHeader_flag() == 2){
            rbTrans.check(R.id.rbSelling);
        }else if (modelTransHeader.getHeader_flag() == 3){
            rbTrans.check(R.id.rbTransfer);
        }

        int position = 0;
        for (ModelWarehouse warehouse : warehouses){
            if (warehouse.getId() == modelTransHeader.getWarehouse_id()){
                spWarehouse.setSelection(position);
            }
            if (warehouse.getId() == modelTransHeader.getDest_warehouse_id() && spDestWarehouse.getVisibility() == View.VISIBLE) {
                spDestWarehouse.setSelection(position);
            }
            position++;
        }



    }

    private void processNext() {
        Calendar c = Calendar.getInstance();
        modelTransHeader.setTransdate(c.getTime());
        int header_flag = 0;
        int warehouse_id = 0;
        int destwarehouse_id = 0;

        if (rbTrans.getCheckedRadioButtonId() == R.id.rbPurchase){
            header_flag = 1;
        }else if (rbTrans.getCheckedRadioButtonId() == R.id.rbSelling) {
            header_flag = 2;
        }else if (rbTrans.getCheckedRadioButtonId() == R.id.rbTransfer) {
            header_flag = 3;
        }

        for (ModelWarehouse modelWarehouse : warehouses){
            if (spWarehouse.getSelectedItem().toString().toLowerCase().equals(modelWarehouse.getWarehousename().toLowerCase())){
                warehouse_id = modelWarehouse.getId();
            }
            if (spDestWarehouse.getVisibility() == View.VISIBLE){
                if (spDestWarehouse.getSelectedItem().toString().toLowerCase().equals(modelWarehouse.getWarehousename().toLowerCase())){
                    destwarehouse_id = modelWarehouse.getId();
                }
            }
        }

        modelTransHeader.setHeader_flag(header_flag);
        modelTransHeader.setWarehouse_id(warehouse_id);
        modelTransHeader.setDest_warehouse_id(destwarehouse_id);

        TransHeaderFragmentDirections.ActionNavTransheaderToNavPickitem action = TransHeaderFragmentDirections.actionNavTransheaderToNavPickitem();
        action.setModeltransheader(modelTransHeader);
        Navigation.findNavController(getView()).navigate(action);
    }


    private void reinitWarehouse() {
        spWarehouseAdapter.clear();
        spDestWarehouseAdapter.clear();

        ControllerWarehouse cw = new ControllerWarehouse(getContext());
        warehouses.clear();
        warehouses.addAll(cw.getWarehouses());

        String last_trans_warehouse = cs.getSettingStr("last_trans_warehouse");
        int position = 0;
        for(ModelWarehouse warehouse : warehouses){
            spWarehouseAdapter.add(warehouse.getWarehousename());
            spDestWarehouseAdapter.add(warehouse.getWarehousename());

            if (String.valueOf(warehouse.getId()).equals(last_trans_warehouse)){
                spWarehouse.setSelection(position);
            }
            position++;
        }

    }


}
