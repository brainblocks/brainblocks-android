package com.mok.brainblocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 2/5/2018.
 */

public class CheckoutDialog extends DialogFragment{
    AlertDialog dialog;

    public CheckoutDialog(){

    }

    public static CheckoutDialog newInstance(String address, String amount){
        CheckoutDialog cd = new CheckoutDialog();

        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("amount", amount);
        cd.setArguments(args);

        return cd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String address = getArguments().getString("address");
        String amount = getArguments().getString("amount");
        StringBuilder sb = new StringBuilder();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        dialog = builder.create();

        //create QR Code for user to scan
        ImageView QRCodeView = (ImageView) view.findViewById(R.id.qr_code);
        Bitmap QRCodeBitmap = TextToQRCode(address);
        if(QRCodeBitmap != null){
            QRCodeView.setImageBitmap(QRCodeBitmap);
        } else {
            QRCodeView.setVisibility(View.GONE);
        }

        QRCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, address);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getActivity().getApplicationContext(), "Address copied to clipboard.", Toast.LENGTH_LONG).show();
            }
        });

        DecimalFormat formatter = new DecimalFormat("0");
        formatter.setMaximumFractionDigits(8);
        double NanoAmount = Double.parseDouble(amount) / 1000000;

        TextView paymentAmountText = view.findViewById(R.id.payment_amount);
        paymentAmountText.setText(sb.append("Pay ").append(formatter.format(NanoAmount)).append(" Nano").toString());

        TextView addressText = view.findViewById(R.id.payment_destination);
        addressText.setText(address);

        TextView cancelButton = view.findViewById(R.id.cancel_payment_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.updateDialogCancelled();
            }
        });

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

        return dialog;
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

    //prevents dialog from disappearing since we are retaining instance
    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
