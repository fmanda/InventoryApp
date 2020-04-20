package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelSellingQty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class SellingQtyAdapter extends RecyclerView.Adapter<SellingQtyAdapter.ViewHolder> {
    private Context context;
    private List<ModelSellingQty> sellingqtys;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;
    private List<ModelSellingQty> sellingGroups;

    public SellingQtyAdapter(Context context, List<ModelSellingQty> sellingqtys, List<ModelSellingQty> sellingGroups) {
        this.context = context;
        this.sellingqtys = sellingqtys;
        this.sellingGroups = sellingGroups;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_sellingqty_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.ModelSellingQty = sellingGroups.get(i);
        viewHolder.txtNama.setText(viewHolder.ModelSellingQty.getItemname());

        SellingQtyAdapterDetail SellingQtyAdapterDetail = new SellingQtyAdapterDetail(context, getStockRepors(viewHolder.ModelSellingQty.getItemname()));
        viewHolder.rvDetail.setLayoutManager(new GridLayoutManager(context, 1));
        viewHolder.rvDetail.setAdapter(SellingQtyAdapterDetail);
    }

    @Override
    public int getItemCount() {
        return sellingGroups.size();
    }

    public List<ModelSellingQty> getStockRepors(String itemname){
        List<ModelSellingQty> details = new ArrayList<>();
        for (ModelSellingQty ModelSellingQty : sellingqtys){
            if (itemname.toLowerCase().equals(ModelSellingQty.getItemname().toLowerCase())){
                details.add(ModelSellingQty);
            }
        }
        return details;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelSellingQty ModelSellingQty;
        public TextView txtNama;
        public RecyclerView rvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNama);
            rvDetail = itemView.findViewById(R.id.rvDetail);
        }
        //
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onClick(ModelSellingQty);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelSellingQty ModelSellingQty);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}


class SellingQtyAdapterDetail extends RecyclerView.Adapter<SellingQtyAdapterDetail.ViewHolder> {
    private Context context;
    private List<ModelSellingQty> sellingqtys;
    private LayoutInflater mInflater;

    public SellingQtyAdapterDetail(Context context, List<ModelSellingQty> sellingqtys) {
        this.context = context;
        this.sellingqtys = sellingqtys;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_sellingqtydetail_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.ModelSellingQty = sellingqtys.get(i);
        viewHolder.txtNama.setText(viewHolder.ModelSellingQty.getWarehousename());
        int qty = (int) viewHolder.ModelSellingQty.getQty();
        viewHolder.txtQty.setText(String.valueOf(qty));

        if (qty >= 0) {
            viewHolder.txtQty.setTextColor(context.getColor(R.color.colorDarkGreen));
        }else{
            viewHolder.txtQty.setTextColor(context.getColor(R.color.colorWarning));
        }
    }

    @Override
    public int getItemCount() {
        return sellingqtys.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelSellingQty ModelSellingQty;
        public TextView txtNama;
        public TextView txtQty;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNama);
            txtQty = itemView.findViewById(R.id.txtQty);
        }

        @Override
        public void onClick(View v) {

        }
    }


}

