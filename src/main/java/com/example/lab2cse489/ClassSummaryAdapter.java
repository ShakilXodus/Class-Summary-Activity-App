package com.example.lab2cse489;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassSummaryAdapter extends ArrayAdapter<ClassSummary> {

    private final Context context;
    private final ArrayList<ClassSummary> summaryArrayList;
    private LayoutInflater inflater;

    public ClassSummaryAdapter(@NonNull Context context, @NonNull ArrayList<ClassSummary> items) {
        super(context, -1, items);
        this.context = context;
        this.summaryArrayList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_lecture_item, parent, false);

        TextView topic = rowView.findViewById(R.id.tvLectureTopic);
        TextView dateTime = rowView.findViewById(R.id.tvLectureDate);
        TextView summary = rowView.findViewById(R.id.tvLectureSummary);
        //TextView eventType = rowView.findViewById(R.id.tvEventType);

        ClassSummary e = summaryArrayList.get(position);


        topic.setText(e.topic);
        dateTime.setText(formatDate(Long.parseLong(e.date)));
        summary.setText(e.summary);


        return rowView;
    }

    private String formatDate(long milliseconds) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date = new Date(milliseconds);
        return formatter.format(date);
    }

}
