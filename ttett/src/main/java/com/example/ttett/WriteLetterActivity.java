package com.example.ttett;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttett.Adapter.WriteAttachmentAdapter;
import com.example.ttett.Contact_module.ContactDao;
import com.example.ttett.Contact_module.ContactService;
import com.example.ttett.CustomDialog.SaveMessageDialogFragment;
import com.example.ttett.Entity.Attachment;
import com.example.ttett.Entity.Email;
import com.example.ttett.Entity.EmailMessage;
import com.example.ttett.Service.AttachmentService;
import com.example.ttett.bean.MessageEvent;
import com.example.ttett.bean.Person;
import com.example.ttett.selectAcitvity.SelectAttachmentActivity;
import com.example.ttett.selectAcitvity.SelectContactActivity;
import com.example.ttett.util.ToastUtil;
import com.example.ttett.util.mailUtil.SendMessage;
import com.example.ttett.util.token.ContactsCompletionView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.tokenautocomplete.FilteredArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.qzb.richeditor.RichEditor;

public class WriteLetterActivity extends BaseActivity implements View.OnClickListener{
    private ImageView IvXdelete,IvSend,IvTrainglemore,IvTO,IvCC,IvBCC;
    private EditText EtSubject;
    private RichEditor EtContent;
    private ContactsCompletionView completionViewTO;
    private ContactsCompletionView completionViewBCC;
    private ContactsCompletionView completionViewCC;
    private String TO = "",BCC = "",CC = "";
    ArrayAdapter<Person> adapter;
    private RelativeLayout RLCC,RLBCC;
    private View VCC,VBCC;
    private boolean show = true;
    private Email email;
    private EmailMessage emailMessage;
    private String initContent = "Sent from JiaMail";
    private String TAG = "WriteLetterActivity";
    private RecyclerView writeLtter_Rv;
    private FloatingActionsMenu fab;
    private FloatingActionButton fab_attachment,fab_pic,fab_timer;
    private List<Integer> id_item = new ArrayList<>();
    private List<Attachment> attachments = null;
    private TextView tvSendEmail;
    private List<Person> people;
    private int email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_letter);
        EventBus.getDefault().register(this);
        email = getIntent().getParcelableExtra("email");
        emailMessage = getIntent().getParcelableExtra("emailMessage");
        if(email!=null){
            email_id = email.getEmail_id();
        } else if (emailMessage != null) {
            email_id = emailMessage.getEmail_id();
        }

        initView();
        entrance();
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void addSendContact(MessageEvent event){
        ContactService service = new ContactService(this);
        if(event.getMessage().equals("TO")){
            if(event.getId_item()!=null){
                for(Integer id:event.getId_item()){
                    completionViewTO.addObjectAsync(service.queryPerson(id));
                }
            }
        }
        if(event.getMessage().equals("BCC")){
            if(event.getId_item()!=null){
                for(Integer id:event.getId_item()){
                    completionViewBCC.addObjectAsync(service.queryPerson(id));
                }
            }
        }
        if(event.getMessage().equals("CC")){
            if(event.getId_item()!=null){
                for(Integer id:event.getId_item()){
                    completionViewCC.addObjectAsync(service.queryPerson(id));
                }
            }
        }

    }

    private void initView(){
        IvXdelete = findViewById(R.id.xdelete);
        IvSend = findViewById(R.id.iv_send1);
        IvTrainglemore = findViewById(R.id.traingle_more);
        IvTO = findViewById(R.id.iv_to);
        IvCC = findViewById(R.id.iv_cc);
        IvBCC = findViewById(R.id.iv_bcc);
        writeLtter_Rv = findViewById(R.id.write_rv);

        completionViewTO = findViewById(R.id.et_to);
        completionViewCC = findViewById(R.id.et_cc);
        completionViewBCC = findViewById(R.id.et_bcc);
        EtSubject = findViewById(R.id.et_subject);
        EtContent = findViewById(R.id.et_content);
        EtContent.setPlaceholder("输入");
        EtContent.setPadding(20, 20, 20, 20);
        EtContent.setBackgroundColor(Color.WHITE);
        RLCC = findViewById(R.id.RL_cc);
        RLBCC = findViewById(R.id.RL_bcc);

        VCC = findViewById(R.id.Vcc);
        VBCC = findViewById(R.id.Vbcc);
        tvSendEmail = findViewById(R.id.tv_send_email);
        fab = findViewById(R.id.fab_menu);
        fab_attachment = findViewById(R.id.fab_attachment);
        fab_pic = findViewById(R.id.fab_pic);
        fab_timer = findViewById(R.id.fab_timer);
        //初始化联系人
        ContactDao dao = new ContactDao(this);
            people = dao.QueryAllPerson(email_id);
            if(people!=null){
                adapter = new FilteredArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, people) {
                    @Override
                    protected boolean keepObject(Person obj, String mask) {
                        mask = mask.toLowerCase();
                        return obj.getName().toLowerCase().startsWith(mask) || obj.getEmail().toLowerCase().startsWith(mask);
                    }
                };
                completionViewTO.setAdapter(adapter);
                completionViewBCC.setAdapter(adapter);
                completionViewCC.setAdapter(adapter);
            }

        completionViewTO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                TO = s.toString();
            }
        });
        completionViewBCC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                BCC = s.toString();
            }
        });
        completionViewCC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CC = s.toString();
            }
        });

        EtContent.setHtml(initContent);
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
        Intent intent = null;
        switch (v.getId()){
            case R.id.xdelete:
                if(EtContent.getHtml().equals(initContent)){
                    finish();
                }else {
                    showDialog();
                }
                break;
            case R.id.iv_send1:
                CheckMail();
                break;
            case R.id.iv_to:
                selectContact("TO");
                break;
            case R.id.iv_cc:
                selectContact("CC");
                break;
            case R.id.iv_bcc:
                selectContact("BCC");
                break;
            case R.id.fab_attachment:
                intent = new Intent(this, SelectAttachmentActivity.class);
                intent.putExtra("email_id",email.getEmail_id());
                startActivityForResult(intent,1);
                break;
            case R.id.fab_pic:
                toPicture();
                break;
            case R.id.fab_timer:
                Toast.makeText(this,"密送人",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }

    /**
     * 选择联系人
     * @param flag
     */
    private void selectContact(String flag){
        if(people != null){
            Intent intent = new Intent(this, SelectContactActivity.class);
            intent.putExtra("flag",flag);
            intent.putExtra("email_id",email.getEmail_id());
            startActivity(intent);
        }else {
            ToastUtil.showTextToas(this,"暂无联系人");
        }
    }

    /**
     * 入口处
     */
    private void entrance(){
        //普通进
        try{
            Log.d(TAG,"email.address = "+ email.getAddress());
            tvSendEmail.setText(email.getAddress());
            tvSendEmail.setVisibility(View.VISIBLE);
        }catch (Exception ignored){
        }

        //联系人点击进
        try{
            String recipient_email = getIntent().getStringExtra("Recipient_email");
            completionViewTO.setText(recipient_email);
            tvSendEmail.setText(email.getAddress());
        }catch (Exception ignored){
        }

        //草稿箱进
        try {
            ContactDao dao = new ContactDao(this);
            tvSendEmail.setText(emailMessage.getFrom());
            String[] strTo = emailMessage.getTo().split("[,]");
            for(int i = 0; i < strTo.length-1;i++){
                completionViewTO.addObjectAsync(new Person(strTo[i],dao.queryContactName(strTo[i])));
            }
            String[] strBCC = emailMessage.getBcc().split("[,]");
            for(int i = 0; i < strBCC.length-1;i++){
                Log.d(TAG,dao.queryContactName(strBCC[i])+"dsdsdasa");
                completionViewBCC.addObjectAsync(new Person(strBCC[i],dao.queryContactName(strBCC[i])));
            }
            String[] strCC = emailMessage.getCc().split("[,]");
            for(int i = 0; i < strCC.length-1;i++){
                completionViewCC.addObjectAsync(new Person(strCC[i],dao.queryContactName(strCC[i])));
            }
            EtSubject.setText(emailMessage.getSubject());
            try{
                EtContent.setHtml(emailMessage.getContent());
            }catch (Exception e){
            }

            if (emailMessage.getIsAttachment() != 0) {
                AttachmentService attachmentService = new AttachmentService(this);
                String[] ids = emailMessage.getAttachment().split("[&]");
                for (String id : ids) {
                    if (!id.equals("")) {
                        id_item.add(Integer.parseInt(id));
                    }
                    attachments = attachmentService.querySelectAttachment(id_item);
                    if (attachments != null) {
                        //选择Attachment时,网格布局
                        writeLtter_Rv.setVisibility(View.VISIBLE);
                        StaggeredGridLayoutManager layoutManager = new
                                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
                        writeLtter_Rv.setLayoutManager(layoutManager);
                        WriteAttachmentAdapter adapter = new WriteAttachmentAdapter(this, attachments);
                        writeLtter_Rv.setAdapter(adapter);
                    }
                }
            }
        }catch (Exception ignored){
        }

        //编辑

    }

    /**
     * 检查必要属性,发送，成功保存
     */
    public void CheckMail(){
        if(isEmptyS(completionViewTO).isEmpty()){
            ToastUtil.showTextToas(this,"JiaMail: 错误! 请输入收件人!");
        }else{
            if(isEmptyS(EtSubject).isEmpty()){
                ToastUtil.showTextToas(this,"JiaMail: 错误! 请输入主题!");
            }else{
                try{
                    Log.d(TAG,emailMessage.getId()+"ss");
                    final SendMessage sendMessage = new SendMessage(emailMessage,email,attachments,this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendMessage.SendMessage();
                            finish();
                        }
                    }).start();
                }catch (NullPointerException e){
                    setEmailMessage();
                    final SendMessage sendMessage = new SendMessage(emailMessage,email,attachments,this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendMessage.SendMessage();
                            finish();
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * 显示对话框是否保存到草稿箱
     */
    private SaveMessageDialogFragment saveMessageDialogFragment;
    private void showDialog(){
        saveMessageDialogFragment = new SaveMessageDialogFragment();
        setEmailMessage();
        emailMessage.setIsSend(0);
        Bundle bundle = new Bundle();
        bundle.putParcelable("message",emailMessage);
        saveMessageDialogFragment.setArguments(bundle);
        saveMessageDialogFragment.show(getSupportFragmentManager(),"saveMessageDialogFragment");
    }


    /**
     * set属性到message中
     */
    private void setEmailMessage(){
        try{


        emailMessage = new EmailMessage();
        if(email!=null){
            emailMessage.setFrom(email.getAddress());
            emailMessage.setUser_id(email.getUser_id());
            emailMessage.setEmail_id(email.getEmail_id());
        }
        emailMessage.setTo(TO);
        emailMessage.setSubject(isEmptyS(EtSubject));
        emailMessage.setCc(CC);
        emailMessage.setBcc(BCC);
        emailMessage.setContent(EtContent.getHtml().toString());
        Log.d(TAG,EtContent.getHtml());
        if(id_item!=null){
            emailMessage.setIsAttachment(1);
            StringBuilder attachments = new StringBuilder("");
            for(int id:id_item){
                attachments.append(String.valueOf(id)).append("&");
            }
            emailMessage.setAttachment(attachments.toString());
        }else {
            emailMessage.setIsAttachment(0);
        }
        }catch (Exception e){

        }
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
                        WriteAttachmentAdapter adapter = new WriteAttachmentAdapter(this, attachments);
                        writeLtter_Rv.setAdapter(adapter);
                    }
                }
            }
        }
        if (requestCode != RESULT_CANCELED) {    //RESULT_CANCELED = 0(也可以直接写“if (requestCode != 0 )”)
            //读取返回码
            switch (requestCode) {
                case 100:   //相册返回的数据（相册的返回码）
                    Log.d("Main", "相册");
                    Uri uri01 = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri01));
                        EtContent.insertImage(uri01.toString(), null, 100);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
        }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent,100);
        Log.d("Main","跳转相册成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(saveMessageDialogFragment!=null){
            saveMessageDialogFragment.dismiss();
        }
        completionViewBCC.refreshDrawableState();
        completionViewTO.refreshDrawableState();
        completionViewCC.refreshDrawableState();
        EventBus.getDefault().unregister(this);
    }
}
