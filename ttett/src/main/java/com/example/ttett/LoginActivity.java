package com.example.ttett;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
private Button BtnlLogin;
private EditText EtCout,EtPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EtCout = findViewById(R.id.et_cout);
        EtPasswd = findViewById(R.id.et_passwd);

        BtnlLogin = findViewById(R.id.btn_login);
        BtnlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login(){

        String Cout = EtCout.getText().toString();
        String Passwd = EtPasswd.getText().toString();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);


    }
}
