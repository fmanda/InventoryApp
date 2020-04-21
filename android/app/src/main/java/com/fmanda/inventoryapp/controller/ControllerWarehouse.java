package com.fmanda.inventoryapp.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ExpandableListAdapter;
//import android.widget.Toast;

import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.helper.DBHelper;
import com.fmanda.inventoryapp.model.ModelWarehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerWarehouse {
    private Context context;

    public ControllerWarehouse(Context context) {
        this.context = context;
    }

    public List<ModelWarehouse> getWarehouses(){
        try {
            List<ModelWarehouse> projects = new ArrayList<ModelWarehouse>();

            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cursor = rdb.rawQuery("select * from warehouse", null);
            while (cursor.moveToNext()) {
                ModelWarehouse project = new ModelWarehouse();
                project.loadFromCursor(cursor);
                projects.add(project);
            }
            return projects;
        }catch(Exception e){
            AppHelper.makeToast(context, e.toString());
        }
        return null;
    }

    public ModelWarehouse getWarehouse(int id){
        try{
            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();

            Cursor cursor = rdb.rawQuery("select * from warehouse where id =" + String.valueOf(id), null);
            ModelWarehouse modelSetting = new ModelWarehouse();
            if (cursor.moveToNext()) {
                modelSetting.loadFromCursor(cursor);
                return modelSetting;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ModelWarehouse();
    }
}

