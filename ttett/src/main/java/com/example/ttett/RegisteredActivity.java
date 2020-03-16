package com.example.ttett;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Constants.ServletConstants;
import com.example.ttett.Entity.User;
import com.example.ttett.util.HttpConnection;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Response;

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener {
private ImageView mRegister_Exit;
private TextView mLogin_Exit;
private EditText mRegister_account,mRegister_password;
private Button BtnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        mRegister_Exit = findViewById(R.id.register_exit);
        mLogin_Exit = findViewById(R.id.login_exit);
        mRegister_account = findViewById(R.id.register_account);
        mRegister_password = findViewById(R.id.register_password);
        BtnRegister = findViewById(R.id.btn_register);

        mLogin_Exit.setOnClickListener(this);
        mRegister_Exit.setOnClickListener(this);
        BtnRegister.setOnClickListener(this);

    }


    public void register() {
        if (mRegister_account.getText().toString().isEmpty() || mRegister_password.getText().toString().isEmpty()) {
            Toast.makeText(RegisteredActivity.this, "学号或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
//                        //开启访问数据库线程
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {

            final User user = new User();
            user.setAccount(mRegister_account.getText().toString());
            user.setPassword(mRegister_password.getText().toString());

            String address = ServletConstants.RegisterServlet;
            HttpConnection.sendOkHttpRequest(address, user, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(RegisteredActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    System.out.println("响应信息： " + responseData);
                    Log.d("LoginActivity.this", "no + " + responseData);
                    Looper.prepare();
                    User result = null;
                    if (response != null) {
                        result = parseJSONWithGSON(responseData);
                    } else {
                        Toast.makeText(RegisteredActivity.this, "响应出错", Toast.LENGTH_SHORT).show();
                    }
                    if (result != null) {
                        if(result.isRegisterresult()){
                            Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            RegisteredActivity.this.finish();
                        }else{
                            Toast.makeText(RegisteredActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(RegisteredActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            });
        }
    }

    public User parseJSONWithGSON (String jsonData){
        if (jsonData != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(jsonData, User.class);
            Log.d("LoginActivity.this", "no + ");
            return user;
        } else {
            return null;
        }
    }
    @Override
    public void onClick (View v){
        switch (v.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.register_exit:
            case R.id.login_exit:
                RegisteredActivity.this.finish();
                break;
            default:
        }
    }
}
