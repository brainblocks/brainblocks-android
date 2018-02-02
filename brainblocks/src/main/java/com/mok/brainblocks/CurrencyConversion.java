package com.mok.brainblocks;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alexander on 2/2/2018.
 */

public class CurrencyConversion {
    String url;
    StringBuilder sb;
    String TAG;
    RequestQueue queue;

    public CurrencyConversion(Context context){
        url = context.getString(R.string.conversion_url);
        TAG = "bb-CurrencyConversion";
        queue = Volley.newRequestQueue(context);
    }

    public void convertFiatToXRB(String currency, String amount, final VolleyCallback callback){
        sb = new StringBuilder();
        String conversionUrl = sb.append(url).append(currency).append("/").append(amount).append("/rai").toString();

        StringRequest verifyTransfer = new StringRequest(Request.Method.GET, conversionUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            String rai = obj.getString("rai");
                            callback.onSuccess(rai);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(verifyTransfer);
    }

}
