package com.example.ttett;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Service.ContactService;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="ContactsActivity" ;
    private ImageView mExit,mSave;
    private EditText name,remark,birthday,company,department,position,email,iphone,address;
    private static String ContactInfo = "ContactInfo";
    private Contact contact = null;
    private ContactService contactService = new ContactService(this);

    /**
     * 手机格式
     * @param value
     * @return
     */
    public boolean checkIphoneNumber(String value){
        if(value != null && value.length() == 11){
            String pattern = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
            boolean isMatch = Pattern.matches(pattern,value);
            return isMatch;
        }
        return false;
    }

    /**
     * 邮件格式
     * @param value
     * @return
     */
    public boolean checkEmail(String value){
        if(value != null){
            String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
            boolean isMatch = Pattern.matches(pattern,value);
            return isMatch;
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mExit = findViewById(R.id.cexit);
        mSave = findViewById(R.id.iv_save);
        name = findViewById(R.id.et_name);
        remark = findViewById(R.id.et_rmark);
        birthday = findViewById(R.id.et_birthday);
        company = findViewById(R.id.et_company);
        department = findViewById(R.id.et_department);
        position = findViewById(R.id.et_position);
        address = findViewById(R.id.et_address);
        email = findViewById(R.id.et_mail);
        iphone = findViewById(R.id.et_phone);

        if((contact =getIntent().getParcelableExtra("contact")) != null){
            name.setText(contact.getName());
            remark.setText(contact.getRemark());
            birthday.setText(contact.getBirthday());
            company.setText(contact.getCompany());
            department.setText(contact.getDepartment());
            position.setText(contact.getPosition());
            address.setText(contact.getAddress());
            email.setText(contact.getEmail());
            iphone.setText(contact.getIphone());
        }


        mExit.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_save:
                checkContact();
            break;
            case R.id.cexit:
            finish();
            break;
            default:
        }
    }

    /**
     * 联系人内容
     *
     */
    public void checkContact(){
        contact = new Contact();
        if(name.getText().toString().isEmpty()){
            Toast.makeText(ContactsActivity.this,"用户名为空",Toast.LENGTH_SHORT).show();
        }else {
            contact.setName(isEmptyS(name));
            if(email.getText().toString().isEmpty()){
                Toast.makeText(ContactsActivity.this,"邮箱地址为空",Toast.LENGTH_SHORT).show();
            }else{
                if(!checkEmail(isEmptyS(email))){
                    Toast.makeText(ContactsActivity.this,"邮箱地址格式不对",Toast.LENGTH_SHORT).show();
                }else{
                    contact.setEmail(email.getText().toString());
                    if(isEmptyS(iphone).equals("")){
                        contact.setIphone(isEmptyS(iphone));
                    }else {
                        if(!checkIphoneNumber(iphone.getText().toString())){
                            Toast.makeText(ContactsActivity.this,"手机号码格式不对",Toast.LENGTH_SHORT).show();
                        }else{

                        };
                    }
                    contact.setRemark(isEmptyS(remark));
                    contact.setBirthday(isEmptyS(birthday));
                    contact.setCompany(isEmptyS(company));
                    contact.setDepartment(isEmptyS(department));
                    contact.setPosition(isEmptyS(position));
                    contact.setAddress(isEmptyS(address));
                    int user_id = getIntent().getIntExtra("user_id",0);
                    contact.setUser_id(user_id);
                    Log.d(TAG,"dd"+user_id);
                    Log.d(TAG,"dd"+contact.getIphone()+contact.getEmail()+contact.getName()+contact.getBirthday()+contact.getUser_id());

                    boolean SaveResult = contactService.SaveContact(contact);
                    if(SaveResult){
                        finish();
                    }else {
                        Toast.makeText(ContactsActivity.this,"该email已经存在联系人",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public String isEmptyS(EditText editText){
        if(!editText.getText().toString().isEmpty()){
            return editText.getText().toString();
        }
        return "";
    }
}
