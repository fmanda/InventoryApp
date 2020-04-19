package com.fmanda.inventoryapp.ui.transheader;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelItem;

public class DialogQtyFragment extends DialogFragment {

    Button btnUpdate;
    Button btnRemove;
    Button btnAdd;
    TextView txtName;
    TextView txtQty;
    ModelItem modelItem;
    private QtyUpdateListener qtyUpdateListener;

    private DialogQtyViewModel mViewModel;

    public static DialogQtyFragment newInstance() {
        return new DialogQtyFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DialogQtyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dialogqty, container, false);

        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnRemove = root.findViewById(R.id.btnRemove);
        btnAdd = root.findViewById(R.id.btnAdd);
        txtName = root.findViewById(R.id.txtName);
        txtQty = root.findViewById(R.id.txtQty);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(txtQty.getText().toString()) + 1;
                txtQty.setText(String.valueOf(qty));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(txtQty.getText().toString()) - 1;
                if (qty < 0) qty = 0;
                txtQty.setText(String.valueOf(qty));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qtyUpdateListener.onFinishUpdate(modelItem, Integer.parseInt(txtQty.getText().toString()));
            }
        });

        if (this.modelItem != null){
            txtName.setText("Update Qty : " + modelItem.getItemname().toString());
            int qty = modelItem.qty;
            if (qty == 0) qty += 1;
            txtQty.setText(String.valueOf(qty));
        }

        return root;
    }

    public void setItem(ModelItem modelItem){
        if (modelItem == null) return;
        this.modelItem = modelItem;
    }

    public interface QtyUpdateListener{
        void onFinishUpdate(ModelItem modelItem, int qty);
    }

    public void setQtyUpdateListener(QtyUpdateListener qtyUpdateListener){
        this.qtyUpdateListener = qtyUpdateListener;
    }




}
