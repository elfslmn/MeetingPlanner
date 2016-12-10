// Elif Salman 
// 2012401102
// elifsalman94@hotmail.com
// CMPE436-Final Project
import java.io.*;
import java.net.*;
import java.util.*;
public class RemoverThread3 extends Thread{
	protected Socket connectionSocket;
	protected BufferedReader inFromClient;
	protected HashMap<String, HashSet<Meeting>> users;
	String userID;
	String dateText;
	int time;
	String title;
	String attendee;

	public RemoverThread3(Socket socket, BufferedReader inFromClient, HashMap<String, HashSet<Meeting>> users) {
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

			if(!users.containsKey(userID)){
				users.put(userID, new HashSet<Meeting>());
			}

			HashSet<Meeting> calendar1, calendar2;
			//The owner of the calendar1 always comes first than the owner of the calendar2.
			//Therefore possible deadlock is prevented due to nested synchronized blocks.
			// Determine the which calendar will be which user.

			if(!attendee.equals("") && users.containsKey(attendee)){ // If there is an registered attendee
				if(userID.compareTo(attendee) < 0){   //userID < attendee
					calendar1=users.get(userID);
					calendar2=users.get(attendee);
				}else{  //attendee < userID
					calendar2=users.get(userID);
					calendar1=users.get(attendee);
				}

				synchronized (calendar1) {

					synchronized (calendar2) {

						Boolean b1,b2;
						b1= users.get(userID).remove(new Meeting(userID, dateText, time, title,attendee));

						if(!attendee.equals("")){
							b2=users.get(attendee).remove(new Meeting(attendee, dateText, time, title,userID));
						}
						else{
							b2=true;
						}
						if(!b1){
							response="MEETING_NOT_FOUND";
						}
						else if(b1&&b2){
							response="DELETED";
						}
						else{
							response="NOT_DELETED";
						}

						System.out.println("Delete response: "+response+","+userID+","+attendee);
					}
				}//end of synch

				outToClient.writeBytes(response+"\n");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
