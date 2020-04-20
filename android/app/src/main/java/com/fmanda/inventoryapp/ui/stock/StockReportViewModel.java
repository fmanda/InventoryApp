package com.fmanda.inventoryapp.ui.stock;

import androidx.lifecycle.ViewModel;

import com.fmanda.inventoryapp.model.ModelStockReport;

import java.util.ArrayList;
import java.util.List;

public class StockReportViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    List<ModelStockReport> stockReports = new ArrayList<>();
    List<ModelStockReport> allStocks = new ArrayList<>();
}
