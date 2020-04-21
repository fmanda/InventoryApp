package com.fmanda.inventoryapp.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.widget.Toast;

import com.fmanda.inventoryapp.helper.AppHelper;
import com.fmanda.inventoryapp.helper.DBHelper;
import com.fmanda.inventoryapp.model.ModelSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerSetting {
    private Context context;
    public boolean isLogin = false;

    public ControllerSetting(Context context) {
        this.context = context;
    }

    private static ControllerSetting mInstance;

    public static synchronized ControllerSetting getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ControllerSetting(context.getApplicationContext());
        }
        return mInstance;
    }

    public List<ModelSetting> getSettings(){
        try {
            List<ModelSetting> settings = new ArrayList<ModelSetting>();

            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cursor = rdb.rawQuery("select * from setting", null);
            while (cursor.moveToNext()) {
                ModelSetting setting = new ModelSetting();
                setting.loadFromCursor(cursor);
                settings.add(setting);
            }
            return settings;
        }catch(Exception e){
            AppHelper.makeToast(context, e.toString());
        }
        return null;
    }

    public void updateSetting(String varname, String varvalue){

        ModelSetting setting = getSetting(varname);
        if (setting != null) {
            setting.setVarvalue(varvalue);
            setting.saveToDB(DBHelper.getInstance(this.context).getWritableDatabase());
        }

    }

    public String getSettingStr(String varname){
        ModelSetting setting = getSetting(varname);
        if (setting != null) {
            return setting.getVarvalue();
        }else{
            return "";
        }
    }



    public ModelSetting getSetting(String varname){
        try {
            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cursor = rdb.rawQuery("select * from setting where varname = '" + varname + "'", null);
            ModelSetting modelSetting = new ModelSetting(varname, "");
            if (cursor.moveToNext()) {
                modelSetting.loadFromCursor(cursor);
                return modelSetting;
            }
        }catch(Exception ex){
            AppHelper.makeToast(context, ex.toString());
        }
        return new ModelSetting(varname,"");
    }

}

