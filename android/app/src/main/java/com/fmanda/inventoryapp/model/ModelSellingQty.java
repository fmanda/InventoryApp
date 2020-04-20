package com.fmanda.inventoryapp.model;

public class ModelSellingQty extends BaseModel {
    @TableField
    private String warehousename;
    @TableField
    private String itemname;
    @TableField
    private double qty;

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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}