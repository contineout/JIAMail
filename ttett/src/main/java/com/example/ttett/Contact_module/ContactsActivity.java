package com.example.ttett.Contact_module;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.CircleTextImage.CircleTextImage;
import com.example.ttett.util.CircleTextImage.CircleTextImageUtil;
import com.example.ttett.util.RegularUtil;
import com.example.ttett.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="ContactsActivity" ;
    private ImageView mExit,mSave;
    private EditText name,remark,birthday,company,department,position,email,iphone,address;
    private CircleTextImage icon;
    private static String ContactInfo = "ContactInfo";
    private Contact contact = null;
    private ContactService contactService = new ContactService(this);
    private boolean isUpdate = false;
    private TextView flagText;

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
        flagText = findViewById(R.id.flagtext);
        icon = findViewById(R.id.iv_avatar);

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
            icon.setText4CircleImage(contact.getName());
            icon.setCircleColor(contact.getAvatar_color());
        }
        try{
            if(Objects.equals(getIntent().getStringExtra("flag"), "update")){
                isUpdate = true;
                flagText.setText("");
            }
        }catch (Exception ignored){
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
        if(contact == null){
            contact = new Contact();
        }
        if(name.getText().toString().isEmpty()){
            ToastUtil.showTextToas(this,"错误! 请填写联系人名称!");
        }else {
            contact.setName(isEmptyS(name));
            if(email.getText().toString().isEmpty()){
                ToastUtil.showTextToas(this,"错误! 请填写联系人邮件地址!");
            }else{
                if(!RegularUtil.checkEmail(isEmptyS(email))){
                    ToastUtil.showTextToas(this,"错误! 请检查邮箱地址格式!");
                }else{
                    contact.setEmail(email.getText().toString());
                    if(isEmptyS(iphone).equals("")){
                        contact.setIphone(isEmptyS(iphone));
                    }else {
                        if(!RegularUtil.checkIphoneNumber(iphone.getText().toString())){
                            ToastUtil.showTextToas(this,"错误! 手机号码格式不对!");
                        }else{
                            contact.setIphone(isEmptyS(iphone));
                        }
                        contact.setRemark(isEmptyS(remark));
                        contact.setBirthday(isEmptyS(birthday));
                        contact.setCompany(isEmptyS(company));
                        contact.setDepartment(isEmptyS(department));
                        contact.setPosition(isEmptyS(position));
                        contact.setAddress(isEmptyS(address));
                        contact.setIphone(isEmptyS(iphone));
                        contact.setAvatar_color(CircleTextImageUtil.getRandomColor());
                        int email_id = getIntent().getIntExtra("email_id",0);
                        contact.setEmail_id(email_id);

                        if(isUpdate){
                            contactService.updateContact(contact);
                            ToastUtil.showTextToas(this,"修改成功");
                            EventBus.getDefault().postSticky(new MessageEvent("updateContact",email_id));
                            finish();
                        }else {
                            boolean SaveResult = contactService.SaveContact(contact,email_id);
                            if(SaveResult){
                                EventBus.getDefault().postSticky(new MessageEvent("new_contact",email_id));
                                finish();
                            }else {
                                ToastUtil.showTextToas(this,"该email已经存在联系人!");
                            }
                        }
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
