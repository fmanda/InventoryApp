package com.fmanda.inventoryapp.ui.selling;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.SellingPeriodAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelSellingPeriod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

//import android.widget.Toast;

public class SellingPeriodFragment extends Fragment {

    private SellingPeriodViewModel mViewModel;
    SellingPeriodAdapter SellingPeriodAdapter;
    RecyclerView rvSellings;
//    SearchView searchItem;

    boolean spMonthinit = true;
    boolean spYearinit = true;
    Spinner spMonth;
    Spinner spYear;

    public static SellingPeriodFragment newInstance() {
        return new SellingPeriodFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SellingPeriodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sellingperiod, container, false);


        rvSellings = root.findViewById(R.id.rvSellings);
//        searchItem = root.findViewById(R.id.searchItem);
        rvSellings.setLayoutManager(new GridLayoutManager(getContext(), 1));
        SellingPeriodAdapter = new SellingPeriodAdapter(getContext(), mViewModel.allSellings,  mViewModel.SellingPeriods);
        rvSellings.setAdapter(SellingPeriodAdapter);
        spMonth = root.findViewById(R.id.spMonth);
        spYear = root.findViewById(R.id.spYear);
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

                loadSellings();
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
                loadSellings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadSellings();

//        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                filterItem(s);
//                return false;
//            }
//        });

        return root;
    }

    private void loadSellings(){
        try {
            int year = Integer.parseInt(spYear.getSelectedItem().toString());
            int month = spMonth.getSelectedItemPosition() + 1;

            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelSellingPeriod[] items = (ModelSellingPeriod[]) obj;
                    loadItemsView(items);
                }
                @Override
                public void onError(String msg) {
                    AppHelper.makeToast(getContext(), msg);
                }
            });
            cr.DownloadSellingPeriod(year,month);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void loadItemsView(ModelSellingPeriod[] items){
        mViewModel.allSellings.clear();
        mViewModel.allSellings.addAll( new ArrayList<ModelSellingPeriod>(Arrays.asList(items)) );
        filterItem("");
    }

    private void filterItem(String s){
        mViewModel.SellingPeriods.clear();
        Boolean isFound;
        for (ModelSellingPeriod modelSellingPeriod : mViewModel.allSellings){
            isFound = false;

            for (ModelSellingPeriod sg : mViewModel.SellingPeriods){
                if (
                    sg.getWarehousename().equals(modelSellingPeriod.getWarehousename())
                        && sg.getTransdate().equals(modelSellingPeriod.getTransdate())
                ){
                    isFound = true;
                    break;
                }
            }

            if (!isFound){
                if (s.equals("")) {
                    mViewModel.SellingPeriods.add(modelSellingPeriod);
                }else{
                    if (modelSellingPeriod.getItemname().toLowerCase().contains(s.toLowerCase())){
                        mViewModel.SellingPeriods.add(modelSellingPeriod);
                    }
                }
            }
        }

        if (rvSellings.getAdapter() != null) {
            SellingPeriodAdapter.notifyDataSetChanged();
        }
    }
}
