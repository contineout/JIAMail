package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ttett.Entity.User;
import com.example.ttett.util.HttpConnection;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
private Button BtnlLogin;
private EditText EtCout,EtPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();


    }
    private void updateUIThread(Message msg){
        Bundle bundle = msg.getData();
        String result = bundle.getString("result");
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void InitView(){
        EtCout = findViewById(R.id.et_cout);
        EtPasswd = findViewById(R.id.et_passwd);
        BtnlLogin = findViewById(R.id.btn_login);

        BtnlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (EtCout.getText().toString().isEmpty()||EtPasswd.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "学号或密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        //开启访问数据库线程
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                                final User user = new User();
                                user.setAccount(EtCout.getText().toString());
                                user.setPassword(EtPasswd.getText().toString());

                                String address = "http://192.168.1.12:8888/MailServlet/UserServlet";
                                HttpConnection.sendOkHttpRequest(address, user, new okhttp3.Callback(){
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseData = response.body().string();
                                        System.out.println("响应信息： " + responseData);
                                        Log.d("LoginActivity.this","no + " + responseData);
                                        Looper.prepare();
                                        User result = null;
                                        if(response != null) {
                                            result = parseJSONWithGSON(responseData);
                                        }else{}

                                        if(result !=null){
                                            login(result);
                                        }else{
                                            Toast.makeText(LoginActivity.this, "密码或用户名错误", Toast.LENGTH_SHORT).show();
                                        }
                                        Looper.loop();
                                    }
                                });

//                            }
//                        }).start();
                    }
            }
        });
    }

    private void login(User user){
        Intent intent = new Intent(this,MainActivity.class);
        int user_id = user.getUser_id();
        Bundle bundle = new Bundle();
        bundle.putInt("id",user_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public User parseJSONWithGSON(String jsonData){
        if(jsonData!=null) {
            Gson gson = new Gson();
            User user = gson.fromJson(jsonData, User.class);
            Log.d("LoginActivity.this", "no + ");
            return user;
        }else {
            return null;
        }
    }

}
