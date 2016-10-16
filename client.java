/*Author:   Nagvarun Kanaparthyi
    Program:  Client program that will do the specfications required by
              project 1. It basically connects to a server and displays response
              to user.
*/
import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) throws IOException {
        //default hostname
        String hostName = getHostname(args);
        int port = 3515;
        Socket clientSocket = new Socket(hostName, port);
        //Attempt To open communications between the server and client
        if(clientSocket != null) {
            try(
                    //Attempt to create the reciving and outputing communications
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                ) {
                //if Successful start reading user's input via a buffer stream
                BufferedReader userInput =
                    new BufferedReader(new InputStreamReader(System.in));
                //Create response Variables
                String serverResponse;
                String userResponse;
                while (!clientSocket.isClosed()) {
                    if((serverResponse = in.readLine()) != null) {
                        //	System.out.println(serverResponse);
                        //the program to exit uses the string "Exit\n"
                        if (serverResponse.equals("Exit")) {
                            System.out.println("Goodbye Program Exiting\n");
                            break;
                        }
                        //reads user's input and forwards it to the Server
                        if(serverResponse.equals("Select Menu Option")) {
                            userResponse = userInput.readLine();
                            if (userResponse != null) {
                                out.println(userResponse);
                            }
                        }
                    } else
                        System.out.println("Null");
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        System.out.println("Goodbye - Session Complete");
        clientSocket.close();
    }
    public static String getHostname(String[] cmdline){
      String hostName = "192.168.100.124";
      if(cmdline.length > 0) {
          return cmdline[0];
      } else {
          /*
          System.out.println("No Port in commandline, bye.");
          return;
          */
          System.out.println("No Port in commandline Choose and option\n1. Exit\n2. Use Default hostname");
          BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
          try {
              int temp = Integer.parseInt(buffer.readLine());
              switch(temp) {
              case 1:
                  System.out.println("Bye");
                  System.exit(1);
              case 2:
                  System.out.println("Using default hostname: " + hostName);
                  return hostName;
              default:
                  System.out.println("Invaild Selection. Exiting Now.");
                  System.exit(1);
              }
          } catch(Exception e) {
              System.out.println("Error with value read, exiting now");
              System.exit(1);
          }
          return null;
      }
    }
}
