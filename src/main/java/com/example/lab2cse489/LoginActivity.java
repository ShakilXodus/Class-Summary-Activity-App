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

public class LoginActivity extends AppCompatActivity {
    private EditText etUserId,etPW;
    private CheckBox cbRememberUserId, cbRememberPassword;

    private Button btnSignup, btnExit, btnGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserId=findViewById(R.id.etUserId);
        etPW=findViewById(R.id.etPW);

        cbRememberUserId=findViewById(R.id.cbRememberUserId);
        cbRememberPassword=findViewById(R.id.cbRememberPassword);

        SharedPreferences sp= this.getSharedPreferences("user_info",MODE_PRIVATE);
        Boolean user_id = sp.getBoolean("REMEMBER_USER" ,false);
        Boolean user_pass = sp.getBoolean("REMEMBER_PASSWORD" ,false);


        //check login checkbox
        Boolean user_login_id = sp.getBoolean("REMEMBER_LOGIN_USER" ,false);
        Boolean user_login_pass = sp.getBoolean("REMEMBER_LOGIN_PASSWORD" ,false);

        if(user_login_id == true ){
            cbRememberUserId.setChecked(true);
        }
        if (user_login_pass == true){
            cbRememberPassword.setChecked(true);
        }
        if (user_id == true){
            cbRememberUserId.setChecked(true);
        }
        if (user_pass == true){
            cbRememberPassword.setChecked(true);
        }

        if(user_id || user_login_id)
            etUserId.setText(sp.getString("USER_ID" ,""));
        if(user_pass || user_login_pass)
            etPW.setText(sp.getString("PASSWORD" ,""));



        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = LoginActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor e =sp.edit();

                e.putBoolean("FLAG",false);
                e.commit();
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
    }





    private void processLogin(){
        String userId= etUserId.getText().toString().trim();
        String userPW= etPW.getText().toString().trim();
        String errMsg="";


        SharedPreferences sp =this.getSharedPreferences( "user_info", MODE_PRIVATE);
        SharedPreferences.Editor e =sp.edit();


        e.putBoolean("REMEMBER_USER",cbRememberUserId.isChecked());
        e.putBoolean("REMEMBER_PASSWORD",cbRememberPassword.isChecked());

        e.putBoolean("REMEMBER_LOGIN_USER",cbRememberUserId.isChecked());
        e.putBoolean("REMEMBER_LOGIN_PASSWORD",cbRememberPassword.isChecked());
        e.commit();






        //cross check from cache
        String sp_userId =sp.getString("USER_ID", "");
        String sp_pass =sp.getString("PASSWORD", "");

        //write code to check validation

        if(userId.equals(sp_userId) == false){
            errMsg+="Invalid Used ID, ";
        }

        if(userPW.equals(sp_pass) == false){
            errMsg+="Invalid Password";
        }

        if(errMsg.length()>0){
            showErrorDialogue(errMsg);
            //System.out.println(errMsg);
            return;
        }

        Intent i = new Intent(LoginActivity.this , MainActivity.class);
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
}