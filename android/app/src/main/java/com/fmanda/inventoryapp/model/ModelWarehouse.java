package com.fmanda.inventoryapp.model;

public class ModelWarehouse extends BaseModel {
    @TableField
    private String warehousename;

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public ModelWarehouse(){
    }

}