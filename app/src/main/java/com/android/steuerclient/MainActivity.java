package com.android.steuerclient;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.smailnet.emailkit.Draft;
import com.smailnet.emailkit.EmailKit;

import static com.android.steuerclient.ActivityCollection.removeAll;


/**
 * @Author： zh
 * @Date： 20/12/20
 */

public class MainActivity extends AppCompatActivity  {

    private int body = 1;

    private Button mBtnPhoto,mBtnAudio,mBtnVideo, mBtnSms, mBtnMail, mBtnSet;
    private EditText mEtNumber;
    private EditText mEtMail, mEtAppPassWord;

    final private static String TAG = "Tag"; //title of logcat
    final private static int REQUEST_CODE = 29; // requestCode

    private SmsManager smsManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (this.lacksPermissions()) {
            this.requestPermissions(this.getPermissions(), 100);
        }

        smsManager = SmsManager.getDefault();
        mBtnPhoto = findViewById(R.id.btn_ecamera);
        mBtnAudio = findViewById(R.id.btn_eaudio);
        mBtnVideo = findViewById(R.id.btn_evideo);

        mEtNumber = findViewById(R.id.et_phone);
        mBtnSet = findViewById(R.id.btn_set);

        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mBtnSet.getText().toString().equals("set")){
                   mEtNumber.setEnabled(false);
                    mBtnSet.setText("Reset");

                } else{
                    mEtNumber.setEnabled(true);
                    mBtnSet.setText("set");
                }
                mEtNumber.setSelection(mEtNumber.getText().length());//Set the cursor position to the end

            }
        });

    }

    private String subject, content;

    /**
     * get permission
     * @return
     */

    private String[] getPermissions() {
        return new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    private boolean lacksPermissions() {
        String[] var3 = this.getPermissions();
        int var4 = var3.length;

        for(int var2 = 0; var2 < var4; ++var2) {
            String permission = var3[var2];
            if (this.lacksPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission((Context)this, permission) == -1;
    }


    /**
     * send Email
     * @param subject
     * @param content
     */
    private void sendEmail(String subject, String content){

        // this Activity receive the data from MainActivity
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        String user = bundle.getString("user");// get user E-mail
        String password = bundle.getString("password"); // get password

        EmailKit.initialize(this);
        EmailKit.Config config = new EmailKit.Config()
                .setSMTP("smtp.gmail.com", 465, true)  //set host、port and ssl of SMTP
                .setIMAP("imap.gmail.com", 993, true)  ////set host、port and ssl of IMAP
                .setAccount(user)          //from which E-mail Address
                .setPassword(password);    //The App-Password of the sender's E-mail

        //write a E-mail
        Draft draft = new Draft()
                .setNickname("ControlProject") //Sender's nickname
                .setTo(user)             //Recipient
                .setSubject(subject)  //subject
                .setText(content);       //content

        //send E-mail
        EmailKit.useSMTPService(config)
                .send(draft, new EmailKit.GetSendCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Sent successfully!");
                    }

                    @Override
                    public void onFailure(String errMsg) {
                        Log.i(TAG, errMsg);
                    }
                });
    }

    /**
     * Email Order Button
     * @param view
     */
    public void ePhoto(View view){
            subject = "order";
            content = "order:photo;1;1";
            sendEmail(subject, content);
            Toast.makeText(this, "Photo E-Mail-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }

    public void eVideo(View view){
            subject = "order";
            content = "order:video;1;1";
            sendEmail(subject, content);
            Toast.makeText(this, "Video E-Mail-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }

    public void eAudio(View view){
            subject = "order";
            content = "order:audio;1;1";
            sendEmail(subject, content);
            Toast.makeText(this, "Audio E-Mail-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }


    /**
     * SMS Order Button
     * @param view
     */
    //take Photo
    public void photo(View view) {
        long time = System.currentTimeMillis();
        String smsContentPhoto = "order:photo" + ";" + body + ";" + time;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        smsManager.sendTextMessage(mEtNumber.getText().toString(), null,
                smsContentPhoto, pendingIntent, null);
        Toast.makeText(this, "Photo SMS-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }

    //video record
    public void video(View view) {
        long time = System.currentTimeMillis();
        String smsContentVideo = "order:video" + ";" + body + ";" + time;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        smsManager.sendTextMessage(mEtNumber.getText().toString(), null,
                smsContentVideo, pendingIntent, null);
        Toast.makeText(this, "Video SMS-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }

    //audio record
    public void audio(View view) {
        long time = System.currentTimeMillis();
        String smsContentAudio = "order:audio" + ";" + body + ";" + time;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        smsManager.sendTextMessage(mEtNumber.getText().toString(), null,
                smsContentAudio, pendingIntent, null);
        Toast.makeText(this, "Audio SMS-Order Sent Finished.", Toast.LENGTH_LONG).show();
    }

    public void send(View view){
        Intent shareInt = new Intent(Intent.ACTION_SEND);
        shareInt.setType("text/plain");
        shareInt.putExtra(Intent.EXTRA_SUBJECT, "share with");
        shareInt.putExtra(Intent.EXTRA_TEXT, "OK");
        shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(shareInt);
    }

    private long exitTime = 0;

    /**
     * remove all Activity
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText( this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            removeAll();
        }
    }
}