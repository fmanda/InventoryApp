package com.fmanda.inventoryapp.ui.stock;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
//import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.StockReportAdapter;
import com.fmanda.inventoryapp.adapter.WarehouseAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelStockReport;
import com.fmanda.inventoryapp.ui.warehouse.WarehouseViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class StockReportFragment extends Fragment {

    private StockReportViewModel mViewModel;
    StockReportAdapter stockReportAdapter;
    RecyclerView rvStocks;
    SearchView searchItem;

    public static StockReportFragment newInstance() {
        return new StockReportFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StockReportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stockreport, container, false);


        rvStocks = root.findViewById(R.id.rvStocks);
        searchItem = root.findViewById(R.id.searchItem);
        rvStocks.setLayoutManager(new GridLayoutManager(getContext(), 1));
        stockReportAdapter = new StockReportAdapter(getContext(), mViewModel.allStocks,  mViewModel.stockReports);
        rvStocks.setAdapter(stockReportAdapter);


        loadStocks();

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

        return root;
    }

    private void loadStocks(){
        try {
            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelStockReport[] items = (ModelStockReport[]) obj;
                    loadItemsView(items);
                }
                @Override
                public void onError(String msg) {
                    AppHelper.makeToast(getContext(), msg);
                }
            });
            cr.DownloadStockReports();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void loadItemsView(ModelStockReport[] items){
        mViewModel.allStocks.clear();
        mViewModel.allStocks.addAll( new ArrayList<ModelStockReport>(Arrays.asList(items)) );
        filterItem("");
    }

    private void filterItem(String s){
        mViewModel.stockReports.clear();
        Boolean isFound;
        for (ModelStockReport modelStockReport : mViewModel.allStocks){
            isFound = false;

            for (ModelStockReport sg : mViewModel.stockReports){
                if (sg.getItemname().equals(modelStockReport.getItemname())){
                    isFound = true;
                    break;
                }
            }

            if (!isFound){
                if (s.equals("")) {
                    mViewModel.stockReports.add(modelStockReport);
                }else{
                    if (modelStockReport.getItemname().toLowerCase().contains(s.toLowerCase())){
                        mViewModel.stockReports.add(modelStockReport);
                    }
                }
            }
        }

        if (rvStocks.getAdapter() != null) {
            stockReportAdapter.notifyDataSetChanged();
        }
    }
}
