// Elif Salman 
// 2012401102
// elifsalman94@hotmail.com
// CMPE436-Final Project
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
