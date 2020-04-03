package com.example.ttett;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ttett.Entity.Contact;
import com.example.ttett.Service.ContactService;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.RegularUtil;
import com.example.ttett.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="ContactsActivity" ;
    private ImageView mExit,mSave;
    private EditText name,remark,birthday,company,department,position,email,iphone,address;
    private static String ContactInfo = "ContactInfo";
    private Contact contact = null;
    private ContactService contactService = new ContactService(this);

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
            ToastUtil.showTextToas(this,"JiaMail: 错误! 请填写联系人名称!");
        }else {
            contact.setName(isEmptyS(name));
            if(email.getText().toString().isEmpty()){
                ToastUtil.showTextToas(this,"JiaMail: 错误! 请填写联系人邮件地址!");
            }else{
                if(!RegularUtil.checkEmail(isEmptyS(email))){
                    ToastUtil.showTextToas(this,"JiaMail: 错误! 请检查邮箱地址格式!");
                }else{
                    contact.setEmail(email.getText().toString());
                    if(isEmptyS(iphone).equals("")){
                        contact.setIphone(isEmptyS(iphone));
                    }else {
                        if(!RegularUtil.checkIphoneNumber(iphone.getText().toString())){
                            ToastUtil.showTextToas(this,"JiaMail: 错误! 手机号码格式不对!");
                        }else{

                        };
                    }
                    contact.setRemark(isEmptyS(remark));
                    contact.setBirthday(isEmptyS(birthday));
                    contact.setCompany(isEmptyS(company));
                    contact.setDepartment(isEmptyS(department));
                    contact.setPosition(isEmptyS(position));
                    contact.setAddress(isEmptyS(address));
                    int email_id = getIntent().getIntExtra("email_id",0);
                    contact.setEmail_id(email_id);

                    boolean SaveResult = contactService.SaveContact(contact);
                    if(SaveResult){
                        EventBus.getDefault().post(new MessageEvent("add_contact",email_id));
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
