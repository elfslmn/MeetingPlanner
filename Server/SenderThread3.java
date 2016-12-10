// Elif Salman 
// 2012401102
// elifsalman94@hotmail.com
// CMPE436-Final Project
import java.io.*;
import java.net.*;
import java.util.*;

public class SenderThread3 extends Thread {
        protected HashMap<String, HashSet<Meeting>> users;
        protected BufferedReader inFromClient;
        protected Socket connectionSocket;

        public SenderThread3(Socket connectionSocket, BufferedReader inFromClient,  HashMap<String, HashSet<Meeting>> users) {
                super();
                this.users = users;
                this.inFromClient = inFromClient;
                this.connectionSocket = connectionSocket;
        }

        public void run(){
                DataOutputStream outToClient;

                try{
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    String userID=inFromClient.readLine();

                    HashSet<Meeting> calendar;
                // Determine the calendar object for lock
                   if(users.containsKey(userID)){
                        calendar=users.get(userID);
                   }
                   else{
                        calendar=new HashSet<Meeting>();
                   }

                    synchronized (calendar) {
                        if(!users.containsKey(userID)){
                            users.put(userID, calendar);
                            System.out.println("new calendar created for user "+userID);
                            outToClient.writeBytes("CREATED\n");
                            outToClient.close();
                         }
                         else{
                            outToClient.writeBytes("SENDING\n");
                            for(Meeting m: calendar){
                                 outToClient.writeBytes(m.meetingEncoder());
                            }
                            outToClient.close();
                            System.out.println("calendar sent to "+userID);
                        }
                    }

                }
                catch(IOException e)    {
                        e.printStackTrace();
                }

        }

}

