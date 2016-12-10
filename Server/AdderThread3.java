// Elif Salman 
// 2012401102
// elifsalman94@hotmail.com
// CMPE436-Final Project
import java.io.*;
import java.net.*;
import java.util.*;
public class AdderThread3 extends Thread{
	protected Socket connectionSocket;
	protected BufferedReader inFromClient;
	protected HashMap<String, HashSet<Meeting>> users;
	String userID;
	String dateText;
	int time;
	String title;
	String attendee="";

	public AdderThread3(Socket socket, BufferedReader inFromClient, HashMap<String, HashSet<Meeting>> users) {
		super();
		connectionSocket= socket;
		this.users=users;
		this.inFromClient=inFromClient;
	}

	public void run(){
		String response="";
		DataOutputStream outToClient;
		try {
			outToClient  = new DataOutputStream(connectionSocket.getOutputStream());
			
			//Read the meeting info from client
			userID =inFromClient.readLine();
			dateText=inFromClient.readLine();
			time=inFromClient.read();
			title=inFromClient.readLine();
			attendee=inFromClient.readLine();

			HashSet<Meeting> calendar1, calendar2;
			if(!users.containsKey(userID)){
				users.put(userID, new HashSet<Meeting>());
			}
			//The owner of the calendar1 always comes first than the owner of the calendar2.
			//Therefore possible deadlock is prevented due to nested synchronized blocks.
			// Determine the which calendar will be which user.
			if(!attendee.equals("") && users.containsKey(attendee)){ // If there is an registered attendee
				if(userID.compareTo(attendee) < 0){   //userID < attendee
					calendar1=users.get(userID);
					calendar2=users.get(attendee);
				}else{                                                          //attendee < userID
					calendar2=users.get(userID);
					calendar1=users.get(attendee);
				}
			}else{ //There is no registered attendee, Both calendars are same object
				calendar1=users.get(userID);
				calendar2=calendar1;
			}

			synchronized (calendar1) {
				synchronized (calendar2) {

					if(attendee.equals("")){
						Boolean b;
						b=users.get(userID).add(new Meeting(userID, dateText, time, title));
						if(b) response="OKAY";  //The meeting is added
						else response ="NOT_OKAY"; //The meeting cannot be added.
					}
					else{
						if(users.containsKey(attendee)){
							Meeting dummy= new Meeting(attendee, dateText, time, title);
							if(users.get(attendee).contains(dummy)){
								response = "CONFLICT";  //Attendee is not available at that time
							}
							else{
								response="AVAILABLE";  //Attendee is available at that time
								users.get(userID).add(new Meeting(userID, dateText, time, title,attendee));
								users.get(attendee).add(new Meeting(attendee, dateText, time, title,userID));
							}
						}
						else{
							response = "ATTENDEE_NOTFOUND";
						}
					}
					System.out.println(userID+" response: "+response);
				}
			}
			//end of synch

			outToClient.writeBytes(response+"\n");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

