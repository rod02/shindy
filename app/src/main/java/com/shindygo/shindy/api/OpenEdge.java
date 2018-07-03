package com.shindygo.shindy.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.shindygo.shindy.Api;
import com.shindygo.shindy.Incept;
import com.shindygo.shindy.interfaces.ShindiServer;
import com.shindygo.shindy.model.OpenedgeSetupResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OpenEdge {

    private static OpenEdge instance;

    private static final String TAG = OpenEdge.class.getSimpleName();
    private static final String BASE_URL = "https://ws.test.paygateway.com/HostPayService/v1/hostpay/";

    private final static String XWEB_ID = "800000012412";
    private final static String TERMINAL_ID = "80027765";
    private final static String AUTH_KEY = "NBVWzxL911QP2XE0SXdkpc15TSaFa0l0";

    private final static String TRANSACTION_TYPE = "CREDIT_CARD";
    private final static String CHARGE_TYPE = "SALE";
    private final static String INDUSTRY = "RETAIL";
    private final static String DUPLICATE_CHECK = "CHECK";
    private final static String ENTRY_MODE = "AUTO";
    private final static String RETURN_TARGET = "_self";
    private final static String POSTBACK_URL = "#";
    private final static String RETURN_URL = "#";



    ShindiServer shindiServer;
    Retrofit retrofit ;
    Context context;

    public OpenEdge(@NonNull Context context) {
        this.context = context;



        OkHttpClient client = new OkHttpClient.Builder()
               // .addInterceptor(new Incept("shindy@admin", "orange@123"))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        this.retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.shindiServer = retrofit.create(ShindiServer.class);

    }


    public void setupRequest(String orderId, String chargeTotal, String orderUserId, Callback<ResponseBody> callback){

        Map<String,String> map = new HashMap<>();
        map.put("order_id",orderId);
        map.put("charge_total",chargeTotal);
        map.put("order_user_id",orderUserId);

        map.put("transaction_type",TRANSACTION_TYPE);
        map.put("charge_type",CHARGE_TYPE);
        map.put("industry ",INDUSTRY);
        map.put("duplicate_check",DUPLICATE_CHECK);
        map.put("entry_mode",ENTRY_MODE);
        map.put("return_target",RETURN_TARGET);
        map.put("return_url",RETURN_URL);

        map.put("xweb_id",XWEB_ID);
        map.put("terminal_id",TERMINAL_ID);
        map.put("auth_key",AUTH_KEY);


/*

        map.put("manage_payer_data",MANAGE_PAYER_DATA);
        map.put("payer_identifier",PAYER_IDENTIFIER);
*/


        shindiServer.setupRequest(map).enqueue(callback);
    }

    public void paypage(String sealedSetupParameters, Callback<ResponseBody> callback){
        shindiServer.paypage(sealedSetupParameters).enqueue(callback);
    }


    public static OpenEdge getInstance() {
        if(instance!=null)
            return instance;
        return new OpenEdge(Api.getContext());
    }

}
