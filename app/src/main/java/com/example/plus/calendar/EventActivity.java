package com.example.plus.calendar;
// https://github.com/StackTipsLab/Add-Calendar-Event-Android/blob/master/app/src/main/java/com/javatechig/addevent/MainActivity.java

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

public class EventActivity extends AppCompatActivity {

    Button button;
    int[] date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        button = (Button) findViewById(R.id.button);

        Intent intent = getIntent();
        date = intent.getIntArrayExtra("SELECTED_DATE");
        String message = date[0] + "/" + date[1] + "/" + date[2];

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(Intent.ACTION_INSERT);
                eventIntent.setType("vnd.android.cursor.item/event");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, date[2]);
                calendar.set(Calendar.MONTH, date[1]);
                calendar.set(Calendar.DAY_OF_MONTH, date[0]);

                long startTime = calendar.getTimeInMillis();
                long endTime = calendar.getTimeInMillis()  + 60 * 60 * 1000;


                eventIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                eventIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
               // eventIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                eventIntent.putExtra(CalendarContract.Events.TITLE, "Neel Birthday");
                eventIntent.putExtra(CalendarContract.Events.DESCRIPTION,  "This is a sample description");
                eventIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
                eventIntent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

                startActivity(eventIntent);
            }
        });



    }
}
