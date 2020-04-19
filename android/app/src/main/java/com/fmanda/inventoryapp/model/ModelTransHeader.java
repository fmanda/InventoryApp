package com.fmanda.inventoryapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelTransHeader extends BaseModel {
    @TableField
    private String transno;
    @TableField
    private Date transdate;
    @TableField
    private int header_flag;
    @TableField
    private int warehouse_id;
    @TableField
    private int dest_warehouse_id;
    private List<ModelTransDetail> items = new ArrayList<ModelTransDetail>();

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
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

    public int getDest_warehouse_id() {
        return dest_warehouse_id;
    }

    public void setDest_warehouse_id(int dest_warehouse_id) {
        this.dest_warehouse_id = dest_warehouse_id;
    }

    public List<ModelTransDetail> getItems() {
        return items;
    }

    public void setItems(List<ModelTransDetail> items) {
        this.items = items;
    }
}

class ModelTransDetail extends BaseModel {
    @TableField
    private int headder_id;
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

    public int getHeadder_id() {
        return headder_id;
    }

    public void setHeadder_id(int headder_id) {
        this.headder_id = headder_id;
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
