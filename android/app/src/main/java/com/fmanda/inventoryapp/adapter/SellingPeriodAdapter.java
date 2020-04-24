package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelSellingPeriod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fma on 7/30/2017.
 */

public class SellingPeriodAdapter extends RecyclerView.Adapter<SellingPeriodAdapter.ViewHolder> {
    private Context context;
    private List<ModelSellingPeriod> SellingPeriods;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;
    private List<ModelSellingPeriod> sellingGroups;
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("id", "ID"));

    public SellingPeriodAdapter(Context context, List<ModelSellingPeriod> SellingPeriods, List<ModelSellingPeriod> sellingGroups) {
        this.context = context;
        this.SellingPeriods = SellingPeriods;
        this.sellingGroups = sellingGroups;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_sellingperiod_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelSellingPeriod = sellingGroups.get(i);
        viewHolder.txtTransdate.setText(formatter.format(viewHolder.modelSellingPeriod.getTransdate()));
        viewHolder.txtWarehouse.setText(viewHolder.modelSellingPeriod.getWarehousename());

        SellingPeriodAdapterDetail SellingPeriodAdapterDetail = new SellingPeriodAdapterDetail(context, getStockRepors(
                viewHolder.modelSellingPeriod.getWarehousename(),
                viewHolder.modelSellingPeriod.getTransdate())
        );
        viewHolder.rvDetail.setLayoutManager(new GridLayoutManager(context, 1));
        viewHolder.rvDetail.setAdapter(SellingPeriodAdapterDetail);
    }

    @Override
    public int getItemCount() {
        return sellingGroups.size();
    }

    public List<ModelSellingPeriod> getStockRepors(String warehousename, Date date){
        List<ModelSellingPeriod> details = new ArrayList<>();
        for (ModelSellingPeriod modelSellingPeriod : SellingPeriods){
            if (
                    warehousename.toLowerCase().equals(modelSellingPeriod.getWarehousename().toLowerCase())
                    && date.equals(modelSellingPeriod.getTransdate())
//                     && formatter.format(date).equals(formatter.format(modelSellingPeriod.getTransdate()))
            ){
                details.add(modelSellingPeriod);
            }
        }
        return details;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelSellingPeriod modelSellingPeriod;
        public TextView txtTransdate;
        public TextView txtWarehouse;
        public RecyclerView rvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTransdate = itemView.findViewById(R.id.txtTransDate);
            txtWarehouse = itemView.findViewById(R.id.txtWarehouse);
            rvDetail = itemView.findViewById(R.id.rvDetail);
        }
        //
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onClick(modelSellingPeriod);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelSellingPeriod ModelSellingPeriod);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}


class SellingPeriodAdapterDetail extends RecyclerView.Adapter<SellingPeriodAdapterDetail.ViewHolder> {
    private Context context;
    private List<ModelSellingPeriod> SellingPeriods;
    private LayoutInflater mInflater;

    public SellingPeriodAdapterDetail(Context context, List<ModelSellingPeriod> SellingPeriods) {
        this.context = context;
        this.SellingPeriods = SellingPeriods;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_sellingperioddetail_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.ModelSellingPeriod = SellingPeriods.get(i);
//        viewHolder.txtWarehouse.setText(viewHolder.ModelSellingPeriod.getWarehousename());
        viewHolder.txtNama.setText(viewHolder.ModelSellingPeriod.getItemname());
        int qty = (int) viewHolder.ModelSellingPeriod.getQty();
        viewHolder.txtQty.setText(String.valueOf(qty));

        if (qty >= 0) {
            viewHolder.txtQty.setTextColor(context.getColor(R.color.colorDarkGreen));
        }else{
            viewHolder.txtQty.setTextColor(context.getColor(R.color.colorWarning));
        }
    }

    @Override
    public int getItemCount() {
        return SellingPeriods.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelSellingPeriod ModelSellingPeriod;
        public TextView txtNama;
        public TextView txtQty;
//        public TextView txtWarehouse;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNama);
            txtQty = itemView.findViewById(R.id.txtQty);
//            txtWarehouse = itemView.findViewById(R.id.txtWarehouse);
        }

        @Override
        public void onClick(View v) {

        }
    }


}

