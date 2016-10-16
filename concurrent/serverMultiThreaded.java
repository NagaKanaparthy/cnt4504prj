/*Author:   Nagvarun Kanaparthy
    Program:  Server program that will do the specfications required by
              project 1. It basically listens for clients and spawns threads to
              handle many clients. The program has a 2 classes. one class for
              main and other is the thread class to do the functions required by
              the specfications.
*/
import java.net.*;
import java.io.*;
/*
  Class: serverMultiThreaded
  Purpose: to run the program and handle Socket Connection.s
*/
public class serverMultiThreaded {
    public static void main(String[] args) throws IOException {
        //This is my portNumber
        int portNumber = 3515;
        System.out.println("Server Started Up");
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                new multiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
/*
  Class: projectOneMultiServerThread
  Purpose: this is the thread class that will handle each client
*/
class multiServerThread extends Thread {
    //global variables
    private Socket socket = null;
    //constructor
    public projectOneMultiServerThread(Socket socket) {
        this.socket = socket;
    }
    /*
      Method Name: run
      Purpose: This will handle a client when connected and responds to the
        client.
      Parameters: NONE
      Return: NONE
      Notes: this method is ran when class is intialized.
    */
   @Override
    public void run(){
        //Variables
        PrintWriter out;
        BufferedReader in;
        String inputLine;
        String menu = "1. Host current Date and Time\n2. Host uptime\n3. Host memory use\n4. Host Netstat\n5. Host current users\n6. Host running processes\n7. Quit";
        //attempt to set up input and output streams
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
            //return message to client and print
            System.out.println("Client connected on port: " + socket.getPort());
            out.println("Connection Accepted");
            //loop
            while(true) {
                //print out menu
                out.println(CommandCaller.menu);
                //let client know server is done messaging
                out.println("Select Menu Option");
                //wait for response
                inputLine = in.readLine();
                //respond with correct response
                out.println(CommandCaller.respondData(inputLine));
                //checkes if cmd was the exit Option
                if(Integer.parseInt(inputLine) == 7) {
                    break;
                }
                //end of response fix
                out.println("---Response-Compelete---");
            }
            socket.close();
            System.out.println("Client on port: " + socket.getPort()+" Exited");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
