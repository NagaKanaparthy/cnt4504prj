import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class client {
  private static String hostName;
  private static int mode;
  private static int numClients;
  private static int port;
  //intialization functions
  public static void handleArgs(String[] args){
    if(args.length > 0){
      if(args.length == 3){
        if(Integer.parseInt(args[1]) == 2){
          mode = 2;
        }
        else if (Integer.parseInt(args[1]) == 3){
          mode = 3;
        }
        numClients = Integer.parseInt(args[2]);
      }
      else{
        mode = 1;
      }
    }
    else{
      mode = 1;
    }
    hostName = getHostname(args);
  }
  public static String getHostname(String[] cmdline){
    String hostName = "192.168.100.124";
    port = 3515;
    if(cmdline.length > 0) {
        return cmdline[0];
    } else {
        /*
        System.out.println("No Port in commandline, bye.");
        return;
        */
        System.out.println("Choose and option\n1. Exit\n2. Use Default hostname");
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

  public static void main(String[] args) throws IOException {
    handleArgs(args);
    switch(mode){
      case 1:
        handleServer(new Socket(hostName,3515));
        break;
      default:
        handleMulti();
        break;
    }
  }
  //handling functions
  public static void handleServer(Socket clientSocket) throws IOException{
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
                    else if(serverResponse.equals("Select Menu Option")) {
                        userResponse = userInput.readLine();
                        if (userResponse != null) {
                            out.println(userResponse);
                        }
                    }
                    else{
                      if (serverResponse != null) {
                          System.out.println(serverResponse);
                      }
                    }
                }
                //printResponse
            }
        } catch (IOException e) {
            System.err.println("Fail: "+e.toString());
        }
    }
    System.out.println("Goodbye - Session Complete");
    clientSocket.close();
  }
  public static void handleMulti(){
    switch(mode){
      case 2:
        //do light load
        /*
        try{
          LiteThread[] threads = new LiteThread[numClients];
          for(int i = 0; i < numClients; i++){
            threads[i] = new LiteThread(new Socket(hostName,port),i,latch);
            threads[i].join();
          }
          System.out.println("Starting Light Test");
          System.out.println("id,time\n");
          for(int i = 0; i < numClients; i++){
            threads[i].start();
          }
          latch.await();
          for(int i = 0; i < numClients; i++){
            System.out.println(threads[i].toString());
          }
          System.out.println("Done");
        } catch(Exception e){}
          */
        break;
      case 3:
        //do heavy load
        CountDownLatch latch = new CountDownLatch(numClients);
        try{
          HeavyThread[] threads = new HeavyThread[numClients];
          for(int i = 0; i < numClients; i++){
            threads[i] = new HeavyThread(new Socket(hostName,port),latch);
            threads[i].join();
          }
          System.out.println("Starting Heavy Test");
          System.out.println("id,time");
          for(int i = 0; i < numClients; i++){
            threads[i].start();
          }
          latch.await();
          for(int i = 0; i < numClients; i++){
            System.out.println(threads[i].toString());
          }
          System.out.println("Done");
        } catch(Exception e){}
        break;
    }
  }
}
class HeavyThread extends Thread{
  private Socket socket = null;
  private Test result;
  private CountDownLatch latch;
  HeavyThread(Socket client, CountDownLatch latch){
    this.socket = client;
    this.latch = latch;
  }
  public String toString(){
    return result.toString();
  }
  @Override
  public void run(){
    try{
      this.performLoad(this.socket);
      this.latch.countDown();

    }catch(Exception e){

    }
  }
  public void performLoad(Socket clientSocket) throws IOException{
    if(clientSocket != null) {
        try(
                //Attempt to create the reciving and outputing communications
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            ) {
            //Create response Variables
            String serverResponse;
            this.result = new Test(System.currentTimeMillis());
            while (!clientSocket.isClosed()) {
              //start Timer
                if((serverResponse = in.readLine()) != null) {
                    //reads user's input and forwards it to the Server
                    if(serverResponse.equals("Select Menu Option")) {
                        out.println("4");
                        while(true){
                          if((serverResponse = in.readLine()) != null){
                            if(serverResponse.equals("Select Menu Option")){
                              this.result.timeEndMillis = System.currentTimeMillis();
                              out.println("7");
                            }
                            do{
                              serverResponse = in.readLine();
                              System.out.println(serverResponse);
                            }while(!serverResponse.equals("Exit"));
                            clientSocket.close();
                          }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
  }
}
class Test{
  public long timeStartMillis;
  public long timeEndMillis;
  Test(long start){
    this.timeStartMillis = start;
  }
  public void setEnd(long end){
    this.timeEndMillis = end;
  }
  public String toString(){
    return Long.toString(timeEndMillis - timeStartMillis);
  }
  public static void appendToFile(int id, Test val){
    try{
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("t",true)));
      out.println(id+","+val.toString());
    } catch (Exception e) {
      System.out.println("NOOO");
    }
  }
}
