package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ttett.Entity.Email;
import com.example.ttett.Service.EmailService;
import com.example.ttett.util.EmailLogin;

import androidx.appcompat.app.AppCompatActivity;

public class LoginEmailActivity extends AppCompatActivity {
    private static final String TAG = "LoginEmailActivity";
    private EditText et_address,et_password;
    private Button bt_login_email;
    public static int RESULT_CODE = 2;
    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        et_address = findViewById(R.id.login_email_account);
        et_password = findViewById(R.id.login_email_password);
        bt_login_email = findViewById(R.id.btn_login_email);

        bt_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EmailConnection(et_address.getText().toString(),et_password.getText().toString());
            }
        });
    }

    private void EmailConnection(final String address, final String password){
        if(password.isEmpty()){
            Toast.makeText(LoginEmailActivity.this,"请输入邮箱密码",Toast.LENGTH_SHORT).show();
        }else{
            if(address.isEmpty()){
                Toast.makeText(LoginEmailActivity.this,"请输入邮箱地址",Toast.LENGTH_SHORT).show();
            }else{
                final EmailService emailService = new EmailService(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final boolean isConnection = selectLogin(address,password);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(isConnection){
                                    Toast.makeText(LoginEmailActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                                    emailService.SaveEmail(email);
                                    Intent intent = new Intent();
                                    intent.putExtra("result",isConnection);
                                    intent.putExtra("address",address);
                                    setResult(RESULT_CODE,intent);
                                    finish();
                                }else {
                                    Toast.makeText(LoginEmailActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private boolean selectLogin(String address,String password){
        EmailLogin emailLogin = new EmailLogin();

        String[] email_type = address.split("[@]");
        email = new Email();
        email.setAddress(address);
        email.setName(email_type[0]);
        email.setType(email_type[1]);
        email.setAuthorizationCode(password);
        email.setUser_id(getIntent().getIntExtra("user_id",0));
        switch (email_type[1]){
            case "qq.com":
                return emailLogin.QQLogin(email);
            case "sina.com":
                return emailLogin.SinaLogin(email);
                default:
        }
        return false;
    }


}
