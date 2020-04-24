package com.fmanda.inventoryapp.model;

import java.util.Date;

public class ModelSellingPeriod extends BaseModel {
    @TableField
    private Date transdate;
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

    public Date getTransdate() {
        return transdate;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }
}