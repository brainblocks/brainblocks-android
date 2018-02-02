package com.brainblocks.mok.android_brainblocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mok.brainblocks.Brainblock;
import com.mok.brainblocks.DialogHelper;

public class MainActivity extends AppCompatActivity {
    TextView payWithXRBText;
    Brainblock bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bb = Brainblock.getBrainBlock(this, "xrb_3anuobu4qk7nrbouax4jc4mx77ye3eun541kn85ceeho415zs4j7ty1uzsfp");

        payWithXRBText = findViewById(R.id.pay_with_xrb);
        payWithXRBText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bb.pay_with_XRB_start(1000);
            }
        });

//        DialogHelper dialogHelper = new DialogHelper(this);
//        dialogHelper.createDialog("awefe", "0.001");
//        dialogHelper.updateDialogFailed();
//        dialogHelper.updateDialogSuccess();
//        dialogHelper.updateDialogCancelled();

//        bb.convertToXRB("cad", "100", new VolleyCallback() {
//            @Override
//            public void onSuccess(String result) {
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//            }
//        });
    }

}
