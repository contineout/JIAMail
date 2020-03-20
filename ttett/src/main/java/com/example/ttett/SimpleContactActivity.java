package com.example.ttett;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ttett.Entity.Contact;
import com.example.ttett.fragment.WriteLetterActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SimpleContactActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name,email;
    private Button send_message,edit_contact,delete_contact;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_contact);

        Bundle bundle = getIntent().getExtras();
        contact = bundle.getParcelable("contact");
        name = findViewById(R.id.simple_contact_name);
        email = findViewById(R.id.simple_contact_email);
        send_message = findViewById(R.id.send_mail);
        edit_contact = findViewById(R.id.edit_contact);
        delete_contact =findViewById(R.id.delete_contact);

        send_message.setOnClickListener(this);
        edit_contact.setOnClickListener(this);
        delete_contact.setOnClickListener(this);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.send_mail:
                intent = new Intent(SimpleContactActivity.this, WriteLetterActivity.class);
                intent.putExtra("Recipient_person",contact.getName());
                intent.putExtra("Recipient_email",contact.getEmail());
                startActivity(intent);
                break;
            case R.id.edit_contact:
                intent = new Intent(SimpleContactActivity.this,ContactsActivity.class);
                intent.putExtra("contact",contact);
                startActivity(intent);
                break;
            case R.id.delete_contact:
                break;
                default:
        }
    }
}
