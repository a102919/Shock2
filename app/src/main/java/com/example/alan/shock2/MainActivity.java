package com.example.alan.shock2;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    Vibrator myVibrator;

    private ImageButton open,setting;
    private CameraManager manager;
    private CheckBox oldp;
    private SharedPreferences pref;
    private setbollon setbollon;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //加入你的設備代碼
        mAdView.loadAd(adRequest);

        initSetting();
    }

    public void initSetting(){
        String password = getSharedPreferences("SETTING", MODE_PRIVATE)
                .getString("PASSWORD", "null");

        setbollon=new setbollon(false,false,false);
        setbollon.setPasseord(password);

        constraintLayout=(ConstraintLayout)findViewById(R.id.back);
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        pref = getSharedPreferences("SETTING", MODE_PRIVATE);
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        open=(ImageButton)findViewById(R.id.openButton);
        open.setImageResource(R.mipmap.power3);
        open.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //Log.i("password","a: "+userid);
                if(setbollon.isSetting_b()){
                    passwordDialog(setbollon.getPasseord());
                }else {
                    setbollon.setModle(true);
                    constraintLayout.setBackgroundColor(Color.BLACK);
                    setting.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        setting = (ImageButton)findViewById(R.id.imageButton);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void open(View view) {
        Log.i("aaa","modle= "+setbollon.isModle());
        if(setbollon.isOn()){
            if(setbollon.isModle()){
                myVibrator.cancel();
            }else {
                try {
                    manager.setTorchMode("0", false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            open.setImageResource(R.mipmap.power3);
            setbollon.setOn(false);
        }else {
            if(setbollon.isModle()){
                myVibrator.vibrate(new long[]{10, 4000}, 0);
            }else {
                try {
                    manager.setTorchMode("0", true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            setbollon.setOn(true);
            open.setImageResource(R.mipmap.power4);
        }
    }
    public void setting(View view){
        changeDialog();
    }
    private void settingDialog(){
        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_layout, null);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.setting)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) item.findViewById(R.id.edittext);
                        pref.edit()
                                .putString("PASSWORD", editText.getText().toString())
                                .commit();
                        setbollon.setSetting_b(true);
                        Toast.makeText(getApplicationContext(), getString(R.string.setting_ok), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .show();
    }
    private void passwordDialog(final String password){
        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_layout, null);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.edit_password)
                .setView(item)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) item.findViewById(R.id.edittext);
                        String password2=editText.getText().toString();
                        Log.i("aaa","b: "+password+" c: "+password2);
                       if(password.equals(password2)){
                           Log.i("aaa","true");
                           constraintLayout.setBackgroundColor(Color.BLACK);
                           setting.setVisibility(View.VISIBLE);
                           setbollon.setModle(true);
                       }else {
                           Log.i("aaa","false");
                           setbollon.setModle(false);
                       }
                    }
                })
                .show();
    }

    private void changeDialog(){
        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.change_latout, null);
         AlertDialog dialog= new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.set)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

        oldp = (CheckBox) item.findViewById(R.id.checkBox);
        oldp.setChecked(setbollon.isSetting_b());
        oldp.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                //判斷CheckBox是否有勾選，同mCheckBox.isChecked()
                if(isChecked) {
                    settingDialog();
                }
                else {
                    setbollon.setSetting_b(false);
                    pref.edit().remove("PASSWORD").commit();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDestroy(){
        super.onDestroy();
        myVibrator.cancel();
        try {
            manager.setTorchMode("0", false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
