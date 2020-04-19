package com.fmanda.inventoryapp.model;

import java.util.Date;

public class ModelTransDetail extends BaseModel {
    @TableField
    private int header_id;
    @TableField
    private Date transdate;
    @TableField
    private int header_flag;
    @TableField
    private int warehouse_id;
    @TableField
    private int item_id;
    @TableField
    private double qty;
    @TableField
    private double purchaseprice;
    @TableField
    private double sellingprice;

    public int getHeader_id() {
        return header_id;
    }

    public void setHeadder_id(int header_id) {
        this.header_id = header_id;
    }

    public Date getTransdate() {
        return transdate;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    public int getHeader_flag() {
        return header_flag;
    }

    public void setHeader_flag(int header_flag) {
        this.header_flag = header_flag;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(double purchaseprice) {
        this.purchaseprice = purchaseprice;
    }

    public double getSellingprice() {
        return sellingprice;
    }

    public void setSellingprice(double sellingprice) {
        this.sellingprice = sellingprice;
    }
}
