// Elif Salman 
// 2012401102
// elifsalman94@hotmail.com
// CMPE436-Final Project
import java.util.*;
import java.io.*;
import java.net.*;

class TCPServer3 {
        public static void main(String argv[]) throws Exception{

                ServerSocket welcomeSocket = new ServerSocket(6788);
                HashMap<String, HashSet<Meeting>> users= new HashMap<String, HashSet<Meeting>>(); //calendars of all the users.

                while(true) {
                        System.out.println("wait...");
                        Socket connectionSocket = welcomeSocket.accept();
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        String command=inFromClient.readLine();
                        if(command.equals("ADD")){  //Adds a new meeting if it is possible
                                new AdderThread3(connectionSocket,inFromClient,users).start();
                        }
                        else if(command.equals("GET")){ //Sends the calendar to the user back
                                new SenderThread3(connectionSocket,inFromClient,users).start();

                        }
                        else if(command.equals("DELETE")){  //Deletes a scheduled meeting
                                new RemoverThread3(connectionSocket,inFromClient,users).start();

                        }
                }
        }
}
