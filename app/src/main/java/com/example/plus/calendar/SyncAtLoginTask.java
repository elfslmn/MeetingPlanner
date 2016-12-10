package com.example.plus.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Elif SALMAN on 04.12.2016.
 */

public class SyncAtLoginTask extends AsyncTask<Object, Void, Boolean> {
    String userID;
    Calendar calendar;
    Activity parent;
    String LOG_TAG = "SyncAtLoginTask";


    public SyncAtLoginTask(String userID, Activity parent) {
        this.userID = userID;
        this.parent=parent;
        this.calendar=EventActivity.calendar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        parent.findViewById(R.id.button).setClickable(false);
        parent.findViewById(R.id.button_show).setClickable(false);
        parent.findViewById(R.id.calendarView).setClickable(false);
        ((TextView)parent.findViewById(R.id.textView_date)).setText("Syncing...");
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        calendar.list.clear();
        Socket clientSocket;
        try {
            clientSocket = new Socket("35.162.22.247", 6788);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes("GET\n");
            outToServer.writeBytes(userID+"\n");
            Log.e(LOG_TAG, inFromServer.readLine());
            String meetingCode;
            while((meetingCode=inFromServer.readLine())!=null){
                Meeting m;
                m=Meeting.meetingDecoder(meetingCode);
                calendar.add(m);
            }
            Log.e(LOG_TAG, "finished");
            outToServer.close();
            inFromServer.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true ;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
            parent.findViewById(R.id.button_show).setClickable(true);
            parent.findViewById(R.id.button).setClickable(true);
            parent.findViewById(R.id.calendarView).setClickable(true);
            ((TextView) parent.findViewById(R.id.textView_date)).setText("Date");
        if(!aBoolean){
            Toast.makeText(parent.getApplicationContext(),"Server is not available.", Toast.LENGTH_LONG).show();
        }


    }
}
