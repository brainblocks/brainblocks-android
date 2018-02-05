package com.mok.brainblocks;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Alexander on 1/29/2018.
 */

public class Brainblock {
    private static Brainblock singleton = null;

    String TAG = "BrainBlocks-Android";
    Context context;
    RequestQueue queue;
    String status;
    String token;
    String url;
    String yourDestination;
    String clientDestination;
    AlertDialog dialog;
    DialogHelper dialogHelper;
    StringBuilder sb;


    private Brainblock(Context context, String destination){
        this.context = context;
        queue = Volley.newRequestQueue(context);
        status = "";
        token = "";
        url = context.getString(R.string.session_url);
        yourDestination = destination;
    }

    public static Brainblock getBrainBlock(Context context, String destination){
        if(singleton == null){
            singleton = new Brainblock(context, destination);
        }

        return singleton;
    }


    public void pay_with_XRB_start(int amount){
        if(amount < 1){
            throw new IllegalArgumentException("Amount must be greater than 1 rai.");
        }

        final String strAmount = String.valueOf(amount);

        StringRequest startSession = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            token = jsonResponse.getString("token");
                            clientDestination = jsonResponse.getString("account");
                            completeTransaction(strAmount, clientDestination);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount", strAmount);
                params.put("destination", yourDestination);
                return params;
            }
        };

        queue.add(startSession);
    }

    public void completeTransaction(String amount, String address){
        dialogHelper = new DialogHelper(context);
        dialogHelper.createDialog(address, amount);

        check_XRB_transfer();
    }

    public void check_XRB_transfer(){
//        String checkTransferUrl = url + "/" + token + "/transfer";
        sb = new StringBuilder();
        String checkTransferUrl = sb.append(url).append(token).append("/transfer").toString();

        StringRequest checkTransfer = new StringRequest(Request.Method.POST, checkTransferUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        //user did not pay within 120 seconds
                        if(response.trim().isEmpty()){
                            dialogHelper.updateDialogFailed();
                        } else {
                            //user paid successfully
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                status = jsonResponse.get("status").toString();

                                complete_XRB_transfer();
                            } catch (JSONException e) {


                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //130 second timeout to make sure we can get a response from the server within 120 seconds
        checkTransfer.setRetryPolicy(new DefaultRetryPolicy(130000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(checkTransfer);
    }


    public void complete_XRB_transfer(){
        sb = new StringBuilder();
//        String verifyUrl = url + "/" + token + "/" + "verify";
        String verifyUrl = sb.append(url).append(token).append("/verify").toString();

        StringRequest verifyTransfer = new StringRequest(Request.Method.GET, verifyUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);

                        dialogHelper.updateDialogSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(verifyTransfer);
    }


    public void convertToXRB(String currency, String amount, VolleyCallback vc){
        CurrencyConversion cc = new CurrencyConversion(context);

        cc.convertFiatToXRB(currency, amount, vc);
    }

    public void emptyRequestQueue(){
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
