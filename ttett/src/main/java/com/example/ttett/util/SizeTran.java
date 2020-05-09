package com.example.ttett.util;

import android.annotation.SuppressLint;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SizeTran{
    private DecimalFormat df = new DecimalFormat("#.0");

    @SuppressLint("SetTextI18n")
    public void start(TextView textView, String size){
        double mSize = Integer.parseInt(size);
        int mb = 1024*1024;
        int kb = 1024;
        if(mSize < kb && mSize > 0){
            textView.setText(df.format(mSize) + " "+"B");
        }else if(mSize < mb && mSize > kb){
            textView.setText(df.format(mSize/ kb) + " "+"K");
        }else if(mSize > mb){
            textView.setText(df.format(mSize/ mb) + " "+"M");
        }
    }


}
