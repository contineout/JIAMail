package com.example.ttett.fragment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ttett.R;

public class WriteLetterActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView IvXdelete,IvSend,IvTrainglemore,IvTO,IvCC,IvBCC;
    private EditText EtTO,EtCC,EtBCC,EtSubject;
    private RelativeLayout RLCC,RLBCC;
    private View VCC,VBCC;
    private boolean show = true;
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_letter);

        IvXdelete = findViewById(R.id.xdelete);
        IvSend = findViewById(R.id.iv_send1);
        IvTrainglemore = findViewById(R.id.traingle_more);
        IvTO = findViewById(R.id.iv_to);
        IvCC = findViewById(R.id.iv_cc);
        IvBCC = findViewById(R.id.iv_bcc);

        EtTO = findViewById(R.id.et_to);
        EtCC = findViewById(R.id.et_cc);
        EtBCC = findViewById(R.id.et_bcc);
        EtSubject = findViewById(R.id.et_subject);

        RLCC = findViewById(R.id.RL_cc);
        RLBCC = findViewById(R.id.RL_bcc);

        VCC = findViewById(R.id.Vcc);
        VBCC = findViewById(R.id.Vbcc);

        IvXdelete.setOnClickListener(this);
        IvSend.setOnClickListener(this);
        IvTrainglemore.setOnClickListener(this);
        IvTO.setOnClickListener(this);
        IvCC.setOnClickListener(this);
        IvBCC.setOnClickListener(this);

        IvTrainglemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show) {
                    RLCC.setVisibility(View.VISIBLE);
                    RLBCC.setVisibility(View.VISIBLE);
                    VCC.setVisibility(View.VISIBLE);
                    VBCC.setVisibility(View.VISIBLE);
                    IvTrainglemore.setImageResource(R.mipmap.traingle_down);
                    show = false;
                }else {
                    IvTrainglemore.setImageResource(R.mipmap.traingle_right);
                    RLCC.setVisibility(View.GONE);
                    RLBCC.setVisibility(View.GONE);
                    VCC.setVisibility(View.GONE);
                    VBCC.setVisibility(View.GONE);
                    show = true;
                }
            }
        });
    }

    private void RLVisibility(boolean show){


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xdelete:
                finish();
                break;
            case R.id.iv_send1:
                Toast.makeText(this,"发送",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_to:
                Toast.makeText(this,"发送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_cc:
                Toast.makeText(this,"抄送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_bcc:
                Toast.makeText(this,"密送人",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }
}
