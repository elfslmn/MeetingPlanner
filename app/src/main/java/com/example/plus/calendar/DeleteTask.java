package com.example.plus.calendar;

import android.app.Activity;
import android.app.ActivityManager;
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

public class DeleteTask extends AsyncTask<Object, Void, String>
{
    String LOG_TAG="DeleteTask";
    Meeting meeting;
    public String response;
    Calendar calendar;
    Activity parent;
    TextView textView;
    ListView listView;
    int selectedPosition;

    public DeleteTask(Activity parent, Meeting meeting, int position) {
        this.meeting=meeting;
        this.parent=parent;
        this.calendar =EventActivity.calendar;
        textView= (TextView) parent.findViewById(R.id.textView_info);
        listView= (ListView) parent.findViewById(R.id.listview);
        selectedPosition=position;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        parent.findViewById(R.id.listview).setClickable(false);
        parent.findViewById(R.id.button_delete).setClickable(false);
        parent.findViewById(R.id.button_sync).setClickable(false);
    }

    @Override
    protected String doInBackground(Object... params) {
        Socket clientSocket;
        try {
            clientSocket = new Socket("35.162.22.247", 6788);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes("DELETE\n");
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
            parent.findViewById(R.id.listview).setClickable(true);
            parent.findViewById(R.id.button_delete).setClickable(true);
            Toast.makeText(parent.getApplicationContext(),"Server is not available.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.equals("DELETED")){
            calendar.list.remove(meeting);
            textView.setText("Meeting is deleted successfully.");

            ArrayAdapter<String> adaptor= ((ArrayAdapter<String>) listView.getAdapter());
            adaptor.remove(adaptor.getItem(selectedPosition));

        }
        else if(s.equals("MEETING_NOT_FOUND")){
            textView.setText("Meeting has already been deleted by "+meeting.attendee+" .Please sync your calendar.");
        }
        else{
            textView.setText("Meeting cannot be deleted.");
        }


        parent.findViewById(R.id.listview).setClickable(true);
        parent.findViewById(R.id.button_delete).setClickable(true);
        parent.findViewById(R.id.button_sync).setClickable(true);
    }
}
