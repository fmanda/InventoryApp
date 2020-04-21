package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.helper.CurrencyHelper;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelWarehouse;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<ModelItem> items;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;

    public ItemAdapter(Context context, List<ModelItem> items) {
        this.context = context;
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelItem = items.get(i);
        viewHolder.txtNama.setText(viewHolder.modelItem.getItemname());
//        viewHolder.txtGroup.setText(viewHolder.modelItem.getGroupname());
//        viewHolder.txtPrice.setText(CurrencyHelper.format(viewHolder.modelItem.getSellingprice(), true) );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelItem modelItem;
        public TextView txtNama;
        public LinearLayout lnItemAdapter;
//        public TextView txtGroup;
//        public TextView txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNama);
            lnItemAdapter = itemView.findViewById(R.id.lnItemAdapter);
//            txtGroup = itemView.findViewById(R.id.txtGroup);
//            txtPrice = itemView.findViewById(R.id.txtPrice);

            lnItemAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(modelItem);
                    }
                }
            });
        }
        //
        @Override
        public void onClick(View v) {
            AppHelper.makeToast(context, "click");
            if (itemClickListener != null) {
                itemClickListener.onClick(modelItem);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelItem modelItem);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


}

