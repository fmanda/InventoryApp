package com.fmanda.inventoryapp.ui.item;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.Toast;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.controller.ControllerRest;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.model.ModelItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class UpdateItemFragment extends Fragment {
    public ModelItem modelItem = null;
    private TextInputEditText txtNama;
    private TextInputEditText txtGroup;
    private TextInputEditText txtHargaBeli;
    private TextInputEditText txtHargaJual;
    private Button btnUpdate;
    private Button btnDelete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_item, container, false);

        txtNama = root.findViewById(R.id.txtNama);
        txtGroup = root.findViewById(R.id.txtGroup);
        txtHargaBeli = root.findViewById(R.id.txtHargaBeli);
        txtHargaJual = root.findViewById(R.id.txtHargaJual);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnDelete = root.findViewById(R.id.btnDelete);

        if (getArguments() != null){
            UpdateItemFragmentArgs args = UpdateItemFragmentArgs.fromBundle(getArguments());
            if (args.getModelItem() != null) {
                modelItem = args.getModelItem();
                loadData(modelItem);
            }else{
                btnDelete.setVisibility(View.GONE);
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        return root;

    }

    private void loadData(ModelItem modelItem){
        txtNama.setText(modelItem.getItemname());
        txtGroup.setText(modelItem.getGroupname());
        txtHargaBeli.setText(modelItem.getPurchaseprice().toString());
        txtHargaJual.setText(modelItem.getSellingprice().toString());
    }

    private void saveData(){
        if (modelItem == null){
            modelItem = new ModelItem();
        }

        modelItem.setItemname(txtNama.getText().toString());
        modelItem.setGroupname(txtGroup.getText().toString());

        try{
            if (!txtHargaBeli.getText().toString().equals("")) {
                modelItem.setPurchaseprice(Double.parseDouble(txtHargaBeli.getText().toString()));
            }
            if (!txtHargaJual.getText().toString().equals("")) {
                modelItem.setSellingprice(Double.parseDouble(txtHargaJual.getText().toString()));
            }
        } catch (NumberFormatException e) {
            AppHelper.makeToast(getContext(), "Nilai Harga Beli / Harga Jual salah");
        }

        ControllerRest cr = new ControllerRest(getContext());
        cr.setListener(new ControllerRest.Listener() {
            @Override
            public void onSuccess(String msg) {
                AppHelper.makeToast(getContext(), "Data Item berhasil disimpan");
                Navigation.findNavController(getView()).navigate(R.id.nav_item);
            }

            @Override
            public void onError(String msg) {
                AppHelper.makeToast(getContext(), msg);
            }

            @Override
            public void onProgress(String msg) {

            }
        });
        cr.UpdateItem(modelItem);



    }

    private void deleteItem(){
        if (modelItem == null){
            Snackbar.make(getView(), "Tidak ada data yang akan Dihapus", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Anda yaking menghapus item ini?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                ControllerRest cr = new ControllerRest(getContext());
                cr.setListener(new ControllerRest.Listener() {
                    @Override
                    public void onSuccess(String msg) {
                        AppHelper.makeToast(getContext(), "Data Item berhasil dihapus");
                        Navigation.findNavController(getView()).navigate(R.id.nav_item);
                    }

                    @Override
                    public void onError(String msg) {
                        AppHelper.makeToast(getContext(), msg);
                    }

                    @Override
                    public void onProgress(String msg) {

                    }
                });
                cr.DeleteItem(modelItem.getId());


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
}
