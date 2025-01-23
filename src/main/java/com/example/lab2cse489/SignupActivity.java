package com.example.lab2cse489;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserName , etEmail,etPhone, etUserId,etPW,etRPW;
    private CheckBox cbRememberUserId, cbRememberPassword;

    private Button btnLogin, btnExit, btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decideNavigation();

        setContentView(R.layout.activity_signup);

        etUserName=findViewById(R.id.etUserName);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etUserId=findViewById(R.id.etUserId);
        etPW=findViewById(R.id.etPW);
        etRPW=findViewById(R.id.etRPW);

        cbRememberUserId=findViewById(R.id.cbRememberUserId);
        cbRememberPassword=findViewById(R.id.cbRememberPassword);

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = SignupActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor e =sp.edit();

                e.putBoolean("FLAG",true);
                e.commit();
                finish();
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = SignupActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor e =sp.edit();

                e.putBoolean("FLAG",true);
                e.commit();
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignup();
            }
        });

    }
    private void processSignup(){
        String userName= etUserName.getText().toString().trim();
        String userEmail= etEmail.getText().toString().trim();
        String userPhone= etPhone.getText().toString().trim();
        String userId= etUserId.getText().toString().trim();
        String userPW= etPW.getText().toString().trim();
        String userRPW= etRPW.getText().toString().trim();
        String errMsg="";


        //write code to check validation
        if (userName.length() < 4 || userName.length() > 15){
            errMsg +="Invalid User Name, ";
        }

        if((userPhone.startsWith("+880") && userPhone.length()==14) ||
                (userPhone.startsWith("880") && userPhone.length()==13)||
                (userPhone.startsWith("01") && userPhone.length()==11)) {
        }else {
            errMsg +="Invalid Phone Number, ";
        }

        if(userId.length()<4 || userId.length() > 8)
            errMsg+="Invalid Used ID, ";

        if(userPW.length()!=4 || !userPW.equals(userRPW))
            errMsg+="Invalid Password";

        if(userEmail.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
            errMsg+="Invalid Email Address, ";

        if(errMsg.length()>0){
            showErrorDialogue(errMsg);
            //System.out.println(errMsg);
            return;
        }

        SharedPreferences sp =this.getSharedPreferences( "user_info",MODE_PRIVATE);
        SharedPreferences.Editor e =sp.edit();

        e.putString("USER_NAME",userName);
        e.putString("USER_ID",userId);
        e.putString("USER_EMAIL",userEmail);
        e.putString("USER_PHONE",userPhone);
        e.putString("PASSWORD",userPW);
        e.putBoolean("REMEMBER_USER",cbRememberUserId.isChecked());
        e.putBoolean("REMEMBER_PASSWORD",cbRememberPassword.isChecked());
        e.putBoolean("FLAG", true);
        e.commit();

        Intent i = new Intent(SignupActivity.this , MainActivity.class);
        startActivity(i);
        finish();



    }
    private void showErrorDialogue(String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert =builder.create();
        alert.show();
    }

    private void decideNavigation(){

        SharedPreferences sp =this.getSharedPreferences("user_info",MODE_PRIVATE);
        String userName = sp.getString("USER_NAME", "NOT-CREATED");
        Boolean flag =sp.getBoolean("FLAG",false);

        if(flag){
            if(!userName.equals("NOT-CREATED")){
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

}
