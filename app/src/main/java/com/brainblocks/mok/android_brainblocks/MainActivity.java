package com.brainblocks.mok.android_brainblocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mok.brainblocks.Brainblock;
import com.mok.brainblocks.DialogHelper;
import com.mok.brainblocks.ResultDialog;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    TextView payWithXRBText;
    Brainblock bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bb = Brainblock.getBrainBlock(this, "xrb_3anuobu4qk7nrbouax4jc4mx77ye3eun541kn85ceeho415zs4j7ty1uzsfp", getFragmentManager());

        payWithXRBText = findViewById(R.id.pay_with_xrb);
        payWithXRBText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bb.pay_with_XRB_start(1000);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if(bb != null){
            bb.setFragmentManager(getFragmentManager());
        }
    }

}
