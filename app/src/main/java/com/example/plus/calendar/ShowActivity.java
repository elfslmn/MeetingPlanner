package com.example.plus.calendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {
    String LOG_TAG="Show Activity";

    String userID;
    ListView listView;
    TextView textView;
    Button deleteButton, syncButton;
    Calendar calendar= EventActivity.calendar;
    Activity activity =this;
    AsyncTask deleteTask, syncTask;
    Meeting m;
    static ArrayList<String> adapterList;
    static ArrayAdapter<String> adapter;

    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        userID =getIntent().getExtras().getString("USER_ID");

        deleteButton= (Button) findViewById(R.id.button_delete);
        syncButton = (Button) findViewById(R.id.button_sync);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView_info);

        textView.setText("Your calendar might not be up-to-date. Please click sync button first.");

        adapterList = calendar.getScheduledEvents();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    selectedPosition=position;
                    String[] array= calendar.convertToArray();
                    String meetingCode=array[position-1].replace("\n","");
                    m = Meeting.meetingDecoder(meetingCode);

                    if(m.attendee.equals("")){
                        textView.setText("Do you want to delete the meeting  "+m.title+" on "+m.date+" at "+m.time+":00?");
                    }else{
                        textView.setText("Do you want to delete the meeting  "
                                +m.title+" on "+m.date+" at "+m.time+":00 with "+m.attendee+"?");
                    }
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m==null){
                    Toast.makeText(getApplicationContext(),
                            "Please click to the meeting that you want to delete", Toast.LENGTH_LONG).show();
                }
                else {
                    deleteTask = new DeleteTask(activity, m,selectedPosition);
                    deleteTask.execute();
                    m=null;
                    selectedPosition=0;
                }
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncTask = new SyncAtShowTask(userID,activity);
                syncTask.execute();
            }
        });




    }
}
