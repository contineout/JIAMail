package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ttett.Constants.ServletConstants;
import com.example.ttett.Entity.User;
import com.example.ttett.Service.UserService;
import com.example.ttett.util.HttpConnection;
import com.example.ttett.util.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
private Button BtnlLogin;
private EditText EtCout,EtPasswd;
private TextView TvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EtCout = findViewById(R.id.et_cout);
        EtPasswd = findViewById(R.id.et_passwd);
        EtPasswd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        BtnlLogin = findViewById(R.id.btn_login);
        TvRegister = findViewById(R.id.register);

        BtnlLogin.setOnClickListener(this);
        TvRegister.setOnClickListener(this);


    }


    private void Login(){
        if (EtCout.getText().toString().isEmpty()||EtPasswd.getText().toString().isEmpty()) {
            ToastUtil.showTextToas(LoginActivity.this,"学号或密码不能为空");
        }
        else{
            final User user = new User();
            user.setAccount(EtCout.getText().toString());
            user.setPassword(EtPasswd.getText().toString());

            String address = ServletConstants.LoginServlet;
            HttpConnection.sendOkHttpRequest(address, user, new okhttp3.Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    ToastUtil.showTextToas(LoginActivity.this,"连接失败");
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    System.out.println("响应信息： " + responseData);
                    Log.d("LoginActivity.this","no + " + responseData);
                    Looper.prepare();
                    User result = null;
                    if(responseData!=null){
                        if(!responseData.equals("null")) {
                            result = parseJSONWithGSON(responseData);
                            Log.d("LoginActivity.this","no + " + result.toString());
                            if(result !=null){
                                login(result);
                            }
                        }else{
                            ToastUtil.showTextToas(LoginActivity.this,"密码或用户名错误");
                        }
                    }else {
                        ToastUtil.showTextToas(LoginActivity.this,"无响应");
                    }
                    Looper.loop();
                }
            });
        }
    }

    private void login(User user){
        UserService userService = new UserService(this);
        boolean i = userService.SaveUser(user);
        Log.d("LoginActivity.this", i+"1");
        Intent intent = new Intent(this,MainActivity.class);
        int user_id = user.getUser_id();
        intent.putExtra("user_id",user_id);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                Login();
                break;
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this,RegisteredActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
