package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ttett.Entity.Email;
import com.example.ttett.Service.EmailService;
import com.example.ttett.util.CircleTextImage.CircleTextImageUtil;
import com.example.ttett.util.ToastUtil;
import com.example.ttett.util.mailUtil.EmailProp;

import javax.mail.Session;
import javax.mail.Transport;

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
        et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
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
            ToastUtil.showTextToas(this,"请输入邮箱密码");
        }else{
            if(address.isEmpty()){
                ToastUtil.showTextToas(this,"请输入邮箱地址");
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
                                    ToastUtil.showTextToas(LoginEmailActivity.this,"连接成功");
                                    emailService.SaveEmail(email);
                                    Intent intent = new Intent();
                                    intent.putExtra("result",isConnection);
                                    intent.putExtra("address",address);
                                    setResult(RESULT_CODE,intent);
                                    finish();
                                }else {
                                    ToastUtil.showTextToas(LoginEmailActivity.this,"连接失败");
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private boolean selectLogin(String address,String password){
        EmailProp emailProp = new EmailProp();
        boolean isConnected = false;
        String[] email_type = address.split("[@]");
        email = new Email();
        email.setAddress(address);
        email.setName(email_type[0]);
        email.setType(email_type[1]);
        email.setAuthorizationCode(password);
        email.setAvatar_color(CircleTextImageUtil.getRandomColor());
        email.setUser_id(getIntent().getIntExtra("user_id",0));
        email.setMessage_count(0);
        Session session = emailProp.getSTMPSession(email);
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect(email.getAddress(),email.getAuthorizationCode());
            isConnected = transport.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return isConnected;
        }
        return isConnected;
    }
}
