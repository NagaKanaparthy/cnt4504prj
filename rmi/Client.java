import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client{
    public static void main(String[] args){
      String hostName = getHostname(args);
      if(args[1] != null && args[2] != null)
        handleMulti(hostName,args[1], args[2]);
      else
        System.out.println("Need more info");
    }
    public static String getHostname(String[] cmdline){
      String hostName = "192.168.100.124";
      if(cmdline.length > 0) {
          return cmdline[0];
      } else {
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
        return hostName;
      }
  }
  public static void handleMulti(String host, String option, String clients){
    int numClients = Integer.parseInt(clients);
    ProcessThread[] threads = new ProcessThread[numClients];
    try{
      for(int i = 0; i < numClients; i++){
        threads[i] = new ProcessThread(host,option);
        threads[i].join();
      }
      System.out.println("Starting "+option+" Test");
      System.out.println("id,time");
      for(int i = 0; i < numClients; i++){
        threads[i].start();
      }
      for(int i = 0; i < numClients; i++){
        System.out.println(i+","+threads[i].toString());
      }
      System.out.println("Done");
    } catch(Exception e){
      System.out.println("ERROR");
    }
  }
}
class ProcessThread extends Thread{
  private String host;
  private Test result;
  private String option = "";
  ProcessThread(String host, String option){
    this.host = host;
    this.option = option;
  }
  public String toString(){
    return result.toString();
  }
  @Override
  public void run(){
    try {
        Registry registry = LocateRegistry.getRegistry(this.host);
        Commander stub = (Commander) registry.lookup("Commander");
        this.result = new Test(System.currentTimeMillis());
        switch (this.option) {
          case "1":
            stub.getDate();
            break;
          case "2":
            stub.getUptime();
            break;
          case "3":
            stub.getMemory();
            break;
          case "4":
            stub.getNetstat();
            break;
          case "5":
            stub.getWho();
            break;
          case "6":
            stub.getProcess();
            break;
          default:
            System.out.println("Error");
            break;
        }
        this.result.timeEndMillis = System.currentTimeMillis();
    } catch (Exception e) {
        System.err.println("Client exception: " + e.toString());
        e.printStackTrace();
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
