package com.fmanda.inventoryapp.model;

public class ModelStockReport extends BaseModel {
    @TableField
    private String warehousename;
    @TableField
    private String itemname;
    @TableField
    private double stock;

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double qty) {
        this.stock = qty;
    }
}