package com.example.ttett.Contact_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ttett.Entity.Contact;
import com.example.ttett.R;
import com.example.ttett.Service.EmailService;
import com.example.ttett.WriteLetterActivity;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.util.CircleTextImage.CircleTextImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;

public class SimpleContactActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name,email,remark,birthday,company,department,position,iphone,address;
    private Button send_message,edit_contact;
    private Contact contact;
    private EmailService emailService;
    private CircleTextImage icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_contact);
        EventBus.getDefault().register(this);

        Bundle bundle = getIntent().getExtras();
        contact = bundle.getParcelable("contact");
        name = findViewById(R.id.simple_contact_name);
        email = findViewById(R.id.simple_contact_email);
        remark = findViewById(R.id.tv_rmark);
        birthday = findViewById(R.id.tv_birthday);
        company = findViewById(R.id.tv_company);
        department = findViewById(R.id.tv_department);
        position = findViewById(R.id.tv_position);
        iphone = findViewById(R.id.tv_phone);
        address = findViewById(R.id.tv_address);
        icon = findViewById(R.id.contacts_Iv);

        send_message = findViewById(R.id.send_mail);
        edit_contact = findViewById(R.id.edit_contact);

        send_message.setOnClickListener(this);
        edit_contact.setOnClickListener(this);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
        icon.setText4CircleImage(contact.getName().substring(0,1));
        icon.setCircleColor(contact.getAvatar_color());
        setContactInfo();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        emailService = new EmailService(this);
        switch (v.getId()){
            case R.id.send_mail:
                intent = new Intent(SimpleContactActivity.this, WriteLetterActivity.class);
                intent.putExtra("Recipient_person",contact.getName());
                intent.putExtra("Recipient_email",contact.getEmail());
                intent.putExtra("email",emailService.queryEmail(contact.getEmail_id()));
                startActivityForResult(intent,1);
                break;
            case R.id.edit_contact:
                intent = new Intent(SimpleContactActivity.this, ContactsActivity.class);
                intent.putExtra("contact",contact);
                intent.putExtra("flag","update");
                startActivity(intent);
                break;
                default:
        }
    }

    public void setContactInfo(){
        if(contact.getRemark()!=null){
            remark.setText(contact.getRemark());
        }
        if(contact.getBirthday()!=null){
            birthday.setText(contact.getBirthday());
        }
        if(contact.getCompany()!=null){
            company.setText(contact.getCompany());
        }
        if(contact.getDepartment()!=null){
            department.setText(contact.getDepartment());
        }
        if(contact.getPosition()!=null){
            position.setText(contact.getPosition());
        }
        if(contact.getIphone()!=null){
            iphone.setText(contact.getIphone());
        }
        if(contact.getAddress()!=null){
            address.setText(contact.getAddress());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void updateContact(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("updateContact")){
            ContactService service = new ContactService(this);
            contact = service.queryContact(contact.getContact_id());
            setContactInfo();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
