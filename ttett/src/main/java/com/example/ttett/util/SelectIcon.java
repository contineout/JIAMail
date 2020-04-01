package com.example.ttett.util;

import android.widget.ImageView;

import com.example.ttett.R;

public class SelectIcon {
    public void Attachment(ImageView imageView,String type){
        switch (type){
            case "txt":
                imageView.setImageResource(R.mipmap.file_txt);
                break;
            case "xlsx":
                imageView.setImageResource(R.mipmap.file_excel_office);
                break;
            case "exe":
                imageView.setImageResource(R.mipmap.file_exe);
                break;
            case "doc":
            case "docx":
            case "rtf":
                imageView.setImageResource(R.mipmap.file_word_office);
                break;
            case "mp3":
                imageView.setImageResource(R.mipmap.file_music);
                break;
            case "pdf":
                imageView.setImageResource(R.mipmap.file_pdf);
                break;
            case "gif":
            case "png":
            case "jpg":
                imageView.setImageResource(R.mipmap.file_pic);
                break;
            case "rar":
            case "zip":
            case ".7":
                imageView.setImageResource(R.mipmap.file_zip);
                break;
            case "mp4":
            case "avi":
            case "flv":
            case "wmv":
            case "rmvb":
            case "mov":
                imageView.setImageResource(R.mipmap.file_video);
                break;
            default:
                imageView.setImageResource(R.mipmap.file_unknown);
                break;
        }
    }
}
