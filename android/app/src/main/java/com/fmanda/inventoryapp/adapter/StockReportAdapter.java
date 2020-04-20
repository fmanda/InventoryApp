package com.fmanda.inventoryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmanda.inventoryapp.R;
import com.fmanda.inventoryapp.model.ModelStockReport;
import com.fmanda.inventoryapp.model.ModelWarehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class StockReportAdapter extends RecyclerView.Adapter<StockReportAdapter.ViewHolder> {
    private Context context;
    private List<ModelStockReport> stockReports;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;
    private List<ModelStockReport> stockGroups;

    public StockReportAdapter(Context context, List<ModelStockReport> stockReports, List<ModelStockReport> stockGroups) {
        this.context = context;
        this.stockReports = stockReports;
        this.stockGroups = stockGroups;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_stockreport_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.modelStockReport = stockGroups.get(i);
        viewHolder.txtNama.setText(viewHolder.modelStockReport.getItemname());

        StockReportAdapterDetail stockReportAdapterDetail = new StockReportAdapterDetail(context, getStockRepors(viewHolder.modelStockReport.getItemname()));
        viewHolder.rvDetail.setLayoutManager(new GridLayoutManager(context, 1));
        viewHolder.rvDetail.setAdapter(stockReportAdapterDetail);
    }

    @Override
    public int getItemCount() {
        return stockGroups.size();
    }

    public List<ModelStockReport> getStockRepors(String itemname){
        List<ModelStockReport> details = new ArrayList<>();
        for (ModelStockReport modelStockReport : stockReports){
            if (itemname.toLowerCase().equals(modelStockReport.getItemname().toLowerCase())){
                details.add(modelStockReport);
            }
        }
        return details;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelStockReport modelStockReport;
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
                itemClickListener.onClick(modelStockReport);
            }
        }


    }

    public interface ItemClickListener {
        void onClick(ModelStockReport modelStockReport);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}


class StockReportAdapterDetail extends RecyclerView.Adapter<StockReportAdapterDetail.ViewHolder> {
    private Context context;
    private List<ModelStockReport> stockReports;
    private LayoutInflater mInflater;

    public StockReportAdapterDetail(Context context, List<ModelStockReport> stockReports) {
        this.context = context;
        this.stockReports = stockReports;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public StockReportAdapterDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_stockreportdetail_layout, viewGroup, false);
        StockReportAdapterDetail.ViewHolder viewHolder = new StockReportAdapterDetail.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StockReportAdapterDetail.ViewHolder viewHolder, int i) {
        viewHolder.modelStockReport = stockReports.get(i);
        viewHolder.txtNama.setText(viewHolder.modelStockReport.getWarehousename());
        int qty = (int) viewHolder.modelStockReport.getStock();
        viewHolder.txtQty.setText("x " + String.valueOf(qty));
    }

    @Override
    public int getItemCount() {
        return stockReports.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ModelStockReport modelStockReport;
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

