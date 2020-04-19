package com.fmanda.inventoryapp.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.fmanda.inventoryapp.helper.DBHelper;
import com.fmanda.inventoryapp.helper.GsonRequest;
import com.fmanda.inventoryapp.model.BaseModel;
import com.fmanda.inventoryapp.model.ModelItem;
import com.fmanda.inventoryapp.model.ModelTransHeader;
import com.fmanda.inventoryapp.model.ModelWarehouse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



public class ControllerRest {
    private Context context;
    private DBHelper db;
    private ControllerSetting controllerSetting;

//    public String base_url;

//    private static ControllerRest mInstance;
//
//    public static synchronized ControllerRest getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new ControllerRest(context.getApplicationContext());
//        }
//        return mInstance;
//    }

    private int user_id;

    private ControllerRequest controllerRequest;
    protected Listener listener;
    protected ObjectListener objectListener;


    public interface Listener {
        void onSuccess(String msg);
        void onError(String msg); //beware using this on asynctask, u will get exception
        void onProgress(String msg);
    }

    public interface ObjectListener {
        void onSuccess(BaseModel[] obj);
        void onError(String msg);
    }

    private AsyncTaskListener asyncTaskListener;

    public interface AsyncTaskListener {
        void onProgressUpdate(String...msg);
    }


    public void setListener(ControllerRest.Listener listener) {
        this.listener = listener;
    }

    public void setObjectListener(ControllerRest.ObjectListener objectListener) {
        this.objectListener = objectListener;
    }

    public void setAsyncTaskListenerListener(ControllerRest.AsyncTaskListener listener) {
        this.asyncTaskListener = listener;
    }

    public ControllerRest(Context context) {
        this.context = context;
        this.db = DBHelper.getInstance(context);
        this.controllerRequest = ControllerRequest.getInstance(context);
        this.controllerSetting = new ControllerSetting((this.context));
//        this.user_id = controllerSetting.getUserID();

//        this.base_url = "http://" + controllerSetting.getSettingStr("rest_url") + "/";
    }

    protected String base_url(){
        return "http://" + controllerSetting.getSettingStr("rest_url") + "/";
    }

    public String url_project(){
        return base_url() + "warehouse";
    }

    public String url_profitloss(){
        return base_url() + "profitloss";
    }

    public String url_cashflow(){
        return base_url() + "cashflow";
    }

    public String url_getUser(){
        return base_url() + "user";
    }

    public String url_currentsales(){
        return base_url() + "currentsales";
    }

    public String url_currentcashflow(){
        return base_url() + "currentcashflow";
    }

    public String url_salesperiod(){
        return base_url() + "salesperiod";
    }

    private void log(String...s) {
        if (asyncTaskListener != null)
            asyncTaskListener.onProgressUpdate(s);
    }

    private void SaveWarehouse(ModelWarehouse[] warehouses){
        try {
            db.getWritableDatabase().execSQL(new ModelWarehouse().generateSQLDelete("")); //always delete all record projects
            for (ModelWarehouse warehouse : warehouses) {
                warehouse.saveToDB(db.getWritableDatabase());
                log("[warehouse] " + warehouse.getWarehousename() + " inserted");
            }
        }catch(Exception e){
            log(e.toString());
        }
    }


    public boolean DownloadItems(){
        try {
            String url = base_url() + "item";
            GsonRequest<ModelItem[]> gsonReq = new GsonRequest<>(url, ModelItem[].class,
                    new Response.Listener<ModelItem[]>() {
                        @Override
                        public void onResponse(ModelItem[] response) {
                            if (objectListener != null){
                                objectListener.onSuccess(response);
                            }
                            if (listener != null) {
                                listener.onSuccess("");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (objectListener != null){
                                objectListener.onError(error.toString());
                            }
                            if (listener != null) {
                                listener.onError(error.toString());
                            }
                        }
                    }
            );
            this.controllerRequest.addToRequestQueue(gsonReq, url);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean DeleteItem(int itemID){
        try {
            String url = base_url() + "item" + "/" + String.valueOf(itemID);
            StringRequest sr = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            if (listener != null) {
                                listener.onSuccess(response);
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (listener != null) {
                                listener.onError(error.toString());
                            }
                        }
                    }
            );
            this.controllerRequest.addToRequestQueue(sr, url);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean DownloadWarehouse(){
        try {
            String url = base_url() + "warehouse";
            GsonRequest<ModelWarehouse[]> gsonReq = new GsonRequest<>(url, ModelWarehouse[].class,
                    new Response.Listener<ModelWarehouse[]>() {
                        @Override
                        public void onResponse(ModelWarehouse[] response) {
                            SaveWarehouse(response);
                            if (objectListener != null){
                                objectListener.onSuccess(response);
                            }
                            if (listener != null) {
                                listener.onSuccess("");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (objectListener != null){
                                objectListener.onError(error.toString());
                            }
                            if (listener != null) {
                                listener.onError(error.toString());
                            }
                        }
                    }
            );
            this.controllerRequest.addToRequestQueue(gsonReq, url);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public void SyncData( final Boolean async){
        try {
            AsyncRestRunner runner = new AsyncRestRunner(this, 1, 2020);
            runner.execute(async);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SyncProfitLoss(int monthperiod, int yearperiod){
        try {
            AsyncRestRunner runner = new AsyncRestRunner(this, monthperiod, yearperiod);
//            runner.syncProject = Boolean.TRUE;
            runner.syncProfitLoss = Boolean.TRUE;
            runner.execute(Boolean.FALSE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void UpdateItem(ModelItem modelItem){
        try {
            String url = base_url() + "item";
            GsonRequest<ModelItem> gsonReq = new GsonRequest<>(url, modelItem,
                    new Response.Listener<ModelItem>() {
                        @Override
                        public void onResponse(ModelItem response) {
                            if (listener != null) {
                                listener.onSuccess("");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (listener != null) {
                                listener.onError(error.toString());
                            }
                        }
                    }
            );
            this.controllerRequest.addToRequestQueue(gsonReq, url);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void UpdateTransHeader(ModelTransHeader modelTransHeader){
        try {
            String url = base_url() + "transheader";
            GsonRequest<ModelTransHeader> gsonReq = new GsonRequest<>(url, modelTransHeader,
                    new Response.Listener<ModelTransHeader>() {
                        @Override
                        public void onResponse(ModelTransHeader response) {
                            if (listener != null) {
                                listener.onSuccess("");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (listener != null) {
                                listener.onError(error.toString());
                            }
                        }
                    }
            );
            this.controllerRequest.addToRequestQueue(gsonReq, url);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

//next ganti ke java.util.concurrent
class AsyncRestRunner extends AsyncTask<Boolean, String, Void> {
    private ControllerRest controllerRest;
//    public Boolean syncProject = Boolean.FALSE;
    public Boolean syncProfitLoss = Boolean.FALSE;
    private int monthperiod;
    private int yearperiod;

    AsyncRestRunner(ControllerRest controllerRest, int monthperiod, int yearperiod) {
        this.monthperiod = monthperiod;
        this.yearperiod = yearperiod;
        this.controllerRest = controllerRest;
        this.controllerRest.setAsyncTaskListenerListener(new ControllerRest.AsyncTaskListener() {
            @Override
            public void onProgressUpdate(String...msg) {
                publishProgress(msg);
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        controllerRest.listener.onSuccess("FINISH");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (values.length>1) {
            if (values[1] == "error") {
                controllerRest.listener.onError(values[0]);
            }
        }else{
            controllerRest.listener.onProgress(values[0]);
        }
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        try{
            boolean async = booleans[0];
            publishProgress("Connecting to Rest API : " + controllerRest.base_url());
            boolean result;
//            if (syncProject) {
//                publishProgress("Download Project");
//                if (!controllerRest.DownloadProject(async)) return null;
//            }

            if (syncProfitLoss) {
                publishProgress("Download Profit Loss");
//                if (!controllerRest.DownloadProfitLoss(async, this.monthperiod, this.yearperiod)) return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}




//PRODUCT
//    public void DownloadProducts(){
//        GsonRequest<ModelProduct[]> gsonReq = new GsonRequest<ModelProduct[]>(product_get_url(), ModelProduct[].class,
//                new Response.Listener<ModelProduct[]>() {
//                    @Override
//                    public void onResponse(ModelProduct[] response) {
//                        try {
//                            ImageHelper img = new ImageHelper(context);
//                            for (ModelProduct prod : response) {
//                                prod.setIDFromUID(db.getReadableDatabase(), prod.getUid());
//
//                                ModelProduct oldProduct = new ModelProduct();
//                                oldProduct.loadFromDB(db.getReadableDatabase(), prod.getId());
//
//                                //delete old modifier
//                                Boolean isFound = false;
//                                for (ModelModifier oldmodifier : oldProduct.modifiers){
//                                    isFound = false;
//                                    for (ModelModifier newmodifier : prod.modifiers) {
//                                        if (newmodifier.getUid() == oldmodifier.getUid()){
//                                            isFound = true;
//                                            continue;
//                                        }
//                                    }
//                                    if (!isFound) oldmodifier.deleteFromDB(db.getWritableDatabase());
//                                }
//
//                                prod.setImg(prod.getImg().replace(".jpg","")); //server contain .jpg
//                                prod.saveToDB(db.getWritableDatabase());
//
//                                for (ModelModifier modelModifier : prod.modifiers){
//                                    modelModifier.setIDFromUID(db.getReadableDatabase(), modelModifier.getUid());
//                                    modelModifier.setProduct_id(prod.getId());
//                                    modelModifier.saveToDB(db.getWritableDatabase());
//                                }
//
//
//                                //replace format to png
//                                if (!prod.getImg().equals("")) {
//                                    img.setFileName(prod.getImg());
//                                    if (!img.checFileExist()) {
//                                        DownloadProductImage(prod.getImg());
//                                    }
//                                }
//                                listener.onSuccess(prod.getName() + " updated");
//                            }
//                        }catch(Exception e){
//                            listener.onError(e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        listener.onError(error.toString());
//                    }
//                }
//        );
//        this.controllerRequest.addToRequestQueue(gsonReq,product_get_url());
//    }


//    public void UploadOrders(){
//        ControllerOrder controllerorder = new ControllerOrder(this.context);
//        List<ModelOrder> orders = controllerorder.getModifiedOrders();
//        final SQLiteDatabase db = DBHelper.getInstance(this.context).getReadableDatabase();
//        final SQLiteDatabase dbw = DBHelper.getInstance(this.context).getWritableDatabase();
//
//        //tambahkan yg diedit saja
//
////        GsonBuilder gsonBuilder;
////        Gson gson;
////        gsonBuilder = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss");
////        gson = gsonBuilder.create();
//
//        for (final ModelOrder modelOrder : orders) {
//            modelOrder.prepareUpload(db);
//            modelOrder.setCompany_id(this.company_id);
//            modelOrder.setUnit_id(this.unit_id);
////            Toast.makeText(context, debug, Toast.LENGTH_SHORT).show();
//
//            GsonRequest<ModelOrder> gsonReq = new GsonRequest<ModelOrder>(order_post_url(), modelOrder,
//                    new Response.Listener<ModelOrder>() {
//                        @Override
//                        public void onResponse(ModelOrder response) {
//                            //update ID
//                            try{
//                                modelOrder.setuid(response.getuid());
//                                modelOrder.setUploaded(1);
//                                modelOrder.saveToDB(dbw);
//                                listener.onSuccess(response.getOrderno() + " updated");
//                            }catch (Exception e){
//                                listener.onError(e.toString());
//                            }
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            listener.onError(error.toString());
//                        }
//                    }
//            );
//            this.controllerRequest.addToRequestQueue(gsonReq,customer_post_url());
//        }
//
//        //test
//
//
//    }
//
//
//




//    public void DownloadProductImage(final String img_name){
//        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
//        String get_url = image_get_url(img_name);
//
//        // Initialize a new ImageRequest
//        ImageRequest imageRequest = new ImageRequest(
//                get_url, // Image URL
//                new Response.Listener<Bitmap>() { // Bitmap listener
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        // Do something with response
//                        ImageHelper img = new ImageHelper(context);
//                        img.setFileName(img_name);
//                        img.save(response);
//                    }
//                },
//                0, // Image width
//                0, // Image height
//                CENTER_CROP, // Image scale type
//                Bitmap.Config.RGB_565, //Image decode configuration
//                new Response.ErrorListener() { // Error listener
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Do something with error response
//                        listener.onError(error.toString());
////                        error.printStackTrace();
//                    }
//                }
//        );
//
//        this.controllerRequest.addToRequestQueue(imageRequest,image_get_url(img_name));
//    }