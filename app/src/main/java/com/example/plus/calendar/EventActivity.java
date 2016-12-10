package com.example.plus.calendar;
//http://abhiandroid.com/ui/calendarview

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button addButton, showButton;
    TextView tvDate;
    AutoCompleteTextView tvTitle;
    AutoCompleteTextView tvWith;
    Spinner spinner;
    Intent showIntent;
    Activity activity =this;

    int[] date= new int[3];
    int time;
    String title;
    String attendee;
    String dateText;
    String userID;
    static Calendar calendar;
    AsyncTask meetingTask, gettingTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        userID = getIntent().getExtras().getString("USER_ID");
        calendar = new Calendar(userID);

        addButton = (Button) findViewById(R.id.button);
        showButton = (Button) findViewById(R.id.button_show);
        tvDate = (TextView) findViewById(R.id.textView_date);
        tvTitle = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_title);
        tvWith = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_with);
        spinner = (Spinner) findViewById(R.id.spinner);

        gettingTask = new SyncAtLoginTask(userID, activity);
        gettingTask.execute();

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = Integer.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                time=0;
            }
        });


        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFocusedMonthDateColor(getResources().getColor(R.color.colorAccent));
        calendarView.setUnfocusedMonthDateColor(Color.BLUE);
        calendarView.setWeekSeparatorLineColor(Color.DKGRAY);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month++;
                date[0]=dayOfMonth;
                date[1]=month;
                date[2] = year;
                dateText = date[0] + "/" + date[1] + "/" + date[2];
                tvDate.setText("Date: "+dateText);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = tvTitle.getText().toString().trim();
                String  text = tvDate.getText().toString();
                attendee = tvWith.getText().toString().trim();

                // Check if the user specified date and title
                if(text.equals("Date")){
                    Toast.makeText(getApplicationContext(),"Select a date", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(title)){
                    Toast.makeText(getApplicationContext(),"Write a title for your event", Toast.LENGTH_SHORT).show();
                }

                // Check the user input if any special character is used
                else if(!title.matches("^[a-zA-Z0-9-_ ]+$")){
                    Toast.makeText(getApplicationContext(),
                            "Title should contain only characters in the adapterList, a-z, A-Z, 0-9,-,_ and space",
                            Toast.LENGTH_LONG).show();
                }
                else if(!attendee.matches("^[a-zA-Z0-9_ ]+$")&& !TextUtils.isEmpty(attendee)){
                    Toast.makeText(getApplicationContext(),
                            "Attendee name should contain only characters in the adapterList, a-z, A-Z, 0-9,underscore and space",
                            Toast.LENGTH_LONG).show();
                }
                else if(attendee.equals(userID)){
                    Toast.makeText(getApplicationContext(),"You cannot be attendee.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Meeting meeting;
                    v.setClickable(false);
                    if(TextUtils.isEmpty(attendee)) {
                        meeting = new Meeting(userID,dateText, time, title);
                    }else{
                        meeting = new Meeting(userID,dateText, time, title,attendee);
                    }

                    if(calendar.list.contains(meeting)){
                        Toast.makeText(getApplicationContext(),"You have a scheduled meeting at that time ", Toast.LENGTH_SHORT).show();
                        v.setClickable(true);
                    }else {
                        meetingTask = new AddingTask(activity, meeting);
                        meetingTask.execute();
                    }

                }

            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntent = new Intent(getApplicationContext(), ShowActivity.class);
                showIntent.putExtra("USER_ID",userID);
                startActivity(showIntent);
            }
        });





    }

}