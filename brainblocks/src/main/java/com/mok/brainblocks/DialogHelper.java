package com.mok.brainblocks;

import android.app.AlertDialog;
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
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 1/31/2018.
 */

public class DialogHelper {
    Context context;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LayoutInflater inflater;
    View view;
    StringBuilder sb;

    public DialogHelper(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        builder = new AlertDialog.Builder(context);
    }


    public void createDialog(String address, String amount){
        view = inflater.inflate(R.layout.layout, null);
        builder.setView(view);
        builder.setNegativeButton("Cancel Payment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateDialogCancelled();
            }
        });

        dialog = builder.create();

        //create QR Code for user to scan
        ImageView QRCodeView = (ImageView) view.findViewById(R.id.qr_code);
        Bitmap QRCodeBitmap = TextToQRCode(address);
        if(QRCodeBitmap != null){
            QRCodeView.setImageBitmap(QRCodeBitmap);
        } else {
            QRCodeView.setVisibility(View.GONE);
        }

        sb = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("0");
        formatter.setMaximumFractionDigits(8);
        double NanoAmount = Double.parseDouble(amount) / 1000000;

        TextView paymentAmountText = view.findViewById(R.id.payment_amount);
        paymentAmountText.setText(sb.append("Pay ").append(formatter.format(NanoAmount)).append(" Nano").toString());

        TextView addressText = view.findViewById(R.id.payment_destination);
        addressText.setText(address);

        //handler to update the 120s timer and progress bar in the dialog
        final Handler h = new Handler();

        final ProgressBar timerProgress = view.findViewById(R.id.timer_progress);
        final TextView timerText = view.findViewById(R.id.timer_text);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                int progress = timerProgress.getProgress();
                timerProgress.setProgress(progress + 1);

                int timeRemaining = 120 - progress - 1;

                timerText.setText(String.valueOf(timeRemaining) + " seconds remaining");

                if(timeRemaining > 0){
                    h.postDelayed(this, 1000);
                }
            }
        };

        h.postDelayed(r, 1000);

        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        negativeButton.setLayoutParams(negativeButtonLL);
    }

    public Bitmap TextToQRCode(String s) {
        try{
            BitMatrix bitMatrix = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, 400, 400, null);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateDialogFailed(){
        dialog.dismiss();

        view = inflater.inflate(R.layout.post_payment_dialog_layout, null);

        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.lottie_animation_view);
        animationView.setAnimation("warning.json");
        animationView.loop(false);
        animationView.setScale(0.15f);

        TextView paymentOutcome = view.findViewById(R.id.payment_outcome);
        paymentOutcome.setText("Payment Failed");

        TextView thankYouText = view.findViewById(R.id.thank_you_text);
        thankYouText.setText("Time exceeded for this payment session. Please try again.");

        builder.setView(view);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        negativeButton.setLayoutParams(negativeButtonLL);

        animationView.playAnimation();
    }

    public void updateDialogSuccess(){
        dialog.dismiss();

        view = inflater.inflate(R.layout.post_payment_dialog_layout, null);

        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.lottie_animation_view);
        animationView.setAnimation("Check-Mark-Success.json");
        animationView.loop(false);
        animationView.setScale(1.0f);

        builder.setView(view);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        negativeButton.setLayoutParams(negativeButtonLL);

        animationView.playAnimation();
    }

    public void updateDialogCancelled(){
        clearRequestQueue();

        dialog.dismiss();

        view = inflater.inflate(R.layout.post_payment_dialog_layout, null);

        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.lottie_animation_view);
        animationView.setAnimation("error.json");
        animationView.loop(false);
        animationView.setScale(0.4f);

        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
        colorFilter = new SimpleColorFilter(Color.RED);
        animationView.addColorFilter(colorFilter);

        TextView paymentOutcome = view.findViewById(R.id.payment_outcome);
        paymentOutcome.setText("Payment Cancelled");

        TextView thankYouText = view.findViewById(R.id.thank_you_text);
        thankYouText.setText("Payment has been cancelled.");

        builder.setView(view);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        negativeButton.setLayoutParams(negativeButtonLL);

        animationView.playAnimation();
    }

    public void clearRequestQueue(){
        Brainblock bb = Brainblock.getBrainBlock(context, "");
        bb.queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
