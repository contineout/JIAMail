package com.example.ttett;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Adapter.WriteAttachmentAdapter;
import com.example.ttett.CustomDialog.SaveMessageDialogFragment;
import com.example.ttett.Dao.MailDao;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.AttachmentService;
import com.example.ttett.util.SendMessage;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class WriteLetterActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView IvXdelete,IvSend,IvTrainglemore,IvTO,IvCC,IvBCC;
    private EditText EtTO,EtCC,EtBCC,EtSubject,EtContent;
    private RelativeLayout RLCC,RLBCC;
    private View VCC,VBCC;
    private boolean show = true;
    private TextView TvSendEmail;
    private Email email;
    private EmailMessage emailMessage;
    private SendMessage sendMessage;
    private MailDao mailDao;
    private SaveMessageDialogFragment saveMessageDialogFragment;
    private String initContent = "Sent from JiaMail";
    private String TAG = "WriteLetterActivity";
    private String Recipient_email;
    private RecyclerView writeLtter_Rv;
    private FloatingActionsMenu fab;
    private FloatingActionButton fab_attachment,fab_pic,fab_timer;
    private List<Integer> id_item = new ArrayList<>();;
    private List<Attachment> attachments;
    private WriteAttachmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_letter);
        initView();
        IvTrainglemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show) {
                    RLCC.setVisibility(View.VISIBLE);
                    RLBCC.setVisibility(View.VISIBLE);
                    VCC.setVisibility(View.VISIBLE);
                    VBCC.setVisibility(View.VISIBLE);
                    IvTrainglemore.setImageResource(R.mipmap.traingle_down);
                    show = false;
                }else {
                    IvTrainglemore.setImageResource(R.mipmap.traingle_right);
                    RLCC.setVisibility(View.GONE);
                    RLBCC.setVisibility(View.GONE);
                    VCC.setVisibility(View.GONE);
                    VBCC.setVisibility(View.GONE);
                    show = true;
                }
            }
        });


    }

    private void initView(){
        IvXdelete = findViewById(R.id.xdelete);
        IvSend = findViewById(R.id.iv_send1);
        IvTrainglemore = findViewById(R.id.traingle_more);
        IvTO = findViewById(R.id.iv_to);
        IvCC = findViewById(R.id.iv_cc);
        IvBCC = findViewById(R.id.iv_bcc);
        writeLtter_Rv = findViewById(R.id.write_rv);

        EtTO = findViewById(R.id.et_to);
        EtCC = findViewById(R.id.et_cc);
        EtBCC = findViewById(R.id.et_bcc);
        EtSubject = findViewById(R.id.et_subject);
        EtContent = findViewById(R.id.et_content);

        RLCC = findViewById(R.id.RL_cc);
        RLBCC = findViewById(R.id.RL_bcc);

        VCC = findViewById(R.id.Vcc);
        VBCC = findViewById(R.id.Vbcc);
        TvSendEmail = findViewById(R.id.tv_send_email);
        fab = findViewById(R.id.fab_menu);
        fab_attachment = findViewById(R.id.fab_attachment);
        fab_pic = findViewById(R.id.fab_pic);
        fab_timer = findViewById(R.id.fab_timer);

        try{
            email = getIntent().getParcelableExtra("email");
            Log.d(TAG,"email.address = "+ email.getAddress());
            TvSendEmail.setText(email.getAddress());
            TvSendEmail.setVisibility(View.VISIBLE);
        }catch (Exception e){
        }

        try{
            Recipient_email = getIntent().getStringExtra("Recipient_email");
            email = getIntent().getParcelableExtra("email");
            EtTO.setText(Recipient_email);
            TvSendEmail.setText(email.getAddress());
        }catch (Exception e){
        }

        EtContent.setText(initContent);

        IvXdelete.setOnClickListener(this);
        IvSend.setOnClickListener(this);
        IvTrainglemore.setOnClickListener(this);
        IvTO.setOnClickListener(this);
        IvCC.setOnClickListener(this);
        IvBCC.setOnClickListener(this);
        fab_attachment.setOnClickListener(this);
        fab_pic.setOnClickListener(this);
        fab_timer.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xdelete:
                if(!isEmptyS(EtContent).equals(initContent)){
                    showDialog();
                }else {
                    finish();
                }
                break;
            case R.id.iv_send1:
                CheckMail();
                break;
            case R.id.iv_to:
                Toast.makeText(this,"发送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_cc:
                Toast.makeText(this,"抄送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_bcc:
                Toast.makeText(this,"密送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_attachment:
                Intent intent = new Intent(this,SelectAttachmentActivity.class);
                intent.putExtra("email_id",email.getEmail_id());
                startActivityForResult(intent,1);
                break;
            case R.id.fab_pic:
                Toast.makeText(this,"抄送人",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_timer:
                Toast.makeText(this,"密送人",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }


    /**
     * 检查必要属性
     */
    public void CheckMail(){
        if(isEmptyS(EtTO).isEmpty()){
            Toast.makeText(this,"JiaMail:请输入收件人",Toast.LENGTH_SHORT).show();
        }else{
            if(isEmptyS(EtSubject).isEmpty()){
                Toast.makeText(this,"JiaMail:请输入主题",Toast.LENGTH_SHORT).show();
            }else{
                setEmailMessage();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendMessage();
                    }
                }).start();
            }
        }
    }

    /**
     * 发送邮件，选择发送邮箱类型
     */
    private void SendMessage(){
        AttachmentService attachmentService = new AttachmentService(this);
        List<Attachment> attachments = attachmentService.querySelectAttachment(id_item);
        sendMessage = new SendMessage(emailMessage,email,attachments);
        switch (email.getType()){
            case "qq.com":
                sendMessage.QQSend();
                saveMessage();
                finish();
                break;
            case  "sina.com":
                sendMessage.SinaSend();
                saveMessage();
                finish();
                break;
            default:
        }
    }

    /**
     * 保存到数据库，在已发送中
     */
    private void saveMessage(){
        Date date;
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str;
        mailDao = new MailDao(this);
        String from_mail = "wu"+"<"+ email.getAddress() + ">";
        emailMessage.setFrom(from_mail);
        emailMessage.setIsSend(1);
        date = new Date();
        str = format.format(date);
        emailMessage.setSendDate(str);
        mailDao.InsertMessages(emailMessage);
    }

    /**
     * 显示对话框是否保存到草稿箱
     */
    private void showDialog(){
        saveMessageDialogFragment = new SaveMessageDialogFragment();
        setEmailMessage();
        emailMessage.setIsSend(0);
        Bundle bundle = new Bundle();
        bundle.putParcelable("message",emailMessage);
        saveMessageDialogFragment.setArguments(bundle);
//        contactsDialogFragment.setTargetFragment(ContactsFragment.this,REQUEST_CODE);
        saveMessageDialogFragment.show(getSupportFragmentManager(),"saveMessageDialogFragment");

    }


    /**
     * set属性到message中
     */
    private void setEmailMessage(){
        emailMessage = new EmailMessage();
        if(email!=null){
            emailMessage.setFrom(email.getAddress());
            emailMessage.setUser_id(email.getUser_id());
            emailMessage.setEmail_id(email.getEmail_id());
        }
        emailMessage.setTo(isEmptyS(EtTO));
        emailMessage.setSubject(isEmptyS(EtSubject));
        emailMessage.setCc(isEmptyS(EtCC));
        emailMessage.setBcc(isEmptyS(EtBCC));
        emailMessage.setContent(isEmptyS(EtContent));
        emailMessage.setIsAttachment(1);
        StringBuilder attachments = new StringBuilder("&&");
        for(int id:id_item){
            attachments.append(String.valueOf(id)).append("&&");
        }
        emailMessage.setAttachment(attachments.toString());

    }

    /**
     * 为空返回"",否则返回值
     * @param editText
     * @return
     */
    public String isEmptyS(EditText editText){
        if(!editText.getText().toString().isEmpty()){
            return editText.getText().toString();
        }
        return "";
    }

    /**
     * 选择附件返回值，刷新附件Rv,去除选择重复
      * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;

                List<Integer> select_id_item = data.getIntegerArrayListExtra("id_item");
                List<Integer> new_id_item =null;
                if(select_id_item!=null){
                    id_item.addAll(select_id_item);
                     new_id_item = new ArrayList<>();
                    for (int id : id_item) {
                        if(!new_id_item.contains(id)){
                            new_id_item.add(id);
                        }
                    }
                }
                if(new_id_item!=null){
                    AttachmentService attachmentService = new AttachmentService(this);
                    attachments = attachmentService.querySelectAttachment(new_id_item);
                    if(attachments != null) {
                        StaggeredGridLayoutManager layoutManager = new
                                StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
                        writeLtter_Rv.setLayoutManager(layoutManager);
                        adapter = new WriteAttachmentAdapter(this,attachments);
                        writeLtter_Rv.setAdapter(adapter);
                    }
                }
            }
        }
    }
}
