package com.example.plus.calendar;
//http://abhiandroid.com/ui/calendarview


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, EventActivity.class);


        calendarView = (CalendarView) findViewById(R.id.calendarView); // get the reference of CalendarView
        calendarView.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of  focused month
        calendarView.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        calendarView.setSelectedWeekBackgroundColor(Color.GRAY); // red color for the selected week's background
        calendarView.setWeekSeparatorLineColor(Color.DKGRAY); // green color for the week separator line
        // perform setOnDateChangeListener event on CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                int[] date ={dayOfMonth,month,year};
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

                intent.putExtra("SELECTED_DATE", date);
                startActivity(intent);  // startActivityForRrsult TODO

            }
        });
    }
}
