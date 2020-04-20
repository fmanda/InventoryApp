package com.fmanda.inventoryapp.ui.transheader;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.media.JetPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.ItemAdapter;
import com.fmanda.inventoryapp.adapter.ListTransAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.model.ModelWarehouse;
import com.fmanda.inventoryapp.ui.item.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ListTransHeader extends Fragment {

    boolean spWarehouseInit = true;
    boolean spMonthinit = true;
    boolean spYearinit = true;
    Spinner spWarehouse;
    Spinner spMonth;
    Spinner spYear;
    ArrayAdapter<String> spWarehouseAdapter;
    List<ModelWarehouse> warehouses = new ArrayList<>();
    List<ModelTransHeader> listtrans = new ArrayList<>();
    ListTransAdapter listTransAdapter;
    RecyclerView rvTrans;

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


        spMonth = root.findViewById(R.id.spMonth);
        spYear = root.findViewById(R.id.spYear);
        spWarehouse = root.findViewById(R.id.spWarehouse);
        spWarehouseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        spWarehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWarehouse.setAdapter(spWarehouseAdapter);
        reInitWarehouse();

        Calendar c = Calendar.getInstance();
        spMonth.setSelection(c.get(Calendar.MONTH));

        for (int i = 0; i < spYear.getAdapter().getCount(); i++) {
            if (Integer.parseInt(spYear.getAdapter().getItem(i).toString()) == c.get(Calendar.YEAR)) {
                spYear.setSelection(i);
                break;
            }
        }

        //set event after initiate, update :useless.. so we use initialSpinner = false;
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spMonthinit) {
                    spMonthinit = false;
                    return;
                }

                loadFromRest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spWarehouseInit) {
                    spWarehouseInit = false;
                    return;
                }
                loadFromRest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spYearinit) {
                    spYearinit = false;
                    return;
                }
                loadFromRest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listTransAdapter = new ListTransAdapter(getContext(), mViewModel.trans);
        rvTrans = root.findViewById(R.id.rvTrans);
        rvTrans.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvTrans.setAdapter(listTransAdapter);

        listTransAdapter.setItemClickListener(new ListTransAdapter.ItemClickListener() {
            @Override
            public void onClick(ModelTransHeader modelTransHeader) {
                loadData(modelTransHeader);
            }
        });

        loadFromRest();
        return root;
    }

    private void loadData(ModelTransHeader modelTransHeader) {
        int id = modelTransHeader.getId();

        try {
            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelTransHeader loadedTrans = (ModelTransHeader) obj[0];
                    addTrans(loadedTrans);
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            cr.DownloadTransHeader(id);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void loadTransView(ModelTransHeader[] list){
        mViewModel.trans.clear();
        mViewModel.trans.addAll( new ArrayList<ModelTransHeader>(Arrays.asList(list)) );
        listTransAdapter.notifyDataSetChanged();
    }

    private void loadFromRest() {
        try {
            int warehouse_id = 0;
            if (spWarehouse.getSelectedItem() != null) {
                for (ModelWarehouse warehouse : warehouses) {
                    if (warehouse.getWarehousename().equals(spWarehouse.getSelectedItem().toString())) {
                        warehouse_id = warehouse.getId();
                    }
                }
            }

            int year = Integer.parseInt(spYear.getSelectedItem().toString());
            int month = spMonth.getSelectedItemPosition() + 1;

            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelTransHeader[] items = (ModelTransHeader[]) obj;
                    loadTransView(items);
                }
                @Override
                public void onError(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            cr.DownloadListTrans(warehouse_id, year, month);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void reInitWarehouse() {
        spWarehouseAdapter.clear();
        ControllerWarehouse cp = new ControllerWarehouse(getContext());
        warehouses.clear();

        ModelWarehouse modelWarehouse = new ModelWarehouse();
        modelWarehouse.setId(0);
        modelWarehouse.setWarehousename("Semua Gudang/Toko");
        warehouses.add(modelWarehouse);
        warehouses.addAll(cp.getWarehouses());

        for(ModelWarehouse mw : warehouses){
            spWarehouseAdapter.add(mw.getWarehousename());
        }
    }

    private void addTrans(ModelTransHeader trans){
        ListTransHeaderDirections.ActionNavListtransToTransHeaderFragment action = ListTransHeaderDirections.actionNavListtransToTransHeaderFragment();
        action.setModeltransheader(trans);
        Navigation.findNavController(getView()).navigate(action);
    }
}
