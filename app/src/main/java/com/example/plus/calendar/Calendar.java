package com.example.plus.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Elif SALMAN on 03.12.2016.
 */

public class Calendar {
    //ArrayList<Meeting> adapterList = new ArrayList<Meeting>();
    HashSet<Meeting> list=new HashSet<Meeting>();
    String userID;
    String LOG_TAG="Calendar";

    public Calendar(String userID) {
        this.userID = userID;
    }

    public void add(Meeting m){
      list.add(m);
    }

    public String[] convertToArray(){
        String[] array = new String[list.size()];
        int i=0;
        for(Meeting m: list){
            array[i]=m.meetingEncoder();
            i++;
        }
        return array;
    }

    public ArrayList<String> getScheduledEvents(){
        String array[]=this.convertToArray();
        ArrayList<String> list = new ArrayList<>();
        list.add("Scheduled Events");

        for(int i=0; i<array.length;i++){
            String[] parts = array[i].split("\\*");
            String message;
            if(!parts[4].equals("\n")) {
                message = "- " + parts[3] + " on " + parts[1] + " at " + parts[2] + ":00 with "+parts[4].replace("\n", "");
            }
            else{
                message ="- " + parts[3] + " on " + parts[1] + " at " + parts[2] + ":00";
            }
            list.add(message);
        }
        return list;
    }



}
