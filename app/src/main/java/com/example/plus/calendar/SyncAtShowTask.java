package com.example.plus.calendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Elif SALMAN on 07.12.2016.
 */

public class SyncAtShowTask extends AsyncTask<Object, Void, Boolean> {
    String userID;
    Calendar calendar;
    Activity parent;
    String LOG_TAG = "SyncAtShowTask";
    ListView listView;

    public SyncAtShowTask(String userID, Activity parent) {
        this.userID = userID;
        this.parent=parent;
        this.calendar=EventActivity.calendar;
        listView = (ListView) parent.findViewById(R.id.listview);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listView.setClickable(false);
        parent.findViewById(R.id.button_delete).setClickable(false);
        parent.findViewById(R.id.button_sync).setClickable(false);
        ((TextView)parent.findViewById(R.id.textView_info)).setText("Syncing...");
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
        if(!aBoolean){
            Toast.makeText(parent.getApplicationContext(),"Server is not available.", Toast.LENGTH_LONG).show();
        }
        else{
           /* ArrayList<String> adapterList = calendar.getScheduledEvents();
            ArrayAdapter<String> adapter=new ArrayAdapter<>(parent.getApplicationContext(),
                    android.R.layout.simple_list_item_1, adapterList);
            listView.setAdapter(adapter);*/

           ArrayList<String> newAdapterList = calendar.getScheduledEvents();
           ShowActivity.adapterList.clear();
            for(String s: newAdapterList){
                ShowActivity.adapter.add(s);
            }
            ShowActivity.adapter.notifyDataSetChanged();

            ((TextView)parent.findViewById(R.id.textView_info)).setText("Synchronization is completed.");
        }

        listView.setClickable(true);
        parent.findViewById(R.id.button_delete).setClickable(true);
        parent.findViewById(R.id.button_sync).setClickable(true);

    }
}
