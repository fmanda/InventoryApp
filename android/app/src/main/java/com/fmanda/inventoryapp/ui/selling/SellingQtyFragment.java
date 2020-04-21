package com.fmanda.inventoryapp.ui.selling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.adapter.SellingQtyAdapter;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelSellingQty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SellingQtyFragment extends Fragment {

    private SellingQtyViewModel mViewModel;
    SellingQtyAdapter SellingQtyAdapter;
    RecyclerView rvSellings;
    SearchView searchItem;

    boolean spMonthinit = true;
    boolean spYearinit = true;
    Spinner spMonth;
    Spinner spYear;

    public static SellingQtyFragment newInstance() {
        return new SellingQtyFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SellingQtyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sellingqty, container, false);


        rvSellings = root.findViewById(R.id.rvSellings);
        searchItem = root.findViewById(R.id.searchItem);
        rvSellings.setLayoutManager(new GridLayoutManager(getContext(), 1));
        SellingQtyAdapter = new SellingQtyAdapter(getContext(), mViewModel.allSellings,  mViewModel.sellingQtys);
        rvSellings.setAdapter(SellingQtyAdapter);
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

    private void loadSellings(){
        try {
            int year = Integer.parseInt(spYear.getSelectedItem().toString());
            int month = spMonth.getSelectedItemPosition() + 1;

            ControllerRest cr = new ControllerRest(this.getContext());
            cr.setObjectListener(new ControllerRest.ObjectListener() {
                @Override
                public void onSuccess(BaseModel[] obj) {
                    ModelSellingQty[] items = (ModelSellingQty[]) obj;
                    loadItemsView(items);
                }
                @Override
                public void onError(String msg) {
                    AppHelper.makeToast(getContext(), msg);
                }
            });
            cr.DownloadSellingQty(year,month);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void loadItemsView(ModelSellingQty[] items){
        mViewModel.allSellings.clear();
        mViewModel.allSellings.addAll( new ArrayList<ModelSellingQty>(Arrays.asList(items)) );
        filterItem("");
    }

    private void filterItem(String s){
        mViewModel.sellingQtys.clear();
        Boolean isFound;
        for (ModelSellingQty ModelSellingQty : mViewModel.allSellings){
            isFound = false;

            for (ModelSellingQty sg : mViewModel.sellingQtys){
                if (sg.getItemname().equals(ModelSellingQty.getItemname())){
                    isFound = true;
                    break;
                }
            }

            if (!isFound){
                if (s.equals("")) {
                    mViewModel.sellingQtys.add(ModelSellingQty);
                }else{
                    if (ModelSellingQty.getItemname().toLowerCase().contains(s.toLowerCase())){
                        mViewModel.sellingQtys.add(ModelSellingQty);
                    }
                }
            }
        }

        if (rvSellings.getAdapter() != null) {
            SellingQtyAdapter.notifyDataSetChanged();
        }
    }
}
