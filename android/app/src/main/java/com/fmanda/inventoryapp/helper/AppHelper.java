package com.fmanda.inventoryapp.helper;

import android.content.Context;
import android.widget.Toast;

public class AppHelper {
    public static void makeToast(Context ctx, String msg){
        try {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
