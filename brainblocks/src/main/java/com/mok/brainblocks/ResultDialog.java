package com.mok.brainblocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.SimpleColorFilter;

/**
 * Created by Alexander on 2/5/2018.
 */

public class ResultDialog extends DialogFragment {
    AlertDialog dialog;

    public ResultDialog(){

    }

    public static ResultDialog newInstance(int resultCode){
        ResultDialog rd = new ResultDialog();

        Bundle args = new Bundle();
        args.putInt("resultCode", resultCode);
        rd.setArguments(args);

        return rd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int resultCode = getArguments().getInt("resultCode");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.post_payment_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.lottie_animation_view);
        TextView paymentOutcome = view.findViewById(R.id.payment_outcome);
        TextView thankYouText = view.findViewById(R.id.thank_you_text);
        TextView okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        switch(resultCode) {
            case 0: //time out
                animationView.setAnimation("warning.json");
                animationView.loop(false);
                animationView.setScale(0.15f);

                paymentOutcome.setText("Payment Failed");

                thankYouText.setText("Time exceeded for this payment session. Please try again.");
                break;

            case 1: //success
                animationView.setAnimation("Check-Mark-Success.json");
                animationView.loop(false);
                animationView.setScale(1.0f);

                paymentOutcome.setText("Payment Success");

                thankYouText.setText("Thank you for using BrainBlocks!");
                break;

            case 2: //cancelled
                animationView.setAnimation("error.json");
                animationView.loop(false);
                animationView.setScale(0.4f);
                PorterDuffColorFilter colorFilter = new SimpleColorFilter(Color.RED);
                animationView.addColorFilter(colorFilter);

                paymentOutcome.setText("Payment Cancelled");

                thankYouText.setText("Payment has been cancelled.");
                break;
        }

        builder.setView(view);
        dialog = builder.create();

        animationView.playAnimation();

        return dialog;
    }
}
