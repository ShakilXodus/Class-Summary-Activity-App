package com.example.lab2cse489;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassSummaryListActivity extends AppCompatActivity {

    private ArrayList<ClassSummary> lectures = new ArrayList<>();

    ListView lvLectureList;

    String CourseName;

   // private Button btnNew, btnBack;
     TextView header;
    ClassSummaryAdapter csAdaptar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summary_list);

        lvLectureList= findViewById(R.id.lvLectureList);


        header = findViewById(R.id.tv_header);
        CourseName = getIntent().getStringExtra("COURSE");

        loadClassSummary();
        csAdaptar = new ClassSummaryAdapter(ClassSummaryListActivity.this , lectures);
        lvLectureList.setAdapter(csAdaptar);
        lvLectureList.setClickable(true);

//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, lectures);

//        ListView listView = (ListView) findViewById(R.id.lvLectureList);
//        listView.setAdapter(adapter);

        lvLectureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                ClassSummary listupdate = lectures.get(position);
                Intent i = new Intent(ClassSummaryListActivity.this,ClassSummaryActivity.class);

                i.putExtra("ID",listupdate.id);

                i.putExtra("COURSE", CourseName);
                i.putExtra("Type",listupdate.type);
                i.putExtra("Date",formatDate(Long.parseLong(listupdate.date)));
                i.putExtra("Lecture",listupdate.lecture);
                i.putExtra("Topic",listupdate.topic);
                i.putExtra("Summary",listupdate.summary);
                startActivity(i);

            }
        });


        if(CourseName != null){
            header.setText(CourseName);
        }


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        findViewById(R.id.btnNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClassSummaryListActivity.this, ClassSummaryActivity.class);
                i.putExtra("COURSE", CourseName);


                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadClassSummary();
        csAdaptar.notifyDataSetChanged();
        csAdaptar.notifyDataSetInvalidated();
    }

    private String formatDate(long milliseconds) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date = new Date(milliseconds);
        return formatter.format(date);
    }

    private void loadClassSummary(){
        String q = "SELECT * FROM lectures WHERE course= '"+CourseName+"';";
        ClassSummaryDB db = new ClassSummaryDB(this);
        Cursor cur = db.selectLectures(q);
        lectures.clear();
        if(cur!=null && cur.getCount() > 0){
            while (cur.moveToNext()){
                String id = cur.getString(0);
                String course = cur.getString(1);
                String type = cur.getString(2);
                String datetime = cur.getString(3);
                String lecture = cur.getString(4);
                String topic = cur.getString(5);
                String summary = cur.getString(6);


                ClassSummary cs = new ClassSummary(id, course, type, datetime, lecture, topic, summary );
                lectures.add(cs);
               // System.out.println(id+ " " + name + " " + uid + " " + course + " " + type + " " + datetime + " " + lecture + " " + topic + " " + summary );

            }

       }
    }
}

