package com.mok.brainblocks;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.SimpleColorFilter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 1/31/2018.
 */

public class DialogHelper {
    static FragmentManager fm;
    static Context context;
    static CheckoutDialog checkoutDialog;
    private static DialogHelper singleton;

    private DialogHelper(FragmentManager fm, Context context){
        this.fm = fm;
        this.context = context;
    }

    public static DialogHelper getDialogHelper(FragmentManager fm, Context context){
        if(singleton == null){
            singleton = new DialogHelper(fm, context);
        }

        return singleton;
    }

    public void createDialog(String address, String amount){
        checkoutDialog = CheckoutDialog.newInstance(address, amount);
        checkoutDialog.show(fm, "checkout-dialog");
    }

    public void updateDialogFailed(){
        checkoutDialog.dismiss();

        ResultDialog resultDialog = ResultDialog.newInstance(0);
        resultDialog.show(fm, null);
    }

    public void updateDialogSuccess(){
        checkoutDialog.dismiss();

        ResultDialog resultDialog = ResultDialog.newInstance(1);
        resultDialog.show(fm, null);
    }

    public static void updateDialogCancelled(){
        clearRequestQueue();

        checkoutDialog.dismiss();

        ResultDialog resultDialog = ResultDialog.newInstance(2);
        resultDialog.show(fm, null);
    }

    public static void clearRequestQueue(){
        Brainblock bb = Brainblock.getBrainBlock(context, "", fm);
        bb.queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void setFragmentManager(FragmentManager fm){
        this.fm = fm;
    }
}
