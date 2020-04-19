package com.fmanda.inventoryapp.ui.item;

import androidx.lifecycle.ViewModel;

import com.fmanda.inventoryapp.model.ModelItem;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public List<ModelItem> items = new ArrayList<ModelItem>();
    public List<ModelItem> allitems = new ArrayList<ModelItem>();
}
