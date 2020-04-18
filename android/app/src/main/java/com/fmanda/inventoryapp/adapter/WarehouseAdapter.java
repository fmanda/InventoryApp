package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelWarehouse;

import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.ViewHolder> {
    private Context context;
    private List<ModelWarehouse> projects;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;

    public WarehouseAdapter(Context context, List<ModelWarehouse> projects) {
        this.context = context;
        this.projects = projects;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_project_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelproject = projects.get(i);
        viewHolder.txtNama.setText(viewHolder.modelproject.getWarehousename());

        WarehouseAdapterDetail warehouseAdapterDetail = new WarehouseAdapterDetail(context, projects);
        viewHolder.rvDetail.setLayoutManager(new GridLayoutManager(context, 1));
        viewHolder.rvDetail.setAdapter(warehouseAdapterDetail);

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelWarehouse modelproject;
        public TextView txtNama;
        public TextView txtKode;
        public RecyclerView rvDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtKode = itemView.findViewById(R.id.txtKode);
            txtNama = itemView.findViewById(R.id.txtNama);
            rvDetail = itemView.findViewById(R.id.rvDetail);
        }
        //
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onClick(modelproject);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelWarehouse modelWarehouse);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


}

class WarehouseAdapterDetail extends RecyclerView.Adapter<WarehouseAdapterDetail.ViewHolder> {
    private Context context;
    private List<ModelWarehouse> projects;
    private LayoutInflater mInflater;

    public WarehouseAdapterDetail(Context context, List<ModelWarehouse> projects) {
        this.context = context;
        this.projects = projects;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public WarehouseAdapterDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_projectdetail_layout, viewGroup, false);
        WarehouseAdapterDetail.ViewHolder viewHolder = new WarehouseAdapterDetail.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WarehouseAdapterDetail.ViewHolder viewHolder, int i) {
        viewHolder.modelproject = projects.get(i);
        viewHolder.txtNama.setText(viewHolder.modelproject.getWarehousename());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelWarehouse modelproject;
        public TextView txtNama;
        public TextView txtKode;

        public ViewHolder(View itemView) {
            super(itemView);
            txtKode = itemView.findViewById(R.id.txtKode);
            txtNama = itemView.findViewById(R.id.txtNama);
        }

        @Override
        public void onClick(View v) {

        }
    }


}