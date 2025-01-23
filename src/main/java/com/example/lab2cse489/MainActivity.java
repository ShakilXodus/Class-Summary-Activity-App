package com.example.lab2cse489;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Button btnCSE477,btnCSE479, btnCSE489, btnCSE485, btnExit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCSE477 = findViewById(R.id.btnCSE477);
        btnCSE479 = findViewById(R.id.btnCSE479);
        btnCSE489 = findViewById(R.id.btnCSE489);
        btnCSE485 = findViewById(R.id.btnCSE485);
        btnExit = findViewById(R.id.btnExit);



        btnCSE477.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClassSummaryListActivity.class);

                i.putExtra("COURSE" ,"CSE477");

                startActivity(i);

            }
        });

        btnCSE479.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClassSummaryListActivity.class);

                i.putExtra("COURSE" ,"CSE479");

                startActivity(i);


            }
        });

        btnCSE489.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClassSummaryListActivity.class);

                i.putExtra("COURSE" ,"CSE489");

                startActivity(i);

            }
        });

        btnCSE485.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClassSummaryListActivity.class);

                i.putExtra("COURSE" ,"CSE485");

                startActivity(i);

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        String keys[] = {"action", "sid", "semester"};

        String values[] = {"restore", "2020-3-60-021", "2024-1"};
        httpRequest(keys, values);
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
                    updateLocalDBByServerData(data);
                }
            }
        }.execute();

    }

    private void updateLocalDBByServerData(String data) {
        System.out.println("found");
        try{
            ClassSummaryDB classSummaryDB = new ClassSummaryDB(MainActivity.this);
            JSONObject jo = new JSONObject(data);
            if(jo.has("classes")){
                //classes.clear();
                JSONArray ja = jo.getJSONArray("classes");
                for(int i=0; i<ja.length(); i++){
                    JSONObject summarys = ja.getJSONObject(i);
                    String id = summarys.getString("id");
                    String course = summarys.getString("course");
                    String topic = summarys.getString("topic");
                    String type = summarys.getString("type");
                    long date = summarys.getLong("date");
                    String lecture = summarys.getString("lecture");
                    String summary = summarys.getString("summary");

                    try {
                        classSummaryDB.insertLecture(id,course, date,  type,  lecture, topic, summary);
                    } catch (Exception e) {}
                }
            }
        }catch(Exception e){}
    }


}

