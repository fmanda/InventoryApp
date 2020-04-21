package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.controller.ControllerWarehouse;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.model.ModelWarehouse;
import com.fmanda.inventoryapp.ui.transheader.ListTransHeader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by fma on 7/30/2017.
 */

public class ListTransAdapter extends RecyclerView.Adapter<ListTransAdapter.ViewHolder> {
    private Context context;
    private List<ModelTransHeader> trans;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;
    private List<ModelWarehouse> warehouses;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm", new Locale("id", "ID"));

    public ListTransAdapter(Context context, List<ModelTransHeader> trans) {
        this.context = context;
        this.trans = trans;
        this.mInflater = LayoutInflater.from(context);
        ControllerWarehouse cw = new ControllerWarehouse(context);
        this.warehouses = cw.getWarehouses();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_listtrans_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelTransHeader = trans.get(i);
        if (viewHolder.modelTransHeader.getHeader_flag() == 1){
            viewHolder.txtTransType.setText("Pembelian Barang");
        }else if (viewHolder.modelTransHeader.getHeader_flag() == 2){
            viewHolder.txtTransType.setText("Penjualan Barang");
        }else if (viewHolder.modelTransHeader.getHeader_flag() == 3){
            viewHolder.txtTransType.setText("Transfer Stock");
        }
        viewHolder.txtTransDate.setText(formatter.format(viewHolder.modelTransHeader.getTransdate()));

        if (this.warehouses !=null){
            for (ModelWarehouse mw : this.warehouses){
                if (mw.getId() == viewHolder.modelTransHeader.getWarehouse_id()){
                    viewHolder.txtLocation.setText(mw.getWarehousename());
                }
            }
        }

//        viewHolder.txtGroup.setText(viewHolder.modelItem.getGroupname());
//        viewHolder.txtPrice.setText(CurrencyHelper.format(viewHolder.modelItem.getSellingprice(), true) );
    }

    @Override
    public int getItemCount() {
        return trans.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelTransHeader modelTransHeader;
        public TextView txtTransType;
        public TextView txtLocation;
        public TextView txtTransDate;
        public LinearLayout lnTransAdapter;
//        public TextView txtGroup;
//        public TextView txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTransType = itemView.findViewById(R.id.txtTransType);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtTransDate = itemView.findViewById(R.id.txtTransDate);
            lnTransAdapter = itemView.findViewById(R.id.lnTransAdapter);
//            txtGroup = itemView.findViewById(R.id.txtGroup);
//            txtPrice = itemView.findViewById(R.id.txtPrice);

            lnTransAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(modelTransHeader);
                    }
                }
            });
        }
        //
        @Override
        public void onClick(View v) {
            AppHelper.makeToast(context, "click");
            if (itemClickListener != null) {
                itemClickListener.onClick(modelTransHeader);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelTransHeader modelTransHeader);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


}

