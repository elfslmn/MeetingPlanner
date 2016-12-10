package com.example.plus.calendar;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Elif SALMAN on 27.11.2016.
 */

public class AddingTask extends AsyncTask<Object, Void, String> {
    String LOG_TAG = "AddingTask";
    Meeting meeting;
    public String response;
    Calendar calendar;
    Activity parent;

    public AddingTask(Activity parent, Meeting meeting) {
        this.meeting=meeting;
        this.parent=parent;
        this.calendar =EventActivity.calendar;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        parent.findViewById(R.id.button).setClickable(false);
        parent.findViewById(R.id.button_show).setClickable(false);
        parent.findViewById(R.id.calendarView).setClickable(false);
        ((TextView)parent.findViewById(R.id.textView_date)).setText("Checking...");
    }

    @Override
    protected String doInBackground(Object... params) {

        Socket clientSocket;
        try {
            clientSocket = new Socket("35.162.22.247", 6788);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes("ADD\n");
            meeting.meetingToStream(outToServer);
            response = inFromServer.readLine();
            Log.e(LOG_TAG, response);

            outToServer.close();
            inFromServer.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("Failed")){
            parent.findViewById(R.id.button_show).setClickable(true);
            parent.findViewById(R.id.button).setClickable(true);
            parent.findViewById(R.id.button_sync).setClickable(true);
            parent.findViewById(R.id.calendarView).setClickable(true);
            ((TextView)parent.findViewById(R.id.textView_date)).setText("Date");
            Toast.makeText(parent.getApplicationContext(),"Server is not available.", Toast.LENGTH_LONG).show();
            return;
        }

        if(s.equals("OKAY")){
            calendar.add(meeting);
            Toast.makeText(parent.getApplicationContext(),"Added to your calender.", Toast.LENGTH_SHORT).show();
        }
        else if(s.equals("NOT_OKAY")){
            Toast.makeText(parent.getApplicationContext(),"Another user added a meeting at that time with you.", Toast.LENGTH_SHORT).show();
            Toast.makeText(parent.getApplicationContext(),"Please click show and sync your calendar.", Toast.LENGTH_LONG).show();
        }
        else if(s.equals("AVAILABLE")){
            calendar.add(meeting);
            Toast.makeText(parent.getApplicationContext(),"A meeting with "+ meeting.attendee+" is added to your calender.", Toast.LENGTH_LONG).show();
        }
        else if(s.equals("CONFLICT")){
            Toast.makeText(parent.getApplicationContext(),meeting.attendee+" is not available at "+
                    meeting.date+","+meeting.time+":00.", Toast.LENGTH_LONG).show();
        }
        else if(s.equals("ATTENDEE_NOTFOUND")){
            Toast.makeText(parent.getApplicationContext(),meeting.attendee+" does not use this application.", Toast.LENGTH_LONG).show();
        }

        for(Meeting m : calendar.list){
            Log.i("MEETINGS",m.meetingEncoder());
        }

        parent.findViewById(R.id.button_show).setClickable(true);
        parent.findViewById(R.id.button).setClickable(true);
        parent.findViewById(R.id.calendarView).setClickable(true);
        ((TextView)parent.findViewById(R.id.textView_date)).setText("Date");

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }




}
