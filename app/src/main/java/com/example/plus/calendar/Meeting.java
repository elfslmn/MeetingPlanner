package com.example.plus.calendar;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Elif SALMAN on 01.12.2016.
 */

public class Meeting {
    String date;
    int time;
    String title;
    String attendee ="";
    String userID;

    public Meeting(String userID, String date, int time, String title) {
        this.userID=userID;
        this.date = date;
        this.time = time;
        this.title = title;
    }

    public Meeting(String userID, String date, int time, String title, String attendee) {
        this.userID=userID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.attendee = attendee;
    }

    public String meetingEncoder(){
        return userID+"*"+date+"*"+time+"*"+title+"*"+attendee+"\n";
    }

    public static Meeting meetingDecoder(String code){
        String[] parts = code.split("\\*");
        Meeting m;
        if(parts.length==5) {
             m = new Meeting(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
        }else{
            m = new Meeting(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
        }
        return m;
    }

    public void meetingToStream(DataOutputStream out) throws IOException{
        out.writeBytes(userID+"\n");
        out.writeBytes(date+"\n");
        out.write(time);
        out.writeBytes(title+"\n");
        out.writeBytes(attendee+"\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meeting meeting = (Meeting) o;

        if (time != meeting.time) return false;
        if (!date.equals(meeting.date)) return false;
        return userID.equals(meeting.userID);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + time;
        result = 31 * result + userID.hashCode();
        return result;
    }
}
