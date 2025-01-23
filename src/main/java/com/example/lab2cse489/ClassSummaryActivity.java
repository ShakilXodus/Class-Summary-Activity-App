package com.example.lab2cse489;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassSummaryActivity extends AppCompatActivity {

    private EditText etName ,etId, etDate,etLecture,etTopic,etSummary;

    private Button btnSave, btnCancel;

    private RadioGroup radioCourse, radioType;

    private RadioButton  radiobtnTheory, radiobtnLab;

    String CourseName;
    private TextView header_Course;

    String lectureID="";

    String userType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summary);


        etLecture=findViewById(R.id.etLecture);
        etTopic=findViewById(R.id.etTopic);
        etSummary=findViewById(R.id.etSummary);
        etDate=findViewById(R.id.etDate);
        radioType=findViewById(R.id.radioType);
        radiobtnTheory=findViewById(R.id.radiobtnTheory);
        radiobtnLab=findViewById(R.id.radiobtnLab);
        header_Course= findViewById(R.id.tvCourseName);



        CourseName = getIntent().getStringExtra("COURSE");

        if(CourseName != null){
            header_Course.setText(CourseName);
        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ClassSummaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processSave();
            }

        });

        Intent i = this.getIntent();

        if(i.hasExtra("Lecture")) {
            String id = i.getStringExtra("ID");
            String course=i.getStringExtra("COURSE");
            String type = i.getStringExtra("Type");
            String date = i.getStringExtra("Date");
            String lecture = i.getStringExtra("Lecture");
            String topic = i.getStringExtra("Topic");
            String summary = i.getStringExtra("Summary");


            if (type.equals("Lab")) {
                radiobtnLab.setChecked(true);
            } else if (type.equals("Theory")) {
                radiobtnTheory.setChecked(true);
            }
            lectureID = id;
            header_Course.setText(course);
            etDate.setText(date);
            etLecture.setText(lecture);
            etTopic.setText(topic);
            etSummary.setText(summary);
        }
    }




    private void checkType() {
        if(radiobtnTheory.isChecked()){
            userType =radiobtnTheory.getText().toString();
        }
        else if(radiobtnLab.isChecked()){
            userType =radiobtnLab.getText().toString();
        }
    }


    private void processSave() {
        String userCourse =CourseName.trim();
        String userDate= etDate.getText().toString().trim();
        String userLecture= etLecture.getText().toString().trim();
        String userTopic= etTopic.getText().toString().trim();
        String userSummary= etSummary.getText().toString().trim();
        String errMsg="";


        checkType();



        if(userLecture.length()==0) {
            errMsg+="Lecture Number Can not be Zero, \n";
        } else if (Integer.parseInt(userLecture) > 32) {
            errMsg += "Lecture Number Must Be within 1 to 32, \n";
        }

        if(userDate.length()==0){
            errMsg+="Invalid Date, \n";
        }
        if(userTopic.length()<5 || userTopic.length()>20)
            errMsg+="Invalid  Topic Size, \n";

        if(userSummary.length()<50){
            errMsg+="Explain More Briefly, \n";
        } else if (userSummary.length()>300) {
            errMsg+="Explain More Shortly, \n";
        }


        if(userType.length()==0){
            errMsg+="Select a Type, \n";
        }

        if(errMsg.length()>0){
            showErrorDialogue(errMsg);
            //System.out.println(errMsg);
            return;
        }


        Date convertDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            convertDate = sdf.parse(userDate);
        }catch (ParseException e){
            showErrorDialogue("PLease Use appropriate date format");
            return;
        }



        if(errMsg.isEmpty()){
            if (lectureID.length() == 0){
                lectureID=userTopic+String.valueOf(System.currentTimeMillis());
                ClassSummaryDB db = new ClassSummaryDB(this);
                db.insertLecture(lectureID,userCourse,convertDate.getTime(), userLecture, userTopic,userSummary,userType);
            }
            else {
                ClassSummaryDB db = new ClassSummaryDB(this);
                db.updateLecture(lectureID,userCourse, convertDate.getTime(), userLecture, userTopic,userSummary,userType);

            }


            String keys[] = {"action", "sid", "semester", "id", "course", "type", "topic", "date", "lecture", "summary"};
            String values[] = {"backup", "2020-3-60-021", "2024-1", lectureID, CourseName, userType, userTopic, userDate,userLecture, userSummary};

            httpRequest(keys, values);

//            Intent i = new Intent(ClassSummaryActivity.this , MainActivity.class);
//
//            startActivity(i);
            finish();
        }



    }

    private void httpRequest(final String keys[],final String values[]) {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

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