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
        String hostName = "139.62.210.150";
        //defaults if args not there
        if(args.length != 0 && args[0] != null){
          hostName = args[0];
        }
        else{
            /*
            System.out.println("No Port in commandline, bye.");
            return;
            */
            System.out.println("No Port in commandline Choose and option\n1. Exit\n2. Use Default hostname");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            try{
                int temp = Integer.parseInt(buffer.readLine());
                switch(temp){
                    case 1:
                        System.out.println("Bye");
                        return;
                    case 2:
                        System.out.println("Using default hostname: " + hostName);
                        break;
                    default:
                        System.out.println("Invaild Selection. Exiting Now.");
                        return;
                }
            }
            catch(Exception e){
                System.out.println("Error with value read, exiting now");
                return;
            }
        }
        int port = 3515;
        Socket clientSocket = new Socket(hostName, port);
        //Attempt To open communications between the server and client
        if(clientSocket != null){
        try(
            //Attempt to create the reciving and outputing communications
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ){
            //if Successful start reading user's input via a buffer stream
            BufferedReader userInput =
                new BufferedReader(new InputStreamReader(System.in));
            //Create response Variables
            String serverResponse;
            String userResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);
                //the program to exit uses the string "Exit\n"
                if (serverResponse.equals("Exit")){
                    System.out.println("Goodbye Program Exiting\n");
                    break;
                }
                //reads user's input and forwards it to the Server
                if(serverResponse.equals("Select Menu Option")){
                    userResponse = userInput.readLine();
                    if (userResponse != null) {
                        out.println(userResponse);
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    clientSocket.close();
    }
}
